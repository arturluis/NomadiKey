package lockscreen.u2d.controller;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import lockscreen.R;

public class QuestionsActivity extends ActionBarActivity {

    RadioButton sex;
    RadioButton age;
    RadioButton unlock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form);

        ((Button) findViewById(R.id.bCancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ((Button) findViewById(R.id.bConfirm)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder answers = new StringBuilder();
                RadioGroup rg;

                boolean result = true;

                rg = (RadioGroup) findViewById(R.id.gender);
                if (rg.getCheckedRadioButtonId() != -1) {
                    answers.append("gender=")
                            .append(
                                    ((RadioButton) findViewById(rg.getCheckedRadioButtonId())).getText().toString());
                } else {
                    result = false;
                }

                rg = (RadioGroup) findViewById(R.id.age);
                if (rg.getCheckedRadioButtonId() != -1) {
                    answers.append(";age=")
                            .append(
                                    ((RadioButton) findViewById(rg.getCheckedRadioButtonId())).getText().toString());
                } else {
                    result = false;
                }

                rg = (RadioGroup) findViewById(R.id.unlock);
                if (rg.getCheckedRadioButtonId() != -1) {
                    answers.append(";unlockMethod=")
                            .append(
                                    ((RadioButton) findViewById(rg.getCheckedRadioButtonId())).getText().toString());
                } else {
                    result = false;
                }

//                answers.append(";usability-standard=").append(((TextView)findViewById(R.id.ustdVal)).getText().toString());
//                answers.append(";usability-random=").append(((TextView)findViewById(R.id.urandVal)).getText().toString());
//                answers.append(";usability-nomadikey=").append(((TextView)findViewById(R.id.unkVal)).getText().toString());
//                answers.append(";security-standard=").append(((TextView)findViewById(R.id.sstdVal)).getText().toString());
//                answers.append(";security-random=").append(((TextView)findViewById(R.id.srandVal)).getText().toString());
//                answers.append(";security-nomadikey=").append(((TextView)findViewById(R.id.snkVal)).getText().toString());


//                rg = (RadioGroup) findViewById(R.id.woulduse);
//                if (rg.getCheckedRadioButtonId() != -1) {
//                    answers.append(";woulduse=")
//                            .append(
//                                    ((RadioButton) findViewById(rg.getCheckedRadioButtonId())).getText().toString());
//                } else {
//                    result = false;
//                }


                if (result) {

                    System.out.println("FormLog: " + answers.toString());
                    Utils.logForm(answers.toString() + "\n");

                    if (getParent() == null) {
                        setResult(Activity.RESULT_OK, null);
                    } else {
                        getParent().setResult(Activity.RESULT_OK, null);
                    }

                    finish();

                } else {

                    Toast.makeText(QuestionsActivity.this, "Por favor, responda à todas as perguntas", Toast.LENGTH_SHORT).show();

                }

            }
        });

////        ((SeekBar) findViewById(R.id.ustd)).setMax(9);
////        ((SeekBar) findViewById(R.id.ustd)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
////            @Override
////            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
////                ((TextView) findViewById(R.id.ustdVal)).setText(String.valueOf(progress + 1));
////            }
////
////            @Override
////            public void onStartTrackingTouch(SeekBar seekBar) {
////
////            }
////
////            @Override
////            public void onStopTrackingTouch(SeekBar seekBar) {
////
////            }
////        });
////
////        ((SeekBar) findViewById(R.id.urand)).setMax(9);
////        ((SeekBar) findViewById(R.id.urand)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
////            @Override
////            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
////                ((TextView) findViewById(R.id.urandVal)).setText(String.valueOf(progress + 1));
////            }
////
////            @Override
////            public void onStartTrackingTouch(SeekBar seekBar) {
////
////            }
////
////            @Override
////            public void onStopTrackingTouch(SeekBar seekBar) {
////
////            }
////        });
////
////        ((SeekBar) findViewById(R.id.unk)).setMax(9);
////        ((SeekBar) findViewById(R.id.unk)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
////            @Override
////            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
////                ((TextView) findViewById(R.id.unkVal)).setText(String.valueOf(progress + 1));
////            }
////
////            @Override
////            public void onStartTrackingTouch(SeekBar seekBar) {
////
////            }
////
////            @Override
////            public void onStopTrackingTouch(SeekBar seekBar) {
////
////            }
////        });
////
////        ((SeekBar) findViewById(R.id.sstd)).setMax(9);
////        ((SeekBar) findViewById(R.id.sstd)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
////            @Override
////            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
////                ((TextView) findViewById(R.id.sstdVal)).setText(String.valueOf(progress + 1));
////            }
////
////            @Override
////            public void onStartTrackingTouch(SeekBar seekBar) {
////
////            }
////
////            @Override
////            public void onStopTrackingTouch(SeekBar seekBar) {
////
////            }
////        });
////
////        ((SeekBar) findViewById(R.id.srand)).setMax(9);
////        ((SeekBar) findViewById(R.id.srand)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
////            @Override
////            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
////                ((TextView) findViewById(R.id.srandVal)).setText(String.valueOf(progress + 1));
////            }
////
////            @Override
////            public void onStartTrackingTouch(SeekBar seekBar) {
////
////            }
////
////            @Override
////            public void onStopTrackingTouch(SeekBar seekBar) {
////
////            }
////        });
////
////        ((SeekBar) findViewById(R.id.snk)).setMax(9);
////        ((SeekBar) findViewById(R.id.snk)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
////            @Override
////            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
////                ((TextView) findViewById(R.id.snkVal)).setText(String.valueOf(progress + 1));
////            }
////
////            @Override
////            public void onStartTrackingTouch(SeekBar seekBar) {
////
////            }
////
////            @Override
////            public void onStopTrackingTouch(SeekBar seekBar) {
////
////            }
//        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_questions, menu);
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
