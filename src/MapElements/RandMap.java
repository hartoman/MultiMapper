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
import java.util.ArrayList;

/**
 *
 * @author chris
 */
public abstract class RandMap {

    protected MapTile[][] maptile;
    protected Hashtable<Integer, Color> colorScheme = new Hashtable<>();
    protected Hashtable<Integer, String> textureScheme = new Hashtable<>();
    protected int maxDim;
    protected int sizeInPixels;
    protected int distortion;
    protected Randomizer r = new Randomizer();
    
    protected Hashtable<String, Integer> tiles;

    

    // maxDim = maximum dimensions of the map, distortion = deviation from angle
    public RandMap(int maxDim, int distortion, int sizeInPixels) {

        this.maptile = new MapTile[maxDim][maxDim];
        this.maxDim = maxDim;
        this.sizeInPixels=sizeInPixels;
        this.distortion=distortion;
        
        tiles=new Hashtable<>();

    }
    
    //returns the map of this object
    public MapTile[][] getMap() {
        return this.maptile;
    }

    //returns the colorscheme
    public Hashtable<Integer, Color> getColorScheme() {
        return this.colorScheme;
    }

    public Hashtable<Integer, String> getTextureScheme() {
        return this.textureScheme;
    }

    // method for exporting the jpg/png without empty borders
    public int getSmallerDim() {
        int last = maptile.length - 1;
        return Math.min(maptile[last][last].getXs()[2], maptile[last][last].getYs()[2]);
    }

        // convenience method to input the hashkey with a final string
    public int stringToHashKey(String texture) {
        int hashkey = 0;
        for (int key = 0; key < textureScheme.size(); key++) {
            if (textureScheme.get(key).equals(texture)) {
                hashkey = key;
            }
        }
        return hashkey;
    }
    
    protected void createGrid() {
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

    protected abstract void setTextureScheme();
    
    protected abstract void AssignElevation(int elevation, int x, int y);


}

// maps for islands
class IslandMap extends RandMap {
        protected int maxElevation;
        protected int maxPeaks;

    public IslandMap(int maxDim, int distortion, int sizeInPixels, int maxElevation, int maxPeaks) {
        super(maxDim, distortion, sizeInPixels);
        this.maxElevation=maxElevation;
        this.maxPeaks=maxPeaks;

        // sets the texture scheme hashtable
        setTextureScheme();

        // creates the map grid
        createGrid();
        setSeed();
    }
   
    // define place for each of the peaks

    public void setSeed() {

        for (int i = 0; i < maxPeaks; i++) {

            // acceptable random coordinates for the first tile, based on maximum elevation
            // so that there is always sea at any border tile
            int x = r.randomBetween(maxElevation + 1, maxDim - maxElevation - 1);
            int y = r.randomBetween(maxElevation + 1, maxDim - maxElevation - 1);

            // guarrantees that 1st peak will be at max elevation, while the rest will be more random
            if (i == 0) {
                AssignElevation(maxElevation, x, y);
            } else {
                AssignElevation(r.randomBetween(maxElevation / 2, maxElevation), x, y);
            }
        }
    }

    /* sets elevation for the tiles */
    @Override
    protected void AssignElevation(int elevation, int x, int y) {

        if (elevation > maptile[x][y].getElevation()) {
            maptile[x][y].setElevation(elevation);
        }

        if (elevation > 0) {
            boolean axis = r.randomBool();
            // expands on the horizontal-vertical axis
            if (axis == true) {
                AssignElevation((elevation - r.randomBetween(1, 2)), x + 1, y);
                AssignElevation((elevation - r.randomBetween(1, 2)), x - 1, y);
                AssignElevation((elevation - r.randomBetween(1, 2)), x, y + 1);
                AssignElevation((elevation - r.randomBetween(1, 2)), x, y - 1);
            } else {
                //expands on diagonal
                AssignElevation((elevation - r.randomBetween(1, 2)), x + 1, y + 1);
                AssignElevation((elevation - r.randomBetween(1, 2)), x - 1, y - 1);
                AssignElevation((elevation - r.randomBetween(1, 2)), x - 1, y + 1);
                AssignElevation((elevation - r.randomBetween(1, 2)), x + 1, y - 1);
            }
        }
    }

    // sets the color scheme of the map
    protected void setColorScheme() {

        colorScheme.put(0, new Color(0, 102, 204, 0));
        colorScheme.put(1, new Color(245, 239, 180));
        colorScheme.put(2, new Color(152, 133, 88));
        colorScheme.put(3, new Color(102, 204, 100));
        colorScheme.put(4, new Color(34, 139, 34));
        colorScheme.put(5, new Color(0, 100, 0));
        colorScheme.put(6, new Color(153, 153, 153));
        colorScheme.put(7, new Color(204, 204, 204));
    }

    // sets the texture scheme of the map
    protected void setTextureScheme() {

        textureScheme.put(0, "water.jpg");
        textureScheme.put(1, "light sand.jpg");
        textureScheme.put(2, "dark sand.jpg");
        textureScheme.put(3, "light jungle.jpg");
        textureScheme.put(4, "medium jungle.jpg");
        textureScheme.put(5, "dark jungle.jpg");
        textureScheme.put(6, "dark rock.jpg");
        textureScheme.put(7, "light rock.jpg");
        
        textureScheme.put(8, "trans.png");

    }
    

}
/////////////////////////////////////
// map for TOWN

class TownMap extends RandMap {

    // for ease of reference to tile types
    private final String road = "dark sand.jpg";
    private final String water = "water.jpg";
    private final String plot = "light sand.jpg";
    private final String building = "wood2.jpg";
    
    

    // for defining map limits
    private int top;
    private int left = top;
    private int bottom;
    private int right = bottom;

    // density of the buildings
    private int density;
    private int numRoads;
    private boolean sea;
    private boolean river;

    // here: distortion is always 0-3
    // maxElevation represents building density 0-100
    // numRoads is the number of horizontal roads
    public TownMap(int maxDim, int distortion, int sizeInPixels, int density , int numRoads,boolean sea,boolean river) {
        super(maxDim, distortion, sizeInPixels);

        this.density=density;
        this.numRoads=numRoads;
        this.sea=sea;
        this.river=river;

        // sets the texture scheme hashtable
        setTextureScheme();

        // creates the map grid
        createGrid();
        
        // configures the initial assisting variables
        initVariables();
        
        // creates the map    
        setSeed();
        
    }

    //sets up the initial assisting variables
    protected void initVariables(){
        
        // the available area for the town limits
        top = maxDim / 5;
        left = top;
        bottom = maxDim * 4 / 5;
        right = bottom;

        // so as to avoid constant recalculation of the most used strings
        tiles.put(road, stringToHashKey(road));
        tiles.put(water, stringToHashKey(water));
        tiles.put(plot, stringToHashKey(plot));
        tiles.put(building, stringToHashKey(building));
    };
    

    
    public void setSeed() {

        // creates a river if the input boolean is true
        createRiver(river);

        // lays down the first main horizontal road
        int mid = (right-left)/2;
        roadHoriz(bottom, r.randomBetween(left,mid-1), r.randomBetween(mid+1,right));

        // every town has at least one road
        int roadCount = 1;

        int startPointX = 0;
        int startPointY = 0;
        // maxPeaks here has the meaning of maximum number of horizontal roads. we always have at least one road
        for (int i = bottom - 3; i > top; i = i - 3) {

            //spawns points for the following roads
            int x = r.randomBetween(left + 1, right - 1);
            maptile[i][x].setElevation(tiles.get(road));

            // draws horizontal road based on this point
            roadHoriz(i, r.randomBetween(left, x), x);
            roadHoriz(i, x, r.randomBetween(x, right));

            // draws vertical roads. on the road going downwards care is taken so as to connect all roads
            roadVert(x, r.randomBetween(top, i), i);
            roadVert(x, i, minToConnect(x, i, bottom));

            // how many roads we have. we always have at least 1
            roadCount++;
            if (roadCount + 1 > numRoads) {
                startPointY = i;
                startPointX = x;
                break;

            }
        }

        // creates a river if the input boolean is true
        createSea(sea);

    }

    /* sets elevation for the tiles */
    @Override
    protected void AssignElevation(int elevation, int y, int x) {

    }

    // sets the color scheme of the map
    protected void setColorScheme() {

    }

    // sets the texture scheme of the map
    protected void setTextureScheme() {

        // most basic land tile
        textureScheme.put(0, "light jungle.jpg");
        // water tiles
        textureScheme.put(4, "water.jpg");
        // the basic stuff of the map

        //empty plot
        textureScheme.put(1, "light sand.jpg");
        //buildings
        textureScheme.put(2, "wood2.jpg");
        // roads
        textureScheme.put(3, "dark sand.jpg");

        textureScheme.put(5, "medium jungle.jpg");
        textureScheme.put(7, "dark jungle.jpg");
        textureScheme.put(6, "light rock.jpg");
        
        textureScheme.put(8, "trans.png");
    }
    
    
    // TODO: FIX MAX ELEVATION
    // TODO: FIX MAX ROADS

    // TODO: ADD OPTION FOR SEA     --on the GUI
    // TODO: ADD OPTION FOR RIVER  --on the GUI
    // TODO: ASSIGN DENSITY FACTOR  --on the GUI
    
    
    // paints the neighbours of a road tile.
    void paintNeighbors(int x, int y) {
        int value = 1;
        for (int i = x - 2; i < x + 3; i++) {
            for (int j = y - 2; j < y + 3; j++) {
                if (maptile[i][j].getElevation() == 0) {
                    // this defines ratio of builing to empty plot creation.
                    // builing is 2
                    // empty plot is 1
                    // the higher the max of randombetween, the less densely populated the city is
                    value = r.randomBetween(1, 100);
                    if (value < density) {
                        value = tiles.get(building);
                    } else {
                        value = tiles.get(plot);
                    }
                    maptile[i][j].setElevation(Math.max(maptile[i][j].getElevation(), value));

                }
            }
        }
    }

    // creates a horizontal road, on the tile with given x, by giving it's tiles the chosen texture
    void roadHoriz(int y, int left, int right) {
        Randomizer r = new Randomizer();

        for (int i = left; i < right + 1; i++) {
            maptile[y][i].setElevation(tiles.get(road));
            //paints the neighbors of this road tile
            paintNeighbors(y, i);
        }
    }

    // creates a vertical road, on the tile with given x, by giving it's tiles the chosen texture
    void roadVert(int x, int top, int bottom) {

        for (int i = top; i < bottom + 1; i++) {
            maptile[i][x].setElevation(tiles.get(road));
            //paints the neighbors of this road tile
            paintNeighbors(i, x);
        }
    }

    // returns the minimum line length that is needed to connect a horizontal road A
    // with the next road beneath B
    // x and y are coordinates of a junction point of road A
    int minToConnect(int x, int y, int bottom) {
        
        // search for a road to connect to
        for (int i = y + 1; i < bottom; i++) {
            if (maptile[i][x].getElevation() == tiles.get(road)) {
                return i;
            }
        }
        // if reached bottom and no road is found yet, extend first road. the x+/-1 is added to make it look more natural than just a corner
        int mid = (right-left)/2;
        if(x<mid){
            roadHoriz(bottom,x+2,mid-1);
        }else{
            roadHoriz(bottom,mid+1,x+2);
        }
        return bottom;
    }



    // creates a river that runs through the map
    void createRiver(boolean river) {
        
        if (river){
            int beginpoint = r.randomBetween(0, maxDim - 1);
        maptile[0][beginpoint].setElevation(tiles.get(water));
        int currentX = beginpoint;
        int xWithinLimits = currentX;
        int xWithinLimits2 = currentX;

        for (int i = 1; i < maxDim; i++) {
            currentX += r.randomBetween(-1, 1);
            xWithinLimits = Math.max(0, Math.min(currentX, maxDim - 1));
            maptile[i][xWithinLimits].setElevation(tiles.get(water));

            // river has 2 squares to look nice, else it looks like a stream
            xWithinLimits2 = Math.max(0, Math.min(xWithinLimits + 1, maxDim - 1));
            maptile[i][xWithinLimits2].setElevation(tiles.get(water));
        }
        }
        
    }

    void createSea(boolean sea) {

        if (sea) {
            for (int i = bottom + 3; i < maxDim; i++) {
                for (int j = 0; j < maxDim; j++) {
                    maptile[i][j].setElevation(tiles.get(water));
                }
            }
        }

    }

}
