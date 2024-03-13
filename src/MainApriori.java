import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Scanner;

import Algorithms.AlgoApriori;
public class MainApriori {
    public static void main(String [] arg) throws IOException {
        long startTimestamp;
        long endTimestamp;
        String input = fileToPath("contextPasquier99.txt");
        String output = ".//output.txt";
        double minsup = 0.4;

        Scanner sc = new Scanner(System.in);
        System.out.println("Would you like to print the output on the terminal? \n(Yes:1    No:2)");
        int n = sc.nextInt();
        AlgoApriori algo = new AlgoApriori();

        startTimestamp = System.currentTimeMillis();
        algo.runAlgorithm(minsup, input, output,n);
        endTimestamp = System.currentTimeMillis();

        algo.printStats();
        System.out.println(" Total time to run ~ " + (endTimestamp - startTimestamp) + " ms");


    }
    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainApriori.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(),"UTF-8");
    }
}