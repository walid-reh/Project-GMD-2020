package connexionsQueries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class HPO_annotation_querier {
	
	Connection connex = null;
	Statement stm = null;
    
    public HPO_annotation_querier() {
   	 //connection to sqlite
    	try {
    		Class.forName("org.sqlite.JDBC");
    		connex =  DriverManager.getConnection("jdbc:sqlite:ressources/databases/hpo_annotations.sqlite");
    		connex.setAutoCommit(false);
    	}catch ( Exception e ) {
    	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
    	      System.exit(0);
    	   }
    	
    }

public ArrayList<String> findDiseaseHpo(String id,String column) throws SQLException{
	ArrayList<String> decipherDiseases = new ArrayList<String>();
	
	try{
		
		stm = connex.createStatement();
		
		ResultSet rs = stm.executeQuery("SELECT disease_label FROM phenotype_annotation WHERE "+column+" LIKE '%"+id+"%'"
																+ " AND disease_db LIKE '%DECIPHER%' ;" );
		
		while (rs.next()){
			
			String disease = rs.getString("disease_label");
			decipherDiseases.add(disease);
			
		}
		rs.close();
		stm.close();
		connex.close();

	}catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	  }
	
	if(!decipherDiseases.isEmpty()){
		DataBaseSources.addSourceDecipher("HPOAnnotation (Decipher)");
	}
	
	
	return decipherDiseases;
	
  }

}
