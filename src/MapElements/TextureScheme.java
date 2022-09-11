/*
 * Made by Christos Chartomatsidis, 2022
 * This application is free to use, but it comes as-is:
 * I hold no responsibility for any damage or loss of that may arise from it's use.
 * Attribution is not required, but would be greatly appreciated.
 * For any comments, bug-reports, and ideas do not hesitate to contact me at:
 * hartoman@gmail.com
 */
package MapElements;
import java.util.HashMap;

/**
 *
 * @author chris
 */
    public abstract class TextureScheme extends HashMap<Integer, String>{
        TextureScheme(){
            put(8, "trans.png");
        }
    }
    
//*             LANDSCAPE       MAPS       *//

    class IslandScheme extends TextureScheme{
        IslandScheme(){
       put(0, "water.jpg");
        put(1, "light sand.jpg");
        put(2, "dark sand.jpg");
        put(3, "light jungle.jpg");
        put(4, "medium jungle.jpg");
        put(5, "dark jungle.jpg");
        put(6, "dark rock.jpg");
        put(7, "light rock.jpg");
        }
    }
    
    class WastelandScheme extends TextureScheme{
        WastelandScheme(){
                put(0, "wBasic.jpg");
                put(1, "wPlot.jpg");
                put(2, "light rock2.jpg");
                put(5, "wRoad.jpg");
                put(3, "light sand2.jpg");
                put(4, "dark sand.jpg");
                put(6, "dark rock.jpg");
                put(7, "uBasic.jpg");
        }
    }
    
    class SpaceScheme extends TextureScheme{
        SpaceScheme(){
                put(0, "sBasic.jpg");
                put(4, "s1.jpg");
                put(1, "s2.jpg");
                put(2, "s3.jpg");
                put(3, "s4.jpg");
                put(5, "s5.jpg");
                put(6, "s6.jpg");
                put(7, "s7.jpg");
        }
    }



//*             SETTLEMENT      MAPS       *//

abstract class TownTextureScheme extends TextureScheme{
    String road,water,plot,building;
    
    public String getRoad(){return road;}
    public String getWater(){return water;}
    public String getPlot(){return plot;}
    public String getBuilding(){return building;}
    
}

// post-apocalyptic color scheme
class PostApocScheme extends TownTextureScheme{
     
     PostApocScheme(){
         super();
         road = "wRoad.jpg";
         water = "wWater.jpg";
         plot = "wBasic.jpg";
         building = "wBuild.jpg";

                put(0, "wBasic.jpg");
                put(4, "wWater.jpg");
                put(1, "wBasic.jpg");
                put(2, "wBuild.jpg");
                put(3, "wRoad.jpg");
                put(5, "wCastle.jpg");
                put(6, "wShrub1.jpg");
                put(7, "wShrub2.jpg");
     }
                     
 }

// dark urban color scheme
class DarkUrbanScheme extends TownTextureScheme{
     
     DarkUrbanScheme(){
         super();
                road = "uRoad.jpg";
                water = "uWater.jpg";
                plot = "dark rock.jpg";
                building = "uBuild.jpg";

                put(0, "uBasic.jpg");
                put(4, "uWater.jpg");
                put(1, "dark rock.jpg");
                put(2, "uBuild.jpg");
                put(3, "uRoad.jpg");
                put(5, "uCastle.jpg");
                put(6, "dark jungle.jpg");
                put(7, "wWater.jpg");
     }
                     
 }

// fantasy town color scheme
class FantasyTownScheme extends TownTextureScheme{
     
     FantasyTownScheme(){
         super();

// for convenience, the more important tiles are marked here
                road = "dark sand.jpg";
                water = "water.jpg";
                plot = "light sand.jpg";
                building = "wood2.jpg";

// basic land tile
                put(0, "light jungle.jpg");
// water tiles
                put(4, "water.jpg");
// the basic stuff of the map
                //empty plot
                put(1, "light sand.jpg");
                //buildings
                put(2, "wood2.jpg");
                // roads
                put(3, "dark sand.jpg");
// castle walls
                put(5, "castle.jpg");
// for the random schrubbery        
                put(6, "shrub1.jpg");
                put(7, "shrub2.jpg");
     }
                     
 }

