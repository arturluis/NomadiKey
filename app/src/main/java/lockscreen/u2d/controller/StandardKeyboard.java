package lockscreen.u2d.controller;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import lockscreen.R;

public class StandardKeyboard extends ActionBarActivity {

    private static float x1;
    private static float y1;
    private static float x2;
    private static float y2;

    private long reactionTime[];
    private final String TAG = StandardKeyboard.class.getSimpleName();
    private int keyIndex;
    private int[] typedPasswd;

    View.OnClickListener ocl = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

//            reactionTime[0] = System.currentTimeMillis() - ((MyButton) findViewById(R.id.b1)).getTime();
//
//            Direction d = Utils.calculateDirection(StandardKeyboard.x1, StandardKeyboard.y1, StandardKeyboard.x2, StandardKeyboard.y2);
//            int key;
//            String text = ((Button) view).getText().toString();
//            if(text.equals("*")){
//                key = 10;
//            }else  if(text.equals("#")){
//                key = 11;
//            }else{
//                key = Integer.parseInt(text);
//            }
//
//            typedPasswd[keyIndex] = key;
//
//            ++keyIndex;
//
//            Password passwd = new Password(typedPasswd);
//
//            Password old = PasswordController.getInstance().getStdPassword();
//            if(old.compareTo(passwd) == 0){
//                Log.w(TAG, "Unlocking device - " + d.toString());
//                Utils.logReactionTime(true, reactionTime, "std " + PasswordController.getInstance().getStdPassword().toString());
//                finish();
//            }else {
//                keyIndex = 0;
//                Log.d(TAG, "Stroke is incorrect. Try again.");
//                Utils.logReactionTime(false, reactionTime, "std " + PasswordController.getInstance().getStdPassword().toString());
//                Toast.makeText(StandardKeyboard.this, R.string.incorrectStroke, Toast.LENGTH_SHORT).show();
//            }

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
