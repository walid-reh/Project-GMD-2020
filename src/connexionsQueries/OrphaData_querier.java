package connexionsQueries;

import java.io.*;
import java.net.URL;
import java.text.ParseException;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class OrphaData_querier {
	
	
	private String syno;
	private String symp;
	
	
	public OrphaData_querier() {	
		
	}
	public OrphaData_querier(String symptom) {	
		this.symp=symptom;
	}


	public OrphaData_querier(String symptom, String synonym) {	
		this.symp=symptom;
		this.syno= synonym;
		
	}
	
	
	public void setSymptom(String symptom) {
		this.symp = symptom;
	}
	
	public void setSynonym(String synonym) {
		this.syno = synonym;
	}
	
	public String getSynonym() {
		return syno;
	}
	
	public String getSymptom() {
		return symp;
	}
	
	
	public ArrayList<String> findDisease(String Symptom) throws IOException, ParseException, org.json.simple.parser.ParseException{
		
		ArrayList<String> res = new ArrayList<String>();
		
		try {
			String s ="ressources/databases/disease_clinical_sign.json";
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("ressources/databases/disease_clinical_sign.json")));
			
			JSONParser parser = new JSONParser();
			
			JSONObject obj = (JSONObject) parser.parse(br);
			JSONArray  rows= (JSONArray) obj.get("rows");
			
			for (int i=0; i<rows.size();i++){
				
				JSONObject object = (JSONObject) rows.get(i);
				String key =String.valueOf(object.get("key"));
				JSONObject val =(JSONObject) object.get("value");
				JSONObject dis = (JSONObject) val.get("disease");
				JSONObject clinic = (JSONObject) val.get("clinicalSign");
				JSONObject name = (JSONObject) dis.get("Name");
				//System.out.println(name);
				String text =(String) name.get("text");
				JSONObject cli_Name = (JSONObject) clinic.get("name");
				String disId = (String) dis.get("id");
				
				
				
				if (key.toUpperCase().contains(Symptom.toUpperCase())){
					if(!res.contains(text)){
						res.add(text);
					}
					
				}
			
			}
		
		}	
		catch (FileNotFoundException e){
			e.printStackTrace();
		}
		
		if(!res.isEmpty()){
			DataBaseSources.addSourceOrpha("OrphaData");
		}
		return res;
	}
	

}
