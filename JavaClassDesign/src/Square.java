/**
 * @author Lalo Berrospe
 * @class Square
 * @description Calculates area and perimeter 
 * @date Thu 7 Jul 2016
 */
public class Square {
	private double base = 0;	//Square base
	private final short NUMBER_SIDES = 4;
	
	public Square(double base) {
	this.base = base;	
	}//Square
	
	public double getBase() {
		return this.base;
	}//getBase
	
	public void setBase(double base) {
		this.base = base;
	}//setBase

	public void calculateArea() {
		double area = 0;
		area = base * base;
		System.out.println("Square area: " + area);
	}//calculateArea

	public void calculatePerimeter() {
		double perimeter = 0;
		perimeter = NUMBER_SIDES * base;
		System.out.println("Square perimeter: " + perimeter);
	}//calculatePerimeter
}//class Square