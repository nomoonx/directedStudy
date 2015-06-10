package thesis_network_growth;

import java.util.ArrayList;




public class Tree {
	
	Object data;
	Group[] groupsAtNode;
	ArrayList<Tree> nodes;
	int sYear, eYear;
	
	
	public Tree (Object tData, boolean hasTimeline, int startYear, int endYear) {
		this.data = tData;
		this.nodes = new ArrayList<Tree>();
		this.sYear = startYear;
		this.eYear = endYear;
		
		if (hasTimeline) {
			// If there is a timeline for this category. That is, there is a Group for each year over a span (i.e. schools have timelines)
			int timeSpan = endYear - startYear + 1;
			groupsAtNode = new Group[timeSpan];
			int g;
			int year;
			for (g = 0; g < timeSpan; g++) {
				year = startYear+g;
				groupsAtNode[g] = new Group(tData.toString(), year);
			} // end for g (loop through timeline years)
		} else {
			// If there is not a timeline for this category.
			groupsAtNode = new Group[1];
			//System.out.println("new singular group: " + tData);
			groupsAtNode[0] = new Group(tData.toString());
		} // end if (check if tree is timelined or singular)
	} // end Tree() constructor
	
	public Tree addTree (String label, boolean hasTimeline, int startYear, int endYear) {
		Tree node = new Tree(label, hasTimeline, startYear, endYear);
		this.nodes.add(node);
		return node;
	} // end addTree()

	public Tree addTree (String label, boolean inheritsTimeline) {
		if (ValidationTools.empty(this.sYear) || ValidationTools.empty(this.eYear)) {
			System.err.println("Attempting to create an i sub-tree '" + label + "', but its parent tree does not contain a timeline.");
			return null;
		}
		Tree node = new Tree(label, true, this.sYear, this.eYear);
		this.nodes.add(node);
		return node;
	} // end addTree()
	
	public Tree addTree (Tree refTree) {
		this.nodes.add(refTree);
		return refTree;
	} // end addTree()
	
	public void displayTree () {
		// To display the entire tree, call traversal() which displays the given tree, and begin at depth 0.
		traversalDisplay(this, 0);
	} // end displayTree()
	
	private void traversalDisplay (Tree tree, int depth) {
		// Display the nodes of the given tree, and all of it's sub-trees, by recursively entering each node and displaying the node name.
		if (this == null) {
			return;
		} // end if (checking if tree is null - implying it is a leaf)

		int numNodes = tree.nodes.size();
		int i;

		for (i = 0; i < depth; i++) {
			System.out.print("\t");
		} // end for i (tabulating based on depth)
		depth += 1; // Increment depth each time recursively in the function.
		//System.out.println("- " + tree.data + " (" + tree.groupsAtNode.length + ") - {" + tree.groupsAtNode[0].GroupLabel + " " + tree.groupsAtNode[0].GroupYear + "}");
		

		int numYearsInNode = tree.groupsAtNode.length;
		System.out.println("- " + tree.data + " - {" + tree.groupsAtNode[0].GroupLabel + " " + tree.groupsAtNode[0].GroupYear + "} -> {" + tree.groupsAtNode[numYearsInNode-1].GroupLabel + " " + tree.groupsAtNode[numYearsInNode-1].GroupYear + "}");			

		
		for (i = 0; i < numNodes; i++) {
			traversalDisplay((Tree)tree.nodes.get(i), depth);
		} // end for i (looping through children nodes of tree)
	} // end traversal()
	
	

	public ArrayList<Tree> getChildren () {
		return nodes;
	} // end getChildren()
	
	
	public Tree getChildByName(String nodeName) {
		int numNodes = this.nodes.size();
		int i;
		Tree node;
		for (i = 0; i < numNodes; i++) {
			node = (Tree)this.nodes.get(i);
			
			if (node.data.equals(nodeName)) {
				// If the child node has the label that matches the given nodeName, then return this sub-tree.
				return node;
			}
		} // end for i (looping through child nodes)
		
		// If none of the child nodes matches the name as the given nodeName, then return null.
		return null;
	}
	
	/*
	private void traversal (Tree tree, int depth) {
		// Display the nodes of the given tree, and all of it's sub-trees, by recursively entering each node and displaying the node name.
		if (this == null) {
			return;
		} // end if (checking if tree is null - implying it is a leaf)

		int numNodes = tree.nodes.size();
		int i;

		for (i = 0; i < depth; i++) {
			System.out.print("\t");
		} // end for i (tabulating based on depth)
		depth += 1; // Increment depth each time recursively in the function.
		System.out.println("- " + tree.data + " (" + tree.groupsAtNode.length + ") - {" + tree.groupsAtNode[0].GroupLabel + " " + tree.groupsAtNode[0].GroupYear + "}");
		
		for (i = 0; i < numNodes; i++) {
			traversal((Tree)tree.nodes.get(i), depth);
		} // end for i (looping through children nodes of tree)
	} // end traversal()
	*/
	
	
	public static ArrayList<Object> treeArrayToObjectArray (ArrayList<Tree> arr) {
		int i;
		int numElements = arr.size();
		ArrayList<Object> newArr = new ArrayList<Object>();
		for (i = 0; i < numElements; i++) {
			
			newArr.add(arr.get(i));
			
		} // end for i (loop through all array elements)
		
		return newArr;
		
	} // end treeArrayToObjectArray()
	
	
	public void DestroyAllNodes (int depth) {
		traverseAndDestroy(this, 0);
	} // end DestroyAllNodes()
	
	
	private void traverseAndDestroy (Tree tree, int depth) {
		// Display the nodes of the given tree, and all of it's sub-trees, by recursively entering each node and displaying the node name.
		if (this == null) {
			return;
		} // end if (checking if tree is null - implying it is a leaf)

		int numNodes = tree.nodes.size();
		int i;

		//for (i = 0; i < depth; i++) {
			//System.out.print("\t");
		//} // end for i (tabulating based on depth)
		depth += 1; // Increment depth each time recursively in the function.
		//System.out.println("- " + tree.data + " (" + tree.groupsAtNode.length + ") - {" + tree.groupsAtNode[0].GroupLabel + " " + tree.groupsAtNode[0].GroupYear + "}");
		

		//int numYearsInNode = tree.groupsAtNode.length;
		//System.out.println("- " + tree.data + " - {" + tree.groupsAtNode[0].GroupLabel + " " + tree.groupsAtNode[0].GroupYear + "} -> {" + tree.groupsAtNode[numYearsInNode-1].GroupLabel + " " + tree.groupsAtNode[numYearsInNode-1].GroupYear + "}");			

		// Release memory.
		groupsAtNode = null;
		//nodes = null;
		
		
		for (i = 0; i < numNodes; i++) {
			traverseAndDestroy((Tree)tree.nodes.get(i), depth);
		} // end for i (looping through children nodes of tree)
	} // end traversal()
	
	
	
}
