package csc365hw03;

import java.util.ArrayList;

/**
 *
 * @author Brandon
 * 
 */
public class Job {
    private int job;                // job number
    private double time;            // duration of job
    private ArrayList<Integer> mcb; // must complete before
    
    public Job(int j, double t){
        job = j;
        time = t;
        mcb = new ArrayList<>();
    }
    
    public int getJob() {
        return job;
    }

    public double getTime() {
        return time;
    }
    
    public ArrayList<Integer> getMcb() {
        return mcb;
    }
    
    public void setMcb(ArrayList<Integer> in) {
        mcb = in;
    }
    
    public String toString(){
        return Integer.toString(job);
    }
    
}
