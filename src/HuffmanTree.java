/*
 * Pranay Shoroff
 * Tyler Jensen AC
 * This class contains methods that help with Huffman-Style 
 * compression and decompression
 * of ASCII text based files.
 */

import java.util.*;
import java.io.*;

public class HuffmanTree {
	private HuffmanNode overallRoot;
	
	//part 1
	//pre: takes in an int[] of frequencies 
	//	   where the index corresponds to a 
	//character's ascii number.
	//post: Constructs the object
	//Throws a new IllegalArgumentException 
	//if the int[] is null or is empty
	public HuffmanTree(int[] count) {
		if (count == null || count.length == 0)
			throw new IllegalArgumentException();
		//intializing the main priorityQueue
		PriorityQueue<HuffmanNode> sorted = 
				new PriorityQueue(count.length);
		//adding everything into a priorityqueue
		for (int i = 0; i < count.length; i++) {
			if (count[i] >= 1) {
				sorted.add(new HuffmanNode(i, count[i]));
			}
		}
		//adding pseudo eof character 256 ascii ID & freq:1
		sorted.add(new HuffmanNode(count.length, 1));
		//combine:
		while (sorted.size() > 1) {
			HuffmanNode first = sorted.remove();
			HuffmanNode second = sorted.remove();
			HuffmanNode newNode = 
					new HuffmanNode(first.counts+second.counts, 
							first, second);	
			//add the new node back to the p-queue:
			sorted.add(newNode);
		}
		assert (sorted.size() == 1);
		overallRoot = sorted.peek();
	}
	
	//part 2
	//pre: takes in a scanner used to construct the tree
	//throws an IllegalArgumentException if input file is null or empty
	//post: constructs a tree based on input file
	public HuffmanTree(Scanner input) {
		if (input == null || !input.hasNext())
			throw new IllegalArgumentException();
		overallRoot = null;
		while(input.hasNextLine()) {
			int n = Integer.parseInt(input.nextLine());
			String binaryCode = input.nextLine();
			overallRoot = traverse(binaryCode, n, overallRoot);
		}
	}
	
	//pre: takes a String to help navigate the tree, 
	//an int to be added to the 
	//node to be created, and a HuffmanNode 
	//used to keep track of position in the tree
	//post: Traverses the tree according to the 
	//binary directions, creating new nodes
	//when they don't exist but should. Returns a new HuffmanNode tree
	private HuffmanNode traverse(String binaryCode, 
			int n, HuffmanNode root) {
		//once binaryCode have completed
		if (binaryCode.length() == 0) {
			//creates and returns new HuffmanNode 
			//with the initial data read from file
			return new HuffmanNode(n, 0);
		} else {
			//if the current root position registers as null,
			//the program creates a new node with meaningless values
			if (root == null) {
				root = new HuffmanNode(-1, -1);
			}
			//0 means traverse left, 1 = right
			if (binaryCode.startsWith("0")) {
				root.left = 
					traverse(binaryCode.substring(1), n, root.left);
			} else {
				root.right = 
					traverse(binaryCode.substring(1), n , root.right);
			}
			return root;
		}
	}
	
	//write code to file
	//pre: takes in a PrintStream to be used as the output file
	//throws an IllegalArgumentException if PrintStream is null
	//post: writes the tree to the file in standard format
	public void write(PrintStream output) {
		if (output == null)
			throw new IllegalArgumentException();
		String traced = "";
		write(output, overallRoot, traced);
	}
	
	//write code to file
	//pre: passes in the same PrintStream 
	//as it's sister write method,
	//A HuffmanNode for tree traversal, 
	//and a String containing the route
	//the code took to a certain node
	//post: Writes the nodes to file with their locations 
	//relative to the overall root
	private void write(PrintStream output, 
			HuffmanNode root, String traced) {
		if (root.isLeaf()) {
			output.println(root.asciiID);
			output.println(traced);
		} else {
			if (root.left != null) {
				write(output, root.left, traced + "0");
			}
			if (root.right != null) {
				write(output, root.right, traced + "1");
			}			
		}
	}
	
	//pre: BitStreamInput use to read off individual bits
	//PrintStream used to print the decoded file
	//int used to tell the program when to stop decoding
	//throws a new IllegalArgumentException if 
	//the PrintStream || BitInputStream is null
	//post: returns a decoded file
	public void decode(BitInputStream input, 
			PrintStream output, int eof) {
		if (input == null || output == null)
			throw new IllegalArgumentException();
		int currNum = decode(input, output, eof, overallRoot);
		while(currNum != eof) {
			output.print(Character.toChars(currNum));
			currNum = decode(input, output, eof, overallRoot);
		}
	}
	
	//pre: Takes a BitInputStream (to read individual bits), 
	//PrintStream to print decoded code to a new file, 
	//an int to tell the code to stop decoding, 
	//and a HuffmanNode to help the program traverse through the tree
	private int decode(BitInputStream input, 
			PrintStream output, int eof, HuffmanNode root) {
		if (root.isLeaf()) {
			return root.asciiID;
		} else if (input.readBit() == 0) {
			return decode(input, output, eof, root.left);
		} else {
			return decode(input, output, eof, root.right);
		}
	}
}