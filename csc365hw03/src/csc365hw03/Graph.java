package csc365hw03;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

/**
 * @author Brandon
 * Implementation of the edge-weighted Directed Acyclic Graph (DAG)
 */

public class Graph {
    private int V; // Vertex Set
    private LinkedList<Node> adj[]; // Adjacency list containing 'Node' objects, w/ vertex 'v' and weight
    private ArrayList<Path> longestPaths; // List representing the longest paths
    private ArrayList<Double> Lp = new ArrayList<>();  // List of the longest paths from source 's' to each job
    private int Lpn = -1;   // Longest Path node -- ending node from which the longest path resides
    
    public Graph(int v){
        this.V = v;
        adj = new LinkedList[V];
        longestPaths = new ArrayList<>();
        for(int i = 0; i < v; i++){
            adj[i] = new LinkedList<>();
        }
    }
    
    public int getLpn(){
        return Lpn;
    }
    
    // Add a new edge to the Graph
    public void newEdge(int u, int v, double w){
        Node n = new Node(v, w);
        adj[u].add(n);
    }
    
    public void topologicalSort (int v, Boolean visited[], Stack stack){
        // Mark current node as visited
        visited[v] = true;
        
        // Recur for all vertices adjacent to this vertex
        Iterator<Node> it = adj[v].iterator();
        while (it.hasNext()){
            Node node = it.next();
            if(!visited[node.getV()])
                topologicalSort(node.getV(), visited, stack);
        }
        stack.push(v);
    }
    
    
    // Algorithm for obtaining the longest path from source 's' to each job
    public void longestPaths(int s){
        Stack stack = new Stack();
        double dist[] = new double[V];
        ArrayList<Path> paths = new ArrayList<>();
        for(int i = 0; i < V; i++){
            paths.add(new Path());
        }
        paths.get(s).getJobs().add(s);
        paths.get(s).setStart(s);
        
         // Mark all the vertices as not visited 
        Boolean visited[] = new Boolean[V]; 
        for (int i = 0; i < V; i++) 
            visited[i] = false; 
        
        // Call topologicalSort() function to store Topological Sort starting from all vertices one by one
        for(int i = 0; i < V; i++)
            if(visited[i] == false)
                topologicalSort(i, visited, stack);
        //System.out.println("STACK: " + stack.toString()); // Debug
        
        // Initialize distances to all vertices as negative infinite and distance to source as 0
        for(int i = 0; i < V; i++)
            dist[i] = Integer.MIN_VALUE;
        dist[s] = 0.;
        
        // Process vertices in topological order
        while(stack.empty() == false){
            // Get next vertex from topological order
            int u = (int)stack.pop();
            // Update distances of all adjacent vertices
            Iterator<Node> it;
            if(dist[u] != Integer.MIN_VALUE){
                it = adj[u].iterator();
                while(it.hasNext()){
                    Node i = it.next();
                    if(dist[i.getV()] < dist[u] + i.getWeight()){
                        //System.out.println("ADDING WEIGHT FROM NODE " + u + " TO " + i.getV() + ": " + i.getWeight()); // Debug
                        dist[i.getV()] = dist[u] + i.getWeight();
                        paths.get(i.getV()).getJobs().addAll(paths.get(u).getJobs());
                        paths.get(i.getV()).getJobs().add(i.getV());
                    }
                } 
            }
        }
        
        // Print calculated longest distances
        for(int i = 0; i < V; i++){
            if(dist[i] == Integer.MIN_VALUE){
                System.out.println(i + " : -- ");
            }
            else{
//                System.out.println(i + " : " + dist[i]);
                Lp.add(dist[i]);
                paths.get(i).setTime(dist[i]);
                paths.get(i).setEnd(i);
            }
        }
        
        double longest = 0;
        for(int i = 0; i < Lp.size(); i++){
            if(Lp.get(i) > longest)
                longest = Lp.get(i);
        }
        for(int i = 0; i < V; i++){
            if(dist[i] == longest){
                Lpn = i;
            }
        }
        longestPaths = paths;
    }
    
    public void printLongestPaths(int s){
        System.out.println("LONGEST PATHS FROM SOURCE " + s + ": ");
        for(int i = 0; i < longestPaths.size(); i++){
            System.out.print(i + " : [ ");
            for(int j = 0; j < longestPaths.get(i).getJobs().size(); j++){
                System.out.print(longestPaths.get(i).getJobs().get(j) + " ");
            }
            System.out.println("]");
        }
    }
    
    public Path getLongestPath(){
        double l = 0;
        for(int i = 0; i < longestPaths.size(); i++){
            if(longestPaths.get(i).getTime() > l){
                l = longestPaths.get(i).getTime();
            }
        }
        for(int j = 0; j < longestPaths.size(); j++){
            if(l == longestPaths.get(j).getTime())
                return longestPaths.get(j);
        }
        return null;
    }
    
    public ArrayList<Path> getLongestPaths(){
        return longestPaths;
    }

}