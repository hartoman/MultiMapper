/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MapElements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.*;

import java.lang.reflect.Type; 
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.*;
import java.nio.file.*;

import java.util.Map;


/**
 *
 * @author chris
 */
public class JsonHandler {
    
    
    
    
    
    // TEMP CAN DELETE
    public void toJson(){
        Gson gson = new Gson();
        String json = gson.toJson(this);
        System.out.println(json);
    }
    
    public JsonHandler(){};
 
    
    /*
        // THE FOLLOWING 2 METHODS WORK FINE, BUT THEY ARE HIGHLY SPECIALIZED.
        // BELOW THERE ARE GENERAL-PURPOSE METHODS THAT DO THE SAME THING
    
    // appends new message to json file. if no file with this name exists, then it is created
    public void saveText(String filename,Message msg){
            
        ArrayList<Message> messages = new ArrayList<>();
        messages = loadText(filename);
    
        try {
        Writer writer = Files.newBufferedWriter(Paths.get(filename));
        Gson gson = new GsonBuilder().setPrettyPrinting().create(); 

        messages.add(msg);
        gson.toJson(messages, writer);
        writer.close();
          System.out.println(gson.toJson(messages));
       } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("no file with this name was found so it was created");
        }
    }
    
    // returns all objects of type Message that are included in the selected json file
    public ArrayList<Message> loadText(String filename){
           
            ArrayList<Message> messages = new ArrayList<>();
        try {
    // create Gson instance
            Gson gson = new GsonBuilder().setPrettyPrinting().create(); 
    // create a reader
            Reader reader = Files.newBufferedReader(Paths.get(filename));
            Type msgType = new TypeToken<ArrayList<Message>>(){}.getType();
            messages = gson.fromJson(reader, msgType); 
            
        // print messages
            System.out.println(gson.toJson(messages));
   
    // close reader
            reader.close();
            
        } catch (Exception ex) {
      //      ex.printStackTrace();
            System.out.println("no file with this name was found");
        }
        return messages;
   }
    */

 ///////////////////////////////////////////////////////////////////////////////////////////////////////////////   
    
    
    
    
// returns all objects of type CLASSNAME that are included in the selected json file
/* TO CALL:
        ArrayList<CLASSNAME> originalList = new ArrayList<>();
    
        msg = th.loader(originalList, "filename.json", CLASSNAME.class);   
    
       NOTA BENE: CLASSNAME must be imported in the texthandler
    
    */
 public <T> ArrayList<T> loader(ArrayList<T> clazz, String filename, Class<?> typeof) {

        if ((filename != null) && !(filename.equals(" "))) {
            try {
                // create Gson instance
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                // create a reader
                Reader reader = Files.newBufferedReader(Paths.get(filename));
                //creates list of given type
                ArrayList<T> listInJson = new ArrayList<>();
                // based on the private method 'token', see below  
                Type type = token(ArrayList.class, typeof);
                // parses json file based on the given class type        
                listInJson = gson.fromJson(reader, type);
                // prettyprint the arraylist            *******************
                //System.out.println(gson.toJson(listInJson));

                // closes reader
                reader.close();
                return listInJson;

            } catch (Exception ex) {
                //      ex.printStackTrace();
                System.out.println("no file with this name was found");
            }
        }
        return null;

    }

    /*          USED ABOVE, HERE FOR CLARITY AND SIMPLICITY:   
     creates object of Type class, needed to parse the json
     ARGUMENTS: (ArrayList.class, CLASSNAME.class)
     Type tokentype = token(ArrayList.class, myType.class);       */
    private Type token(Class<?> main, Class<?> type) {
        return TypeToken.getParameterized(main, type).getType();
    }

    //    appends new class object to json file that contains an arraylist of similar objects
    //    if no file with this name exists, then it is created
    /*
        TO CALL:
        
        CLASSNAME newClassObject = new CLASSNAME(whatever);
        ArrayList<CLASSNAME> originalList = new ArrayList<>();
        th.appender(newClassObject,originalList, "filename.json", CLASSNAME.class);   
      
     */
    //suppresswarnings is to avoid unchecked operations warning
    @SuppressWarnings("unchecked")
    public <T> void appender(Object obj, ArrayList<T> clazz, String filename, Class<?> typeof) {

        if ((filename != null) && !(filename.equals(" "))) {
            ArrayList<T> listInJson = new ArrayList<>();
            listInJson = loader(clazz, filename, typeof);

            try {
                Writer writer = Files.newBufferedWriter(Paths.get(filename));
                Gson gson = new GsonBuilder().setPrettyPrinting().create();

                listInJson.add((T) obj);
                gson.toJson(listInJson, writer);
                writer.close();
                System.out.println(gson.toJson(listInJson));
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("no file with this name was found so it was created");
            }
        }
    }

    //    entirely replaces arraylist of objects contained in  json file
    //    if no file with this name exists, then it is created
    /*
        TO CALL:

    ArrayList<CLASSNAME> newlist = new ArrayList<>();
        th.replacer(newlist, "filename.json");   
      
     */
    
    //suppresswarnings is to avoid unchecked operations warning
    @SuppressWarnings("unchecked")
    public <T> void replacer(ArrayList<T> clazz, String filename) {

        if ((filename != null) && !(filename.equals(" "))) {
            try {
                Writer writer = Files.newBufferedWriter(Paths.get(filename));
                Gson gson = new GsonBuilder().setPrettyPrinting().create();

                gson.toJson(clazz, writer);
                writer.close();
                //  System.out.println(gson.toJson(clazz));
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("no file with this name was found so it was created");
            }
        }
    }





//////
}
///////     END OF CLASS TEXTHANDLER ///////////////////////////////////////////////////////////////////////




   /* 
    ////////////////////// EXAMPLE of class with dynamically assigned return value
    //          KEEP ONLY FOR REFERENCE
     //       how to call:
     //       String s = th.foo(String.class);
     //       Island s = th.foo(Island.class);
       public <T> T foo(Class<T> clazz, Object...args){
        
        ArrayList<T> listInJson = new ArrayList<T>();
        Island b = new Island(1,2,3);
        listInJson.add((T)b);               // add only cast as same type
        
        System.out.println(listInJson);
        return (T)b;            // careful what to return
    }
   */ 
    
///////////////////////////////////////////////////////////////////////////////       
    



