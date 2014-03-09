import java.util.ArrayList;


public class Heap<E extends Comparable> {

	private ArrayList<E> array;
	public Heap(){
		array = new ArrayList<E>();
	}
	
	public ArrayList<E> getArray(){
		return  array;
	}
	
	private int getLeft(int i){
		return (i*2)+1;
	}
	
	private int getRight(int i){
		return (i*2)+2;
	}
	
	private  boolean hasLeft(int i){
		return (((i*2)+1)< array.size() );
	}
	
	private  boolean hasRight(int i){
		return (((i*2)+2)< array.size() );
	}
	
	private int getParent(int i){
		if(i%2 == 0 && i>1)
			return (i-2)/2;
		else if(i%2 == 1 && i>0)
			return (i-1)/2;
		return -1;
	}
	
	private boolean hasParent(int i){
		return (i>0);
	}
	
	public int getSize(){
		return array.size();
	}
	
	private E getMinChild(int index){
		E min = null ;
		if(hasLeft(index) && hasRight(index)){
			E left = array.get(getLeft(index));
			E right = array.get(getRight(index));
			if(left.compareTo(right) <= 0){
				min = left;
			}else
				min = right;
		}
		
		else if(hasLeft(index)){
			min = array.get(getLeft(index));
		}
		else if(hasRight(index)){
			min = array.get(getRight(index));
		}
		return min;
		
	}
	
	public void insert(E o){
		array.add(o);
		int index = array.size()-1;
		int parent=0;
		parent = getParent(index);
		while((index >0) && array.get(index).compareTo(array.get(parent)) < 0 ){
			E tmp = array.get(index);
			E tmp2 = array.get(parent);
			array.remove(index);
			array.add(index, tmp2);
			array.remove(parent);
			array.add(parent, tmp);
			index = parent;
			parent = getParent(index);
		}
	}
	
	public void remove(E o) throws Exception{
		if(!array.contains(o))
			throw new Exception("NO such element!");
		else{
			int index = array.indexOf(o);
			int last = array.size()-1;
			E tmp = array.get(last);
			array.remove(index);
			array.add(index, tmp);
			array.remove(last);
			E min = getMinChild(index);
			int parent = getParent(index);
			int minIndex = array.indexOf(min);
			if((!hasLeft(index) && (!hasRight(index))) && array.get(index).compareTo(array.get(parent)) < 0){
				E tmp1 = array.get(index);
				E tmp2 = array.get(parent);
				array.remove(index);
				array.add(index, tmp2);
				array.remove(parent);
				array.add(parent, tmp1);
				index = parent;
				parent = getParent(index);
			}
			while( (hasLeft(index) || hasRight(index)) && (min.compareTo(tmp)<0)  ){
				array.remove(index);
				array.add(index, min);
				array.remove(minIndex);
				array.add(minIndex, tmp);
				index = minIndex;
				tmp = array.get(index);
				min = getMinChild(index);
				if(min != null)
					minIndex = array.indexOf(min);
				
			}
		}		
	}
	
	public E removeMin(){
		int index = 0;
		E rMin = array.get(0);
		int last = array.size()-1;
		E tmp = array.get(last);
		array.remove(index);
		array.add(index, tmp);
		array.remove(last);
		E min = getMinChild(index);
		int minIndex = array.indexOf(min);
		while( (hasLeft(index) || hasRight(index)) && (min.compareTo(tmp)<0)  ){
			array.remove(index);
			array.add(index, min);
			array.remove(minIndex);
			array.add(minIndex, tmp);
			index = minIndex;
			tmp = array.get(index);
			min = getMinChild(index);
			if(min != null)
				minIndex = array.indexOf(min);
		}
		return rMin;
	}
	
	public E getMin(){
		return array.get(0);
	}
	
	
	public static void main(String[] args) throws Exception {
		Heap<Integer> hepo = new Heap<Integer>();
		hepo.insert(10);
		hepo.insert(20);
		hepo.insert(30);
		hepo.insert(22);
		hepo.insert(39);
		hepo.insert(31);
		hepo.insert(32);
		hepo.insert(25);
		hepo.insert(100);
		hepo.insert(50);
		hepo.insert(60);
		hepo.remove(39);
		ArrayList<Integer> array = hepo.getArray();
		for(int i=0; i<array.size(); i++){
			System.out.println(array.get(i));
		}
		

	}

}
