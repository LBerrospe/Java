/**
 * @Alumno Hector Eduardo Berrospe Barajas
 * @Codigo 210168457
 * @Seccion 
 * @Tarea #03
 */

import java.util.Scanner;

public class Encapsulation {
	
	static Scanner sc = new Scanner(System.in);
	
	public void menu(MyDate myDate) {
		int option, value;
		
		while(true) {
			System.out.printf("%-14s%d/%d/%d\n","Current date: ",myDate.getDay(),myDate.getMonth(),myDate.getYear() );
			System.out.printf("%-4s%-20s\n","[1]","Change day");
			System.out.printf("%-4s%-20s\n","[2]","Change month");
			System.out.printf("%-4s%-20s\n","[3]","Change year");
			System.out.printf("%-4s%-20s\n","[4]","Exit");
			System.out.printf("Option: ");
			option = sc.nextInt();
			
			switch(option) {
			case 1:
				System.out.printf("%-5s","Day:");
				value = sc.nextInt();
				
				if(!(myDate.setDay(value)))
					System.out.println("ERROR: invalid date");
			
				break;
				
			case 2:
				System.out.printf("%-5s","Month:");
				value = sc.nextInt();
				
				if(!(myDate.setMonth(value)))
					System.out.println("ERROR: invalid date");
			
				break;
			
			case 3:
				System.out.printf("%-5s","Year:");
				value = sc.nextInt();
				
				if(!(myDate.setYear(value)))
					System.out.println("ERROR: invalid date");
			
				break;
			
			case 4:
				System.exit(0);
				break;
				
			default: 
					System.out.println("ERROR");
				break;
			}//switch option
			System.out.printf("\n\n");
		}//while(true)
	}//menu
}//class Encapsulation

