package edu.whu.wang.dataStruct;

//import java.util.HashSet;
//import java.util.Iterator;

class SingleVertex{
		private int vID;
		private SingleVertex next;
		
		public SingleVertex(int vID){
			this.vID = vID;
			this.next = null;
		}
		
		public SingleVertex(int vID,SingleVertex next){
			this.vID = vID;
			this.next = next;
		}

		public int getVID() {
			return vID;
		}

		public void setVID(int vid) {
			vID = vid;
		}

		public SingleVertex getNext() {
			return next;
		}

		public void setNext(SingleVertex next) {
			this.next = next;
		}
		
		
}
public class MyVertex{
	
		private SingleVertex first;
		private int size ;
		
		public MyVertex(){
			this.first = null;
			this.size = 0;			
		}
		
		public void add(int i){
			
			//G-->V is one to one ,so don't need to judge the same Vertex,or not
			if(size == 0){//it is empty
				first = new SingleVertex(i);
			}else{
				first = new SingleVertex(i,first);
			}
			
			//modify the size
			this.size++;
		}

		public int getSize(){
			return size;
		}
		
		public int[] getVertexes(){
			if(size == 0) return null;
			int[] res = new int[size];
			int index=0;
			SingleVertex cursor = first;
			while(cursor != null&&index<size){
				 res[index++] = cursor.getVID();
				 cursor = cursor.getNext();
			}
			return res;
		}
		
		public static void main(String[] args){
			MyVertex v = new MyVertex();
			v.add(1);
			v.add(2);
			v.add(3);
			for(int j : v.getVertexes()){
				System.out.print(j +" ");
			}
			System.out.println();
		}
}

//public class MyVertex {
//	private HashSet<Integer> vertex;
//	
//	public MyVertex(){
//		this.vertex = new HashSet<Integer>();
//	}
//	
//	public void add(Integer newVertex){
//		if(!vertex.contains(newVertex)){
//			vertex.add(newVertex);
//		}
//	}
//	
//	public Iterator<Integer> getIterator(){
//		return vertex.iterator();
//	}
//	
//	public int size(){
//		return vertex.size();
//	}
//}
