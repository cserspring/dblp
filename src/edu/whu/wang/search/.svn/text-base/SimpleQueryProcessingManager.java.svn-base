package edu.whu.wang.search;

import java.io.IOException; //import java.util.ArrayList;
import java.util.HashSet;
//import java.util.ListIterator;

//import com.tomcat.hbase.MyHbase;

import edu.whu.wang.dataStruct.DDRadiusSubGraph; //import edu.whu.wang.dataStruct.GraphIndexItem;
import edu.whu.wang.dataStruct.GraphMatrix;
import edu.whu.wang.dataStruct.PrimitiveMatchedSubgraph;
import edu.whu.wang.dataStruct.SimpleAnswer;
import edu.whu.wang.dataStruct.SimpleMatchedSubgraph; //import edu.whu.wang.dataStruct.SimpleSubgraphRecord;
//import edu.whu.wang.dataStruct.SimpleSubgraphRecordGroup;
import edu.whu.wang.util.SearchAlgorithm;

public class SimpleQueryProcessingManager {
	private static HashSet<String> keywordSet = new HashSet<String>();
	
	private GraphMatrix graph;
	// private MyHbase myHbase;

	private static SimpleQueryProcessingManager sqpm = new SimpleQueryProcessingManager();

	private SimpleQueryProcessingManager() {
	}

	public static SimpleQueryProcessingManager getInstance() {
		return sqpm;
	}

	public void init(String path, int vSum) {
		// myHbase = MyHbase.getInstance();
		graph = new GraphMatrix(vSum);// (1534839);
		graph.loadEdges(path);
	}

	public boolean isGraphNull() {
		return graph == null ? true : false;
	}

	public int[] getNeighborsFromGraphMatrix(int id) {
		return graph.genNeighbors(id);
	}
	
	public static HashSet<String> getKeywordSet(){
		return keywordSet;
	}
	public String searchFromIndex(String statement, int k) {
		String result = "";
		SimpleAnswer[] answers = null;
		try {
			answers = search(statement, k);
			if (answers != null) {
				double sum = 0.0d;
				int i = 0;
				for (; i < answers.length; i++) {
					SimpleAnswer at = answers[i];
					if (at == null) {
						break;
					}
					result += "Rank " + (i + 1) + ": {" + at.getScore() + "} ";
					result += " root[" + at.getRoot() + "] ";
					sum += at.getScore();
					for (int j = 0; j < at.getLeafNumber(); j++) {
						int[] path = at.getPath(j);
						result += "( ";
						if (path != null) {
							for (int m = 0; m < path.length; m++) {
								result += path[m] + ", ";
							}
						}
						result += " )";
					}
					// result +="<br>";
					System.out.println(result);
					result = "";
					System.out.println(at.treeToString());

				}
				if (i == 0) {
					result += "Sorry, no result is found.";
				} else {
					result += "Average score: " + (sum / Math.min(k, i));
				}

				// System.out.println("Total time exhausted: " +
				// (endTime.getTime() - startTime.getTime()) + " ms");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	private SimpleAnswer[] search(String statement, int k) throws IOException {

		String[] keywords = statement.split(" ");
		for (int i = 0; i < keywords.length; i++)
			keywordSet.add(keywords[i].toLowerCase());
		// int[] ks = new int[] { 10, 25, 50 };//
		// int z = 1;

		try {
			// search
			// SimpleSubgraphRecordGroup[] groups = produceGroups(keywords);

			// union
			SimpleMatchedSubgraph[] matched_subgraphs = new SimpleMatchedSubgraph[1];
			matched_subgraphs[0] = new SimpleMatchedSubgraph(statement);
			// for (int w = 0; w < z; w++) {
			// matched_subgraphs = intersectGroups(groups);
			// }
			if (matched_subgraphs == null) {
				return null;
			}

			SearchAlgorithm sa = null;
			// statistic in matched subgraphs
			int[] vertexNum = new int[keywords.length];
			for (int i = 0; i < matched_subgraphs.length; i++) {
				for (int j = 0; j < vertexNum.length; j++) {
					System.out.println(keywords[j] + "匹配节点的个数：" + matched_subgraphs[i].getGroup(j).length);
					vertexNum[j] += matched_subgraphs[i].getGroup(j).length;
				}
			}

			if (matched_subgraphs.length == 0) {
				return null;
			}

			PrimitiveMatchedSubgraph[] pmss = loadRRadiusSubGraphs(matched_subgraphs);

			sa = new BestAlgorithm(graph, pmss);

			SimpleAnswer[] result = null;

			sa.init();
			result = sa.process(k);
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	// private SimpleSubgraphRecordGroup[] produceGroups(String[] keywords) {
	// int size = keywords.length;
	// SimpleSubgraphRecordGroup[] groups = new SimpleSubgraphRecordGroup[size];
	// for (int i = 0; i < size; i++) {
	// groups[i] = lookupGraphIndex(i + 1, keywords[i]);
	// }
	// return groups;
	// }
	//
	// private SimpleMatchedSubgraph[] intersectGroups(
	// SimpleSubgraphRecordGroup[] groups) {
	// ArrayList<SimpleMatchedSubgraph> matched = new
	// ArrayList<SimpleMatchedSubgraph>();
	//
	// if (groups[0] != null) {
	// for (int i = 0; i < groups[0].getSize(); i++) {
	// boolean match = true;
	// int[][] temp = new int[groups.length][];
	// temp[0] = groups[0].getVertexes(i);
	// for (int j = 1; j < groups.length; j++) {
	// if (groups[j] == null) {
	// return null;
	// }
	// if (match) {
	// match = false;
	//
	// int low = 0;
	// int mid = 0;
	// int top = groups[j].getSize() - 1;
	//
	// while (low <= top) {
	// mid = (low + top) / 2;
	// if (groups[0].getSubgraph(i) < groups[j]
	// .getSubgraph(mid)) {
	// top = mid - 1;
	// } else if (groups[0].getSubgraph(i) > groups[j]
	// .getSubgraph(mid)) {
	// low = mid + 1;
	// } else if (groups[0].getSubgraph(i) == groups[j]
	// .getSubgraph(mid)) {
	// match = true;
	// temp[j] = groups[j].getVertexes(mid);
	// break;
	// }
	// }
	// } else {
	// break;
	// }
	// }
	// if (match) {
	// matched.add(new SimpleMatchedSubgraph(groups[0]
	// .getSubgraph(i), temp));
	// }
	// }
	//
	// SimpleMatchedSubgraph[] result = new SimpleMatchedSubgraph[matched
	// .size()];
	// ListIterator<SimpleMatchedSubgraph> it = matched.listIterator();
	// int i = 0;
	// while (it.hasNext()) {
	// result[i] = it.next();
	// i++;
	// }
	// return result;
	// }
	// return null;
	// }

	private PrimitiveMatchedSubgraph[] loadRRadiusSubGraphs(
			SimpleMatchedSubgraph[] matched_subgraphs) {
		PrimitiveMatchedSubgraph[] result = new PrimitiveMatchedSubgraph[matched_subgraphs.length];
		for (int i = 0; i < matched_subgraphs.length; i++) {
			try {
				// DDRadiusSubGraph rrsg =
				// myHbase.searchFromGraphToVertext(matched_subgraphs[i].getSubgraphID());
				// 2011-05-03
				DDRadiusSubGraph rrsg = new DDRadiusSubGraph();
				PrimitiveMatchedSubgraph pms = new PrimitiveMatchedSubgraph(
						matched_subgraphs[i].getSubgraphID(),
						matched_subgraphs[i].getGroups());
				pms.setSubgraph(rrsg);
				result[i] = pms;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	// private SimpleSubgraphRecordGroup lookupGraphIndex(int id, String
	// keyword) {
	// SimpleSubgraphRecordGroup ssrg = new SimpleSubgraphRecordGroup(id,
	// keyword);
	// // GraphIndexItem gii = myHbase.searchFromIndex(keyword);
	// // if (gii == null) {
	// // return null;
	// // }
	// // int[] subgraphs = gii.getGraphs();
	// int[] subgraphs = { 0, 1 };
	// GraphIndexItem gii = new GraphIndexItem();
	// // for (int i = 0; i < subgraphs.length; i++) {
	// // int[] vertexes = gii.getVertexGroup(i);
	// SimpleSubgraphRecord ssr = new SimpleSubgraphRecord(subgraphs[0], gii
	// .getVertexGroup(0));
	// ssrg.addRecord(ssr);
	// // }
	// return ssrg;
	// }
}
