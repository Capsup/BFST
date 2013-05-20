package Graph;

import java.util.HashMap;

/**
 * This class handles nodes
 */
public class Node{
	private static HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
	private int id;
	private double xCoord;
	private double yCoord;
	
	
	/**
	 * Constructs a new node
	 * @param id - nodes id
	 * @param xCoord - nodes x-coordinate
	 * @param yCoord - nodes y-coordinate
	 * @return the created node
	 */
	public static Node makeNode(int id, double xCoord, double yCoord){
		return nodes.put(id, new Node(id, xCoord, yCoord));
	}
	
	/**
	 * A HashMap of every node is saved, this void deletes that list. This can be used if the map is no longer needed
	 */
	public static void nullNodes(){ nodes = new HashMap<Integer, Node>(); }
	
	/**
	 * Returns a given node
	 * @param id
	 * @return the node with given id
	 */
	public static Node getNode(int id){ return nodes.get(id); }

	/**
	 * Constructs a new node
	 * @param id - the nodes id
	 * @param xCoord - the nodes x-coordinate
	 * @param yCoord - the nodes y-coordinate
	 */
	private Node( int id, double xCoord, double yCoord )
	{
		this.id = id;
		this.xCoord = xCoord;
		this.yCoord = yCoord;
	}
	
	/**
	 * Returns the nodes ID
	 * @return id
	 */
	public int getID()
	{
		return id;
	}

	/**
	 * Returns the x-coordinate
	 * @return x
	 */
	public double getX()
	{
		return xCoord;
	}
	
	/**
	 * Returns the y-coordinate
	 * @return y
	 */
	public double getY()
	{
		return yCoord;
	}

	public static int size() {
		return nodes.size();
	}

}
