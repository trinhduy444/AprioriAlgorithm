package Algorithms;

import memory.Memorytools;
import patterns.Itemsets;

import patterns.Itemset;
import java.io.*;
import java.util.*;

public class AlgoApriori {
    protected int k;
    protected int totalCandidateCount = 0;

    private int databaseSize;
    private int minsupRelative;
    private List<int[]> database = null;
    protected Itemsets patterns = null;
    BufferedWriter writer = null;
    private int maxPatternLength = 10000;

    public AlgoApriori() {
    }
    public Itemsets runAlgorithm(double minsup, String input, String output,int n) throws IOException {

        //luu ket qua vao file
        patterns = null;
        writer = new BufferedWriter(new FileWriter(output));

        totalCandidateCount = 0;
        databaseSize = 0;

        Memorytools.getInstance().reset();

        Map<Integer, Integer> mapItemCount = new HashMap<Integer, Integer>(); //bien dem support moi item

        database = new ArrayList<int[]>(); //tao database de luu

        BufferedReader reader = new BufferedReader(new FileReader(input));
        String line;
        //doc tung dong
        while (((line = reader.readLine()) != null)) {
            if (line.isEmpty() == true ||
                    line.charAt(0) == '#' || line.charAt(0) == '%'
                    || line.charAt(0) == '@') {
                continue;
            }
            String[] lineSplited = line.split(" ");

            // tao transaction luu ket qua tung dong
            int transaction[] = new int[lineSplited.length];

            for (int i=0; i< lineSplited.length; i++) {
                Integer item = Integer.parseInt(lineSplited[i]);
                transaction[i] = item;
                Integer count = mapItemCount.get(item);
                if (count == null) {
                    mapItemCount.put(item, 1);
                } else {
                    mapItemCount.put(item, ++count);
                }
            }
            database.add(transaction);
            databaseSize++;
        }
        reader.close();
        // tinh minsup
        this.minsupRelative = (int) Math.ceil(minsup * databaseSize);
        // Print

        // Candicate size k = 1
        k = 1;
        List<Integer> frequent1 = new ArrayList<Integer>();
        for(Map.Entry<Integer, Integer> entry : mapItemCount.entrySet()){
            if(entry.getValue() >= minsupRelative){
                frequent1.add(entry.getKey());
                saveItemsetToFile(entry.getKey(), entry.getValue(),n);
            }
        }
        mapItemCount = null;
        Collections.sort(frequent1, new Comparator<Integer>() {
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });

        if(frequent1.size() == 0 || maxPatternLength <= 1){
            Memorytools.getInstance().checkMemory();
            if(writer != null){
                writer.close();
            }
            return patterns;
        }
        totalCandidateCount += frequent1.size();
        //Candicate size k > 1
        List<Itemset> level = null;
        k = 2;
        do{
            Memorytools.getInstance().checkMemory();

            List<Itemset> candidatesK;

            if(k ==2){
                candidatesK = candidate2(frequent1);
            }else{
                candidatesK = candidateSizeK(level,frequent1);
            }
            totalCandidateCount += candidatesK.size();
            // Kiem tra support
            checkSupport(candidatesK,database);

            level = new ArrayList<Itemset>();
            if(k < maxPatternLength +1){

                for (Itemset candidate : candidatesK) {
                    if (candidate.getAbsoluteSupport() >= minsupRelative) {
                        level.add(candidate);
                        saveItemset(candidate,n);

                    }
                }
            }
            k++;

        }while(level.isEmpty() == false);


        if(writer != null){
            writer.close();
        }
        return patterns;
    }
    private void checkSupport(List<Itemset> candidatesK, List<int[]> database ){
        for(int[] transaction: database){
            if(transaction.length < k) {
                continue;
            }
            loopCand:	for(Itemset candidate : candidatesK){
                int pos = 0;
                for(int item: transaction){
                    if(item == candidate.itemset[pos]){
                        pos++;
                        if(pos == candidate.itemset.length){
                            candidate.support++;
                            continue loopCand;
                        }
                    }else if(item > candidate.itemset[pos]){
                        continue loopCand;
                    }
                }
            }
        }
    }

    // Khoi tao frequent item voi size k = 2
    private List<Itemset> candidate2(List<Integer> frequent1) {
        List<Itemset> candidates = new ArrayList<Itemset>();
        for (int i = 0; i < frequent1.size(); i++) {
            int item1 = frequent1.get(i);
            for (int j = i + 1; j < frequent1.size(); j++) {
                int item2 = frequent1.get(j);
                candidates.add(new Itemset(new int []{item1, item2}));
            }
        }
        return candidates;
    }
    // Khoi tao frequent item voi size k > 2
    protected List<Itemset> candidateSizeK(List<Itemset> levelK_1, List<Integer> frequent1) {

        List<Itemset> candidates = new ArrayList<Itemset>();
        for (int i = 0; i < levelK_1.size(); i++){
            int[] itemsetSizeK_1 =levelK_1.get(i).itemset;
            for(int j = 2; j < frequent1.size(); j++){
                int newItemsetSizeK_1[] = new int[itemsetSizeK_1.length+1];
                if(itemsetSizeK_1[itemsetSizeK_1.length-1] < frequent1.get(j)){
                    System.arraycopy(itemsetSizeK_1, 0, newItemsetSizeK_1, 0, itemsetSizeK_1.length);
                    newItemsetSizeK_1[itemsetSizeK_1.length] = frequent1.get(j);
                    candidates.add(new Itemset(newItemsetSizeK_1));
                }
            }
        }
        return candidates;
    }
    void saveItemset(Itemset itemset,int n) throws IOException {
        if(writer != null){
            writer.write(itemset.toString() + " #SUP: "
                    + itemset.getAbsoluteSupport());
            if(n == 1){
                System.out.println(itemset + " #SUP: "
                        + itemset.getAbsoluteSupport());
            }
            writer.newLine();
        }
        else{
            patterns.addItemset(itemset, itemset.size());
        }
    }
    void saveItemsetToFile(Integer item, Integer support, int n) throws IOException {
        if(writer != null){
            writer.write(item + " #SUP: " + support);
            if (n ==1 ){
                System.out.println(item + " #SUP: " + support);
            }
            writer.newLine();
        }
        else{
            Itemset itemset = new Itemset(item);
            itemset.setAbsoluteSupport(support);
            patterns.addItemset(itemset, 1);
        }
    }
    public void printStats() {
        System.out.println("=============  APRIORI  =============");
        System.out.println(" Candidates count: " + totalCandidateCount +" and the size of Candicates: " + (k-2));
        System.out.println(" Maximum memory usage : " + Memorytools.getInstance().getMaxMemory() + " mb");

//        System.out.println("=============================================");
    }

    public void printAlgo(Itemset itemset) throws IOException{
        System.out.println(itemset.toString() + " #SUP: "
                + itemset.getAbsoluteSupport());
    }
}
