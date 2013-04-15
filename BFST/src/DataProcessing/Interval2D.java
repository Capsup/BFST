package DataProcessing;

public class Interval2D<Key extends Comparable<Key>> {
    private Interval<Key> x;
    private Interval<Key> y;

    public Interval2D(Interval<Key> x, Interval<Key> y) {
        this.x = x;
        this.y = y;
    }
    
    public Interval<Key> intervalX(){
    	return x;
    }
    
    public Interval<Key> intervalY(){
    	return y;
    }

    public boolean contains(Key x, Key y) {
        return this.x.contains(x)  && this.y.contains(y);
    }
  
    public String toString() {
        return x + " x " + y;
    }
}