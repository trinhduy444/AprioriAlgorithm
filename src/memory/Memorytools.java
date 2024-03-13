package memory;

public class Memorytools {
    private static Memorytools instance = new Memorytools();

    private double maxMemory = 0;
    public static Memorytools getInstance(){
        return instance;
    }

    public double getMaxMemory() {
        return maxMemory;
    }


    public void reset(){
        maxMemory = 0;
    }

    public double checkMemory() {
        double currentMemory = (Runtime.getRuntime().totalMemory() -  Runtime.getRuntime().freeMemory())
                / 1024d / 1024d;
        if (currentMemory > maxMemory) {
            maxMemory = currentMemory;
        }
        return currentMemory;
    }
}
