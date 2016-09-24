package com.xxl.search.client.es;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Elasticsearch 工具类
 * @author xuxueli 2016-3-26 16:48:29
 	<pre>

	《elasticsearch 文档》
		[官网下载发行版](https://www.elastic.co/downloads/elasticsearch)
		[github源码](https://github.com/elastic/elasticsearch/releases)
		[文档](https://github.com/elastic/elasticsearch/blob/master/docs/java-api/client.asciidoc)

	《elasticsearch 简介》 : A Distributed RESTful Search Engine
		Lucene可以说是当今最先进，最高效的全功能开源搜索引擎框架。但是**Lucene只是一个框架，要充分利用它的功能，需要使用JAVA，并且在程序中集成Lucene**。需要很多的学习了解，才能明白它是如何运行的，Lucene确实非常复杂。

		 **Elasticsearch是一个建立在全文搜索引擎 Apache Lucene™ 基础上的搜索引擎**。

		Elasticsearch使用Lucene作为内部引擎，但是在使用它做全文搜索时，只需要使用统一开发好的API即可，而不需要了解其背后复杂的Lucene的运行原理。

		当然Elasticsearch并不仅仅是Lucene这么简单，它不但包括了全文搜索功能，还可以进行以下工作:
		- 分布式实时文件存储，并将每一个字段都编入索引，使其可以被搜索。
		- 实时分析的分布式搜索引擎。(lucene必须事先跑完索引,才可以提供完整的搜索服务,有延迟)
		- 可以扩展到上百台服务器，处理PB级别的结构化或非结构化数据。
		- 这么多的功能被集成到一台服务器上，你可以轻松地通过客户端或者任何你喜欢的程序语言与ES的RESTful API进行交流。

		Elasticsearch的上手是非常简单的。它附带了很多非常合理的默认值，这让初学者很好地避免一上手就要面对复杂的理论，它安装好了就可以使用了，用很小的学习成本就可以变得很有生产力。

	《自动选举》
		- **master-slave**: elasticsearch集群一旦建立起来以后，会选举出一个master，其他都为slave节点。 但是具体操作的时候，每个节点都提供写和读的操作。就是说，你不论往哪个节点中做写操作，这个数据也会分配到集群上的所有节点中。
		- **replicate**: 这里有某个节点挂掉的情况，如果是slave节点挂掉了，那么首先关心，数据会不会丢呢？不会。如果你开启了replicate，那么这个数据一定在别的机器上是有备份的。别的节点上的备份分片会自动升格为这份分片数据的主分片。这里要注意的是这里会有一小段时间的yellow状态时间。
		- **防止脑裂(discovery.zen.minimum_master_nodes)**: 设置参数： discovery.zen.minimum_master_nodes 为3(超过一半的节点数)，那么当两个机房的连接断了之后，就会以大于等于3的机房的master为主，另外一个机房的节点就停止服务了。
		- **负载均航(RESTful API)** :对于自动服务这里不难看出，如果把节点直接暴露在外面，不管怎么切换master，必然会有单节点问题。所以一般我们会在可提供服务的节点前面加一个负载均衡。

	《自动发现：》
		elasticsearch的集群是内嵌自动发现功能的。
		意思就是说，你只需要在每个节点配置好了集群名称，节点名称，互相通信的节点会根据es自定义的服务发现协议去按照多播的方式来寻找网络上配置在同样集群内的节点。
		和其他的服务发现功能一样，es是支持多播（本机）和单播的。多播和单播的配置分别根据这几个参数（上吻参数“# 集群自动发现机制”）
		多播是需要看服务器是否支持的，由于其安全性，其实现在基本的云服务（比如阿里云）是不支持多播的，所以即使你开启了多播模式，你也仅仅只能找到本机上的节点。
		单播模式安全，也高效，但是缺点就是如果增加了一个新的机器的话，就需要每个节点上进行配置才生效了。

	《安装部署 Elasticsearch 》
		- 1、Elasticsearch安装：官网下载Zip安装包解压目录。执行 "/bin/elasticsearch.bat" 启动脚本, 然后访问：http://localhost:9200
		- 2、ES集群管理工具安装, H5编写: 切换bin目录下，cmd执行："plugin install mobz/elasticsearch-head"  , 然后访问：http://localhost:9200/_plugin/head/
		- 3、基本配置：修改 "/config/elasticsearch.yml" 文件如下：
			```
			# 配置集群的名字，为了能进行自动查找
			cluster.name: elasticsearch-cluster-centos
			# 配置当前节点的名字，当然每个节点的名字都应该是唯一的
			node.name: "es-node1"
			# 为节点之间的通信设置一个自定义端口(默认为9300)
			transport.tcp.port: 9300
			# 绑定host，0.0.0.0代表所有IP，为了安全考虑，建议设置为内网IP
			network.host: "127.0.0.1"
			# 对外提供http服务的端口，安全考虑，建议修改，不用默认的9200
			http.port: 9200
			# 表示这个节点是否可以充当主节点，这个节点是否充当数据节点,如果你的节点数目只有两个的话，为了防止脑裂的情况，需要手动设置主节点和数据节点。其他情况建议直接不设置，默认两个都为true.
			node.master: false
			node.data: true
			# 集群自动发现机制 (单机时,忽略)
			discovery.zen.ping.multicast.enabled: false        // 把组播的自动发现给关闭了，为了防止其他机器上的节点自动连入，默认是true
			discovery.zen.fd.ping_timeout: 100s                // 节点与节点之间的连接ping时长
			discovery.zen.ping.timeout: 100s				   // 设置集群中自动发现其它节点时ping连接超时时间，默认为3秒，对于比较差的网络环境可以高点的值来防止自动发现时出错
			discovery.zen.minimum_master_nodes: 2              // 推荐设置超过一半的节点数, 为了避免脑裂 (保证分裂后,只会有一个大脑保持存活,小脑kill掉) 。比如3个节点的集群，如果设置为2，那么当一台节点脱离后，不会自动成为master。
			discovery.zen.ping.unicast.hosts: ["127.0.0.1:9300"]     // 设置集群中master节点的初始列表，可以通过这些节点来自动发现新加入集群的节点。

			另外：在bin/elasticsearch里面增加两行：
			ES_HEAP_SIZE=4g
			MAX_OPEN_FILES=65535
			这两行设置了节点可以使用的内存数和最大打开的文件描述符数。
			```

	《索引文件结构》
		- Index：这是ES存储数据的地方，类似于关系数据库的database。
		- Document type：嗯，类似关系数据库的表，主要功能是将完全不同schema（这个概念以后会讲到，不急）的数据分开，一个index里面可以有若干个Document type。
		- Document：好吧，这个类似关系数据库的一行，在同一个Document type下面，每一Document都有一个唯一的ID作为区分；
		- Filed：类似关系数据库的某一列，这是ES数据存储的最小单位。
		- Cluster和Node：ES可以以单点或者集群方式运行，以一个整体对外提供search服务的所有节点组成cluster，组成这个cluster的各个节点叫做node。
		- shard：通常叫分片，这是ES提供分布式搜索的基础，其含义为将一个完整的index分成若干部分存储在相同或不同的节点上，这些组成index的部分就叫做shard。
		- Replica：和replication通常指的都是一回事，即index的冗余备份，可以用于防止数据丢失，或者用来做负载分担。

	</pre>
 */
public class ElasticsearchUtil {
	private static Logger logger = LogManager.getLogger();

	private static Client instance = getInstance();
    public static Client getInstance(){
    	if (instance==null) {
			try {
				Settings settings = Settings.settingsBuilder()
						.put("cluster.name", "xxl-search")		// 设置集群名称：默认是elasticsearch
						.put("client.transport.sniff", true)	// 客户端嗅探整个集群状态，把集群中的其他机器IP加入到客户端中
						.build();
				instance = TransportClient.builder().settings(settings).build()
						//.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("host02"), 9300));
						.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
			} catch (UnknownHostException e) {
				logger.error("", e);
				if (instance!=null) {
					instance.close();
				}
			}
		}
        return instance;
    }

    /**
     * 1、新建-索引库（如果已存在，将会覆盖）
	 *
     * @param index
     * @param type
     * @param id
     * @param source_json
     * @return
     * 		_index 		Index name
     * 		_type 		Type name
     * 		_id 		Document ID (generated or not)
     * 		_version 	Version (if it's the first time you index this document, you will get: 1)
     * 		isCreated	isCreated() is true if the document is a new one, false if it has been updated
     */
    public static IndexResponse prepareIndex(String index, String type, @Nullable String id, String source_json){
    	IndexResponse response = getInstance().prepareIndex(index, type, id).setSource(source_json).get();
    	return response;
    }
    
    /**
     * 2、删除-索引库：
	 *
     * @param index
     * @param type
     * @param id
     * @return
     */
    public static DeleteResponse prepareDelete(String index, String type, String id){
    	DeleteResponse response = getInstance().prepareDelete(index, type, id).get();
    	return response;
    }
    
    /**
	 * 更新
	 *
     * @param index
     * @param type
     * @param id
     * @param doc_json
     * @return
     */
	public static UpdateResponse prepareUpdate(String index, String type, String id, String doc_json) {
		UpdateResponse response = getInstance().prepareUpdate(index, type, id).setDoc(doc_json).get();
		return response;
	}
	
	/**
	 * update 更新/新增（已存在执行update，不存在执行insert + update）
	 *
	 * @param index
	 * @param type
	 * @param id
	 * @param source_json
	 * @param doc_json
	 * @return
	 */
	public static UpdateResponse update(String index, String type, String id, String source_json, String doc_json){
		IndexRequest indexRequest = new IndexRequest("index", "type", "1")
		        .source(source_json);
		UpdateRequest updateRequest = new UpdateRequest("index", "type", "1")
		        .doc(doc_json)
		        .upsert(indexRequest);
		try {
			return getInstance().update(updateRequest).get();
		} catch (InterruptedException e) {
			logger.error("", e);
		} catch (ExecutionException e) {
			logger.error("", e);
		}
		return null;
	}
    
    /**
     * prepareGet	ID查询
     * @param index
     * @param type
     * @param id
     */
    public static GetResponse prepareGet(String index, String type, String id){
    	GetResponse response = getInstance().prepareGet(index, type, id).setOperationThreaded(true).get();
    	return response;
    }
    
    /**
     * prepareMultiGet 多条件，ID查询
     * @param index
     * @param type
     * @param id
     * @param id2
     * @return
     */
	public static MultiGetResponse prepareMultiGet(String index, String type, String id, String id2) {
		MultiGetResponse multiGetItemResponses = getInstance().prepareMultiGet()
				.add(index, type, id)
				.add(index, type, id, id2)
				.get();
		for (MultiGetItemResponse itemResponse : multiGetItemResponses) {
			GetResponse response = itemResponse.getResponse();
			if (response.isExists()) {
				String json = response.getSourceAsString();
				System.out.println(json);
			}
		}
		return null;
	}
	
	
	public static SearchResponse prepareSearch(String index, String type){
    	SearchResponse response = getInstance().prepareSearch(index)
    	        .setTypes(type)
    	        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
    	        .setFrom(0).setSize(60).setExplain(true)
    	        .execute()
    	        .actionGet();
    	return response;
    }
	
    /**
     * prepareSearch TermQuery查询，全部匹配才返回结果
     * @param index_arr
     * @param type_arr
     * @return
     */
    public static SearchResponse prepareSearch(String[] index_arr, String[] type_arr){
    	
    	QueryBuilder queryBuilder = QueryBuilders.termQuery("name", "jack");
    	QueryBuilder postFilter = QueryBuilders.rangeQuery("age").from(12).to(18);
    	
    	SearchResponse response = getInstance().prepareSearch(index_arr)
    	        .setTypes(type_arr)
    	        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
    	        .setQuery(queryBuilder)
    	        .setPostFilter(postFilter)
    	        .setFrom(0).setSize(60).setExplain(true)
    	        .execute()
    	        .actionGet();
    	
    	return response;
    }
    
	public static void main(String[] args) {
    	String index = "demo-index";
    	String type = "user";
    	
    	if (true) {

    		Map<String, Object> map = new HashMap<String, Object>();
        	map.put("user","kimchy");
        	map.put("postDate",new Date());
        	map.put("message","trying out Elasticsearch");

			String source = JacksonUtil.writeValueAsString(map);
        	
        	// 创建索引
        	prepareIndex(index, type, null, source);
		}
    	
    	if (true) {
    		SearchResponse data = prepareSearch(index, type);
    		System.out.println(JacksonUtil.writeValueAsString(data));
    		//DeleteResponse response = ElasticsearchUtil.getInstance().prepareDelete(index, type, "i");
    		//System.out.println(JacksonUtil.writeValueAsString(response));
		}
    	
    	if (false) {
    		// 查询某个索引
    		GetResponse response = prepareGet(index, type, "12");
    		Map<String, Object> retMap = response.getSource();
    		System.out.println(JacksonUtil.writeValueAsString(retMap));
		}

    	if (false) {
    		// TermQuery查询，要求全部匹配才会返回结果
    		SearchResponse response = prepareSearch(index, type);
		}
    	
    	
	}
    
}
