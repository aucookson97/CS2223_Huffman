
public class LeafNode extends Node{

	
	private char character;

	public LeafNode(char character, int value) {
		super(null, null, value);
		this.character = character;
	}
	
	public char getChar() {
		return this.character;
	}
	public boolean isLeaf() {
		return true;
	}
	
	@Override
	public String toString() {
		return "Character: " + this.character + ", Value: " + this.getValue();
	}
}
