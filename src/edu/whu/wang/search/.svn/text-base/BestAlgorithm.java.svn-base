package edu.whu.wang.search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.Environment;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

import edu.whu.wang.dataStruct.GraphMatrix;
import edu.whu.wang.dataStruct.PrimitiveMatchedSubgraph;
import edu.whu.wang.dataStruct.SimpleAnswer;
import edu.whu.wang.treeeditdistance.TreeEditDistance;
import edu.whu.wang.util.ExpandableIntArray;
import edu.whu.wang.util.GroupIteratorInFullGraph;
import edu.whu.wang.util.MyMath;
import edu.whu.wang.util.SPVertex;
import edu.whu.wang.util.SearchAlgorithm;
import edu.whu.wang.util.SequentialIntArray;
import edu.whu.wang.util.UnderflowException;

public class BestAlgorithm extends SearchAlgorithm {

	Environment myEnv;
	Database myDatabase;
	Cursor myCursor;
	HashSet<String> stopwordlist;
	HashMap<Integer, String> hashMap = new HashMap<Integer, String>();

	private int groupNum;
	private GraphMatrix graph;

	private PrimitiveMatchedSubgraph[] matched_subgraphs;

	private GroupIteratorInFullGraph[] iterators;
	private boolean[] groupEmpty;

	public int[] initVertexNums;

	public BestAlgorithm(GraphMatrix graph,
			PrimitiveMatchedSubgraph[] matched_subgraphs) {
		this.graph = graph;
		groupNum = matched_subgraphs[0].groupNum();
		groupEmpty = new boolean[groupNum];
		this.matched_subgraphs = matched_subgraphs;
	}

	public void init() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					"stop_word_list.txt"));
			stopwordlist = new HashSet<String>();
			while (br.ready()) {
				stopwordlist.add(br.readLine().trim());
			}
			br.close();

			myEnv = new Environment(new File("dbEnv_graph"), null);
			myDatabase = myEnv.openDatabase(null, "vertex", null);
			myCursor = myDatabase.openCursor(null, null);

			DatabaseEntry foundKey = new DatabaseEntry();
			DatabaseEntry foundValue = new DatabaseEntry();
			while (myCursor.getNext(foundKey, foundValue, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
				TupleBinding<Integer> intergerBinding = TupleBinding
						.getPrimitiveBinding(Integer.class);
				Integer i = (Integer) intergerBinding.entryToObject(foundKey);
				String str = new String(foundValue.getData(), "UTF-8");
				hashMap.put(i, str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (myCursor != null)
				myCursor.close();
			if (myDatabase != null)
				myDatabase.close();
			if (myEnv != null)
				myEnv.close();
		}

		iterators = new GroupIteratorInFullGraph[groupNum];
		SequentialIntArray vertexes = new SequentialIntArray(graph.getN());

		for (int i = 0; i < matched_subgraphs.length; i++) {
			int[] subVertexes = matched_subgraphs[i].getSubgraph()
					.getVertexes();
			for (int j = 0; j < subVertexes.length; j++) {
				vertexes.insert(subVertexes[j]);
			}
		}

		try {
			initVertexNums = new int[groupNum];
			for (int i = 0; i < groupNum; i++) {
				SequentialIntArray initVertexes = new SequentialIntArray();
				for (int j = 0; j < matched_subgraphs.length; j++) {
					int[] subInitVertexes = matched_subgraphs[j].getGroup(i);
					for (int k = 0; k < subInitVertexes.length; k++) {
						initVertexes.insert(subInitVertexes[k]);
					}
				}
				initVertexNums[i] = initVertexes.getCapacity();
				iterators[i] = new GroupIteratorInFullGraph(graph,
						initVertexes, vertexes, i);

				// initVertexes include the ith keyword vertexes
				// vertexes include all subgraphs' vertexes
				// i means the ith keyword
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public SimpleAnswer[] process(int k) {
		int size = 0;
		SimpleAnswer[] topk = new SimpleAnswer[k];

		HashMap<Integer, double[]> allDistances = new HashMap<Integer, double[]>();
		HashMap<Integer, int[]> allPredecessors = new HashMap<Integer, int[]>();

		boolean overBound = false;

		HashSet<Integer> abandoned = new HashSet<Integer>();
		HashSet<Integer> rest = new HashSet<Integer>();

		// begin to search
		// if groupEmpty[] are all true, isEmpty return true, then while loop
		// stop ,
		while (!isEmpty()) {
			try {

				for (int i = 0; i < groupNum; i++) {
					if (!iterators[i].hasNext()) {
						groupEmpty[i] = true;
						continue;
					}
					SPVertex spv = iterators[i].next();

					int root = spv.getID();
					double distance = spv.getDistance();
					int predecessor = spv.getPredecessor();

					Integer R = new Integer(root);

					// maintaining predecessor records, which is always
					// necessary
					if (!allPredecessors.containsKey(R)) {
						int[] predecessors = new int[groupNum];
						predecessors[i] = predecessor;
						allPredecessors.put(R, predecessors);
					} else {
						allPredecessors.get(R)[i] = predecessor;
					}

					if (abandoned.contains(R)) {
						continue;
					}

					double[] distances = allDistances.get(R);
					if (distances == null) {
						// arbitrary new root cannot hold a tree smaller than
						// current topk
						if (overBound) {
							continue;
						}

						distances = new double[groupNum];
						distances[i] = distance;
						for (int j = 0; j < groupNum; j++) {
							if (j != i) {
								distances[j] = Double.MAX_VALUE;
							}
						}
						allDistances.put(R, distances);
						rest.add(R);
					} else {
						distances[i] = distance;
					}

					if (size == k && sumLBD(distances) > topk[k - 1].getScore()) {
						abandoned.add(R);
						rest.remove(R);
					}

					double sum = sumDistances(distances);
					if (sum != Double.MAX_VALUE) {
						if (size < k || sum < topk[k - 1].getScore()) {
							SimpleAnswer at = genAnswer(root, allPredecessors);
							size = addTopKAnswer(at, topk, size);
						}

						// System.out.print(R + "\t");
						// for (int j = 0; j < 4; j++) {
						// System.out.print("(" + allPredecessors.get(R)[j]
						// + "," + allDistances.get(R)[j] + ")"
						// + " ");
						// }
						// System.out.println("结果个数为：" + size);

						abandoned.add(R);
						rest.remove(R);

						// check if algorithm can be stopped
						if (size < k) {
							continue;
						}

						// make a assessment
						if (!overBound) {
							double total = 0.0d;
							for (int j = 0; j < groupNum; j++) {
								total += iterators[j].peek();
							}
							if (total >= topk[k - 1].getScore()) {
								overBound = true;
							}
						}

						// also make a assessment
						if (overBound) {
							boolean noSmallerTree = true;
							Iterator<Integer> vIt = rest.iterator();
							while (vIt.hasNext()) {
								Integer V = vIt.next();
								double[] dis = allDistances.get(V);
								double lowerBound = sumLBD(dis);
								if (lowerBound > topk[k - 1].getScore()) {
									abandoned.add(V);
									vIt.remove();
								} else {
									noSmallerTree = false;
									break;
								}
							}

							if (noSmallerTree) {
								Arrays.sort(topk, 0, size);
								return topk;
							}
						}
					}
				}
			} catch (UnderflowException ex) {
				ex.printStackTrace();
			}
		}

		Arrays.sort(topk, 0, size);

		return topk;
	}

	private boolean isEmpty() {
		for (int i = 0; i < groupNum; i++) {
			if (!groupEmpty[i]) {
				return false;
			}
		}
		return true;
	}

	private double sumDistances(double[] distances) {
		double sum = 0.0d;
		for (int j = 0; j < groupNum; j++) {
			if (distances[j] == Double.MAX_VALUE) {
				return Double.MAX_VALUE;
			} else {
				sum += distances[j];
			}
		}
		return sum;
	}

	private double sumLBD(double[] distances) throws UnderflowException {
		double sum = 0.0d;
		for (int j = 0; j < groupNum; j++) {
			if (distances[j] == Double.MAX_VALUE) {
				if (iterators[j].hasNext()) {
					sum += iterators[j].peek();
				} else {
					return Double.MAX_VALUE;
				}
			} else {
				sum += distances[j];
			}
		}
		return sum;
	}

	private SimpleAnswer genAnswer(int root,
			HashMap<Integer, int[]> allPredecessors) {
		int[] leaves = new int[groupNum];
		int[][] paths = new int[groupNum][];
		SequentialIntArray allVertexes = new SequentialIntArray();
		allVertexes.insert(root);

		for (int i = 0; i < groupNum; i++) {
			paths[i] = getPath(root, i, allVertexes, allPredecessors);
			if (paths[i] == null) {
				leaves[i] = root;
			} else {
				leaves[i] = paths[i][paths[i].length - 1];
			}
		}

		return new SimpleAnswer(root, leaves, paths, allVertexes.getArray());
	}

	private int[] getPath(int root, int i, SequentialIntArray allVertexes,
			HashMap<Integer, int[]> allPredecessors) {
		ExpandableIntArray eia = new ExpandableIntArray();
		int current = root;

		do {
			current = allPredecessors.get(new Integer(current))[i];
			if (current == 0) {
				System.err.println("Error: predecessor not found");
				System.exit(1);
			} else if (current != -1) {
				allVertexes.insert(current);
				eia.add(current);
			} else {
				break;
			}
		} while (true);

		return eia.toArray();
	}

	private int addTopKAnswer(SimpleAnswer answer, SimpleAnswer[] topk, int size) {
		if (size != 0) {
			int pos = -1;
			int insertedPosition = size;
			boolean inserted = false;
			for (int i = 0; i < size; i++) {
				int x = answer.compare(topk[i]);
				if (x == MyMath.ARR1_CONTAIN_ARR2) {
					return size;
				} else if (x == MyMath.ARR2_CONTAIN_ARR1) {
					// 对于这种情况，去掉topk[i]，把answer放在最后，然后计算
					if (!inserted) {
						for (int j = i; j > pos + 1; j--) {
							topk[j] = topk[j - 1];
						}
						// topk[pos + 1]先不动之，在最后做一个向前靠拢的循环，覆盖之
						// topk[pos + 1] = answer;
						insertedPosition = pos + 1;
						inserted = true;
					} else {
						for (int j = i; j < size - 1; j++) {
							topk[j] = topk[j + 1];
						}
						size--;
					}
				} else if (x == MyMath.ARR1_EQUAL_ARR2) {
					// 相等则不做任何处理
					if (answer.getScore() < topk[i].getScore()) {
						for (int j = i; j > pos + 1; j--) {
							topk[j] = topk[j - 1];
						}
						topk[pos + 1] = answer;
						return size;
					} else {
						return size;
					}
				}

				if (answer.getScore() >= topk[i].getScore()) {
					pos = i; // pos only changed here
				}
			} // end of for loop

			// 这里针对的就是ARR2_CONTAIN_ARR1的情况，
			if (inserted) {
				for (int i = insertedPosition; i < size - 1; i++)
					topk[i] = topk[i + 1];
				answer.setIntegratedScore(computeIntegatedScore(answer, topk));
				topk[size - 1] = answer;
				return size;
			}

			// 备注：这里整个的调整现在不需要了，只需加到最后，然后做惩罚
			// if (pos < size - 1) {
			// if (size < topk.length) {
			// for (int i = size; i > pos + 1; i--) {
			// topk[i] = topk[i - 1];
			// }
			// size++;
			// } else {
			// for (int i = topk.length - 1; i > pos + 1; i--) {
			// topk[i] = topk[i - 1];
			// }
			// }
			// topk[pos + 1] = answer;
			// } else if (size < topk.length) {
			// topk[size] = answer;
			// size++;
			// }
			if (size < topk.length) {
				answer.setIntegratedScore(computeIntegatedScore(answer, topk));
				topk[size++] = answer;
			}
			return size;
		} else {
			answer.setIntegratedScore(Double.MAX_VALUE);
			topk[size++] = answer;
			return size;
		}

	}

	// 综合值
	private double computeIntegatedScore(SimpleAnswer goal,
			SimpleAnswer[] answers) {
		double e = 100;
		// System.out.println( 1 / (goal.getScore() + e *
		// redundancyPenalty(goal, answers)));
		return 1 / (goal.getScore() + e * redundancyPenalty(goal, answers));
	}

	// rpt值
	private double redundancyPenalty(SimpleAnswer goal, SimpleAnswer[] answers) {
		int keywordsLen = goal.getPaths().length;

		double[] result = new double[answers.length];
		int count = 0;
		for (int i = 0; i < answers.length; i++)
			if (answers[i] != null)
				result[count++] = sim3(keywordsLen, goal, answers[i]);

		double max = Double.MIN_VALUE;
		for (int i = 0; i < result.length; i++)
			if (result[i] > max)
				max = result[i];
		return max;
	}

	// sim函数
	private double sim(int n, SimpleAnswer goal, SimpleAnswer current) {
		double distance = 0.0;
		for (int i = 0; i < n - 1; i++)
			for (int j = i + 1; j < n; j++) {
				TreeEditDistance ted = new TreeEditDistance(goal
						.getPartialAnswer(i, j).treeToString(), current
						.getPartialAnswer(i, j).treeToString());
				distance += ted.getDistance();
			}
		return 1.0 / (1.0 + distance
				/ (goal.getAllVertexes().length > current.getAllVertexes().length ? goal
						.getAllVertexes().length
						: current.getAllVertexes().length));

	}

	private double sim2(int n, SimpleAnswer goal, SimpleAnswer current) {
		HashSet<String> set1 = getRemainKeywordsByAnswer(goal);
		HashSet<String> set2 = getRemainKeywordsByAnswer(current);

		int count = 0;
		Iterator<String> it1 = set1.iterator();
		while (it1.hasNext()) {
			if (set2.contains(it1.next()))
				count++;
		}

		return (double) count
				/ (set1.size() > set2.size() ? set1.size() : set2.size());
	}

	private double sim3(int n, SimpleAnswer goal, SimpleAnswer current) {
		String[] result1 = convertAnswerToText(goal);
		String[] result2 = convertAnswerToText(current);

		double result = 0.0;
		for (int i = 0; i < result1.length; i++){
			result += getCosValue(result1[i].split(" "), result2[i].split(" "));
		}
		System.out.println(result/result1.length);
		return result/result1.length;
		// String[] allWords = new String[result1.length + result2.length];
		// for (int i = 0; i < result1.length; i++){
		// allWords[i] = result1[i];
		// }
		// for (int i = result1.length; i < allWords.length; i++){
		// allWords[i] = result2[i - result1.length];
		// }
		
	}

	private double getCosValue(String[] result1, String[] result2){
		HashSet<String> set = new HashSet<String>();
		for (int i = 0; i < result1.length; i++)
			set.add(result1[i]);
		for (int i = 0; i < result2.length; i++)
			set.add(result2[i]);

		HashMap<String, Integer> hm1 = new HashMap<String, Integer>();
		HashMap<String, Integer> hm2 = new HashMap<String, Integer>();

		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			String tmp = it.next();
			hm1.put(tmp, 0);
			hm2.put(tmp, 0);
		}

		for (int i = 0; i < result1.length; i++) {
			hm1.put(result1[i], hm1.get(result1[i]) + 1);
		}

		for (int i = 0; i < result2.length; i++) {
			hm2.put(result2[i], hm2.get(result2[i]) + 1);
		}

		int[] vector1 = new int[set.size()];
		int[] vector2 = new int[set.size()];
		Iterator<String> it1 = hm1.keySet().iterator();
		int position = 0;
		while(it1.hasNext()){
			vector1[position++] = hm1.get(it1.next());
		}
		Iterator<String> it2 = hm2.keySet().iterator();
		position = 0;
		while(it2.hasNext()){
			vector2[position++] = hm2.get(it2.next());
		}

		return 1- cos(vector1, vector2);
	}
	private double cos(int[] a, int[] b){
		int up = 0;
		for (int i = 0; i < a.length; i++){
			up += a[i]*b[i];
		}
		
		int down1 = 0;
		for (int i = 0; i < a.length; i++){
			down1 += a[i]*a[i];
		}
		int down2  = 0;
		for (int i = 0; i < b.length; i++){
			down2 += b[i]*b[i];
		}
//		System.out.println(down1);
//		System.out.println(down2);
//		System.out.println(Math.sqrt((double)up));
//		System.out.println(Math.sqrt((double)down1));
//		System.out.println(Math.sqrt((double)down2));
		return Math.sqrt((double)up)/(Math.sqrt((double)down1)*Math.sqrt((double)down2));
	}
	
	private HashSet<String> getRemainKeywordsByAnswer(SimpleAnswer answer) {
		int[] allVertex = answer.getAllVertexes();

		HashSet<String> keywordsSet = SimpleQueryProcessingManager
				.getKeywordSet();
		HashSet<String> remainKeywordsSet = new HashSet<String>();

		for (int i = 0; i < allVertex.length; i++) {
			String str = hashMap.get(allVertex[i]);
			String[] arr = str.split(" ");
			for (int j = 0; j < arr.length; j++) {
				String tmp = arr[j].toLowerCase();
				if (!stopwordlist.contains(tmp) && !keywordsSet.contains(tmp))
					remainKeywordsSet.add(tmp);
			}
		}

		return remainKeywordsSet;
	}

	private String[] convertAnswerToText(SimpleAnswer answer) {
		int len = answer.getMatched_vertexes().length;
		String[] result = new String[len];
		for (int i = 0; i < len; i++) {
			result[i] = "";
		}
		int[][] paths = answer.getPaths();

		for (int i = 0; i < paths.length; i++) {
			result[i] += hashMap.get(answer.getRoot()) + " ";
			if (paths[i] != null) {
				for (int j = 0; j < paths[i].length; j++) {
					result[i] += hashMap.get(paths[i][j]) + " ";
				}
			}
		}
		return result;
	}
}
