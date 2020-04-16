package connexionsQueries;

import java.util.ArrayList;

public class DataBaseSources {
	
	private static ArrayList<String> dataSourceCIDs = new ArrayList<String>();
	private static ArrayList<String> dataSourceOmimOnto = new ArrayList<String>();
	private static ArrayList<String> dataSourceFindMed = new ArrayList<String>();
	private static ArrayList<String> dataSourceDrugBank = new ArrayList<String>();
	private static ArrayList<String> dataSourceDrugBankCause = new ArrayList<String>();

	private static ArrayList<String> dataSourceOrpha = new ArrayList<String>();
	private static ArrayList<String> dataSourceOmim = new ArrayList<String>();
	private static ArrayList<String> dataSourceDecipher = new ArrayList<String>();


	




	
	public DataBaseSources(){
	}
	
	public static ArrayList<String> getDataSourceCIDs(){
		return dataSourceCIDs;
	}
	public static ArrayList<String> getDataSourceOmimOnto(){
		return dataSourceOmimOnto;
	}
	public static ArrayList<String> getDataSourceFindMed(){
		return dataSourceFindMed;
	}
	public static ArrayList<String> getDataSourceDrugBank(){
		return dataSourceDrugBank;
	}
	public static ArrayList<String> getDataSourceDrugBankCause(){
		return dataSourceDrugBankCause;
	}
	public static ArrayList<String> getDataSourceOrpha(){
		return dataSourceOrpha;
	}
	public static ArrayList<String> getDataSourceOmim(){
		return dataSourceOmim;
	}
	public static ArrayList<String> getDataSourceDecipher(){
		return dataSourceDecipher;
	}
	
	
	
	public static void addSourceCIDs(String source){
		dataSourceCIDs.add(source);
	}
	public static void addSourceOmimOnto(String source){
		dataSourceOmimOnto.add(source);
	}
	public static void addSourceFindMed(String source){
		dataSourceFindMed.add(source);
	}
	public static void addSourceDrugBank(String source){
		dataSourceDrugBank.add(source);
	}
	public static void addSourceDrugBankCause(String source){
		dataSourceDrugBankCause.add(source);
	}
	public static void addSourceOrpha(String source){
		dataSourceOrpha.add(source);
	}
	public static void addSourceOmim(String source){
		dataSourceOmim.add(source);
	}
	public static void addSourceDecipher(String source){
		dataSourceDecipher.add(source);
	}
	
	
	
	
	
	public static void clearCIDs(){
		dataSourceCIDs.clear();
	}
	public static void clearOmimOnto(){
		dataSourceOmimOnto.clear();
	}
	public static void clearFindMed(){
		dataSourceFindMed.clear();
	}
	public static void clearDrugBank(){
		dataSourceDrugBank.clear();
	}
	public static void clearDrugBankCause(){
		dataSourceDrugBankCause.clear();
	}
	public static void clearOrpha(){
		dataSourceOrpha.clear();
	}
	public static void clearOmim(){
		dataSourceOmim.clear();
	}
	public static void clearDecipher(){
		dataSourceDecipher.clear();
	}


}
