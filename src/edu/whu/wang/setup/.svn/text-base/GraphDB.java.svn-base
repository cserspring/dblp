package edu.whu.wang.setup;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import com.sleepycat.bind.tuple.IntegerBinding;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.SecondaryConfig;
import com.sleepycat.je.SecondaryDatabase;

public class GraphDB {

	private String dbEnvName;
	private Environment env;
	private Database vertex;
	private Database edge;
	private Database ref_cite;
	private Database lost;
	private HashMap<String, SecondaryDatabase> secDBs = new HashMap<String, SecondaryDatabase>();
	private SecondaryDatabase keys;
	private SecondaryDatabase persons;
	private SecondaryDatabase publishers;
	private SecondaryDatabase books;
	private SecondaryDatabase schools;
	
	private Database vertex_keyword;
	
	public GraphDB() {
		this.dbEnvName = "dbEnv";//get the environment's name
	}
	
	public GraphDB(String dbEnv) {
		this.dbEnvName = dbEnv;
	}
	
	public boolean initEnv() {
		EnvironmentConfig envConfig = new EnvironmentConfig();
		envConfig.setAllowCreate(true);
		
		try {
			this.env = new Environment(new File(dbEnvName), envConfig);
			return true;
		}
		catch (DatabaseException ex) {
			ex.printStackTrace();
			System.err.println("Error: opening database environment");
			return false;
		}
	}
	
	public boolean openDB() {
		try {
			DatabaseConfig dbConfig = new DatabaseConfig();
			dbConfig.setAllowCreate(true);
			vertex = env.openDatabase(null, "vertex", dbConfig);
			edge = env.openDatabase(null, "edge", dbConfig);
			ref_cite = env.openDatabase(null, "ref_cite", dbConfig);
			lost = env.openDatabase(null, "lost", dbConfig);
			
			//Open Secondary Database: keys
			SecondaryConfig keySecConfig = new SecondaryConfig();
			keySecConfig.setAllowCreate(true);
			keySecConfig.setKeyCreator(new KeyKeyCreator());
			keys = env.openSecondaryDatabase(null, "keys", vertex, keySecConfig);
			secDBs.put("keys", keys);
			
			//Open Secondary Database: persons
			SecondaryConfig personSecConfig = new SecondaryConfig();
			personSecConfig.setAllowCreate(true);
			personSecConfig.setKeyCreator(new PersonKeyCreator());
			persons = env.openSecondaryDatabase(null, "persons", vertex, personSecConfig);
			secDBs.put("persons", persons);
			
			//Open Secondary Database: publishers
			SecondaryConfig publisherSecConfig = new SecondaryConfig();
			publisherSecConfig.setAllowCreate(true);
			publisherSecConfig.setKeyCreator(new PublisherKeyCreator());
			publishers = env.openSecondaryDatabase(null, "publishers", vertex, publisherSecConfig);
			secDBs.put("publishers", publishers);
			
			//Open Secondary Database: books
			SecondaryConfig bookSecConfig = new SecondaryConfig();
			bookSecConfig.setAllowCreate(true);
			bookSecConfig.setKeyCreator(new BookKeyCreator());
			books = env.openSecondaryDatabase(null, "books", vertex, bookSecConfig);
			secDBs.put("books", books);
			
			//Open Secondary Database: schools
			SecondaryConfig schoolSecConfig = new SecondaryConfig();
			schoolSecConfig.setAllowCreate(true);
			schoolSecConfig.setKeyCreator(new SchoolKeyCreator());
			schools = env.openSecondaryDatabase(null, "schools", vertex, bookSecConfig);
			secDBs.put("schools", schools);
			
			return true;
		}
		catch (DatabaseException ex) {
			ex.printStackTrace();
			System.err.println("Error: opening databases");
			return false;
		}
	}
	
	public void closeDB() {
		try {
			if (schools != null) {
				schools.close();
			}
			if (books != null) {
				books.close();
			}
			if (publishers != null) {
				publishers.close();
			}
			if (persons != null) {
				persons.close();
			}
			if (keys != null) {
				keys.close();
			}
			if (lost != null) {
				lost.close();
			}
			if (ref_cite != null) {
				ref_cite.close();
			}
			if (edge != null) {
				edge.close();
			}
			if (vertex != null) {
				vertex.close();
			}
			if (env != null) {
				env.close();
			}
		}
		catch (DatabaseException ex) {
			System.err.println("Error: closing databases and environment.");
			ex.printStackTrace();
		}
	}
	
	public void printDB(FileWriter vOutput, FileWriter eOutput, FileWriter rOutput, FileWriter lOutput) {
		Cursor vCursor = null;
		Cursor eCursor = null;
		Cursor rCursor = null;
		Cursor lCursor = null;
		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry value = new DatabaseEntry();
		try {
			vCursor = vertex.openCursor(null, null);
			while (vCursor.getNext(key, value, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
				String strKey = "" + IntegerBinding.entryToInt(key);
				String strValue = new String(value.getData(), "UTF-8");
				//delete "key" attribute
				int pos = strValue.indexOf("@@key:");
				if (pos != -1) {
					int pos2 = strValue.indexOf("@@", pos + 6);
					if(pos2 == -1){
						System.err.println(strValue);
					}
					strValue = strValue.substring(0, pos) + strValue.substring(pos2);
				}
				for (int i = 0; i < (8 - strKey.length()); i++) {
					vOutput.write(" ");
				}
				vOutput.write(strKey + "     " + strValue + "\n");
			}
			
			eCursor = edge.openCursor(null, null);
			while (eCursor.getNext(key, value, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
				String strKey = "" + IntegerBinding.entryToInt(key);
				String strValue = new String(value.getData(), "UTF-8");
				for (int i = 0; i < (8 - strKey.length()); i++) {
					eOutput.write(" ");
				}
				eOutput.write(strKey + "     " + strValue + "\n");
			}
			
			rCursor = ref_cite.openCursor(null, null);
			while (rCursor.getNext(key, value, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
				String strKey = "" + IntegerBinding.entryToInt(key);
				String strValue = new String(value.getData(), "UTF-8");
				for (int i = 0; i < (8 - strKey.length()); i++) {
					rOutput.write(" ");
				}
				rOutput.write(strKey + "     " + strValue + "\n");
			}
			
			lCursor = lost.openCursor(null, null);
			while (lCursor.getNext(key, value, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
				String strKey = "" + IntegerBinding.entryToInt(key);
				String strValue = new String(value.getData(), "UTF-8");
				for (int i = 0; i < (8 - strKey.length()); i++) {
					lOutput.write(" ");
				}
				lOutput.write(strKey + "     " + strValue + "\n");
			}
		}
		catch (DatabaseException ex) {
			ex.printStackTrace();
			System.err.println("Error: printing databases");
		}
		catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
		finally {
			try {
				if (vCursor != null) {
					vCursor.close();
				}
				if (eCursor != null) {
					eCursor.close();
				}
				if (rCursor != null) {
					rCursor.close();
				}
				if (lCursor != null) {
					lCursor.close();
				}
			}
			catch (DatabaseException ex) {
				ex.printStackTrace();
				System.err.println("Error: closing cursor");
			}
		}
	}
	
	// To produce a vertex_keyword database
	public void parseVertexes() {
		Cursor cursor = null;
		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry value = new DatabaseEntry();
		try {
			cursor = vertex.openCursor(null, null);
			while (cursor.getNext(key, value, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
				Integer intKey = new Integer(IntegerBinding.entryToInt(key));
				String strValue = new String(value.getData(), "UTF-8");
			}
		}
		catch (DatabaseException ex) {
			ex.printStackTrace();
		}
		catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
		finally {
			try {
				if (cursor != null) {
					cursor.close();
				}
			}
			catch (DatabaseException ex) {
				ex.printStackTrace();
				System.err.println("Error: closing cursor");
			}
		}
	}
	
	public HashMap<Integer, String> getRef_Cite() {
		HashMap<Integer, String> result = new HashMap<Integer, String>();
		Cursor cursor = null;
		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry value = new DatabaseEntry();
		try {
			cursor = ref_cite.openCursor(null, null);
			while (cursor.getNext(key, value, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
				Integer intKey = new Integer(IntegerBinding.entryToInt(key));
				String strValue = new String(value.getData(), "UTF-8");
				result.put(intKey, strValue);
			}
			return result;
		}
		catch (DatabaseException ex) {
			ex.printStackTrace();
		}
		catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
		finally {
			try {
				if (cursor != null) {
					cursor.close();
				}
			}
			catch (DatabaseException ex) {
				ex.printStackTrace();
				System.err.println("Error: closing cursor");
			}
		}
		return null;
	}
	
	public void delRef_Cite(int rcid) throws UnsupportedEncodingException, DatabaseException {
		DatabaseEntry key = new DatabaseEntry();
		IntegerBinding.intToEntry(rcid, key);
		ref_cite.delete(null, key);
	}
	
	public void insert(boolean isVertex, int key, String value) throws UnsupportedEncodingException, DatabaseException {
		DatabaseEntry deKey = new DatabaseEntry();
		DatabaseEntry deValue = new DatabaseEntry(value.getBytes("UTF-8"));
		IntegerBinding.intToEntry(key, deKey);
		if (isVertex) {
			vertex.put(null, deKey, deValue);
		}
		else {
			edge.put(null, deKey, deValue);
		}
	}
	
	public void insertRef_Cite(int key, String value) throws UnsupportedEncodingException, DatabaseException {
		DatabaseEntry deKey = new DatabaseEntry();
		DatabaseEntry deValue = new DatabaseEntry(value.getBytes("UTF-8"));
		IntegerBinding.intToEntry(key, deKey);
		ref_cite.put(null, deKey, deValue);
	}
	
	public void insertLost(int key, String value) throws UnsupportedEncodingException, DatabaseException {
		DatabaseEntry deKey = new DatabaseEntry();
		DatabaseEntry deValue = new DatabaseEntry(value.getBytes("UTF-8"));
		IntegerBinding.intToEntry(key, deKey);
		lost.put(null, deKey, deValue);
	}
	
	public int checkKey(String key) {
		return check("keys", key);
	}
	
	public int checkPerson(String name) {
		return check("persons", name);
	}
	
	public int checkPublisher(String name) {
		return check("publishers", name);
	}
	
	public int checkBook(String title) {
		return check("books", title);
	}
	
	public int checkSchool(String name) {
		return check("schools", name);
	}
	
	public int check(String dbName, String keyword) {
		try {
			DatabaseEntry searchEntry = new DatabaseEntry(keyword.getBytes("UTF-8"));
			DatabaseEntry key = new DatabaseEntry();
			DatabaseEntry value = new DatabaseEntry();
			SecondaryDatabase secDB = secDBs.get(dbName);
			if (secDB.get(null, searchEntry, key, value, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
				int id = IntegerBinding.entryToInt(key);
				return id;
			}
		}
		catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
		catch (DatabaseException ex) {
			ex.printStackTrace();
			System.err.println("Error: checking books");
		}
		return -1;
	}
	
	public Database getVertex() {
		return vertex;
	}
	
	public String get(int key) throws UnsupportedEncodingException, DatabaseException {
		DatabaseEntry keyword = new DatabaseEntry();
		DatabaseEntry list = new DatabaseEntry();
		IntegerBinding.intToEntry(key, keyword);
		if (vertex.get(null, keyword, list, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
			return new String(list.getData(), "UTF-8");
		}
		else {
			return null;
		}
	}
	
}
