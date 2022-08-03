/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MapElements;

import java.awt.Point;

/**
 *
 * @author chris
 */
public class MapTile {

    private int elevation;

    // 1 = top left
    // 2 = top right
    // 3 = bottom right
    // 4 = bottom left
    private int[] Xs = new int[4];
    private int[] Ys = new int[4];

    public MapTile() {

    }

    public void setPointX(int num, int value) {

        this.Xs[num] = value;
    }

    public void setPointY(int num, int value) {

        this.Ys[num] = value;
    }

    public int[] getXs() {
        return this.Xs;
    }

    public int[] getYs() {
        return this.Ys;
    }

    public void setElevation(int elevation) {
        this.elevation = elevation;
    }

    public int getElevation() {
        return this.elevation;
    }

    enum corners {
        topLeft(1),
        topRight(2),
        bottomLeft(3),
        bottomRight(4);

        private int num;

        corners(int numberInArray) {
            this.num = numberInArray;
        }

        public int getNum() {
            return this.num;
        }
    }
}
