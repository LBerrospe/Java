/**
 * @author Lalo Berrospe
 * @class DiagonalDifference
 * @description Given a square matrix of size NxN, calculate the absolute
 * 			 	difference between the sums of its diagonals.
 */
import java.util.Scanner;

public class DiagonalDifference {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n=sc.nextInt();
		int matrix[][] = new int[n][n];
		int primaryDiagonal=0;
		int secondaryDiagonal=0;
		int auxSD = n-1; // auxiliar to get secondaryDiagonal;
		int result=0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				matrix[i][j] = sc.nextInt();
			}//for
		}//for
		for (int i = 0; i < n; i++) {
			primaryDiagonal += matrix[i][i];
		}//for
		for (int i = 0 ; i < n; i++) {
			secondaryDiagonal += matrix[i][auxSD];
			auxSD--;
		}//for
		result = Math.abs(primaryDiagonal - secondaryDiagonal);
		System.out.println(result);
	}//main
}//class
