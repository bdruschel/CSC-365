package csc365hw01;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;

/**
 *
 * @author Brandon Druschel
 */ 

public class Main {

// SIEVE OF ERATOSTHEMES
// https://en.wikipedia.org/wiki/Sieve_of_Eratosthenes
    private static BitSet eratosthenes(int n){
        BitSet a = new BitSet();
        int i;
        int j;
        // create list of consecutive integers from 2->n
        for (i = 2; i <= n; i++) {
            a.set(i, true); // all slots (besides 0, 1) are initially true
        }
        int p = 2; // Let p = 2, the smallest pime #
        while (p * p < n) {
            j = p + p; // let j = 2p
            while (j <= n) {
                a.set(j, false); // mark multiples of p in the list
                j = j + p; // count to n from 2p in increments of p
            }
            p++;
            while (a.get(p) == false) {
                p++; // find the first #>p in the list that is not marked
            }
        }
        return a;
    }
    
// SIEVE OF SUNDARAM
// https://en.wikipedia.org/wiki/Sieve_of_Sundaram
    private static BitSet sundaram(int n) {
        BitSet L = new BitSet();
        int m = n / 2;
        int i;
        int j = 1;
        for (i = 1; i <= m; i++) {
            L.set(i, true); // start w/ list of ints from 1->n
        }
        for (i = 1; i < m; i++) {
            for (j = i; j <= (m - i) / ((2 * i) + 1); j++) {
                L.set(i + j + (2 * i * j), false); // remove all numbers of the form i+j+2ij
            }
        }
        return L;
    }
    
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        int t = 500000;
        BitSet b;

        long start = System.currentTimeMillis(); // start time of eratosthenes()
        b = eratosthenes(t);
        StringBuilder sb = new StringBuilder();
        int c = 0;
        for (int i = 0; i < b.length(); i++) {
            if (b.get(i) == true) {
                if(c == 10){
                    sb.append(i + "\n");
                    c = 0;
                }
                else{
                    sb.append(i + " "); // add primes to string
                    c++;
                }

            }
        }
        
        PrintWriter w = new PrintWriter("SieveOfEratosthenes.txt", "UTF-8");

        w.println("ERATOSTHENES:\n" + sb);
        long end = System.currentTimeMillis(); // end time of eratosthenes()
        long timeElapsed = end - start;
        w.println("Running Time: " + timeElapsed + " ms");
        w.close();
        
        
        start = System.currentTimeMillis(); // start time of sundaram()
        BitSet s;
        s = sundaram(t);
        StringBuilder sb2 = new StringBuilder();
        c = 1;
        for (int i = 0; i < s.length(); i++) {
            if (s.get(i) == true) {
                // sieve of sundaram doubles i, then incrememnts by one to get
                // the prime number
                // add primes to string
                if(c == 10){
                    if((i * 2) + 1 < t){
                        sb2.append((i * 2) + 1 + "\n");
                        c = 0;
                    }
                }
                else{
                    if((i * 2) + 1 < t) {
                        sb2.append((i * 2) + 1 + " "); // add primes to string
                        c++;
                    }
                }
            }
        }
        
        PrintWriter w2 = new PrintWriter("SieveOfSundaram.txt", "UTF-8");

        // sundaram algorithm doesn't include 2 in its list of primes
        w2.println("SUNDARAM:\n2 " + sb2);
        end = System.currentTimeMillis(); // end time of sundaram()
        timeElapsed = end - start;
        w2.println("Running Time: " + timeElapsed + " ms");  
        w2.close();
    }
    
}
