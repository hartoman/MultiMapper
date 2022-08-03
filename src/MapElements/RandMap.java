/*
Made by Christos Chartomatsidis, 2022
This application is free to use, but it comes as-is:
I hold no responsibility for any damage or loss of that may arise from it's use.
Attribution is not required, but would be greatly appreciated.
For any comments, bug-reports, and ideas do not hesitate to contact me at:
hartoman@gmail.com

 */
package MapElements;

import java.awt.Color;
import java.lang.Math;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.Hashtable;

/**
 *
 * @author chris
 */
public abstract class RandMap {

    protected MapTile[][] maptile;
    protected Hashtable<Integer, Color> colorScheme = new Hashtable<>();

    // maxDim = maximum dimensions of the map, distortion = deviation from angle
    public RandMap(int maxDim, int distortion, int sizeInPixels, int maxElevation, int maxPeaks) {

        this.maptile = new MapTile[maxDim][maxDim];

        // sets the colorscheme hashtable
        setColorScheme();
        
        // creates the map grid
        createGrid(maxDim,distortion,sizeInPixels);

        // sets the starting location for the recursion that sets the elevation
        setSeed(maxDim, maxElevation, maxPeaks);
    }

    
    
    
    
    //returns the map of this object
    public MapTile[][] getMap() {
        return this.maptile;
    }

    //returns the colorscheme
    public Hashtable<Integer, Color> getColorScheme() {
        return this.colorScheme;
    }

    // method for exporting the jpg/png without empty borders
    public int getSmallerDim() {
        int last = maptile.length - 1;
        return Math.min(maptile[last][last].getXs()[2], maptile[last][last].getYs()[2]);
    }

    private void createGrid(int maxDim, int distortion, int sizeInPixels){
        int averageSide = (int) (sizeInPixels / maxDim);

        // initialize maptiles
        for (int row = 0; row < maxDim; row++) {
            for (int col = 0; col < maxDim; col++) {
                maptile[row][col] = new MapTile();
            }
        }

        // initialize noise based on input distortion
        int noise = 0;
        if (distortion != 0) {
            Randomizer r = new Randomizer();
            noise = distortion;
        }

        // initialize corner
        maptile[0][0].setPointX(0, 0);
        maptile[0][0].setPointX(1, averageSide + noise);
        maptile[0][0].setPointX(2, averageSide - noise);
        maptile[0][0].setPointX(3, 0);

        maptile[0][0].setPointY(0, 0);
        maptile[0][0].setPointY(1, 0);
        maptile[0][0].setPointY(2, averageSide + noise);
        maptile[0][0].setPointY(3, averageSide - noise);

        // loop first row
        for (int col = 1; col < maptile[0].length; col++) {

            noise = noise * (-1);

            maptile[0][col].setPointX(0, maptile[0][col - 1].getXs()[1]);
            maptile[0][col].setPointX(1, maptile[0][col].getXs()[0] + averageSide + noise);
            maptile[0][col].setPointX(2, maptile[0][col].getXs()[1] - noise);
            maptile[0][col].setPointX(3, maptile[0][col - 1].getXs()[2]);

            maptile[0][col].setPointY(0, 0);
            maptile[0][col].setPointY(1, 0);
            maptile[0][col].setPointY(2, maptile[0][col - 1].getYs()[2] - noise);//
            maptile[0][col].setPointY(3, maptile[0][col - 1].getYs()[2]);

        }

        //loop first column
        for (int row = 1; row < maptile.length; row++) {

            noise = noise * (-1);

            maptile[row][0].setPointX(0, 0);
            maptile[row][0].setPointX(1, maptile[row - 1][0].getXs()[2]);
            maptile[row][0].setPointX(2, maptile[row][0].getXs()[1] + noise);
            maptile[row][0].setPointX(3, 0);

            maptile[row][0].setPointY(0, maptile[row - 1][0].getYs()[3]);
            maptile[row][0].setPointY(1, maptile[row - 1][0].getYs()[2]);
            maptile[row][0].setPointY(2, maptile[row][0].getYs()[0] + averageSide - noise);
            maptile[row][0].setPointY(3, maptile[row][0].getYs()[0] + averageSide + noise);
        }

        // third final loop
        for (int row = 1; row < maptile.length; row++) {

            for (int col = 1; col < maptile[0].length; col++) {

                noise = noise * (-1);

                maptile[row][col].setPointX(0, maptile[row - 1][col - 1].getXs()[2]);
                maptile[row][col].setPointX(1, maptile[row - 1][col].getXs()[2]);
                maptile[row][col].setPointX(2, maptile[row][col].getXs()[1] + noise);
                maptile[row][col].setPointX(3, maptile[row][col - 1].getXs()[2]);

                maptile[row][col].setPointY(0, maptile[row - 1][col - 1].getYs()[2]);
                maptile[row][col].setPointY(1, maptile[row - 1][col].getYs()[2]);
                maptile[row][col].setPointY(3, maptile[row][col - 1].getYs()[2]);
                maptile[row][col].setPointY(2, (maptile[row][col].getYs()[3] - noise));
            }
        }
    }
    
    protected abstract void setColorScheme();

    protected abstract void setSeed(int maxDim, int maxElevation, int maxPeaks);

    protected abstract void setElevation(int maxElevation, int x, int y);

}

// maps for islands
class IslandMap extends RandMap {

    public IslandMap(int maxDim, int distortion, int sizeInPixels, int maxElevation, int maxPeaks) {
        super(maxDim, distortion, sizeInPixels, maxElevation, maxPeaks);
        }
    
            // define place for each of the peaks
        @Override
        public void setSeed(int maxDim, int maxElevation, int maxPeaks) {

        for (int i = 0; i < maxPeaks; i++) {
            Randomizer r = new Randomizer();

            // acceptable random coordinates for the first tile, based on maximum elevation
            // so that there is always sea at any border tile
            int x = r.randomBetween(maxElevation + 1, maxDim - maxElevation - 1);
            int y = r.randomBetween(maxElevation + 1, maxDim - maxElevation - 1);

            // guarrantees that 1st peak will be at max elevation, while the rest will be more random
            if (i == 0) {
                setElevation(maxElevation, x, y);
            } else {
                setElevation(r.randomBetween(maxElevation / 2, maxElevation), x, y);
            }
        }
    }

    /* sets elevation for the tiles */
    public void setElevation(int maxElevation, int x, int y) {

        Randomizer r = new Randomizer();
        if (maxElevation > maptile[x][y].getElevation()) {
            maptile[x][y].setElevation(maxElevation);
        }

        if (maxElevation > 0) {
            boolean axis = r.randomBool();
            // expands on the horizontal-vertical axis
            if (axis == true) {
                setElevation((maxElevation - r.randomBetween(1, 2)), x + 1, y);
                setElevation((maxElevation - r.randomBetween(1, 2)), x - 1, y);
                setElevation((maxElevation - r.randomBetween(1, 2)), x, y + 1);
                setElevation((maxElevation - r.randomBetween(1, 2)), x, y - 1);
            } else {
                //expands on diagonal
                setElevation((maxElevation - r.randomBetween(1, 2)), x + 1, y + 1);
                setElevation((maxElevation - r.randomBetween(1, 2)), x - 1, y - 1);
                setElevation((maxElevation - r.randomBetween(1, 2)), x - 1, y + 1);
                setElevation((maxElevation - r.randomBetween(1, 2)), x + 1, y - 1);
            }
        }
    }
    
    
    // sets the color scheme of the map
    protected void setColorScheme(){
        
        colorScheme.put(0, new Color(0, 102, 204, 255));
        colorScheme.put(1, new Color(245, 239, 180));
        colorScheme.put(2, new Color(152, 133, 88));
        colorScheme.put(3, new Color(102, 204, 100));
        colorScheme.put(4, new Color(34, 139, 34));
        colorScheme.put(5, new Color(0, 100, 0));
        colorScheme.put(6, new Color(153, 153, 153));
        colorScheme.put(7, new Color(204, 204, 204));
    };
    
    /*
        // sets the color scheme of the map
    protected void setColorScheme(){
        
        colorScheme.put(0, Color.red);
        colorScheme.put(1, Color.WHITE);
        colorScheme.put(2, Color.LIGHT_GRAY);
        colorScheme.put(3, Color.darkGray);
        colorScheme.put(4, Color.black);
        colorScheme.put(5, Color.pink);
        colorScheme.put(6, Color.BLUE);
        colorScheme.put(7, Color.YELLOW);
    };
    
    */
    
}
