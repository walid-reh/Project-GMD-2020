package app;

import java.util.Scanner;
import java.util.Set;
import org.apache.lucene.queryparser.classic.ParseException;

import com.trolltech.qt.gui.QApplication;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

import indexations.*;

import connexionsQueries.*;

public class ConsoleDisplayResults {

	public static void main(String[] args) throws Exception {

		QApplication.initialize(args);

		InterfaceFinale window = new InterfaceFinale();
		window.show();

		QApplication.execStatic();


		//this will contain drugs that cause the symtom
		ArrayList<String> medscause = new ArrayList<String>();

		//this will contain diseases that cause the symptom
		ArrayList<String> diseasecause  = new ArrayList<String>();

		//this will contain drugs that cure the symptom and disease
		ArrayList<String> medsheal = new ArrayList<String>();

		// instance of HPO class
		HPO_obo hpo = new HPO_obo();


		//let take sign /symptom from console and make design for outputting our search results
		Scanner sc = new Scanner(System.in);
		StringBuilder sb=new StringBuilder();
		sb.append("\n--------------------------------------------------------------------------------------------------------");
		sb.append("\n-------------------------------------- Biomedical Data Mediator ----------------------------------------");
		sb.append("\n--------------------------------------------------------------------------------------------------------");
		sb.append("\n\nWelcome ! This is an application that queries several databases to get information about diseases and \nmedications that cause or treat a given clinical sign.");
		sb.append("\n********************************************************************************************************");
		System.out.println(sb.toString());
		//System.out.println("\n");
		System.out.println("Please enter a symptom:");
		String str = sc.nextLine();
		System.out.println("................................................................................................");
		System.out.println("You entered : " + str);
		System.out.println("The results are going to be displayed in few seconds....");
		System.out.println("................................................................................................");
		System.out.println();
		System.out.println("--------------------------------------------------------------CAUSE MEDICATION SIDE EFFECT------------------------------------------");

		// let get the symptom back
		String sign = str;

		//If AND query are found we call the intersection function
		if(sign.indexOf(" AND ") != -1){
			System.out.println("AND query found");
			Request.intersection(sign);



		}
		else if(sign.indexOf(" OR ") != -1){ // If OR query are found we call the union function
			// call union function
			System.out.println("OR query found");
			Request.union(sign);




		}// for synonyms, please uncomment the following:
			else if(hpo.getSynonyms(sign).size()>1){
				System.out.println("////////////////////////////////////////////////////////////synonyms detected///////////////////////////////////////////////////////");

				ArrayList<String> signs = hpo.getSynonyms(sign);
				//System.out.println("coucou");
				for(String s: signs){
					System.out.println(s);
				}

				Request.synonyms(signs);



			}

		else{ // this is only for a single sign
			System.out.println();


			if (!(medscause  = sideEffectCause(sign)).isEmpty()){
				System.out.println("This side effect is caused by these drugs:   ");
				System.out.println();

				for(int i=0;i<medscause.size();i++){
					System.out.println(medscause.get(i));

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

				//System.out.println();

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



				medsheal = findMedication(sign); //find the drugs for the side effect


				if(!medsheal.isEmpty()){
					System.out.println("--------------------------------------------------------------TREAT MEDICATION SIDE EFFECT------------------------------------------");
					System.out.println("The drugs that treat the side effect are:   ");
					System.out.println();

					for(int i=0;i<medsheal.size();i++){
						System.out.println(medsheal.get(i));

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



			}
			if(!(diseasecause = findAllDisease(sign)).isEmpty()){
				//the disease
				System.out.println("--------------------------------------------------------------Diseases------------------------------------------------");

				System.out.println("This sign is the manifestation of these diseases:   ");
				System.out.println();

				for(int i=0;i<diseasecause.size();i++){
					System.out.println(diseasecause.get(i));

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
				System.out.println("--------------------------------------------------------------TREAT MEDICATION DISEASE-------------------------------------------------------");



				for(int i=0;i<diseasecause.size();i++){
					medsheal.addAll(findMedication(diseasecause.get(i)));

				}



				ArrayList<String> medshealdistinct = duplicate(medsheal);


				if(!medshealdistinct.isEmpty()){
					System.out.println("The drugs that treats the disease are:   ");





					System.out.println();
					for(int i=0;i<medshealdistinct.size();i++){
						System.out.println(medshealdistinct.get(i));

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


	public static  ArrayList<String> fusion(ArrayList<String> a, ArrayList<String> b){

		for(int i=0;i<b.size();i++) {
			if(!a.contains(b.get(i))){
				a.add(b.get(i));
			}
		}
		return a;
	}

	public static ArrayList<String> findAllDisease(String symptom) throws IOException, ParseException, java.text.ParseException, org.json.simple.parser.ParseException, SQLException{

		OrphaData_querier orpha= new OrphaData_querier();
		Omim_txt omim =new Omim_txt();
		HPO_obo hpo = new HPO_obo();
		HPO_annotation_querier annot = new HPO_annotation_querier();
		ArrayList<String> annotationDiseases= new ArrayList<String>();

		ArrayList<String> Omimdiseases = omim.findDisease(symptom, "Symptom");
		ArrayList<String> Orphadiseases = orpha.findDisease(symptom);
		ArrayList<String> OrphaOmimdiseases = fusion(Omimdiseases,Orphadiseases);

		if(hpo.getID(symptom)!=null) {
			String id_disease = hpo.getID(symptom);
			annotationDiseases=annot.findDiseaseHpo(id_disease, "sign_id");
		}

		ArrayList<String> diseaseResults = fusion(OrphaOmimdiseases,annotationDiseases);

		return diseaseResults;

	}




	public static ArrayList<String> findMedication(String disease) throws IOException, ParseException{
		DrugBank_index_search drugbank = new DrugBank_index_search();
		ArrayList<String> medFusion = new ArrayList<String>();
		ArrayList<String> drugBanktreatmed = new ArrayList<String>();
		medFusion = findMedication1(disease);
		drugBanktreatmed = drugbank.findMed(disease,"indication");


		for(String a : drugBanktreatmed){
			if(!medFusion.contains(a)){
				medFusion.add(a);
			}
		}

		return medFusion;

	}


	public static ArrayList<String> findMedication1(String disease) throws IOException, ParseException{
		//we look in sider:
		ArrayList<String> CIDs = new ArrayList<>();
		SiderQuerier sider = new SiderQuerier();
		CIDs = sider.findMed(disease, "meddra_all_indications", "meddra_concept_name");

		//we search in stitch:
		Stitch stitch = new Stitch();
		ArrayList<String> ATCs = new ArrayList<>(); // that will contain the numATC of all CIDs
		for(String cid1: CIDs){
			String cidm= cid1.replace("CID1", "CIDm");
			ArrayList<String> atc = new ArrayList<>(); // qui contiendra les nums atc pour un CID
			atc = stitch.find(cidm,"chemical");

			int n = atc.size();
			for(int i=0; i<n;i++){
				ATCs.add(atc.get(i));

			}
		}
		ArrayList<String> ATCResults = new ArrayList<>(); //will contain the list of all ATCnum distincts
		ATCResults = duplicate(ATCs);

		//we search in ATC
		ATC atc = new ATC();
		ArrayList<String> medicines = new ArrayList<String>(); //will contain the list of all drugs names of each ATCnum

		for(String a: ATCResults){
			ArrayList<String> meds = new ArrayList<>(); // will contain the drugs names for one ATC code
			meds=atc.find(a, "CodeATC");
			int k = meds.size();
			for(int i=0; i<k;i++){
				medicines.add(meds.get(i));
			}
		}
		ArrayList<String> MEDSResults = new ArrayList<>(); //will contain the list of all ATCnum distinct
		MEDSResults = duplicate(medicines);

		if(!MEDSResults.isEmpty()){
			if(!DataBaseSources.getDataSourceFindMed().contains("Sider (meddra_all_indications)")){
				DataBaseSources.addSourceFindMed("Sider (meddra_all_indications)");
				DataBaseSources.addSourceFindMed("Stitch");
				DataBaseSources.addSourceFindMed("ATC");
			}

		}

		return MEDSResults;

	}

	public static ArrayList<String> duplicate( ArrayList<String> a){

		Set s = new HashSet<String>();
		s.addAll(a);
		ArrayList<String> distinct = new ArrayList<String>(s);
		return distinct;
	}


	//function that returns all the drugs that cause the side effect:
	public static ArrayList<String> sideEffectCause(String sign) throws IOException, ParseException{

		DrugBank_index_search drug = new DrugBank_index_search();

		ArrayList<String> results = new ArrayList<String>();
		ArrayList<String> sideEffectCauseOmim = new ArrayList<String>();
		ArrayList<String> drugBankcause = new ArrayList<String>();
		results = findSideEffectCauseCIDs(sign);

		sideEffectCauseOmim = findSideEffectCauseOmimOnto(sign);
		drugBankcause = drug.findSideEffectCause(sign,"toxicity");

		for(String a : sideEffectCauseOmim){
			if(!results.contains(a)){
				results.add(a);
			}
		}
		//drugbank fusion
		for(String b : drugBankcause){
			if(!results.contains(b)){
				results.add(b);
			}
		}

		return results;

	}





	public static ArrayList<String> findSideEffectCauseCIDs(String sign) throws IOException, ParseException{
		//we look in sider:
		SiderQuerier sider = new SiderQuerier();
		ArrayList<ArrayList<String>> CIDs = new ArrayList<ArrayList<String>>();
		CIDs = sider.findSideEffect(sign, "meddra_all_se", "side_effect_name");

		//we searche in stitch:
		Stitch sti = new Stitch();
		ArrayList<String> ATCs = new ArrayList<>(); // will contain the numATCs of all CID

		for(ArrayList<String> cid: CIDs){

			String cidm= cid.get(0).replace("CID1", "CIDm");
			String cids= cid.get(1).replace("CID0", "CIDs");

			ArrayList<String> atc = new ArrayList<>(); // will contain the ATCnum for one CID
			atc = sti.findDouble(cidm,"chemical",cids,"alias");
			int n = atc.size();
			for(int i=0; i<n;i++){
				ATCs.add(atc.get(i));

			}

		}

		ArrayList<String> ATCResults = new ArrayList<>(); //will contain a list of all ATCnum distinct
		ATCResults = duplicate(ATCs);

		//we search in ATC:
		ATC atc = new ATC();
		ArrayList<String> medicines = new ArrayList<String>(); //will contain the drugs names for all codeATCs
		for(String a: ATCResults){
			ArrayList<String> meds = new ArrayList<>(); // will contain the drugs names for one codeATC
			meds=atc.find(a, "CodeATC");
			int k = meds.size();
			for(int i=0; i<k;i++){
				medicines.add(meds.get(i));
			}
		}

		ArrayList<String> MEDSResults = new ArrayList<>(); //will contain the list of all ATCnum distinct
		MEDSResults = duplicate(medicines);

		//data source:
		if(!MEDSResults.isEmpty()){
			DataBaseSources.addSourceCIDs("Sider (meddra_all_se)");
			DataBaseSources.addSourceCIDs("Stitch");
			DataBaseSources.addSourceCIDs("ATC");
		}

		return MEDSResults;


	}


	public static ArrayList<String> findSideEffectCauseOmimOnto(String sign) throws IOException, ParseException {

		ArrayList<String> cuis =  new ArrayList<String>();
		OmimOnto omimOnto = new OmimOnto();
		cuis =  omimOnto.find(sign,"label");

		SiderQuerier sider = new SiderQuerier();
		ArrayList<String> Meds = new ArrayList<String>(); //will contain all the drugs names that cause the symptom
		for(String cui : cuis){
			ArrayList<ArrayList<String>> CIDs = new ArrayList<ArrayList<String>>();
			CIDs = sider.findSideEffect(cui, "meddra_all_se", "cui");


			Stitch sti = new Stitch();
			ArrayList<String> ATCs = new ArrayList<>(); //will contain the numATCs of all CIDs
			ArrayList<String> ATCResults = new ArrayList<>(); //will contain the list of all ATCnums distincts
			for(ArrayList<String> cid: CIDs){

				String cidm= cid.get(0).replace("CID1", "CIDm");
				String cids= cid.get(1).replace("CID0", "CIDs");
				ArrayList<String> atc = new ArrayList<>(); // will contain the ATCnum for one CID
				atc = sti.findDouble(cidm,"chemical",cids,"alias");

				int n = atc.size();
				for(int i=0; i<n;i++){
					ATCs.add(atc.get(i));

				}

			}

			ATCResults = duplicate(ATCs);

			//we search in ATC:
			ATC atc = new ATC();
			ArrayList<String> medicines = new ArrayList<String>(); //will contain the drugs names of all ATCnums

			for(String a: ATCResults){
				ArrayList<String> meds = new ArrayList<>(); //will contain the drugs names for one CodeATC
				meds=atc.find(a, "CodeATC");
				int k = meds.size();
				for(int i=0; i<k;i++){
					medicines.add(meds.get(i));
				}
			}
			ArrayList<String> MEDSResults = new ArrayList<>(); //will contain the list of all ATCnum distincts
			MEDSResults = duplicate(medicines);
			for(int i=0;i<MEDSResults.size();i++){
				Meds.add(MEDSResults.get(i));
			}


		}
		ArrayList<String> MedsResults = duplicate(Meds);

		//data source:
		if(!MedsResults.isEmpty()){
			DataBaseSources.addSourceOmimOnto("OmimOnto");
			DataBaseSources.addSourceOmimOnto("Sider (meddra_all_se)");
			DataBaseSources.addSourceOmimOnto("Stitch");
			DataBaseSources.addSourceOmimOnto("ATC");
		}

		return MedsResults; //list of drugs that cause the symptom


	}




}
