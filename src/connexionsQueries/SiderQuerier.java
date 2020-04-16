package connexionsQueries;


import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import indexations.*;
import app.*;

public class SiderQuerier {
	
	
	String url;
    String username;
    String password;
    
    public SiderQuerier() {
   	 url = "jdbc:mysql://neptune.telecomnancy.univ-lorraine.fr:3306/gmd";
   	 username = "gmd-read";
   	 password = "esial";
    }
public ArrayList<String> findMed(String desease,String table,String column){ 
    try (java.sql.Connection connection = DriverManager.getConnection(url, username, password)){
   	 Statement st = connection.createStatement();
   	 st.execute("SELECT stitch_compound_id FROM " + table + " WHERE " + column + " LIKE \"%" + desease + "%\"");
   	 ResultSet rs = st.getResultSet(); 
   	 
   	 ArrayList<String> CIDs = new ArrayList<>();
   	 while (rs.next()){
   		 CIDs.add(rs.getString(1));
   	 }
   	 ArrayList<String> distinct = new ArrayList<>();
   	 distinct = ConsoleDisplayResults.duplicate(CIDs);
   	 
   	 
   	 st.close();
   	 connection.close();
   	 
   			 return distinct;

    }
   	 catch (SQLException ex) {
   		 System.err.println("SQLException information");
   		 while(ex!=null) {
   			 System.err.println("Error msg:" + ex.getMessage());
   			 System.err.println("SQLSTATE:" + ex.getSQLState());
   			 System.err.print("Error code: " + ex.getErrorCode());
   			 ex.printStackTrace();
   			 ex = ex.getNextException();
   		 }
   	 }
    return null;
   		 
}

//we look for the two CID
public ArrayList<ArrayList<String>> findSideEffect(String sign,String table,String column){ 

    try (java.sql.Connection connection = DriverManager.getConnection(url, username, password)){

   	 Statement st = connection.createStatement();

   	 st.execute("SELECT stitch_compound_id1, stitch_compound_id2  FROM " + table + " WHERE " + column + " LIKE \"%" + sign + "%\"");
   	 ResultSet rs = st.getResultSet(); 
   	 ResultSetMetaData metadata = rs.getMetaData();
   	 int nbColumns = metadata.getColumnCount();
   	 


   	ArrayList<ArrayList<String>> CIDs = new ArrayList<ArrayList<String>>();  //containing the CIDs couples
   	ArrayList<ArrayList<String>> CIDsDistinct = new ArrayList<ArrayList<String>>(); 
   	ArrayList<String> CID;   //contains one couple of CIDs for each symptom

   	
   	 while (rs.next()){
   		 CID =  new ArrayList<String>();
   		 for(int i=1;i<nbColumns+1;i++){
   			
   			 CID.add(rs.getString(i));
   		
   		 }
   		 CIDs.add(CID);	
   		CIDsDistinct = removeduplicate(CIDs);
   		
   	
   	 }
   	 

   	 st.close();
   	 connection.close();

   	 
   	 		return CIDsDistinct;

    }
   	 catch (SQLException ex) {
     	

   		 System.err.println("SQLException");
   		 while(ex!=null) {
   			 System.err.println("Error msg:" + ex.getMessage());
   			 System.err.println("SQLSTATE:" + ex.getSQLState());
   			 System.err.print("Error code: " + ex.getErrorCode());
   			 ex.printStackTrace();
   			 ex = ex.getNextException();
   		 }
   	 }
    return null;
   		 
}
public static ArrayList<ArrayList<String>> removeduplicate( ArrayList<ArrayList<String>> list){
    
    
    ArrayList<ArrayList<String>> res = new ArrayList<ArrayList<String>>();
    for (ArrayList<String> a : list){
    	if (!res.contains(a)){
    		res.add(a);
    	}
    }
    return res;
}

	

}
