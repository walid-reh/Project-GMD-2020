package app;

import java.awt.Color;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

import org.apache.lucene.queryparser.classic.ParseException;

import com.trolltech.qt.QSignalEmitter.Signal1;
import com.trolltech.qt.core.*;
//import com.trolltech.qt.gui.*;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
//import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QCompleter;
import com.trolltech.qt.gui.QFont;
//import com.trolltech.qt.gui.QDial;
//import com.trolltech.qt.gui.QDialogButtonBox;
//import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QListView;
//import com.trolltech.qt.gui.QMessageBox;
//import com.trolltech.qt.gui.QScrollBar;
//import com.trolltech.qt.gui.QSlider;
//import com.trolltech.qt.gui.QSpinBox;
import com.trolltech.qt.gui.QStringListModel;
import com.trolltech.qt.gui.QListWidget;
//import com.trolltech.qt.gui.QTableWidgetItem;
//import com.trolltech.qt.gui.QTextItem;
//import com.trolltech.qt.gui.QTextTableFormat;
//import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;

import connexionsQueries.DataBaseSources;
import connexionsQueries.HPO_annotation_querier;
import connexionsQueries.OrphaData_querier;
import connexionsQueries.Request;
import connexionsQueries.SiderQuerier;
import indexations.ATC;
import indexations.DrugBank_index_search;
import indexations.HPO_obo;
import indexations.Omim_txt;
import indexations.OmimOnto;
import indexations.Stitch;

import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QHBoxLayout;
import indexations.*;
import connexionsQueries.*;

public class InterfaceFinale extends QWidget {

	private static QListWidget listWidget1;
	private static QListWidget listWidget2;
	private static QListWidget listWidget3;
	private static QListWidget listWidget4;

	private static QListWidget list1;
	private static QListWidget list2;
	private static QListWidget list3;
	private static QListWidget list4;

	private QLabel label1;
	private QLabel label2;
	private QLabel label3;
	private QLabel label4;

	private QComboBox echoComboBox;
	private QComboBox nbSym;

	private final QGridLayout layout = new QGridLayout();


	private QHBoxLayout champs;

	private final QHBoxLayout champ1 = new QHBoxLayout();
	private final QHBoxLayout champ2 = new QHBoxLayout();
	private final QHBoxLayout champ3 = new QHBoxLayout();

	private QLabel guide;


	private QPushButton prec1;
	private QPushButton prec2;
	private QPushButton prec3;

	private QPushButton suiv1;
	private QPushButton suiv2;
	private QPushButton suiv3;




	private final QPushButton but1 = new QPushButton();

	private QLabel nbsymres ;
	private QLabel nbdisperpage ;

	private int nbform;

	private QLineEdit edit1;
	private QLineEdit edit2;
	private QLineEdit edit3;
	private QLineEdit edit4;

	private QPushButton search;

	private String line;
	private String[] ligne;
	public final Signal1<String> configured = new Signal1<String>();

	public InterfaceFinale(){

		label1 = new QLabel(tr("Symptom's causing medicines"));
		listWidget1 = new QListWidget();
		listWidget1.setFixedHeight(200);
		list1 = new QListWidget();
		list1.setMaximumHeight(50);

		label2 = new QLabel(tr("Symptom's treating medicines"));
		listWidget2 = new QListWidget();
		listWidget2.setFixedHeight(200);
		list2 = new QListWidget();
		list2.setMaximumHeight(50);

		label3 = new QLabel(tr("Symptom's causing diseases"));
		listWidget3 = new QListWidget();
		listWidget3.setFixedHeight(200);
		list3 = new QListWidget();
		list3.setMaximumHeight(50);

		label4 = new QLabel(tr("Diseases' treating medicines"));
		listWidget4 = new QListWidget();
		listWidget4.setFixedHeight(200);
		list4 = new QListWidget();
		list4.setMaximumHeight(50);

		QPushButton refresh = new QPushButton("Refresh");
		refresh.clicked.connect(this,"clear()");

		nbform = 0;

		/**QFile testCSV = new QFile("C:\\Users\\Sofian\\Desktop\\tuto_qt\\iris\\iris.csv");
		 testCSV.open(new QIODevice.OpenMode(QIODevice.OpenModeFlag.ReadOnly,
		 QIODevice.OpenModeFlag.Text));


		 QTextStream in = new QTextStream(testCSV);
		 while (!in.atEnd()) {



		 line = in.readLine();
		 ligne = line.split("\n");

		 for(int i=0;i<ligne.length;i++){

		 System.out.println(ligne[i]+"\n");
		 System.out.println("\n");
		 }
		 } **/




		nbsymres = new QLabel("Enter the symptom(s) you want to search");
		nbsymres.setFont(new QFont("Times", 14, QFont.Weight.Bold.value()));

		guide = new QLabel("Here's how to use our application to search for drugs and symptoms very easily.\n"
				+ "You will only need to enter your request in the dedicated field. Once you've done that, please press \"Search\".\n"
				+ "You will then be provided with the answer, which (from left to right then top to bottom) consists of"
				+ " the causing durgs first, then the treating ones, the diseases which might\n"
				+ "cause the symptom(s) and finally the drugs which treat the latter. "
				+ "You'll also be provided with the sources (i.e the databases) from which the results come from.\n"
				+ "The results will be displayed in the big rectangle and the sources in the small one below. "
				+ "Finally, should your requests include conjunctions or disjunctions (e.g symptom 1 or/and symptom 2),\n"
				+ "make sure to use capital letters for OR/AND (symptom 1 OR/AND symptom 2).\n"
				+ "Note that you can use regular expressions as well. \n"
				+"Finaly if you want to move on to a new request, press the \"Refresh\" button first.\n"
				+ "The former results will then be cleared out and you'll be able to enter a new request. Thanks for your attention and have a nice time using our app!\n");

		guide.setFont(new QFont("Times", 10, QFont.Weight.Bold.value()));

		edit1 = new QLineEdit();
		edit2 = new QLineEdit();
		edit3 = new QLineEdit();
		edit4 = new QLineEdit();
		QStringListModel list = new QStringListModel();



		List<String> wordList = new Vector<String>();
		wordList.add("alpha");
		wordList.add("omega");
		wordList.add("omicron");
		wordList.add("zeta");

		QCompleter completer = new QCompleter(wordList);

		completer.setCaseSensitivity(Qt.CaseSensitivity.CaseInsensitive);
		edit1.setCompleter(completer);

		QPushButton afficher = creeButton("Display", "addtab()");
		QPushButton search = creeButton("Search","display()");


		nbSym = new QComboBox();

		layout.addWidget(nbsymres,1,0);


		edit1 = new QLineEdit();

		layout.addWidget(edit1,1,1);



		layout.addWidget(afficher,3,0,1,1);
		layout.addWidget(refresh,3,2,1,1);
		layout.addWidget(search,3,1,1,1);


		layout.addWidget(guide,0,0);


		setWindowTitle("GMD-2020");
		setLayout(layout);

		System.out.println(nbform);


		nbSym.activated.connect(this, "addFormSyme(String)");



	}



	/*private*/ public QPushButton creeButton(String text, String goldMember)
	{
		QPushButton button = new QPushButton(text);
		button.clicked.connect(this, goldMember);

		return button;
	}

	public void clear() {

		listWidget1.clear();
		listWidget2.clear();
		listWidget3.clear();
		listWidget4.clear();

		list1.clear();
		list2.clear();
		list3.clear();
		list4.clear();

	}

	public void addFormSyme(String b){

		layout.removeItem(champs);

		champs = new QHBoxLayout();



		champ1.addWidget(edit1);

		champ2.addWidget(edit1);
		champ2.addWidget(edit2);

		champ3.addWidget(edit1);
		champ3.addWidget(edit2);
		champ3.addWidget(edit3);



		int a = Integer.parseInt(b);
		if(a == 1){


			layout.addItem(champ1,1,0);


		}

		if(a == 2){



			layout.addItem(champ2,1,0);

		}
		if(a == 3){



			layout.addItem(champ3,1,0);

		}




	}

	public void addtab(){

		layout.removeWidget(guide);
		guide.hide();

		layout.addWidget(label1,4,0);
		layout.addWidget(listWidget1,5,0);
		layout.addWidget(list1,6,0);

		layout.addWidget(label2,4,1);
		layout.addWidget(listWidget2,5,1);
		layout.addWidget(list2,6,1);

		layout.addWidget(label3,7,0);
		layout.addWidget(listWidget3,8,0);
		layout.addWidget(list3,9,0);

		layout.addWidget(label4,7,1);
		layout.addWidget(listWidget4,8,1);
		layout.addWidget(list4,9,1);

	}

	private static void remplissageTableau1(QListWidget e, ArrayList<String> labl) {

		for(int i=0;i<labl.size();i++)
			e.addItem(labl.get(i));

	}

	public void display() throws org.json.simple.parser.ParseException, Exception {

		//this will contain drugs that cause the symtom
		ArrayList<String> medscause = new ArrayList<String>();

		//this will contain diseases that cause the symptom
		ArrayList<String> diseasecause  = new ArrayList<String>();

		//this will contain drugs that cure the symptom and disease
		ArrayList<String> medsheal = new ArrayList<String>();

		// instance of HPO class
		HPO_obo hpo = new HPO_obo();


		//let take sign /symptom from console and make design for outputting our search results
		//Scanner sc = new Scanner(System.in);
		StringBuilder sb=new StringBuilder();
		sb.append("\n--------------------------------------------------------------------------------------------------------");
		sb.append("\n-------------------------------------- Biomedical Data Mediator ----------------------------------------");
		sb.append("\n--------------------------------------------------------------------------------------------------------");
		sb.append("\n\nWelcome ! This is an application that queries several databases to get information about diseases and \nmedications that cause or treat a given clinical sign.");
		sb.append("\n********************************************************************************************************");
		System.out.println(sb.toString());
		//System.out.println("\n");
		System.out.println("Please enter a symptom:");
		String str = edit1.text();
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
			intersection(sign);



		}
		else if(sign.indexOf(" OR ") != -1){ // If OR query are found we call the union function
			// call union function
			System.out.println("OR query found");
			union(sign);




		}// for synonyms, please uncomment the following:
				/*else if(hpo.getSynonyms(sign).size()>1){
					System.out.println("synonyms detected");

					ArrayList<String> signs = hpo.getSynonyms(sign);
					//System.out.println("coucou");
					for(String s: signs){
						System.out.println(s);
					}

					Request.synonyms(signs);



				}*/

		else{ // this is only for a single sign
			System.out.println();


			if (!(medscause  = sideEffectCause(sign)).isEmpty()){
				System.out.println("This side effect is caused by these drugs:   ");
				System.out.println();
				ArrayList<String> source= new ArrayList<String>();

				remplissageTableau1(listWidget1,medscause);

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
						System.out.print(" => " + s);
						source.add("=>"+s);
					}
					//System.out.println(this.list1.text());
				}

				System.out.println();

				if(!DataBaseSources.getDataSourceOmimOnto().isEmpty()){
					for(String s : DataBaseSources.getDataSourceOmimOnto()){
						System.out.print(" -> " + s);
						source.add("=>"+s);

					}
				}

				System.out.println();

				if(!DataBaseSources.getDataSourceDrugBankCause().isEmpty()){
					for(String s : DataBaseSources.getDataSourceDrugBankCause()){
						System.out.print(" -> " + s);
						source.add("=>"+s);

					}
				}

				remplissageTableau1(list1,source);

				DataBaseSources.clearCIDs();
				DataBaseSources.clearOmimOnto();
				DataBaseSources.clearDrugBankCause();


				System.out.println();



				medsheal = findMedication(sign); //find the drugs for the side effect


				if(!medsheal.isEmpty()){
					System.out.println("--------------------------------------------------------------TREAT MEDICATION SIDE EFFECT------------------------------------------");
					System.out.println("The drugs that treat the side effect are:   ");
					System.out.println();
					ArrayList<String> source1 = new ArrayList<String>();

					remplissageTableau1(listWidget2,medsheal);

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
							source1.add("=>"+s);

						}
					}
					System.out.println();
					if(!DataBaseSources.getDataSourceDrugBank().isEmpty()){
						for(String s : DataBaseSources.getDataSourceDrugBank()){
							System.out.print(" -> " + s);
							source1.add("=>"+s);

						}
					}

					remplissageTableau1(list2,source1);

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

				remplissageTableau1(listWidget3,diseasecause);

				for(int i=0;i<diseasecause.size();i++){
					System.out.println(diseasecause.get(i));

				}
				System.out.println();

				ArrayList<String> source2 = new ArrayList<String>();
				if(!DataBaseSources.getDataSourceOrpha().isEmpty() || !DataBaseSources.getDataSourceOmim().isEmpty() || !DataBaseSources.getDataSourceDecipher().isEmpty()){
					System.out.println("*****************Source*****************");
					System.out.println("Data coming from the sources:");
					System.out.println();



					for(String s : DataBaseSources.getDataSourceOrpha()){
						System.out.print(" -> " + s);
						source2.add("=>"+s);

					}
					System.out.println();


					for(String s : DataBaseSources.getDataSourceOmim()){
						System.out.print(" -> " + s);
						source2.add("=>"+s);

					}

					System.out.println();

					for(String s : DataBaseSources.getDataSourceDecipher()){
						System.out.print(" -> " + s);
						source2.add("=>"+s);

					}


				}
				remplissageTableau1(list3,source2);

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
				ArrayList<String> source3 = new ArrayList<String>();


				if(!medshealdistinct.isEmpty()){
					System.out.println("The drugs that treat the disease are:   ");


					remplissageTableau1(listWidget4,medshealdistinct);


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
							source3.add("=>"+s);

						}
					}
					System.out.println();
					if(!DataBaseSources.getDataSourceDrugBank().isEmpty()){
						for(String s : DataBaseSources.getDataSourceDrugBank()){
							System.out.print(" -> " + s);
							source3.add("=>"+s);

						}
					}

				}


				remplissageTableau1(list4,source3);


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

		ArrayList<String> source = new ArrayList<String>();
		ArrayList<String> source1 = new ArrayList<String>();
		ArrayList<String> source2 = new ArrayList<String>();
		ArrayList<String> source3 = new ArrayList<String>();

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
			remplissageTableau1(listWidget1,results);
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
					source.add("=>"+s);

				}
			}

			System.out.println();

			if(!DataBaseSources.getDataSourceOmimOnto().isEmpty()){
				for(String s : DataBaseSources.getDataSourceOmimOnto()){
					System.out.print(" -> " + s);
					source.add("=>"+s);

				}
			}

			System.out.println();

			if(!DataBaseSources.getDataSourceDrugBankCause().isEmpty()){
				for(String s : DataBaseSources.getDataSourceDrugBankCause()){
					System.out.print(" -> " + s);
					source.add("=>"+s);

				}
			}

			remplissageTableau1(list1,source);


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
			remplissageTableau1(listWidget2,medsHealDistinct);


			for(int i=0;i<medsHealDistinct.size();i++){
				System.out.println(medsHealDistinct.get(i));

			}

			System.out.println("*****************Source*****************");System.out.println();
			System.out.println("Data coming from the sources:");
			System.out.println();

			if(!DataBaseSources.getDataSourceFindMed().isEmpty()){
				for(String s : DataBaseSources.getDataSourceFindMed()){
					System.out.print(" -> " + s);
					source1.add("=>"+s);

				}
			}
			System.out.println();
			if(!DataBaseSources.getDataSourceDrugBank().isEmpty()){
				for(String s : DataBaseSources.getDataSourceDrugBank()){
					System.out.print(" -> " + s);
					source1.add("=>"+s);

				}
			}

			remplissageTableau1(list2,source1);

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
			remplissageTableau1(listWidget3,diseaseResults);


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
					source2.add("=>"+s);

				}
				System.out.println();


				for(String s : DataBaseSources.getDataSourceOmim()){
					System.out.print(" -> " + s);
					source2.add("=>"+s);

				}

				System.out.println();

				for(String s : DataBaseSources.getDataSourceDecipher()){
					System.out.print(" -> " + s);
					source2.add("=>"+s);

				}


			}

			remplissageTableau1(list3,source2);

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
				remplissageTableau1(listWidget4,medshealdistinct2);

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
						source3.add("=>"+s);

					}
				}
				System.out.println();
				if(!DataBaseSources.getDataSourceDrugBank().isEmpty()){
					for(String s : DataBaseSources.getDataSourceDrugBank()){
						System.out.print(" -> " + s);
						source3.add("=>"+s);

					}
				}

			}

			remplissageTableau1(list4,source3);

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

		ArrayList<String> source = new ArrayList<String>();
		ArrayList<String> source1 = new ArrayList<String>();
		ArrayList<String> source2 = new ArrayList<String>();
		ArrayList<String> source3 = new ArrayList<String>();

		for(int j=1;j<signs.size();j++){
			medscause2 = ConsoleDisplayResults.sideEffectCause(signs.get(j));
			results = intersectionResults(sign, results, medscause2); //here is the intersection

		}


		// the print of results


		if (!results.isEmpty()){
			System.out.println("These side effects are caused by the union of these drugs:   ");
			System.out.println();
			remplissageTableau1(listWidget1,results);


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
					source.add("=>"+s);

				}
			}

			System.out.println();

			if(!DataBaseSources.getDataSourceOmimOnto().isEmpty()){
				for(String s : DataBaseSources.getDataSourceOmimOnto()){
					System.out.print(" -> " + s);
					source.add("=>"+s);

				}
			}

			System.out.println();

			if(!DataBaseSources.getDataSourceDrugBankCause().isEmpty()){
				for(String s : DataBaseSources.getDataSourceDrugBankCause()){
					System.out.print(" -> " + s);
					source.add("=>"+s);

				}
			}
			remplissageTableau1(list1,source);

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
			remplissageTableau1(listWidget2,results);


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
					source1.add("=>"+s);

				}
			}
			System.out.println();
			if(!DataBaseSources.getDataSourceDrugBank().isEmpty()){
				for(String s : DataBaseSources.getDataSourceDrugBank()){
					System.out.print(" -> " + s);
					source1.add("=>"+s);

				}
			}

			remplissageTableau1(list2,source1);
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
			remplissageTableau1(listWidget3,diseaseResults);


			for(int i=0;i<diseaseResults.size();i++){
				System.out.println(diseaseResults.get(i));

			}
			System.out.println();

			if(!DataBaseSources.getDataSourceOrpha().isEmpty() || !DataBaseSources.getDataSourceOmim().isEmpty() || !DataBaseSources.getDataSourceDecipher().isEmpty()){
				System.out.println("Data coming from the sources:");//to complete
				System.out.println();

				for(String s : DataBaseSources.getDataSourceOrpha()){
					System.out.print(" -> " + s);
					source2.add("=>"+s);

				}
				System.out.println();

				for(String s : DataBaseSources.getDataSourceOmim()){
					System.out.print(" -> " + s);
					source2.add("=>"+s);

				}

				System.out.println();

				for(String s : DataBaseSources.getDataSourceDecipher()){
					System.out.print(" -> " + s);
					source2.add("=>"+s);

				}

			}

			remplissageTableau1(list3,source2);

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

				remplissageTableau1(listWidget4,medshealdistinct2);




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
						source3.add("=>"+s);


					}
				}
				System.out.println();
				if(!DataBaseSources.getDataSourceDrugBank().isEmpty()){
					for(String s : DataBaseSources.getDataSourceDrugBank()){
						System.out.print(" -> " + s);
						source2.add("=>"+s);

					}
				}

			}

			remplissageTableau1(list4,source3);

			DataBaseSources.clearFindMed();
			DataBaseSources.clearDrugBank();

		}

	}


	public static void main(String args[])
	{
		QApplication.initialize(args);

		InterfaceFinale widget = new InterfaceFinale();
		widget.show();

		QApplication.instance().exec();
		QApplication.shutdown();
	}


}
