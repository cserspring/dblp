package edu.whu.wang.dataStruct;

public class GraphEdge {

	private int source;
	private int destination;
	private GraphEdge nextEdgeFromSource;
	private GraphEdge nextEdgeToDestination;
	
	public GraphEdge(int source, int destination) {
		this(source, destination, null, null);
	}
	
	public GraphEdge(int source, int destination,
			GraphEdge nextEdgeFromSource,
			GraphEdge nextEdgeToDestination) {
		super();
		this.source = source;
		this.destination = destination;
		this.nextEdgeFromSource = nextEdgeFromSource;
		this.nextEdgeToDestination = nextEdgeToDestination;
	}

	public GraphEdge getNextEdgeFromSource() {
		return nextEdgeFromSource;
	}

	public void setNextEdgeFromSource(GraphEdge nextEdgeFromSource) {
		this.nextEdgeFromSource = nextEdgeFromSource;
	}

	public GraphEdge getNextEdgeToDestination() {
		return nextEdgeToDestination;
	}

	public void setNextEdgeToDestination(GraphEdge nextEdgeToDestination) {
		this.nextEdgeToDestination = nextEdgeToDestination;
	}

	public int getSource() {
		return source;
	}

	public int getDestination() {
		return destination;
	}

}

//
///**
// * 有向图中的边，双向链表结构
// * @author Administrator
// *
// */
//public class GraphEdge
//{	
//		private int source;/**边的起始点**/
//		private int destination;/**边的结束点**/
//		private GraphEdge nextEdge;/**当前边节点的下一个边节点**/
//		//private GraphEdge prevEdge;/**当前边节点的上一个节点**/
//		
//
//		/**
//		 * 构造函数
//		 */
//		public GraphEdge(int source, int destination,GraphEdge nextEdge,GraphEdge prevEdge) 
//		{
//			this.source = source;
//			this.destination = destination;
//			this.nextEdge = nextEdge;
//			//this.prevEdge = prevEdge;
//		}
//
//		public GraphEdge getNextEdge() 
//		{
//			return nextEdge;
//		}
//
//		public void setNextEdge(GraphEdge nextEdge) 
//		{
//			this.nextEdge = nextEdge;
//		}
//
////		public GraphEdge getPrevEdge() 
////		{
////			return prevEdge;
////		}
////
////		public void setPrevEdge(GraphEdge prevEdge) 
////		{
////			this.prevEdge = prevEdge;
////		}
//
//		public int getSource() 
//		{
//			return source;
//		}
//
//		public int getDestination() 
//		{
//			return destination;
//		}
//}
