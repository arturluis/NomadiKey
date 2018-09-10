package lockscreen.u2d.controller;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import lockscreen.R;
import lockscreen.u2d.model.Direction;
import lockscreen.u2d.model.EnumKeyboard;
import lockscreen.u2d.model.EnumPasswordType;
import lockscreen.u2d.model.Password;

public class InputPasswordActivity extends ActionBarActivity {

    private final String TAG = InputPasswordActivity.class.getSimpleName();

    private Button mCancel;
    private Button mConfirm;
    private Button mErase;
    private EditText mEditText;
    private TextView mTextView;
    private TextView mPassTextView;

    private ArrayList<String> strPass;
    private ArrayList<Direction> strokes;

    private Password typedPassword;
    private boolean confirmation;
    private String pass;

    private EnumKeyboard enumKeyboard;
    private EnumPasswordType enumPasswordType;

    private float x1;
    private float y1;
    private float x2;
    private float y2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_password);

        setTitle(getResources().getString(R.string.inputPasswdTitle));

        Intent i = getIntent();

        pass = "";

        if (i.getBooleanExtra("confirmation", false)){

            Log.w(TAG, "Confirmation activity");
            confirmation = true;
            typedPassword = (Password) i.getSerializableExtra("passwd");
            System.out.println(typedPassword.toString());

            pass = (String) getIntent().getExtras().get("pass");

        }else{
            pass = PasswordGenerator.generatePassword(4, false, 1);
        }

        if (i.getExtras().get("keyboard") != null ){

            enumKeyboard = (EnumKeyboard) i.getExtras().getSerializable("keyboard");
            System.out.println(enumKeyboard.toString());

        }

        if (i.getExtras().get("type") != null ){

            enumPasswordType = (EnumPasswordType) i.getExtras().getSerializable("type");
            System.out.println(enumPasswordType.toString());

        }

        strPass = new ArrayList<>();
        strokes = new ArrayList<>();

        mPassTextView = (TextView) findViewById(R.id.passwd);
        mPassTextView.setText(pass);

        mEditText = (EditText) findViewById(R.id.editText);

        mErase = (Button) findViewById(R.id.bErase);
        mErase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mEditText.getText().length() > 0){

                    mEditText.setText(mEditText.getText().subSequence(0, mEditText.getText().length() - 1));
                    strPass.remove(strPass.size() - 1);
                    if(enumPasswordType == EnumPasswordType.STROKE || enumPasswordType == EnumPasswordType.COLOR_STROKE) {
                        strokes.remove(strokes.size() - 1);
                    }

                }
            }
        });

        mTextView = (TextView) findViewById(R.id.tvInput);

        View.OnClickListener ocl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.append(((Button)v).getText());
                strPass.add(((Button)v).getText().toString());
                Direction d = Utils.calculateDirection(x1, y1, x2, y2, false, true);
                if(enumPasswordType == EnumPasswordType.STROKE || enumPasswordType == EnumPasswordType.COLOR_STROKE){
                    Toast.makeText(InputPasswordActivity.this, ((Button)v).getText().toString() + " " + d.toString(), Toast.LENGTH_SHORT).show();
                    Log.w(TAG, ((Button)v).getText().toString() + " " +  d);
                    strokes.add(d);
                }else {
//                    Toast.makeText(InputPasswordActivity.this, ((Button) v).getText().toString(), Toast.LENGTH_SHORT).show();
                    Log.w(TAG, ((Button) v).getText().toString());
                }

            }
        };

        View.OnTouchListener otl = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i(TAG, "X: " + event.getX() + " Y: " + event.getY() + " - rX: " + event.getRawX() + " rY: " + event.getRawY() + " Orientation: " + event.getOrientation());
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    x1 = event.getRawX();
                    y1 = event.getRawY();
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    x2 = event.getRawX();
                    y2 = event.getRawY();
                    v.callOnClick();
                }


                return true;
            }
        };

        if(enumPasswordType == EnumPasswordType.STROKE || enumPasswordType == EnumPasswordType.COLOR_STROKE) {
            ((Button) findViewById(R.id.b0)).setOnTouchListener(otl);
            ((Button) findViewById(R.id.b1)).setOnTouchListener(otl);
            ((Button) findViewById(R.id.b2)).setOnTouchListener(otl);
            ((Button) findViewById(R.id.b3)).setOnTouchListener(otl);
            ((Button) findViewById(R.id.b4)).setOnTouchListener(otl);
            ((Button) findViewById(R.id.b5)).setOnTouchListener(otl);
            ((Button) findViewById(R.id.b6)).setOnTouchListener(otl);
            ((Button) findViewById(R.id.b7)).setOnTouchListener(otl);
            ((Button) findViewById(R.id.b8)).setOnTouchListener(otl);
            ((Button) findViewById(R.id.b9)).setOnTouchListener(otl);
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


        if(confirmation){
            mTextView.setText(R.string.tvConfirmPasswd);
        }

        mCancel = (Button) findViewById(R.id.bCancel);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Finish activity");
                if(confirmation){
                    if (getParent() == null) {
                        setResult(Activity.RESULT_CANCELED, null);
                    } else {
                        getParent().setResult(Activity.RESULT_CANCELED, null);
                    }
                    finish();
                }else {
                    InputPasswordActivity.this.onBackPressed();
                }

            }
        });

        mConfirm = (Button) findViewById(R.id.bConfirm);
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mEditText.getText().length() > 0) {

                    if (confirmation) {

                        Password typed = null;
                        if (isWithStroke(enumPasswordType)){
                            typed = new Password(strPass, strokes);
                        }else {
                            typed = new Password(strPass);
                        }

                        if (typedPassword.compareTo(typed) == 0) {

                            PasswordController.getInstance().setPassword(typedPassword, enumKeyboard);
                            Toast.makeText(InputPasswordActivity.this, R.string.newPasswdSet, Toast.LENGTH_SHORT).show();

                            // Store saved password into a file
                            PasswordController.getInstance().storePassword(enumKeyboard);

                            Log.v(TAG, "New password set. Destroying activity");

                            if (getParent() == null) {
                                setResult(Activity.RESULT_OK, null);
                            } else {
                                getParent().setResult(Activity.RESULT_OK, null);
                            }

                            finish();
                        } else {
                            Toast.makeText(InputPasswordActivity.this, R.string.typedNotMatchPasswd, Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Password p;
                        Intent i = new Intent(InputPasswordActivity.this, InputPasswordActivity.class);
                        i.putExtra("confirmation", true);
                        i.putExtra("keyboard", enumKeyboard);
                        p = new Password(strPass);

                        System.out.println(p.passwordToString() + " " + pass);

                        if(p.passwordToString().equals(pass)) {
                            i.putExtra("keyboard", enumKeyboard);
                            if (enumPasswordType == EnumPasswordType.COLOR_STROKE ||
                                    enumPasswordType == EnumPasswordType.STROKE) {
                                p.setStrokeDirection(strokes);
                            }
                            i.putExtra("keyboard", enumKeyboard);
                            i.putExtra("type", enumPasswordType);
                            i.putExtra("passwd", p);
                            i.putExtra("pass", pass);
                            startActivityForResult(i, 0);
                        }else{
                            Toast.makeText(InputPasswordActivity.this, R.string.typedNotMatchPasswd, Toast.LENGTH_LONG).show();
                        }
//

                    }

                }else{
                    Toast.makeText(InputPasswordActivity.this, R.string.emptyPasswd, Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private boolean isWithStroke(EnumPasswordType enumPasswordType){
        return (enumPasswordType == EnumPasswordType.STROKE || enumPasswordType == EnumPasswordType.COLOR_STROKE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0){
            if(resultCode == Activity.RESULT_OK){
                finish();
            }else{
//                Intent i2 = new Intent(InputPasswordActivity.this, InputPasswordActivity.class);
//                i2.putExtra("keyboard", (EnumKeyboard) getIntent().getExtras().get("keyboard"));
//                i2.putExtra("type", (EnumPasswordType) getIntent().getExtras().get("type"));
//                startActivity(i2);
            }
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
