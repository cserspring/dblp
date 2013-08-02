package edu.whu.wang.setup;

import java.io.UnsupportedEncodingException;

import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.SecondaryKeyCreator;

public class PersonKeyCreator implements SecondaryKeyCreator {

	public boolean createSecondaryKey(SecondaryDatabase secondary,
			DatabaseEntry key, DatabaseEntry data, DatabaseEntry result)
			throws DatabaseException {
		try {
			String value = new String(data.getData(), "UTF-8");
			if (value.indexOf("type:person@@") != -1) {
				String str = "@@name:";
				int pos = value.indexOf(str);
				String theKey = value.substring(pos + str.length());
				result.setData(theKey.getBytes("UTF-8"));
				return true;
			}
		}
		catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
			System.err.println("Error: creating secondary key for person");
		}
		return false;
	}

}
