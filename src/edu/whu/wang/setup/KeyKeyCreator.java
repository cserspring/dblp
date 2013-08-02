package edu.whu.wang.setup;

import java.io.UnsupportedEncodingException;

import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.SecondaryKeyCreator;

public class KeyKeyCreator implements SecondaryKeyCreator {

	public boolean createSecondaryKey(SecondaryDatabase secondary,
			DatabaseEntry key, DatabaseEntry data, DatabaseEntry result)
			throws DatabaseException {
		try {
			String value = new String(data.getData(), "UTF-8");
			String str = "@@key:";
			int pos = value.indexOf(str);
			if (pos != -1) {
				String rest = value.substring(pos + str.length());
				pos = rest.indexOf("@@");
				String theKey = null;
				if (pos != -1) {
					theKey = rest.substring(0, pos);
				}
				else {
					theKey = rest.substring(0);
				}
				result.setData(theKey.getBytes("UTF-8"));
				return true;
			}
		}
		catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
			System.err.println("Error: creating secondary key for key");
		}
		return false;
	}

}
