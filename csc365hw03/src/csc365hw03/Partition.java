package csc365hw03;

import java.util.ArrayList;

/**
 *
 * @author Brandon
 * 
 */

public class Partition {
    private int n; // Partition Number
    private ArrayList<SchedJob> jobs = new ArrayList<>(); // jobs in the partition

    public Partition(int n) {
        this.n = n;
    }

    /**
     * @return Partition Number
     */
    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public ArrayList<SchedJob> getJobs() {
        return jobs;
    }

    public void setJobs(ArrayList<SchedJob> jobs) {
        this.jobs = jobs;
    }
    
    public void addJob(SchedJob j){
        jobs.add(j);
    }
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        for(int i = 0; i < jobs.size(); i++){
            if(i == jobs.size() - 1)
                sb.append(jobs.get(i).getJob() + " ");
            else
                sb.append(jobs.get(i).getJob() + ", ");
        }
        sb.append("]");
        return "P" + (n+1) + ": " + sb.toString();
    }
    
}
