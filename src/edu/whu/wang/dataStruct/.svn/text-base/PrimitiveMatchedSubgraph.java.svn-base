package edu.whu.wang.dataStruct;

public class PrimitiveMatchedSubgraph {

	private int subgraph_id;
	private int[][] groups_of_matched_vertexes;
	private DDRadiusSubGraph subgraph;

	public PrimitiveMatchedSubgraph(SimpleSubgraphRecord[] records) {

	}
	
	public PrimitiveMatchedSubgraph(int subgraph, int[][] vertexes) {
		this.subgraph_id = subgraph;
		this.groups_of_matched_vertexes = vertexes;
	}
	
	public int getSubgraphID() {
		return subgraph_id;
	}

	public int groupNum() {
		return groups_of_matched_vertexes.length;
	}

	public int groupSize(int group_id) {
		return groups_of_matched_vertexes[group_id].length;
	}

	public int getVertex(int group_id, int index) {
		return groups_of_matched_vertexes[group_id][index];
	}
	
	public int[] getGroup(int index) {
		return groups_of_matched_vertexes[index];
	}
	
	public DDRadiusSubGraph getSubgraph() {
		return subgraph;
	}
	
	public void setSubgraph(DDRadiusSubGraph subgraph) {
		this.subgraph = subgraph;
	}

}
