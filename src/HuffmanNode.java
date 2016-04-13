import java.util.*;
import java.io.*;

public class HuffmanNode implements Comparable<HuffmanNode> {
	
	public int asciiID; //manages the ascii number of the 
						//character associated with the node
	public int counts; //manages the frequency of a 
					   //character associated with the node
	public HuffmanNode right; //reference to the node 
							  //directly right of this
	public HuffmanNode left; //reference to the node 
							 //directly left of this
	
	private int invalidAscii = -1; //out of bounds ascii ID value
	
	//Constructor takes in parameters for 
	//the node's Ascii number, and the frequency
	public HuffmanNode(int asciiID, int counts) {
		this.asciiID = asciiID;
		this.counts = counts;
	}
	
	//Constructor takes in values for the 
	//frequency, the left node, and the right node
	public HuffmanNode(int counts, HuffmanNode left, HuffmanNode right) {
		this.asciiID = invalidAscii;
		this.counts = counts;
		this.left = left;
		this.right = right;
	}
	
	//implements comparable interface
	//post: returns an int based on comparison of values
	//positive when this is greater than the compared
	//zero when equal
	//negative when smaller
	//throws a new IlleaglArgumentException 
	//when other HuffmanNode is null
	public int compareTo(HuffmanNode other) {
		if (other == null) {
			throw new IllegalArgumentException();
		}
		return counts - other.counts;
	}
	
	//states the node's 'leaf' status
	public boolean isLeaf() {
		return (right == null && left == null);
	}
}
