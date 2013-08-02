package edu.whu.wang.index;

import java.util.ArrayList;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

public class ArrayListTupleBinding extends TupleBinding<ArrayList<Integer>> {
	public void objectToEntry(ArrayList<Integer> list, TupleOutput to) {
		for(int i = 0; i < list.size(); i++){
			to.writeInt(list.get(i));
		}
	}

	public ArrayList<Integer> entryToObject(TupleInput ti) {
		ArrayList<Integer> tmpList = new ArrayList<Integer>();
		int size = ti.getBufferLength()/4;
		for(int i = 0; i < size; i++)
			tmpList.add(ti.readInt());
		return tmpList;
	}
}