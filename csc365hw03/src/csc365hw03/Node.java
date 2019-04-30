package csc365hw03;

/**
 *
 * @author Brandon
 * An adjacency list node.
 * 
 */

public class Node {
    private int v;  // vertex to which the edge connects
    private double weight; // weight of the edge (duration)
    
    public Node(int v, double w){
        this.v = v;
        this.weight = w;
    }
    
    public int getV(){
        return v;
    }
    
    public double getWeight(){
        return weight;
    }
}
