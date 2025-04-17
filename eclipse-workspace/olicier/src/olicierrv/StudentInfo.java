package olicierrv;

public class StudentInfo {
	
	String firstName, lastName, course, section;
	int year;
	double midtermGrade, finalGrade;
	
	StudentInfo(String firstName, String lastName, String course, 
			String section, int year, double midtermGrade, double finalGrade) {
		
		this.firstName = firstName;
		this.lastName = lastName;
		this.course = course;
		this.section = section;
		this.year = year;
		this.midtermGrade = midtermGrade;
		this.finalGrade = finalGrade;
		
	}
	
	void introduceSelf(String firstName, String lastName, String course, 
			String section){
		System.out.println("Hello "+ao.capitalize(lastName) + ", " + ao.capitalize(firstName) + ", " + course.toUpperCase() + " " + section.toUpperCase() + " " + year);
	}
	
	void evaluateGrade(double midtermGrade, double finalGrade) {
		double average = (midtermGrade + finalGrade)/2;
		String idk;
		if (average > 100) {
			idk = "Invalid grade (higher than 100)";
		} else if (average >= 98) {
			idk = ", you Passed with Highest Honors";
		} else if (average >= 95) {
			idk = ", you Passed with High Honors";
		} else if (average >= 90) {
			idk = ", you Passed with Honors";
		} else if (average >= 75) {
			idk = ", you Passed";
		} else {
			idk = ", you Failed";
		}
		System.out.println(average+idk);
	}
	
}