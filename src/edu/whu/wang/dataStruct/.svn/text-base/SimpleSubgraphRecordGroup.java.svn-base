package edu.whu.wang.dataStruct;

public class SimpleSubgraphRecordGroup {

	private final int id;
	private final String keyword;
	private SimpleSubgraphRecord[] subgraphs;
	private int size;
	
	public SimpleSubgraphRecordGroup(int id, String keyword) {
		this.id = id;
		this.keyword = keyword;
		subgraphs = new SimpleSubgraphRecord[8];
	}
	
	public SimpleSubgraphRecordGroup(int id, String keyword, SimpleGraphIndexItem item) {
		this.id = id;
		this.keyword = keyword;
		this.subgraphs = item.getRecords();
	}

	public void addRecord(SimpleSubgraphRecord ssr) {
		if (size == subgraphs.length) {
			SimpleSubgraphRecord[] temp = subgraphs;
			subgraphs = new SimpleSubgraphRecord[2 * size];
			for (int i = 0; i < size; i++) {
				subgraphs[i] = temp[i];
			}
		}
		subgraphs[size] = ssr;
		size++;
	}
	
	public int getSize() {
		return size;
	}
	
	public int getSubgraph(int index) {
		return subgraphs[index].getSubgraph();
	}
	
	public int[] getVertexes(int index) {
		return subgraphs[index].getVertexes();
	}
}