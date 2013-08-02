package edu.whu.wang.dataStruct;

import java.util.*;

import edu.whu.wang.util.MyMath;

public class SimpleAnswer implements Comparable<SimpleAnswer>{

	@Override
	public int compareTo(SimpleAnswer arg0) {
		// TODO Auto-generated method stub
		if(this.integratedScore < arg0.integratedScore)
			return 1;
		else
			return 0;
	}

	private int root;
	private int[] matched_vertexes;
	private int[][] paths;
	private int[] all_vertexes;
	private double score;
	private double integratedScore;

	public SimpleAnswer(int root, int[] matched_vertexes, int[][] paths,
			int[] allVertexes) {
		this.root = root;
		this.matched_vertexes = matched_vertexes;
		this.paths = paths;
		this.all_vertexes = allVertexes;
		score = score();
	}

	public int compare(SimpleAnswer at) {
		// return MyMath.compareIntArrays2(all_vertexes, at.getAllVertexes());
		return MyMath.compareDoubleArr(this.root, at.getRoot(), this.paths, at
				.getPaths());
	}

	// modify 05-06
	// public int MyMath
	private double score() {
		double score = 0.0d;
		for (int i = 0; i < matched_vertexes.length; i++) {
			if (paths[i] != null)
				score += paths[i].length;
		}
		return score;
	}

	// add on 20110524
	public String treeToString() {
		ArrayList<String> set = new ArrayList<String>();

		for (int i = 0; i < paths.length; i++) {
			if (paths[i] == null)
				continue;
			// System.out.println("该列第二个节点：" + paths[i].length);
			if (!(set.contains(root + "-" + paths[i][0])))
				set.add(root + "-" + paths[i][0]);

			for (int j = 0; j < paths[i].length - 1; j++) {
				if (!(set.contains(paths[i][j] + "-" + paths[i][j + 1])))
					set.add(paths[i][j] + "-" + paths[i][j + 1]);
			}
		}

		StringBuilder result = new StringBuilder("");
		if (set != null) {
			Iterator<String> it = set.iterator();
			while (it.hasNext())
				result.append(it.next() + ";");
		} else {
			result.append(root);
		}
		return result.toString();
	}

	public double getScore() {
		return score;
	}

	public int getRoot() {
		return root;
	}

	public int[] getAllVertexes() {
		return all_vertexes;
	}

	public int getLeafNumber() {
		return paths.length;
	}

	public int[] getPath(int index) {
		return paths[index];
	}

	public int[] getLeaves() {
		return matched_vertexes;
	}

	public int[][] getTreeArray() {
		int maxL = 0;
		for (int j = 0; j < getLeafNumber(); j++) {
			int[] path = getPath(j);
			if (path != null) {
				if (maxL < path.length) {
					maxL = path.length;
				}
			}
		}

		int[][] v = new int[paths.length][maxL + 1];// include root
		for (int i = 0; i < paths.length; i++) {
			v[i][0] = root;
		}

		if (maxL != 0) {
			for (int i = 0; i < getLeafNumber(); i++) {
				int[] path = getPath(i);
				if (path != null) {
					for (int j = 0; j < path.length; j++) {
						v[i][j + 1] = path[j];
					}
				}
			}
		}

		return v;
	}

	public int[][] change(int[][] v) {
		int maxL = 0;

		for (int i = 0; i < v.length; i++) {
			if (maxL < v[i].length) {
				maxL = v[i].length;
			}
		}

		int[][] vv = new int[v.length][maxL];
		int vv_rows = 0;

		int vv_k = 0;

		for (int i = 0; i < maxL; i++) {// v_column
			if (vv_k > vv_rows) {
				vv_rows = vv_k;
			}
			vv_k = 0;

			for (int j = 0; j < v.length; j++) {// v_row
				int v_new = v[j][i];
				int v_new_prev;

				if (v_new == 0)
					continue;

				int m = 0;
				for (; m < vv_k; m++) {
					if (vv[m][maxL - i - 1] == v_new) {
						break;
					}
				}
				if (m >= vv_k) {
					if (i == 0) {
						vv[vv_k][maxL - i - 1] = v_new;
					} else {// v_column is the next column
						v_new_prev = v[j][i - 1];
						for (int h = 0; h < v.length; h++) {
							if (vv[h][maxL - i] == v_new_prev) {
								if (vv[h][maxL - i - 1] == 0) {
									vv[h][maxL - i - 1] = v_new;
								} else {// 需要挪动
									for (int d = vv_rows - 1; d > h; d--) {
										for (int s = maxL - i - 1; s <= maxL - 1; s++) {
											vv[d + 1][s] = vv[d][s];
											vv[d][s] = 0;
										}
									}
									vv[h + 1][maxL - i - 1] = v_new;
									vv_rows += 1;
								}
								break;
							}// end if
						}// end for
					}
					vv_k++;
				} else {
					continue;
				}
			}
		}// end the first for loop

		int[][] res = new int[2 * (v.length) - 1][2 * maxL - 1];

		int xx = -2;// 以后该处画横线
		int xy = -3;// 以后该处画"┚"
		int yy = -4;// 以后该处画竖线

		// copy the data to new array
		for (int i = 0; i < vv.length; i++) {// row
			boolean flag = false;
			for (int j = 0; j < maxL; j++) {// column
				int newV = vv[i][j];
				if (newV == 0) {
					if (j == 0) {// 第一列为0，其余为0则将该处为
						flag = true;
					}
				} else {// 该数不为0
					if (flag) {// 将该数填到尾部
						res[2 * i][0] = newV;
						// 从1到2*j都置为横线
						for (int k = 1; k <= 2 * j; k++) {
							res[2 * i][k] = xx;
						}
						flag = false;
					} else {
						res[2 * i][2 * j] = newV;
						// 将左边的置为横线
						if ((2 * j - 1) >= 0) {
							res[2 * i][2 * j - 1] = xx;
						}
					}

					// 判断后继数是否为0
					if ((j + 1) < maxL) {// 确保不是最后一个数
						if (vv[i][j + 1] == 0) {// 本数为newV是该行的头节点
							res[2 * i][2 * j + 1] = xy;// 画折线"┚"
							// 将该列从上一个非0行开始画竖线
							int m = 2 * i - 1;
							while (m >= 0 && (res[m][2 * j + 1] == 0)) {
								res[m][2 * j + 1] = yy;
								m--;
							}
							break;
						}
					}
				}
			}
		}

		return res;

		// print
		// for(int x=0;x<v.length;x++){
		// for(int y=0;y<maxL;y++){
		// System.out.print(vv[x][y]+" ");
		// }
		// System.out.println("");
		// }

	}

	public HashMap<Integer, Integer> getNextForEdge(int[][] v) {
		HashMap<Integer, Integer> edge = new HashMap<Integer, Integer>();
		for (int i = 0; i < v.length; i++) {
			for (int j = 0; j < v[i].length; j++) {
				if ((j + 1) < v[i].length && v[i][j + 1] != 0) {
					edge.put(v[i][j + 1], v[i][j]);
				}
			}
		}
		return edge;
	}

	// 得到partial answer
	public SimpleAnswer getPartialAnswer(int i, int j) {
		int[] iPath = paths[i];
		int[] jPath = paths[j];

		int newRoot = this.root;
		boolean flag = false;
		int m = -1;
		int n = -1;
		if (iPath != null && jPath != null) {
			for (m = iPath.length - 1; m >= 0; m--) {
				for (n = jPath.length - 1; n >= 0; n--) {
					if (iPath[m] == jPath[n]) {
						newRoot = iPath[m];
						flag = true;
						break;
					}
				}
				if (flag)
					break;
			}
		}

		int[] newMatchVertex = new int[2];
		newMatchVertex[0] = iPath == null ? this.root : iPath[iPath.length - 1];
		newMatchVertex[1] = jPath == null ? this.root : jPath[jPath.length - 1];

		int[][] newPaths = new int[2][];
		if (newRoot == this.root) {
			newPaths[0] = iPath;
			newPaths[1] = jPath;
		} else {
			if (iPath.length - m - 1 == 0)
				newPaths[0] = null;
			else {
				newPaths[0] = new int[iPath.length - m - 1];
				for (int p = 0; p < newPaths[0].length; p++)
					newPaths[0][p] = iPath[++m];
			}

			if (jPath.length - n - 1 == 0)
				newPaths[1] = null;
			else {
				newPaths[1] = new int[jPath.length - n - 1];
				for (int q = 0; q < newPaths[1].length; q++)
					newPaths[1][q] = jPath[++n];
			}
		}

		int iLen = newPaths[0] == null ? 0 : newPaths[0].length;
		int jLen = newPaths[1] == null ? 0 : newPaths[1].length;
		int[] newAllVertex = new int[iLen + jLen + 1];
		int t = 0;
		newAllVertex[t++] = newRoot;
		for (int p = 0; p < iLen; p++)
			newAllVertex[t++] = newPaths[0][p];
		for (int p = 0; p < jLen; p++)
			newAllVertex[t++] = newPaths[1][p];

		return new SimpleAnswer(newRoot, newMatchVertex, newPaths, newAllVertex);
	}

	public int[][] getPaths() {
		return paths;
	}

	public void setPaths(int[][] paths) {
		this.paths = paths;
	}

	public double getIntegratedScore() {
		return integratedScore;
	}

	public void setIntegratedScore(double integratedScore) {
		this.integratedScore = integratedScore;
	}

	public int[] getMatched_vertexes() {
		return matched_vertexes;
	}

	public void setMatched_vertexes(int[] matched_vertexes) {
		this.matched_vertexes = matched_vertexes;
	}
}
