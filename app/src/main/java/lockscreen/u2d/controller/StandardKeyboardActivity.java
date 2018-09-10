package lockscreen.u2d.controller;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import lockscreen.R;
import lockscreen.u2d.model.MyButton;
import lockscreen.u2d.model.Password;

public class StandardKeyboardActivity extends ActionBarActivity {

    private static float x1;
    private static float y1;
    private static float x2;
    private static float y2;

    private long reactionTime[];
    private final String TAG = StandardKeyboardActivity.class.getSimpleName();
    private int keyIndex;
    private int[] typedPasswd;
    private long current, last;
    private boolean color;

    View.OnClickListener ocl = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            last = current;
            current = System.currentTimeMillis();

            if(keyIndex == 0) {
                last = ((MyButton) findViewById(R.id.b0)).getTime();
            }
            reactionTime[keyIndex] = current - last;

            int key;
            String text = ((Button) view).getText().toString();
            if(text.equals("*")){
                key = 10;
            }else  if(text.equals("#")){
                key = 11;
            }else{
                key = Integer.parseInt(text);
            }

            typedPasswd[keyIndex] = key;

            Password old = PasswordController.getInstance().getStdPassword();

            ++keyIndex;

            if(keyIndex == old.getPasswordLength()) {

                Password passwd = new Password(typedPasswd);

                if (old.compareTo(passwd) == 0) {
                    Log.w(TAG, "Unlocking device - ");
                    Utils.logReactionTime(true, reactionTime, (color == true ? "std+color " : "std ") + PasswordController.getInstance().getStdPassword().toString());

                } else {
                    keyIndex = 0;
                    Log.d(TAG, "Stroke is incorrect. Try again.");
                    Utils.logReactionTime(false, reactionTime, (color == true? "std+color " : "std ") + PasswordController.getInstance().getStdPassword().toString());
                    Toast.makeText(StandardKeyboardActivity.this, R.string.incorrectStroke, Toast.LENGTH_SHORT).show();
                }
                finish();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.std_keyboard);

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

        reactionTime = new long[PasswordController.getInstance().getStdPassword().getPassword().length];
        typedPasswd = new int[PasswordController.getInstance().getStdPassword().getPassword().length];

        color = getIntent().getBooleanExtra("color", false);

        if(color){

            ((Button) findViewById(R.id.b1)).setBackgroundColor(Color.RED);
            ((Button) findViewById(R.id.b2)).setBackgroundColor(Color.RED);
            ((Button) findViewById(R.id.b3)).setBackgroundColor(Color.RED);
            ((Button) findViewById(R.id.b4)).setBackgroundColor(Color.GREEN);
            ((Button) findViewById(R.id.b5)).setBackgroundColor(Color.GREEN);
            ((Button) findViewById(R.id.b6)).setBackgroundColor(Color.GREEN);
            ((Button) findViewById(R.id.b7)).setBackgroundColor(Color.BLUE);
            ((Button) findViewById(R.id.b8)).setBackgroundColor(Color.BLUE);
            ((Button) findViewById(R.id.b9)).setBackgroundColor(Color.BLUE);
            ((Button) findViewById(R.id.b0)).setBackgroundColor(Color.WHITE);

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_standard_keyboard, menu);
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
}
