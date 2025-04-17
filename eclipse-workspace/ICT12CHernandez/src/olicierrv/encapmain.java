package olicierrv;
import java.util.*;
public class encapmain{

    public static void main(String[] args){
    	ao.a();
        Scanner ad = new Scanner(System.in);
        
        System.out.print("Enter your User ID: ");
        int uid = ad.nextInt();
        ad.nextLine();
        System.out.print("Enter your Username: ");
        String uname = ad.nextLine();
        System.out.print("Enter your First Name: ");
        String fname = ad.nextLine();
        System.out.print("Enter your Last Name: ");
        String lname = ad.nextLine();
        
        encap e = new encap(uid, uname, fname, lname);
        
        System.out.println(e.getUID());
        ad.close();
    }

}
