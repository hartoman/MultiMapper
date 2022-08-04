/*
Made by Christos Chartomatsidis, 2022
This application is free to use, but it comes as-is:
I hold no responsibility for any damage or loss of that may arise from it's use.
Attribution is not required, but would be greatly appreciated.
For any comments, bug-reports, and ideas do not hesitate to contact me at:
hartoman@gmail.com.
 */
package MapElements;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.Hashtable;

/**
 *
 * @author chris
 */
public class Colorpanel extends javax.swing.JPanel {

    private RandMap map;
    private boolean transparentGrid = false;
//    private boolean transparentBackground = false;

    public Colorpanel(RandMap map) {
        this.map=map;
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        // this is necessary to assure that the png file will give transparent background
        Color background = new Color(0, 0, 0, 0);
        this.setBackground(background);

        // without this, in every new transparent map there is nasty overlap of transparent and non-transparent cells
        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(AlphaComposite.Src);
        g2d.setColor(Color.BLUE);
        g2d.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());

        ColorizeMap(transparentGrid, map, g);

    }

    // sets a map in the Colorpanel. this method is used by the Mapper's loadMap
    public void setMap(RandMap map, boolean transGrid) {
        this.map = map;
        transparentGrid = transGrid;
     //   transparentBackground = transpBack;
        removeAll();
    }

    // colorizes the entire map
    public void ColorizeMap(boolean transparentGrid, RandMap map, Graphics g) {
        Color cellPerimeter = new Color(0f, 0f, 0f, 0f);
        Color background = new Color(0f, 0f, 0f, 0f);

        // if true, then cell perimeter is transparent, else is black
        // 0f: makes grid invisible
        // 1f: makes opaque grid
        if (transparentGrid == true) {
            cellPerimeter = new Color(0.5f, 0.5f, 0.5f, 0.5f);
        } else {
            cellPerimeter = new Color(0f, 0f, 0f, 1f);
        }
  /*      // if true the background is transparent, else color of the sea
        if (transparentBackground == true) {
            background = new Color(0, 180, 204, 0);

        } else {
            background = map.getColorScheme().get(0);
            //background = new Color(0, 102, 204, 255);
        }
        */
        MapTile[][] maptiles = map.getMap();
        Polygon[][] polys = new Polygon[maptiles.length][maptiles.length];

        repaint();

        //sets the color of the line perimeter
        g.setColor(cellPerimeter);
        for (int i = 0; i < maptiles.length; i++) {
            for (int j = 0; j < maptiles[0].length; j++) {
                // draws map grid
                polys[i][j] = new Polygon(maptiles[i][j].getXs(), maptiles[i][j].getYs(), 4);
                g.drawPolygon(polys[i][j]);
                // colorizes the map
                g.setColor(colorizeTile(maptiles[i][j].getElevation(), background));
                g.fillPolygon(polys[i][j]);
                // to return to default background color after square with higher elevation occurs
                g.setColor(cellPerimeter);
            }
        }
        g.dispose();
    }

    // sets the rules for the color of one single tile based on its elevation value
    private Color colorizeTile(int elevation, Color background) {
     //   Color color = new Color(0, 0, 0, 0);

        if (map.getColorScheme().containsKey(elevation)){
            return map.getColorScheme().get(elevation);
        }else{
            return background;
        }
        

    }

    
    
}
