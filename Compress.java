import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class Compress {

	private InputStream read;
	private int[] freq;
	private long fileLength;
	private Heap<Node> heap;
	private HashMap<Byte, Long> tableforCompress;
	private HashMap<Long, Byte> tableforDecompress;
	private File file;
	private int[] header;
	private byte[] header2;

	public Compress(String path) throws FileNotFoundException {
		file = new File(path);
		fileLength = file.length();
		read = new FileInputStream(file);
		freq = new int[256];
		heap = new Heap<Node>();
		tableforCompress = new HashMap<Byte, Long>();
		tableforDecompress = new HashMap<Long, Byte>();
	}

	public void makeStatistics() throws IOException {
		long mb = 1048576;
		int chunkLength = 0;
		byte[] buffer;
		long fileLength1 = fileLength;
		chunkLength = (int) mb * 100;
		while (fileLength1 > 0) {
			buffer = new byte[chunkLength];
			int readed = read.read(buffer);
			for (int i = 0; i < readed; i++) {
				freq[buffer[i] & 255]++;	
			}
			fileLength1 -= readed;
			if (fileLength1 < chunkLength)
				chunkLength = (int) fileLength1;
		}
		read.close();
	}

	public void build() {
		for (byte i = -128; i <128; i++) {
			
			if (freq[i&255] != 0) {
				Node n = new Node(freq[i&255], null, null, null);
				n.setByte(i);
				heap.insert(n);
			}
			
			if(i == 127)
				break;
		}
		header = new int[heap.getSize()];
		header2 = new byte[heap.getSize()];
		ArrayList<Node> tmp = heap.getArray();
		for(int i=0; i<tmp.size() ; i++){
			header[i] = tmp.get(i).getValue();
			header2[i] = tmp.get(i).getByte();
		}

		while (heap.getSize() > 1) {
			Node left = heap.removeMin();
			Node right = heap.removeMin();
			Node newRoot = new Node((left.getValue() + right.getValue()), null, null, null);
			left.setParent(newRoot);
			right.setParent(newRoot);
			newRoot.setLeft(left);
			newRoot.setRight(right);
			heap.insert(newRoot);
		}

		this.traverse(heap.getMin(), 0, 0);

	}

	public void traverse(Node n, long codeword, int i) {
		if ((!n.hasLeft()) && (!n.hasRight())) {
			codeword |= (1 << i);
			byte v = n.getByte();
			tableforCompress.put(v, codeword);
			tableforDecompress.put(codeword, v);
		}
		if (n.hasLeft()){
			traverse(n.getLeft(), codeword, i + 1);
		}
		if (n.hasRight()) {
			codeword |= (1 << i);
			traverse(n.getRight(), codeword, i + 1);
		}
	}

	public void compress(String path) throws IOException {
		File f = new File(path);
		FileOutputStream write = new FileOutputStream(f);
		write.write(header.length);
		write.write(header2);
		for(int i=0; i<header.length ; i++){
			write.write(header[i]);
		}
		read = new FileInputStream(file);
		int chunklength = 100 * 1048576;
		byte[] bufferRead;
		int readed;
		int placeatbyte = 0;
		while (fileLength > 0) {
			bufferRead = new byte[chunklength];
			readed = read.read(bufferRead);
			byte toWrite = 0;
			long codeword;
			int ln;
			int place = 0;
			boolean written = false;
			for (int i = 0; i < readed; i++) {
				written = false;
				codeword = tableforCompress.get(bufferRead[i]);
				ln = 63 - Long.numberOfLeadingZeros(codeword);
				if((fileLength -i) ==1){
					bufferRead[place] = (byte) ln;
					place++;
				}
				for (int j = 0; j < ln; j++) {
					if ((codeword &= (1 << j)) > 0)
						toWrite |= (1 << placeatbyte);
					placeatbyte++;
					if (placeatbyte == 8) {
						written = true;
						placeatbyte = 0;
						bufferRead[place] = toWrite;
						toWrite = 0;
						place++;
					}
				}
			}
			if (!written) {
				bufferRead[place] = toWrite;
				place++;
			}
			write.write(bufferRead,0, place);
			fileLength -= readed;
		}
		write.close();
		read.close();
	}

	public static void main(String[] args) throws IOException {
		Compress c = new Compress("D:\\readme.txt");
		c.makeStatistics();
		c.build();
		c.compress("D:\\ja.bin");
		System.out.println((float)34148352/34222080);
		System.out.println();

	}
}
