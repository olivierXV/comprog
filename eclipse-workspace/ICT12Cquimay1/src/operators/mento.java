package operators;

import java.util.Scanner;

public class mento{
public static void operators() {
	Scanner cs = new Scanner(System.in);
	int a,b;
	int name;
	String answer;
	do {
		System.out.println("ENTER A NUMBER 1-4");
		System.out.println("1. ADD");
		System.out.println("2 SUBTAR");
		System.out.println("3. MULTI");
		System.out.println("4. DIVIDE");
		System.out.print("ENTER A NUMBER: ");
		name = cs.nextInt();
		cs.nextLine();	
		System.out.print("ENTER THE FIRST NUMBER: ");
		a = cs.nextInt();
		cs.nextLine();
		System.out.print("ENTER THE SECOND NUMBER: ");
		b = cs.nextInt();
		cs.nextLine();
		switch (name) {
		case 1:
			System.out.println(mento.add(a,b));
			break;
		case 2:
			System.out.println(mento.subtar(a,b));
			break;
		case 3:
			System.out.println(mento.multi(a,b));
			break;
		case 4:
			System.out.println(mento.divide(a,b));
			break;
		
		default:
			System.out.println("ERROR");
			break;
		
	}
		System.out.print("Ganahan ba ka bomalik? (y/N): ");
		answer = cs.nextLine();
	} while(answer.equalsIgnoreCase("y")); 
		System.out.println("Thank You");
	

}

	



	public static int add (int a, int b) {
		int c = a+b;
		return c;
	}
	public static int subtar (int a, int b) {
		int c = a-b;
		return c;
	}
	public static int multi (int a, int b) {
		int c = a*b;
		return c;
	}
	public static int divide (int a, int b) {
		int c = a/b;
		return c;	
		}
}
