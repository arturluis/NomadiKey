package lockscreen.u2d.controller;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import lockscreen.u2d.model.Direction;
import lockscreen.u2d.model.MyConstants;

/**
 * Created by Luiz on 14/05/2015.
 */
public class Utils {

    private final static String TAG = "Utils";

    public static Direction calculateDirection(float x1, float y1, float x2, float y2, boolean useThreshold, boolean useFourDirections){
        Direction direction = null;

        double dist;
        float deltaX = x2 - x1;
        float deltaY = y2 - y1;

        double angle =  Math.atan2(deltaY, deltaX) * 180 / Math.PI;
        dist = calcEuclideanDistance(x1, y1, x2, y2);
        Log.d("Utils.CalcDir", "Distance: " + dist);

        if(useThreshold && dist < MyConstants.threshold) {
            direction = Direction.NONE;
        }
        if(useFourDirections){
            return getDirectionFromAngle_4(angle);
        }else {
            return getDirectionFromAngle_8(angle);
        }
    }

    private static Direction getDirectionFromAngle_8(double angle){
        Direction direction = null;

        if (angle >= -22.5 && angle <= 22.5) {
            direction = Direction.E;
        } else if (angle >= 22.5 && angle <= 67.5) {
            direction = Direction.SE;
        } else if (angle >= 67.5 && angle <= 112.5) {
            direction = Direction.S;
        } else if (angle >= 112.5 && angle <= 157.5) {
            direction = Direction.SW;
        } else if (angle >= -179.999 && angle <= -157.5 || angle >= 157.5 && angle <= 179.99 || angle == 180.0 || angle == -180.0) {
            direction = Direction.W;
        } else if (angle >= -157.5 && angle <= -112.5) {
            direction = Direction.NW;
        } else if (angle >= -122.5 && angle <= -67.5) {
            direction = Direction.N;
        } else if (angle >= -67.5 && angle <= -22.5) {
            direction = Direction.NE;
        }

        return direction;

    }

    private static Direction getDirectionFromAngle_4(double angle){
        Direction direction = null;

        if (angle >= -45.0 && angle <= 45.0) {
            direction = Direction.E;
        } else if (angle >= 45.0 && angle <= 135.5) {
            direction = Direction.S;
        } else if (angle >= -179.999 && angle <= -135.0 || angle >= 135.0 && angle <= 179.99 || angle == 180.0 || angle == -180.0) {
            direction = Direction.W;
        } else if (angle >= -135.0 && angle <= -45.0) {
            direction = Direction.N;
        }

        return direction;

    }

    public static Direction calculateDirection4(float x1, float y1, float x2, float y2, boolean useThreshold){
        Direction direction = null;

        double dist;
        float deltaX = x2 - x1;
        float deltaY = y2 - y1;

        double angle =  Math.atan2(deltaY, deltaX) * 180 / Math.PI;
        dist = calcEuclideanDistance(x1, y1, x2, y2);
        Log.d("Utils.CalcDir", "Distance: " + dist);


        // 0,0 is top-left
        if(dist > MyConstants.threshold) {
            if (angle >= -22.5 && angle <= 22.5) {
                direction = Direction.E;
            } else if (angle >= 22.5 && angle <= 67.5) {
                direction = Direction.SE;
            } else if (angle >= 67.5 && angle <= 112.5) {
                direction = Direction.S;
            } else if (angle >= 112.5 && angle <= 157.5) {
                direction = Direction.SW;
            } else if (angle >= -179.999 && angle <= -157.5 || angle >= 157.5 && angle <= 179.99 || angle == 180.0 || angle == -180.0) {
                direction = Direction.W;
            } else if (angle >= -157.5 && angle <= -112.5) {
                direction = Direction.NW;
            } else if (angle >= -122.5 && angle <= -67.5) {
                direction = Direction.N;
            } else if (angle >= -67.5 && angle <= -22.5) {
                direction = Direction.NE;
            }
        }else{
            direction = Direction.NONE;
        }

        return direction;
    }

    public static double calcEuclideanDistance(float x1, float y1, float x2, float y2){

        double dist = 0.0f;

        dist = Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));

        return dist;


    }

    public static String longArrayToString(long[] data){

        StringBuffer str = new StringBuffer();

        for (int i = 0; i < data.length; ++i){

            str.append(data[i]).append(";");

        }

        return str.toString();

    }

    public static void logReactionTime(boolean success, long reactionTime[], String extra){

        boolean result;
        result = PasswordController.getInstance().writeLog(MyConstants.StrokeLockLogFile, Utils.longArrayToString(reactionTime)
                + " #" + (success? "Success" : "Denied")
                + " #" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS").format(new Date())
                + " ## " + extra);
        Log.d(TAG, "WriteLog: " + result);

    }

    public static void logForm(String extra){

        boolean result;
        result = PasswordController.getInstance().writeLog(MyConstants.StrokeLockLogFile,
                "form #" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS").format(new Date())
                + " ## " + extra);
        Log.d(TAG, "WriteLog: " + result);

    }
}
