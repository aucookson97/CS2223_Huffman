
public class Node {
	private Node leftNode;
	private Node rightNode;
	
	public Node(Node left, Node right) {
		this.leftNode = left;
		this.rightNode = right;
	}
	
	
	public Node getLeft() {
		return this.leftNode;
	}
	
	public Node getRight() {
		return this.rightNode;
	}
}
