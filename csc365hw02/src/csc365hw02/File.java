package csc365hw02;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author Brandon Druschel
 * File objects track all information given from the name of an image file.
 * 
 */
public class File {
    
    private int pID;            // patientID - xxxxxxx
    private DateClass dClass;   // date and time - MMddyyhhmmss
    private int iCode;          // image code - ccccccc
    private String name;        // name of file - xxxxxxxMMddyyhhmmss.ccccccc
    
    public File(int id, DateClass dc, int ic){
        pID = id;
        dClass = dc;
        iCode = ic;
        
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("MMddyyhhmmss");
        sb.append(String.format("%07d", id));
        sb.append(sdf.format(dc.getDate()));
        sb.append(".").append(String.format("%07d", ic));
        
        name.equals(sb.toString());
    }
    
    public File(String n){
        parseFName(n);
    }
    
    public void parseFName(String n){
        String x = n.substring(0, 7);
        pID = Integer.parseInt(x);
        
        int month = Integer.parseInt(n.substring(7, 9));
        int day = Integer.parseInt(n.substring(9, 11));
        int year = Integer.parseInt(n.substring(11, 13));
        int hour = Integer.parseInt(n.substring(13, 15));
        int min = Integer.parseInt(n.substring(15, 17));
        int sec = Integer.parseInt(n.substring(17, 19));
        
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR, hour);
        c.set(Calendar.MINUTE, min);
        c.set(Calendar.SECOND, sec);
        int doy = c.get(Calendar.DAY_OF_YEAR);
        
        dClass = new DateClass(year, doy, hour, min, sec);
        dClass.setDate(c.getTime());
        iCode = Integer.parseInt(n.substring(20, 27));
        
        name = n;
    }
    
    public String toString(){
        return "- - - FILE - - -\n"
             + "patientID: " + pID + "\n"
             + "imageCode: " + iCode + "\n"
             + dClass.toString() + "\n";
    }
    
    public int getID(){
        return pID;
    }
    
    public DateClass getDate(){
        return dClass;
    }
    
    public int getImageCode(){
        return iCode;
    }
    
    public String getName(){
        return name;
    }
    
}
