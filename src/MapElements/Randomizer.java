/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MapElements;

import java.util.Random;
import java.util.ArrayList;
import java.awt.Point;

public class Randomizer {

    private Random random = new Random();
    // wrapper class Integer needed to store primitives in arraylist
    private ArrayList<Integer> list = new ArrayList<Integer>();

    // to create a new imaginary map
    private Point map = new Point();
    private ArrayList<Integer> maplist = new ArrayList<Integer>();

    public boolean randomBool() {
        if (randomBetween(0, 10000000) % 2 == 0) {
            return true;
        } else {
            return false;
        }
    }

    // returns integer between (including) min and max
    public int randomBetween(int min, int max) {
        return random.nextInt(max +1- min) + min;
    }

    // sets a range of numbers between (including) min and max. used for getUniqueInRange
    public void setListOfNoDuplicates(int min, int max) {

        if (min <= max) {
            list.clear();
            int tmp = min;
            for (int i = 0; i < (max + 1 - min); i++) {
                list.add(tmp);                              // creates a list with the numbers in range
                tmp++;
            }
        } else {
            System.out.println("min must be equal or less than max");
        }
    }

    // gets each number from the list specified by setListInRange, ONLY ONCE
    public int getUnique() {

        if (list.size() > 0) {
            int randomindex = randomBetween(0, list.size());
            int unique = list.get(randomindex);
            list.remove(randomindex);           // each number drawn is then removed from the list
            return unique;
        } else {
            System.out.println("list is empty! reset list!");
            return 0;
        }
    }

    // creates a map that has index (0) ton top-left corner and (x*y-1) at bottom-right
    public void setMapofNoDuplicates(int x, int y) {

        this.map.x = x;
        this.map.y = y;

        int totalMapSquares = map.x * map.y;
        maplist.clear();
        for (int i = 0; i < totalMapSquares; i++) {
            maplist.add(i);                              // creates a list with the numbers in range
        }
    }

    // gets unique coordinates from map
    public Point getUniqueCoordinates() {

        if (maplist.size() > 0) {
            int randomindex = randomBetween(0, maplist.size());
            int unique = maplist.get(randomindex);
            maplist.remove(randomindex);

            int y = unique / this.map.x;
            int x = unique % this.map.x;

            Point coords = new Point(x, y);

            //   System.out.println(coords);
            //     System.out.println(maplist);
            return coords;
        } else {
            System.out.println("no more free map spaces!");
            return null;
        }
    }

    public ArrayList<Integer> getUniqueRemaining() {
        return this.list;
    }

    public void setUniqueRemaining(ArrayList<Integer> list) {
        this.list = list;
    }

    // the x, y that represent the total map dimensions will be stored in the last 2 index numbers
    // in a way, we return a 'folded' map that contains its own dimensions
    public ArrayList<Integer> getMapRemaining() {

        ArrayList<Integer> tmp = new ArrayList<Integer>();
        tmp = this.maplist;
        //   System.out.println(tmp);
        return tmp;

    }

    // will create a map, by taking the two last integers of input array as x,y
    // we unfold the map
    public void setMapRemaining(ArrayList<Integer> tmplist, int columns) {

        this.map.x = columns;
        maplist.clear();
        this.maplist = tmplist;                   // remaining empty map places
    }

    // gets unique coordinates from map
    public Point getCoordsNOTadjacent() {

        if (maplist.size() > 0) {
            int randomindex = randomBetween(0, maplist.size());
            int unique = maplist.get(randomindex);

            /*       for (int i=-2;i<3;i++){
                for (int j=-2;j<3;j++){
                    if (maplist.contains(Integer.valueOf(unique+i+(map.x)*j))){
                        maplist.remove(Integer.valueOf(unique+i+(map.x)*j));
                    }
                }
            }       */
            for (int i = -2; i < 3; i++) {
                if (maplist.contains(Integer.valueOf(unique + i))) {
                    maplist.remove(Integer.valueOf(unique + i));
                }
                if (maplist.contains(Integer.valueOf(unique + (map.x) * i))) {
                    maplist.remove(Integer.valueOf(unique + (map.x) * i));
                }
            }

            int y = unique / this.map.x;
            int x = unique % this.map.x;

            Point coords = new Point(x, y);

            //   System.out.println(coords);
            // System.out.println(unique);
            //  System.out.println(maplist);
            return coords;

        } else {
            System.out.println("no more free map spaces!");
            return null;
        }
    }

}
