/**
 * @author Brandon Druschel
 */

/**
 * 
 * CSC 365 PROJECT 2
 * 
 * Problem: The Radiology Department decides to create a file server with 
 * optimal access to medical imaging files of the patients, with the restriction 
 * that each folder can store N files maxim. Each imaging file name has a coding 
 * scheme, of the type xxxxxxxmmddyyhhmmss.ccccccc where the x digits will 
 * represent the patientID, mmddyy - the month, day and year when the study took 
 * place, hhmmss the hour, minute and second when the imaging study acquisition 
 * starts, and ccccccc is the image code. 
 * 
 * Solution: B-tree implementation
 * 
 */

package csc365hw02;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Csc365hw02 {

// Generate a random date using the appropriate mmddyy format
    private static DateClass randomDate(){
        Random r = new Random();
        int y = r.nextInt(18);
        int d = r.nextInt(365) + 1;
        
        int h = r.nextInt(23);
        int min = r.nextInt(59);
        int s = r.nextInt(59);
        
        DateClass da = new DateClass(y, d, h, min, s);
        da.setDate(da.getCal().getTime());
        
        return da;
    }
    
/**
 * Create an array of random file names -- for testing
 * @param n - Number of file objects to generate 
 */
    private static ArrayList<File> randomFiles(int n){
        ArrayList<File> op = new ArrayList<>();        
        
        for(int i = 0; i < n; i++){
            StringBuilder sb = new StringBuilder();
            
            // patiendID: xxxxxxx
            Random r = new Random();
            int x = r.nextInt(9999999);
            sb.append(String.format("%07d", x));
            // date & time: mmddyyhmmss
            SimpleDateFormat sdf = new SimpleDateFormat("MMddyyhhmmss");
            sb.append(sdf.format(randomDate().getDate()));
            // image code: ccccccc
            int c = r.nextInt(9999999);
            sb.append(".").append(String.format("%07d", c));
            
            op.add(new File(sb.toString()));
        }

        return op;
    }
    
// Verify that the input is a proper integer
    private static boolean checkInt(String t){
        int i;
            try{
                i = Integer.parseInt(t);  
            }
            catch(NumberFormatException nfe){
                return false;
            }
            return true;
    }
        
/**
 * Simply stores the command interface
 * @param tree - the B-tree
 * @param in - user input
 */
    private static void cmd(BTree tree, Scanner in){
        for(;;){
            System.out.println("- - -");
            System.out.println("Enter a command (searchPID <PatientID>, search <FileName>, "
                    + "insert <FileName>, traverse, help, quit):");
            System.out.print(" > ");
            String input = in.nextLine();

            if (input.contains(" ")) {
                String command = input.substring(0, input.lastIndexOf(" "));
                String name = input.substring(input.indexOf(" ") + 1);
                
                if (command.equals("searchPID")) {
                    if(!checkInt(name)){
                        System.out.println("ERROR: Input is not a valid number!");
                    }
                    else{
                        tree.searchPID(tree.getRoot(), Integer.parseInt(name));
                        if(tree.c == 0){
                            System.out.println("SearchPID: Can't find any matching key (" + name + ")");
                        }
                        tree.c = 0;
                        tree.h = 0;
                    }
                }
                else if(command.equals("insert")){
                    if(name.matches("\\d{19}.\\d{7}")){
                        tree.insert(name);
                    }
                    else
                        System.out.println("ERROR: Improper name format (xxxxxxxmmddyyhhmmss.ccccccc).");
                }
                else if (command.equals("search")){
                    if(name.matches("\\d{19}.\\d{7}")){
                        tree.search(tree.getRoot(), name);
                        tree.h = 0;
                    }
                    else
                        System.out.println("ERROR: Improper name format (xxxxxxxmmddyyhhmmss.ccccccc).");
                }
                else{
                    System.out.println("Invalid command -- " + command);
                }
            }
            else if (input.equals("traverse")){
                System.out.println("\nTraversing tree...\nNOTE: h = height");
                tree.traverse(tree.getRoot());
                System.out.println("Tree successfully traversed.");
            }
            else if (input.equals("help")){
                System.out.println("...\nCOMMAND LIST (case-sensitive):");
                System.out.println(" searchPID <PatientID>: Find all files whose PatientID matches the input.");
                System.out.println(" search <FileName>: Find a file whose name directly matches the input.");
                System.out.println(" insert <FileName>: Insert the specified file name as a new key within "
                        + "the B-tree. Appropriate date and time format expected.");
                System.out.println(" traverse: Traverse the entire B-tree, printing all file names.");
                System.out.println(" quit: Quit program.");
            }
            else if(input.equals("quit")){
                System.out.println("Goodbye.");
                break;
            }
            else{
                System.out.println("Invalid command -- " + input);
            }
        }
    }
    
    public static void main(String[] args) {
        int f = 10000; // number of random files to generate
        int t = 2;  // default minimum degree of the tree

        Scanner in = new Scanner(System.in);
        
        System.out.print("Welcome. ");
        for(;;){
            System.out.println("Please enter the desired minimum degree: ");
            System.out.print(" > ");
            
            String s = in.nextLine();
            
            if(!checkInt(s)){
                System.out.println("ERROR: Input is not a valid number!");
            }
            else if(!(Integer.parseInt(s) >= 2)){
                System.out.println("ERROR: Minimum degree must be >= 2!");
            }
            else{
                t = Integer.parseInt(s);
                break;
            }
        }

        System.out.println("");
        
        BTree tree = new BTree(t);
        ArrayList<File> rFiles = randomFiles(f);
        
        System.out.println("Random files generated.");
        System.out.println("Inserting keys...");
        
        for(int i = 0; i < rFiles.size(); i++){
            tree.insert(rFiles.get(i).getName());
        }
        
//         Testing with a fixed set of keys
//        tree.insert("8675309032516100711.3645197");
//        tree.insert("8675309032516100711.7917334");
//        tree.insert("8675309101212121433.9221144");
//        tree.insert("7776884032107104246.5582512");
//        tree.insert("8675309032101014121.2953531");
        
        System.out.println("Keys inserted.");
        System.out.println("Minimum degree =  " + tree.getT());
        System.out.println("B-tree is ready.");
        
        cmd(tree, in); // Start command interface
        
        System.out.println("");
        
        // Test search function on all generated random files
//        tree.h = 0;
//        for(int i = 0; i < rFiles.size(); i++){
//            tree.search(tree.getRoot(), rFiles.get(i).getName());
//            tree.h = 0;
//        }

    }
    
}
