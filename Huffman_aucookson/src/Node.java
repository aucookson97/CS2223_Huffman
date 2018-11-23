
public class Node {
	private Node leftNode;
	private Node rightNode;
	private int value;
	
	public Node(Node left, Node right, int value) {
		this.leftNode = left;
		this.rightNode = right;
		this.value = value;
	}
	
	
	public Node getLeft() {
		return this.leftNode;
	}
	
	public Node getRight() {
		return this.rightNode;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public boolean isLeaf() {
		return false;
	}
	
	@Override
	public String toString() {
		return "Value: " + value;
	}
}
