package lockscreen.u2d.controller;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import lockscreen.u2d.model.Direction;
import lockscreen.u2d.model.EnumKeyboard;
import lockscreen.u2d.model.MyConstants;
import lockscreen.u2d.model.Password;

/**
 * Created by Luiz on 24/04/2015.
 */
public class PasswordController {

    private String TAG = PasswordController.class.getSimpleName();

    private static PasswordController passwordController;

    private Password stdPassword;
    private Password nomadPassword;
    private Password answerCall;

    public static PasswordController getInstance(){
        if(passwordController == null){
            passwordController = new PasswordController();
        }
        return passwordController;
    }

    private PasswordController(){
        super();
    }

    public Password getNomadPassword() {
        return nomadPassword;
    }

    public void setNomadPassword(Password nomadPassword) {
        Log.d(TAG, "New nomadPassword set! " + nomadPassword.toString());
        this.nomadPassword = nomadPassword;
    }

    public Password loadPassword(String file){

        Password result = null;

        try {
            File f = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM), file);
            if(f.exists()) {

                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
                result = (Password) ois.readObject();

                if (nomadPassword == null) {
                    Log.w(TAG + ".loadPassword", "nomadPassword is null");
                    return null;
                } else {
                    Log.w(TAG + ".loadPassword", "nomadPassword is " + this.nomadPassword.toString());
                }
                Log.i(TAG + ".loadPassword", "" + this.nomadPassword.toString());
                ois.close();
                return result;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean loadPassword(EnumKeyboard p){

        String file = null;
        switch (p){
            case STD:
                file = MyConstants.stdPasswdFile;
                break;

            case NOMAD:
                file = MyConstants.nomadPasswdFile;
                break;

        }
        if(file != null) {
            return (loadPassword(file) != null);
        }else{
            return false;
        }

    }

    public boolean loadPasswords(){

        Password p;
        boolean result = false;
        p = this.loadPassword(MyConstants.stdPasswdFile);
        if(p != null){
            this.setPassword(p, EnumKeyboard.STD);
            result = true;
        }
        p = this.loadPassword(MyConstants.nomadPasswdFile);
        if(p != null){
            this.setPassword(p, EnumKeyboard.NOMAD);
            result = true;
        }
        p = this.loadPassword(MyConstants.answerCallFile);
        if(p != null){
            this.setPassword(p, EnumKeyboard.ANSWER_CALL);
            result = true;
        }

        return result;
    }

    public boolean storePassword(EnumKeyboard password){

        String file = null;

        switch (password){
            case STD:
                file = MyConstants.stdPasswdFile;
                break;

            case NOMAD:
                file = MyConstants.nomadPasswdFile;
                break;

            case ANSWER_CALL:
                file = MyConstants.answerCallFile;

        }

        return storePassword(file, password);

    }

    public boolean storePassword(String file, EnumKeyboard password){

        ObjectOutputStream os;
        try {
            File f = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM), file);
            if(!f.exists()){
                f.createNewFile();
            }
            os =  new ObjectOutputStream(new FileOutputStream(f));
            os.writeObject(PasswordController.getInstance().getPassword(password));
            os.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean deletePassword(String file){

        File f = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), file);
        if(f.exists()) {
            f.delete();
        }

        return true;
    }

//    public boolean deletePassword(){
//
//        File f = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_DCIM), MyConstants.answerCallFile);
//        if(f.exists()) {
//            f.delete();
//        }
//
//        return true;
//    }

    public Password getPassword(EnumKeyboard p){
        Password pass = null;

        switch (p){
            case STD:
                pass = this.stdPassword;
                break;

            case NOMAD:
                pass = this.nomadPassword;
                break;

            case ANSWER_CALL:
                pass = this.answerCall;
                break;

        }

        return pass;

    }

//    public boolean loadOneStroke(){
//
//        try {
//            File f = new File(Environment.getExternalStoragePublicDirectory(
//                    Environment.DIRECTORY_DCIM), MyConstants.oneStrokeFile);
//            if(f.exists()) {
//                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(Environment.getExternalStoragePublicDirectory(
//                        Environment.DIRECTORY_DCIM), MyConstants.oneStrokeFile)));
//                this.stroke = (OneStroke) ois.readObject();
//                if (nomadPassword == null) {
//                    Log.w(TAG + ".loadStroke", "stroke is null");
//                    return false;
//                } else {
//                    Log.w(TAG + ".loadStroke", "stroke is " + this.stroke.toString());
//                }
//                Log.i(TAG + ".loadStroke", "" + this.stroke.toString());
//                ois.close();
//            }
//            return true;
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (StreamCorruptedException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        return true;
//
//    }

//    public boolean storeOneStroke(){
//
//        ObjectOutputStream os;
//        try {
//            File f = new File(Environment.getExternalStoragePublicDirectory(
//                    Environment.DIRECTORY_DCIM), MyConstants.oneStrokeFile);
//            if(!f.exists()){
//                f.createNewFile();
//            }
//            os =  new ObjectOutputStream(new FileOutputStream(f));
//            os.writeObject(PasswordController.getInstance().getOneStroke());
//            os.close();
//            return true;
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return true;
//    }

//    public boolean enableOneStroke(){
//
//        if(this.stroke == null){
//            Log.w(TAG, "stroke is null");
//            // show toast asking to create a stroke first
//
//            return false;
//
//        }else{
//            Log.w(TAG, "stroke is " + this.stroke.toString());
//            setOneStrokeEnabled(true);
//            // start listener if needed
//            return true;
//        }
//
//    }
//
//    public boolean disableOneStroke(){
//        this.setOneStrokeEnabled(false);
//        // stop listeners if needed
//        return true;
//    }

//    public boolean isOneStrokeSet(){
//        Log.w(TAG, "stroke is " + this.stroke + " -> " + (this.stroke != null));
//        return this.stroke != null;
//    }
//
//    public OneStroke getOneStroke() {
//        return this.stroke;
//    }

//    public void setOneStroke(OneStroke oneStroke) {
//        this.stroke = oneStroke;
//    }

//    public boolean isOneStrokeEnabled() {
//        return oneStrokeEnabled;
//    }
//
//    public void setOneStrokeEnabled(boolean oneStrokeEnabled) {
//        this.oneStrokeEnabled = oneStrokeEnabled;
//    }
//
//    public void resetStroke() {
//        this.stroke = null;
//        Log.w(TAG, "Stroke set to null. Stroke is " + this.stroke + " -> " + (this.stroke != null));
//        this.setOneStrokeEnabled(false);
//        this.deleteStroke();
//    }

    public boolean checkPassword(Password pass, int[] typedPasswd, Direction[] stroke){

        Log.d(TAG, "kI: " + pass.getPasswordLength() + " typed.length: " + typedPasswd.length);
        if(pass.getPasswordLength() == typedPasswd.length){

            Password other = new Password(typedPasswd, stroke);
            if(PasswordController.getInstance().getNomadPassword().compareTo(other) == 0){
                return true;
            }else{
                Log.d(TAG, "False on compareTo");
                return false;
            }
        }else if (pass.getPasswordLength() > typedPasswd.length){
            Log.e(TAG, "Something really bad has just happened");
        }

        return false;

    }

    public boolean writeLog(String file, String data){

        File logFile = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), file);
        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        try{
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(data);
//            buf.newLine();
            buf.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }

        return true;

    }

    public Password getStdPassword() {
        System.out.println(stdPassword);
        return stdPassword;
    }

    public void setPassword(Password passwd, EnumKeyboard enumKeyboard){

        switch (enumKeyboard){
            case STD:
                stdPassword = passwd;
                break;

            case NOMAD:
                nomadPassword = passwd;
                break;

            case ANSWER_CALL:
                answerCall = passwd;
        }

    }

    public Password getPassword(Password passwd, EnumKeyboard enumKeyboard) {

        Password result = null;

        switch (enumKeyboard) {
            case STD:
                result = stdPassword;
                break;

            case NOMAD:
                result = nomadPassword;
                break;

            case ANSWER_CALL:
                result = answerCall;
        }

        return result;

    }

//    public Password getNomadStrokePassword() {
//        return nomadStrokePassword;
//    }
//
//    public void setNomadStrokePassword(Password nomadStrokePassword) {
//        this.nomadStrokePassword = nomadStrokePassword;
//    }

    public boolean isPasswordSet(EnumKeyboard p){

        boolean result = false;

        switch (p){
            case STD:
                result = this.stdPassword != null;
                break;

            case NOMAD:
                result = this.nomadPassword != null;
                break;

            case ANSWER_CALL:
                result = this.answerCall != null;
                break;
        }

        return result;

    }

}
