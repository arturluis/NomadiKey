package lockscreen.u2d.model;

import java.io.Serializable;

/**
 * Created by Luiz on 12/06/2015.
 */
public class OneStroke implements Comparable, Serializable{

    private Direction d;
    private int key;

    public OneStroke(Direction d, int key){
        this.d = d;
        this.key = key;
    }

    @Override
    public int compareTo(Object another) {

        OneStroke other = (OneStroke) another;

        if (this.d == other.d && this.key == other.key){
            return 0;
        }else{
            return -1;
        }

    }

    @Override
    public String toString() {
        return key + d.toString() + "\n";
    }
}