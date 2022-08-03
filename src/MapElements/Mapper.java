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
import java.awt.Graphics;
import java.awt.Component;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.lang.Math;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import static java.awt.image.ImageObserver.HEIGHT;
import java.security.CodeSource;

/**
 *
 * @author chris
 */
public class Mapper extends javax.swing.JFrame {

    /**
     * Creates new form ArrayMapScreen
     */
    private Colorpanel panel;
    private JPanel uiPane;

    private int squaresPerSide;
    private int distortion;
    private int inputPixels;
    private int maxElevationValue;
    private int numPeaksValue;

    // defines dimensions of the ui panel
    private final int uipanelWidth = 300;
    private final int uipanelHeight = 700;

    private final int panelX = 20;      // x where map appears and also distance between map and ui
    private final int panelY = 20;      // y where map and ui appear

    private RandMap map;
    boolean transparentGrid = false;
    boolean transparentBackground = false;

    ///////////////////////////////////////////////////////////////////////
    public Mapper() {
        
        setUIFont(new javax.swing.plaf.FontUIResource("Serif", Font.PLAIN, 18));

        // so that everything is autoarranged in one row, 2 columns
        //  this.setLayout(new GridLayout(1, 2));
        this.setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));

        // define map parameters
        squaresPerSide = 20;
        distortion = 5;
        maxElevationValue = 7;
        numPeaksValue = 1;

        // gets the maximum screen height   -- the*0.92 is to adjust for lost screen space on linux
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        this.inputPixels = (int) (gd.getDisplayMode().getHeight() * 0.92);

        // creates map and visualizes it through the colorpanel
        this.map = new IslandMap(squaresPerSide, distortion, inputPixels, maxElevationValue, numPeaksValue);

        // creates the map panel
        panel = new Colorpanel(map);
        panel.setLocation(panelX, panelY);
        
        panel.setSize((int) (inputPixels * 0.98), inputPixels);
        
        panel.setMinimumSize(new Dimension(inputPixels, inputPixels));
        
        add(panel);

        //creates elements of the uiPanel
        add(createUI());

        // defines dimensions of the frame
        this.setSize((panel.getSize().width + uiPane.getSize().width), Math.max((int) (inputPixels * 1.06), uipanelHeight));

        // adds menu bar to the frame        
        setJMenuBar(setupMenuBar());

    }

    public static void main(String args[]) {

    }
    ///////////////////////////////////////////////////////////////////////////////////////////////
    //  ACTION LISTENERS //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    // menuitem Save as jpg
    /*    // TODO: NEEDS A LOT OF ADJUSTMENT
        // purpose is to dynamically resize everything based on map size
        
    private void MapComponentResized(java.awt.event.ComponentEvent evt) {
            setSize((panel.getSize().width+uiPane.getSize().width), Math.max(panel.getSize().height, uipanelHeight));
    }       */
    ////////////////////////////////////////////////////////////////////////////////////////
    // sets the global font size for the application
    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }
/////////////////////////////////////////////////////////////////////////////////////////
    // creates all elements of the ui

    public JPanel createUI() {

        //sets up basic dimensions and layout
        uiPane = new JPanel();
        uiPane.setSize(uipanelWidth, uipanelHeight);
        uiPane.setPreferredSize(new Dimension(uipanelWidth, uipanelHeight));
        uiPane.setMaximumSize(new Dimension(uipanelWidth, uipanelHeight));
        uiPane.setLocation(this.getSize().width - uipanelWidth, panelY);
        //    uiPane.setLayout(new BoxLayout(uiPane, BoxLayout.Y_AXIS));

        //////////////**************//////////////
        // creates panel with the map parameters
        JPanel mapParams = new JPanel();
        mapParams.add(new javax.swing.Box.Filler(new java.awt.Dimension(1, 15), new java.awt.Dimension(1, 15), new java.awt.Dimension(1, 15)));
        mapParams.setPreferredSize(new Dimension(uiPane.getWidth() * 3 / 4, 150));
        mapParams.setLayout(new BoxLayout(mapParams, BoxLayout.PAGE_AXIS));
        mapParams.setAlignmentY(Component.TOP_ALIGNMENT);
        mapParams.setAlignmentX(Component.CENTER_ALIGNMENT);
        mapParams.add(new JLabel("Map Parameters"));
        mapParams.add(new JSeparator());
        mapParams.add(new JSeparator());
        mapParams.add(new JLabel("Number of Squares"));
        JTextField squares = new JTextField("20");
        squares.setMaximumSize(new Dimension(100, 100));
        mapParams.add(squares);
        mapParams.add(new JLabel("Distortion"));
        JTextField dist = new JTextField("5");
        dist.setMaximumSize(new Dimension(100, 100));
        mapParams.add(dist);
        uiPane.add(mapParams);

        //////////////**************//////////////
        //creates panel with elevation parameters
        JPanel elevationParams = new JPanel();
        elevationParams.add(new javax.swing.Box.Filler(new java.awt.Dimension(1, 15), new java.awt.Dimension(1, 15), new java.awt.Dimension(1, 15)));
        elevationParams.setPreferredSize(new Dimension(uiPane.getWidth() * 3 / 4, 150));
        elevationParams.setLayout(new BoxLayout(elevationParams, BoxLayout.PAGE_AXIS));
        elevationParams.setAlignmentY(Component.CENTER_ALIGNMENT);
        elevationParams.setAlignmentX(Component.CENTER_ALIGNMENT);
        elevationParams.add(new JLabel("Elevation Parameters"));
        elevationParams.add(new JSeparator());
        elevationParams.add(new JSeparator());
        elevationParams.add(new JLabel("numPeaks"));
        JTextField numPeaks = new JTextField("5");
        numPeaks.setMaximumSize(new Dimension(100, 100));
        elevationParams.add(numPeaks);
        elevationParams.add(new JLabel("maxElevation"));
        JTextField maxElevation = new JTextField("7");
        maxElevation.setMaximumSize(new Dimension(100, 100));
        elevationParams.add(maxElevation);
        uiPane.add(elevationParams);

        //////////////**************//////////////
        //creates panel to choose transparencies for grid lines and background
        JPanel transpPanel = new JPanel();
        transpPanel.add(new javax.swing.Box.Filler(new java.awt.Dimension(1, 15), new java.awt.Dimension(1, 15), new java.awt.Dimension(1, 15)));
        transpPanel.setPreferredSize(new Dimension(uiPane.getWidth() * 3 / 4, 200));
        transpPanel.setLayout(new BoxLayout(transpPanel, BoxLayout.PAGE_AXIS));
        transpPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        transpPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        transpPanel.add(new JLabel("Map Transparencies"));
        transpPanel.add(new JSeparator());
        transpPanel.add(new JSeparator());

        //creates two radiobuttons and assigns them to the same buttongroup
        transpPanel.add(new JLabel("Grid Line Color"));
        transpPanel.add(new JSeparator());
        JRadioButton line1 = new JRadioButton("Black", true);

        line1.addItemListener(new java.awt.event.ItemListener() {
            /* we go for itemEventListener because it fires only once.
            On the contrary, actionEventListener, fires every time there's a click*/
            public void itemStateChanged(java.awt.event.ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {

                    transparentGrid = false;
                    loadMap(map, transparentGrid, transparentBackground);
                }
            }
        });
        transpPanel.add(line1);

        JRadioButton line2 = new JRadioButton("Transparent", false);
        line2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    transparentGrid = true;
                    panel.removeAll();
                    panel.updateUI();
                    loadMap(map, transparentGrid, transparentBackground);
                }
            }
        });
        transpPanel.add(line2);

        ButtonGroup lines = new ButtonGroup();
        lines.add(line1);
        lines.add(line2);

        //creates two radiobuttons and assigns them to the same buttongroup
        transpPanel.add(new JLabel("Background Color"));
        transpPanel.add(new JSeparator());
        JRadioButton back1 = new JRadioButton("Blue Sea", true);
        back1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    transparentBackground = false;
                    // panel.removeAll();
                    //  panel.updateUI();
                    loadMap(map, transparentGrid, transparentBackground);
                }
            }
        });
        transpPanel.add(back1);

        JRadioButton back2 = new JRadioButton("Transparent", false);
        back2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    transparentBackground = true;
                    //    panel.removeAll();
                    //    panel.updateUI();
                    loadMap(map, transparentGrid, transparentBackground);

                }
            }
        });
        transpPanel.add(back2);

        ButtonGroup fondo = new ButtonGroup();
        fondo.add(back1);
        fondo.add(back2);

        transpPanel.add(new javax.swing.Box.Filler(new java.awt.Dimension(1, 15), new java.awt.Dimension(1, 15), new java.awt.Dimension(1, 15)));
        uiPane.add(transpPanel);

        //////////////**************//////////////
        // creates panel containing map creation button
        JPanel createPanel = new JPanel();
        createPanel.add(new javax.swing.Box.Filler(new java.awt.Dimension(1, 15), new java.awt.Dimension(1, 15), new java.awt.Dimension(1, 15)));
        createPanel.setPreferredSize(new Dimension(uiPane.getWidth() * 3 / 4, 200));
        createPanel.setLayout(new BoxLayout(createPanel, BoxLayout.X_AXIS));
        createPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        createPanel.setAlignmentX(Component.BOTTOM_ALIGNMENT);
        createPanel.add(new JSeparator());

        //  button to create new map
        JButton createMapbutton = new JButton("Create!");
        createMapbutton.setSize(uiPane.getWidth(), HEIGHT);

        createMapbutton.setMinimumSize(new Dimension(uiPane.getWidth() * 3 / 4 - 10, 100));
        createMapbutton.setPreferredSize(new Dimension(uiPane.getWidth() * 3 / 4, 100));

        createPanel.add(createMapbutton);
        createMapbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                panel.removeAll();
                panel.updateUI();

                // in case the user asks for impossible stuff, this corrects the values to the normal
                int numSquares = limitedInputRangeCorrecting(squares.getText(), 4, 100, 20);
                squares.setText(String.valueOf(numSquares));

                //     int maxDist = (inputPixels / (2 * numSquares)) - 1;
                int maxDist = (inputPixels / numSquares);
                int distortion = limitedInputRangeCorrecting(dist.getText(), 0, maxDist, 0);
                dist.setText(String.valueOf(distortion));

                int maxElevationValue = limitedInputRangeCorrecting(maxElevation.getText(), 1, 7, 7);
                maxElevation.setText(String.valueOf(maxElevationValue));

                int maxPossiblePeaks = (map.getMap().length - (2 * maxElevationValue + 2)) * (map.getMap().length - (2 * maxElevationValue + 2));
                int numPeaksValue = limitedInputRangeCorrecting(numPeaks.getText(), 1, maxPossiblePeaks, 5);
                numPeaks.setText(String.valueOf(numPeaksValue));

                map = new IslandMap(numSquares, distortion, inputPixels, maxElevationValue, numPeaksValue);
                loadMap(map, transparentGrid, transparentBackground);
            }
        });
        uiPane.add(createPanel);

        return uiPane;
    }

    private JMenuBar setupMenuBar() {

        // creates menubar
        JMenuBar menu = new JMenuBar();
        // first columne
        JMenu column1 = new JMenu("Import/Export");
        JMenuItem m10 = new JMenuItem("Load map from json");
        JMenuItem m13 = new JMenuItem("Export map as json");
        JMenuItem m11 = new JMenuItem("Save image as jpg");
        JMenuItem m12 = new JMenuItem("Save image as png (transparent background)");
        column1.add(m10);
        column1.add(m13);
        column1.add(m11);
        column1.add(m12);

        menu.add(column1);
        // second column
        JMenu column2 = new JMenu("Help");
        JMenuItem m21 = new JMenuItem("How to use");
        JMenuItem m22 = new JMenuItem("About");
        column2.add(m21);
        column2.add(m22);
        menu.add(column2);

        //assign action listeners to menuitems
        //  "Save image as jpg"
        m11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String filename = JOptionPane.showInputDialog(null, "Enter filename", "Save as jpg (blue background)", JOptionPane.INFORMATION_MESSAGE);
                String fullpath = getCurrentDirectoryPath()+filename;
                if ((filename != null) && (!filename.isEmpty())) {
                    mapToJpg(fullpath);
                }

            }
        });
        //  "Save image as png"
        m12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String filename = JOptionPane.showInputDialog(null, "Enter filename", "Save as jpg (blue background)", JOptionPane.INFORMATION_MESSAGE);
                String fullpath = getCurrentDirectoryPath()+filename;
                if ((filename != null) && (!filename.isEmpty())) {
                    mapToPng(fullpath);
                }

            }
        });

        m13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String filename = JOptionPane.showInputDialog(null, "Enter filename", "Save as jpg (blue background)", JOptionPane.INFORMATION_MESSAGE);
                String fullpath = getCurrentDirectoryPath()+filename;
                if ((filename != null) && (!filename.isEmpty())) {
                    mapToJson(fullpath);
                }

            }
        });

        m10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String filename = JOptionPane.showInputDialog(null, "Enter filename", "Save as jpg (blue background)", JOptionPane.INFORMATION_MESSAGE);
                String fullpath = getCurrentDirectoryPath()+filename;
                if ((filename != null) && (!filename.isEmpty())) {
                    mapFromJson(fullpath);
                }
            }
        });
        
        ////////////////////// column 2 ////////////////////////
        
        m21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JOptionPane.showMessageDialog(null, "Map Parameters: \n"
                        + "Number of Squares = how many squares the grid has on each side. For example, 20 means 20x20 map.\n"
                        + "Distortion = noise added to the lines of the grid. 0 means that there is no distortion.\n\n"
                        + "Elevation Parameters: \n"
                        + "Number of peaks = how many peaks is the map going to have. Each map has at least one peak.\n"
                        + "Maximum elevation = what is the maximum elevation that a peak can have.\nAt least one peak is guaranteed to have maximum elevation.\n\n"
                        + "Transparencies:\n"
                        + "Sets the grid lines and the background to be transparent or not.\n\n"
                        + "Import/Export Options:\n"
                        + "Save map as jpg/ png = exports the map as image of the chosen type, with the selected transparencies.\n"
                        + "Load/export from json = store/load raw map data in json format. All files must be in the folder where the jar is run from.",
                        "How to Use", JOptionPane.QUESTION_MESSAGE);
            }
        });
        m22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JOptionPane.showMessageDialog(null, "Random Map Generator\n\n"
                        + "is made by Christos Chartomatsidis, 2022.\n"
                        + "Free for any use, attribution not required but is much appreciated.\n"
                        + "This application may be used as-is, I hold no responsibility if something goes wrong.\nHopefully it won't.\n\n"
                        + "For comments, bug reports, suggestions, or anything else, drop me a line at\nhartoman@gmail.com\n"
                        + "I hope you guys have as much fun using it, as I had creating it :)", 
                        
                        "About", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        
        return menu;

    }

    // returns the folder name of the current directory where the jar runs from
    public static String getCurrentDirectoryPath() {

        String jarDir = new String();
        // this gets the folder name from where the jar file is executed        
        try {
            CodeSource codeSource = Mapper.class.getProtectionDomain().getCodeSource();
            File jarFile = new File(codeSource.getLocation().toURI().getPath());
            jarDir = jarFile.getParentFile().getPath()+"/";

        } catch (Exception e) {
            System.out.println("exception or whatever");
        }
        return jarDir;
    }

    // exports map as jpg picture
    private void mapToJpg(String filename) {

        String fullfilename = filename + ".jpg";
        
        // replaced panel.getSize() with map.getSmallerDim(), so that there are no emply 'blue' areas 
        //BufferedImage bi = new BufferedImage(panel.getSize().width, panel.getSize().height, BufferedImage.TYPE_INT_RGB);
        BufferedImage bi = new BufferedImage(map.getSmallerDim(), map.getSmallerDim(), BufferedImage.TYPE_INT_RGB);
        Graphics g = bi.createGraphics();
        panel.paint(g);  //this == JComponent
        g.dispose();
        try {
            ImageIO.write(bi, "jpg", new File(fullfilename));
        } catch (Exception e) {
        }
    }

    // exports map as transparent png picture
    private void mapToPng(String filename) {

        String fullfilename = filename + ".png";
        BufferedImage bi = new BufferedImage(map.getSmallerDim(), map.getSmallerDim(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        panel.paint(g);  //this == JComponent
        g.dispose();
        try {
            ImageIO.write(bi, "png", new File(fullfilename));
        } catch (Exception e) {
        }
    }

    // exports map data as json file
    private void mapToJson(String filename) {
        JsonHandler th = new JsonHandler();
        String fullfilename = filename + ".json";
        ArrayList<RandMap> map = new ArrayList<>();
        map.add(this.map);
        th.replacer(map, fullfilename);
    }

    // imports map data from json file
    private void mapFromJson(String filename) {
        JsonHandler th = new JsonHandler();
        String fullfilename = filename + ".json";
        ArrayList<RandMap> map = new ArrayList<>();
        map = th.loader(map, fullfilename, RandMap.class);
        this.map = map.get(0);
        loadMap(this.map, this.transparentGrid, this.transparentBackground);

    }

    public void loadMap(RandMap map, boolean transGrid, boolean transpBack) {
        // sets the map, in the panel (method of Colorpanel)
        this.panel.setMap(map, transGrid, transpBack);
        //this.inputPixels = map.getPixelsPerSide();    REDUNDANT: map will always get fixed pixels per side based on screen height
    }

    // checks that user input values are indeed integers and within allowed range
    public int limitedInputRangeCorrecting(String input, int min, int max, int defaultingTo) {

        int returnValue = 0;
        if ((input.equals(" ")) || (input == null)) {
            return defaultingTo;
        }
        try {
            returnValue = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return defaultingTo;
        }
        if (returnValue < min) {
            return min;
        }
        if (returnValue > max) {
            return max;
        } else {
            return returnValue;
        }
    }

}