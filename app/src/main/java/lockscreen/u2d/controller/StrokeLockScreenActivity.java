package lockscreen.u2d.controller;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Date;

import lockscreen.R;
import lockscreen.u2d.model.MyConstants;

public class StrokeLockScreenActivity extends Activity{


    private final String TAG = StrokeLockScreenActivity.class.getSimpleName();
    private View view;

    private static float x1;
    private static float y1;
    private static float x2;
    private static float y2;

    private long timeStart, timeEnd, timeTotal;
    private long[] reactionTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_stroke_lock_screen);

        View.OnTouchListener otl = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    StrokeLockScreenActivity.x1 = event.getRawX();
                    StrokeLockScreenActivity.y1 = event.getRawY();
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    StrokeLockScreenActivity.x2 = event.getRawX();
                    StrokeLockScreenActivity.y2 = event.getRawY();
                    v.callOnClick();
                }
                return true;
            }
        };

        View.OnClickListener ocl = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                reactionTime[0] = System.currentTimeMillis() - ((MyButton) findViewById(R.id.b1)).getTime();
//
//                Direction d = Utils.calculateDirection(StrokeLockScreenActivity.x1, StrokeLockScreenActivity.y1, StrokeLockScreenActivity.x2, StrokeLockScreenActivity.y2, false, false);
//                int key;
//                String text = ((Button) view).getText().toString();
//                if(text.equals("*")){
//                    key = 10;
//                }else  if(text.equals("#")){
//                    key = 11;
//                }else{
//                    key = Integer.parseInt(text);
//                }
//
//                OneStroke os = new OneStroke(d, key);
//
//                String extra = "onestroke " + PasswordController.getInstance().getPassword(EnumKeyboard.ANSWER_CALL).toString();
//
//                OneStroke old = PasswordController.getInstance().getOneStroke();
//                if(old.compareTo(os) == 0){
//                    Log.w(TAG, "Unlocking device - " + d.toString());
//                    Utils.logReactionTime(true, reactionTime, extra);
//                    finish();
//                }else {
//                    Log.d(TAG, "Stroke is incorrect. Try again.");
//                    Utils.logReactionTime(false, reactionTime, extra);
//                    Toast.makeText(StrokeLockScreenActivity.this, R.string.incorrectStroke, Toast.LENGTH_SHORT).show();
//                }

            }
        };

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

        reactionTime = new long[1];

    }

    private void logReactionTime(boolean success){

        boolean result;
        result = PasswordController.getInstance().writeLog(MyConstants.StrokeLockLogFile, Utils.longArrayToString(reactionTime) +
                " #" + (success? "Success" : "Denied") + " #"
                + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS").format(new Date()));
        Log.d(TAG, "WriteLog: " + result);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stroke_lock_screen, menu);
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
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        timeStart = System.currentTimeMillis();
    }


}
