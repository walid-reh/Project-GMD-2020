package connexionsQueries;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.lucene.queryparser.classic.ParseException;

import indexations.*;
import app.*;


public class Request {
	
	public static ArrayList<String> requestParsing (String signs){
		
		ArrayList<String> fields = new ArrayList<String>();
		
		String and = " AND ";
		String or = " OR ";
		String left;
		String right;
		
		int i = 0;

		String sign = "";
	
			
		
					while (i != signs.length()) {
						
						while (((i+4) < signs.length()) && ((!(signs.substring(i,i+5)).equals(and)) ) && ((!(signs.substring(i,i+4)).equals(or)) )) {
						sign = sign+ signs.charAt(i);
						i++;
						
						}
						/*if((signs.substring(i, i+4)).equals(or) || (signs.substring(i, i+5)).equals(and)) {
							
						}*/
						if ((i+4) >= signs.length()) {
							sign = sign+ signs.substring(i,signs.length());
							
						}
						fields.add(sign);
						sign ="";
						i = i+4;
						
					}
					/*for(int i1=0; i1<fields.size();i1++) {
						System.out.println(fields.get(i1));
					}*/
					return fields;
					
		
	}
	
	public static ArrayList<String> intersectionResults (String signs, ArrayList<String> side1, ArrayList<String> side2) {
		
		ArrayList<String> union = new ArrayList<String>();
		ArrayList<String> intersection = new ArrayList<String>();
		union = side1;
		String and = " AND ";
		String or = " OR ";
		
	
		if (signs.indexOf(and) != -1) {
					
			for (String a : side1) {
				for (String b : side2) {
					if (a.equals(b)) {
						intersection.add(b);
		
								
					}
				}
			}
				
					
		return intersection;	
						
		}
					
		
		if (signs.indexOf(or) != -1) {
			for (String a : side2) {
				if (!union.contains(a)) {
					
					union.add(a);
				}
			}
					
					
		return union;
		}

return null;
}

public static ArrayList<String> unionResults (ArrayList<String> side1, ArrayList<String> side2) {
		
		ArrayList<String> union = new ArrayList<String>();
		union = side1;
		
		
			for (String a : side2) {
				if (!union.contains(a)) {
					
					union.add(a);
				}
			}
					
					
		return union;
		

}

	
	public static void intersection(String sign) throws org.json.simple.parser.ParseException, Exception{
		ArrayList<String> signs = new ArrayList<String>();
		signs = requestParsing(sign);

		
		
		System.out.println("Request intersection result is");
		//System.out.println(signs.get(0));
		/*for(int i1=0; i1<signs.size();i1++) {
		System.out.println(signs.get(i1));
	   }*/
		ArrayList<String> medscause1 = ConsoleDisplayResults.sideEffectCause(signs.get(0));
		
		ArrayList<String> medscause2 = new ArrayList<String>();
		
		//this array is for the results for AND or OR
		ArrayList<String> results = new ArrayList<String>();
		
		for(int j=1;j<signs.size();j++){
			results.clear();
			medscause2 = ConsoleDisplayResults.sideEffectCause(signs.get(j));
			
			//let's call intersection function
			results = intersectionResults(sign, medscause1, medscause2);
			medscause1.clear();
			for (int k=0; k < results.size();k++) {
				medscause1.add(results.get(k));

			}
		}
		

		// It's now time to print the results
		
	  	if (!results.isEmpty()){
	  		System.out.println("These side effects are caused by the intersection of these drugs:   ");
		  	System.out.println();
		  	
			for(int i=0;i<results.size();i++){
				System.out.println(results.get(i));
				
			}
			// data source:
			System.out.println();
			System.out.println("*****************Source*****************");
			System.out.println("Data coming from the sources:");
			System.out.println();

			if(!DataBaseSources.getDataSourceCIDs().isEmpty()){
				for(String s : DataBaseSources.getDataSourceCIDs()){
					System.out.print(" -> " + s);

				}
			}
			
	  		System.out.println();

	  		if(!DataBaseSources.getDataSourceOmimOnto().isEmpty()){
				for(String s : DataBaseSources.getDataSourceOmimOnto()){
					System.out.print(" -> " + s);

				}
			}

	  		System.out.println();

	  		if(!DataBaseSources.getDataSourceDrugBankCause().isEmpty()){
				for(String s : DataBaseSources.getDataSourceDrugBankCause()){
					System.out.print(" -> " + s);

				}
			}

	  		
	  		DataBaseSources.clearCIDs();
	  		DataBaseSources.clearOmimOnto();
	  		DataBaseSources.clearDrugBankCause();

			
	  		System.out.println();
	  		
		
	}
	  	

	  	//we search for drugs that cure the se
	  	
	  	ArrayList<String> medsheal = new ArrayList<String>();
	  	ArrayList<String> medsHealDistinct = new ArrayList<String>();

	  	//we find drug for every sign 
	  	for(int j=0;j<signs.size();j++){
  			medsheal.addAll(ConsoleDisplayResults.findMedication(signs.get(j)));
  		}
  		
	  	//now let's get the list of drug that causes the side effect.
		medsHealDistinct = ConsoleDisplayResults.duplicate(medsheal);
	  	
  		if(!medsHealDistinct.isEmpty()){
  			System.out.println("--------------------------------------------------------------");
	  		System.out.println("The drugs that treat all side effects are:   ");
	  		System.out.println();

	  		for(int i=0;i<medsHealDistinct.size();i++){
				System.out.println(medsHealDistinct.get(i));
				
			}
	  		
	  		System.out.println("*****************Source*****************");System.out.println();
	  		System.out.println("Data coming from the sources:");
	  		System.out.println();
	  		
	  		if(!DataBaseSources.getDataSourceFindMed().isEmpty()){
	  			for(String s : DataBaseSources.getDataSourceFindMed()){
					System.out.print(" -> " + s);

				}
	  		}
	  		System.out.println();
	  		if(!DataBaseSources.getDataSourceDrugBank().isEmpty()){
	  			for(String s : DataBaseSources.getDataSourceDrugBank()){
					System.out.print(" -> " + s);

				}
	  		}
	  		
	  		DataBaseSources.clearFindMed();
			DataBaseSources.clearDrugBank();
			System.out.println();
  			
  		}
		
	
  		
  		//let's find the diseases that cause the signs/symptoms
  		
  		ArrayList<String> diseasecause1 = ConsoleDisplayResults.findAllDisease(signs.get(0));
		
		ArrayList<String> diseasecause2 = new ArrayList<String>();
		
		//as you know this array will contain intersection or union of diseases
		ArrayList<String> diseaseResults = new ArrayList<String>();
		
		for(int j=1;j<signs.size();j++){
			diseaseResults.clear();
			diseasecause2 = ConsoleDisplayResults.findAllDisease(signs.get(j));
			diseaseResults = intersectionResults(sign, diseasecause1, diseasecause2);
			diseasecause1.clear();
			for (int k=0; k < diseaseResults.size();k++) {
				diseasecause1.add(diseaseResults.get(k));

			}
		}
		//Now it's time to print the result
		
		   if(!diseaseResults.isEmpty()){
			  
				System.out.println("--------------------------------------------------------------");
	
			  	System.out.println("This signs are the manifestation of these diseases (intersection):   ");
				System.out.println();
				
			  		for(int i=0;i<diseaseResults.size();i++){
			  			System.out.println(diseaseResults.get(i));
					
			  		}
					System.out.println();

					
					if(!DataBaseSources.getDataSourceOrpha().isEmpty() || !DataBaseSources.getDataSourceOmim().isEmpty() || !DataBaseSources.getDataSourceDecipher().isEmpty()){
						System.out.println("*****************Source*****************");
						System.out.println("Data coming from the sources:");
						System.out.println();


						for(String s : DataBaseSources.getDataSourceOrpha()){
							System.out.print(" -> " + s);

						}
						System.out.println();


						for(String s : DataBaseSources.getDataSourceOmim()){
							System.out.print(" -> " + s);

						}
						
						System.out.println();

						for(String s : DataBaseSources.getDataSourceDecipher()){
							System.out.print(" -> " + s);

						}
						
						
					}
			  		
					DataBaseSources.clearOrpha();
					DataBaseSources.clearFindMed();
					DataBaseSources.clearDrugBank();
					DataBaseSources.clearOmim();
					DataBaseSources.clearDecipher();

					
			  		
				System.out.println();
				System.out.println("--------------------------------------------------------------");
				
				
				
				//sometimes it can be a great idea to find drugs that cure the previous diseases... so let's do it ! 

				ArrayList<String> medshealDisease = new ArrayList<String>();
				
			  	for(int i=0;i<diseaseResults.size();i++){
			  		medshealDisease.addAll(ConsoleDisplayResults.findMedication(diseaseResults.get(i))); 
			  		
			  	}	
			  
			  	
			  		
			  	ArrayList<String> medshealdistinct2 = ConsoleDisplayResults.duplicate(medshealDisease);	
			  	
			  	
			  	if(!medshealdistinct2.isEmpty()){
			  		System.out.println("The drugs that treat the diseases are (intersection):   ");
			  		
				  	System.out.println();
				  	for(int i=0;i<medshealdistinct2.size();i++){
			  			System.out.println(medshealdistinct2.get(i));
					
			  		}
				  	
				  	System.out.println();
				  	System.out.println("*****************Source*****************");
					System.out.println("Data coming from the sources:");
			  		System.out.println();
			  		
			  		if(!DataBaseSources.getDataSourceFindMed().isEmpty()){
			  			for(String s : DataBaseSources.getDataSourceFindMed()){
							System.out.print(" -> " + s);

						}
			  		}
			  		System.out.println();
			  		if(!DataBaseSources.getDataSourceDrugBank().isEmpty()){
			  			for(String s : DataBaseSources.getDataSourceDrugBank()){
							System.out.print(" -> " + s);

						}
			  		}
				  	
			  	}
				  
			DataBaseSources.clearFindMed();
			DataBaseSources.clearDrugBank();
			
		   }
		  
	}
	
	
	
	public static void union(String sign) throws org.json.simple.parser.ParseException, Exception{
		ArrayList<String> signs = new ArrayList<String>();
		signs = requestParsing(sign);

		
		
		System.out.println("Request union result is");
		ArrayList<String> results = ConsoleDisplayResults.sideEffectCause(signs.get(0));
		
		ArrayList<String> medscause2 = new ArrayList<String>();
		
		for(int j=1;j<signs.size();j++){
			medscause2 = ConsoleDisplayResults.sideEffectCause(signs.get(j));
			results = intersectionResults(sign, results, medscause2); //here is the intersection
			
		}
		

		// the print of results
		
		
	  	if (!results.isEmpty()){
	  		System.out.println("These side effects are caused by the union of these drugs:   ");
		  	System.out.println();
		  	
			for(int i=0;i<results.size();i++){
				System.out.println(results.get(i));
				
			}
			// let's print data sources
			System.out.println();
			System.out.println("*****************Source*****************");
			System.out.println("Data coming from the sources:");
			System.out.println();

			if(!DataBaseSources.getDataSourceCIDs().isEmpty()){
				for(String s : DataBaseSources.getDataSourceCIDs()){
					System.out.print(" -> " + s);

				}
			}
			
	  		System.out.println();

	  		if(!DataBaseSources.getDataSourceOmimOnto().isEmpty()){
				for(String s : DataBaseSources.getDataSourceOmimOnto()){
					System.out.print(" -> " + s);

				}
			}

	  		System.out.println();

	  		if(!DataBaseSources.getDataSourceDrugBankCause().isEmpty()){
				for(String s : DataBaseSources.getDataSourceDrugBankCause()){
					System.out.print(" -> " + s);

				}
			}

	  		
	  		DataBaseSources.clearCIDs();
	  		DataBaseSources.clearOmimOnto();
	  		DataBaseSources.clearDrugBankCause();

			
	  		System.out.println();
	  		
		
	}
	  	
	 	  	
	  	//now let's search for drug that treat the se
	  	
	  	ArrayList<String> medsheal = new ArrayList<String>();
	  	ArrayList<String> medsHealDistinct = new ArrayList<String>();

	  	//as you know we find drug for every sign
	  	for(int j=0;j<signs.size();j++){
  			medsheal.addAll(ConsoleDisplayResults.findMedication(signs.get(j)));
  		}
  		
	  	//and finally the drugs' list that 
		medsHealDistinct = ConsoleDisplayResults.duplicate(medsheal); //Meds list containing meds that causes the side effect
	  	
  		if(!medsHealDistinct.isEmpty()){
  			System.out.println("--------------------------------------------------------------");
	  		System.out.println("The drugs that treat all side effects are (Union):   ");
	  		System.out.println();

	  		for(int i=0;i<medsHealDistinct.size();i++){
				System.out.println(medsHealDistinct.get(i));
				
			}
	  		System.out.println();
	  		System.out.println("*****************Source*****************");
	  		System.out.println("Data coming from the sources:");
	  		System.out.println();
	  		
	  		if(!DataBaseSources.getDataSourceFindMed().isEmpty()){
	  			for(String s : DataBaseSources.getDataSourceFindMed()){
					System.out.print(" -> " + s);

				}
	  		}
	  		System.out.println();
	  		if(!DataBaseSources.getDataSourceDrugBank().isEmpty()){
	  			for(String s : DataBaseSources.getDataSourceDrugBank()){
					System.out.print(" -> " + s);

				}
	  		}
	  		
	  		DataBaseSources.clearFindMed();
			DataBaseSources.clearDrugBank();
			System.out.println();
  			
  		}
		
	
  		// diseases that cause the signs
  		
  		ArrayList<String> diseaseResults = ConsoleDisplayResults.findAllDisease(signs.get(0));
		
		ArrayList<String> diseasecause2 = new ArrayList<String>();
		
		for(int j=1;j<signs.size();j++){
			diseasecause2 = ConsoleDisplayResults.findAllDisease(signs.get(j));
			diseaseResults = intersectionResults(sign, diseaseResults, diseasecause2);
			
		}
		//let's print diseases
		
		   if(!diseaseResults.isEmpty()){
			  	
				System.out.println("--------------------------------------------------------------");
	
			  	System.out.println("This signs are the manifestation of these diseases (union):   ");
				System.out.println();
				
			  		for(int i=0;i<diseaseResults.size();i++){
			  			System.out.println(diseaseResults.get(i));
					
			  		}
					System.out.println();

					if(!DataBaseSources.getDataSourceOrpha().isEmpty() || !DataBaseSources.getDataSourceOmim().isEmpty() || !DataBaseSources.getDataSourceDecipher().isEmpty()){
						System.out.println("Data coming from the sources:");//to complete
						System.out.println();

						for(String s : DataBaseSources.getDataSourceOrpha()){
							System.out.print(" -> " + s);

						}
						System.out.println();

						for(String s : DataBaseSources.getDataSourceOmim()){
							System.out.print(" -> " + s);

						}
						
						System.out.println();

						for(String s : DataBaseSources.getDataSourceDecipher()){
							System.out.print(" -> " + s);

						}
					
					}
			  		
					DataBaseSources.clearOrpha();
					DataBaseSources.clearFindMed();
					DataBaseSources.clearDrugBank();
					DataBaseSources.clearOmim();
					DataBaseSources.clearDecipher();

				System.out.println();
				System.out.println("--------------------------------------------------------------");
				
				
				   // now the drugs that treat the diseases:

				ArrayList<String> medshealDisease = new ArrayList<String>();
				
			  	for(int i=0;i<diseaseResults.size();i++){
			  		medshealDisease.addAll(ConsoleDisplayResults.findMedication(diseaseResults.get(i))); 
			  		
			  	}	
			  
			  	
			  		
			  	ArrayList<String> medshealdistinct2 = ConsoleDisplayResults.duplicate(medshealDisease);	
			  	
			  	
			  	if(!medshealdistinct2.isEmpty()){
			  		System.out.println("The drugs that treat the diseases are (union):   ");
			  		
			  		
			  	
			  		
			  		
				  	System.out.println();
				  	for(int i=0;i<medshealdistinct2.size();i++){
			  			System.out.println(medshealdistinct2.get(i));
					
			  		}
				  	
				  	System.out.println();

					
				  	System.out.println("*****************Source*****************");
					System.out.println("Data coming from the sources:");
			  		System.out.println();
			  		
			  		if(!DataBaseSources.getDataSourceFindMed().isEmpty()){
			  			for(String s : DataBaseSources.getDataSourceFindMed()){
							System.out.print(" -> " + s);

						}
			  		}
			  		System.out.println();
			  		if(!DataBaseSources.getDataSourceDrugBank().isEmpty()){
			  			for(String s : DataBaseSources.getDataSourceDrugBank()){
							System.out.print(" -> " + s);

						}
			  		}
				  	
			  	}
				  	
			DataBaseSources.clearFindMed();
			DataBaseSources.clearDrugBank();
			
		   }
		   
	}
	public static void synonyms(ArrayList<String> signs) throws org.json.simple.parser.ParseException, Exception{
		
		System.out.println("Request (by synonyms) result is");
		ArrayList<String> results = ConsoleDisplayResults.sideEffectCause(signs.get(0));
		
		ArrayList<String> medscause2 = new ArrayList<String>();
		
		for(int j=1;j<signs.size();j++){
			medscause2 = ConsoleDisplayResults.sideEffectCause(signs.get(j));
			results = unionResults(results, medscause2);
			
		}
		

		// now we print the results
		
		
	  	if (!results.isEmpty()){
	  		System.out.println("This side effect or its synonyms are caused by these drugs:   ");
		  	System.out.println();
		  	
			for(int i=0;i<results.size();i++){
				System.out.println(results.get(i));
				
			}
			// data sources
			System.out.println();
			System.out.println("*****************Source*****************");
			System.out.println("Data coming from the sources:");
			System.out.println();

			if(!DataBaseSources.getDataSourceCIDs().isEmpty()){
				for(String s : DataBaseSources.getDataSourceCIDs()){
					System.out.print(" -> " + s);

				}
			}
			
	  		System.out.println();

	  		if(!DataBaseSources.getDataSourceOmimOnto().isEmpty()){
				for(String s : DataBaseSources.getDataSourceOmimOnto()){
					System.out.print(" -> " + s);

				}
			}
	  		System.out.println();
	  		if(!DataBaseSources.getDataSourceDrugBankCause().isEmpty()){
				for(String s : DataBaseSources.getDataSourceDrugBankCause()){
					System.out.print(" -> " + s);
				}
			}

	  		DataBaseSources.clearCIDs();
	  		DataBaseSources.clearOmimOnto();
	  		DataBaseSources.clearDrugBankCause();

	  		System.out.println();
	  		
	}
	  	
	
	  	//we do the same as other resarch.. let's start finding drugs that cures the se then print them
	  	
	  	ArrayList<String> medsheal = new ArrayList<String>();
	  	ArrayList<String> medsHealDistinct = new ArrayList<String>();

	  	
	  	for(int j=0;j<signs.size();j++){
  			medsheal.addAll(ConsoleDisplayResults.findMedication(signs.get(j)));  //find med for every sign
  		}
  		
		medsHealDistinct = ConsoleDisplayResults.duplicate(medsheal); //list of the results
	  	
  		if(!medsHealDistinct.isEmpty()){
  			System.out.println("--------------------------------------------------------------Treat-----------------------------------");
	  		System.out.println("The drugs that treat this side effect and its synonyms are:   ");
	  		System.out.println();

	  		for(int i=0;i<medsHealDistinct.size();i++){
				System.out.println(medsHealDistinct.get(i));
				
			}
	  		System.out.println();
	  		System.out.println("*****************Source*****************");
	  		System.out.println("Data coming from the sources:");
	  		System.out.println();
	  		
	  		if(!DataBaseSources.getDataSourceFindMed().isEmpty()){
	  			for(String s : DataBaseSources.getDataSourceFindMed()){
					System.out.print(" -> " + s);

				}
	  		}
	  		System.out.println();
	  		if(!DataBaseSources.getDataSourceDrugBank().isEmpty()){
	  			for(String s : DataBaseSources.getDataSourceDrugBank()){
					System.out.print(" -> " + s);

				}
	  		}
	  		
	  		DataBaseSources.clearFindMed();
			DataBaseSources.clearDrugBank();
			System.out.println();
  			
  		}
		
	
 
  		//so here we are.. let's do the same as previous search to find diseases that cause the signs
  		
  		ArrayList<String> diseaseResults = ConsoleDisplayResults.findAllDisease(signs.get(0));
		
		ArrayList<String> diseasecause2 = new ArrayList<String>();
		
		for(int j=1;j<signs.size();j++){
			diseasecause2 = ConsoleDisplayResults.findAllDisease(signs.get(j));
			diseaseResults = unionResults(diseaseResults, diseasecause2); //here is the intersection
			
		}
		
		// let's print the results
		
		   if(!diseaseResults.isEmpty()){
				System.out.println("--------------------------------------------------------------");
	
			  	System.out.println("This sign or its synonyms is the manifestation of these diseases (synonyms):   ");
				System.out.println();
				
			  		for(int i=0;i<diseaseResults.size();i++){
			  			System.out.println(diseaseResults.get(i));
					
			  		}
					System.out.println();

					if(!DataBaseSources.getDataSourceOrpha().isEmpty() || !DataBaseSources.getDataSourceOmim().isEmpty() || !DataBaseSources.getDataSourceDecipher().isEmpty()){
						System.out.println("*****************Source*****************");
						System.out.println("Data coming from the sources:");//to complete
						System.out.println();


						for(String s : DataBaseSources.getDataSourceOrpha()){
							System.out.print(" -> " + s);

						}
						System.out.println();


						for(String s : DataBaseSources.getDataSourceOmim()){
							System.out.print(" -> " + s);

						}
						
						System.out.println();

						for(String s : DataBaseSources.getDataSourceDecipher()){
							System.out.print(" -> " + s);

						}
						
					}
			  		
					DataBaseSources.clearOrpha();
					DataBaseSources.clearFindMed();
					DataBaseSources.clearDrugBank();
					DataBaseSources.clearOmim();
					DataBaseSources.clearDecipher();

				System.out.println();
				System.out.println("--------------------------------------------------------------");
				
				//search for drug that can treat the diseases

				ArrayList<String> medshealDisease = new ArrayList<String>();
				
			  	for(int i=0;i<diseaseResults.size();i++){
			  		medshealDisease.addAll(ConsoleDisplayResults.findMedication(diseaseResults.get(i))); 
			  		
			  	}	
			  
			  	ArrayList<String> medshealdistinct2 = ConsoleDisplayResults.duplicate(medshealDisease);	
			  	
			  	if(!medshealdistinct2.isEmpty()){
			  		System.out.println("The drugs that treat the diseases are (synonyms):   ");
			  		
				  	System.out.println();
				  	for(int i=0;i<medshealdistinct2.size();i++){
			  			System.out.println(medshealdistinct2.get(i));
					
			  		}
				  	
				  	System.out.println();
				  	
				  	System.out.println("*****************Source*****************");
					System.out.println("Data coming from the sources:");
			  		System.out.println();
			  		
			  		if(!DataBaseSources.getDataSourceFindMed().isEmpty()){
			  			for(String s : DataBaseSources.getDataSourceFindMed()){
							System.out.print(" -> " + s);

						}
			  		}
			  		System.out.println();
			  		if(!DataBaseSources.getDataSourceDrugBank().isEmpty()){
			  			for(String s : DataBaseSources.getDataSourceDrugBank()){
							System.out.print(" -> " + s);

						}
			  		}
				  	
			  	}
				  	
			DataBaseSources.clearFindMed();
			DataBaseSources.clearDrugBank();
			
		   }
		   
	}


}
