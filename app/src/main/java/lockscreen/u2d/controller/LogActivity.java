package lockscreen.u2d.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import lockscreen.R;
import lockscreen.u2d.model.MyConstants;

public class LogActivity extends ActionBarActivity {

    TextView label;
    TextView log;
    Button b1, b2;

    String logType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        label = (TextView) findViewById(R.id.logLabelTextView);
        log = (TextView) findViewById(R.id.logTextView);

//        logType = (String) getIntent().getExtras().get("flag");
//
//        if(logType.equals(MyConstants.pin)){
//            // Load PIN data
//            label.setText(R.string.tvPinReactionLog);
//            log.setText(readLog(MyConstants.StrokeLockLogFile));
//
//
//        }else if (logType.equals(MyConstants.stroke)){
//            // Load Stroke data
//            label.setText(R.string.tvStrokeReactionLog);
//            log.setText(readLog(MyConstants.StrokeLockLogFile));
//        }

        label.setText("Log file");
        log.setText(readLog(MyConstants.StrokeLockLogFile));

        b1 = (Button) findViewById(R.id.bErase);
        b1.setEnabled(false);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File f = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DCIM);
                File logFile = null;
//                if(logType.equals(MyConstants.pin)){
//                    logFile = new File(f, MyConstants.PINLockLogFile);
//                }else if(logType.equals(MyConstants.stroke)){
//                    logFile = new File(f, MyConstants.StrokeLockLogFile);
//                }
                logFile = new File(f, MyConstants.PINLockLogFile);
                if(logFile != null) {
                    logFile.delete();
                    log.setText("");
                }

            }
        });

        b2 = (Button) findViewById(R.id.bSend);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = null;
                String type = "";

                logType = MyConstants.stroke;

                if(logType.equals(MyConstants.pin)){
                    // Load PIN data
                    file = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DCIM), MyConstants.PINLockLogFile);
                    type = "Pin Lock";


                }else if (logType.equals(MyConstants.stroke)){
                    // Load Stroke data
                    file = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DCIM), MyConstants.StrokeLockLogFile);
                    type = "Stroke Lock";
                }
                if(file != null){
                    try {
                        final Intent emailIntent = new Intent(
                                android.content.Intent.ACTION_SEND_MULTIPLE);
                        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                                new String[] {"arturluis@dcc.ufmg.br"});
                        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "LockScreenApp");
                        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Reaction time.");

                        emailIntent.setType("plain/text");
                        ArrayList<Uri> attachment = new ArrayList<Uri>();
                        attachment.add(Uri.fromFile(file));
                        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, attachment);

                        LogActivity.this.startActivity(Intent.createChooser(emailIntent,
                                "Sending email..."));

                    } catch (Throwable t) {
                        Toast.makeText(LogActivity.this,
                                "Request failed try again: " + t.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                }

                b1.setEnabled(true);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_log, menu);
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

    private String readLog(String log){

        StringBuffer buffer = new StringBuffer();
        String str;
        File f = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), log);
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            while((str = br.readLine()) != null){
                buffer.append(str).append("\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return buffer.toString();

    }
}
