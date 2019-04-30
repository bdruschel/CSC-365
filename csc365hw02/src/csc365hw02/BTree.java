package csc365hw02;

import java.io.IOException;
import java.util.Arrays;

/**
 * 
 * @author Brandon Druschel
 * BTree holds information and methods relevant to maintaining, modifying and
 * traversing a B-tree
 * 
 */
public class BTree {
    
    private Node root;  // root of the tree
    private int t;      // minimum degree
    int h = 0;          // current height
    int c = 0;          // counting integer

    public BTree(int t){
        this.t = t;
        root = new Node(true, t);
    }
    
    /**
     * Find all keys whose PID matches the input
     * @param n - Node to begin the search
     * @param id - PatientID to search for
     */
    public void searchPID(Node n, int id) {
        int i;
        File f2;
        for(i = 0; i < n.getN(); i++){
            f2 = new File(n.getKey(i));
            if(!n.isLeaf()){
                h++;
                searchPID(n.getChild(i), id);
                h--;
            }
            if(f2.getID() == id){
                c++;
                System.out.println("SearchPID: Found matching key " + i +  
                       " at height " + h + ": " + n.getKey(i));
            }
            
        }
        if(!n.isLeaf()){
            searchPID(n.getChild(i), id);
        }
    }
    
    /**
     * Traverse the tree, starting at a particular node
     * @param n - Node to begin traversing
     */
    public void traverse(Node n) {
        int i;
        for(i = 0; i < n.getN(); i++){
            if(!n.isLeaf()){
                // System.out.println("Traverse (h="+h+"): Node isn't leaf, recursing at child " + i + "...");
                h++;
                traverse(n.getChild(i));
                // System.out.println("Traverse (h="+h+"): Child traversed, returning to parent...");
                h--;
            }
            System.out.println("Traverse (h="+h+"): Key " + i + " = " 
                    + n.getKey(i));
        }
        
        if(!n.isLeaf()){
            // System.out.println("Traverse (h="+h+"): Node isn't leaf, recursing at child " + i + "...");
            h++;
            traverse(n.getChild(i));
        }
    }
    
    /**
     * Search for the key that matches String k
     * @param x - Node 
     * @param k - key to search for
     */
    public Entry search(Node x, String k){
        int i = 0;
        File f1 = new File(k);
        File f2 = new File(x.getKey(i));
        
        System.out.println("Search (h="+h+"): " + f1.getID() + " --> " + f2.getID());
        
        while(i < x.getN() && compareFiles(f1, f2 = new File(x.getKey(i))) == 1){
            System.out.println("Search (h="+h+"): " + f1.getID() + " >>> " + f2.getID());
            i++;
        }
        
        if(i < x.getN() && compareFiles(f1, f2 = new File(x.getKey(i))) == 0){
            System.out.println("Search (h="+h+"): Match found!! (" + k + ")"); // DEBUG
            return new Entry(x, i);
        }
        
        if(x.isLeaf()){
            System.out.println("Search (h="+h+"): Failed to find key " + k);
            return null;
        }
        
        h++;
        // System.out.println("Search (h="+h+"): Recursing..."); // DEBUG
        return search(x.getChild(i), k);
    }
    
    /**
     * Insert a new key into the tree.
     * @param k - key to insert
     */
    public void insert(String k){
        // System.out.println("Insert: Key " + k);
        if(root.isFull()){
            // System.out.println("Insert: Root is full. Splitting..."); // DEBUG
            Node s = new Node(false, t);
            s.setChild(0, root);
            s.split(0, root);
            
            File f1 = new File(k);
            File f2;
            int i = 0;
            if(compareFiles(f1, f2 = new File(s.getKey(0))) == 1){ // k > s.keys[0]
                i++;
            }
            insertNonfull(s.getChild(i), k);
            
            root = s;
        }
        else
            insertNonfull(root, k);
    }
    
    /**
     * Second insert method which accounts for nodes that aren't full
     * @param x - Node
     * @param k - key to insert 
     */
    public void insertNonfull(Node x, String k){
        int i = x.getN()-1;
        File f1 = new File(k);
        File f2;
        
        if(x.isLeaf()){
            while(i >= 0 && compareFiles(f1, f2 = new File(x.getKey(i))) == -1){ // k < keys[i]
                x.setKey(i+1, x.getKey(i));
                i--;
            }
            x.setKey(i+1, k);
            x.incN();
        }
        else{
            while(i >= 0 && compareFiles(f1, f2 = new File(x.getKey(i))) == -1){ // k < keys[i]
                i--;
            }

            if(x.getChild(i+1).isFull()){
                // System.out.println("Nonfull: Node detected as full. Splitting... "); // DEBUG
                x.split(i+1, x.getChild(i+1));
                if(compareFiles(f1, f2 = new File(x.getKey(i+1))) == 1) // k > keys[i+1]
                    i++;
            }
            insertNonfull(x.getChild(i+1), k);
        }
    }
    
    /**
     * Compare two file objects according to:
     * 1. Their PatiendID (xxxxxxx)
     * 2. Their month/day/year (mmddyy)
     * 3. Their time in hours/minutes/seconds (hhmmss)
     * 4. Their image code (ccccccc)
     * Used primarily for sorting keys when constructing the B-tree
     * @param a - First file
     * @param b - Second file
     */
    public static int compareFiles(File a, File b){
        if(a.getID() < b.getID())
            return -1;
        else if(a.getID() > b.getID())
            return 1;
        else if(a.getDate().getYear() < b.getDate().getYear())
            return -1;
        else if(a.getDate().getYear() > b.getDate().getYear())
            return 1;
        else if(a.getDate().getDay() < b.getDate().getDay())
            return -1;
        else if(a.getDate().getDay() > b.getDate().getDay())
            return 1;
        else if(a.getDate().getHour() < b.getDate().getHour())
            return -1;
        else if(a.getDate().getHour() > b.getDate().getHour())
            return 1;
        else if(a.getDate().getMin() < b.getDate().getMin())
            return -1;
        else if(a.getDate().getMin() > b.getDate().getMin())
            return 1;
        else if(a.getDate().getSec() < b.getDate().getSec())
            return -1;
        else if(a.getDate().getSec() > b.getDate().getSec())
            return 1;
        else if(a.getImageCode() < b.getImageCode())
            return -1;
        else if(a.getImageCode() > b.getImageCode())
           return 1;
        else
            return 0;
    }
    
    public Node getRoot(){
        return root;
    }
    public int getT(){
        return t;
    }
    public void setRoot(Node n){
        root = n;
    }
    public void setT(int i){
        t = i;
    }
        
}
