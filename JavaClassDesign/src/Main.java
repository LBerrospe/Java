/**
 * @author Lalo Berrospe
 * @class Main
 * @description Calculates area and perimeter 
 * @date Thu 7 Jul 2016
 */
import java.util.Scanner;

public class Main {
	private static Scanner sc = null;

	public static void main(String[] args) {
		int optionUser = 0;
		double sideA = 0;
		double sideB = 0;
		double sideC = 0;
		double altitude = 0;
		final short CIRCLE=1,TRAPEZOID=2,SQUARE=3,RECTANGLE=4,TRIANGLE=5;
		
		sc  = new Scanner(System.in);
		Circle circle;
		Trapezoid trapezoid;
		Square square;
		Rectangle rectangle;
		Triangle triangle;
		System.out.println("Perimeter and area\n");
		System.out.println("[1] Circle\n" +
						  "[2] Trapezoid\n" +
						  "[3] Square\n" +
						  "[4] Rectangle\n" +
						  "[5] Triangle\n" +
						  "Choose an option: ");
		optionUser = sc .nextInt();
		switch (optionUser) {
		case CIRCLE:		//Circle
			System.out.println("Enter the radix of the circle");
			circle = new Circle(sc .nextDouble());
			circle.calculateArea();
			circle.calculatePerimeter();
			break;
			
		case TRAPEZOID:		//Trapezoid
			System.out.println("Enter the length of the base one");
			sideA = sc .nextDouble();
			System.out.println("Enter the length of the base two");
			sideB = sc .nextDouble();
			trapezoid = new Trapezoid(sideA, sideB);
			trapezoid.calculateArea();
			trapezoid.calculatePerimeter();
			System.out.println("\nEnther the altitude");
			altitude = sc .nextDouble();
			
			trapezoid = new Trapezoid(sideA, sideB, altitude);
			trapezoid.calculateArea();
			trapezoid.calculatePerimeter();
			break;
			
		case SQUARE:		//Square
			System.out.println("Enter the base of the square");
			square = new Square(sc .nextDouble());
			square.calculateArea();
			square.calculatePerimeter();
			break;
			
		case RECTANGLE:		//Rectangle
			System.out.println("Enter the width");
			sideA = sc .nextDouble();
			System.out.println("Enter the length of the rectangle");
			sideB = sc .nextDouble();
			rectangle = new Rectangle(sideA, sideB);
			rectangle.calculateArea();
			rectangle.calculatePerimeter();
			break;
			
		case TRIANGLE:		//Triangle
			System.out.println("Enter the length of the base");
			sideA = sc .nextDouble();
			System.out.println("Enter the length of the left side");
			sideB = sc .nextDouble();
			System.out.println("Enter the length of the right side");
			sideC = sc .nextDouble();
			if ((sideA == sideB) && (sideA == sideC)) { // length of all sides area equal (Equilateral triangle)
				System.out.println("This is an equilateral triangle");
				triangle = new Triangle(sideA);
				triangle.calculateArea();
				triangle.calculatePerimeter();
			} else if ((sideB == sideC)) { 				// length of two sides area equal (Isosceles triangle)
				System.out.println("This is an isosceles triangle");
				triangle = new Triangle(sideA, sideB);
				triangle.calculateArea();
				triangle.calculatePerimeter();
			} else {	// length of all sides are different (Scalene triangle)
				System.out.println("This is a scalene triangle");
				triangle = new Triangle(sideA, sideB, sideC);
				triangle.calculateArea();
				triangle.calculatePerimeter();
			}
			break;
			
		default :
			System.out.println("Enter a valid option");
			break;
		}//switch
		sc.close();
	}//main
}//class