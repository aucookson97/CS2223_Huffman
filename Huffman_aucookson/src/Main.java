
public class Main {
	
	public static void main(String[] args) {
		
		String manip = args[0];
		String inputFile = args[1];
		String outputFile = args[2];
		
		Huffman hf = new Huffman();
		
		if (manip.equals("Encode")) {
			hf.encode(inputFile, outputFile);
		} else if (manip.equals("Decode")) {
			hf.decode(inputFile, outputFile);
		} else {
			System.out.println("Please Choose Either \"Encode\" or \"Decode\"");
		}		
	}
}
