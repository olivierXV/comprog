package Stundentsmain;

public class Studentsinfo {

	String firstName,lastName,course;
	int year, section;
	float midtermGD,finalGD;
	
	Studentsinfo (String firstName, String lastName, String course, int section ,float midtermGD, float finalGD, int year) {
		
		this.firstName = firstName;
		this.lastName = lastName;
		this.course = course;
		this.section = section;
		this.midtermGD = midtermGD;
		this.finalGD = finalGD;
		this.year = year;
		
	}
	void introduceSelf (String firstName, String lastName, String course, int Section, int year) {
		System.out.println(firstName + "," + lastName + "," + course + "," + Section + "," + year );
	}
	void evaluateGrade (float midteramGD, float finalGD ) {
		double average = (midteramGD + finalGD)/2;
		if (average > 100) {
			System.out.println("YOUR AVERAGE IS A MISTAKE!!!!!!");
		}
		else if (average >= 98 ) {
			System.out.println("You Reach With Highest Honor");
		}
		else if (average >= 95) {
			System.out.println("You Reach With High Honor");
		}
		else if (average >= 90) {
			System.out.println("You Reach With  Honor");
		}
		else if (average >= 75) {
			System.out.println("You Passed");
		}
		else {
			System.out.println("YOU FAIL YOUR FAMILY!!!");
		}
	}



}

