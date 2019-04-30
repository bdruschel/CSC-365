package csc365hw02;

/**
 * 
 * @author Brandon Druschel
 * The Entry object represents the ordered pair (y, i) which is returned in
 * B-TREE-SEARCH.
 * 
 */
public class Entry {
    private Node y; // Node
    private int i;  // index
    
    public Entry(Node a, int b) {
        y = a;
        i = b;
    }
    
    public String toString(){
        return "= = = ENTRY = = =\n" +
               y + "\n\n" +
               "Index: " + i;
    }
}
