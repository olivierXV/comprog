package olicierrv;

public class encap {
    private int userID;
    private String uname;
    private String prenom;
    private String nomdf;

    encap(int userID, String uname, String prenom, String nomdf){
        this.userID = userID;
        this.uname = uname;
        this.prenom = prenom;
        this.nomdf = nomdf;
    }

    int getUID (){
        return userID;
    }

    String getUname (){
        return uname;
    }

    String getFname (){
        return prenom;
    }

    String getLname (){
        return nomdf;
    }
    
    void setName(String newName) {
    	this.prenom = newName;
    }
    
    void setLname(String newLname) {
    	this.nomdf = newLname;
    }
    
    

}
