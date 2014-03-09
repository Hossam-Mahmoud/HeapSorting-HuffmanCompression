
public class Node implements Comparable<Node> {
	private int value;
	private Node left;
	private Node right;
	private Node parent;
	private byte original;
	
	public Node(){
		value = 0;
		left = null;
		right = null;
		parent = null;
	}
	
	public Node(int v, Node l, Node r, Node p){
		value = v;
		left = l;
		right = r;
		parent = p;
	}
	
	public int getValue(){
		return value;
	}
	
	public byte getByte(){
		return original;
	}
	
	public void setByte(byte v){
		original = v;
	}
	
	public Node getRight(){
		return right;
	}
	
	public boolean hasRight(){
		return (right != null);
	}
	public boolean hasLeft(){
		return (left != null);
	}
	
	public Node getLeft(){
		return left;
	}
	
	public Node getParent(){
		return parent;
	}
	
	public void setValue(int v){
		value = v;
	}
	
	public void setLeft(Node l){
		left = l;
	}
	
	public void setRight(Node r){
		right = r;
	}
	
	public void setParent(Node p){
		parent = p;
	}

	@Override
	public int compareTo(Node n) {
		if(this.getValue() > n.getValue())
			return 1;
		else if(this.getValue() == n.getValue())
			return 0;
		else
			return -1;
	}
}
