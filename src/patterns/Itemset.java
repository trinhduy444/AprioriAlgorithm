package patterns;



import java.util.*;

public class Itemset extends AbstractItemset{
    public Set<Integer> transactionsIds = new HashSet<Integer>();
    public int[] itemset;

    public int support = 0;
    public Itemset(int item){
        itemset = new int[]{item};
    }

    public Itemset(int [] items){
        this.itemset = items;
    }

    public int getAbsoluteSupport(){
        return support;
    }
    public int size() {
        return itemset.length;
    }
    public Set<Integer> getTransactionsIds() {
        return transactionsIds;
    }
    public Integer get(int position) {
        return itemset[position];
    }
    public void setAbsoluteSupport(Integer support) {
        this.support = support;
    }
    public void setTIDs(Set<Integer> listTransactionIds) {
        this.transactionsIds = listTransactionIds;
    }
    public int[] getItems() {
        return itemset;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(itemset);
    }

}