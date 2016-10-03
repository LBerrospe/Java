/**
 * @author Lalo Berrospe
 * @class Circle
 * @description Calculates area and perimeter 
 * @date Thu 7 Jul 2016
 */
public class Circle {
	private double radix = 0;			//radix circle
	private final double PI = 3.1416;	//constant PI
	
	public Circle(double radix) {
		this.radix = radix;
	}//CONSTRUCTOR
	
	public double getRadix() {
		return this.radix;
	}//getRadix
	
	public void setRadix(double radix) {
		this.radix = radix;
	}//setRadix
	
	public void calculateArea() {
		double area = 0;
		area = PI * (radix * radix);
		System.out.println("Circle area: " + area);
	}//calculateArea
	
	public void calculatePerimeter() {
		double perimeter = 0;
		perimeter = 2 * PI * radix;
		System.out.println("Circle perimeter: " + perimeter);
	}//calculatePerimeter
}//class Circle