package Stundentsmain;
import java.util.*;
public class mainStudents {

	public static void main(String[] args) {
		Scanner cs = new Scanner (System.in);
		
		System.out.println("Enter your First name");
		String firstname = cs.nextLine();
		System.out.println("Enter your last name");
		String LTname = cs.nextLine();
		System.out.println("Enter your course");
		String course = cs.nextLine();
		System.out.println("Enter your section");
		String section = cs.nextLine();
		System.out.println("Enter your year");
		int year = cs.nextInt();
		System.out.println("Enter your midterm grade");
		int mtgrade = cs.nextInt();
		System.out.println("Enter your final grade");
		int flgrade = cs.nextInt();

		Studentsinfo es = new Studentsinfo( firstname, LTname, course, section, mtgrade, flgrade, year)
		

	}

}
