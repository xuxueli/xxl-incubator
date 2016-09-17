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
 *
 * @author xuxueli 2016-3-26 16:48:29
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
