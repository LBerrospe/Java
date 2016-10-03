/**
 * @author Lalo Berrospe
 * @class Rectangle
 * @description Calculates area and perimeter 
 * @date Thu 7 Jul 2016
 */
public class Rectangle {
	private double width = 0;
	private double length = 0;
	private final short NUMBER_SIDES = 2;
	
	public Rectangle(double width, double length) {
		this.width = width;
		this.length = length;
	}//Constructor
	
	public double getWidth() {
		return this.width;
	}//getWidth
	
	public void setWidth(double width) {
		this.width = width;
	}//setWidth
	
	public double getLength() {
		return this.length;
	}//getLength

	public void setLength(double length) {
		this.length = length;
	}//setLength	

	public void calculateArea() {
		double area = 0;
		area = width * length;
		System.out.println("Rectangle area: " + area);
	}//calculateARea

	public void calculatePerimeter() {
		double perimeter = 0;
		perimeter = NUMBER_SIDES * (width + length);
		System.out.println("Rectangle perimeter: " + perimeter);
	}//calculatePerimeter
}