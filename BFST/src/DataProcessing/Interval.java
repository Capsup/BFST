package DataProcessing;

public class Interval<Key extends Comparable<Key>> {

    private final Key left;
    private final Key right;

    public Interval(Key left, Key right) {
        this.left  = left;
        this.right = right;
    }

    public Key low() { return left; }
    public Key high() { return right; }


    // does this interval contain x?
    public boolean contains(Key x) {
        return (left.compareTo(x) <= 0) && (right.compareTo(x) >= 0);
    }

    // string representation of this interval        
    public String toString() {
        return "[" + left + ", " + right + "]";
    }
}