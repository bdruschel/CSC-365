package csc365hw03;

/**
 *
 * @author Brandon
 */

public class SchedJob {
    private int job;
    private double start;
    private double finish;
    private boolean chk;
    
    public SchedJob(int j, double s, double f){
        job = j;
        start = s;
        finish = f;
        chk = false;
    }

    public int getJob() {
        return job;
    }

    public void setJob(int job) {
        this.job = job;
    }

    public double getStart() {
        return start;
    }

    public void setStart(double start) {
        this.start = start;
    }

    public double getFinish() {
        return finish;
    }

    public void setFinish(double finish) {
        this.finish = finish;
    }

    public boolean isChk() {
        return chk;
    }

    public void setChk(boolean chk) {
        this.chk = chk;
    }    
    
    public double getTime(){
        return finish - start;
    }
    
    public String toString(){
        return job + " : " + start + " .. " + finish;
    }
    
}
