import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;

public class Tree {
	private final int UP=0, RIGHT=1, DOWN=2, LEFT=3;
	Node root=null;
	Queue<Node> queue = new LinkedList<Node>();
	Queue<Node> nextQueue = new LinkedList<Node>();
	Hashtable<String, Integer> states = new Hashtable<String, Integer>();
	
	public Tree(int row, int column, boolean[][] landscape) {
		root = new Node(null, generateUniqueId(row, column, landscape), row, column, landscape);
		queue.add(root);
		root.nodeToString();
	}//constructor
	
	public boolean isNewState(String idState) {
		return !states.containsKey(idState);
	}//newState
	
	public boolean isRowValidMovement(int row) {
		return row >= 0 && row < Main.landscapeRow;
	}//isXRowalidMovement
	
	public boolean isColumnValidMovement(int column) {
		return column >= 0 && column < Main.landscapeColumn;
	}//isColumnValidMovement

	public String conditionActionRules(int OPERATION, int row, int column, boolean[][] landscape) {
		String state=null;
		switch (OPERATION) {
		case UP:
			if (isRowValidMovement(row-1)) {
				landscape[row-1][column]=true;
				state=generateUniqueId(row-1, column, landscape);
				if (isNewState(state)) {
					return state;
				} else {
					return null;
				}//if{}else{}
			} else {
				return null;
			}//if{}else{}
			
		case RIGHT:
			if (isColumnValidMovement(column+1)) {
				landscape[row][column+1]=true;
				state=generateUniqueId(row, column+1, landscape);
				if (isNewState(state)) {
					return state;
				} else {
					return null;
				}//if{}else{}
			} else {
				return null;
			}//if{}else{}
			
		case DOWN:
			if (isRowValidMovement(row+1)) {
				landscape[row+1][column]=true;
				state=generateUniqueId(row+1, column, landscape);
				if (isNewState(state)) {
					return state;
				} else {
					return null;
				}//if{}else{}
			} else {
				return null;
			}//if{}else{}
			
		case LEFT:
			if (isColumnValidMovement(column-1)) {
				landscape[row][column-1]=true;
				state=generateUniqueId(row, column-1, landscape);
				if (isNewState(state)) {
					return state;
				} else {
					return null;
				}//if{}else{}
			} else {
				return null;
			}//if{}else{}
			
			default:
				return null;
		}//switch
	}//conditionActionRules
	
	private String generateUniqueId(int row, int column, boolean[][] landscape) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.valueOf(row));
		sb.append(String.valueOf(column));
		for (int i=0; i < Main.landscapeRow ; i++) {
			for (int j=0; j < Main.landscapeColumn ; j++) {
				if (landscape[i][j]) {
					sb.append('T');
				} else {
					sb.append('F');
				}//if{}else{}
			}//for column
		}//for row
		return sb.toString();
	}//generateUniqueId
	
	public boolean[][] setDiscoveredRegion(boolean[][] landscape, int row, int column) {
		landscape[row][column]=true;
		return landscape;
	}//setDiscoveredRegion

	public Node successorFunctionReachedGoal(Node node){
		Node child=null;
		String state=null;
		int row= node.getRow();
		int column=node.getColumn();
		boolean[][] landscape=node.getLandscape();
		
		if ((state = conditionActionRules(UP, row, column,landscape)) != null) {
			System.out.println("Entre al UP ");
			child = new Node(node, state, row-1, column, setDiscoveredRegion(landscape,row-1,column));
			node.addChild(child);
			nextQueue.add(child);
			child.nodeToString();
			if (child.isReachedGoal()) {
				return child;
			}//if{}
		}//if{}
		
		node.nodeToString();
		if ((state = conditionActionRules(RIGHT, row, column,landscape)) != null) {
			System.out.println("Entre al RIGHT ");
			child = new Node(node, state, row, column+1, setDiscoveredRegion(landscape,row,column+1));
			node.addChild(child);
			nextQueue.add(child);
			child.nodeToString();
			if (child.isReachedGoal()) {
				return child;
			}//if{}
		}//if{}
		
		if ((state = conditionActionRules(DOWN, row, column,landscape)) != null) {
			System.out.println("Entre al DOWN ");
			child = new Node(node, state, row+1, column, setDiscoveredRegion(landscape,row+1,column));
			node.addChild(child);
			nextQueue.add(child);
			child.nodeToString();
			if (child.isReachedGoal()) {
				return child;
			}//if{}
		}//if{}
		
		if ((state = conditionActionRules(LEFT, row, column,landscape)) != null) {
			System.out.println("Entre al LEFT ");
			child = new Node(node, state, row, column-1, setDiscoveredRegion(landscape,row,column-1));
			node.addChild(child);
			nextQueue.add(child);
			child.nodeToString();
			if (child.isReachedGoal()) {
				return child;
			}//if{}
		}//if{}
		return null;
	}//successorFunction
	
	public Node nextDepth() {
		Node n=null;
		while (!queue.isEmpty()) {
			n = successorFunctionReachedGoal(queue.remove());
		}//while
		queue = nextQueue;
		return n;
	}//nextDepth

}//Tree
