import java.util.Scanner;
import java.util.Stack;

public class Main {
	private Scanner input=new Scanner(System.in);
	private int column=0, row=0, hillClimbed=0;
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
		System.out.print("# rows: ");
		landscapeRow = input.nextInt();
		System.out.print("# columns: ");
		landscapeColumn = input.nextInt();
		
		landscape = new boolean[landscapeRow][landscapeColumn];
	}//setLandscapeSize
	
	public void setInitialRobotPosition() {
		System.out.println("Type robot's position.");
		System.out.print("Row: ");
		row=input.nextInt();
		System.out.print("Column: ");
		column=input.nextInt();

		
		landscape[row][column]=true;
		hillClimbed++;
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
			node.nodeToString();
			System.out.println("");
		}//while
	}//moveAgent
	
	public void start() {
		Node n=null;
		setLandscapeSize();
		setInitialRobotPosition();
		Tree tree = new Tree(row, column, hillClimbed, landscape);
		while (n == null) {
			n=tree.nextDepth();
		}//while
		System.out.println("**********************************************");
		System.out.println("       Problem solved....");
		System.out.println("**********************************************");
		moveRobot(getPath(n));		
	}//start
	
	public static void main(String[] args) {
		Main main = new Main();
		main.start();
	}//main
	

}//class
