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
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import app.*;

public class ATC {


	static final File INDEXEDoc = new File ("index/ATC_index"); //indexPath

	private String path;
	private static File file;
	private static Analyzer analyzer;
	Directory ind;
	ArrayList<String> MedicineResults;


	//For our full-text  indexation we do as we did in TP.
	public ATC () throws IOException {
		path = "/home/walid/Bureau/gmd-project-final/ressources/databases/br08303.keg"; // directory for the data base
		file = new File(path); // we don't need this some other versions of Lucene ---> see the documentation of Lucene
		analyzer = new KeywordAnalyzer();
		ind = FSDirectory.open(INDEXEDoc);
		MedicineResults = new ArrayList<String>();

	}


	public void indexATC() throws IOException {
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
		int cpt=0;
		try {
			InputStream ips = new FileInputStream(file);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);

			for(int i =0;i<10;i++){
				br.readLine(); //to skip the 10 first lines
			}

			String line;
			while ((line= br.readLine()) != null && line.charAt(0)!='!') {
				if(line.charAt(0)=='#'){
					line = br.readLine();
					line = br.readLine();
				}

				ArrayList<String> fields = new ArrayList<String>();
				int i = 0;

				String a = "";

				while(line.charAt(i)!=' '){


					a = a+ line.charAt(i);
					i=i+1;

				}
				fields.add(a);


				while(line.charAt(i)==' '){
					i = i + 1;
				}
				String b = "";
				while(line.charAt(i)!=' '){
					b = b+ line.charAt(i);
					i=i+1;
				}
				fields.add(b);

				// now, we browse the empty chars to find the drug name:
				while(line.charAt(i)==' '){
					i = i + 1; // the last "i" will be the index of the first letter
				}

				String c = "";
				while(i < line.length()){
					c = c+ line.charAt(i);
					i=i+1;
				}
				fields.add(c);

				Document doc = new Document();

				//add 3 fields to it
				doc.add(new StringField("Classification", fields.get(0), Field.Store.NO ));
				doc.add(new StringField("CodeATC", fields.get(1), Field.Store.NO ) );
				doc.add(new StringField("medicine", fields.get(2), Field.Store.YES));


				w.addDocument(doc);
				cpt++;
			}
			br.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}


//One we've indexed the file, we move to the next step: searching !

	//returns a list on drug names from "code ATC":
	public ArrayList<String> find(String word, String field) throws ParseException, IOException{

		// indexATC();
		DirectoryReader ireader = DirectoryReader.open(ind);
		IndexSearcher isearch = new IndexSearcher(ireader);
		analyzer = new KeywordAnalyzer();
		QueryParser parser = new QueryParser(Version.LUCENE_40, field, analyzer);
		parser.setLowercaseExpandedTerms(false);
		Query query = parser.parse(word);
		ScoreDoc[] hits;
		int hitsPerPage = 50;
		TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
		isearch.search(query, collector);
		hits = collector.topDocs().scoreDocs;

		ArrayList<String> medicine = new ArrayList<String>();//this is the list of drug's name
		for (int i = 0; i < hits.length; ++i) {
			int docId = hits[i].doc;
			Document d = isearch.doc(docId);
			medicine.add(d.get("medicine"));
			MedicineResults = ConsoleDisplayResults.duplicate(medicine);

		}

		return MedicineResults;

	}




}