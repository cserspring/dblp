package edu.whu.wang.setup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.sleepycat.je.DatabaseException;

public class DBLP {

	//log
	private FileWriter errLog;
	
	//ids
	private int vid = 0;
	private int eid = 0;
	private int rcid = 0;
	private int lid = 0;
	
	//statistic
	private int topTagNum = 0;     //Total number of top-level tags in the xml document
	private int genEntNum = 0;     //Total number of new generated entities
	
	//database
	private GraphDB gdb;
	
	public DBLP() {
		gdb = new GraphDB();
	}
	
	public DBLP(String bdbFile) {
		gdb = new GraphDB(bdbFile);
	}

	public static void main(String[] args) {
		
		File file = new File("dbEnv_graph");
		if(!file.exists()) file.mkdirs();
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f:files) {
				f.delete();
			}
		}
		
		DBLP dblp = new DBLP("dbEnv_graph");
		dblp.init();//初始化 primary database和secondaryDatabase
		
		Date stime = new Date();
		
//		dblp.X2G("files/dblp.xml");//_5000029.xml");//主要是得到信息，并插入到vertex数据库中
		//dblp.DX2G("dblp-splitted-files");
		dblp.X2G("files/dblp_5000029.xml");
		dblp.procRef_Cite();//插入到edge数据库中
		
		Date etime = new Date();
		dblp.print();
		dblp.close();
		dblp.statistic();
		System.out.println("Time exhausted: " + (etime.getTime() - stime.getTime()) + " ms");
	}
	
	public void init() {
		if (!gdb.initEnv()) {
			System.exit(1);
		}
		if (!gdb.openDB()) {
			System.exit(1);
		}
		try {
			errLog = new FileWriter("err_log.txt");
		}
		catch (IOException ex) {
			ex.printStackTrace();
			System.err.println("Error: creating log");
		}
	}
	
	public void close() {
		gdb.closeDB();
		try {
			errLog.close();
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public void DX2G(String dname) {
		File file = new File(dname);
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				X2G(files[i]);
			}
		}
	}
	
	public void X2G(String xmlFile) {
		File file = new File(xmlFile);
		X2G(file);
	}
	
	public void X2G(File xmlFile) {
		InputStream is = null;
		XMLStreamReader xsr = null;
		XMLInputFactory xif = XMLInputFactory.newInstance();
		
		try {
			is = new FileInputStream(xmlFile);
			xsr = xif.createXMLStreamReader(is);
			
			HashSet<Integer> authors = null;  //author ids
			HashSet<Integer> editors = null;  //editor ids
			String booktitle = null;          //book id
			String journal = null;
			int school = 0;
			int publisher = 0;                //publisher id
			String crossref = null;           //
			HashSet<Integer> validCites = null;     //
			HashSet<String> invalidCites = null;     //
			String head = null;
			String title = null;
			String attrs = null;
			
			String tag = null;
			String mark = null;
			
			for (int event = xsr.next(); event != XMLStreamReader.END_DOCUMENT; event = xsr.next()) {
//				if (vid % 1000 == 0) {
//					System.gc();
//				}
				switch (event) {
				case XMLStreamReader.START_ELEMENT:
					tag = xsr.getLocalName();
					if (tag.equals("inproceedings")) {
						topTagNum++;
						mark = tag;
						authors = new HashSet<Integer>();
						editors = new HashSet<Integer>();
						booktitle = null;
						crossref = null;
						validCites = new HashSet<Integer>();
						invalidCites = new HashSet<String>();
						head = "type:paper@@key:" + getKey(xsr);
						title = "";
						attrs = "@@";
					}
					else if (tag.equals("article")) {
						topTagNum++;
						mark = tag;
						authors = new HashSet<Integer>();
						editors = new HashSet<Integer>();
						booktitle = null;
						journal = null;
						crossref = null;
						validCites = new HashSet<Integer>();
						invalidCites = new HashSet<String>();
						head = "type:paper@@key:" + getKey(xsr);
						title = "";
						attrs = "@@";
					}
					else if (tag.equals("incollection")) {
						topTagNum++;
						mark = tag;
						authors = new HashSet<Integer>();
						booktitle = null;
						crossref = null;
						validCites = new HashSet<Integer>();
						invalidCites = new HashSet<String>();
						head = "type:paper@@key:" + getKey(xsr);
						title = "";
						attrs = "@@";
					}
					else if (tag.equals("proceedings")) {
						topTagNum++;
						mark = tag;
						authors = new HashSet<Integer>();
						editors = new HashSet<Integer>();
						booktitle = null;
						publisher = 0;
						validCites = new HashSet<Integer>();
						invalidCites = new HashSet<String>();
						head = "type:proceedings@@key:" + getKey(xsr);
						title = "";
						attrs = "@@";
					}
					else if (tag.equals("book")) {
						topTagNum++;
						mark = tag;
						authors = new HashSet<Integer>();
						editors = new HashSet<Integer>();
						publisher = 0;
						booktitle = null;
						validCites = new HashSet<Integer>();
						invalidCites = new HashSet<String>();
						head = "type:book@@key:" + getKey(xsr);
						title = "";
						attrs = "@@";
					}
					else if (tag.equals("www")) {
						topTagNum++;
						mark = tag;
						authors = new HashSet<Integer>();
						editors = new HashSet<Integer>();
						validCites = new HashSet<Integer>();
						invalidCites = new HashSet<String>();
						head = "type:www@@key:" + getKey(xsr);
						title = "";
						attrs = "@@";
					}
					else if (tag.equals("phdthesis") | tag.equals("mastersthesis")) {
						topTagNum++;
						mark = tag;
						authors = new HashSet<Integer>();
						school = 0;
						head = "type:" + tag + "@@key:" + getKey(xsr);
						title = "";
						attrs = "@@";
					}
					else if (tag.equals("title")) {
						title += "@@title:";
					}
					else if (tag.equals("author")) {
						String name = xsr.getElementText().trim();
						int id = gdb.checkPerson(name);
						if (id == -1) {
							genEntNum++;
							vid++;
							String value = "type:person@@name:" + name;
							gdb.insert(true, vid, value);
							authors.add(new Integer(vid));
						}
						else {
							authors.add(new Integer(id));
						}
					}
					else if (tag.equals("editor")) {
						if (mark.equals("proceedings") || mark.equals("book") || mark.equals("article") || 
								mark.equals("inproceedings") || mark.equals("www")) {
							String name = xsr.getElementText().trim();
							int id = gdb.checkPerson(name);
							if (id == -1) {
								genEntNum++;
								vid++;
								String value = "type:person@@name:" + name;
								gdb.insert(true, vid, value);
								editors.add(new Integer(vid));
							}
							else {
								editors.add(new Integer(id));
							}
						}
						else {
							errLog.write("Found a new entity containing editor: " + mark + "#" + vid + "\n");
						}
					}
					else if (tag.equals("booktitle")) {
						if (mark.equals("inproceedings") || mark.equals("incollection") || mark.equals("article")) {
							booktitle = xsr.getElementText().trim();
							attrs += tag + ":" + booktitle + "@@";
						}
						else if (mark.equals("proceedings") || mark.equals("book") || mark.equals("www")) {
							attrs += tag + ":" + xsr.getElementText().trim() + "@@";
						}
						else {
							errLog.write("Found another entity having booktitle: " + mark + "#" + vid + "\n");
						}
					}
					else if (tag.equals("journal")) {
						if (mark.equals("article")) {
							journal = xsr.getElementText().trim();
							attrs += tag + ":" + journal + "@@";
						}
						else if (mark.equals("proceedings")) {
							attrs += tag + ":" + xsr.getElementText().trim() + "@@";
						}
						else {
							errLog.write("Found another entity having journal: " + mark + "#" + vid+ "\n");
						}
					}
					else if (tag.equals("pages") | tag.equals("year") | tag.equals("month") | 
							tag.equals("isbn") | tag.equals("volume") | tag.equals("series") | 
							tag.equals("address") | tag.equals("number") | tag.equals("chapter")) {
						attrs += tag + ":" + xsr.getElementText().trim() + "@@";
					}
					else if (tag.equals("publisher")) {
						String name = xsr.getElementText().trim();
						int id = gdb.checkPublisher(name);
						if (id == -1) {
							genEntNum++;
							vid++;
							String value = "type:publisher@@name:" + name;
							gdb.insert(true, vid, value);
							publisher = vid;
						}
						else {
							publisher = id;
						}
					}
					else if (tag.equals("school")) {
						String name = xsr.getElementText().trim();
						int id = gdb.checkSchool(name);
						if (id == -1) {
							genEntNum++;
							vid++;
							String value = "type:school@@name:" + name;
							gdb.insert(true, vid, value);
							school = vid;
						}
						else {
							school = id;
						}
					}
					else if (tag.equals("cite")) {
						if (mark.equals("inproceedings") || mark.equals("incollection") || mark.equals("article") || 
								mark.equals("www") || mark.equals("proceedings") || mark.equals("book")) {
							String name = xsr.getElementText().trim();
							if (!name.equals("...")) {
								int id = gdb.checkKey(name);
								if (id != -1) {
									validCites.add(new Integer(id));
								}
								else {
									invalidCites.add(name);
								}
							}
						}
						else {
							errLog.write("Found another entity having cite: " + mark + "#" + vid + "\n");
						}
					}
					else if (tag.equals("crossref")) {
						crossref = xsr.getElementText().trim();
					}
					else if (tag.equals("sub") | tag.equals("sup") | tag.equals("i") |
							tag.equals("tt") | tag.equals("ref")) {
						title += "<" + xsr.getLocalName() + ">";
					}
					else if (tag.equals("url")) {
						if (mark.equals("www")) {
							attrs += tag + ":" + xsr.getElementText().trim() + "@@";
						}
						else {
							xsr.getElementText();
						}
					}
					else if (tag.equals("cdrom") | tag.equals("note") | tag.equals("ee")) {
						xsr.getElementText();
					}
					else if (tag.equals("dblp")) {
						System.out.println("Parsing started...");
					}
					else {
						errLog.write("Found unknown tag: " + tag + "\n");
					}
					break;
				case XMLStreamReader.END_ELEMENT:
					tag = xsr.getLocalName();
					if (tag.equals(mark)) {
						//Adding a vertex for a top-level tag
						vid++;
						gdb.insert(true, vid, head + title + attrs.substring(0, attrs.length() - 2));//+"\n");//插入'\n'，用于文本的显示
						if (title.length() == 0) {
							errLog.write("Found an entity having no title: " + mark + "#" + vid);
						}
						
						//Adding edges for this top-level tag						
						if (mark.equals("inproceedings")) {
							genRel4Set(authors, "write");//author-paper关系边
							genRel4Set(editors, "edit");
							if (booktitle != null) {
								genRel4Str(crossref, "inproceedings", booktitle);//paper-book（paper-conference）关系
							}
							else {
								errLog.write("Found an inproceedings having no booktitle: #" + vid+ "\n");
							}
							genInvRel4Set(validCites, "cite");//paper-paper关系，之前已找到
							collectCites(invalidCites);//paper-paper关系，之前没找到，先收集起来
							
						}
						else if (mark.equals("article")) {
							genRel4Set(authors, "write");
							genRel4Set(editors, "edit");
							if (journal != null) {
								genRel4Str(crossref, "inproceedings", journal);
							}
							else {
								if (booktitle != null) {
									genRel4Str(crossref, "inproceedings", booktitle);
								}
								else {
									errLog.write("Found an article having neither journal nor booktitle: #" + vid+ "\n");
								}
							}
							genInvRel4Set(validCites, "cite");
							collectCites(invalidCites);
						}
						else if (mark.equals("incollection")) {
							genRel4Set(authors, "write");
							if (booktitle != null) {
								genRel4Str(crossref, "incollection", booktitle);
							}
							else {
								errLog.write("Found an incollection having no booktitle: #" + vid+ "\n");
							}
							genInvRel4Set(validCites, "cite");
							collectCites(invalidCites);
						}
						else if (mark.equals("book")) {
							genRel4Set(authors, "write");
							genRel4Set(editors, "edit");
							genRel4Int(publisher, "publish");
							genInvRel4Set(validCites, "cite");
							collectCites(invalidCites);
						}
						else if (mark.equals("proceedings")) {
							genRel4Set(authors, "write");
							genRel4Set(editors, "edit");
							genRel4Int(publisher, "publish");
							genInvRel4Set(validCites, "cite");
							collectCites(invalidCites);
						}
						else if (mark.equals("www")) {
							genRel4Set(authors, "www");
							genRel4Set(editors, "edit");
							genInvRel4Set(validCites, "cite");
							collectCites(invalidCites);
						}
						else if (tag.equals("phdthesis") | tag.equals("mastersthesis")) {
							genRel4Set(authors, "write");
							genRel4Int(school, "school");
						}
						mark = null;
					}
					if (tag.equals("title")) {
						
					}
					if (tag.equals("sub") | tag.equals("sup") | tag.equals("i") |
							tag.equals("tt") | tag.equals("ref")) {
						title += "</" + xsr.getLocalName() + ">";
					}
					break;
				case XMLStreamReader.CHARACTERS:
					title += xsr.getText().replace('\n', ' ').trim();//过滤掉结尾token后的'\n'
					break;
				}
			}
		}
		catch (NullPointerException ex) {
			ex.printStackTrace();
			System.err.println("Error: null pointer found in vertex #" + (vid + 1));
		}
		catch (FileNotFoundException ex) {
			ex.printStackTrace();
			System.err.println("Error: file \"" + xmlFile + "\" not found");
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
		catch (XMLStreamException ex) {
			ex.printStackTrace();
		}
		catch (DatabaseException ex) {
			ex.printStackTrace();
			System.err.println("Error: in vertex #" + vid);
		}
		finally {
			try {
				xsr.close();
				is.close();
			}
			catch (IOException ex) {
				System.err.println("error in closing io");
			}
			catch (XMLStreamException ex) {
				System.err.println("error in closing xsr");
			}
		}
	}
	
	public void procRef_Cite() {
		try {
			HashMap<Integer, String> ref_cite = gdb.getRef_Cite();
			if (ref_cite != null) {
				System.out.println("Total number of refs and cites: " + ref_cite.size());
				int counter = 0;
				Iterator<Integer> it = ref_cite.keySet().iterator();
				while (it.hasNext()) {
					int key = it.next().intValue();
					String value = ref_cite.get(key);
					int pos = value.indexOf("@@");
					String v = value.substring(0, pos);
					value = value.substring(pos + 2);
					pos = value.indexOf("@@");
					String k = value.substring(0, pos);
					String rel = value.substring(pos + 2);
					int id = gdb.checkKey(k);
					if (id != -1) {
						eid++;
						gdb.insert(false, eid, v + "-->" + id + ":" + rel);
						gdb.delRef_Cite(key);
						counter++;
					}
				}
				System.out.println("Total number of refs and cites processed: " + counter);
			}
		}
		catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
			System.err.println("Error: processing refs and cites");
		}
		catch (DatabaseException ex) {
			ex.printStackTrace();
			System.err.println("Error: processing refs and cites");
		}
	}
	
	private void genRel4Set(HashSet<Integer> set, String rel) throws UnsupportedEncodingException, DatabaseException {
		Iterator<Integer> li = set.iterator();
		while (li.hasNext()) {
			int i = li.next().intValue();
			eid++;
			String edge = i + "-->" + vid + ":" + rel;
			gdb.insert(false, eid, edge);
		}
	}
	
	private void genInvRel4Set(HashSet<Integer> set, String rel) throws UnsupportedEncodingException, DatabaseException {
		Iterator<Integer> li = set.iterator();
		while (li.hasNext()) {
			int i = li.next().intValue();
			eid++;
			String edge = vid + "-->" + i + ":" + rel;
			gdb.insert(false, eid, edge);
		}
	}
	
	private void genRel4Int(int i, String rel) throws UnsupportedEncodingException, DatabaseException {
		if (i != 0) {
			eid++;
			String edge = i + "-->" + vid + ":" + rel;
			gdb.insert(false, eid, edge);
		}
	}
	
	private void genRel4Str(String s, String rel, String btORjour) throws UnsupportedEncodingException, DatabaseException {
		if (s != null) {
			int i = gdb.checkKey(s);
			if (i != -1) {
				eid++;
				String edge = vid + "-->" + i + ":" + rel;
				gdb.insert(false, eid, edge);
			}
			else {
				rcid++;
				gdb.insertRef_Cite(rcid, vid + "@@" + s + "@@" + rel);//没找到，先存起来
			}
		}
		else {
			lid++;
			gdb.insertLost(lid, vid + "@@" + btORjour + "@@" + rel);
		}
	}
	
	private void collectCites(HashSet<String> cites) throws UnsupportedEncodingException, DatabaseException {
		Iterator<String> li = cites.iterator();
		while (li.hasNext()) {
			String cite = li.next();
			rcid++;
			gdb.insertRef_Cite(rcid, vid + "@@" + cite + "@@cite");
		}
	}
	
	private String getKey(XMLStreamReader xsr) throws IOException {
		String key = null;
		for (int i = 0, n = xsr.getAttributeCount(); i < n; i++) {
			if (xsr.getAttributeLocalName(i).equals("key")) {
				key = xsr.getAttributeValue(i);
				return key;
			}
		}
		if (key == null) {
			errLog.write("Found an item which missed the key.");
		}
		return key;
	}
	
	public void print() {
		FileWriter vOutput = null;
		FileWriter eOutput = null;
		FileWriter rOutput = null;
		FileWriter lOutput = null;
		try {
			vOutput = new FileWriter("graph/vertex.txt");
			eOutput = new FileWriter("graph/edge.txt");
			rOutput = new FileWriter("graph/ref_cite.txt");
			lOutput = new FileWriter("graph/lost.txt");
			gdb.printDB(vOutput, eOutput, rOutput, lOutput);
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
		finally {
			try {
				if (vOutput != null) {
					vOutput.close();
				}
				if (eOutput != null) {
					eOutput.close();
				}
				if (rOutput != null) {
					vOutput.close();
				}
				if (lOutput != null) {
					eOutput.close();
				}
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public void statistic() {
		System.out.println("Done!");
		System.out.println("Top-level tag numbers: " + topTagNum);
		System.out.println("New-generated entity numbers: " + genEntNum);
		System.out.println("Vertexes: " + vid);
		System.out.println("Edges: " + eid);
	}

}
