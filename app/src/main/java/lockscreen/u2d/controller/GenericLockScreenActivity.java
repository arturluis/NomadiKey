package lockscreen.u2d.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Vibrator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import lockscreen.R;
import lockscreen.u2d.model.Direction;
import lockscreen.u2d.model.EnumKeyboard;
import lockscreen.u2d.model.EnumPasswordType;
import lockscreen.u2d.model.MyButton;
import lockscreen.u2d.model.Password;

public class GenericLockScreenActivity extends Activity {

    protected String  TAG = GenericLockScreenActivity.class.getSimpleName();

    protected HashMap<Integer, Button> buttons;
    private EditText passwordField;

    protected long timeStart, timeEnd, timeSum, timeTotal;

    private Button swipe;

    protected int keyIndex;
    protected int[] typedPasswd;
    protected Direction[] stroke;

    // used to store the reaction times
    protected long[] reactionTime;
    protected long lastReactionTime;

    private KeysPositions kp;

    // set the type of keyboard
    protected EnumKeyboard enumKeyboard;
    protected EnumPasswordType enumPasswordType;

    // enables/disable 'tap' when stroke is on
    protected boolean useTap = false;

    // enable 4 or 8 directions when stroke is on
    protected boolean useFourDirections = true;

    // used to correctly log the first reaction time
    protected boolean first = true;

    // used when stroke is enabled
    protected static float x1;
    protected static float y1;
    protected static float x2;
    protected static float y2;

    boolean shuffle = false;

    private int salt;
    private int salt2;
    final int START_SALT = 700;
    final int NEXT_DIGIT = 400;

    View.OnTouchListener otl = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                GenericLockScreenActivity.x1 = event.getRawX();
                GenericLockScreenActivity.y1 = event.getRawY();
            }else if(event.getAction() == MotionEvent.ACTION_UP){
                GenericLockScreenActivity.x2 = event.getRawX();
                GenericLockScreenActivity.y2 = event.getRawY();
                v.callOnClick();
            }
            return true;
        }
    };

    View.OnClickListener ocl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            timeEnd = System.currentTimeMillis();
            lastReactionTime = timeEnd - timeStart;
            timeStart = timeEnd;
            int shoulderFlag=0;

            if (enumPasswordType == EnumPasswordType.SHOULDER) shoulderFlag = 2;

            if(isWithStroke(enumPasswordType)) {
                Direction d = Utils.calculateDirection(GenericLockScreenActivity.x1, GenericLockScreenActivity.y1, GenericLockScreenActivity.x2, GenericLockScreenActivity.y2,
                        useTap, useFourDirections);

                Log.d(TAG, "Reaction time: " + lastReactionTime + " ms");
            }
            int key;
            String text = ((Button) v).getText().toString();
            key = Integer.parseInt(text);

            typedPasswd[keyIndex] = key;

            if (isWithStroke(enumPasswordType)) {
                Direction d = Utils.calculateDirection(GenericLockScreenActivity.x1, GenericLockScreenActivity.y1, GenericLockScreenActivity.x2, GenericLockScreenActivity.y2,
                        useTap, useFourDirections);
                Log.d(TAG, "Reaction time: " + lastReactionTime + " ms");
                stroke[keyIndex] = d;
            }

            if (enumKeyboard != EnumKeyboard.ANSWER_CALL) {
                String str = "";
                if (passwordField.getText() != null) {
                    str = passwordField.getText().toString();
                }
                passwordField.setText(str + key);
            }
            if (keyIndex == 0 && first) {
                reactionTime[keyIndex] = timeEnd - ((MyButton) findViewById(R.id.b0)).getTime();
                first = false;
            } else {
                reactionTime[keyIndex] = lastReactionTime;
            }
            keyIndex++;



            if (keyIndex == PasswordController.getInstance().getPassword(enumKeyboard).getPasswordLength()+shoulderFlag) {
                String extra = "";
                if (enumKeyboard == EnumKeyboard.STD) {
                    if (shuffle) {
                        extra = "random";
                    } else {
                        extra = "std";
                    }
                } else if (enumKeyboard == EnumKeyboard.NOMAD) {
                    extra = "nomad";
                } else if (enumPasswordType == EnumPasswordType.SHOULDER) {
                    extra = "nomad++";
                }
                else if (enumKeyboard == EnumKeyboard.ANSWER_CALL) {
                    extra = "answer";
                } else if (enumKeyboard == EnumKeyboard.ANSWER_CALL_DEFAULT) {
                    extra = "answer-default";
                }


                if (enumPasswordType == EnumPasswordType.COLOR) {
                    extra += "+color ";
                } else if (enumPasswordType == EnumPasswordType.STROKE) {
                    extra += "+stroke ";
                } else if (enumPasswordType == EnumPasswordType.COLOR_STROKE) {
                    extra += "+color+stroke ";
                } else {
                    extra += " ";
                }

                StringBuffer str = new StringBuffer();
                for (int i = 0; i < typedPasswd.length; ++i) {
                    str.append(typedPasswd[i]);
                    if (isWithStroke(enumPasswordType)) {
                        str.append(stroke[i]);
                    }
                    str.append(" ");
                }

                extra += PasswordController.getInstance().getPassword(enumKeyboard).toString() +
                        " passwd-typed " + str.toString();
                if (enumPasswordType == EnumPasswordType.SHOULDER)
                    extra += "salts:" + salt + " " + salt2 + "\n";

                if (enumPasswordType == EnumPasswordType.SHOULDER){
                    if (checkShoulderPassword()) {
                        Log.d(TAG, getResources().getString(R.string.passwdCorrect));
                        Utils.logReactionTime(true, reactionTime, extra);

                    } else {
                        Utils.logReactionTime(false, reactionTime, extra);
                        Log.d(TAG, getResources().getString(R.string.incorrectPasswd));
                        Toast.makeText(GenericLockScreenActivity.this, R.string.incorrectPasswd, Toast.LENGTH_SHORT).show();
                    }

                    if (getParent() == null) {
                        setResult(Activity.RESULT_OK, null);
                    } else {
                        getParent().setResult(Activity.RESULT_OK, null);
                    }
                }
                else {
                    if (checkPassword()) {
                        Log.d(TAG, getResources().getString(R.string.passwdCorrect));

                        Utils.logReactionTime(true, reactionTime, extra);

                    } else {
                        if (enumKeyboard != EnumKeyboard.ANSWER_CALL) {
                            passwordField.setText("");
                        }
                        Utils.logReactionTime(false, reactionTime, extra);
                        Log.d(TAG, getResources().getString(R.string.incorrectPasswd));
                        Toast.makeText(GenericLockScreenActivity.this, R.string.incorrectPasswd, Toast.LENGTH_SHORT).show();
                    }

                    if (getParent() == null) {
                        setResult(Activity.RESULT_OK, null);
                    } else {
                        getParent().setResult(Activity.RESULT_OK, null);
                    }
                }

                finish();

            }
        }
    };

    private boolean checkShoulderPassword(){
        int i, j;
        i = 0;
        j = 0;
        int len = PasswordController.getInstance().getPassword(enumKeyboard).getPasswordLength();
        while (i < keyIndex){
            if ((j < len)&&(typedPasswd[i] == PasswordController.getInstance().getPassword(enumKeyboard).getPassword()[j])){
                i++;
                j++;
            }
            else if (typedPasswd[i] == salt){
                i++;
            }
            else if (typedPasswd[i] == salt2){
                i++;
            }
            else{
                return false;
            }
        }
        return true;
    }

    private int findSalt(){
        int lastDigit = keyIndex-2;
        for (int i = 0; i < lastDigit; i++){
            if ((typedPasswd[i] != PasswordController.getInstance().getPassword(enumKeyboard).getPassword()[i])&&(typedPasswd[i] == salt)){
                return i;
            }
            else if ((typedPasswd[i] != PasswordController.getInstance().getPassword(enumKeyboard).getPassword()[i])&&(typedPasswd[i] != salt)){
                return -2;
            }
        }
        if (typedPasswd[lastDigit] == salt){
            return lastDigit;
        }
        return -1;
    }

    private boolean removeSalt(){
        int i = findSalt();
        if (i == -2) {
            return false;
        }
        else if (i == -1){
            Log.d(TAG, "Couldn't find salt or mistake");
            return false;
        }
        for (; i<keyIndex-1; i++){
            typedPasswd[i] = typedPasswd[i+1];
        }
        return true;
    }

    private boolean checkNomadPassword(){
        int len = PasswordController.getInstance().getPassword(enumKeyboard).getPasswordLength();
        int[] _typedPasswd = new int[len];
//        removeSalt();
        if(removeSalt()) {
            for (int i = 0; i < len; i++) {
                _typedPasswd[i] = typedPasswd[i];
            }
            Password other;

            other = new Password(_typedPasswd);

            if (PasswordController.getInstance().getPassword(enumKeyboard).compareTo(other) == 0) {
                return true;
            } else {
                Log.d(TAG, "False on compareTo");
                resetPasswordInput();
                return false;
            }
        }
        else {
            return false;
        }

    }


    private boolean checkPassword(){


        if(keyIndex == typedPasswd.length){

            Password other;

            if(isWithStroke(enumPasswordType)) {
                other = new Password(typedPasswd, stroke);
            }else{
                 other = new Password(typedPasswd);
            }

            if(PasswordController.getInstance().getPassword(enumKeyboard).compareTo(other) == 0){
                return true;
            }else{
                Log.d(TAG, "False on compareTo");
                resetPasswordInput();
                return false;
            }
        }else if (keyIndex > typedPasswd.length - 1){
            Log.e(TAG, "Something really bad has just happened");
        }

        return false;

    }

    private void resetPasswordInput(){
        Log.d(TAG, "Reseting password input");
        this.typedPasswd = new int[PasswordController.getInstance().getPassword(enumKeyboard).getPasswordLength()];
        this.stroke = new Direction[PasswordController.getInstance().getPassword(enumKeyboard).getPasswordLength()];
        keyIndex = 0;
    }
//
//    private void logReactionTime(boolean success){
//
//        boolean result;
//        result = PasswordController.getInstance().writeLog(MyConstants.PINLockLogFile, Utils.longArrayToString(reactionTime) + " #" + (success? "Sucess" : "Denied") + " #"
//                + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS").format(new Date()));
//        Log.d(TAG, "WriteLog: " + result);
//
//    }

    private DisplayMetrics getScreenResolution(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        DisplayMetrics metrics2 = new DisplayMetrics();
        display.getMetrics(metrics);
        System.out.println("AQUI" + metrics.toString());
        display.getRealMetrics(metrics2);

        return metrics;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        buttons = new HashMap<>();

        if(i.getExtras() != null){
            if(i.getExtras().get("keyboard") != null){
                enumKeyboard = (EnumKeyboard) i.getExtras().get("keyboard");
            }
        }

        if(i.getExtras() != null){
            if(i.getExtras().get("type") != null){
                enumPasswordType = (EnumPasswordType) i.getExtras().get("type");
            }
        }

        this.shuffle = getIntent().getBooleanExtra("random", false);

        switch(enumKeyboard){
            case STD:
                setContentView(R.layout.activity_standard_keyboard);
                initStdKeyboard(this.shuffle);
                ((TextView) findViewById(R.id.passwd)).setText(PasswordController.getInstance().getStdPassword().passwordToString());
                break;

            case NOMAD:
                setContentView(R.layout.activity_lock_screen);
                initNomadKeyboard();
                ((TextView) findViewById(R.id.passwd)).setText(PasswordController.getInstance().getNomadPassword().passwordToString());
                if (enumPasswordType == EnumPasswordType.SHOULDER) iteratedOOB();
                break;

            case ANSWER_CALL:
                setContentView(R.layout.activity_stroke_lock_screen);
                initStdKeyboard(false);
                break;

            case ANSWER_CALL_DEFAULT:
                setContentView(R.layout.layout_swipe);
                initSwipeKeyboard();
                break;
        }



        passwordField = (EditText) findViewById(R.id.passwd_field);
        System.out.println(passwordField);

        if(enumPasswordType == EnumPasswordType.COLOR_STROKE){
            configColors();
            configStroke();
        }else if(enumPasswordType == EnumPasswordType.COLOR){
            configColors();
        }else if (enumPasswordType == EnumPasswordType.STROKE){
            configStroke();
        }

        if(enumPasswordType == EnumPasswordType.SHOULDER){
            typedPasswd = new int[PasswordController.getInstance().getPassword(enumKeyboard).getPasswordLength()+2];
            reactionTime = new long[PasswordController.getInstance().getPassword(enumKeyboard).getPasswordLength()+2];
        }
        else if(enumKeyboard != EnumKeyboard.ANSWER_CALL_DEFAULT) {
            typedPasswd = new int[PasswordController.getInstance().getPassword(enumKeyboard).getPasswordLength()];
            reactionTime = new long[PasswordController.getInstance().getPassword(enumKeyboard).getPasswordLength()];
        }else{
            reactionTime = new long[1];
        }


        if (isWithStroke(enumPasswordType) && enumKeyboard != EnumKeyboard.ANSWER_CALL_DEFAULT) {
            stroke = new Direction[PasswordController.getInstance().getPassword(enumKeyboard).getPasswordLength()];
        }

    }

    protected void configColors(){

        ArrayList<Integer> myB = new ArrayList<>();
        myB.add(new Integer(0)); myB.add(new Integer(1)); myB.add(new Integer(2)); myB.add(new Integer(3));

        int lines = 0;
        int val;
        int pos;

        while (myB.size() > 0){

            pos = new Random().nextInt(myB.size());
            val = myB.remove(pos);

            if(val == 0){
                switch (lines){
                    case 0:
                        buttons.get(val).setBackgroundColor(Color.RED);
                        break;

                    case 1:
                        buttons.get(val).setBackgroundColor(Color.GREEN);
                        break;

                    case 2:
                        buttons.get(val).setBackgroundColor(Color.BLUE);
                        break;

                    case 3:
                        buttons.get(val).setBackgroundColor(Color.WHITE);
                        break;
                }
            }else {
                val = val * 3;
                switch (lines) {
                    case 0:
                        buttons.get(val).setBackgroundColor(Color.RED);
                        buttons.get(val - 1).setBackgroundColor(Color.RED);
                        buttons.get(val - 2).setBackgroundColor(Color.RED);
                        break;

                    case 1:
                        buttons.get(val).setBackgroundColor(Color.GREEN);
                        if (val == 0) {
                            continue;
                        }
                        buttons.get(val - 1).setBackgroundColor(Color.GREEN);
                        buttons.get(val - 2).setBackgroundColor(Color.GREEN);
                        break;

                    case 2:
                        buttons.get(val).setBackgroundColor(Color.BLUE);
                        if (val == 0) {
                            continue;
                        }
                        buttons.get(val - 1).setBackgroundColor(Color.BLUE);
                        buttons.get(val - 2).setBackgroundColor(Color.BLUE);
                        break;

                    case 3:
                        buttons.get(val).setBackgroundColor(Color.WHITE);
                        if (val == 0) {
                            continue;
                        }
                        buttons.get(val - 1).setBackgroundColor(Color.WHITE);
                        buttons.get(val - 2).setBackgroundColor(Color.WHITE);
                        break;
                }
            }

            lines++;

        }

    }

    protected void configStroke(){

        ArrayList<Button> bList = getArrayListButtons();

        for(Button b : bList){
            b.setOnTouchListener(otl);
        }

    }

    private void configOnClickListener(){
        ArrayList<Button> bList = getArrayListButtons();

        for(Button b : bList){
            b.setOnClickListener(ocl);
        }

    }

    protected ArrayList<Button> getArrayListButtons(){

        return new ArrayList<>(buttons.values());

    }

    private void initSwipeKeyboard(){
        swipe = (Button) findViewById(R.id.swipeButton);
        swipe.setOnTouchListener(otl);
        swipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timeEnd = System.currentTimeMillis();
                lastReactionTime = timeEnd - ((MyButton) swipe).getTime();
                timeStart = timeEnd;

                reactionTime[0] = lastReactionTime;

                Direction d = Utils.calculateDirection(GenericLockScreenActivity.x1, GenericLockScreenActivity.y1, GenericLockScreenActivity.x2, GenericLockScreenActivity.y2,
                        false, true);

                String extra;
                extra = "answer-default\n";

                if(d == Direction.E){
                    Utils.logReactionTime(true, reactionTime, extra);
                    finish();
                }else {
                    Utils.logReactionTime(false, reactionTime, extra);
                    Toast.makeText(GenericLockScreenActivity.this, "Incorrect direction", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    private void initStdKeyboard(boolean shuffle){

        int[] keys = new int[]{0,1,2,3,4,5,6,7,8,9};

        Log.v("initStdKeyboard", "Initializing STD keyboard. Random? " + shuffle );

        if(shuffle){
            Log.v("initStdKeyboard", "shuffling ..");
            KeysPositions.shuffleArray(keys);
            Log.v("initStdKeyboard", "shuffled");
            StringBuilder str = new StringBuilder();
            for(int i : keys){
                str.append(i).append(" ");
            }

            Log.v("initStdKeyboard", "New sequence: " + str.toString());

        }

        ((Button) findViewById(R.id.b0)).setOnClickListener(ocl);
        ((Button) findViewById(R.id.b1)).setOnClickListener(ocl);
        ((Button) findViewById(R.id.b2)).setOnClickListener(ocl);
        ((Button) findViewById(R.id.b3)).setOnClickListener(ocl);
        ((Button) findViewById(R.id.b4)).setOnClickListener(ocl);
        ((Button) findViewById(R.id.b5)).setOnClickListener(ocl);
        ((Button) findViewById(R.id.b6)).setOnClickListener(ocl);
        ((Button) findViewById(R.id.b7)).setOnClickListener(ocl);
        ((Button) findViewById(R.id.b8)).setOnClickListener(ocl);
        ((Button) findViewById(R.id.b9)).setOnClickListener(ocl);

        buttons.put(keys[0], (Button) findViewById(R.id.b0));
        ((Button) findViewById(R.id.b0)).setText(String.valueOf(keys[0]));
        buttons.put(keys[1], (Button) findViewById(R.id.b1));
        ((Button) findViewById(R.id.b1)).setText(String.valueOf(keys[1]));
        buttons.put(keys[2], (Button) findViewById(R.id.b2));
        ((Button) findViewById(R.id.b2)).setText(String.valueOf(keys[2]));
        buttons.put(keys[3], (Button) findViewById(R.id.b3));
        ((Button) findViewById(R.id.b3)).setText(String.valueOf(keys[3]));
        buttons.put(keys[4], (Button) findViewById(R.id.b4));
        ((Button) findViewById(R.id.b4)).setText(String.valueOf(keys[4]));
        buttons.put(keys[5], (Button) findViewById(R.id.b5));
        ((Button) findViewById(R.id.b5)).setText(String.valueOf(keys[5]));
        buttons.put(keys[6], (Button) findViewById(R.id.b6));
        ((Button) findViewById(R.id.b6)).setText(String.valueOf(keys[6]));
        buttons.put(keys[7], (Button) findViewById(R.id.b7));
        ((Button) findViewById(R.id.b7)).setText(String.valueOf(keys[7]));
        buttons.put(keys[8], (Button) findViewById(R.id.b8));
        ((Button) findViewById(R.id.b8)).setText(String.valueOf(keys[8]));
        buttons.put(keys[9], (Button) findViewById(R.id.b9));
        ((Button) findViewById(R.id.b9)).setText(String.valueOf(keys[9]));

    }

    private void initNomadKeyboard() {

        System.out.println("Initing keyboard ...");

        DisplayMetrics dm = getScreenResolution(this);

        RelativeLayout ll = (RelativeLayout) findViewById(R.id.rLayout);

        Button b;
        buttons = new HashMap<>();

        for (int i = 0; i < 10; i++) {

            if (i == 0) {
                b = (Button) View.inflate(this, R.layout.mybutton, null);
                b.setId(R.id.b0);
            } else {
                b = (Button) View.inflate(this, R.layout.button, null);
            }

            b.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            b.setText(String.valueOf(i));
            b.setOnClickListener(ocl);

            buttons.put(i, b);

//            ll.addView(b);

        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if(enumKeyboard == EnumKeyboard.NOMAD){
            Button b;

            DisplayMetrics dm = getScreenResolution(this);
            Log.w(TAG + "AQUI", dm.densityDpi + "dpi" );

//            kp = new KeysPositions(dm.widthPixels, dm.heightPixels - passwordField.getHeight(),
//                    buttons.get(0).getWidth(), buttons.get(0).getHeight());
            kp = new KeysPositions(dm.widthPixels, dm.heightPixels - passwordField.getHeight(),
                    ((Button) findViewById(R.id.b0)).getWidth(), ((Button) findViewById(R.id.b0)).getHeight());
            kp.Distribute();

            Log.w(TAG + "BD", ((Button) findViewById(R.id.b0)).getWidth() + " " + ((Button) findViewById(R.id.b0)).getHeight());

            buttons.put(0, (Button) findViewById(R.id.b0));
            ((Button) findViewById(R.id.b0)).setX(kp.getKeyXPosition(0) - (buttons.get(0).getWidth() / 2));
            ((Button) findViewById(R.id.b0)).setY(kp.getKeyYPosition(0) - (buttons.get(0).getHeight() / 2));
            ((Button) findViewById(R.id.b0)).setOnClickListener(ocl);

            buttons.put(1, (Button) findViewById(R.id.b1));
            ((Button) findViewById(R.id.b1)).setX(kp.getKeyXPosition(1) - (buttons.get(0).getWidth() / 2));
            ((Button) findViewById(R.id.b1)).setY(kp.getKeyYPosition(1) - (buttons.get(0).getHeight() / 2));
            ((Button) findViewById(R.id.b1)).setOnClickListener(ocl);

            buttons.put(2, (Button) findViewById(R.id.b2));
            ((Button) findViewById(R.id.b2)).setX(kp.getKeyXPosition(2) - (buttons.get(0).getWidth() / 2));
            ((Button) findViewById(R.id.b2)).setY(kp.getKeyYPosition(2) - (buttons.get(0).getHeight() / 2));
            ((Button) findViewById(R.id.b2)).setOnClickListener(ocl);

            buttons.put(3, (Button) findViewById(R.id.b3));
            ((Button) findViewById(R.id.b3)).setX(kp.getKeyXPosition(3) - (buttons.get(0).getWidth() / 2));
            ((Button) findViewById(R.id.b3)).setY(kp.getKeyYPosition(3) - (buttons.get(0).getHeight() / 2));
            ((Button) findViewById(R.id.b3)).setOnClickListener(ocl);

            buttons.put(4, (Button) findViewById(R.id.b4));
            ((Button) findViewById(R.id.b4)).setX(kp.getKeyXPosition(4) - (buttons.get(0).getWidth() / 2));
            ((Button) findViewById(R.id.b4)).setY(kp.getKeyYPosition(4) - (buttons.get(0).getHeight() / 2));
            ((Button) findViewById(R.id.b4)).setOnClickListener(ocl);

            buttons.put(5, (Button) findViewById(R.id.b5));
            ((Button) findViewById(R.id.b5)).setX(kp.getKeyXPosition(5) - (buttons.get(0).getWidth() / 2));
            ((Button) findViewById(R.id.b5)).setY(kp.getKeyYPosition(5) - (buttons.get(0).getHeight() / 2));
            ((Button) findViewById(R.id.b5)).setOnClickListener(ocl);

            buttons.put(6, (Button) findViewById(R.id.b6));
            ((Button) findViewById(R.id.b6)).setX(kp.getKeyXPosition(6) - (buttons.get(0).getWidth() / 2));
            ((Button) findViewById(R.id.b6)).setY(kp.getKeyYPosition(6) - (buttons.get(0).getHeight() / 2));
            ((Button) findViewById(R.id.b6)).setOnClickListener(ocl);

            buttons.put(7, (Button) findViewById(R.id.b7));
            ((Button) findViewById(R.id.b7)).setX(kp.getKeyXPosition(7) - (buttons.get(0).getWidth() / 2));
            ((Button) findViewById(R.id.b7)).setY(kp.getKeyYPosition(7) - (buttons.get(0).getHeight() / 2));
            ((Button) findViewById(R.id.b7)).setOnClickListener(ocl);

            buttons.put(8, (Button) findViewById(R.id.b8));
            ((Button) findViewById(R.id.b8)).setX(kp.getKeyXPosition(8) - (buttons.get(0).getWidth() / 2));
            ((Button) findViewById(R.id.b8)).setY(kp.getKeyYPosition(8) - (buttons.get(0).getHeight() / 2));
            ((Button) findViewById(R.id.b8)).setOnClickListener(ocl);

            buttons.put(9, (Button) findViewById(R.id.b9));
            ((Button) findViewById(R.id.b9)).setX(kp.getKeyXPosition(9) - (buttons.get(0).getWidth() / 2));
            ((Button) findViewById(R.id.b9)).setY(kp.getKeyYPosition(9) - (buttons.get(0).getHeight() / 2));
            ((Button) findViewById(R.id.b9)).setOnClickListener(ocl);

        }

    }

    private boolean isWithStroke(EnumPasswordType enumPasswordType){
        return (enumPasswordType == EnumPasswordType.STROKE || enumPasswordType == EnumPasswordType.COLOR_STROKE);
    }

    private void iteratedOOB(){
        salt = new Random().nextInt(10);
        salt2 = new Random().nextInt(10);

        highlightButton(R.id.b1, START_SALT, salt, 1);
        vibrateButton(START_SALT, salt, 1);
        unhighlightButton(R.id.b1, START_SALT+NEXT_DIGIT);

        highlightButton(R.id.b2, START_SALT+NEXT_DIGIT, salt, 2);
        vibrateButton(START_SALT+NEXT_DIGIT, salt, 2);
        unhighlightButton(R.id.b2, (START_SALT+NEXT_DIGIT*2));

        highlightButton(R.id.b3, (START_SALT+NEXT_DIGIT*2), salt, 3);
        vibrateButton((START_SALT+NEXT_DIGIT*2), salt, 3);
        unhighlightButton(R.id.b3, (START_SALT+NEXT_DIGIT*3));

        highlightButton(R.id.b4, (START_SALT+NEXT_DIGIT*3), salt, 4);
        vibrateButton((START_SALT+NEXT_DIGIT*3), salt, 4);
        unhighlightButton(R.id.b4, (START_SALT+NEXT_DIGIT*4-50));

        highlightButton(R.id.b5, (START_SALT+NEXT_DIGIT*4), salt, 5);
        vibrateButton((START_SALT+NEXT_DIGIT*4), salt, 5);
        unhighlightButton(R.id.b5, (START_SALT+NEXT_DIGIT*5));

        highlightButton(R.id.b6, (START_SALT+NEXT_DIGIT*5), salt, 6);
        vibrateButton((START_SALT+NEXT_DIGIT*5), salt, 6);
        unhighlightButton(R.id.b6, (START_SALT+NEXT_DIGIT*6));

        highlightButton(R.id.b7, (START_SALT+NEXT_DIGIT*6), salt, 7);
        vibrateButton((START_SALT+NEXT_DIGIT*6), salt, 7);
        unhighlightButton(R.id.b7, (START_SALT+NEXT_DIGIT*7));

        highlightButton(R.id.b8, (START_SALT+NEXT_DIGIT*7), salt, 8);
        vibrateButton((START_SALT+NEXT_DIGIT*7), salt, 8);
        unhighlightButton(R.id.b8, (START_SALT+NEXT_DIGIT*8));

        highlightButton(R.id.b9, (START_SALT+NEXT_DIGIT*8), salt, 9);
        vibrateButton((START_SALT+NEXT_DIGIT*8), salt, 9);
        unhighlightButton(R.id.b9, (START_SALT+NEXT_DIGIT*9));

        highlightButton(R.id.b0, (START_SALT+NEXT_DIGIT*9), salt, 0);
        vibrateButton((START_SALT+NEXT_DIGIT*9), salt, 0);
        unhighlightButton(R.id.b0, (START_SALT+NEXT_DIGIT*10));
    }

    private void highlightButton(final int buttonId, int waitTime, final int salt, final int btn){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                (findViewById(buttonId)).setBackgroundColor(getResources().getColor(R.color.blue));
            }
        }, waitTime);
    }

    private void vibrateButton(int waitTime, final int salt, final int btn){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if ((salt == btn)||(salt2 == btn)) {
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(200);
                }
            }
        }, waitTime+100);
    }

    private void unhighlightButton(final int buttonId, int waitTime){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                (findViewById(buttonId)).setBackgroundColor(getResources().getColor(R.color.white));
            }
        }, waitTime);
    }


}
