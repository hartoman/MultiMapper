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

    private final int maxSquaresPerSide = 100;

    private final int panelX = 20;      // x where map appears and also distance between map and ui
    private final int panelY = 20;      // y where map and ui appear

    private RandMap map;
    private boolean transparentGrid = false;
    private boolean transparentBackground = false;

    private final int maxIsleElevation = 7;
    private int maxIslePeaks = 7;

    private int maxTownRoads = 7;
    private final int maxTownDensity = 100;

    private boolean townHasSea = false;
    private boolean townHasRiver = false;
    private boolean townHasCastle= false;

    private String chosenTextureScheme;

    ///////////////////////////////////////////////////////////////////////
    public Mapper() {

        setUIFont(new javax.swing.plaf.FontUIResource("Serif", Font.PLAIN, 16));

        // so that everything is autoarranged in one row, 2 columns
        //  this.setLayout(new GridLayout(1, 2));
        this.setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));

        // define map parameters
        squaresPerSide = 50;
        distortion = 2;
        maxElevationValue = 8;
        numPeaksValue = 50;
        chosenTextureScheme= "Islands";

        // gets the maximum screen height   -- the*0.92 is to adjust for lost screen space on linux
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        this.inputPixels = (int) (gd.getDisplayMode().getHeight() * 0.92);

        // creates map and visualizes it through the colorpanel
        this.map = new IslandMap(squaresPerSide, distortion, inputPixels,chosenTextureScheme, maxElevationValue, numPeaksValue);


        // creates the map panel
        panel = new Colorpanel(map);

        panel.setLocation(panelX, panelY);

        panel.setSize((int) (inputPixels * 0.98), inputPixels);

        panel.setMinimumSize(new Dimension(inputPixels, inputPixels));
        panel.setMaximumSize(new Dimension(inputPixels, inputPixels));
        //   panel.setPreferredSize(new Dimension(inputPixels, inputPixels));

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
        mapParams.setPreferredSize(new Dimension(uiPane.getWidth() * 3 / 4, 135));
        mapParams.setLayout(new BoxLayout(mapParams, BoxLayout.PAGE_AXIS));
        mapParams.setAlignmentY(Component.TOP_ALIGNMENT);
        mapParams.setAlignmentX(Component.CENTER_ALIGNMENT);
        mapParams.add(new JLabel("Map Parameters"));
        mapParams.add(new JSeparator());
        //   mapParams.add(new JSeparator());
        mapParams.add(new JLabel("Number of Squares"));
        JTextField squares = new JTextField("50");
        squares.setMaximumSize(new Dimension(100, 100));
        mapParams.add(squares);
        mapParams.add(new JLabel("Distortion"));
        JTextField dist = new JTextField("2");
        dist.setMaximumSize(new Dimension(100, 100));
        mapParams.add(dist);
        uiPane.add(mapParams);

        //////////////**************//////////////
        //creates panel with elevation parameters
        JPanel elevationParams = new JPanel();
        elevationParams.add(new javax.swing.Box.Filler(new java.awt.Dimension(1, 15), new java.awt.Dimension(1, 15), new java.awt.Dimension(1, 15)));
        elevationParams.setPreferredSize(new Dimension(uiPane.getWidth() * 3 / 4, 135));
        elevationParams.setLayout(new BoxLayout(elevationParams, BoxLayout.PAGE_AXIS));
        elevationParams.setAlignmentY(Component.CENTER_ALIGNMENT);
        elevationParams.setAlignmentX(Component.CENTER_ALIGNMENT);
        elevationParams.add(new JLabel("Elevation Parameters"));
        elevationParams.add(new JSeparator());
        JLabel peaksLabel = new JLabel("Max possible peaks");
        elevationParams.add(peaksLabel);
        //  elevationParams.add(new JLabel("Max possible peaks"));
        JTextField numPeaks = new JTextField("50");
        numPeaks.setMaximumSize(new Dimension(100, 100));
        elevationParams.add(numPeaks);
        JLabel elevationLabel = new JLabel("Max Elevation");
        elevationParams.add(elevationLabel);
        //elevationParams.add(new JLabel("maxElevation"));
        JTextField maxElevation = new JTextField("7");
        maxElevation.setMaximumSize(new Dimension(100, 100));
        elevationParams.add(maxElevation);
        uiPane.add(elevationParams);

        //////////////**************//////////////
        //creates panel to choose transparency for grid lines 
        JPanel transpPanel = new JPanel();
        //    transpPanel.add(new javax.swing.Box.Filler(new java.awt.Dimension(1, 15), new java.awt.Dimension(1, 15), new java.awt.Dimension(1, 15)));
        transpPanel.setPreferredSize(new Dimension(uiPane.getWidth() * 3 / 4, 30));
        transpPanel.setLayout(new BoxLayout(transpPanel, BoxLayout.PAGE_AXIS));
        transpPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        transpPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //creates checkbox for grid transparency
        JCheckBox gridbox = new JCheckBox("Show Gridlines");
        gridbox.setSelected(true);
        gridbox.addItemListener(new java.awt.event.ItemListener() {
            /* we go for itemEventListener because it fires only once.
            On the contrary, actionEventListener, fires every time there's a click*/
            public void itemStateChanged(java.awt.event.ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    panel.removeAll();
                    panel.updateUI();
                    transparentGrid = false;
                    loadMap(map);
                } else {
                    transparentGrid = true;
                    panel.removeAll();
                    panel.updateUI();
                    loadMap(map);
                }
            }
        });

        transpPanel.add(gridbox);
        uiPane.add(transpPanel);

        //////////////**************//////////////
        // creates panel to choose from creation of new type of Island or Town
        JPanel mapChoicePanel = new JPanel();

        // we need to have declared in advance all variables that are going to be affected by the buttonchoice
        JPanel townOptionsPanel = new JPanel();
        townOptionsPanel.setVisible(false);

        JRadioButton textureChoice1 = new JRadioButton("Islands", true);
        JRadioButton textureChoice2 = new JRadioButton("Wasteland", true);
        JRadioButton textureChoice3 = new JRadioButton("Alien Planet", true);

        ///////////////////////////////////////////////////
        mapChoicePanel.setPreferredSize(new Dimension(uiPane.getWidth() * 3 / 4, 80));
        mapChoicePanel.setLayout(new BoxLayout(mapChoicePanel, BoxLayout.PAGE_AXIS));
        mapChoicePanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        mapChoicePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //creates two radiobuttons and assigns them to the same buttongroup
        mapChoicePanel.add(new JLabel("Create a new:"));
        mapChoicePanel.add(new JSeparator());
        JRadioButton back1 = new JRadioButton("Landscape", true);
        back1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    townOptionsPanel.setVisible(false);
                    peaksLabel.setText("Max Possible Peaks");
                    elevationLabel.setText("Max Elevation");
                    textureChoice1.setText("Islands");
                    textureChoice2.setText("Wasteland");
                    textureChoice3.setText("Alien Planet");
                    dist.setText("2");
                    numPeaks.setText("50");
                    maxElevation.setText("7");
                    chosenTextureScheme = "Islands";
                    textureChoice1.setSelected(true);
                }
            }
        });
        mapChoicePanel.add(back1);

        JRadioButton back2 = new JRadioButton("Settlement", false);
        back2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    townOptionsPanel.setVisible(true);
                    peaksLabel.setText("Max Horizontal Streets");
                    elevationLabel.setText("% Building Density");
                    textureChoice1.setText("Village");
                    textureChoice2.setText("Post-Apoc");
                    textureChoice3.setText("Dark Urban");
                    dist.setText("0");
                    numPeaks.setText("10");
                    maxElevation.setText("35");
                    chosenTextureScheme = "Village";
                    textureChoice1.setSelected(true);
                }
            }
        });
        mapChoicePanel.add(back2);

        ButtonGroup fondo = new ButtonGroup();
        fondo.add(back1);
        fondo.add(back2);

        //  mapChoicePanel.add(new javax.swing.Box.Filler(new java.awt.Dimension(1, 15), new java.awt.Dimension(1, 15), new java.awt.Dimension(1, 15)));
        uiPane.add(mapChoicePanel);

        //////////////**************//////////////
        //creates panel to choose transparency for grid lines 
        //    transpPanel.add(new javax.swing.Box.Filler(new java.awt.Dimension(1, 15), new java.awt.Dimension(1, 15), new java.awt.Dimension(1, 15)));
        townOptionsPanel.setPreferredSize(new Dimension(uiPane.getWidth() * 3 / 4, 80));
        townOptionsPanel.setLayout(new BoxLayout(townOptionsPanel, BoxLayout.PAGE_AXIS));
        townOptionsPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        townOptionsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //creates checkboxes for existence of sea and river
        JCheckBox seaBox = new JCheckBox("Sea");
        seaBox.setSelected(false);
        seaBox.addItemListener(new java.awt.event.ItemListener() {
            /* we go for itemEventListener because it fires only once.
            On the contrary, actionEventListener, fires every time there's a click*/
            public void itemStateChanged(java.awt.event.ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    townHasSea = true;
                } else {
                    townHasSea = false;
                }
            }
        });

        JCheckBox riverBox = new JCheckBox("River");
        riverBox.setSelected(false);
        riverBox.addItemListener(new java.awt.event.ItemListener() {
            /* we go for itemEventListener because it fires only once.
            On the contrary, actionEventListener, fires every time there's a click*/
            public void itemStateChanged(java.awt.event.ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    townHasRiver = true;
                } else {
                    townHasRiver = false;
                }
            }
        });
        
        JCheckBox castleBox = new JCheckBox("Castle");
        castleBox.setSelected(false);
        castleBox.addItemListener(new java.awt.event.ItemListener() {
            /* we go for itemEventListener because it fires only once.
            On the contrary, actionEventListener, fires every time there's a click*/
            public void itemStateChanged(java.awt.event.ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    townHasCastle = true;
                } else {
                    townHasCastle = false;
                }
            }
        });
        
        
        townOptionsPanel.add(seaBox);
        townOptionsPanel.add(riverBox);
        townOptionsPanel.add(castleBox);
        uiPane.add(townOptionsPanel);

        //////////////**************//////////////
        //creates panel to choose texture scheme 
        JPanel textureChoicePanel = new JPanel();
        //    transpPanel.add(new javax.swing.Box.Filler(new java.awt.Dimension(1, 15), new java.awt.Dimension(1, 15), new java.awt.Dimension(1, 15)));
        textureChoicePanel.setPreferredSize(new Dimension(uiPane.getWidth() * 3 / 4, 110));
        textureChoicePanel.setLayout(new BoxLayout(textureChoicePanel, BoxLayout.PAGE_AXIS));
        textureChoicePanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        textureChoicePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //creates checkboxes for existence of sea and river
        //creates two radiobuttons and assigns them to the same buttongroup
        textureChoicePanel.add(new JLabel("Choose a Theme:"));
        textureChoicePanel.add(new JSeparator());

        //   JRadioButton textureChoice1 = new JRadioButton("theme1", true);
        textureChoice1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    // Landscape
                    if (back1.isSelected() == true) {
                        chosenTextureScheme = "Islands";
                    } // Settlement
                    else {
                        chosenTextureScheme = "Village";
                    }
                }
            }
        });

        //   JRadioButton textureChoice2 = new JRadioButton("theme2", true);
        textureChoice2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    // Landscape
                    if (back1.isSelected() == true) {
                        chosenTextureScheme = "Wasteland";
                    } // Settlement
                    else {
                        chosenTextureScheme = "Post-Apoc";
                    }
                }
            }
        });

        //    JRadioButton textureChoice3 = new JRadioButton("theme3", false);
        textureChoice3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    // Landscape
                    if (back1.isSelected() == true) {
                        chosenTextureScheme = "Space";
                    } // Settlement
                    else {
                        chosenTextureScheme = "Dark Urban";
                    }
                }
            }
        });

        ButtonGroup textureChoices = new ButtonGroup();
        textureChoices.add(textureChoice1);
        textureChoices.add(textureChoice2);
        textureChoices.add(textureChoice3);

        textureChoicePanel.add(textureChoice1);
        textureChoicePanel.add(textureChoice2);
        textureChoicePanel.add(textureChoice3);
        uiPane.add(textureChoicePanel);

        //////////////**************//////////////
        // creates panel containing map creation button
        JPanel createPanel = new JPanel();
        createPanel.add(new javax.swing.Box.Filler(new java.awt.Dimension(1, 15), new java.awt.Dimension(1, 15), new java.awt.Dimension(1, 15)));
        createPanel.setPreferredSize(new Dimension(uiPane.getWidth() * 3 / 4, 100));
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
                int maxPossiblePeaks = 2;
                if (back1.isSelected() == true) {
                    squaresPerSide = limitedInputRangeCorrecting(squares.getText(), 5, maxSquaresPerSide, 50);
                    maxElevationValue = Math.min((squaresPerSide - 3) / 2, maxIsleElevation);
                    maxPossiblePeaks = (inputPixels - (2 * maxElevationValue + 2)) * (inputPixels - (2 * maxElevationValue + 2));
                } else {
                    if (townHasCastle){
                        squaresPerSide = limitedInputRangeCorrecting(squares.getText(), 20, maxSquaresPerSide, 50);
                    }else{
                        squaresPerSide = limitedInputRangeCorrecting(squares.getText(), 11, maxSquaresPerSide, 50);
                    }
                    
                    maxElevationValue = maxTownDensity;
                    maxPossiblePeaks = squaresPerSide / 5;
                }

                squares.setText(String.valueOf(squaresPerSide));

                int maxDist = (inputPixels / squaresPerSide);
                int distortion = limitedInputRangeCorrecting(dist.getText(), 0, maxDist, 2);
                dist.setText(String.valueOf(distortion));

                maxElevationValue = limitedInputRangeCorrecting(maxElevation.getText(), 0, maxElevationValue, maxElevationValue);
                maxElevation.setText(String.valueOf(maxElevationValue));

                numPeaksValue = limitedInputRangeCorrecting(numPeaks.getText(), 1, maxPossiblePeaks, 5);
                numPeaks.setText(String.valueOf(numPeaksValue));

                if (back1.isSelected()) {
                    map = new IslandMap(squaresPerSide, distortion, inputPixels, chosenTextureScheme,maxElevationValue, numPeaksValue);
                } else {
                    map = new TownMap(squaresPerSide, distortion, inputPixels,chosenTextureScheme, maxElevationValue, numPeaksValue, townHasSea, townHasRiver,townHasCastle);
                }

                loadMap(map);
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
                String fullpath = getCurrentDirectoryPath() + filename;
                if ((filename != null) && (!filename.isEmpty())) {
                    mapToJpg(fullpath);
                }

            }
        });
        //  "Save image as png"
        m12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String filename = JOptionPane.showInputDialog(null, "Enter filename", "Save image as png (transparent background)", JOptionPane.INFORMATION_MESSAGE);
                String fullpath = getCurrentDirectoryPath() + filename;
                if ((filename != null) && (!filename.isEmpty())) {

                    transparentBackground = true;
                    panel.removeAll();
                    panel.updateUI();
                    loadMap(map);
                    mapToPng(fullpath);

                    transparentBackground = false;
                    loadMap(map);
                }

            }
        });

        m13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String filename = JOptionPane.showInputDialog(null, "Enter filename", "Export map as json", JOptionPane.INFORMATION_MESSAGE);
                String fullpath = getCurrentDirectoryPath() + filename;
                if ((filename != null) && (!filename.isEmpty())) {
                    mapToJson(fullpath);
                }

            }
        });

        m10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String filename = JOptionPane.showInputDialog(null, "Enter filename", "Load map from json", JOptionPane.INFORMATION_MESSAGE);
                String fullpath = getCurrentDirectoryPath() + filename;
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
                        + "Various map Options:\n"
                        + "Just play around and experiment, there are a lot of customization options for various types of maps"
                        + "Import/Export Options:\n"
                        + "Save map as jpg/ png = exports the map as image of the chosen type, with the selected transparencies.\n"
                        + "Load/export from json = store/load raw map data in json format. All files must be in the folder where the jar is run from.",
                        "How to Use", JOptionPane.QUESTION_MESSAGE);
            }
        });
        m22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JOptionPane.showMessageDialog(null, "Random Map Generator v_1.1\n\n"
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
            jarDir = jarFile.getParentFile().getPath() + "/";

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
        loadMap(this.map);

    }

    public void loadMap(RandMap map) {
        // sets the map, in the panel (method of Colorpanel)
        this.panel.setMap(map, transparentGrid, transparentBackground);
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
