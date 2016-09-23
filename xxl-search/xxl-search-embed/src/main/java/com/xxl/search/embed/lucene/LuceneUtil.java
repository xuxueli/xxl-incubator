package com.xxl.search.embed.lucene;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 搜索小程序
 * @author xuxueli 2016-07-03 16:43:30
 */
public class LuceneUtil {
	private static Logger logger = LogManager.getLogger();


	// index path
	private static String INDEX_DIRECTORY = "/Users/xuxueli/Downloads/tmp/LuceneUtil";
	public static void setDirectory(String directory){
		INDEX_DIRECTORY = directory;
		destory();
		init();
	}

	private static Directory directory = null;
	private static IndexWriter indexWriter = null;
	private static void init() {
		if (indexWriter==null) {
			try {
				directory = new SimpleFSDirectory(Paths.get(INDEX_DIRECTORY));

				//Analyzer analyzer = new SmartChineseAnalyzer();
				Analyzer analyzer = new IKAnalyzer();
				IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
				indexWriter = new IndexWriter(directory, indexWriterConfig);
			} catch (IOException e) {
				logger.error("", e);
			}
		}
	}

	public static void destory() {
		try {
			if (indexWriter!=null) {
				if (indexWriter.isOpen()) {
					indexWriter.commit();
					indexWriter.close();
				}
				indexWriter = null;
			}
			if (directory!=null) {
				directory.close();
				directory = null;
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	/**
	 * 删除全部索引
	 * @throws Exception
	 */
	public static boolean deleteAll() {
		try {
			indexWriter.deleteAll();
			indexWriter.commit();
			return true;
		} catch (IOException e) {
			logger.error("", e);
		}
		return false;
	}

	/**
	 * 创建一条索引	(create or overwrite index)
	 * @throws Exception
	 */
	private static boolean addDocument(Document document) {
		try {
			indexWriter.addDocument(document);
			indexWriter.commit();
			return true;
		} catch (IOException e) {
			logger.error("", e);
		}
		return false;
	}

	/**
	 * 索引查询
	 * @throws Exception
	 */
	private static LuceneSearchResult search(List<Query> queries, int offset, int pagesize) {
		LuceneSearchResult result = new LuceneSearchResult();

		try {
			// init query
			BooleanQuery.Builder booleanBuild = new BooleanQuery.Builder();	// Occur (MUST=与、SHOULD=或、MUST_OUT-非)
			if (queries!=null && queries.size()>0) {
				for (Query query: queries) {
					booleanBuild.add(query, BooleanClause.Occur.SHOULD);
				}
			}
			BooleanQuery booleanQuery = booleanBuild.build();

            // IndexReader
            IndexReader indexReader = DirectoryReader.open(directory);
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);

			// search
			TopDocs topDocs = indexSearcher.search(booleanQuery, offset+pagesize);
			if (offset > topDocs.totalHits) {
				return result;
			}
			ScoreDoc[] hits = topDocs.scoreDocs;
			result.setTotalHits(topDocs.totalHits);
			Integer end = Math.min(offset + pagesize, topDocs.totalHits);
			List<Document> list = new ArrayList<>();
			for (int i = offset; i < end; i++) {
				ScoreDoc hit = hits[i];
				Document hitDoc = indexSearcher.doc(hit.doc);
				list.add(hitDoc);
			}
			result.setDocuments(list);
			return result;
		} catch (Exception e) {
			logger.error("", e);
		}

		return result;
	}


	// -------------------- util --------------------
	public static class SearchDto{
		public static final String ID = "id";
		public static final String TITLE = "title";
		public static final String KEYWORD = "keywork";

		private int id;
		private String title;
		private String keywork;

		public SearchDto(int id, String title, String keywork) {
			this.id = id;
			this.title = title;
			this.keywork = keywork;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getKeywork() {
			return keywork;
		}

		public void setKeywork(String keywork) {
			this.keywork = keywork;
		}
	}

	public static void createIndex(List<SearchDto> list){
		// deleteAll
		deleteAll();

		// addDocument
		for (SearchDto searchDto: list) {
			Document doc = new Document();
			doc.add(new IntField(SearchDto.ID, searchDto.getId(), Field.Store.YES));
			doc.add(new StringField(SearchDto.TITLE, searchDto.getTitle(), Field.Store.YES));
			doc.add(new TextField(SearchDto.KEYWORD, searchDto.getKeywork(), Field.Store.YES));
			addDocument(doc);
		}
	}

	public static LuceneSearchResult queryIndex(String keyword, int offset, int pagesize){
		// 查询
		List<Query> querys = new ArrayList<>();
		if (keyword!=null && keyword.trim().length()>0) {
			try {
				//Analyzer analyzer = new SmartChineseAnalyzer();
				Analyzer analyzer = new IKAnalyzer();
				QueryParser parser = new QueryParser(SearchDto.KEYWORD, analyzer);
				Query shopNameQuery = parser.parse(keyword);
				querys.add(shopNameQuery);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		LuceneSearchResult result = search(querys, offset, pagesize);
		return result;
	}

	public static void main(String[] args) throws Exception {
		// 索引目录
		LuceneUtil.setDirectory("/Users/xuxueli/Downloads/tmp/search_fs");

		// query
		LuceneSearchResult result = queryIndex("分词", 0, 20);
		for (Document item: result.getDocuments()) {
			System.out.println(item);
		}
	}

	/*public static void main(String[] args) throws Exception {
		// 索引目录
		LuceneUtil.setDirectory("/Users/xuxueli/Downloads/tmp/LuceneUtil");

		// create
		List<SearchDto> list = new ArrayList<>();
		for (int i = 1; i <= 30; i++) {
			list.add(new SearchDto(i, "标题"+i, "关键字"+i));
		}

		createIndex(list);

		// query
		LuceneSearchResult result = queryIndex("关键字", 0, 20);
		for (Document item: result.getDocuments()) {
			System.out.println(item);
		}
	}*/

}
