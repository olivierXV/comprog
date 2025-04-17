package olicierrv;
import java.util.*;
public class StudentMain{
	
	public static void main(String args[]) {
		ao.q();
		Scanner ad = new Scanner(System.in);
		
		System.out.print("Enter your first name: ");
		String fname = ad.nextLine();
		System.out.print("Enter your last name: ");
		String lname = ad.nextLine();
		System.out.print("Enter your course: ");
		String course = ad.nextLine();
		System.out.print("Enter your section: ");
		String section = ad.nextLine();
		System.out.print("Enter your year: ");
		int year = ad.nextInt();
		ad.nextLine();
		System.out.print("Enter your midterm grade: ");
		double midterm = ad.nextFloat();
		ad.nextLine();
		System.out.print("Enter your final grade: ");
		double finals = ad.nextFloat();
		ad.nextLine();
		
		ao.clearConsole();
		StudentInfo s = new StudentInfo(fname, lname, course, section, year, midterm, finals);
		s.introduceSelf(fname, lname, course, section);
		s.evaluateGrade(midterm, finals);
		
		System.out.println();
		ao.a();
		
		ad.close();
	}
	
}