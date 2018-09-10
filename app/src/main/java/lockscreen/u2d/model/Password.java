package lockscreen.u2d.model;

import android.text.Editable;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Luiz on 24/04/2015.
 */
public class Password implements Serializable, Comparable{

    private String TAG = Password.class.getSimpleName();

    private int passwordLength;
    private int[] password;
    private Direction[] strokeDirection;
    private boolean hasStroke;

    public Password(int passLen, int[] password){

        this.passwordLength = passLen;
        password = Arrays.copyOf(password, passwordLength);

    }

    public Password(Editable passwd){

        this.password = new int[passwd.length()];
        this.strokeDirection = new Direction[passwd.length()];
        this.passwordLength = passwd.length();

        Log.d(TAG, "Argument Password: " + passwd);
        Log.d(TAG, "Argument Password length: " + passwd);
        Log.d(TAG, "Object Password length: " + passwd);

        for (int i = 0; i < this.passwordLength; i++){

            this.password[i] = Integer.parseInt(passwd.charAt(i) + "");

        }

        Log.i(TAG, "New password object");

    }

    public Password(int[] passwd){

        this.hasStroke = false;

        this.password = Arrays.copyOf(passwd, passwd.length);
        this.passwordLength = passwd.length;

        this.hasStroke = false;

        Log.d(TAG, "Argument Password: " + passwd);
        Log.d(TAG, "Argument Password length: " + passwd.length);

        Log.i(TAG, "New password object");

    }

    public Password(ArrayList<String> passwd, ArrayList<Direction> stroke){

        this.hasStroke = true;

        int size = passwd.size();
        this.passwordLength = size;

        password = new int[size];
        strokeDirection = new Direction[size];

        for (int i = 0; i < size; i++){
            if(passwd.get(i).equals("*")){
                password[i] = 10;
            }else if(passwd.get(i).equals("#")){
                password[i] = 11;
            }else {
                password[i] = Integer.parseInt(passwd.get(i));
            }
            strokeDirection[i] = stroke.get(i);
        }

    }

    public Password(ArrayList<String> passwd){

        this.hasStroke = false;

        int size = passwd.size();
        this.passwordLength = size;

        password = new int[size];

        for (int i = 0; i < size; i++){
            if(passwd.get(i).equals("*")){
                password[i] = 10;
            }else if(passwd.get(i).equals("#")){
                password[i] = 11;
            }else {
                password[i] = Integer.parseInt(passwd.get(i));
            }
        }

        this.hasStroke = false;

    }

    public Password(int[] passwd, Direction[] stroke){

        int size = passwd.length;
        this.passwordLength = size;

        password = new int[size];
        strokeDirection = new Direction[size];

        for (int i = 0; i < size; i++){
            password[i] = passwd[i];
            strokeDirection[i] = stroke[i];
        }

        this.hasStroke = true;

    }

    @Override
    public String toString(){

        StringBuilder str = new StringBuilder();
        str.append("Password length: ").append(this.passwordLength);
        str.append(", Password: ");
        for (int i = 0; i < passwordLength; i++){
            if(hasStroke) {
                str.append(this.password[i] + this.strokeDirection[i].toString() + " ");
            }else{
                str.append(this.password[i] + " ");
            }
        }
        //str.append("\n");

        return str.toString();
    }

    public String passwordToString(){

        StringBuilder str = new StringBuilder();

        for (int i = 0; i < passwordLength; i++){
            if(hasStroke) {
                str.append(this.password[i] + this.strokeDirection[i].toString() + " ");
            }else{
                str.append(this.password[i] + " ");
            }
        }

        return str.toString();

    }

    public int getPasswordLength() {
        return passwordLength;
    }

    public void setPasswordLength(int passwordLength) {
        this.passwordLength = passwordLength;
    }

    public int[] getPassword() {
        return password;
    }

    public void setPassword(int[] password) {
        this.password = password;
    }

    public void setStrokeDirection(Direction[] stroke){
        this.strokeDirection = Arrays.copyOf(stroke, stroke.length);
    }

    public void setStrokeDirection(ArrayList<Direction> strokes){

        Direction[] newStrokes = new Direction[strokes.size()];

        for(int i = 0; i < strokes.size(); ++i) {
            newStrokes[i] = strokes.get(i);
        }

        this.strokeDirection = newStrokes;

        this.hasStroke = true;
    }

    @Override
    public int compareTo(Object another) {

        Password other = (Password) another;

        Log.d(TAG, another.toString());

        if(this.getPasswordLength() != other.getPasswordLength()){
            Log.d(TAG, "Sizes are different");
            return -1;
        }else{

            for (int i = 0; i < this.getPasswordLength(); i++){
                if(this.hasStroke) {
                    if (this.password[i] != other.password[i] || this.strokeDirection[i] != other.strokeDirection[i]) {
                        Log.d(TAG, "this[" + i + "]: " + this.password[i] + " other[" + i + "]: " + other.password[i] +
                                " this[" + i + "]: " + this.strokeDirection[i] + " other[" + i + "]: " + other.strokeDirection[i]);
                        return -1;
                    }
                }else{
                    if (this.password[i] != other.password[i]) {
                        Log.d(TAG, "this[" + i + "]: " + this.password[i] + " other[" + i + "]: " + other.password[i]);
                        return -1;
                    }
                }
            }

            return 0;

        }

    }
}
