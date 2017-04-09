import java.util.Scanner;
import java.util.Stack;

public class Main {
	private Scanner input=new Scanner(System.in);
	private int column=0, row=0;
	private boolean[][] landscape=null;
	public static int landscapeRow,landscapeColumn;
	
	public boolean[][] getLandscape() {
		return landscape;
	}//getLandscape
	
	public void setLandscape(boolean[][] landscape) {
		this.landscape=landscape;
	}//setLandscape
	
	public void setLandscapeSize() {
		System.out.println("Type the size of the landscape.");
		System.out.print("# columns: ");
		landscapeColumn = input.nextInt();
		System.out.print("# rows: ");
		landscapeRow = input.nextInt();
		
		
		landscape = new boolean[landscapeRow][landscapeColumn];
	}//setLandscapeSize
	
	public void setInitialRobotPosition() {
		System.out.println("Type robot's position.");
		System.out.print("Column: ");
		column=input.nextInt();
		System.out.print("Y: ");
		row=input.nextInt();
		
		landscape[row][column]=true;
	}//setRobotPosition()
	
	public void setRobotPosition(int row, int column) {
		this.row=row;
		this.column=column;
	}//setRobotPosition;
	
	public Stack<Node> getPath(Node n) {
		Stack<Node> stack= new Stack<Node>();
		while (n.parent != null) {
			stack.push(n);
			n=n.parent;
		}//while
		stack.push(n);
		return stack;
	}//getPath
	
	public void moveRobot(Stack<Node> s) {
		Node node=null;
		while (!s.isEmpty()) {
			node=s.pop();
			setRobotPosition(node.getRow(), node.getColumn());
		}//while
	}//moveAgent
	
	public void start() {
		setLandscapeSize();
		setInitialRobotPosition();
		Node n=null;
//		Tree tree = new Tree(row, column, landscape);
//		tree.nextDepth();
//		while (n != null) {
//			n=tree.nextDepth();
//		}//while
		n = new Node(null, "A", row, column, landscape);

		landscape[row+1][column]=true;
		Node c = new Node(n, "B", row+1, column, landscape);
		
		landscape[row][column+1]=true;
		Node c2 = new Node(n, "C", row, column+1, landscape);
		
		n.nodeToString();
		c.nodeToString();
		c2.nodeToString();
		
		//moveRobot(getPath(n));		
	}//start
	
	public static void main(String[] args) {
		Main main = new Main();
		main.start();
	}//main
	

}//class
