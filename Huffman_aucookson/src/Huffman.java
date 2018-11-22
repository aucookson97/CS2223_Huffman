import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Huffman {
	
	private HashMap<Character, Integer> charFreq;
	
	public Huffman(String inputFile) {
		charFreq = new HashMap<Character, Integer>();
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(inputFile));
			
			String line;
			
			while ((line = br.readLine()) != null) {
				System.out.println(line);
				for (char c : line.toCharArray()) {
					if (charFreq.containsKey(c)) {
						charFreq.put(c, charFreq.get(c) + 1);
					} else {
						charFreq.put(c,  1);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
		
	}
	
	private void generateHuffmanTree() {
		ArrayList<Node> nodes = new ArrayList<Node>();
		
		
	}
	
	public static void main(String[] args) {
		String inputFile = "huffmanTest.txt";
		Huffman hf = new Huffman(inputFile);
		System.out.println("Done");
	}
}