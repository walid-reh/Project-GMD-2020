package indexations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import app.*;



public class HPO_obo {

	static final File INDEXEDoc = new File ("index/HPO_index");

	private String path;
	private static File file;
	private static Analyzer analyzer;
	static Directory ind;
	static ArrayList<String> meds;
	static ArrayList<String> medsCause;


	//we look for the file to index:
	public HPO_obo () throws IOException {
		path = "/home/walid/Bureau/gmd-project-final/ressources/databases/hp.obo";
		file = new File(path);
		analyzer = new StandardAnalyzer(Version.LUCENE_40);

		ind = FSDirectory.open(INDEXEDoc);
		meds = new ArrayList<String>();
		medsCause = new ArrayList<String>();

	}


	public void indexHPO() throws IOException {

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

			for(int i=0;i<34;i++){
				br.readLine();
			}
			String line = br.readLine();
			while(line!=null){
				if(!line.isEmpty()){

					if(line.substring(0, 6).equals("[Term]")){


						String id = "";
						String name = "";
						String synonyms = "";

						Document doc = new Document();

						//id:
						line =br.readLine();

						int i =0;
						while(line.charAt(i)!='H'){
							i = i+1;
						}

						id = line.substring(i);
						doc.add(new StringField("id", id, Field.Store.YES));

						line = br.readLine();

						if(line.substring(0, 4).equals("name")){
							name = line.substring(6);
							doc.add(new StringField("name", name, Field.Store.YES));
						}

						line = br.readLine();


						while(!line.isEmpty()   &&  !line.substring(0, 7).equals("synonym")){
							line = br.readLine();

						}


						//now, line contains the first synonym line

						if(!line.isEmpty()){
							while(line.substring(0, 7).equals("synonym")){
								int a=10;
								while(line.charAt(a)!='"'){
									a = a+1;
								}
								synonyms= synonyms   + line.substring(10,a) + "!!";
								line = br.readLine();
							}
						}

						doc.add(new StringField("synonyms", synonyms, Field.Store.YES));


						w.addDocument(doc);
					}
				}
				line = br.readLine();

			}

		}catch (Exception e){
			System.out.println(e.toString());
		}

	}

	public  ArrayList<String> getSynonyms(String sign) throws IOException{

	//	indexHPO();
		DirectoryReader ireader = DirectoryReader.open(ind);

		IndexSearcher isearch = new IndexSearcher(ireader);



		Term term = new Term("name","*"+sign+"*");

		Query query = new WildcardQuery(term);


		ScoreDoc[] hits;
		int hitsPerPage = 50;
		TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
		isearch.search(query, collector);
		hits = collector.topDocs().scoreDocs;
		if(hits.length != 0){
			ArrayList<String> synonyms = new ArrayList<String>();
			for(int i=0;i<hits.length;i++){
				int docId = hits[i].doc;
				Document d = isearch.doc(docId);
				synonyms.add(d.get("synonyms"));
				synonyms.add(d.get("name"));

				synonyms.add(sign);

			}
			ArrayList<String> synonymsDist = ConsoleDisplayResults.duplicate(synonyms);


			ArrayList<String> syns = new ArrayList<String>();
			int index = 0;
			boolean b = false;

			for(String s : synonymsDist){
				b = false;
				index = 0;

				for(int i=2;i<s.length()-1;i++){

					if(s.charAt(i)=='!' && s.charAt(i+1)=='!' ){

						syns.add(s.substring(index, i));

						index = i+2;
						b = true;
					}
				}
				if(b==false){
					syns.add(s);
				}
			}

			return syns;
		}
		else{

			Term term2 = new Term("synonyms","*"+sign+"*");
			Query query2 = new WildcardQuery(term2);
			ScoreDoc[] hits2;
			int hitsPerPage2 = 50;
			TopScoreDocCollector collector2 = TopScoreDocCollector.create(hitsPerPage2, true);
			isearch.search(query2, collector2);
			hits2 = collector2.topDocs().scoreDocs;

			ArrayList<String> synonyms2 = new ArrayList<String>();
			for(int i=0;i<hits.length;i++){
				int docId2 = hits2[i].doc;
				Document d2 = isearch.doc(docId2);
				synonyms2.add(d2.get("name"));
				synonyms2.add(sign);

				synonyms2.add(d2.get("synonyms"));
			}
			ArrayList<String> synonymsDist2 = ConsoleDisplayResults.duplicate(synonyms2);


			ArrayList<String> syns2 = new ArrayList<String>();
			int index = 0;
			boolean b = false;
			for(String s : synonymsDist2){
				b = false;
				for(int i=2;i<s.length()-1;i++){
					if(s.charAt(i)=='!' && s.charAt(i+1)=='!' ){
						syns2.add(s.substring(index, i));
						index = i+2;
						b = true;
					}
				}
				if(b==false){
					syns2.add(s);
				}
			}

			return syns2;

		}
	}

	public String getID(String sign) throws IOException{

		// indexHPO();
		DirectoryReader ireader = DirectoryReader.open(ind);

		IndexSearcher isearch = new IndexSearcher(ireader);



		Term term = new Term("name","*"+sign+"*");

		Query query = new WildcardQuery(term);


		ScoreDoc[] hits;
		int hitsPerPage = 50;
		TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
		isearch.search(query, collector);
		hits = collector.topDocs().scoreDocs;

		if(hits.length != 0){
			String id ="";
			if(hits.length!=0){
				int docId = hits[0].doc;
				Document d = isearch.doc(docId);
				id = d.get("id");
				return id;
			}
			else{
				return null;
			}


		}
		else{
			Term term2 = new Term("synonyms","*"+sign+"*");
			Query query2 = new WildcardQuery(term2);
			ScoreDoc[] hits2;
			int hitsPerPage2 = 50;
			TopScoreDocCollector collector2 = TopScoreDocCollector.create(hitsPerPage2, true);
			isearch.search(query2, collector2);
			hits2 = collector2.topDocs().scoreDocs;
			String id2="";
			if(hits2.length!=0){
				int docId2 = hits2[0].doc;
				Document d2 = isearch.doc(docId2);
				id2 = d2.get("id");
				return id2;


			}
			else{
				return null;
			}

		}

	}


}