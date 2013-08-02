package edu.whu.wang.setup_new;

import java.io.*;

import com.sleepycat.bind.*;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.je.*;
import java.util.*;

public class Experiment {
	public void parseVertex(String path) {
		try {
			EnvironmentConfig envConfig = new EnvironmentConfig();
			envConfig.setAllowCreate(true);

			Environment env = new Environment(new File("dbEnv_graph"),
					envConfig);
			DatabaseConfig dbConfig = new DatabaseConfig();
			dbConfig.setAllowCreate(true);
			Database vertex = env.openDatabase(null, "vertex", dbConfig);

			BufferedReader br = new BufferedReader(new FileReader(
					new File(path)));

			HashMap<Integer, String> vertexHashMap = new HashMap<Integer, String>();
			String line = "";
			while (br.ready()) {
				line = br.readLine().trim();
				if (line.length() > 0) {
					String[] arr = line.split("type:person@@name:");
					Integer i = Integer.parseInt(arr[0].trim());
					vertexHashMap.put(i, arr[1].trim());
				}// end of if
			}// end of while
			
			Iterator<Integer> it = vertexHashMap.keySet().iterator();
			while (it.hasNext()) {
				Integer keyword = it.next();
				String value = vertexHashMap.get(keyword);
				
				DatabaseEntry deValue = new DatabaseEntry(value.getBytes("UTF-8"));
				DatabaseEntry deKey = new DatabaseEntry();
				EntryBinding<Integer> integerBinding = TupleBinding.getPrimitiveBinding(Integer.class);
				integerBinding.objectToEntry(keyword, deKey);
				vertex.put(null, deKey, deValue);
			}
			br.close();
			vertex.close();
			env.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
