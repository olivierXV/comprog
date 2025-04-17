package main_2;
import java.util.Scanner;
public class constant {
	

	  public static void gradeclalu(Scanner cs) {
	        float gr1, gr2, gr3, gr4, total1, total;
	        String name, choice, menu, answer;
	        do {
	        System.out.println("Grade Calculator"); 
	        System.out.print("Enter your name: ");
	        name = cs.nextLine();
	        System.out.print("Enter your grade on english: ");
	        gr1 = cs.nextFloat();
	        cs.nextLine();
	        System.out.print("Enter your grade on filipino: ");
	        gr2 = cs.nextFloat();
	        cs.nextLine();
	        System.out.print("Enter your grade on mathematics: ");
	        gr3 = cs.nextFloat();
	        cs.nextLine();
	        System.out.print("Enter your grade on philosophy: ");
	        gr4 = cs.nextFloat();
	        cs.nextLine();
	        
	        total = gr1 + gr2 + gr3 + gr4;
	        total1 = total / 4;
	        
	        System.out.println();
	        System.out.println(name);
	        System.out.printf("Your grade average is: %.2f\n", total1);
	        
	        if (total1 > 100) {
	            System.out.println("Invalid grade");
	        } else if (total1 >= 98) {
	            System.out.println("Passed with Highest Honors");
	        } else if (total1 >= 95) {
	            System.out.println("Passed with High Honors");
	        } else if (total1 >= 90) {
	            System.out.println("Passed with Honors");
	        } else if (total1 >= 75) {
	            System.out.println("Passed");
	        } else 
	        	System.out.println("ERROR NUMBERs");
	        System.out.print("Ganahan ba ka bomalik? (y/N): ");
			answer = cs.nextLine();
	         } while(answer.equalsIgnoreCase("y")); 
			System.out.println("Thank You");
	  }
}

	       
	  
                                                                                   