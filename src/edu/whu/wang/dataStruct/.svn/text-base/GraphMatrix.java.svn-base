package edu.whu.wang.dataStruct;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import edu.whu.wang.util.ExpandableIntArray;
import edu.whu.wang.util.SequentialIntArray;

/**
 * @author clock
 * 
 */
public class GraphMatrix {

	protected final int n; // number of vertexes
	protected int m; // number of edges
	// protected HashMap<Integer, GraphVertex> vertexes;

	protected HashMap<Integer, GraphEdge> rowHeads;
	protected HashMap<Integer, GraphEdge> columnHeads;

	public GraphMatrix(int n) {
		this.n = n;
		this.rowHeads = new HashMap<Integer, GraphEdge>();
		this.columnHeads = new HashMap<Integer, GraphEdge>();
	}

	// protected void loadVertexes() {
	// vertexes = new HashMap<Integer, GraphVertex>();
	// for (int i = 0; i < n; i++) {
	// vertexes.put(new Integer(i + 1), new GraphVertex(i + 1));
	// }
	// }

	public void loadEdges(String dataFile) {
		this.fillDiagonal();
		try {
			BufferedReader br = new BufferedReader(new FileReader(dataFile));
			while (br.ready()) {
				String line = br.readLine().trim();
				String edge = line.substring(line.lastIndexOf(" ") + 1);
				int pos1 = edge.indexOf("-->");
				int pos2 = edge.indexOf(":");
				if (pos1 == -1 || pos2 == -1) {
					System.err.println("Found a wrong edge: " + edge);
					continue;
				}
				int row = new Integer(edge.substring(0, pos1)).intValue();
				int column = new Integer(edge.substring(pos1 + 3, pos2))
						.intValue();
				if (row != column) {
					this.insert(row, column);
					this.insert(column, row);
					m++;
				}
			}
			System.out.println("Loading finished");
			br.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public DDRadiusSubGraph extractSubgraph(int source, int radius) {
		SequentialIntArray vertexes = new SequentialIntArray();
		vertexes.insert(source);
		ExpandableIntArray neighbors = new ExpandableIntArray();
		neighbors.add(source);

		// breadth-first
		for (int i = 0; i < radius; i++) {
			SequentialIntArray newVertexes = new SequentialIntArray();
			ExpandableIntArray newNeighbors = new ExpandableIntArray();
			for (int j = 0; j < neighbors.getSize(); j++) {
				int[] temp = genNeighbors(source);
				for (int k = 0; k < temp.length; k++) {
					if (vertexes.binarySearch(temp[k]) == -1) {
						newVertexes.insert(temp[k]);
						newNeighbors.add(temp[k]);
					}
				}
			}
			neighbors = newNeighbors;
			for (int j = 0; j < newVertexes.getCapacity(); j++) {
				vertexes.insert(newVertexes.get(j));
			}
		}
		return new DDRadiusSubGraph(source, vertexes.getArray());
	}

	public DDRadiusSubGraph extractSubgraph(int source, int radius,
			double upperBound, double lowerBound) {
		SequentialIntArray vertexes = new SequentialIntArray();
		vertexes.insert(source);
		ExpandableIntArray neighbors = new ExpandableIntArray();
		neighbors.add(source);

		// breadth-first
		for (int i = 0; i < radius; i++) {
			SequentialIntArray newVertexes = new SequentialIntArray();
			ExpandableIntArray newNeighbors = new ExpandableIntArray();
			for (int j = 0; j < neighbors.getSize(); j++) {
				int[] temp = genNeighbors(neighbors.get(j));
				for (int k = 0; k < temp.length; k++) {
					if (vertexes.binarySearch(temp[k]) == -1) {

						// if the current subgraph size is going to surpass the
						// upper bound
						if (vertexes.getCapacity() + newVertexes.getCapacity() == upperBound) {
							return new DDRadiusSubGraph(source, vertexes
									.getArray());
						}
						newVertexes.insert(temp[k]);
						newNeighbors.add(temp[k]);
					}
				}
			}
			neighbors = newNeighbors;
			for (int j = 0; j < newVertexes.getCapacity(); j++) {
				vertexes.insert(newVertexes.get(j));
			}
		}

		// if the final subgraph size is smaller than the lower bound
		if (vertexes.getCapacity() < lowerBound) {

		}

		return new DDRadiusSubGraph(source, vertexes.getArray());
	}
	
	public DDRadiusSubGraph extractSubgraph(int source, int radius,
			double upperBound, double lowerBound, double neighborBound) {
		if (source < 1) {
			return null;
		}
		
		
		SequentialIntArray vertexes = new SequentialIntArray();
		vertexes.insert(source);
		ExpandableIntArray neighbors = new ExpandableIntArray();
		neighbors.add(source);

		// breadth-first
		for (int i = 0; i < radius; i++) {
			SequentialIntArray newVertexes = new SequentialIntArray();
			ExpandableIntArray newNeighbors = new ExpandableIntArray();
			for (int j = 0; j < neighbors.getSize(); j++) {
				int[] temp = genNeighbors(neighbors.get(j));
				for (int k = 0; k < temp.length; k++) {
					if (vertexes.binarySearch(temp[k]) == -1) {

						// if the current subgraph size is going to surpass the
						// upper bound
						if (vertexes.getCapacity() + newVertexes.getCapacity() == upperBound) {
							return new DDRadiusSubGraph(source, vertexes
									.getArray());
						}
						newVertexes.insert(temp[k]);
						newNeighbors.add(temp[k]);
					}
				}
			}
			neighbors = newNeighbors;
			for (int j = 0; j < newVertexes.getCapacity(); j++) {
				vertexes.insert(newVertexes.get(j));
			}
		}

		// if the final subgraph size is smaller than the lower bound
		if (vertexes.getCapacity() < lowerBound) {

		}

		return new DDRadiusSubGraph(source, vertexes.getArray());
	}

	public int[] genNeighbors(int source) {
		GraphEdge re = rowHeads.get(new Integer(source));
		ExpandableIntArray neighbors = new ExpandableIntArray();

		while (re != null) {
			int gv = re.getDestination();
			neighbors.add(gv);
			re = re.getNextEdgeFromSource();
		}

		return neighbors.toArray();
	}
	
	public int[] getEnds(int source) {
		GraphEdge ge = rowHeads.get(new Integer(source));
		ExpandableIntArray neighbors = new ExpandableIntArray();

		while (ge != null) {
			int gv = ge.getDestination();
			// only return the edges in which end is larger than source
			if (gv > source) {
				neighbors.add(gv);
			}
			ge = ge.getNextEdgeFromSource();
		}

		return neighbors.toArray();
	}

	public void insert(int source, int destination) {
//		if (source > n | destination > n | source < 1 | destination < 1) {
//			System.err.println("Error: illegal edge (" + source + ","
//					+ destination + ")");
//			return;
//		}
		Integer s = new Integer(source);
		Integer d = new Integer(destination);
		GraphEdge newEdge = new GraphEdge(source, destination);
		GraphEdge rowHead = this.rowHeads.get(s);
		GraphEdge columnHead = this.columnHeads.get(d);
		if (rowHead == null) {
			this.rowHeads.put(s, newEdge);
		} else {
			if (rowHead.getDestination() > destination) {
				newEdge.setNextEdgeFromSource(rowHead);
				this.rowHeads.put(s, newEdge);
			} else if (rowHead.getDestination() < destination) {
				GraphEdge re = null;
				for (re = rowHead; re.getNextEdgeFromSource() != null
						&& re.getNextEdgeFromSource().getDestination() < destination; re = re
						.getNextEdgeFromSource())
					;
				if (re.getNextEdgeFromSource() == null) {
					re.setNextEdgeFromSource(newEdge);
				} else {
					if (re.getNextEdgeFromSource().getDestination() > destination) {
						newEdge.setNextEdgeFromSource(re
								.getNextEdgeFromSource());
						re.setNextEdgeFromSource(newEdge);
					}
					else {
						if (source > destination)
						m--;
					}
				}
			}
		}
		if (columnHead == null) {
			this.columnHeads.put(d, newEdge);
		} else {
			if (columnHead.getSource() > source) {
				newEdge.setNextEdgeToDestination(columnHead);
				this.columnHeads.put(d, newEdge);
			} else if (columnHead.getSource() < source) {
				GraphEdge re = null;
				for (re = columnHead; re.getNextEdgeToDestination() != null
						&& re.getNextEdgeToDestination().getSource() < source; re = re
						.getNextEdgeToDestination())
					;
				if (re.getNextEdgeToDestination() == null) {
					re.setNextEdgeToDestination(newEdge);
				} else {
					if (re.getNextEdgeToDestination().getSource() > source) {
						newEdge.setNextEdgeToDestination(re
								.getNextEdgeToDestination());
						re.setNextEdgeToDestination(newEdge);
					}
				}
			}
		}
	}

	public void fillDiagonal() {
		for (int i = 1; i < n + 1; i++) {
			this.insert(i, i);
		}
	}

	public HashMap<Integer, GraphEdge> getRowHeads() {
		return rowHeads;
	}

	public HashMap<Integer, GraphEdge> getColumnHeads() {
		return columnHeads;
	}

	public int getN() {
		return n;
	}

	public int getM() {
		return m;
	}

}



//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.HashMap;
//
//import edu.whu.rose.clock.ssql.graph.RRadiusSubGraph;
//import edu.whu.rose.clock.ssql.util.ExpandableIntArray;
//import edu.whu.rose.clock.ssql.util.SequentialIntArray;
//
///**
// * @author clock
// * 
// */
//public class GraphMatrix {
//
//	protected final int n; // number of vertexes
//	protected int m; // number of edges
//	// protected HashMap<Integer, GraphVertex> vertexes;
//
//	protected HashMap<Integer, GraphEdge> rowHeads;
//	protected HashMap<Integer, GraphEdge> columnHeads;
//
//	public GraphMatrix(int n) {
//		this.n = n;
//		this.rowHeads = new HashMap<Integer, GraphEdge>();
//		this.columnHeads = new HashMap<Integer, GraphEdge>();
//	}
//
//	// protected void loadVertexes() {
//	// vertexes = new HashMap<Integer, GraphVertex>();
//	// for (int i = 0; i < n; i++) {
//	// vertexes.put(new Integer(i + 1), new GraphVertex(i + 1));
//	// }
//	// }
//
//	public void loadEdges(String dataFile) {
//		this.fillDiagonal();
//		try {
//			BufferedReader br = new BufferedReader(new FileReader(dataFile));
//			while (br.ready()) {
//				String line = br.readLine().trim();
//				String edge = line.substring(line.lastIndexOf(" ") + 1);
//				int pos1 = edge.indexOf("-->");
//				int pos2 = edge.indexOf(":");
//				if (pos1 == -1 || pos2 == -1) {
//					System.err.println("Found a wrong edge: " + edge);
//					continue;
//				}
//				int row = new Integer(edge.substring(0, pos1)).intValue();
//				int column = new Integer(edge.substring(pos1 + 3, pos2))
//						.intValue();
//				if (row != column) {
//					this.insert(row, column);
//					this.insert(column, row);
//					m++;
//				}
//			}
//			System.out.println("Loading finished");
//			br.close();
//		} catch (IOException ex) {
//			ex.printStackTrace();
//		}
//	}
//
//	public RRadiusSubGraph extractSubgraph(int source, int radius) {
//		SequentialIntArray vertexes = new SequentialIntArray();
//		vertexes.insert(source);
//		ExpandableIntArray neighbors = new ExpandableIntArray();
//		neighbors.add(source);
//
//		// breadth-first
//		for (int i = 0; i < radius; i++) {
//			SequentialIntArray newVertexes = new SequentialIntArray();
//			ExpandableIntArray newNeighbors = new ExpandableIntArray();
//			for (int j = 0; j < neighbors.getSize(); j++) {
//				int[] temp = genNeighbors(source);
//				for (int k = 0; k < temp.length; k++) {
//					if (vertexes.binarySearch(temp[k]) == -1) {
//						newVertexes.insert(temp[k]);
//						newNeighbors.add(temp[k]);
//					}
//				}
//			}
//			neighbors = newNeighbors;
//			for (int j = 0; j < newVertexes.getCapacity(); j++) {
//				vertexes.insert(newVertexes.get(j));
//			}
//		}
//		return new RRadiusSubGraph(source, vertexes.getArray());
//	}
//
//	public RRadiusSubGraph extractSubgraph(int source, int radius,
//			double upperBound, double lowerBound) {
//		SequentialIntArray vertexes = new SequentialIntArray();
//		vertexes.insert(source);
//		ExpandableIntArray neighbors = new ExpandableIntArray();
//		neighbors.add(source);
//
//		// breadth-first
//		for (int i = 0; i < radius; i++) {
//			SequentialIntArray newVertexes = new SequentialIntArray();
//			ExpandableIntArray newNeighbors = new ExpandableIntArray();
//			for (int j = 0; j < neighbors.getSize(); j++) {
//				int[] temp = genNeighbors(neighbors.get(j));
//				for (int k = 0; k < temp.length; k++) {
//					if (vertexes.binarySearch(temp[k]) == -1) {
//
//						// if the current subgraph size is going to surpass the
//						// upper bound
//						if (vertexes.getCapacity() + newVertexes.getCapacity() == upperBound) {
//							return new RRadiusSubGraph(source, vertexes
//									.getArray());
//						}
//						newVertexes.insert(temp[k]);
//						newNeighbors.add(temp[k]);
//					}
//				}
//			}
//			neighbors = newNeighbors;
//			for (int j = 0; j < newVertexes.getCapacity(); j++) {
//				vertexes.insert(newVertexes.get(j));
//			}
//		}
//
//		// if the final subgraph size is smaller than the lower bound
//		if (vertexes.getCapacity() < lowerBound) {
//
//		}
//
//		return new RRadiusSubGraph(source, vertexes.getArray());
//	}
//	
//	public RRadiusSubGraph extractSubgraph(int source, int radius,
//			double upperBound, double lowerBound, double neighborBound) {
//		if (source < 1) {
//			return null;
//		}
//		
//		
//		SequentialIntArray vertexes = new SequentialIntArray();
//		vertexes.insert(source);
//		ExpandableIntArray neighbors = new ExpandableIntArray();
//		neighbors.add(source);
//
//		// breadth-first
//		for (int i = 0; i < radius; i++) {
//			SequentialIntArray newVertexes = new SequentialIntArray();
//			ExpandableIntArray newNeighbors = new ExpandableIntArray();
//			for (int j = 0; j < neighbors.getSize(); j++) {
//				int[] temp = genNeighbors(neighbors.get(j));
//				for (int k = 0; k < temp.length; k++) {
//					if (vertexes.binarySearch(temp[k]) == -1) {
//
//						// if the current subgraph size is going to surpass the
//						// upper bound
//						if (vertexes.getCapacity() + newVertexes.getCapacity() == upperBound) {
//							return new RRadiusSubGraph(source, vertexes
//									.getArray());
//						}
//						newVertexes.insert(temp[k]);
//						newNeighbors.add(temp[k]);
//					}
//				}
//			}
//			neighbors = newNeighbors;
//			for (int j = 0; j < newVertexes.getCapacity(); j++) {
//				vertexes.insert(newVertexes.get(j));
//			}
//		}
//
//		// if the final subgraph size is smaller than the lower bound
//		if (vertexes.getCapacity() < lowerBound) {
//
//		}
//
//		return new RRadiusSubGraph(source, vertexes.getArray());
//	}
//
//	public int[] genNeighbors(int source) {
//		GraphEdge re = rowHeads.get(new Integer(source));
//		ExpandableIntArray neighbors = new ExpandableIntArray();
//
//		while (re != null) {
//			int gv = re.getDestination();
//			neighbors.add(gv);
//			re = re.getNextEdgeFromSource();
//		}
//
//		return neighbors.toArray();
//	}
//	
//	public int[] getEnds(int source) {
//		GraphEdge ge = rowHeads.get(new Integer(source));
//		ExpandableIntArray neighbors = new ExpandableIntArray();
//
//		while (ge != null) {
//			int gv = ge.getDestination();
//			// only return the edges in which end is larger than source
//			if (gv > source) {
//				neighbors.add(gv);
//			}
//			ge = ge.getNextEdgeFromSource();
//		}
//
//		return neighbors.toArray();
//	}
//
//	public void insert(int source, int destination) {
////		if (source > n | destination > n | source < 1 | destination < 1) {
////			System.err.println("Error: illegal edge (" + source + ","
////					+ destination + ")");
////			return;
////		}
//		Integer s = new Integer(source);
//		Integer d = new Integer(destination);
//		GraphEdge newEdge = new GraphEdge(source, destination);
//		GraphEdge rowHead = this.rowHeads.get(s);
//		GraphEdge columnHead = this.columnHeads.get(d);
//		if (rowHead == null) {
//			this.rowHeads.put(s, newEdge);
//		} else {
//			if (rowHead.getDestination() > destination) {
//				newEdge.setNextEdgeFromSource(rowHead);
//				this.rowHeads.put(s, newEdge);
//			} else if (rowHead.getDestination() < destination) {
//				GraphEdge re = null;
//				for (re = rowHead; re.getNextEdgeFromSource() != null
//						&& re.getNextEdgeFromSource().getDestination() < destination; re = re
//						.getNextEdgeFromSource())
//					;
//				if (re.getNextEdgeFromSource() == null) {
//					re.setNextEdgeFromSource(newEdge);
//				} else {
//					if (re.getNextEdgeFromSource().getDestination() > destination) {
//						newEdge.setNextEdgeFromSource(re
//								.getNextEdgeFromSource());
//						re.setNextEdgeFromSource(newEdge);
//					}
//					else {
//						if (source > destination)
//						m--;
//					}
//				}
//			}
//		}
//		if (columnHead == null) {
//			this.columnHeads.put(d, newEdge);
//		} else {
//			if (columnHead.getSource() > source) {
//				newEdge.setNextEdgeToDestination(columnHead);
//				this.columnHeads.put(d, newEdge);
//			} else if (columnHead.getSource() < source) {
//				GraphEdge re = null;
//				for (re = columnHead; re.getNextEdgeToDestination() != null
//						&& re.getNextEdgeToDestination().getSource() < source; re = re
//						.getNextEdgeToDestination())
//					;
//				if (re.getNextEdgeToDestination() == null) {
//					re.setNextEdgeToDestination(newEdge);
//				} else {
//					if (re.getNextEdgeToDestination().getSource() > source) {
//						newEdge.setNextEdgeToDestination(re
//								.getNextEdgeToDestination());
//						re.setNextEdgeToDestination(newEdge);
//					}
//				}
//			}
//		}
//	}
//
//	public void fillDiagonal() {
//		for (int i = 1; i < n + 1; i++) {
//			this.insert(i, i);
//		}
//	}
//
//	public HashMap<Integer, GraphEdge> getRowHeads() {
//		return rowHeads;
//	}
//
//	public HashMap<Integer, GraphEdge> getColumnHeads() {
//		return columnHeads;
//	}
//
//	public int getN() {
//		return n;
//	}
//
//	public int getM() {
//		return m;
//	}
//
//}
//
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.HashMap;
//
//import edu.whu.rose.clock.ssql.graph.RRadiusSubGraph;
//import edu.whu.rose.clock.ssql.util.ExpandableIntArray;
//import edu.whu.rose.clock.ssql.util.SequentialIntArray;
//
///**
// * @author clock
// * 
// */
//public class GraphMatrix {
//
//	protected final int n; // number of vertexes
//	protected int m; // number of edges
//	// protected HashMap<Integer, GraphVertex> vertexes;
//
//	protected HashMap<Integer, GraphEdge> rowHeads;
//	protected HashMap<Integer, GraphEdge> columnHeads;
//
//	public GraphMatrix(int n) {
//		this.n = n;
//		this.rowHeads = new HashMap<Integer, GraphEdge>();
//		this.columnHeads = new HashMap<Integer, GraphEdge>();
//	}
//
//	// protected void loadVertexes() {
//	// vertexes = new HashMap<Integer, GraphVertex>();
//	// for (int i = 0; i < n; i++) {
//	// vertexes.put(new Integer(i + 1), new GraphVertex(i + 1));
//	// }
//	// }
//
//	public void loadEdges(String dataFile) {
//		this.fillDiagonal();
//		try {
//			BufferedReader br = new BufferedReader(new FileReader(dataFile));
//			while (br.ready()) {
//				String line = br.readLine().trim();
//				String edge = line.substring(line.lastIndexOf(" ") + 1);
//				int pos1 = edge.indexOf("-->");
//				int pos2 = edge.indexOf(":");
//				if (pos1 == -1 || pos2 == -1) {
//					System.err.println("Found a wrong edge: " + edge);
//					continue;
//				}
//				int row = new Integer(edge.substring(0, pos1)).intValue();
//				int column = new Integer(edge.substring(pos1 + 3, pos2))
//						.intValue();
//				if (row != column) {
//					this.insert(row, column);
//					this.insert(column, row);
//					m++;
//				}
//			}
//			System.out.println("Loading finished");
//			br.close();
//		} catch (IOException ex) {
//			ex.printStackTrace();
//		}
//	}
//
//	public RRadiusSubGraph extractSubgraph(int source, int radius) {
//		SequentialIntArray vertexes = new SequentialIntArray();
//		vertexes.insert(source);
//		ExpandableIntArray neighbors = new ExpandableIntArray();
//		neighbors.add(source);
//
//		// breadth-first
//		for (int i = 0; i < radius; i++) {
//			SequentialIntArray newVertexes = new SequentialIntArray();
//			ExpandableIntArray newNeighbors = new ExpandableIntArray();
//			for (int j = 0; j < neighbors.getSize(); j++) {
//				int[] temp = genNeighbors(source);
//				for (int k = 0; k < temp.length; k++) {
//					if (vertexes.binarySearch(temp[k]) == -1) {
//						newVertexes.insert(temp[k]);
//						newNeighbors.add(temp[k]);
//					}
//				}
//			}
//			neighbors = newNeighbors;
//			for (int j = 0; j < newVertexes.getCapacity(); j++) {
//				vertexes.insert(newVertexes.get(j));
//			}
//		}
//		return new RRadiusSubGraph(source, vertexes.getArray());
//	}
//
//	public RRadiusSubGraph extractSubgraph(int source, int radius,
//			double upperBound, double lowerBound) {
//		SequentialIntArray vertexes = new SequentialIntArray();
//		vertexes.insert(source);
//		ExpandableIntArray neighbors = new ExpandableIntArray();
//		neighbors.add(source);
//
//		// breadth-first
//		for (int i = 0; i < radius; i++) {
//			SequentialIntArray newVertexes = new SequentialIntArray();
//			ExpandableIntArray newNeighbors = new ExpandableIntArray();
//			for (int j = 0; j < neighbors.getSize(); j++) {
//				int[] temp = genNeighbors(neighbors.get(j));
//				for (int k = 0; k < temp.length; k++) {
//					if (vertexes.binarySearch(temp[k]) == -1) {
//
//						// if the current subgraph size is going to surpass the
//						// upper bound
//						if (vertexes.getCapacity() + newVertexes.getCapacity() == upperBound) {
//							return new RRadiusSubGraph(source, vertexes
//									.getArray());
//						}
//						newVertexes.insert(temp[k]);
//						newNeighbors.add(temp[k]);
//					}
//				}
//			}
//			neighbors = newNeighbors;
//			for (int j = 0; j < newVertexes.getCapacity(); j++) {
//				vertexes.insert(newVertexes.get(j));
//			}
//		}
//
//		// if the final subgraph size is smaller than the lower bound
//		if (vertexes.getCapacity() < lowerBound) {
//
//		}
//
//		return new RRadiusSubGraph(source, vertexes.getArray());
//	}
//	
//	public RRadiusSubGraph extractSubgraph(int source, int radius,
//			double upperBound, double lowerBound, double neighborBound) {
//		if (source < 1) {
//			return null;
//		}
//		
//		
//		SequentialIntArray vertexes = new SequentialIntArray();
//		vertexes.insert(source);
//		ExpandableIntArray neighbors = new ExpandableIntArray();
//		neighbors.add(source);
//
//		// breadth-first
//		for (int i = 0; i < radius; i++) {
//			SequentialIntArray newVertexes = new SequentialIntArray();
//			ExpandableIntArray newNeighbors = new ExpandableIntArray();
//			for (int j = 0; j < neighbors.getSize(); j++) {
//				int[] temp = genNeighbors(neighbors.get(j));
//				for (int k = 0; k < temp.length; k++) {
//					if (vertexes.binarySearch(temp[k]) == -1) {
//
//						// if the current subgraph size is going to surpass the
//						// upper bound
//						if (vertexes.getCapacity() + newVertexes.getCapacity() == upperBound) {
//							return new RRadiusSubGraph(source, vertexes
//									.getArray());
//						}
//						newVertexes.insert(temp[k]);
//						newNeighbors.add(temp[k]);
//					}
//				}
//			}
//			neighbors = newNeighbors;
//			for (int j = 0; j < newVertexes.getCapacity(); j++) {
//				vertexes.insert(newVertexes.get(j));
//			}
//		}
//
//		// if the final subgraph size is smaller than the lower bound
//		if (vertexes.getCapacity() < lowerBound) {
//
//		}
//
//		return new RRadiusSubGraph(source, vertexes.getArray());
//	}
//
//	public int[] genNeighbors(int source) {
//		GraphEdge re = rowHeads.get(new Integer(source));
//		ExpandableIntArray neighbors = new ExpandableIntArray();
//
//		while (re != null) {
//			int gv = re.getDestination();
//			neighbors.add(gv);
//			re = re.getNextEdgeFromSource();
//		}
//
//		return neighbors.toArray();
//	}
//	
//	public int[] getEnds(int source) {
//		GraphEdge ge = rowHeads.get(new Integer(source));
//		ExpandableIntArray neighbors = new ExpandableIntArray();
//
//		while (ge != null) {
//			int gv = ge.getDestination();
//			// only return the edges in which end is larger than source
//			if (gv > source) {
//				neighbors.add(gv);
//			}
//			ge = ge.getNextEdgeFromSource();
//		}
//
//		return neighbors.toArray();
//	}
//
//	public void insert(int source, int destination) {
////		if (source > n | destination > n | source < 1 | destination < 1) {
////			System.err.println("Error: illegal edge (" + source + ","
////					+ destination + ")");
////			return;
////		}
//		Integer s = new Integer(source);
//		Integer d = new Integer(destination);
//		GraphEdge newEdge = new GraphEdge(source, destination);
//		GraphEdge rowHead = this.rowHeads.get(s);
//		GraphEdge columnHead = this.columnHeads.get(d);
//		if (rowHead == null) {
//			this.rowHeads.put(s, newEdge);
//		} else {
//			if (rowHead.getDestination() > destination) {
//				newEdge.setNextEdgeFromSource(rowHead);
//				this.rowHeads.put(s, newEdge);
//			} else if (rowHead.getDestination() < destination) {
//				GraphEdge re = null;
//				for (re = rowHead; re.getNextEdgeFromSource() != null
//						&& re.getNextEdgeFromSource().getDestination() < destination; re = re
//						.getNextEdgeFromSource())
//					;
//				if (re.getNextEdgeFromSource() == null) {
//					re.setNextEdgeFromSource(newEdge);
//				} else {
//					if (re.getNextEdgeFromSource().getDestination() > destination) {
//						newEdge.setNextEdgeFromSource(re
//								.getNextEdgeFromSource());
//						re.setNextEdgeFromSource(newEdge);
//					}
//					else {
//						if (source > destination)
//						m--;
//					}
//				}
//			}
//		}
//		if (columnHead == null) {
//			this.columnHeads.put(d, newEdge);
//		} else {
//			if (columnHead.getSource() > source) {
//				newEdge.setNextEdgeToDestination(columnHead);
//				this.columnHeads.put(d, newEdge);
//			} else if (columnHead.getSource() < source) {
//				GraphEdge re = null;
//				for (re = columnHead; re.getNextEdgeToDestination() != null
//						&& re.getNextEdgeToDestination().getSource() < source; re = re
//						.getNextEdgeToDestination())
//					;
//				if (re.getNextEdgeToDestination() == null) {
//					re.setNextEdgeToDestination(newEdge);
//				} else {
//					if (re.getNextEdgeToDestination().getSource() > source) {
//						newEdge.setNextEdgeToDestination(re
//								.getNextEdgeToDestination());
//						re.setNextEdgeToDestination(newEdge);
//					}
//				}
//			}
//		}
//	}
//
//	public void fillDiagonal() {
//		for (int i = 1; i < n + 1; i++) {
//			this.insert(i, i);
//		}
//	}
//
//	public HashMap<Integer, GraphEdge> getRowHeads() {
//		return rowHeads;
//	}
//
//	public HashMap<Integer, GraphEdge> getColumnHeads() {
//		return columnHeads;
//	}
//
//	public int getN() {
//		return n;
//	}
//
//	public int getM() {
//		return m;
//	}
//
//}
//
//
//
//
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.File;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.Arrays;
//
//import edu.whu.rose.clock.ssql.sang.dataStruct.RRadiusSubGraph;
//import edu.whu.rose.clock.ssql.sang.util.ExpandableIntArray;
//import edu.whu.rose.clock.ssql.sang.util.SequentialIntArray;
//
//public class GraphMatrix 
//{
//	private final int vertexSum;/**节点总数**/
//	private int edgeSum=0;/**边的总数**/
//	
//	protected HashMap<Integer,GraphEdge> rowEdgeHead;/**存储边关系的hash链表对象**/
//	
//	public GraphMatrix(int vertexSum)
//	{
//		this.vertexSum = vertexSum;
//		rowEdgeHead = new HashMap<Integer,GraphEdge>();
//	}
//	
//	/**
//	 * 初始化图矩阵
//	 * @param edgeFile
//	 */
//	public void init(String edgeFile)
//	{
//		/**
//		 * 先将单个的节点加入到图矩阵的对角线上
//		 * 构成有单个节点组成的子图集合*/
//		for(int index=0;index<vertexSum;index++)
//		{
//			addEdgeToMatrix(index,index,false);
//		}
//		
//		/**从文件中读取边的信息*/
//		try
//		{
//			BufferedReader br = new BufferedReader(new FileReader(edgeFile));
//			while (br.ready()) 
//			{
//				String line = br.readLine().trim();
//				String edge = line.substring(line.lastIndexOf(" ") + 1);
//				int pos1 = edge.indexOf("-->");
//				int pos2 = edge.indexOf(":");
//				if (pos1 == -1 || pos2 == -1) 
//				{
//					System.err.println("Found a wrong edge: " + edge);
//					continue;
//				}
//				int row = new Integer(edge.substring(0, pos1)).intValue();/**边的一个节点*/
//				int column = new Integer(edge.substring(pos1 + 3, pos2)).intValue();/**边的另一个节点*/
//				if (row != column) 
//				{
//					edgeSum++;/**先增加，然后再插入过程中去忽略重复的记录，并将边的数量减少*/
//					this.addEdgeToMatrix(row, column,true);/**第一次判断的重复为准*/
//					this.addEdgeToMatrix(column, row,false);					
//				}
//			}
//			System.out.println("Loading finished! The sum of edges is:"+edgeSum);
//			br.close();
//			
//		}catch(IOException ex)
//		{
//			ex.printStackTrace();
//		}
//		
//	}
//
//	/**
//	 * 将一条边关系加入到图矩阵中
//	 * @param begin：边的起始点
//	 * @param end：边的结束点
//	 * @param isOutput:是否写入到文件
//	 */
//	public void addEdgeToMatrix(int begin,int end,boolean isOutput)
//	{
//		Integer sour = new Integer(begin);/**起始点*/
//		Integer dest = new Integer(end);
//		
//		GraphEdge newEdge=new GraphEdge(sour,dest,null,null);/**新建一条边,将引用都赋值为null**/
//		GraphEdge tempEdge = rowEdgeHead.get(sour);/**找到以该点为起始点的所有边的链表集合，并返回该集合中的第一条边*/
//		
//		if(tempEdge == null)/**说明以该点为起始点的边还没有存入图矩阵*/
//		{
//			rowEdgeHead.put(sour, newEdge);/**新加入*/
//		}
//		else
//		{
//			/**构建顺序链表，先得判断两条边结束点的大小，然后再决定如何插入*/
//			if(dest < tempEdge.getDestination())
//			{
//				/**将newEdge放到最前面，成为新的头节点*/
//				newEdge.setNextEdge(tempEdge);/**建立和“新的头节点”间的联系*/
//				rowEdgeHead.put(sour, newEdge);			
//			}
//			else if(dest > tempEdge.getDestination())
//			{
//				/**依次遍历链表，找到合适的位置再插入*/
//				GraphEdge rightEdge = tempEdge;
//				while((rightEdge.getNextEdge()!=null)&&(rightEdge.getNextEdge().getDestination() < dest))
//				{
//					rightEdge = rightEdge.getNextEdge();
//				}
//				
//				if(rightEdge.getNextEdge() == null)/**到达链表的结尾，直接将新边加到链表的尾部*/
//				{
//					rightEdge.setNextEdge(newEdge);
//				}
//				else/**可能找到了合适的位置*/
//				{			
//					if(rightEdge.getNextEdge().getDestination() > dest)/**找到*/
//					{
//						newEdge.setNextEdge(rightEdge.getNextEdge());
//						rightEdge.setNextEdge(newEdge);
//					}
//					else/**注：如果相等，边数不增加*/
//					{
//						if(isOutput)/**写入文件*/
//						{
//							edgeSum--;
//							printToFile("edgeDecrease.txt","not first: "+sour+"---"+dest);/**记录重复信息，测试用*/
//						}
//					}
//				}
//				
//			}
//			else/**注：如果相等，边数不增加; 此处为第一次判断*/
//			{
//				if(isOutput)/**写入文件*/
//				{
//					edgeSum--;
//					printToFile("edgeDecrease.txt","first: "+sour+"---"+dest);/**记录重复信息，测试用*/
//				}
//			}
//		}
//		
//	}
//
//	/**
//	 * 将字符串写到文本文件中，用于测试
//	 * @param fileName：文件的路径或者文件名
//	 * @param content：要写入的内容
//	 */
//	public void printToFile(String fileName,String content)
//	{	
//		FileWriter fileW = null;
//		try
//		{
//			File f=new File(fileName);
//			if(!f.exists())/**如果文件不存在，则创建*/
//			{
//				f.createNewFile();
//			}
//			fileW=new FileWriter(fileName,true);
//			
//			fileW.write(content+"\r\n");
//		}
//		catch(IOException ex)
//		{
//			ex.printStackTrace();
//		}
//		finally
//		{
//			try
//			{
//				if(fileW!=null)
//				{
//					fileW.close();
//				}
//			}catch(IOException ex)
//			{
//				ex.printStackTrace();
//			}
//		}
//	}
//
//	/**
//	 * 取得图中的节点数
//	 * @return
//	 */
//	public int getVertexSum()
//	{
//		return vertexSum;
//	}
//
//	/**
//	 * 抽取以source节点为中心的radius子图
//	 * @param source
//	 * @param radius
//	 * @param upperBound
//	 * @param lowerBound
//	 * @return
//	 */
//	public RRadiusSubGraph extractSubgraph(int source, int radius,double upperBound, double lowerBound) 
//	{
//		SequentialIntArray vertexes = new SequentialIntArray();/**存储最终的节点集合*/
//		vertexes.insert(source);/**先加入起始节点*/
//		
//		ExpandableIntArray neighbors = new ExpandableIntArray();
//		neighbors.add(source);
//
//		for (int i = 0; i < radius; i++) 
//		{
//			SequentialIntArray newVertexes = new SequentialIntArray();
//			ExpandableIntArray newNeighbors = new ExpandableIntArray();
//			
//			for (int j = 0; j < neighbors.getSize(); j++) 
//			{
//				int[] temp = getNeighbors(neighbors.get(j));
//				for (int k = 0; k < temp.length; k++) 
//				{
//					if (vertexes.binarySearch(temp[k]) == -1) 
//					{
//						if (vertexes.getCapacity() + newVertexes.getCapacity() == upperBound) 
//						{
//							return new RRadiusSubGraph(source, vertexes
//									.getArray());
//						}
//						newVertexes.insert(temp[k]);
//						newNeighbors.add(temp[k]);
//					}
//				}
//			}
//			neighbors = newNeighbors;
//			for (int j = 0; j < newVertexes.getCapacity(); j++) {
//				vertexes.insert(newVertexes.get(j));
//			}
//		}
//		return new RRadiusSubGraph(source, vertexes.getArray());
//	}
//	
//	/**
//	 * 得到当前节点的临近节点，distance为1
//	 * @param source：当前节点
//	 * @return
//	 */
//	public int[] getNeighbors(int source) 
//	{
//		GraphEdge re = rowEdgeHead.get(new Integer(source));
//		ExpandableIntArray neighbors = new ExpandableIntArray();
//		while (re != null) 
//		{
//			neighbors.add(new Integer(re.getDestination()));/**暂存临近节点*/
//			re = re.getNextEdge();
//		}
//
//		return neighbors.toArray();
//	}
//}
