package lockscreen.u2d.controller;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import lockscreen.R;
import lockscreen.u2d.model.Direction;
import lockscreen.u2d.model.OneStroke;

public class OneStrokeInputPasswordActivity extends ActionBarActivity {

    private final String TAG = OneStrokeInputPasswordActivity.class.getSimpleName();

    private float x1;
    private float y1;
    private float x2;
    private float y2;

    private Button mConfirm;
    private Button mCancel;
    private Button clickNDrag;
    private TextView text;

    private boolean confirmation = false;
    private OneStroke oldStroke;
    private int typedPasswd;

    View.OnClickListener ocl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Direction d = Utils.calculateDirection(x1, y1, x2, y2, false, false);
            String text = ((Button)v).getText().toString();

            typedPasswd = Integer.parseInt(text);

            Toast.makeText(OneStrokeInputPasswordActivity.this, "Your stroke: " + text + " " + d.toString(), Toast.LENGTH_SHORT).show();
            Log.w(TAG, ((Button) v).getText().toString() + " " + d);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_stroke_input_password);

        if(getIntent().getBooleanExtra("confirm", false)) {
            confirmation = true;
            this.oldStroke = (OneStroke) getIntent().getSerializableExtra("stroke");
            text = (TextView)findViewById(R.id.tvInput);
            text.setText(R.string.tvConfirmStroke);
        }

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

        mCancel = (Button) findViewById(R.id.bCancel);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Finish activity");
                OneStrokeInputPasswordActivity.this.onBackPressed();

            }
        });

        mConfirm = (Button) findViewById(R.id.bConfirm);
//        mConfirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Direction d = Utils.calculateDirection(x1, y1, x2, y2, false, false);
//                Password os = new Password(typedPasswd);
//
//                if(confirmation){
//
//                    System.out.println(os);
//                    System.out.println(oldStroke);
//                    if(oldStroke.compareTo(os) == 0){
//
//                        PasswordController.getInstance().setP(os);
//                        Toast.makeText(OneStrokeInputPasswordActivity.this, R.string.newStrokeSet, Toast.LENGTH_SHORT).show();
//
//                        // Store saved stroke into a file
//                        PasswordController.getInstance().storeOneStroke();
//                        Log.v(TAG, "New stroke set. Destroying activity");
//                        finish();
//
//                    }else{
//                        Toast.makeText(OneStrokeInputPasswordActivity.this, R.string.typedNotMatchPasswd, Toast.LENGTH_LONG).show();
//                    }
//
//                }else{
//                    Log.i(TAG, "I'll call the confirmation stage");
//                    Intent i = new Intent(OneStrokeInputPasswordActivity.this, OneStrokeInputPasswordActivity.class);
//                    i.putExtra("confirm", true);
//                    i.putExtra("stroke", os);
//                    startActivity(i);
//                    finish();
//                }
//
//            }
//        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_one_stroke_input_password, menu);
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
