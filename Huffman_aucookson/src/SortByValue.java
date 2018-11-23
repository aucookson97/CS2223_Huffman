import java.util.Comparator;

public class SortByValue implements Comparator<Node>{
	public int compare(Node a, Node b) {
		if (a.getValue() < b.getValue()) {
			return -1;
		} else if (a.getValue() == b.getValue()) {
			return 0;
		} else {
			return 1;
		}
	}
}
