package edu.whu.wang.util;

public interface MyPriorityQueue<E extends Comparable<? super E>> {

		public boolean isEmpty();
		public void clear();
		public int enqueue(E e);
		public E findMin() throws UnderflowException;
		public E dequeueMin() throws UnderflowException;
		public void decreaseKey(int indexID, E e) throws UnderflowException;

}
