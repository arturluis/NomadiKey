package lockscreen.u2d.controller;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import lockscreen.R;
import lockscreen.u2d.model.Direction;
import lockscreen.u2d.model.EnumKeyboard;
import lockscreen.u2d.model.MyButton;
import lockscreen.u2d.model.MyConstants;
import lockscreen.u2d.model.Password;


public class LockScreenActivity extends Activity {

    private final String  TAG = LockScreenActivity.class.getSimpleName();

    private HashMap<Integer, Button> buttons;
    private long timeStart, timeEnd, timeSum, timeTotal;

    private int keyIndex;
    private int[] typedPasswd;
    private Direction[] stroke;
    private long[] reactionTime;
    private long lastReactionTime;

    private EnumKeyboard typePass;

    private int mult = 20;
    private int multW = 18;

    private View v;

    private boolean active = false;
    private boolean first = true;

    private static float x1;
    private static float y1;
    private static float x2;
    private static float y2;

    private KeysPositions kp;

    View.OnTouchListener otl = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                LockScreenActivity.x1 = event.getRawX();
                LockScreenActivity.y1 = event.getRawY();
            }else if(event.getAction() == MotionEvent.ACTION_UP){
                LockScreenActivity.x2 = event.getRawX();
                LockScreenActivity.y2 = event.getRawY();
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

            Direction d = Utils.calculateDirection(LockScreenActivity.x1, LockScreenActivity.y1, LockScreenActivity.x2, LockScreenActivity.y2, false, false);

            Log.d(TAG, "Reaction time: " + lastReactionTime + " ms");
            int key;
            String text = ((Button) v).getText().toString();
            if(text.equals("*")){
                key = 10;
            }else  if(text.equals("#")){
                key = 11;
            }else{
                key = Integer.parseInt(text);
            }
            typedPasswd[keyIndex] = key;
            stroke[keyIndex] = d;

            if(keyIndex == 0 && first) {
                reactionTime[keyIndex] = timeEnd - ((MyButton) buttons.get(0)).getTime();
                first = false;
            }else{
                reactionTime[keyIndex] = lastReactionTime;
            }
            keyIndex++;


            if(keyIndex == PasswordController.getInstance().getPassword(typePass).getPasswordLength()){

                String extra;
                if(typePass == EnumKeyboard.NOMAD){
                    extra = "nomad " + PasswordController.getInstance().getPassword(typePass).toString();
                }else{
                    extra = "nomad+stroke " + PasswordController.getInstance().getPassword(typePass).toString();
                }


                if(checkPassword()){
                    active = false;
                    Log.d(TAG, "Password is correct. Unlocking device.");

                    Utils.logReactionTime(true, reactionTime, extra);
                    finish();
                }else {
                    Utils.logReactionTime(false, reactionTime, extra);
                    Log.d(TAG, "Password is incorrect. Try again.");
                    Toast.makeText(LockScreenActivity.this, R.string.incorrectPasswd, Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    private void logReactionTime(boolean success){

        boolean result;
        result = PasswordController.getInstance().writeLog(MyConstants.PINLockLogFile, Utils.longArrayToString(reactionTime) + " #" + (success? "Sucess" : "Denied") + " #"
                + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS").format(new Date()));
        Log.d(TAG, "WriteLog: " + result);

    }

    public BroadcastReceiver mybroadcast = new BroadcastReceiver() {

        //When Event is published, onReceive method is called
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            Log.i(TAG, "MyReceiver");

            if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
                Log.w(TAG, "Screen ON");
                Intent i = new Intent(LockScreenActivity.this, LockScreenActivity.class);
                context.startActivity(i);
            }
            else if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
                Log.i(TAG, "Screen OFF");
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set FULLSCREEN
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // End Set FULLSCREEN

        setContentView(R.layout.activity_lock_screen);

        Intent i = getIntent();
        if(i.getBooleanExtra("simpleLoad", false)){
            Log.i(TAG, "Moving to back");
            onStop();
        }

        typePass = (EnumKeyboard) i.getExtras().get("type");
        Password p = PasswordController.getInstance().getPassword(typePass);

        typedPasswd = new int[p.getPasswordLength()];
        stroke = new Direction[p.getPasswordLength()];
        reactionTime = new long[p.getPasswordLength()];

        initKeyboard();

    }

    private DisplayMetrics getScreenResolution(Context context)
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        return metrics;

    }

    private void initKeyboard(){

        RelativeLayout ll = (RelativeLayout) findViewById(R.id.rLayout);

        Button b;
        buttons = new HashMap<>();

        for (int i = 0; i < 12; i++){

            if(i == 0){
                b = (Button) View.inflate(this, R.layout.mybutton, null);
            }else {
                b = (Button) View.inflate(this, R.layout.button, null);
            }

            if (i >= 1 && i < 4){
                b.setBackgroundColor(getResources().getColor(R.color.red));
            }else if(i >= 4 && i < 7){
                b.setBackgroundColor(getResources().getColor(R.color.green));
            }else if(i >=7 && i < 10){
                b.setBackgroundColor(getResources().getColor(R.color.blue));
            }else{
                b.setBackgroundColor(getResources().getColor(R.color.white));
            }

            b.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            b.setOnClickListener(ocl);
            b.setOnTouchListener(otl);

            if(i == 10) {
                b.setText("*");
            }else if(i == 11) {
                b.setText("#");
            }else{
                b.setText(String.valueOf(i));
            }

            buttons.put(i, b);

            ll.addView(b);

        }

    }

    private boolean checkPassword(){

        if(keyIndex == typedPasswd.length){

            Password other = new Password(typedPasswd, stroke);
            if(PasswordController.getInstance().getPassword(typePass).compareTo(other) == 0){
                active = false;
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
        this.typedPasswd = new int[PasswordController.getInstance().getPassword(typePass).getPasswordLength()];
        this.stroke = new Direction[PasswordController.getInstance().getPassword(typePass).getPasswordLength()];
        keyIndex = 0;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        if(!active) {

            active = true;

            Button b;

            DisplayMetrics dm = getScreenResolution(this);
            kp = new KeysPositions(dm.widthPixels, dm.heightPixels, buttons.get(0).getWidth(), buttons.get(0).getHeight());
            kp.Distribute();

            for (int i = 0; i < buttons.size(); i++) {

                b = buttons.get(i);
                b.setX(kp.getKeyXPosition(i) - (buttons.get(0).getWidth() / 2));
                b.setY(kp.getKeyYPosition(i) - (buttons.get(0).getHeight() / 2));

            }

            timeStart = System.currentTimeMillis();
            timeTotal = timeStart;

        }

    }

    private Point getPositionOfKey(int key){

        Point p = new Point();

        if(key == 1){
            p.set(10 * multW, 10 * mult);
        }else  if(key == 2){
            p.set(20 * multW, 20 * mult);
        }else if(key == 3){
            p.set(30 * multW, 30 * mult);
        }else if(key == 4){
            p.set(10 * multW, 40 * mult);
        }else if(key == 5){
            p.set(20 * multW, 50 * mult);
        }else if(key == 6){
            p.set(30 * multW, 60 * mult);
        }else if(key == 7){
            p.set(10 * multW, 70 * mult);
        }else if(key == 8){
            p.set(20 * multW, 80 * mult);
        }else if(key == 0){
            p.set(30 * multW, 90 * mult);
        }else {
            p.set(50 * multW, 100 * mult);
        }

        return p;

    }

    private void printButton(Button b){

        Log.d(TAG, "Button id " + b.getId());
        Log.d(TAG, "Text: " + b.getText());
        Log.d(TAG, "Dim: " + b.getMeasuredWidth() + " x " + b.getMeasuredHeight());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause(){
        super.onPause();
        active = false;
        PasswordController.getInstance().storePassword(typePass);
    }

    @Override
    protected void onResume() {
        super.onResume();
        active = false;

        Log.w(TAG, "onResume");

        PasswordController.getInstance().loadPasswords();

        if(!PasswordController.getInstance().isPasswordSet(typePass)){
            Toast.makeText(LockScreenActivity.this, R.string.passwdNotSet, Toast.LENGTH_SHORT).show();
        }else {
            resetPasswordInput();
            Log.i(TAG, "LockScreen running now");
        }

        timeStart = System.currentTimeMillis();

    }

}
