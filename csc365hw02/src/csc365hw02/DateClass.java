/**
 *
 * @author Brandon Druschel
 * DateClass holds all time and date information for a File object, allowing
 * for easier comparisons between filename attributes.
 * 
 */

package csc365hw02;

import java.util.Calendar;
import java.util.Date;

public class DateClass {
    private final Calendar cal = Calendar.getInstance();
    private Date date;
    private int year;
    private int day;
    private int hour;
    private int min;
    private int sec;
    
    public DateClass(int y, int d, int h, int m, int s){
        cal.set(Calendar.YEAR, y);
        cal.set(Calendar.DAY_OF_YEAR, d);
        cal.set(Calendar.HOUR_OF_DAY, h);
        cal.set(Calendar.MINUTE, m);
        cal.set(Calendar.SECOND, s);
        
        year = y;
        day = d;
        hour = h;
        min = m;
        sec = s;
    }
    
    public String toString(){
        return "- - - DATECLASS - - -\n"
             + "Year: " + year + "\n"
             + "Day: " + day + "\n"
             + "Hour: " + hour + "\n"
             + "Min: " + min + "\n"
             + "Sec: " + sec + "\n";
    }
    
    public Date getDate(){
        return date;
    }
    
    public void setDate(Date d){
        date = d;
    }
    
    public Calendar getCal(){
        return cal;
    }
    
    public int getYear(){
        return year;
    }
    
    public int getDay(){
        return day;
    }
    
    public int getHour(){
        return hour;
    }
    
    public int getMin(){
        return min;
    }
    
    public int getSec(){
        return sec;
    }
}
