package com.xxl.search.client.lucene;

import com.xxl.search.client.es.JacksonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Lucene工具类 (文章标题/内容,分词索引)
 * @author xuxueli 2016-07-03 16:43:30
 *
 *
 	<pre>
	《索引文件分析》
		- Index: 类似数据库实例
			- 一个目录一个索引，在Lucene中一个索引是放在一个文件夹中的。
			- 同一文件夹中的所有的文件构成一个Lucene索引。
		- Segment: 类似数据库分表
			- 一个索引可以包含多个段，段与段之间是独立的，添加新文档可以生成新的段，不同的段可以合并。在建立索引的时候对性能影响最大的地方就是在将索引写入文件的时候, 所以在具体应用的时候就需要对此加以控制，段(Segment) 就是实现这种控制的。
			- 具有相同前缀文件的属同一个段，如文件两个段 "_0" 和 "_1"。
			- segments.gen和segments_5是段的元数据文件，也即它们保存了段的属性信息。
		- Document: 类似数据库行数据
			- 文档是我们建索引的基本单位，不同的文档是保存在不同的段中的，一个段可以包含多篇文档。
			- 新添加的文档是单独保存在一个新生成的段中，随着段的合并，不同的文档合并到同一个段中。
		- Field: 类似数据库列
			- 一篇文档包含不同类型的信息，可以分开索引，比如标题，时间，正文，作者等，都可以保存在不同的域里。
			- 不同域的索引方式可以不同。
		- Term:
			- 词是索引的最小单位，是经过词法分析和语言处理后的字符串。
	《Field区别》
		- StringField 不分词索引
		- TextField 分词索引
	</pre>
 */
public class LuceneUtil {
	private static Logger logger = LogManager.getLogger();

	// index path
	public static final String INDEX_DIRECTORY = "/Users/xuxueli/Downloads/tmp/LuceneUtil";

	/**
	 * 删除全部索引
	 * @throws Exception
	 */
	public static void deleteAll() throws Exception {
		long start = System.currentTimeMillis();
		logger.info("deleteAll start:{}", start);

		// init index writer
		Directory directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
		IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);

		// delete all index
		indexWriter.deleteAll();

		// writer close
		indexWriter.close();
		directory.close();
		long end = System.currentTimeMillis();
		logger.info("deleteAll end:{}, cost:{}", new Object[]{start, (end - start)});
	}

	/**
	 * 创建一条索引	(create or overwrite index)
	 * @throws Exception
	 */
	public static void createDocument(List<Field> fields) {
		if (fields==null || fields.size()<1) {
			return;
		}

		Directory directory = null;
		Analyzer analyzer = null;
		IndexWriter indexWriter = null;

		try {
			// init index writer
			directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));		// 1、index directory
			analyzer = new StandardAnalyzer();								// 2、analyzer (StandardAnalyzer)
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);	// 3、index writer config ( OpenMode.CREATE = create or overwrite index)
			indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
			indexWriter = new IndexWriter(directory, indexWriterConfig);// 4、index writer

			// write index
			Document doc = new Document();											// 5、write ducument index
			for (Field field: fields) {
				doc.add(field);
			}
			indexWriter.addDocument(doc);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// writer close
				indexWriter.commit();
				indexWriter.close();													// 5、writer close
				directory.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 删除一条索引
	 * @param term
     */
	public static void deleteDocument(Term term){
		if (term==null) {
			return;
		}

		Directory directory = null;
		Analyzer analyzer = null;
		IndexWriter indexWriter = null;
		try {
			// init index writer
			directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));
			analyzer = new StandardAnalyzer();
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
			indexWriter = new IndexWriter(directory, indexWriterConfig);

			// delete document by term
			indexWriter.deleteDocuments(term);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// writer close
				indexWriter.commit();
				indexWriter.close();
				directory.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 索引查询
	 * @throws Exception
	 */
	public static LuceneSearchResult search(List<Query> queries, Sort sort, int offset, int pagesize) {
		LuceneSearchResult result = new LuceneSearchResult();

		Directory directory = null;
		Analyzer analyzer = null;
		IndexReader indexReader = null;
		try {
			// init index reader
			directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));
			indexReader = DirectoryReader.open(directory);
			IndexSearcher indexSearcher = new IndexSearcher(indexReader);

			// init query
			BooleanQuery.Builder booleanBuild = booleanBuild = new BooleanQuery.Builder();	// Occur (MUST=与、SHOULD=或、MUST_OUT-非)
			if (queries!=null && queries.size()>0) {
				for (Query query: queries) {
					booleanBuild.add(query, BooleanClause.Occur.MUST);
				}
			}
			BooleanQuery booleanQuery = booleanBuild.build();

			// search
			TopFieldCollector topFieldCollector = TopFieldCollector.create(sort, offset+pagesize, false, false, false);

			indexSearcher.search(booleanQuery, topFieldCollector);
			ScoreDoc[] scoreDocs = topFieldCollector.topDocs(offset, pagesize).scoreDocs;
			logger.info(">>>>>>>>>>> search result, query:{}, result:{}", queries, JacksonUtil.writeValueAsString(topFieldCollector));

			result.setTotalHits(topFieldCollector.getTotalHits());
			if (scoreDocs!=null && scoreDocs.length > 0) {
				List<Document> documents = new ArrayList<>();
				for (int i = 0; i < scoreDocs.length; i++) {
					ScoreDoc scoreDoc = scoreDocs[i];
					Document document = indexSearcher.doc(scoreDoc.doc);

					documents.add(document);
				}
				result.setDocuments(documents);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// indexReader close
				indexReader.close();
				directory.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return result;
	}
	
	public static void main(String[] args) throws Exception {

		// 清空
		deleteAll();

		// 初始化
		for (int i = 1; i <= 100; i++) {
			List<Field> fields = new ArrayList<>();
			fields.add(new IntField("id", i, Store.YES));
			fields.add(new StringField("title", "标题"+i, Store.YES));
			fields.add(new TextField("content", "文章内容"+i, Store.YES));
			createDocument(fields);
		}

		// 查询
		List<Query> querys = new ArrayList<>();
		//querys.add(new TermQuery(new Term("title", "标题")));
		querys.add(new QueryParser("content", new StandardAnalyzer()).parse("文章内容"));

		Sort sort = new Sort(new SortField("id", SortField.Type.INT));

		LuceneSearchResult result = search(querys, sort, 0, 10);
		System.out.println(result);
	}


}
