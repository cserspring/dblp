package edu.whu.wang.dataStruct;

import java.io.File;
import java.util.ArrayList;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

public class SimpleSubgraphRecord {

	private final int subgraph_id;
	private final int[] matched_vertex_ids;
	
	public SimpleSubgraphRecord(int subgraph_id, int[] vertex_ids) {
		this.subgraph_id = subgraph_id;
		this.matched_vertex_ids = vertex_ids;
	}
	
	public SimpleSubgraphRecord() {
		subgraph_id = 0;
		
		ArrayList<Integer> tmpList = new ArrayList<Integer>();
		Environment myDbEnvironment = null;
		Database myDatabase = null;
		Cursor myCursor = null;
		try {
			myDbEnvironment = new Environment(new File("dbEnv_graph"), null);
			myDatabase = myDbEnvironment.openDatabase(null, "vertex",
					null);
			myCursor = myDatabase.openCursor(null, null);
			DatabaseEntry foundKey = new DatabaseEntry();
			DatabaseEntry foundData = new DatabaseEntry();

			while (myCursor.getNext(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
//
//				String keyString = new String(foundKey.getData(), "UTF-8");
//				String searchString = "Specifying";
//				if (keyString.equals(searchString.toLowerCase())) {
//					ArrayListTupleBinding bind = new ArrayListTupleBinding();
//					ArrayList<Integer> list = bind.entryToObject(foundData);
//					//String dataString = new String(foundData.getData(), "UTF-8");
//					System.out.println("匹配总数为：" + list.size());
//					for(int i = 0; i < list.size(); i++)
//					System.out.println("Key | Data : " + keyString + " | "
//							+ list.get(i) + "");
//				}
				EntryBinding myBinding  = TupleBinding.getPrimitiveBinding(Integer.class);
				Integer i = (Integer)myBinding.entryToObject(foundKey);
				tmpList.add(i);
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
		matched_vertex_ids = new int[tmpList.size()];
		for(int i = 0; i < tmpList.size(); i++){
			matched_vertex_ids[i] = tmpList.get(i);
		}
	}
	public int getSubgraph() {
		return subgraph_id;
	}
	
	public int[] getVertexes() {
		return matched_vertex_ids;
	}

}
