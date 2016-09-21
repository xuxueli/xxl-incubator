package com.xxl.search.client.lucene;

import com.xxl.search.client.es.JacksonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.BytesRefBuilder;
import org.apache.lucene.util.NumericUtils;

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
		- IntField 可定制积分排序等
		- StringField 不分词索引
		- TextField 分词索引
	《功能》
		- 1、清空索引
		- 2、新增一条索引:
			- IntField: int索引, 不分词。 可作为排序字段
			- StringField: string索引, 部分次
			- TextField: string索引, 可分词
			- 一个Field支持索引绑定多个值, 实现一对多索引List功能; 注意, 次数查询结果会出现多个重复的Field, 值不同;
		- 3、更新一条索引
		- 4、删除一条索引
		- 5、查询: (至少一个查询条件,如根据城市等, 至少一个排序条件,如时间戳等)
			- 精确查询, IntField/StringField;
			- 分词查询, TextField
			- 范围查询, 针对同一个Field支持重复设置query, SHOULD模式, 实现范围查询
			- 关联查询, 支持针对多个Filed, 设置query list, MUST模式, 实现关联查询
			- 分页
			- 排序
	</pre>
 */
public class LuceneUtil {
	private static Logger logger = LogManager.getLogger();

	// FieldType for IntField sort
	public static final FieldType INT_FIELD_TYPE_STORED_SORTED = new FieldType(IntField.TYPE_STORED);
	static {
		INT_FIELD_TYPE_STORED_SORTED.setDocValuesType(DocValuesType.NUMERIC);	// 设置排序value
		INT_FIELD_TYPE_STORED_SORTED.freeze();
	}


	// index path
	public static final String INDEX_DIRECTORY = "/Users/xuxueli/Downloads/tmp/LuceneUtil";

	private static Directory directory = null;
	private static IndexWriter indexWriter = null;
	private static SearcherManager searcherManager = null;
	static {	init();	}
	private static void init() {
		if (indexWriter==null || searcherManager==null) {
			try {
				directory = new SimpleFSDirectory(Paths.get(INDEX_DIRECTORY));

				Analyzer analyzer = new SmartChineseAnalyzer();
				IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
				indexWriter = new IndexWriter(directory, indexWriterConfig);

				searcherManager = new SearcherManager(indexWriter, false, new SearcherFactory());
				TrackingIndexWriter trackingIndexWriter = new TrackingIndexWriter(indexWriter);
				ControlledRealTimeReopenThread controlledRealTimeReopenThread = new ControlledRealTimeReopenThread<IndexSearcher>(trackingIndexWriter, searcherManager, 5.0, 0.025);
				controlledRealTimeReopenThread.setDaemon(true);//设为后台进程
				controlledRealTimeReopenThread.start();
			} catch (IOException e) {
				logger.error("", e);
			}
		}
	}

	private static void destory() {
		try {
			if (indexWriter!=null) {
				indexWriter.commit();
				indexWriter.close();
			}
			if (directory!=null) {
				directory.close();
			}
		} catch (IOException e) {
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
			init();
		}
		return false;
	}

	/**
	 * 创建一条索引	(create or overwrite index)
	 * @throws Exception
	 */
	public static boolean addDocument(Document document, boolean ifCommit) {
		try {
			indexWriter.addDocument(document);
			if (ifCommit) {
				indexWriter.commit();
			}
			return true;
		} catch (IOException e) {
			logger.error("", e);
			init();
		}
		return false;
	}
	public static boolean commitDocument() {
		try {
			indexWriter.commit();
			return true;
		} catch (IOException e) {
			logger.error("", e);
			init();
		}
		return false;
	}

	/**
	 * 更新一条索引	(更新一行,多条则更新最后一条,没有则新增)
	 * @param term
	 * @param document
     * @return
     */
	public static boolean updateDocument(Term term, Document document) {
		try {
			indexWriter.updateDocument(term, document);
			indexWriter.commit();
			return true;
		} catch (IOException e) {
			logger.error("", e);
			init();
		}
		return false;
	}

	/**
	 * 删除一条/多条索引
	 * @param terms
     */
	public static boolean deleteDocument(Term... terms){
		try {
			indexWriter.deleteDocuments(terms);	// Query... queries
			indexWriter.commit();
			return true;
		} catch (IOException e) {
			logger.error("", e);
			init();
		}
		return false;
	}

	/**
	 * 索引查询
	 * @throws Exception
	 */
	public static LuceneSearchResult search(List<Query> queries, Sort sort, int offset, int pagesize) {
		LuceneSearchResult result = new LuceneSearchResult();

		IndexSearcher indexSearcher = null;
		try {
			// init query
			BooleanQuery.Builder booleanBuild = booleanBuild = new BooleanQuery.Builder();	// Occur (MUST=与、SHOULD=或、MUST_OUT-非)
			if (queries!=null && queries.size()>0) {
				for (Query query: queries) {
					booleanBuild.add(query, BooleanClause.Occur.MUST);
					// new TermQuery(new Term("key", "value"));
					// NumericRangeQuery.newIntRange("key", value, value, true, true);
				}
			}
			BooleanQuery booleanQuery = booleanBuild.build();

			// TopFieldCollector
			TopFieldCollector topFieldCollector = TopFieldCollector.create(sort, offset+pagesize, false, false, false);

			// IndexSearcher
			searcherManager.maybeRefresh();
			indexSearcher =  searcherManager.acquire();

			// search
			indexSearcher.search(booleanQuery, topFieldCollector);

			// parse result
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

			/*TopDocs topDocs = searcher.search(booleanQuery, offset+pagesize, sort);
			ScoreDoc[] hits = topDocs.scoreDocs;
			if (offset > topDocs.totalHits) {
				throw new Exception("totalHits is less than start");
			}
			Integer end = Math.min(offset + pagesize, topDocs.totalHits);
			List<Document> list = new ArrayList<>();
			for (int i = offset; i < end; i++) {
				ScoreDoc hit = hits[i];
				Document hitDoc = searcher.doc(hit.doc);
				list.add(hitDoc);
			}
			return list;*/
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			try {
				// release
				searcherManager.release(indexSearcher);
			} catch (IOException e) {
				logger.error("", e);
			}
		}

		return result;
	}
	
	public static void main(String[] args) throws Exception {

		// deleteAll
		deleteAll();

		// addDocument
		for (int i = 0; i < 10; i++) {
			Document doc = new Document();
			doc.add(new IntField("id", i, Field.Store.YES));
			doc.add(new IntField("cityid", 1, Field.Store.YES));
			doc.add(new TextField("shopname", "文章内容"+i, Field.Store.YES));
			doc.add(new StringField("group", "group", Field.Store.YES));
			doc.add(new IntField("score", 5000+i, INT_FIELD_TYPE_STORED_SORTED));
			doc.add(new IntField("hotscore", 5000-i, INT_FIELD_TYPE_STORED_SORTED));
			addDocument(doc, true);
		}
		//commitDocument();

		// updateDocument
		Document doc = new Document();
		doc.add(new IntField("id", 1, Field.Store.YES));
		doc.add(new IntField("cityid", 1, Field.Store.YES));
		doc.add(new TextField("shopname", "asdfasdfasdf", Field.Store.YES));
		doc.add(new StringField("group", "group", Field.Store.YES));

		BytesRefBuilder bytes = new BytesRefBuilder();
		NumericUtils.intToPrefixCoded(1, 0, bytes);
		Term term = new Term("id", bytes);

		updateDocument(term, doc);

		// deleteDocument
		deleteDocument(term);

		// 查询
		List<Query> querys = new ArrayList<>();
		querys.add(NumericRangeQuery.newIntRange("cityid", 1, 1, true, true));
		querys.add(new TermQuery(new Term("group", "group")));

		Sort scoreSort = new Sort(new SortField("score", SortField.Type.INT, true));	// INT=按照int值排序
		Sort hotScoreSort = new Sort(new SortField("hotscore", SortField.Type.INT, true));

		LuceneSearchResult result = search(querys, scoreSort, 0, 20);
		for (Document item: result.getDocuments()) {
			System.out.println(item);
		}

	}

}
