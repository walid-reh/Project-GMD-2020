package indexations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import app.*;
import connexionsQueries.*;

public class DrugBank_index_search {


	static final File INDEXEDoc = new File ("index/DrugBank_index");

	private String path;
	private static File file;
	private static Analyzer analyzer;
	static Directory ind;
	static ArrayList<String> meds;
	static ArrayList<String> medsCause;


	//we look for the file to index:
	public DrugBank_index_search () throws IOException {
		path = "/home/walid/Bureau/gmd-project-final/ressources/databases/full_database.xml";
		file = new File(path);
		analyzer = new StandardAnalyzer(Version.LUCENE_40);
		ind = FSDirectory.open(INDEXEDoc);
		meds = new ArrayList<String>();
		medsCause = new ArrayList<String>();

	}

	public void indexDrugBank() throws IOException {
		// indexing
		try {

			IndexWriterConfig configuration = new IndexWriterConfig(Version.LUCENE_40, analyzer);
			IndexWriter writer = new IndexWriter (ind, configuration );
			addDocument(writer);

			writer.close();

		}  catch (Exception e) {
			System.out.println(e.toString());
		}

	}

	private static void addDocument(IndexWriter w) throws IOException {
		try {
			InputStream ips = new FileInputStream(file);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);

			br.readLine();

			String line = br.readLine();
			Boolean bool = false;
			while (line != null){ // we read the second line

				bool = false;
				int i=0;

				if(line.contains("<")){
					while(line.charAt(i)!='<'){

						i =i+1;

					}

					if(line.charAt(i+1)=='d' && line.charAt(i+2)=='r' && line.charAt(i+3)=='u' && line.charAt(i+4)=='g' && line.charAt(i+5)==' '){
						String name ="";
						String indication ="";
						String toxicity="";

						Document doc = new Document();

						line = br.readLine();

						// name:

						int k= 0;
						if(line.contains("<")){
							while(line.charAt(k)!='<'){

								k =k+1;

							}

						}else{
							k=0;

						}

						while(!(line.charAt(k+1)=='n' && line.charAt(k+2)=='a' && line.charAt(k+3)=='m' && line.charAt(k+4)=='e' && line.charAt(k+5)=='>')){
							line = br.readLine();
						}

						int b = k+6;
						while(line.charAt(b)!='<'){
							name = name + line.charAt(b);
							b = b+1;
						}


						doc.add(new StringField("name", name, Field.Store.YES));

						//indication:

						int c =0;
						if(line.contains("<")){

							while(line.charAt(c)!='<'){

								c =c+1;

							}

						}else{
							c=0;

						}

						while(!(line.charAt(c+1)=='i' && line.charAt(c+2)=='n' && line.charAt(c+3)=='d' && line.charAt(c+4)=='i' && line.charAt(c+5)=='c' && line.charAt(c+6)=='a' && line.charAt(c+7)=='t' && line.charAt(c+8)=='i' && line.charAt(c+9)=='o' && line.charAt(c+10)=='n')){
							line = br.readLine();
							c=0;
							if(line.contains("<")){

								while(line.charAt(c)!='<'){

									c =c+1;

								} // i is the index of '<'

							}

						}


						int z = c+12;
						while(line.charAt(z)!='<'){
							if(z==line.length()-1){
								line = br.readLine();
								z=0;
								indication = indication + ' ';
							}

							indication = indication + line.charAt(z);
							z = z+1;
						}
						doc.add(new StringField("indication", indication, Field.Store.NO));


						//toxicity:

						int n =0;
						if(line.contains("<")){

							while(line.charAt(n)!='<'){

								n =n+1;

							}

						}else{
							n=0;

						}

						while(!(line.charAt(n+1)=='d' && line.charAt(n+2)=='r' && line.charAt(n+3)=='u' && line.charAt(n+4)=='g' && line.charAt(n+5)==' ' )            &&      !(line.charAt(n+1)=='t' && line.charAt(n+2)=='o' && line.charAt(n+3)=='x' && line.charAt(n+4)=='i' && line.charAt(n+5)=='c' && line.charAt(n+6)=='i' && line.charAt(n+7)=='t' && line.charAt(n+8)=='y')){
							line = br.readLine();
							if(line.contains("<")){
								n=0;
								while(line.charAt(n)!='<'){

									n =n+1;

								} // i is the index of '<'
							}


						}


						if(!(line.charAt(n+1)=='d' && line.charAt(n+2)=='r' && line.charAt(n+3)=='u' && line.charAt(n+4)=='g' && line.charAt(n+5)==' ' )){
							int y = n+10;
							while(line.charAt(y)!='<'){
								if(y==line.length()-1){
									line = br.readLine();
									y=0;
									toxicity = toxicity + ' ';
								}

								toxicity = toxicity + line.charAt(y);
								y = y+1;
							}



						}
						if(line.charAt(n+1)=='d' && line.charAt(n+2)=='r' && line.charAt(n+3)=='u' && line.charAt(n+4)=='g' && line.charAt(n+5)==' '){
							toxicity = "has no toxicity";
							bool = true;
						}

						doc.add(new StringField("toxicity", toxicity, Field.Store.NO));

						w.addDocument(doc);





					}
				}
				if(bool==false){
					line = br.readLine();

				}
			}

		}catch (Exception e){
			System.out.println(e.toString());
		}


	}




	//Let's move on the search step !

	//drug list that cure disease
	public ArrayList<String> findMed(String word, String field) throws ParseException, IOException{
		// indexDrugBank();
		DirectoryReader ireader = DirectoryReader.open(ind);
		IndexSearcher isearch = new IndexSearcher(ireader);
		Term term = new Term(field,"*"+word+"*");
		Query query = new WildcardQuery(term);

		ScoreDoc[] hits;
		int hitsPerPage = 50;
		TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
		isearch.search(query, collector);
		hits = collector.topDocs().scoreDocs;


		ArrayList<String> drugs = new ArrayList<String>();

		for (int i = 0; i < hits.length; ++i) {
			int docId = hits[i].doc;
			Document d = isearch.doc(docId);

			meds.add(d.get("name"));
			drugs = ConsoleDisplayResults.duplicate(meds);

		}
		if(!drugs.isEmpty()){
			DataBaseSources.addSourceDrugBank("DrugBank");
		}

		return drugs;

	}

	//returns a drugs list that can cause the symptom
	public ArrayList<String> findSideEffectCause(String word, String field) throws ParseException, IOException{
		// indexDrugBank();
		DirectoryReader ireader = DirectoryReader.open(ind);

		IndexSearcher isearch = new IndexSearcher(ireader);
		analyzer = new KeywordAnalyzer();
		Term term = new Term(field,"*"+word+"*");
		Query query = new WildcardQuery(term);


		ScoreDoc[] hits;
		int hitsPerPage = 50;
		TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
		isearch.search(query, collector);
		hits = collector.topDocs().scoreDocs;


		ArrayList<String> drugsCause = new ArrayList<String>();

		for (int i = 0; i < hits.length; ++i) {
			int docId = hits[i].doc;
			Document d = isearch.doc(docId);

			medsCause.add(d.get("name"));
			drugsCause = ConsoleDisplayResults.duplicate(medsCause);

		}
		if(!drugsCause.isEmpty()){
			DataBaseSources.addSourceDrugBankCause("DrugBank");
		}

		return drugsCause;

	}



}