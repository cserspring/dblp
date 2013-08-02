package edu.whu.wang.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.NoSuchElementException;

import edu.whu.wang.dataStruct.GraphMatrix;

public class GroupIteratorInFullGraph {

	private final GraphMatrix graph;
	private final int groupID;
	private final SequentialIntArray initVertexes;
	private final SequentialIntArray vertexes;

	private HashMap<Integer, SPVertex> set;
	private HashSet<Integer> hasOutput;
	private FibonacciHeapPQ<SPVertex> queue;

	public GroupIteratorInFullGraph(GraphMatrix graph,
			SequentialIntArray initVertexes, SequentialIntArray vertexes,
			int groupID) {
		this.graph = graph;
		this.groupID = groupID;

		this.initVertexes = initVertexes;
		this.vertexes = vertexes;

		this.set = new HashMap<Integer, SPVertex>();
		this.hasOutput = new HashSet<Integer>();
		this.queue = new FibonacciHeapPQ<SPVertex>();

		for (int i = 0; i < this.initVertexes.getCapacity(); i++) {
			SPVertex spv = new SPVertex(this.initVertexes.get(i), 0.0D);
			spv.setPredecessor(-1);
			int indexID = queue.enqueue(spv);
			spv.setIndexID(indexID);
			set.put(new Integer(this.initVertexes.get(i)), spv);
		}
	}

	public boolean hasNext() {
		try {
			return !(peek() == Double.MAX_VALUE);
		} catch (UnderflowException ex) {
			return false;
		}
	}

	public SPVertex next() {
		if (queue.isEmpty()) {
			throw new NoSuchElementException();
		}
		SPVertex min = null;
		try {
			min = queue.dequeueMin();
		} catch (UnderflowException ex) {
			ex.printStackTrace();
		}
		if (hasOutput.contains(min.getID())) {
			System.out.println("Error: found exsiting shortest path");
		}
		set.remove(min.getID());
		hasOutput.add(min.getID());

		int[] neighbors = graph.genNeighbors(min.getID());
		for (int i = 0; i < neighbors.length; i++) {

			if (hasOutput.contains(neighbors[i])
					|| vertexes.binarySearch(neighbors[i]) == -1) {
				continue;
			}
			SPVertex spv = set.get(neighbors[i]);
			if (spv == null) {
				spv = new SPVertex(neighbors[i], min.getDistance() + 1.0D);
				spv.setPredecessor(min.getID());
				int indexID = queue.enqueue(spv);
				spv.setIndexID(indexID);
				set.put(neighbors[i], spv);
			} else {
				double dis = min.getDistance() + 1.0D;
				if (dis < spv.getDistance()) {
					spv.setDistance(dis);
					spv.setPredecessor(min.getID());
					relax(spv);
				}
			}
		}
		return min;
	}

	private void relax(SPVertex spv) {
		try {
			queue.decreaseKey(spv.getIndexID(), spv);
		} catch (UnderflowException ex) {
			ex.printStackTrace();
		}
	}

	public double peek() throws UnderflowException {
		return queue.findMin().getDistance();
	}

	public int getGroupID() {
		return groupID;
	}

}
