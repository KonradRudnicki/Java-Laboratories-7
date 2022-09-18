import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;

public class HashTable {
    LinkedList[] arr; // use pure array
    private final static int defaultInitSize = 8;
    private final static double defaultMaxLoadFactor = 0.7;
    private int size;
    private final double maxLoadFactor;

    public HashTable() {
        this(defaultInitSize);
    }

    public HashTable(int size) {
        this(size, defaultMaxLoadFactor);
    }

    public HashTable(int initCapacity, double maxLF) {
        this.size = initCapacity;
        this.arr = new LinkedList[initCapacity];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = new LinkedList<>();
        }

        this.maxLoadFactor = maxLF;
    }

    @SuppressWarnings("unchecked")
    public boolean add(Object elem) {
        if (this.get(elem) == null) return false;

        int key = elem.hashCode() % size;
        arr[key].add(elem);

        if (calcLoadFactor() > defaultMaxLoadFactor) {
            doubleArray();
        }

        return true;
    }
    private void doubleArray() {
        this.size *= 2;
        LinkedList[] temp = arr;
        this.arr = new LinkedList[size];

        for (int i = 0; i < arr.length; i++) {
            arr[i] = new LinkedList<>();
        }

        for (LinkedList array : temp) {
            for (Object o : array) {
                this.add(o);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < arr.length; i++) {
            result.append(i + ":");
            for (int j = 0; j < arr[i].size(); j++) {
                if (j == arr[i].size() - 1) {
                    IWithName x = (IWithName) arr[i].get(j);
                    result.append(" " + x.getName());

                }
                else {
                    IWithName x = (IWithName) arr[i].get(j);

                    result.append(" " + x.getName() + ",");
                }
            }
            result.append("\n");
        }

        return result.toString();
    }

    public Object get(Object toFind) {
        int index = toFind.hashCode() % size;

        if (!arr[index].isEmpty()){
            for (Object o : arr[index]) {
                if (o.equals(toFind)){
                    return o;
                }
            }
        }

        return null;
    }

    private double calcLoadFactor() {
        int usedSlots = 0;

        for (LinkedList list : arr) {
            usedSlots += list.size();
        }

        return (double) usedSlots / size;
    }
}