package lockscreen.u2d.controller;

import java.util.Random;
/**
 * Created by Cotta on 17/07/2015.
 */
public class KeysPositions {


    float width;
    float height;
    float keys[][] = new float[10][2];
    float bW, bH;
    int coord[][] = new int[10][2];
    float R[][] = new float[10][4];


    public KeysPositions(float w, float h, float buttonW, float buttonH){
        width = w;
        height = h;
        bW = buttonW;
        bH = buttonH;
        int k;

        //defining relative coordinates

        //1
        coord[1][0] = 0;
        coord[1][1] = 0;
        //2
        coord[2][0] = 0;
        coord[2][1] = 1;
        //3
        coord[3][0] = 0;
        coord[3][1] = 2;
        //4
        coord[4][0] = 1;
        coord[4][1] = 0;
        //5
        coord[5][0] = 1;
        coord[5][1] = 1;
        //6
        coord[6][0] = 1;
        coord[6][1] = 2;
        //7
        coord[7][0] = 2;
        coord[7][1] = 0;
        //8
        coord[8][0] = 2;
        coord[8][1] = 1;
        //9
        coord[9][0] = 2;
        coord[9][1] = 2;
        //0
        coord[0][0] = 3;
        coord[0][1] = 1;

        //initializing minimum regions

        for(k = 0; k < 10; k++){
            //xMin
            R[k][0] = bW * coord[k][1] + bW/2;
            //xMax
            R[k][1] = width - ((2 - coord[k][1]) * bW) - bW/2;
            //yMin
            R[k][2] = bH * coord[k][0] + bH/2;
            //yMax
            R[k][3] = height - ((3 - coord[k][0]) * bH) - bH/2;

        }



    }

    public void Distribute(){

        int order[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};
        shuffleArray(order);
        int i, j, key;

        for(i = 0; i < 10; i ++){
            key = order[i];
            //position key according to region R
            keys[key][0] = randomInRange(R[key][0], R[key][1]);
            keys[key][1] = randomInRange(R[key][2], R[key][3]);

            //redefine the regions of the keys still to be placed
            for(j = 0; j < 10; j++){
                if(coord[j][1] > coord[key][1] && keys[key][0] + (coord[j][1] - coord[key][1] - 1)*bW + bW > R[j][0]) R[j][0] = (coord[j][1] - coord[key][1] - 1)*bW + keys[key][0] + bW;
                if(coord[j][1] < coord[key][1] && keys[key][0] - (coord[key][1] - coord[j][1] - 1)*bW - bW < R[j][1]) R[j][1] = keys[key][0] - (coord[key][1] - coord[j][1] - 1)*bW - bW;
                if(coord[j][0] > coord[key][0] && keys[key][1] + (coord[j][0] - coord[key][0] - 1)*bH + bH > R[j][2]) R[j][2] = (coord[j][0] - coord[key][0] - 1)*bH + keys[key][1] + bH;
                if(coord[j][0] < coord[key][0] && keys[key][1] - (coord[key][0] - coord[j][0] - 1)*bH - bH < R[j][3]) R[j][3] = keys[key][1] - (coord[key][0] - coord[j][0] - 1)*bH - bH;
            }

        }


    }

    protected static Random random = new Random();
    public float randomInRange(float min, float max) {
        float range = max - min;
        float scaled = random.nextFloat() * range;
        float shifted = scaled + min;
        return shifted;
    }

    public float getKeyXPosition(int P){
        return keys[P][0];
    }

    public float getKeyYPosition(int P){
        return keys[P][1];
    }

    public static void shuffleArray(int[] ar)
    {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }
}
