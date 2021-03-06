package edu.whu.wang.treeeditdistance;

import java.util.HashMap;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

/**
 * This defines are tree. The tree contains labelled nodes. Labels must be
 * unique. Sibling order is defined by the order in which edges are added to a
 * parent node.
 * 
 * Populating the tree is done at construction time, which must also specify
 * what type of tree node traversal to number all the nodes. Default is
 * POSTORDER
 * 
 * INSERT-LICENCE-INFO
 */
public class BasicTree extends TreeDefinition {

	// A set of nodes (once set, this is not changed)
	public HashMap<String, ArrayList<String>> treeStructure = new HashMap<String, ArrayList<String>>();

	public BasicTree() {
	}

	/**
	 * This takes a |e| x 2 array of string, where |e| is the number of edges.
	 */
	public BasicTree(HashMap<String, ArrayList<String>> tree, String _root,
			int ordering) {
		setRoot(_root);
		treeStructure = tree;

		//System.out.println("TreeDefinition.init: root: " + getRoot());

		orderNodes(ordering);

	}

	/** Returns the parent nodes in the tree */
	public Collection<String> getNodes() {
		return treeStructure.keySet();
	}

	/** Returns the children of the node given as a parameter. */
	public List<String> getChildren(String nodeLabel) {
		return treeStructure.get(nodeLabel);
	}

	public String toString() {
		int rootID = getRootID();
		StringBuffer rStr = new StringBuffer();

		for (int i = rootID; i > 0; i--) {
			rStr.append(getLabel(i) + "(" + i + ") \n");
			for (String child : getChildren(getLabel(i))) {
				rStr.append(" - " + child + "(" + getNodeID(child) + ")  \n");
			}
		}
		return rStr.toString();
	}
}