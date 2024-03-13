package input;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TransactionDatabase {

    private final Set<Integer> items = new HashSet<Integer>();
    private final List<List<Integer>> transactions = new ArrayList<List<Integer>>();


    public List<List<Integer>> getTransactions() {
        return transactions;
    }


}
