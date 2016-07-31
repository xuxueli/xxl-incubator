package com.xxl.search.core.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.util.StringUtils;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Lucene工具类 (文章标题/内容,分词索引)
 * @author xuxueli 2016-07-03 16:43:30
 */
public class LuceneUtilAiticleSearch {
	private static Logger logger = LogManager.getLogger(LuceneUtilAiticleSearch.class.getName());

	// index path
	public static String IndexFile = PathUtil.webPath() + "LuceneUtilAiticleSearch";
	// index
	public enum IndexArticleField{
		/**
		 * 文章ID
         */
		id,
		/**
		 * 文章标题
         */
		title,
		/**
		 * 文章内容
         */
		content;
	}

	/**
	 * 创建索引文件
	 * @throws Exception
	 */
	public static void initArticleIndex() throws Exception {
		long start = System.currentTimeMillis();
		logger.info("创建索引 start:{}", start);
		
		// IndexWriter
		Directory dir = FSDirectory.open(Paths.get(IndexFile));	// 索引库 Directory
		Analyzer analyzer = new StandardAnalyzer();				// 分词器	// Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
		//indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
		//indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
		IndexWriter indexWriter = new IndexWriter(dir, indexWriterConfig);

		// IndexWriter init
		indexWriter.deleteAll();
		for (int i = 1; i <= 5000; i++) {
			Document doc = new Document();
			doc.add(new IntField(IndexArticleField.id.name(), i, Store.YES));					// 索引,文章ID
			doc.add(new StringField(IndexArticleField.title.name(), "标题" + i, Store.YES));		// 索引,文章标题		// StringField 不分词索引
			doc.add(new TextField(IndexArticleField.content.name(), "文章内容" + i, Store.YES));	// 索引,文章内容		// TextField 查询时, 需要分词索引
			indexWriter.addDocument(doc);
		}
		indexWriter.close();
		
		long end = System.currentTimeMillis();
		logger.info("创建索引 end:{}, cost:{}", new Object[]{start, (end - start)});
	}

	/**
	 * 索引查询
	 * @throws Exception
	 */
	public static List<String> search(String title, String content) throws Exception {
		List<String> result = new ArrayList<String>();
		if (!StringUtils.hasText(title) && !StringUtils.hasText(content)) {
			return result;
		}

		long start = System.currentTimeMillis();
		logger.info("查询索引 start:{}", start);
				
		// IndexSearcher
		Directory dir = FSDirectory.open(Paths.get(IndexFile));	// Directory
		IndexReader reader = DirectoryReader.open(dir);			// IndexReader
		IndexSearcher indexSearcher = new IndexSearcher(reader);

		// Query
		/*QueryParser parser = new QueryParser(IndexArticleField.content.name(), new StandardAnalyzer());
		Query query = parser.parse(content);*/

		BooleanQuery.Builder booleanBuild = new BooleanQuery.Builder();
		if (StringUtils.hasText(title)) {
			booleanBuild.add(new TermQuery(new Term(IndexArticleField.title.name(), title)), BooleanClause.Occur.MUST);		// MUST=与、SHOULD=或、MUST_OUT-非
		}
		if (StringUtils.hasText(content)) {
			booleanBuild.add(new QueryParser(IndexArticleField.content.name(), new StandardAnalyzer()).parse(content), BooleanClause.Occur.MUST);
		}
		BooleanQuery booleanQuery = booleanBuild.build();

		// search - ScoreDoc
		TopDocs topdocs = indexSearcher.search(booleanQuery, 100);
		ScoreDoc[] scoreDocs = topdocs.scoreDocs;
		
		logger.info("查询结果总数:" + topdocs.totalHits + ",最大的评分:" + topdocs.getMaxScore() + ", 耗时:" + (System.currentTimeMillis() - start));
		if (scoreDocs.length > 0) {
			for (int i = 0; i < scoreDocs.length; i++) {
				ScoreDoc scoreDoc = scoreDocs[i];
				Document document = indexSearcher.doc(scoreDoc.doc);
				logger.info("*********** scoreDoc: {}, document: {}", scoreDoc, document);
				result.add(document.get("content") + "<br>");
			}
		}
		
		// IndexReader close
		reader.close();
		
		long end = System.currentTimeMillis();
		logger.info("查询索引 end:{}, cost:{}", new Object[]{start, (end - start)});
		
		return result;
	}
	
	public static void main(String[] args) throws Exception {
		IndexFile = "LuceneUtilAiticleSearch";
		//initArticleIndex();

		List<String> result = search("标题99", "文章内容999");
		if (result!=null && result.size() > 0) {
			for (String item : result) {
				System.out.println(item);
			}
		} else {
			System.out.println("搜索结果为空");
		}
	}

}
