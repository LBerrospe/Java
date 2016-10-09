/**
 * @author Lalo Berrospe
 * @class Power
 * @description Calculates the power of any number,	it will prompt 
 * 		the user base and power ( both integer ).
 * @date Thu 7 Jul 2016
 */
import java.util.Scanner;

public class Power {
	public static void main(String[] args) {
		int base = 0;			
		int exponent = 0;		
		double resPow = 1;			//Result from number raised to power
		Scanner sc = null;
		sc  = new Scanner(System.in);
		System.out.println("Enter the base number");
		base = sc .nextInt();		//Read user's answer
		System.out.println("Enter the exponent");
		exponent = sc .nextInt();	//Read user's answer
		sc.close();	//Close Scanner
		/*
		 * 	if the exponent is positive multiply the base number 'n' times
		 * 		else the exponent is negative invert the base dividing 1 by the result of multiply the base number 'n' times
		 */
		if (exponent > 0) {
			for(int i = 0; i < exponent; i++) {
				resPow *= base;
			}//for	
		} else {
			for(int i = 0; i > exponent; i--) {	
				resPow *= base;
			}//for
			resPow = 1 / resPow; 	//divide 1 by the result
		}//else
		System.out.println("Result is: " +resPow);
	}//main
}//class Power
