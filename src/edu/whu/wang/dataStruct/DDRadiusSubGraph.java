package edu.whu.wang.dataStruct;

import java.io.*;
import java.util.*;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.je.*;

import edu.whu.wang.index.ArrayListTupleBinding;

public class DDRadiusSubGraph {
	
	private int row;/**gid*/
	private int[] vertexes;/**节点集合*/
	
	public DDRadiusSubGraph() {
		row = 0;
		
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
		vertexes = new int[tmpList.size()];
		for(int i = 0; i < tmpList.size(); i++){
			vertexes[i] = tmpList.get(i);
		}
	}
	public DDRadiusSubGraph(int row, int[] vertexes) {
		this.row = row;
		this.vertexes = vertexes;
	}


	public int getRow() {
		return row;
	}


	public void setRow(int row) {
		this.row = row;
	}


	public int[] getVertexes() {
		return vertexes;
	}


	public void setVertexes(int[] vertexes) {
		this.vertexes = vertexes;
	}

	
	
}
