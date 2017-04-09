/**
 * @author Lalo Berrospe
 * @class Staircase
 * @description Observe that its base and height are both equal to , 
 * 				and the image is drawn using # symbols and spaces. 
 * 				The last line is not preceded by any spaces.
 * 				Write a program that prints a staircase of size .
 */
import java.util.Scanner;

public class Staircase {

	public static void main(String[] args) {
		Scanner sc=new Scanner(System.in);
		int n = sc.nextInt();	//size of the staircase
		sc.close();
		for (int i = 1; i <= n; i++) {
			for (int space = n-i; space > 0; space--) {
				System.out.printf(" ");
			}//for
			for (int symbol = 1; symbol <= i; symbol++) {
				System.out.printf("#");
			}//for
			System.out.printf("\n");
		}//for
	}//main
}//class