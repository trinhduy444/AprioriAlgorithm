package patterns;

import java.util.ArrayList;
import java.util.List;

public class Itemsets{
    private final List<List<Itemset>> levels = new ArrayList<List<Itemset>>();
    private int itemsetsCount = 0;
    private String name;


    public Itemsets(String name) {
        this.name = name;
        levels.add(new ArrayList<Itemset>());
    }

    public void addItemset(Itemset itemset, int k) {
        while (levels.size() <= k) {
            levels.add(new ArrayList<Itemset>());
        }
        levels.get(k).add(itemset);
        itemsetsCount++;
    }

}
