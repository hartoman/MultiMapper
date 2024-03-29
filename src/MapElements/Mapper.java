/*
Made by Christos Chartomatsidis, 2022
This application is free to use, but it comes as-is:
I hold no responsibility for any damage or loss of that may arise from it's use.
Attribution is not required, but would be greatly appreciated.
For any comments, bug-reports, and ideas do not hesitate to contact me at:
hartoman@gmail.com

 */
package MapElements;

import Toolz.*;
import java.awt.Graphics;
import java.awt.Component;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.event.ItemEvent;
import static java.awt.image.ImageObserver.HEIGHT;
import java.security.CodeSource;

/**
 *
 * @author chris
 */
public class Mapper extends JFrame {

    
    private Colorpanel panel;
    private UiPanel uiPane;
    private MenuBar menu;

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
    private boolean townHasCastle = false;

    private String chosenTextureScheme;
    private String mapType;

    
    ///////////////////////////////////////////////////////////////////////
    public Mapper() {

        // start-up application customization
        initializeParameters();

        // creates map and visualizes it through the colorpanel
        this.map = new IslandMap(squaresPerSide, distortion, inputPixels, chosenTextureScheme, maxElevationValue, numPeaksValue);

        // start-up of drawing map panel
        initializeColorPanel();
        
        //creates elements of the uiPanel
        uiPane = new UiPanel();
        add(uiPane);

        // defines dimensions of the frame
        this.setSize((panel.getSize().width + uiPane.getSize().width), Math.max((int) (inputPixels * 1.06), uipanelHeight));

        // adds menu bar to the frame        
        menu = new MenuBar();
        setJMenuBar(menu);

    }

    public static void main(String args[]) {

    }


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
    
    // sets up basic application parameters at start-up
    void initializeParameters(){
        // sets font
        setUIFont(new javax.swing.plaf.FontUIResource("Serif", Font.PLAIN, 16));

        // so that everything is autoarranged in one row, 2 columns
        //  this.setLayout(new GridLayout(1, 2));
        this.setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));

        // define map parameters
        squaresPerSide = 50;
        distortion = 2;
        maxElevationValue = 8;
        numPeaksValue = 50;
        chosenTextureScheme = "Islands";
        mapType = "Landscape";

        // gets the maximum screen height   -- the*0.92 is to adjust for lost screen space on linux
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        this.inputPixels = (int) (gd.getDisplayMode().getHeight() * 0.92);
    }
    
    void initializeColorPanel(){
                // creates the map panel
        panel = new Colorpanel(map);
        panel.setLocation(panelX, panelY);
        panel.setSize((int) (inputPixels * 0.98), inputPixels);
        panel.setMinimumSize(new Dimension(inputPixels, inputPixels));
        panel.setMaximumSize(new Dimension(inputPixels, inputPixels));
        add(panel);
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

        if (mapType == "Landscape") {
            String fullfilename = getCurrentDirectoryPath() + "LAND-" + filename + ".json";
            ArrayList<IslandMap> map = new ArrayList<>();
            IslandMap tmpmap = (IslandMap) this.map;
            map.add(tmpmap);
            th.replacer(map, fullfilename);
        } else {
            String fullfilename = getCurrentDirectoryPath() + "TOWN-" + filename + ".json";
            ArrayList<TownMap> map = new ArrayList<>();
            TownMap tmpmap = (TownMap) this.map;
            map.add(tmpmap);
            th.replacer(map, fullfilename);
        }

    }

    // imports map data from json file
    private void mapFromJson(String filename) {
        JsonHandler th = new JsonHandler();

        if (filename.startsWith("LAND-")) {
            String fullfilename = getCurrentDirectoryPath() + filename + ".json";
            ArrayList<IslandMap> map = new ArrayList<>();
            map = th.loader(map, fullfilename, IslandMap.class);
            this.map = map.get(0);
            mapType = "Landscape";
        }
        if (filename.startsWith("TOWN-")) {
            String fullfilename = getCurrentDirectoryPath() + filename + ".json";
            ArrayList<TownMap> map = new ArrayList<>();
            map = th.loader(map, fullfilename, TownMap.class);
            this.map = map.get(0);
            mapType = "Settlement";
        }
        panel.removeAll();
        panel.updateUI();
        loadMap(this.map);

    }

    // loads a map
    public void loadMap(RandMap map) {
        // sets the map, in the panel (method of Colorpanel)
        this.panel.setMap(map, transparentGrid, transparentBackground);
        //this.inputPixels = map.getPixelsPerSide();    REDUNDANT: map will always get fixed pixels per side based on screen height
    }

    
    // map customization options panel
    class UiPanel extends JPanel{
    
        JTextField squares;
        JTextField dist;
        JTextField numPeaks;
        JLabel elevationLabel;
        JTextField maxElevation;
        JLabel peaksLabel;
        JButton createMapbutton;
        JRadioButton back1;
        JRadioButton back2;
        JRadioButton textureChoice1; 
        JRadioButton textureChoice2; 
        JRadioButton textureChoice3;
        JPanel townOptionsPanel;
        
        
        UiPanel() {

        //sets up basic dimensions and layout
      //  uiPane = new JPanel();
        this.setSize(uipanelWidth, uipanelHeight);
        this.setPreferredSize(new Dimension(uipanelWidth, uipanelHeight));
        this.setMaximumSize(new Dimension(uipanelWidth, uipanelHeight));
        this.setLocation(this.getSize().width - uipanelWidth, panelY);
                
        // create the various sub-panels with customization options
        addElementsToUI();
        }
        
    // create the various sub-panels with customization options
    void addElementsToUI(){
        addMapParamsPanel();
        addElevationParamsPanel();
        addtranspPanel();
        addTextureChoicePanel();
        addMapChoicePanel();
        addTownOptionsPanel();
        addCreateButtonPanel();
       }
             
    // panel with general map parameters
    void addMapParamsPanel(){
        
        JPanel mapParams = new JPanel();
        mapParams.add(new javax.swing.Box.Filler(new java.awt.Dimension(1, 15), new java.awt.Dimension(1, 15), new java.awt.Dimension(1, 15)));
        mapParams.setPreferredSize(new Dimension(getWidth() * 3 / 4, 135));
        mapParams.setLayout(new BoxLayout(mapParams, BoxLayout.PAGE_AXIS));
        mapParams.setAlignmentY(Component.TOP_ALIGNMENT);
        mapParams.setAlignmentX(Component.CENTER_ALIGNMENT);
        mapParams.add(new JLabel("Map Parameters"));
        mapParams.add(new JSeparator());
        //   mapParams.add(new JSeparator());
        mapParams.add(new JLabel("Number of Squares"));
        squares = new JTextField("50");
        squares.setMaximumSize(new Dimension(100, 100));
        mapParams.add(squares);
        mapParams.add(new JLabel("Distortion"));
        dist = new JTextField("2");
        dist.setMaximumSize(new Dimension(100, 100));
        mapParams.add(dist);
        add(mapParams);
        }
    
    // creates panel with elevation parameters
    void addElevationParamsPanel(){
                
        JPanel elevationParams = new JPanel();
        elevationParams.add(new javax.swing.Box.Filler(new java.awt.Dimension(1, 15), new java.awt.Dimension(1, 15), new java.awt.Dimension(1, 15)));
        elevationParams.setPreferredSize(new Dimension(getWidth() * 3 / 4, 135));
        elevationParams.setLayout(new BoxLayout(elevationParams, BoxLayout.PAGE_AXIS));
        elevationParams.setAlignmentY(Component.CENTER_ALIGNMENT);
        elevationParams.setAlignmentX(Component.CENTER_ALIGNMENT);
        elevationParams.add(new JLabel("Elevation Parameters"));
        elevationParams.add(new JSeparator());
        peaksLabel = new JLabel("Max possible peaks");
        elevationParams.add(peaksLabel);
        //  elevationParams.add(new JLabel("Max possible peaks"));
        numPeaks = new JTextField("50");
        numPeaks.setMaximumSize(new Dimension(100, 100));
        elevationParams.add(numPeaks);
        elevationLabel = new JLabel("Max Elevation");
        elevationParams.add(elevationLabel);
        //elevationParams.add(new JLabel("maxElevation"));
        maxElevation = new JTextField("7");
        maxElevation.setMaximumSize(new Dimension(100, 100));
        elevationParams.add(maxElevation);
        add(elevationParams);
    }
            
    // creates panel to choose transparency for grid lines        
    void addtranspPanel(){
                
        JPanel transpPanel = new JPanel();
        transpPanel.setPreferredSize(new Dimension(getWidth() * 3 / 4, 30));
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

                    transparentGrid = false;
                    panel.removeAll();
                    panel.updateUI();
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
        add(transpPanel);
    }
      
    // creates panel for choosing map texture
    void addTextureChoicePanel(){
        
        JPanel textureChoicePanel = new JPanel();
        textureChoicePanel.setPreferredSize(new Dimension(getWidth() * 3 / 4, 110));
        textureChoicePanel.setLayout(new BoxLayout(textureChoicePanel, BoxLayout.PAGE_AXIS));
        textureChoicePanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        textureChoicePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //creates checkboxes for existence of sea and river
        //creates two radiobuttons and assigns them to the same buttongroup
        textureChoicePanel.add(new JLabel("Choose a Theme:"));
        textureChoicePanel.add(new JSeparator());
        
        textureChoice1 = new JRadioButton("Islands", true);
        textureChoice2 = new JRadioButton("Wasteland", true);
        textureChoice3 = new JRadioButton("Alien Planet", true);
        back1= new JRadioButton();

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
        add(textureChoicePanel);
    }
    
    // creates panel for choosing map of landscape or town
    void addMapChoicePanel(){
        
        JPanel mapChoicePanel = new JPanel();
        mapChoicePanel.setPreferredSize(new Dimension(getWidth() * 3 / 4, 80));
        mapChoicePanel.setLayout(new BoxLayout(mapChoicePanel, BoxLayout.PAGE_AXIS));
        mapChoicePanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        mapChoicePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //creates two radiobuttons and assigns them to the same buttongroup
        mapChoicePanel.add(new JLabel("Create a new:"));
        mapChoicePanel.add(new JSeparator());
                        
        // if Landscape selected
        back1 = new JRadioButton("Landscape", true);
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
        
        // if Settlement selected
        back2 = new JRadioButton("Settlement", false);
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

        // makes buttons mutually exclusive
        ButtonGroup fondo = new ButtonGroup();
        fondo.add(back1);
        fondo.add(back2);

        add(mapChoicePanel);
    }
    
    // creates panel for town options
    void addTownOptionsPanel(){
        
        // we need to have declared in advance all variables that are going to be affected by the buttonchoice
        townOptionsPanel = new JPanel();
        townOptionsPanel.setVisible(false);
        
        townOptionsPanel.setPreferredSize(new Dimension(getWidth() * 3 / 4, 80));
        townOptionsPanel.setLayout(new BoxLayout(townOptionsPanel, BoxLayout.PAGE_AXIS));
        townOptionsPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        townOptionsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //creates checkboxes for existence of sea
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

        //creates checkboxes for existence of river
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

        //creates checkboxes for existence of castle
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
        add(townOptionsPanel);
    }
                
    // creates panel containing map creation button
    void addCreateButtonPanel(){
                
        JPanel createPanel = new JPanel();
        createPanel.add(new javax.swing.Box.Filler(new java.awt.Dimension(1, 15), new java.awt.Dimension(1, 15), new java.awt.Dimension(1, 15)));
        createPanel.setPreferredSize(new Dimension(getWidth() * 3 / 4, 100));
        createPanel.setLayout(new BoxLayout(createPanel, BoxLayout.X_AXIS));
        createPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        createPanel.setAlignmentX(Component.BOTTOM_ALIGNMENT);
        createPanel.add(new JSeparator());

        //  button to create new map
        createMapbutton = new JButton("Create!");
        createMapbutton.setSize(getWidth(), HEIGHT);
        createMapbutton.setMinimumSize(new Dimension(getWidth() * 3 / 4 - 10, 100));
        createMapbutton.setPreferredSize(new Dimension(getWidth() * 3 / 4, 100));
        createPanel.add(createMapbutton);
        setCreateButtonListener();
        
        add(createPanel);
    }
    
    // what happens when user clicks on the create button
    void setCreateButtonListener(){
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
                    if (townHasCastle) {
                        squaresPerSide = limitedInputRangeCorrecting(squares.getText(), 20, maxSquaresPerSide, 50);
                    } else {
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
                    map = new IslandMap(squaresPerSide, distortion, inputPixels, chosenTextureScheme, maxElevationValue, numPeaksValue);
                    mapType = "Landscape";
                } else {
                    map = new TownMap(squaresPerSide, distortion, inputPixels, chosenTextureScheme, maxElevationValue, numPeaksValue, townHasSea, townHasRiver, townHasCastle);
                    mapType = "Settlement";
                }

                loadMap(map);
            }
        });
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
    
    // menu bar options
    class MenuBar extends JMenuBar{
        
        JMenuItem m10 ,m13,m11,m12,m21,m22;
              
        MenuBar(){
            addMenuButtons();
            addMenuListeners();
        }
        
        // adds the buttons to the menu
        void addMenuButtons(){
                    // first columne
        JMenu column1 = new JMenu("Import/Export");
        m10 = new JMenuItem("Load map from json");
        //  m10.setEnabled(false);
        m13 = new JMenuItem("Export map as json");
        m11 = new JMenuItem("Save image as jpg");
        m12 = new JMenuItem("Save image as png (transparent background)");
        column1.add(m10);
        column1.add(m13);
        column1.add(m11);
        column1.add(m12);
        add(column1);
        
        // second column
        JMenu column2 = new JMenu("Help");
        m21 = new JMenuItem("How to use");
        m22 = new JMenuItem("About");
        column2.add(m21);
        column2.add(m22);
        add(column2);

        }
        
        //assign action listeners to menuitems
        void addMenuListeners(){
            
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

                if ((filename != null) && (!filename.isEmpty())) {
                    mapToJson(filename);
                }

            }
        });

        m10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String filename = JOptionPane.showInputDialog(null, "Enter filename", "Load map from json", JOptionPane.INFORMATION_MESSAGE);
                //   String fullpath = getCurrentDirectoryPath() + filename;
                if ((filename != null) && (!filename.isEmpty())) {
                    mapFromJson(filename);
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
                JOptionPane.showMessageDialog(null, "Random Map Generator v_1.2\n\n"
                        + "is made by Christos Chartomatsidis, 2022.\n"
                        + "Free for any use, attribution not required but is much appreciated.\n"
                        + "This application may be used as-is, I hold no responsibility if something goes wrong.\nHopefully it won't.\n\n"
                        + "For comments, bug reports, suggestions, or anything else, drop me a line at\nhartoman@gmail.com\n"
                        + "I hope you guys have as much fun using it, as I had creating it :)",
                        "About", JOptionPane.ERROR_MESSAGE);
            }
        });
        }

    }
    }
