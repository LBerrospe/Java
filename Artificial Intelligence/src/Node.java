import java.util.ArrayList;

public class Node {
	Node parent=null;
	private String state=null;
	private int row=0, column=0;
	private int hillClimbed=0;
	private boolean[][] landscape=null;
	private boolean reachedGoal=false;
	private ArrayList<Node> children=null;
	
	
	public Node(Node parent, String state, int row, int column, int hillClimbed, boolean landscape[][]) {
		this.parent=parent;
		this.state=state;
		this.row=row;
		this.column=column;
		this.hillClimbed=hillClimbed;
		this.landscape= new boolean[landscape.length][];
		for(int i=0; i<landscape.length; i++)
			this.landscape[i]=landscape[i].clone();
		this.reachedGoal=verifyReachedGoal();
		children = new ArrayList<Node>();
	}//Constructor
	
	public String getState() {		return state;	}
	public void setState(String state) {	this.state = state;	}
	
	public int getRow() {	return row;	}
	public void setRow(int row) {	this.row = row;	}

	public int getColumn() {	return column;	}
	public void setColumn(int column) {	this.column = column;	}
	
	public int getHill() {	return hillClimbed;	}
	public void setHill(int hillClimbed) {	this.hillClimbed = hillClimbed;	}

	public boolean[][] getLandscape() {	return landscape.clone();	}
	public void setLandscape(boolean[][] landscape) {	this.landscape = landscape;	}
	
//	public boolean getLandscapePosition(int row, int column) { return this.landscape[row][column];	}
	
	public boolean isReachedGoal() {	return reachedGoal;	}

	public void addChild( Node child ) {	children.add(child);	}
	
	public boolean verifyReachedGoal() {
		boolean flag=true;
		for (int i=0; i < Main.landscapeRow; i++) {
			for (int j=0; j < Main.landscapeColumn; j++) {
				if (!landscape[i][j]) {
					flag=false;
					break;
				}//if{}
			}//for
		}//for
		return flag;
	}//verifyReachedGoal
	
	public void nodeToString() {
		if (parent != null) {
			System.out.printf("Parent: ");
			System.out.println(parent.state);
		}
		System.out.println("This: "+ state);
		System.out.println("Position: "+ row + " " + column + "   Hill Climbed: "+hillClimbed);
		for (int i = 0; i < Main.landscapeRow; i++) {
			for (int j = 0; j < Main.landscapeColumn; j++) {
				if (this.landscape[i][j]) {
					if (i == row && j == column) {
						System.out.printf("%-4s","[o]");
					} else {
						System.out.printf("%-4s","[ ]");	
					}//if{}
						
				} else {
					System.out.printf("%-4s","[x]");
				}//if{}else{}
			}
			System.out.println("");
		}
	}//nodeToString

}//Node
