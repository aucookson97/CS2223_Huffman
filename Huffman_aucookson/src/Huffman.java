import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
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
		try {
			br = new BufferedReader(new FileReader(inputFile));
			
			String line;
			
			while ((line = br.readLine()) != null) {
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
		
		//ByteBuffer bytes;
		String bits = "";
		
		try {
			br = new BufferedReader(new FileReader(inputFile));
			
			String line;
			
			while ((line = br.readLine()) != null) {
				for (char c : line.toCharArray()) {
					for (char b : codeTable.get(c).toCharArray()) {
						bits += b;
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
		byte[] bytes = new BigInteger(bits, 2).toByteArray();
		
		FileOutputStream stream = null;
		try {
			stream = new FileOutputStream(outputFile);
			long numBits = (long)(bits.length());
			ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
			buffer.putLong(numBits);
			stream.write(buffer.array());
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
		
		int b;
		String bits = "";
		int bitsRead = 0;
		
		/*
		FileInputStream stream = null;
		try {
			byte[] bitBuffer = new byte[8];
			long numBits = 0;
			stream = new FileInputStream(inputFile);
			stream.read(bitBuffer);
			for (int i = 0; i < bitBuffer.length; i++)
			{
			   numBits = (numBits << 8) + (bitBuffer[i] & 0xff);
			}
			boolean reading = true;
			while ((b = stream.read()) != -1 && reading) {
				System.out.println(b);
				for (int i = Integer.BYTES * 8; i >= 0; i--) {
					bits += Integer.toString(getBit(b, i));
					if (++bitsRead > numBits) {
						reading = false;
						break;
					}
				}
			}
			reverseCodeTable();
			decodeBits(outputFile, bits);
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
		*/
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
	}
	
	public void reverseCodeTable() {
		codeTableReversed = new HashMap<String, Character>();
		for (Entry<Character, String> entry : codeTable.entrySet()) {
			codeTableReversed.put(entry.getValue(), entry.getKey());
		}
	}
	
	int getBit(int num, int k) {
	    return (num >> k) & 1;
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
		String inputFile = "test2.txt";
		Huffman hf = new Huffman();
		hf.encode(inputFile, "test.dat");
		hf.decode("test.dat", "outputTest.txt");
		
	}
}