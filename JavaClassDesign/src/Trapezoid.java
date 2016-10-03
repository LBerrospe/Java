/**
 * @author Lalo Berrospe
 * @class Trapezoid
 * @description Calculates area and perimeter 
 * @date Thu 7 Jul 2016
 */
public class Trapezoid {
	private double sideA = 0;
	private double sideB = 0;
	private double sideC = 0;
	private double altitude = 0;

	public Trapezoid(double sideA, double sideB) {
		this.sideA = sideA;
		this.sideB = sideB;
	}// Constructor 2 args
	
	public Trapezoid(double sideA, double sideB, double altitude) {
		this.sideA = sideA;
		this.sideB = sideB;
		this.altitude = altitude;
	}//Constructor 3 args
	
	public double getSideA() {
		return sideA;
	}

	public void setSideA(double sideA) {
		this.sideA = sideA;
	}

	public double getSideB() {
		return sideB;
	}

	public void setSideB(double sideB) {
		this.sideB = sideB;
	}
	
	public double getSideC() {
		return sideC;
	}
	
	public void setSideC(double sideC) {
		this.sideC = sideC;
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	public void calculateArea() {
		double area = 0;
		if (altitude == 0) {	//if the user doesn't define the altitude
			altitude = (sideA + sideB) / 2;
			System.out.println("\nTrapezoid with perpendicular diagonals");
		} else {				//if the user define the altitude
			System.out.println("\nTrapezoid without perpendicular diagonals");
		}
		area = ( (sideA + sideB) / 2 ) * altitude ;
		System.out.println("Trapezoid area: " + area);
	}//calculateArea

	public void calculatePerimeter() {
		double perimeter = 0;
		double auxiliar = (sideA - sideB) / 2 ;	//auxiliar to get the length of the base of the triangles of its ends
		sideC = Math.sqrt((auxiliar * auxiliar) + (altitude * altitude)); //pythagorean theorem
		perimeter = sideA + sideB + (sideC * 2);
		System.out.println("Trapezoid perimeter: " + perimeter);
	}//calculatePerimeter

}//class Trapezoid