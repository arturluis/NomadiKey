package lockscreen.u2d.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;

import lockscreen.R;
import lockscreen.u2d.model.EnumKeyboard;
import lockscreen.u2d.model.EnumPasswordType;

public class SettingsActivity extends PreferenceActivity {

    private Handler mHandler = new Handler();
    private final String TAG = SettingsActivity.class.getSimpleName();

    private Runnable delayedActivity = new Runnable() {
        @Override
        public void run() {
            Intent i = new Intent(SettingsActivity.this, GenericLockScreenActivity.class);
            startActivity(i);
        }
    };

    private int tests = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i0 = new Intent(SettingsActivity.this, AEScreenOnOffService.class);
        startService(i0);

        Intent i1 = new Intent(SettingsActivity.this, ServerService.class);
        startService(i1);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        setupSimplePreferencesScreen();

    }

    private void setupSimplePreferencesScreen() {

        addPreferencesFromResource(R.xml.preferences_general);

        Preference p;

        // STD
        p = (Preference) findPreference("stdTest");
        p.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Intent i = new Intent(SettingsActivity.this, SettingsPasswordTestActivity.class);
                i.putExtra("keyboard", EnumKeyboard.STD);
                i.putExtra("type", EnumPasswordType.NONE);
                i.putExtra("passwds", 0);
                startActivityForResult(i, 0);

                System.out.println("Retornou");

                return true;
            }
        });


        // RANDOM
        p = (Preference) findPreference("randomTest");
        p.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Intent i = new Intent(SettingsActivity.this, SettingsPasswordTestActivity.class);
                i.putExtra("keyboard", EnumKeyboard.STD);
                i.putExtra("random", true);
                i.putExtra("type", EnumPasswordType.NONE);
                startActivityForResult(i, 0);

                return true;
            }
        });

        // NOMAD
        p = (Preference) findPreference("nomadTest");
        p.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Intent i = new Intent(SettingsActivity.this, SettingsPasswordTestActivity.class);
                i.putExtra("keyboard", EnumKeyboard.NOMAD);
                i.putExtra("type", EnumPasswordType.NONE);
                startActivityForResult(i, 0);

                return true;
            }
        });

        // NOMAD++
        p = (Preference) findPreference("nomadTest2");
        p.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Intent i = new Intent(SettingsActivity.this, SettingsPasswordTestActivity.class);
                i.putExtra("keyboard", EnumKeyboard.NOMAD);
                i.putExtra("type", EnumPasswordType.SHOULDER);
                startActivityForResult(i, 0);

                return true;
            }
        });

//        p = (Preference) findPreference("nomadColorTest");
//        p.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            @Override
//            public boolean onPreferenceClick(Preference preference) {
//
//                Intent i = new Intent(SettingsActivity.this, SettingsPasswordTestActivity.class);
//                i.putExtra("keyboard", EnumKeyboard.NOMAD);
//                i.putExtra("type", EnumPasswordType.COLOR);
//                startActivity(i);
//
//                return true;
//            }
//        });
//
//        p = (Preference) findPreference("nomadStrokeTest");
//        p.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            @Override
//            public boolean onPreferenceClick(Preference preference) {
//
//                Intent i = new Intent(SettingsActivity.this, SettingsPasswordTestActivity.class);
//                i.putExtra("keyboard", EnumKeyboard.NOMAD);
//                i.putExtra("type", EnumPasswordType.STROKE);
//                startActivity(i);
//
//                return true;
//            }
//        });
//
//        p = (Preference) findPreference("nomadColorStrokeTest");
//        p.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            @Override
//            public boolean onPreferenceClick(Preference preference) {
//
//                Intent i = new Intent(SettingsActivity.this, SettingsPasswordTestActivity.class);
//                i.putExtra("keyboard", EnumKeyboard.NOMAD);
//                i.putExtra("type", EnumPasswordType.COLOR_STROKE);
//                startActivity(i);
//
//                return true;
//            }
//        });

//        // ANSWER CALL
//        p = (Preference) findPreference("acTest0");
//        p.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            @Override
//            public boolean onPreferenceClick(Preference preference) {
//
//                Intent i = new Intent(SettingsActivity.this, SettingsPasswordTestActivity.class);
//                i.putExtra("keyboard", EnumKeyboard.ANSWER_CALL_DEFAULT);
//                i.putExtra("type", EnumPasswordType.STROKE);
//                startActivity(i);
//
//                return true;
//            }
//        });
//
//        p = (Preference) findPreference("acTest1");
//        p.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            @Override
//            public boolean onPreferenceClick(Preference preference) {
//
//                Intent i = new Intent(SettingsActivity.this, SettingsPasswordTestActivity.class);
//                i.putExtra("keyboard", EnumKeyboard.ANSWER_CALL);
//                i.putExtra("type", EnumPasswordType.STROKE);
//                startActivity(i);
//
//                return true;
//            }
//        });
//
//        p = (Preference) findPreference("acTest2");
//        p.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            @Override
//            public boolean onPreferenceClick(Preference preference) {
//
//                Intent i = new Intent(SettingsActivity.this, SettingsPasswordTestActivity.class);
//                i.putExtra("keyboard", EnumKeyboard.ANSWER_CALL);
//                i.putExtra("type", EnumPasswordType.STROKE);
//                startActivity(i);
//
//                return true;
//            }
//        });

        // FORM
        p = (Preference) findPreference("openQuestion");
        p.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Intent i = new Intent(SettingsActivity.this, QuestionsActivity.class);
                startActivityForResult(i, 0);

                return true;
            }
        });

        // LOG
        p = (Preference) findPreference("viewLog");
        p.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Intent i = new Intent(SettingsActivity.this, LogActivity.class);
                startActivity(i);
                tests = 0;

                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        PasswordController.getInstance().loadPasswords();

//        ++tests;
        System.out.println("Tests: " + tests);

        switch (6){
            case 0:
                ((Preference) findPreference("stdTest")).setEnabled(true);
                ((Preference) findPreference("nomadTest")).setEnabled(false);
                ((Preference) findPreference("randomTest")).setEnabled(false);
                ((Preference) findPreference("nomadTest2")).setEnabled(false);
                ((Preference) findPreference("openQuestion")).setEnabled(false);
                break;

            case 1:
                ((Preference) findPreference("stdTest")).setEnabled(false);
                ((Preference) findPreference("nomadTest")).setEnabled(true);
                ((Preference) findPreference("randomTest")).setEnabled(false);
                ((Preference) findPreference("nomadTest2")).setEnabled(false);
                ((Preference) findPreference("openQuestion")).setEnabled(false);
                break;

            case 2:
                ((Preference) findPreference("stdTest")).setEnabled(false);
                ((Preference) findPreference("nomadTest")).setEnabled(false);
                ((Preference) findPreference("randomTest")).setEnabled(true);
                ((Preference) findPreference("nomadTest2")).setEnabled(false);
                ((Preference) findPreference("openQuestion")).setEnabled(false);
                break;

            case 3:
                ((Preference) findPreference("stdTest")).setEnabled(false);
                ((Preference) findPreference("nomadTest")).setEnabled(false);
                ((Preference) findPreference("randomTest")).setEnabled(false);
                ((Preference) findPreference("nomadTest2")).setEnabled(true);
                ((Preference) findPreference("openQuestion")).setEnabled(false);
                break;

            case 4:
                ((Preference) findPreference("stdTest")).setEnabled(false);
                ((Preference) findPreference("nomadTest")).setEnabled(false);
                ((Preference) findPreference("randomTest")).setEnabled(false);
                ((Preference) findPreference("nomadTest2")).setEnabled(false);
                ((Preference) findPreference("openQuestion")).setEnabled(true);
                break;
            case 5:
                ((Preference) findPreference("stdTest")).setEnabled(false);
                ((Preference) findPreference("nomadTest")).setEnabled(false);
                ((Preference) findPreference("randomTest")).setEnabled(false);
                ((Preference) findPreference("nomadTest2")).setEnabled(false);
                ((Preference) findPreference("openQuestion")).setEnabled(false);
            case 6:
                ((Preference) findPreference("stdTest")).setEnabled(true);
                ((Preference) findPreference("nomadTest")).setEnabled(true);
                ((Preference) findPreference("randomTest")).setEnabled(true);
                ((Preference) findPreference("nomadTest2")).setEnabled(true);
                ((Preference) findPreference("openQuestion")).setEnabled(true);
                break;
        }



//        PasswordController.getInstance().loadOneStroke();

        Log.w("[SettingsActivity]", "Resuming");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0){
            if(resultCode == Activity.RESULT_OK){
                ++tests;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
