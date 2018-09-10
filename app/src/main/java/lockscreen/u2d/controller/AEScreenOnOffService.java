package lockscreen.u2d.controller;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;


public class AEScreenOnOffService extends Service {
    BroadcastReceiver mReceiver=null;
    boolean boot = true;

    private final String TAG = AEScreenOnOffService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        // Register receiver that handles screen on and screen off logic
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new AEScreenOnOffReceiver();
        registerReceiver(mReceiver, filter);
        Log.d(TAG, "Starting service");




    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        boolean screenOn = false;

        if(!boot) {


            Log.d(TAG, "Non-boot invoke");

            try {
                // Get ON/OFF values sent from receiver ( AEScreenOnOffReceiver.java )
                screenOn = intent.getBooleanExtra("screen_state", false);

            } catch (Exception e) {
            }

//            if (PasswordController.getInstance().isPasswordSet(EnumKeyboard.NOMAD_STROKE)) {
//                if (!screenOn) {
//
//                    //Toast.makeText(getBaseContext(), "Screen on, ", Toast.LENGTH_SHORT).show();
//                    Log.d(TAG, "Screen on");
//                    Intent i = new Intent(AEScreenOnOffService.this, LockScreenActivity.class);
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    i.putExtra("type", EnumKeyboard.NOMAD_STROKE);
//                    startActivity(i);
//
//
//                } else {
//
//                    // your code here
//                    // Some time required to stop any service to save battery consumption
//                    //Toast.makeText(getBaseContext(), "Screen off,", Toast.LENGTH_SHORT).show();
//                    Log.d(TAG, "Screen off");
////                    Intent i = new Intent(AEScreenOnOffService.this, LockScreenActivity.class);
////                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                    startActivity(i);
//                }
//            }
//        } else {
//            Log.d(TAG, "Boot invoke");
//            boot = false;
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onDestroy() {

        Log.i("ScreenOnOff", "Service destroy");
        if(mReceiver!=null) {
            unregisterReceiver(mReceiver);
        }

    }
}
