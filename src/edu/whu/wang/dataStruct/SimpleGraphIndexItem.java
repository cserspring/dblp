package edu.whu.wang.dataStruct;

public class SimpleGraphIndexItem {

	private final SimpleSubgraphRecord[] records;
	
	public SimpleGraphIndexItem(SimpleSubgraphRecord[] records) {
		this.records = records;
	}
	
	public SimpleSubgraphRecord[] getRecords() {
		return records;
	}

}