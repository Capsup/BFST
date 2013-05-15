package DataProcessing;

import java.util.ArrayList;
public class QuadTree<Key extends Comparable<Key>, Value>  {
    private Node root;
 
    // helper node data type
    private class Node {
        Key x, y;              
        Node NW, NE, SE, SW;   
        Value value;

        Node(Key x, Key y, Value value) {
            this.x = x;
            this.y = y;
            this.value = value;
        }
    }


    public void insert(Key x, Key y, Value value) {
        root = insert(root, x, y, value);
    }

    private Node insert(Node h, Key x, Key y, Value value) {
        if (h == null) return new Node(x, y, value);
        else if ( less(x, h.x) &&  less(y, h.y)) h.SW = insert(h.SW, x, y, value);
        else if ( less(x, h.x) && !less(y, h.y)) h.NW = insert(h.NW, x, y, value);
        else if (!less(x, h.x) &&  less(y, h.y)) h.SE = insert(h.SE, x, y, value);
        else if (!less(x, h.x) && !less(y, h.y)) h.NE = insert(h.NE, x, y, value);
        return h;
    }

    
    public ArrayList<Value> query2D(Interval2D<Key> rect) {
        return query2D(root, rect);
    }

    private ArrayList<Value> query2D(Node h, Interval2D<Key> rect) {
    	ArrayList<Value> v = new ArrayList<Value>();
        if (h == null) return v;
        Key xmin = rect.intervalX().low();
        Key ymin = rect.intervalY().low();
        Key xmax = rect.intervalX().high();
        Key ymax = rect.intervalY().high();
        if (rect.contains(h.x, h.y)){
            v.add(h.value);
        }
        if ( less(xmin, h.x) &&  less(ymin, h.y)) v.addAll(query2D(h.SW, rect));
        if ( less(xmin, h.x) && !less(ymax, h.y)) v.addAll(query2D(h.NW, rect));
        if (!less(xmax, h.x) &&  less(ymin, h.y)) v.addAll(query2D(h.SE, rect));
        if (!less(xmax, h.x) && !less(ymax, h.y)) v.addAll(query2D(h.NE, rect));
        return v;
    }

    private boolean less(Key k1, Key k2) { return k1.compareTo(k2) <  0; }
}