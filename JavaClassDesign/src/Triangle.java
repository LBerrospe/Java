/**
 * @author Lalo Berrospe
 * @class Triangle
 * @description Calculates area and perimeter 
 * @date Thu 7 Jul 2016
 */
public class Triangle {
	private double sideA = 0;
	private double sideB = 0;
	private double sideC = 0;
	private double altitude = 0;
	private final double CONSTANT_FORMULA_EQUILATERAL = Math.sqrt(3) / 4;
	
	public Triangle(double sideA) {
		this.sideA = sideA;
		this.sideB = sideA;
		this.sideC = sideA;
	}//Constructor 1 arg
	
	public Triangle(double sideA, double sideB) {
		this.sideA = sideA;
		this.sideB = sideB;
		this.sideC = sideB;
	}//Constructor 2 arg

	public Triangle(double sideA, double sideB, double sideC) {
		this.sideA = sideA;
		this.sideB = sideB;
		this.sideC = sideC;
	}//Constructor 3 arg

	public double getSideA() {
		return sideA;
	}//getSideA
	
	public void setSideA(double sideA) {
		this.sideA = sideA;
	}//setSideA

	public double getSideB() {
		return sideB;
	}//getSideB

	public void setSideB(double sideB) {
		this.sideB = sideB;
	}//setSideB

	public double getSideC() {
		return sideC;
	}//getSideC

	public void setSideC(double sideC) {
		this.sideC = sideC;
	}//setSideC

	public void calculateArea() {
		double area = 0;
		if ((sideA == sideB) && (sideA == sideC)) { // length of all sides area equal (Equilateral triangle)
			area = CONSTANT_FORMULA_EQUILATERAL * (sideA * sideA);
		} else if ((sideB == sideC)) { 				// length of two sides area equal (Isosceles triangle)
			altitude = Math.sqrt( (sideB * sideB) - ((sideA * sideA) / 4));
			area = sideA * altitude * 1/2;
		} else {	// length of all sides are different (Scalene triangle)
			double semiperimeter = 0;
			semiperimeter = (sideA + sideB + sideC) / 2;
			area = Math.sqrt((semiperimeter) * (semiperimeter - sideA)
							  * (semiperimeter - sideB) * (semiperimeter - sideC));
		}
		if (area == 0) {
			System.out.println("Error: it is not possible to calculate with these values\n" +
					           "Rule: One side must be less than the sum of the lengths of " +
					           "the other two sides and greater than the difference of the\n" +
					           "lengths of the other sids");
		} else {
			System.out.println("Area: " + area);
		}
	}//calculateArea

	public void calculatePerimeter() {
		double perimeter;
		perimeter = sideA + sideB + sideC;; 
		System.out.println("Perimeter: " + perimeter);
	}//calculatePerimeter
}//class Triangle