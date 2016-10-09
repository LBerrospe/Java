/**
 * @author Lalo Berrospe
 * @class AVeryBigSum
 * @description You are given an array of integers of size N. 
 * 				You need to print the sum of the elements in 
 * 				the array, keeping in mind that some of those 
 * 				integers may be quite large.
 */
import java.util.Scanner;

public class AVeryBigSum {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int arr[] = new int[n];
        long result = 0;
        for(int i=0; i < n; i++){
            arr[i] = sc.nextInt();
        }//for
        for(int i=0; i < n; i++){
            result+=arr[i];
        }//for
        System.out.println(result);
    }//main
}//class