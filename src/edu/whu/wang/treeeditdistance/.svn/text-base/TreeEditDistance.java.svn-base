package edu.whu.wang.treeeditdistance;

public class TreeEditDistance {

	private String firstTreeString;
	private String secondTreeString;
	
	public TreeEditDistance(String firstTreeString, String secondTreeString){
		this.firstTreeString = firstTreeString;
		this.secondTreeString = secondTreeString;
	}
	
	public double getDistance(){
		TreeDefinition aTree = CreateTreeHelper.makeTree(firstTreeString);
		//System.out.println("The tree is: \n"+aTree);
		TreeDefinition bTree = CreateTreeHelper.makeTree(secondTreeString);
		//System.out.println("The tree is: \n"+bTree);
	
		ComparisonZhangShasha treeCorrector = new ComparisonZhangShasha();
		OpsZhangShasha costs = new OpsZhangShasha();
		Transformation transform = treeCorrector.findDistance(aTree, bTree, costs);
		return transform.getCost();
	}
	
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		String str1 = "a-b";
//		String str2 = "a-b;a-c";
//		TreeEditDistance obj = new TreeEditDistance(str1, str2);
//		System.out.println(obj.getDistance());
//	}

}
