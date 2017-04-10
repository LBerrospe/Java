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

	public Node successorFunctionReachedGoal(Node node){
		Node child=null;
		String state=null;
		int row= node.getRow();
		int column=node.getColumn();
		boolean[][] landscape=node.getLandscape();
		
		if ((state = conditionActionRules(UP, row, column,landscape)) != null) {
			System.out.println("UP ");
			landscape[row-1][column]=true;
			child = new Node(node, state, row-1, column, landscape);
			landscape[row-1][column]=false;
			node.addChild(child);
			nextQueue.add(child);
			child.nodeToString();
			if (child.isReachedGoal()) {
				return child;
			}//if{}
		}//if{}
		

		if ((state = conditionActionRules(RIGHT, row, column,landscape)) != null) {
			System.out.println("RIGHT ");
			landscape[row][column+1]=true;
			child = new Node(node, state, row, column+1, landscape);
			landscape[row][column+1]=false;
			node.addChild(child);
			nextQueue.add(child);
			child.nodeToString();
			if (child.isReachedGoal()) {
				return child;
			}//if{}
		}//if{}
		
		if ((state = conditionActionRules(DOWN, row, column,landscape)) != null) {
			System.out.println("DOWN ");
			landscape[row+1][column]=true;
			child = new Node(node, state, row+1, column, landscape);
			landscape[row+1][column]=false;
			node.addChild(child);
			nextQueue.add(child);
			child.nodeToString();
			if (child.isReachedGoal()) {
				return child;
			}//if{}
		}//if{}
		
		if ((state = conditionActionRules(LEFT, row, column,landscape)) != null) {
			System.out.println("LEFT ");
			landscape[row][column-1]=true;
			child = new Node(node, state, row, column-1, landscape);
			landscape[row][column-1]=false;
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
		while (!queue.isEmpty() && n == null) {
			n = successorFunctionReachedGoal(queue.remove());
		}//while
		queue = nextQueue;
		return n;
	}//nextDepth

}//Tree
