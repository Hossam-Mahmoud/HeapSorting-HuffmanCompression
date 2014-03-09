import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class Decompress {

	private Heap<Node> heap;
	private InputStream read;
	private long filelength;
	
	public Decompress(String path) throws FileNotFoundException{
		File f = new File(path);
		read = new FileInputStream(f);
		heap = new Heap<Node>();
		filelength = f.length();
	}
	
	public void buildTree() throws IOException{
		int length = read.read();
		byte[] bytes = new byte[length];
		
		read.read(bytes);
		for(int j=0; j<bytes.length; j++){
			System.out.println(bytes[j]);
		}
		for(int i=0; i<length ; i++){
			int z = read.read();
			Node n = new Node(z, null, null, null);
			n.setByte(bytes[i]);
			heap.insert(n);
		}
		filelength -= (1+(2*length));
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
		
	}
	
	
	
	public void decompress(String path) throws IOException{
		File f = new File(path);
		FileOutputStream write = new FileOutputStream(f);
		int chunk = 100 * 1048576;
		byte[] buffer;
		byte[] toWrite = new byte[10 * 1048576];
		int counter=0;
		Node root = heap.getMin();
		Node tmp = root;
		int placeAtByte =0;
		boolean wrote =false;
		while(filelength >0){
			
			buffer = new byte[chunk];
			int readed = read.read(buffer);
			for(int i=0; i<readed ; i++){
				
			}
			int i=0;
			while(i<readed){
				if(filelength-i == 2){
					if(!wrote){
						write.write(toWrite,0, counter-1);
						wrote = true;
					}
					int x = buffer[i];
					i++;
					for(int k=0; k< x ; k++){
						if((!tmp.hasLeft()) && (!tmp.hasRight())){
							byte[] last = new byte[1];
							last[0] = tmp.getByte();
							tmp = root;
							write.write(last);
						}else if((buffer[i] & (1<<k)) == 0){
							tmp = tmp.getLeft();
							placeAtByte++;
						}else if((buffer[i] & (1<<k)) > 0){
							tmp = tmp.getRight();
							placeAtByte++;
						}
					}
					break;
				}
				
				else{
					if((!tmp.hasLeft()) && (!tmp.hasRight())){
						wrote = false;
						toWrite[counter] = tmp.getByte();
						tmp = root;
						if(counter+1 == toWrite.length){
							wrote = true;
							write.write(toWrite);
							counter = 0;
							toWrite = new byte[10 * 1048576];
						}else
							counter++;
					}else if((buffer[i] & (1<<placeAtByte)) == 0){
						tmp = tmp.getLeft();
						placeAtByte++;
					}else if((buffer[i] & (1<<placeAtByte)) > 0){
						tmp = tmp.getRight();
						placeAtByte++;
					}
					if(placeAtByte == 8){
						placeAtByte =0;
						i++;
					}
				}
				
			}
			filelength -= readed;
		}
		if(!wrote){
			write.write(toWrite,0, counter-1);
			wrote = true;
		}
		write.close();
		read.close();
	}
	
	
	public static void main(String[] args) throws IOException {
		Decompress de = new Decompress("D:\\ja.bin");
		de.buildTree();
		de.decompress("D:\\readme2.txt");
	}

}
