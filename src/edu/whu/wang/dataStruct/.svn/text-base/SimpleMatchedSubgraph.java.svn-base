package edu.whu.wang.dataStruct;

import java.io.File;
import java.util.ArrayList;
import edu.whu.wang.index.*;
import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

public class SimpleMatchedSubgraph {

	private int subgraph_id;
	private int[][] groups_of_matched_vertexes;
	//private String statement;

	// private SubgraphMatrix subgraph_matrix;

	public SimpleMatchedSubgraph(String statement) {
		subgraph_id = 0;

		String[] arr = statement.split(" ");
		groups_of_matched_vertexes = new int[arr.length][];
		Environment myDbEnvironment = null;
		Database myDatabase = null;
		Cursor myCursor = null;
		try {
			myDbEnvironment = new Environment(new File("dbEnv_graph"), null);
			myDatabase = myDbEnvironment.openDatabase(null, "keyword_vertex",
					null);
			myCursor = myDatabase.openCursor(null, null);
			DatabaseEntry foundKey = new DatabaseEntry();
			DatabaseEntry foundData = new DatabaseEntry();

			while (myCursor.getNext(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {

				String keyString = new String(foundKey.getData(), "UTF-8");
				for (int i = 0; i < arr.length; i++) {
					String searchString = arr[i];
					if (keyString.equals(searchString.toLowerCase())) {
						ArrayListTupleBinding bind = new ArrayListTupleBinding();
						ArrayList<Integer> list = bind.entryToObject(foundData);

						Object[] tmp = list.toArray();
						groups_of_matched_vertexes[i] = new int[tmp.length];
						for (int j = 0; j < groups_of_matched_vertexes[i].length; j++)
							groups_of_matched_vertexes[i][j] = (Integer) tmp[j];
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (myCursor != null) {
					myCursor.close();
				}
				if (myDatabase != null) {
					myDatabase.close();
				}
				if (myDbEnvironment != null) {
					myDbEnvironment.close();
				}
			} catch (DatabaseException dbe) {
				System.err.println("Error in close: " + dbe.toString());
			}
		}
	}

	public SimpleMatchedSubgraph(SimpleSubgraphRecord[] records) {

	}

	public SimpleMatchedSubgraph(int subgraph, int[][] vertexes) {
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

	public int[][] getGroups() {
		return groups_of_matched_vertexes;
	}

	// public SubgraphMatrix getSubgraphMatrix() {
	// return subgraph_matrix;
	// }
	//	
	// public void setSubgraphMatrix(SubgraphMatrix subgraph_matrix) {
	// this.subgraph_matrix = subgraph_matrix;
	// }

}
