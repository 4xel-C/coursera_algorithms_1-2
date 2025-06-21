import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    
    private static int R = 256; // Radix.
    
    public static void encode() {
        
        // sequence of characters.
        int[] sequence = new int[R];
        
        // initialize sequence:
        for (int i = 0; i < R; i++) {
            sequence[i] = i;
        }
        
        // read the standard input char by char.
        while(!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            
            // Find the character in the sequence
            for (int i = 0; i < R; i++) {
                if (sequence[i] == c) {
                    
                    // shift all the elements to the right to position the char in the front.
                    for (int j = i; j > 0; j--) {
                        sequence[j] = sequence[j-1];
                    }
                    sequence[0] = c;
                    
                    BinaryStdOut.write(i, 8);
                    break;
                } // end if.
            } // end for.
        } // end while.
        BinaryStdIn.close();
        BinaryStdOut.close();
    }
    
    public static void decode() {
        
        // sequence of characters.
        int[] sequence = new int[R];
        
        // initialize sequence:
        for (int i = 0; i < R; i++) {
            sequence[i] = i;
        }
        
        while(!BinaryStdIn.isEmpty()) {
            int cidx = BinaryStdIn.readChar();
            
            // get the character
            int c = sequence[cidx];
            
            // shift the characters to the right.
            for (int j = cidx; j > 0; j--) {
                sequence[j] = sequence[j-1];
            }
            sequence[0] = c;
            BinaryStdOut.write(c, 8); 
        }
        
        // close the Binarywriter.
        BinaryStdIn.close();
        BinaryStdOut.close();
    }
    
    
    public static void main(String[] args) {
        if (args[0].equals("-"))
            MoveToFront.encode();
        if (args[0].equals("+"))
            MoveToFront.decode();
        return;
    }

}
