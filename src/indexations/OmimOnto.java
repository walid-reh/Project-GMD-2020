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

public class OmimOnto {

	static final File INDEXEDoc = new File ("index/OmimOnto_index");

	private String path;
	private static File file;
	private static Analyzer analyzer;
	static Directory ind;
	static ArrayList<String> cuilist;


	//we look for the file to index:
	public OmimOnto() throws IOException {
		path = "/home/walid/Bureau/gmd-project-final/ressources/databases/omim_onto.csv";
		file = new File(path);
		analyzer = new KeywordAnalyzer();
		ind = FSDirectory.open(INDEXEDoc);
		cuilist = new ArrayList<String>();

	}
	public static void indexOmimOno() throws IOException {
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
			String line;
			while ((line= br.readLine()) != null){
				String[] fields;

				fields = line.split(";");

				Document doc = new Document();

				doc.add(new StringField("label", fields[1], Field.Store.NO ) );
				doc.add(new StringField("cui", fields[5], Field.Store.YES));
				w.addDocument(doc);
				System.out.println(doc.getFields());




			}
			br.close();
		}catch (Exception e){
			System.out.println(e.toString());
		}
	}


	//Search step
	public static ArrayList<String> find(String word, String field) throws ParseException, IOException{

		// indexOmimOno();
		DirectoryReader ireader = DirectoryReader.open(ind);

		IndexSearcher isearch = new IndexSearcher(ireader);
		analyzer = new KeywordAnalyzer();
		QueryParser parser = new QueryParser(Version.LUCENE_40, field, analyzer);

		Term term = new Term(field,"*"+word+"*");
		Query query = new WildcardQuery(term);

		ScoreDoc[] hits;
		int hitsPerPage = 50;
		TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
		isearch.search(query, collector);
		hits = collector.topDocs().scoreDocs;


		ArrayList<String> cui = new ArrayList<String>();

		for (int i = 0; i < hits.length; ++i) {
			int docId = hits[i].doc;
			Document d = isearch.doc(docId);
			cui.add(d.get("cui"));
			cuilist = ConsoleDisplayResults.duplicate(cui);

		}

		return cuilist;

	}


}