package csc365hw02;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 
 * @author Brandon Druschel
 * Node objects hold information important to an individual node within
 * a B-tree. Internal/external nodes are determined by the 'leaf' boolean.
 * 
 */
public class Node {

    private int n;           // # of keys
    private String[] keys;   // the keys, AKA file names
    private Node[] children; // pointers to children
    private boolean leaf;    // is node a leaf?
    private int t;           // minimum degree
            
    public Node(boolean l, int t){
        n = 0;
        this.t = t;
        leaf = l;
        keys = new String[(2*t)-1];
        if(leaf == false){
            children = new Node[2*t];
        }
    }
    
    /**
     * @param i - integer index of the child
     * @param y - parent node
     */
    public void split(int i, Node y) {
//        System.out.println("Split: At index " + i + ", running on Node..."
//                + "\nSplit: " + x); // DEBUG
        Node z = new Node(y.isLeaf(), t);
        z.setN(t-1);
        
        for(int j = 0; j < t-1; j++){
            z.setKey(j, y.getKey(j+t));
        }
        
        if(!y.isLeaf()){
            for(int j = 0; j < t; j++){
                z.setChild(j, y.getChild(j+t));
            }
        }
        
        y.setN(t-1);
        
        for(int j = n; j >= i+1; j--){
            children[j+1] = children[j];
        }
        
        children[i+1] = z;
        
        for(int j = n-1; j >= i; j--){
            keys[j+1] = keys[j];
        }
        
        keys[i] = y.getKey(t-1);
        n++;
    }
    
    public String toString(){
        return "{{NODE:" + "#KEYS=" + n + "||KEYS LIST=" + 
               Arrays.toString(keys) + "||<CHILDREN=" + Arrays.toString(children) +">||LEAF=" + leaf 
                + "}}";
    }
    
    public boolean isFull(){
        if(n == (2*t)-1){
            return true;
        }
        return false;
    }
    public int getMedKeys(){
        return keys.length / 2;
    }
    public int getN(){
        return n;
    }
    public void incN(){
        n++;
    }
    public String[] getKeys(){
        return keys;
    }
    public String getKey(int i){
//        if(keys[i] == null)
//            return "0000000000000000000.0000000";
        return keys[i];
    }
    public Node[] getChildren(){
        return children;
    }
    public Node getChild(int i){
        return children[i];
    }   
    public boolean isLeaf(){
        return leaf;
    }
    public void setN(int i){
        n = i;
    }
    public void setKey(int i, String s){
        keys[i] = s;
    }
    public void setChild(int i, Node n){
        children[i] = n;
//        System.out.println("setChild: At index " + i + ", set to " + n + 
//        "\nCurrent Children: " + Arrays.toString(children)); // DEBUG
    }
    public void setLeaf(Boolean b){
        leaf = b;
    }
    public int getNumKeys(){
        return keys.length;
    }
        
}
