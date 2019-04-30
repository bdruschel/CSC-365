/**
 * @author Brandon Druschel
 */

/**
 * 
 * CSC 365 PROJECT 3
 * 
 * PROBLEM: 
 * Given a set of jobs of specified duration to be completed, with 
 * precedence constraints that specify that certain jobs have to be completed
 * before certain other jobs are begun, how can we schedule the jobs on 
 * identical processors (as many as needed) such that they are all completed 
 * in the minimum amount of time while still respecting the constraints?
 * 
 * SOLUTION: 
 * Formulate as "longest paths problem" in an edge-weighted Directed
 * Acyclic Graph (DAG). 
 * Create a DAG with a source 's' and a sink 't', and two vertices for each job 
 * (a start and an end vertex). For each job, add an edge from its start index
 * to its end vertex, with weight equal to its duration.
 * 
 * This program will implement Topological Sorting
 * 
 */

package csc365hw03;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class Main {
    
    // Parse an array of integers, returning as an ArrayList
    public static ArrayList<Integer> parseIntArray(String[] s){
        ArrayList<Integer> lst = new ArrayList<>();
        for(int i = 0; i < s.length; i++){
            lst.add(Integer.parseInt(s[i]));
        }
        return lst;
    }
    
    // Read from input file (input.txt)
    public static ArrayList<Job> readFile(){
        ArrayList<Job> jobs = new ArrayList<>();
        try{
            FileInputStream fstream = new FileInputStream("C:\\Users\\Brandon\\OneDrive\\School\\Semester 7\\CSC 365\\Workspace\\csc365hw03\\src\\csc365hw03\\input.txt");
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(" ");
                Job j = new Job(Integer.parseInt(tokens[0]), Double.parseDouble(tokens[1]));
                if(tokens.length == 3){
                    String[] mcbs = tokens[2].split(",");
                    j.setMcb(parseIntArray(mcbs));
                }
                jobs.add(j);
            }
            in.close();
        } catch (Exception e){
            System.err.println("Error: " + e.getMessage());
        }
        return jobs;
    }
    
    // Generate a set of random jobs, for testing random input.
    public static ArrayList<Job> randomJobs(){
        Random r = new Random();
        ArrayList<Job> jobs = new ArrayList<>();
        int numJobs = r.nextInt((15 - 2) + 1) + 2;
        double time;
        for(int i = 0; i < numJobs; i++){
            time = 0.1 + (200 - 0.1) * r.nextDouble();
            time = Math.round(time * 10.0) / 10.0;
            Job j = new Job(i, time);
            jobs.add(j);
        }
        
        // 'Must complete before' condition hasn't been randomized optimally
        for(int i = 0; i < jobs.size(); i++){
            int numMcb = r.nextInt(4);
            for(int j = 0; j < numMcb; j++){
                int randJob = r.nextInt(jobs.size());
                while(randJob == jobs.get(i).getJob())
                    randJob = r.nextInt(jobs.size());
                if(!jobs.get(i).getMcb().contains(randJob))
                    jobs.get(i).getMcb().add(randJob);
            }
        }
        return jobs;
    }
    
    // Print the list of jobs -- testing
    public static void printJobs(ArrayList<Job> j){
        for(int i = 0; i < j.size(); i++){
            System.out.print(j.get(i).getJob() + " " + j.get(i).getTime() + " ");
            for(int k = 0; k < j.get(i).getMcb().size(); k++){
                System.out.print(j.get(i).getMcb().get(k) + " ");
            }
            System.out.println("");
        }
    }
    
    // Add all edges from given input (jobs) to Graph g
    public static void addEdges(Graph g, ArrayList<Job> jobs, int l){
        // Source and Sink
        int source = 2*l;
        int sink = 2*l + 1;
        for(int i = 0; i < jobs.size(); i++){
            double w = jobs.get(i).getTime();
            g.newEdge(source, i, 0.0);
            g.newEdge(i+l, sink, 0.0);
            g.newEdge(i, i+l, w);
            
            // The precedence constraints
            for(int j = 0; j < jobs.get(i).getMcb().size(); j++){
                int v = jobs.get(i).getMcb().get(j);
                g.newEdge(l+i, v, 0.0);
            }
        }
    }
    
    // Generate a partition w/ the longest path -- the most important partition
    public static Partition generateLPP(ArrayList<SchedJob> s, Partition part, double e){
        ArrayList<SchedJob> sched = s;
        Partition p = part;
        double end = e;
//        System.out.println("Finish = " + e); // DEBUG
        for(int i = 0; i < sched.size(); i++){
//            System.out.println("Comparing: " + sched.get(i).getFinish() + "=" + end + "? false" + "=" + sched.get(i).isChk() + "?"); // DEBUG
            if(sched.get(i).getFinish() == end && sched.get(i).isChk() == false){
//                System.out.println("YES!"); // Debug
                p.addJob(sched.get(i));
                sched.get(i).setChk(true);
                generateLPP(sched, p, sched.get(i).getStart());
            }
        }
        // If the whole schedule should be traversed once we reach the first job,
        // since theoretically no job should finish with a time of zero.
        
        // Fix the order of jobs
        ArrayList<SchedJob> j = new ArrayList<>();
        for(int i = p.getJobs().size()-1; i >= 0; i--){
            j.add(p.getJobs().get(i));
        }
        p.setJobs(j);
        // System.out.println(p.toString()); // DEBUG
        return p;
    }
    
    // Ensure the entire schedule has been checked (every job within the schedule is in some partition)
    public static boolean isChk(ArrayList<SchedJob> sched){
        for(int i = 0; i < sched.size(); i++){
            if(!sched.get(i).isChk())
                return false;
        }
        return true;
    }
    
    // Sort the schedule array by length of the job (ascending)
    public static ArrayList<SchedJob> sortSched(ArrayList<SchedJob> sched){ 
        int n = sched.size(); 
        for(int i = 0; i < n-1; i++){ 
            int min_idx = i; 
            for(int j = i+1; j < n; j++){
                if(sched.get(j).getTime() < sched.get(min_idx).getTime()) 
                    min_idx = j; 
            }
            SchedJob temp = sched.get(min_idx);
            sched.set(min_idx, sched.get(i));
            sched.set(i, temp); 
        } 
        return sched;
    } 

    public static void main(String[] args) {
        ArrayList<Job> jobs = readFile(); // List of all the jobs, read from the txt file
//        ArrayList<Job> jobs = randomJobs(); // Random list of jobs, for testing
//        printJobs(jobs);
        int n = jobs.size(); // # of jobs
        int source = 2*n;
        int sink = 2*n + 1;
        
        Graph g = new Graph(2*n + 2);          // DAG with 22 vertices
        addEdges(g, jobs, n);                // Add edges to the graph
        g.longestPaths(source);
        
        // Make a list of Path objects representing the longest paths in G
        ArrayList<Path> lps = g.getLongestPaths();
        
        // Array of SchedJob objects represents the schedule itself, for traversal
        ArrayList<SchedJob> schedule = new ArrayList<>();
        
        System.out.println(" Job   Start  Finish");
        System.out.println("--------------------");
        for (int i = 0; i < n; i++) {
            System.out.printf("%4d %7.1f %7.1f\n", i, lps.get(i).getTime(), lps.get(i+n).getTime());
            schedule.add(new SchedJob(i, lps.get(i).getTime(), lps.get(i+n).getTime()));
        }
        System.out.printf("Finish time: %7.1f\n", lps.get(sink).getTime());
        System.out.println("");
        
        ArrayList<Partition> partitions = new ArrayList<>();
        
        // Find latest finish 
        for(int i = 0; i < schedule.size(); i++){
            if(schedule.get(i).getFinish() == lps.get(sink).getTime()){
                Partition p = new Partition(partitions.size());
                p.addJob(schedule.get(i));
                partitions.add(p);
                schedule.get(i).setChk(true);
                break;
            }
        }
        
        // Generate the longest path partition
        partitions.set(0, generateLPP(schedule, partitions.get(0), partitions.get(0).getJobs().get(0).getStart()));
        
        ArrayList<SchedJob> sortedSched = sortSched(schedule);
        
        // Generate the rest of the partitions
        boolean c = false;
        while(!isChk(sortedSched)){
            for(int i = 0; i < sortedSched.size(); i++){ // Traverse schedule
                if(!sortedSched.get(i).isChk()){
                    for(int j = 0; j < partitions.size(); j++){ // Traverse partitions
                        // Check for overlap 
                        for(int k = 0; k < partitions.get(j).getJobs().size(); k++){ // Traverse jobs in partition
                            c = false;
                            double sFinish = sortedSched.get(i).getFinish();
                            double sStart = sortedSched.get(i).getStart();
                            double pFinish = partitions.get(j).getJobs().get(k).getFinish();
                            double pStart = partitions.get(j).getJobs().get(k).getStart();
                            if( (sStart >= pStart && sFinish <= pFinish) ||  
                                (sStart <= pStart && sFinish >= pStart) ||
                                (sStart <= pFinish && sFinish >= pFinish) || 
                                (sStart <= pStart && sFinish >= pFinish )){
                                c = true;
                                break; // Overlap detected. Break out of partition's jobs.
                            }
                        }
                        if(c)
                            continue;
                        else{
                            partitions.get(j).addJob(sortedSched.get(i));
                            sortedSched.get(i).setChk(true);
                            c = false;
                            break;
                        }
                        // Move on to next partition...
                    }
                    if(c){ // Can't find ANY partition to fit job 'i' in
                            Partition np = new Partition(partitions.size());
                            np.addJob(sortedSched.get(i));
                            sortedSched.get(i).setChk(true);
                            partitions.add(np);
                            c = false;
                    }
                }
            }
        }
        
        // Generate start times for final output
        ArrayList<Double> startTimes = new ArrayList<>();
        for(int i = 0; i < partitions.get(0).getJobs().size(); i++){
            startTimes.add(partitions.get(0).getJobs().get(i).getStart());
        }
        startTimes.add(lps.get(sink).getTime());
        
        System.out.println("START TIMES: " + startTimes.toString());
        System.out.println("");
        
        System.out.println("ALL PARTITIONS");
        for(int i = 0; i < partitions.size(); i++){
            System.out.println(partitions.get(i).toString());
        }
        System.out.println("");
        
        // Final Output (Table)
        StringBuilder border = new StringBuilder();
        System.out.print("  Start\n  Times   ");
        for(int i = 0; i < partitions.size(); i++){
            System.out.print("P" + (partitions.get(i).getN()+1) + "  ");
            border.append("----");
        }
        System.out.print("\n----------");
        System.out.println(border.toString());
        int count = 0;
        for(int i = 0; i < startTimes.size(); i++){
            System.out.printf("%7.1f ", startTimes.get(i));
            for(int j = 0; j < partitions.size(); j++){
                for(int k = 0; k < partitions.get(j).getJobs().size(); k++){
                    if(partitions.get(j).getJobs().get(k).getStart() == startTimes.get(i)){
                        count++;
                        System.out.printf("%4d", partitions.get(j).getJobs().get(k).getJob());
                        break;
                    }
                }
                if(count == 0){
                    System.out.print("  --");
                }
                count = 0;
            }
            System.out.println("");
        }
        
    }
    
}
