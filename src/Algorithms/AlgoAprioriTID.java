package Algorithms;

import input.TransactionDatabase;
import memory.Memorytools;
import patterns.Itemset;
import patterns.Itemsets;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class AlgoAprioriTID {
    protected int k;

    Map<Integer, Set<Integer>> mapItemTIDS = new HashMap<Integer, Set<Integer>>();

    int minSupport;
    BufferedWriter writer = null;

    protected Itemsets patterns = null;

    private int databaseSize = 0;

    private TransactionDatabase database = null;

    boolean showTransactionIdentifiers = false;

    public AlgoAprioriTID() {
    }
    public Itemsets runAlgorithm(String input, String output, double minsup, int n)
            throws NumberFormatException, IOException {

        // luu ket qua vao file output va khoi tao output
        patterns = null;
        writer = new BufferedWriter(new FileWriter(output));

        mapItemTIDS = new HashMap<Integer, Set<Integer>>();

        databaseSize = 0;

        BufferedReader reader = new BufferedReader(new FileReader(input));

        String line;
        while (((line = reader.readLine()) != null)) { // for each transaction
            if (line.isEmpty() == true ||
                    line.charAt(0) == '#' || line.charAt(0) == '%'
                    || line.charAt(0) == '@') {
                        continue;
                    }
            String[] lineSplited = line.split(" ");
            for (String token : lineSplited) {
                    int item = Integer.parseInt(token);
                    Set<Integer> tids = mapItemTIDS.get(item);
                    if (tids == null) {
                        tids = new HashSet<Integer>();
                        mapItemTIDS.put(item, tids);
                    }
                    tids.add(databaseSize);
                }
                databaseSize++;
            }
            reader.close();



        this.minSupport = (int) Math.ceil(minsup * databaseSize);

        //Candicate size k = 1
        k = 1;
        List<Itemset> level = new ArrayList<Itemset>();
        Iterator<Map.Entry<Integer, Set<Integer>>> iterator = mapItemTIDS.entrySet().iterator();
        while (iterator.hasNext()) {
            Memorytools.getInstance().checkMemory();

            Map.Entry<Integer, Set<Integer>> entry = (Map.Entry<Integer, Set<Integer>>) iterator
                    .next();
            if (entry.getValue().size() >= minSupport ) {
                Integer item = entry.getKey();
                Itemset itemset = new Itemset(item);
                itemset.setTIDs(mapItemTIDS.get(item));
                level.add(itemset);
                //luu vao itemset
                saveItemset(itemset,n);
            }
        }
        //Lay gia tri last candicate size k = 1
        Itemset lastItemlv1 = level.get(level.size()-1);
        // Sap xep tang dan
        Collections.sort(level, new Comparator<Itemset>() {
            public int compare(Itemset o1, Itemset o2) {
                return o1.get(0) - o2.get(0);
            }
        });

        //Candicate size k > 1
        k = 2;
        while (!level.isEmpty()) {
            level = generateCandidateSizeK(level, lastItemlv1,n);
            k++;
        }


        // close output file
        if(writer != null){
            writer.close();
        }

        return patterns;
    }

    protected List<Itemset> generateCandidateSizeK(List<Itemset> levelK_1,Itemset lastItemlv1,int n)
            throws IOException {
        int count = 0;
        List<Itemset> candidates = new ArrayList<Itemset>();
        Loopitem1: for (int i = 0; i < levelK_1.size(); i++){
            Itemset itemset1 = levelK_1.get(i);
            for(int j = i + 1; j < levelK_1.size(); j++ ){
                Itemset itemset2 = levelK_1.get(j);

                //lay support
                if(itemset1.get(itemset1.size()-1) < itemset2.get(itemset1.size()-1)){
                    Set<Integer> lstCheckSup = new HashSet<Integer>();
                    for(int value: itemset1.getTransactionsIds()){
                        if(itemset2.getTransactionsIds().contains(value)){
                            lstCheckSup.add(value);
                        }
                    }
                    //Check support
                    if(lstCheckSup.size() >= minSupport){
                        int itemsetCandi[] = new int[itemset1.size()+1];
                        System.arraycopy(itemset1.itemset, 0, itemsetCandi, 0, itemset1.size());
                        itemsetCandi[itemset1.size()] = itemset2.getItems()[itemset2.size() -1];
                        Itemset cand = new Itemset(itemsetCandi);
                        cand.setTIDs(lstCheckSup);
                        candidates.add(cand);
                        saveItemset(cand,n);
                        //loai bo phan tu trung nhau trong Itemset
                        if(itemset1.size() > 1 && itemset2.get(itemset2.size()-1) == lastItemlv1.get(0)){
                            continue Loopitem1;
                        }
                    }
                }
            }
        }
        return candidates;
    }

    void saveItemset(Itemset itemset,int n) throws IOException {
        if(writer != null){
            writer.write(itemset.toString() + " #SUP: "
                    + itemset.getTransactionsIds().size() );
            if(n == 1){
                System.out.println(itemset +" #SUP: "
                        + itemset.getTransactionsIds().size()  );
            }
            if(showTransactionIdentifiers) {
                writer.append(" #TID:");
                for (Integer tid: itemset.getTransactionsIds()) {
                    writer.append(" " + tid);
                }
            }
            writer.newLine();
        }
        else{
            patterns.addItemset(itemset, itemset.size());
        }
    }


    public void printStats() {
        System.out.println("=============  APRIORI TID =============");
        System.out.println(" Transactions count from database : " + databaseSize);
        System.out.println(" Maximum memory usage : " + Memorytools.getInstance().getMaxMemory() + " mb");
    }

}
