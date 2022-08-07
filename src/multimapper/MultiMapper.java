/*
 * Made by Christos Chartomatsidis, 2022
 * This application is free to use, but it comes as-is:
 * I hold no responsibility for any damage or loss of that may arise from it's use.
 * Attribution is not required, but would be greatly appreciated.
 * For any comments, bug-reports, and ideas do not hesitate to contact me at:
 * hartoman@gmail.com
 */
package multimapper;

import javax.swing.JFrame;
import MapElements.*;

/**
 *
 * @author chris
 */
public class MultiMapper {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Mapper visual = new Mapper();
        visual.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        visual.setTitle("Island Map Generator");
        visual.setLocationRelativeTo(null);
        visual.setVisible(true);

    }

}
