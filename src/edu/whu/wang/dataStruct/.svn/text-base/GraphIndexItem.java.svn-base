package edu.whu.wang.dataStruct;

import edu.whu.wang.util.SequentialIntArray;

public class GraphIndexItem {

	private int[] graphs;
	private SequentialIntArray[] vertexGroups;
	
	private int size;
	
	private short k = 4;
	
	private final static int default_size_of_graphs = 4;
	
	public GraphIndexItem() {
		this.graphs = new int[GraphIndexItem.default_size_of_graphs];
		this.vertexGroups = new SequentialIntArray[GraphIndexItem.default_size_of_graphs];
	}
	
	public GraphIndexItem(int size, int[] graphs, SequentialIntArray[] vertexGroups) {
		this.size = size;
		this.graphs = graphs;
		this.vertexGroups = vertexGroups;
	}
	
	public void add(int[] newGraphs, int vertex) {
		int low = 0;
		for (int i = 0; i < newGraphs.length; i++) {
			int top = size - 1;
			while (low <= top) {
				int mid = (low + top) / 2;
				if (newGraphs[i] < graphs[mid]) {
					top = mid - 1;
				}
				else if (newGraphs[i] > graphs[mid]) {
					low = mid + 1;
				}
				this.addGraph(low, newGraphs[i], vertex);
			}
		}
	}
	
	private void addGraph(int index, int graph/*, int sgkn*/, int vertex) {
		if (size == graphs.length) {
			int[] temp1 = graphs;
			graphs = new int[2 * graphs.length];
			SequentialIntArray[] temp2 = vertexGroups;
			vertexGroups = new SequentialIntArray[2 * vertexGroups.length];
			for (int i = 0; i < temp1.length; i++) {
				graphs[i] = temp1[i];
				vertexGroups[i] = temp2[i];
			}
		}
		
		for (int i = size; i > index; i--) {
			graphs[i] = graphs[i - 1];
			vertexGroups[i] = vertexGroups[i - 1];
		}
		graphs[index] = graph;
		vertexGroups[index] = new SequentialIntArray(k);
		vertexGroups[index].insert(vertex);
		size++;
	}

	public int getSize() {
		return size;
	}
	
	public int[] getGraphs() {
		return graphs;
	}
	
	public int getGraph(int index) {
		return graphs[index];
	}

	public int getVertexGroupSize(int index) {
		return vertexGroups[index].getCapacity();
	}
	
	public int getVertex(int index1, int index2) {
		return vertexGroups[index1].get(index2);
	}
	
	public int[] getVertexGroup(int index) {
		return vertexGroups[index].getArray();
	}
}
