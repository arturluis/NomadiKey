package lockscreen.u2d.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import lockscreen.R;
import lockscreen.u2d.model.EnumKeyboard;
import lockscreen.u2d.model.EnumPasswordType;
import lockscreen.u2d.model.MyConstants;


public class SettingsPasswordTestActivity extends PreferenceActivity {

    private static final boolean ALWAYS_SIMPLE_PREFS = false;

    private int attempts;
    private int passwords;
    private Preference p;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        Intent i2 = new Intent(SettingsPasswordTestActivity.this, InputPasswordActivity.class);
        i2.putExtra("keyboard", (EnumKeyboard) getIntent().getExtras().get("keyboard"));
        i2.putExtra("type", (EnumPasswordType) getIntent().getExtras().get("type"));
        startActivity(i2);

        attempts = 0;
        passwords = getIntent().getIntExtra("passwds", 0);

        System.out.println("Passwds: " + passwords);

        setupSimplePreferencesScreen(getIntent());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);



        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void setupSimplePreferencesScreen(Intent i) {

        final Intent i0 = i;
        final EnumKeyboard enumKeyboard = (EnumKeyboard) i0.getExtras().get("keyboard");
        final EnumPasswordType enumPasswordType = (EnumPasswordType) i0.getExtras().get("type");

        System.out.println("EK: " + enumKeyboard + " ET: " + enumPasswordType);

        String name = makeName(enumKeyboard, enumPasswordType, i.getBooleanExtra("random", false));

        addPreferencesFromResource(R.xml.preferences_test);

        Preference p;

        p = findPreference("testCategory");
        p.setTitle(name);

//        p = findPreference("changePasswd");
//        p.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            @Override
//            public boolean onPreferenceClick(Preference preference) {
//                Intent i2 = new Intent(SettingsPasswordTestActivity.this, InputPasswordActivity.class);
//                i2.putExtra("keyboard", enumKeyboard);
//                i2.putExtra("type", enumPasswordType);
//
//                startActivity(i2);
//
//                return true;
//            }
//        });

//        if(enumKeyboard == EnumKeyboard.ANSWER_CALL_DEFAULT){
//            p.setEnabled(false);
//        }

        this.p = findPreference("testPasswd");

        if(enumKeyboard == EnumKeyboard.ANSWER_CALL || enumKeyboard == EnumKeyboard.ANSWER_CALL_DEFAULT) {

            this.p.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    if ((PasswordController.getInstance().getPassword(enumKeyboard) != null && enumKeyboard != EnumKeyboard.ANSWER_CALL_DEFAULT)
                            || enumKeyboard == EnumKeyboard.ANSWER_CALL_DEFAULT) {
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                Intent i = new Intent(SettingsPasswordTestActivity.this, GenericLockScreenActivity.class);

                                if (enumKeyboard == EnumKeyboard.ANSWER_CALL_DEFAULT) {
                                    i.putExtra("keyboard", EnumKeyboard.ANSWER_CALL_DEFAULT);
                                } else {
                                    i.putExtra("keyboard", EnumKeyboard.ANSWER_CALL);
                                }
                                i.putExtra("type", EnumPasswordType.STROKE);
                                startActivityForResult(i, 0);
                            }
                        }, 5000);
                    } else {
                        Toast.makeText(SettingsPasswordTestActivity.this, R.string.createPasswdFirst, Toast.LENGTH_SHORT).show();
                    }

                    return true;
                }
            });

        }else{

            this.p.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    if(PasswordController.getInstance().getPassword(enumKeyboard) != null) {
                        Intent i2 = new Intent(SettingsPasswordTestActivity.this, GenericLockScreenActivity.class);
                        i2.putExtra("keyboard", enumKeyboard);
                        i2.putExtra("type", enumPasswordType);
                        i2.putExtra("random", getIntent().getBooleanExtra("random", false));

                        startActivityForResult(i2, 0);
                    }else{
                        Toast.makeText(SettingsPasswordTestActivity.this, R.string.createPasswdFirst, Toast.LENGTH_SHORT).show();
                    }

                    return true;
                }
            });

        }

    }

    private String makeName(EnumKeyboard enumKeyboard, EnumPasswordType enumPasswordType, boolean shuffle){

        StringBuffer str = new StringBuffer();

        switch (enumKeyboard){
            case STD:
                if(shuffle){
                    str.append("Teclado aleatório");
                }else {
                    str.append("Teclado padrão");
                }
                break;

            case NOMAD:
                str.append("Teclanômade");
                break;

            case ANSWER_CALL:
                str.append("Answer call");
                break;

            case ANSWER_CALL_DEFAULT:
                str.append("Answer call - Default");
                break;

        }

        switch (enumPasswordType){
            case NONE:
                break;

            case COLOR:
                str.append(" with color");
                break;

            case STROKE:
                str.append(" with stroke");
                break;

            case COLOR_STROKE:
                str.append(" with color and stroke");
                break;

        }

        return str.toString();

    }

    private Runnable delayedActivity = new Runnable() {
        @Override
        public void run() {
            Intent i = new Intent(SettingsPasswordTestActivity.this, GenericLockScreenActivity.class);
            i.putExtra("keyboard", EnumKeyboard.ANSWER_CALL);
            i.putExtra("type", EnumPasswordType.STROKE);
            startActivity(i);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        p.setTitle("Teste " + (attempts + 1) + " de " + MyConstants.MAX_ATTEMPTS);

//        ++attempts;
//        if(attempts < MyConstants.MAX_ATTEMPTS) {
//
//            p.setTitle("Teste " + (attempts + 1) + " de " + MyConstants.MAX_ATTEMPTS);
//
//
//        }else{
//            ++passwords;
//            if(passwords < MyConstants.MAX_PASSWORDS){
//
//                Intent i2 = new Intent(SettingsPasswordTestActivity.this, SettingsPasswordTestActivity.class);
//                i2.putExtra("keyboard", (EnumKeyboard) getIntent().getExtras().get("keyboard"));
//                i2.putExtra("type", (EnumPasswordType) getIntent().getExtras().get("type"));
//                i2.putExtra("random", getIntent().getBooleanExtra("random", false));
//                i2.putExtra("passwds", passwords);
//
//                startActivity(i2);
//
//            }
//            finish();
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0){
            if(resultCode == Activity.RESULT_OK){
                ++attempts;
                if(attempts < MyConstants.MAX_ATTEMPTS) {

                    p.setTitle("Teste " + (attempts + 1) + " de " + MyConstants.MAX_ATTEMPTS);


                }else{
                    ++passwords;
                    if(passwords < MyConstants.MAX_PASSWORDS){

                        Intent i2 = new Intent(SettingsPasswordTestActivity.this, SettingsPasswordTestActivity.class);
                        i2.putExtra("keyboard", (EnumKeyboard) getIntent().getExtras().get("keyboard"));
                        i2.putExtra("type", (EnumPasswordType) getIntent().getExtras().get("type"));
                        i2.putExtra("random", getIntent().getBooleanExtra("random", false));
                        i2.putExtra("passwds", passwords);

                        startActivity(i2);

                    }

                    if (getParent() == null) {
                        setResult(Activity.RESULT_OK, null);
                    } else {
                        getParent().setResult(Activity.RESULT_OK, null);
                    }

                    finish();

                }
            }
        }

    }



    @Override
    public void onBackPressed() {

    }
}
