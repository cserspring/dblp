package edu.whu.wang.index;

import java.io.*;
import java.util.*;

import com.sleepycat.je.*;

public class InvertedList {

	private String[] parse(String value) {
		String[] words = value.split(" ");
		for (int i = 0; i < words.length; i++) {
			String word = words[i].trim();
			while (word.startsWith("(") || word.startsWith("\"")
					|| word.startsWith("'")) {
				word = word.substring(1);
			}
			while (word.endsWith(",") || word.endsWith(".")
					|| word.endsWith(")") || word.endsWith("?")
					|| word.endsWith(":") || word.endsWith("!")
					|| word.endsWith("\"") || word.endsWith("'")) {
				word = word.substring(0, word.length() - 1);
			}
			words[i] = word;
		}
		return words;
	}

	private String[] naiveParse(String value) {
		return value.split(" ");
	}

	private String[] getKeywords(String value) {
		String[] pairs = value.split("@@");
		String[] keywords = null;
		for (int i = 0; i < pairs.length; i++) {
			String pair = pairs[i].trim();
			if (pair.length() > 0) {
				int pos = pair.indexOf(":");
				if (pos == -1) {
					System.out.println("Found wrong attr-value pair: " + pair);
					continue;
				}
				String attrName = pair.substring(0, pos).trim();
				String attrValue = pair.substring(pos + 1).trim();

				if (attrName.equals("title")) {
					keywords = this.parse(attrValue);
				} else if (attrName.equals("name")) {
					keywords = this.naiveParse(attrValue);
				} else {
					continue;
				}
			}
		}

		return keywords;
	}

	public void parseVertex(String path) {
		try {
			EnvironmentConfig envConfig = new EnvironmentConfig();
			envConfig.setAllowCreate(true);

			Environment env = new Environment(new File("dbEnv_graph"),
					envConfig);
			DatabaseConfig dbConfig = new DatabaseConfig();
			dbConfig.setAllowCreate(true);
			Database keyword_vertex = env.openDatabase(null, "keyword_vertex", dbConfig);

			BufferedReader br = new BufferedReader(new FileReader(
					"stop_word_list.txt"));
			HashSet<String> stopwordlist = new HashSet<String>();
			while (br.ready()) {
				stopwordlist.add(br.readLine().trim());
			}
			br.close();

			br = new BufferedReader(new FileReader(new File(path)));

			HashMap<String, ArrayList<Integer>> keywordVertexHashMap = new HashMap<String, ArrayList<Integer>>();
			String line = "";
			while (br.ready()) {
				line = br.readLine().trim();
				if (line.length() > 0) {
					int pos = line.indexOf(" ");
					if (pos == -1)
						return;
					String key = line.substring(0, pos);
					String value = line.substring(pos).trim();

					// get corresponding subgraphs from vertex id (key)
					int vertexID = Integer.parseInt(key);
					String[] keywords = getKeywords(value);

					for (String keyword : keywords) {
						keyword = keyword.trim().toLowerCase();

						if (keyword.length() > 0) {
							if (stopwordlist.contains(keyword)) {
								continue;
							}
							if(keywordVertexHashMap.containsKey(keyword)){
								ArrayList<Integer> tmpList = keywordVertexHashMap.get(keyword);
								tmpList.add(new Integer(vertexID));
								keywordVertexHashMap.put(keyword, tmpList);
							}
							else{
								ArrayList<Integer> tmpList = new ArrayList<Integer>();
								tmpList.add(new Integer(vertexID));
								keywordVertexHashMap.put(keyword, tmpList);
							}
						}
					}// end of for
				}// end of if
			}// end of while
			br.close();

			Iterator<String> it = keywordVertexHashMap.keySet().iterator();
			while (it.hasNext()) {
				String keyword = it.next();
				ArrayList<Integer> tmpList = keywordVertexHashMap.get(keyword);
				
				DatabaseEntry deKey = new DatabaseEntry(keyword.getBytes("UTF-8"));
				DatabaseEntry deValue = new DatabaseEntry();
				ArrayListTupleBinding altb = new ArrayListTupleBinding();
				altb.objectToEntry(tmpList, deValue);
				keyword_vertex.put(null, deKey, deValue);
			}
			keyword_vertex.close();
			env.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
