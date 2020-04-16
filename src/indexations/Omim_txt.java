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
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import connexionsQueries.*;
import app.*;

public class Omim_txt {


	static final File INDEXEDoc = new File ("index/Omim_index");

	private String path;
	private static File file;
	private static Analyzer analyzer;
	static Directory ind;
	ArrayList<String> disease;



	public Omim_txt() throws IOException {
		path = "/home/walid/Bureau/gmd-project-final/ressources/databases/omim.txt";
		file = new File(path);
		analyzer = new KeywordAnalyzer();
		ind = FSDirectory.open(INDEXEDoc);
		disease = new ArrayList<String>();

	}
	public void indexOmim() throws IOException {

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
		Document doc = null;
		try {
			InputStream ips = new FileInputStream(file);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String line;
			while ((line= br.readLine()) != null){


				if (line.contains("*RECORD*")){
					doc = new Document();

				}
				else{


					if (line.equals("*FIELD* TI")){
						String disease ="";
						line=br.readLine();


						int i=0;
						while (line.charAt(i) != ' '){
							i=i+1;
						}
						line =line.substring(i);

						while (!line.equals("*FIELD* TX")){

							disease=disease+"\n"+line;
							line=br.readLine();

						}
						doc.add(new StringField("Disease", disease, Field.Store.YES));


					}
					else if (line.equals("*FIELD* CS")){
						String symptom ="";
						line=br.readLine();
						while (!line.contains("*FIELD*")){
							symptom=symptom+"\n"+line;
							line=br.readLine();
						}
						doc.add(new StringField("Symptom", symptom, Field.Store.YES));
						w.addDocument(doc);

					}


				}
			}
			br.close();
		}catch (Exception e){
			System.out.println(e.toString());
		}
	}

	public ArrayList<String> findDisease(String word, String field) throws ParseException, IOException{
	//	indexOmim();
		DirectoryReader ireader = DirectoryReader.open(ind);
		IndexSearcher isearch = new IndexSearcher(ireader);
		analyzer = new KeywordAnalyzer();



		Term term = new Term(field,"*"+word+"*");
		Query query = new WildcardQuery(term);

		int hitsPerPage=50;
		TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage,true);
		isearch.search(query,collector);
		ScoreDoc[] results= collector.topDocs().scoreDocs;
		ArrayList<String> res= new ArrayList<String>();

		for (int i=0; i<results.length;++i){

			int docId= results[i].doc;
			Document d= isearch.doc(docId);
			res.add(d.get("Disease"));
			disease = ConsoleDisplayResults.duplicate(res);

		}
		if(!disease.isEmpty()){
			DataBaseSources.addSourceOmim("Omimtxt");
		}


		return disease;
	}


}