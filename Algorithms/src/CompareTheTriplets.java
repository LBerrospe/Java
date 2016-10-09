/**
 * @author Lalo Berrospe
 * @class CompareTheTriplets
 * @description Alice and Bob created one problem for HackerRank. A reviewer rates 
 * 				the two challenges, awarding points on a scale from 1 to 100 for
 * 				three categories: problem clarity, originality and difficulty.
 * 				Given Alice's and Bob's scores, can you compare the two
 * 				challenges and print their respective comparison points?
 */
import java.util.Scanner;

public class CompareTheTriplets {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        final short TRIPLET = 3;
        int tripletA[] = new int [TRIPLET];
        int tripletB[] = new int [TRIPLET];
        int aliceScore=0;
        int bobScore=0;
        /* Scores earned by Alice */
        for (int i = 0; i < TRIPLET; i++) { 
            tripletA[i] = sc.nextInt();
        }//for
        /* Scores earned by Bob */
        for (int i = 0; i < TRIPLET; i++) { 
            tripletB[i] = sc.nextInt();
        }
        /* Comparison score */
        for (int i = 0; i < TRIPLET; i++) {
            if(tripletA[i] > tripletB[i]){
                aliceScore+=1;    //Alice receives 1 point
            } else if (tripletA[i] < tripletB[i]) {
                bobScore+=1;  //Bob receives 1 point
            } else {/*Nobody receive points*/}
        }//for
        System.out.println(aliceScore+ " " +bobScore);
    }//main
}//class