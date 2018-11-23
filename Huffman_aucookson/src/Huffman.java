import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

public class Huffman {
	
	private HashMap<Character, Integer> charFreq;
	private HashMap<Character, String> codeTable;
	private HashMap<String, Character> codeTableReversed;

	private String inputFile;
	
	private void encode(String inputFile, String outputFile) {
		
		this.inputFile = inputFile;
		
		charFreq = new HashMap<Character, Integer>();
		codeTable = new HashMap<Character, String>();
		
		
		BufferedReader br = null;
		InputStreamReader stream = null;
		try {
			stream = new InputStreamReader(new FileInputStream(inputFile));

			int c;
			while ((c = stream.read()) != -1) {
				if (charFreq.containsKey((char)c)) {
					charFreq.put((char)c, charFreq.get((char)c) + 1);
				} else {
					charFreq.put((char)c,  1);
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
		
		ArrayList<Node> nodes = new ArrayList<Node>();
		for (char element: charFreq.keySet()) {
			nodes.add(new LeafNode(element, charFreq.get(element)));
		}
				
		while (nodes.size() > 2) {
			sortArray(nodes);
			
			int rootValue = nodes.get(0).getValue() + nodes.get(1).getValue();
			Node newRoot = new Node(nodes.get(0), nodes.get(1), rootValue);
			nodes.remove(0);
			nodes.remove(0);
			nodes.add(newRoot);
		}
		
		int rootValue = nodes.get(0).getValue() + nodes.get(1).getValue();
		Node root = new Node(nodes.get(0), nodes.get(1), rootValue);

		createCodeTable(codeTable, root, "");
		
		encodeFile(outputFile);
	}
	
	private void encodeFile(String outputFile) {
		BufferedReader br = null;
		InputStreamReader reader = null;
		//ByteBuffer bytes;
		String bits = "";
		
		try {
			reader = new InputStreamReader(new FileInputStream(inputFile));

			int c;
			while ((c = reader.read()) != -1) {
				for (char b : codeTable.get((char)c).toCharArray()) {
					bits += b;
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
		
		byte numIgnore = (byte)(((bits.length()+7) / 8) * 8 - bits.length());
		byte[] bytes = new BigInteger(bits, 2).toByteArray();
		
		FileOutputStream stream = null;
		try {
			stream = new FileOutputStream(outputFile);
			stream.write(numIgnore);
			stream.write(bytes);
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		    try {
		    	if (stream != null) {
		    		stream.close();
		    	}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void decode(String inputFile, String outputFile) {
		
		byte[] bytes = null;
		String bits = "";
		
		try {
			bytes = Files.readAllBytes(FileSystems.getDefault().getPath("", inputFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int numIgnore = bytes[0];

		for (int i=1; i<bytes.length; i++) {
			for (int k=0; k<8;k++) {
				if (i != 1 || k >= numIgnore) {
					String bit = Integer.toString(getBit(bytes[i], 7-k));
					bits += bit;
				}
			}
		}
		reverseCodeTable();
		decodeBits(outputFile, bits);
		System.out.println("Done");
	}
	
	private void decodeBits(String outputFile, String bits) {
		BufferedWriter writer = null;
		try {
			String code = "";

			writer = new BufferedWriter(new FileWriter(outputFile));
			
			for (char b : bits.toCharArray()) {
				code += b;
				
				Character c = codeTableReversed.get(code);
				
				if (c != null) {
					writer.write(c);
					code = "";
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		    try {
		    	if (writer != null) {
		    		writer.close();
		    	}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
		System.out.println("Done");
	}
	
	public void reverseCodeTable() {
		codeTableReversed = new HashMap<String, Character>();
		for (Entry<Character, String> entry : codeTable.entrySet()) {
			codeTableReversed.put(entry.getValue(), entry.getKey());
		}
	}
	
	private int getBit(byte b, int k) {
	    return (b >> k) & 1;
	}
	
	private void createCodeTable(HashMap<Character, String> codeTable, Node node, String code) {
		if (node.isLeaf()) {
			codeTable.put(((LeafNode) node).getChar(), code);
			return;
		}
		createCodeTable(codeTable, node.getLeft(), code + "0");
		createCodeTable(codeTable, node.getRight(), code + "1");
	}
	
	private void sortArray(ArrayList<Node> nodeList) {
		Collections.sort(nodeList, new SortByValue());
	}
	
	public static void main(String[] args) {
		String inputFile = "huffmanTest.txt";
		Huffman hf = new Huffman();
		hf.encode(inputFile, "test.dat");
		hf.decode("test.dat", "outputTest.txt");
		
	}
}