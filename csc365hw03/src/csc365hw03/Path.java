package csc365hw03;

import java.util.ArrayList;

/**
 * @author Brandon
 */

public class Path {
    private ArrayList<Integer> jobs; // List of each job, in order
    private int start;
    private int end;
    private double time; // Total length of the path from start to finish
    
    public Path(){
        jobs = new ArrayList<>();
        time = 0;
    }
    
    public ArrayList<Integer> getJobs(){
        return jobs;
    }
    
    public double getTime(){
        return time;
    }
    
    public void setTime(double t){
        time = t;
    }
    
    public int getStart(){
        return start;
    }
    
    public void setStart(int s){
        start = s;
    }
    
    public int getEnd(){
        return end;
    }
    
    public void setEnd(int e){
        end = e;
    }
     
    public String toString(){
        return start + "->" + end + ": " +jobs.toString() + " ... " + time;
    }
}
