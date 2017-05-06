import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;

public class Tree {
	private final int UP=0, RIGHT=1, DOWN=2, LEFT=3;
	Node root=null;
	Queue<Node> queue = new LinkedList<Node>();
	Queue<Node> nextQueue = new LinkedList<Node>();
	int maxHillClimbed=0;
	Hashtable<String, Integer> states = new Hashtable<String, Integer>();
	
	public Tree(int row, int column, int hill, boolean[][] landscape) {
		root = new Node(null, generateUniqueId(row, column, landscape), row, column, hill, landscape);
		queue.add(root);
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

	public Node conditionActionRules(int OPERATION, Node n) {
		int row= n.getRow();
		int column=n.getColumn();
		boolean[][] landscape=n.getLandscape();
		boolean flag=false;
		
		switch (OPERATION) {
		case UP:
			row-=1;
			flag=isRowValidMovement(row);
			break;
			
		case RIGHT:
			column+=1;
			flag=isColumnValidMovement(column);
			break;
			
		case DOWN:
			row+=1;
			flag=isRowValidMovement(row);
			break;
			
		case LEFT:
			column-=1;
			flag=isColumnValidMovement(column);
			break;
		}//switch

		if (flag) {
			if (landscape[row][column]) {
				String state=generateUniqueId(row, column, landscape);
				if (isNewState(state)) {
					return new Node(n, state, row, column, n.getHill(), landscape);
				} else {
					return null;
				}//if{}else{}
			} else {
				
				landscape[row][column]=true;
				String state=generateUniqueId(row, column, landscape);
				if (isNewState(state)) {
					Node newNode = new Node(n, state, row, column, n.getHill()+1, landscape);
					landscape[row][column]=false;
					maxHillClimbed=n.getHill()+1;
					return newNode;
				} else {
					return null;
				}//if{}else{}
			}//if{}else{}			
		} else {
			return null;
		}//if{}else{}
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
		if (node.isReachedGoal()) {
			return node;
		} else {
			Node child[]= new Node[4];

			child[0]=conditionActionRules(UP, node);
			child[1]=conditionActionRules(RIGHT,node);
			child[2]=conditionActionRules(DOWN, node);
			child[3]=conditionActionRules(LEFT, node);
			for (int i=0; i < 4; i++) {
				if (child[i] != null) {
					if (child[i].getHill() == maxHillClimbed) {
						node.addChild(child[i]);
						nextQueue.add(child[i]);
					}//if{}
				}//if{}
			}//for{}
		}//if{}else{}
		
		return null;
	}//successorFunction
	
	public Node nextDepth() {
		Node n=null;
		while (!queue.isEmpty()) {
			n = successorFunctionReachedGoal(queue.remove());
		}//while
		while (!nextQueue.isEmpty()) {
			queue.add(nextQueue.remove());
		}
		return n;
	}//nextDepth

}//Tree
