package patterns;



public abstract class AbstractItemset {

    public AbstractItemset() {
        super();
    }
    public abstract int size();

    public String toString(){
        if(size() == 0) {
            return "EMPTYSET";
        }

        StringBuilder r = new StringBuilder ();

        for(int i=0; i< size(); i++){
            r.append(get(i));
            r.append(' ');
        }
        return r.toString();
    }

    public abstract Integer get(int position);
    public abstract int getAbsoluteSupport();




}