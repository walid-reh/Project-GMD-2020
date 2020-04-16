package indexations;

import java.io.*;
import java.util.*;
import java.nio.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import app.*;
import connexionsQueries.*;

public class Stitch {

	static final File INDEXEDoc = new File ("index/stitch_index");
	private String path;
	private static File file;
	private static Analyzer analyzer;
	static Directory ind;
	ArrayList<String> numATC;




	public Stitch() throws IOException {
		path = "/home/walid/Bureau/gmd-project-final/ressources/databases/chemical.sources.v5.0.tsv";
		file = new File(path);
		analyzer = new KeywordAnalyzer();
		ind = FSDirectory.open(INDEXEDoc);
		numATC = new ArrayList<String>();

	}



	public static void indexStitch() throws IOException {

		try {

			IndexWriterConfig configuration = new IndexWriterConfig(Version.LUCENE_40, analyzer);
			IndexWriter writer = new IndexWriter (ind, configuration );
			addDocument(writer);
			writer.close();


		}  catch (Exception e) {
			System.out.println(e.toString());
		}

	}


	public static void addDocument(IndexWriter writer) throws IOException {

		try {

			InputStream ips = new FileInputStream(file);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			for(int i=0;i<9;i++){
				br.readLine();
			}
			String line;
			while ((line= br.readLine()) != null){
				String[] fields;

				fields = line.split("\t");
				if (fields[2].equals("ATC")){

					Document doc = new Document();

					doc.add(new StringField("chemical", fields[0], Field.Store.NO ));
					doc.add(new StringField("alias", fields[1], Field.Store.NO ) );
					doc.add(new StringField("source", fields[3], Field.Store.YES));
					writer.addDocument(doc);
				}
				else{
					break;
				}



			}
			br.close();
		}catch (Exception e){
			System.out.println(e.toString());
		}
	}



	public ArrayList<String> find(String word, String field) throws ParseException, IOException{
	// 	indexStitch();
		DirectoryReader ireader = DirectoryReader.open(ind);
		IndexSearcher isearch = new IndexSearcher(ireader);
		analyzer = new KeywordAnalyzer();
		QueryParser parser = new QueryParser(Version.LUCENE_40, field, analyzer);
		parser.setLowercaseExpandedTerms(false);
		Query query = parser.parse(word);
		int hitsPerPage=50;
		TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage,true);
		isearch.search(query,collector);
		ScoreDoc[] results= collector.topDocs().scoreDocs;
		ArrayList<String> res= new ArrayList<String>();

		for (int i=0; i<results.length;++i){

			int docId= results[i].doc;
			Document d= isearch.doc(docId);
			res.add(d.get("source"));
			numATC = ConsoleDisplayResults.duplicate(res);

		}
		return numATC;
	}



	public ArrayList<String> findDouble(String word, String field, String word2, String field2) throws ParseException, IOException{ // prend en parametre un couple de CIDS
		// indexStitch();
		DirectoryReader ireader = DirectoryReader.open(ind);
		IndexSearcher isearch = new IndexSearcher(ireader);
		analyzer = new KeywordAnalyzer();

		String[] fields = {field, field2};
		String[] entries = {word, word2};
		BooleanClause.Occur[] clause = {BooleanClause.Occur.MUST, BooleanClause.Occur.MUST};
		Query query = MultiFieldQueryParser.parse(Version.LUCENE_40, entries, fields, clause, analyzer);


		int hitsPerPage=50;
		TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage,true);
		isearch.search(query,collector);
		ScoreDoc[] results= collector.topDocs().scoreDocs;
		ArrayList<String> res= new ArrayList<String>();

		for (int i=0; i<results.length;++i){

			int docId= results[i].doc;
			Document d= isearch.doc(docId);
			res.add(d.get("source"));
			numATC = ConsoleDisplayResults.duplicate(res);

		}
		return numATC;
	}

}