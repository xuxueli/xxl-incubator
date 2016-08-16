package com.xxl.util.core.util;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientOptions.Builder;
import com.mongodb.WriteConcern;

/**
 * MongoDB工具类 Mongo实例代表了一个数据库连接池，即使在多线程的环境中，一个Mongo实例对我们来说已经足够了
 * 注意Mongo已经实现了连接池，并且是线程安全的
 * 设计为单例模式， 因 MongoDB的Java驱动是线程安全的，对于一般的应用，只要一个Mongo实例即可
 * Mongo有个内置的连接池（默认为10个） 对于有大量写和读的环境中，为了确保在一个Session中使用同一个DB时，
 * DB和DBCollection是绝对线程安全的
 *
 *
 *	 <!-- mongodb -->
	 <dependency>
		 <groupId>org.mongodb</groupId>
		 <artifactId>mongo-java-driver</artifactId>
		 <version>2.13.2</version>
	 </dependency>
 *
 * @author xuxueli 2015-7-16 20:00:18
 */
public class MongoDBUtil {
	private static Logger logger = LoggerFactory.getLogger(MongoDBUtil.class);
	public static String defauleDbName;	// 默认数据库名称
	
	/**
	 * 一个数据库链接池
	 */
	private static MongoClient client = getInstance();
	private static MongoClient getInstance(){
		if (client == null) {
			Properties prop = PropertiesUtil.loadProperties("mongodb.properties");
			String host = PropertiesUtil.getString(prop, "host");
			int port = PropertiesUtil.getInt(prop, "port");
			defauleDbName = "admin";
			try {
				client = new MongoClient(host, port);
			} catch (UnknownHostException e) {
				logger.info("{}", e);
			}
			
			// or, to connect to a replica set, with auto-discovery of the primary, supply a seed list of members
	        // List<ServerAddress> listHost = Arrays.asList(new ServerAddress("localhost", 27017),new ServerAddress("localhost", 27018));
	        // instance.mongoClient = new MongoClient(listHost);
			// 大部分用户使用mongodb都在安全内网下，但如果将mongodb设为安全验证模式，就需要在客户端提供用户名和密码：
			//boolean auth = client.authenticate(myUserName, myPassword);
			
			Builder options = new MongoClientOptions.Builder();
			options.connectionsPerHost(300);		// 连接池设置为300个连接,默认为100
	        options.connectTimeout(15000);			// 连接超时，推荐>3000毫秒
	        options.maxWaitTime(5000); 
	        options.socketTimeout(0);				// 套接字超时时间，0无限制
	        options.threadsAllowedToBlockForConnectionMultiplier(5000);// 线程队列数，如果连接线程排满了队列就会抛出 "Out of semaphores to get db"错误。
	        options.writeConcern(WriteConcern.SAFE);
	        options.build();
		}
		return client;
	}
	
	// 数据库实例操作------------------------------
	/**
	 * 获取DB实例 = 数据库实例
	 * 
	 * @param dbName
	 * @return
	 */
	public static DB getDB(String dbName) {
        if (dbName != null && !"".equals(dbName)) {
        	DB database = getInstance().getDB(dbName);
            return database;
        }
        return null;
    }
	
    /**
     * 查询链接下, 所有数据库实例名称
     * 
     * @return
     */
    public List<String> getAllDBNames() {
    	List<String> databaseNames = getInstance().getDatabaseNames();
        return databaseNames;
    }
    
    /**
     * 删除一个数据库实例
     */
    public void dropDB(String dbName) {
    	DB db = getDB(dbName);
    	if (db != null) {
    		db.dropDatabase();
    	}
    }
    
    /**
     * 查询DB下, 所有表名
     * 
     * @param dbName
     * @return
     */
    public Set<String> getAllCollections(String dbName) {
    	Set<String> colls = getDB(dbName).getCollectionNames();
        return colls;
    }
    
    // 表操作-----------------------------------------------
	/**
     * 获取DBCollection对象 = 表
     * 
     * @param collName
     * @return
     */
    public static DBCollection getCollection(String dbName, String collName) {
    	DB db = getDB(dbName);
    	if (db != null) {
    		DBCollection collection = db.getCollection(collName);
    		return collection;
    	}
        return null;
    }
    
    /**
     * 新增DBCollection对象 = 表
     * @param dbName
     * @param collName
     * @param options
     * @return
     */
    public static DBCollection addCollection(String dbName, String collName, DBObject options) {
    	DB db = getDB(dbName);
    	if (db != null) {
    		DBCollection collection = db.createCollection(collName, options);
    		return collection;
    	}
        return null;
    }
    
    // 数据操作---------------------------
    /**
     * 保存
     * @param dbName
     * @param dbObject
     */
    public static void save(String dbName, String collName, DBObject dbObject) {
    	DBCollection collection = getCollection(dbName, collName);
    	if (collection != null) {
    		collection.save(dbObject);
		}
    }
    
    /**
     * 删除
     * @param dbName
     * @param collName
     * @param dbObject
     */
    public static void delete(String dbName, String collName, DBObject dbObject) {
    	DBCollection collection = getCollection(dbName, collName);
    	if (collection != null) {
    		collection.remove(dbObject);
		}
    }
    
    // 此处不使用toArray()方法直接转换为List,是因为toArray()会把结果集直接存放在内存中，
    // 如果查询的结果集很大，并且在查询过程中某一条记录被修改了，就不能够反应到结果集中，从而造成"不可重复读"
    // 而游标是惰性获取数据
    /**
     * 条件查询, 列表, 限制pageSize
     * 
     * @param dbName
     * @param collName
     * @param query
     * @param fields
     * @param limit
     * @return
     */
    public static List<DBObject> find(String dbName, String collName, DBObject query, DBObject fields, int pageSize) {
        List<DBObject> list = new LinkedList<DBObject>();
        Cursor cursor = getCollection(dbName, collName).find(query, fields).limit(pageSize);
        while (cursor.hasNext()) {
            list.add(cursor.next());
        }
        return list.size() > 0 ? list : null;
    }
 
    /**
     * 条件查询, 分页数据
     * @param dbName
     * @param collName
     * @param query
     * @param fields
     * @param orderBy
     * @param pageNum
     * @param pageSize
     * @return
     */
    public static List<DBObject> find(String dbName, String collName, DBObject query, DBObject fields, DBObject orderBy, int pageNum, int pageSize) {
        List<DBObject> list = new ArrayList<DBObject>();
        Cursor cursor = getCollection(dbName, collName).find(query, fields).skip((pageNum - 1) * pageSize).limit(pageSize).sort(orderBy);
        while (cursor.hasNext()) {
            list.add(cursor.next());
        }
        return list.size() > 0 ? list : null;
    }
    
    /**
     * 分页查询,count
     * @param dbName
     * @param collName
     * @param query
     * @return
     */
    public static long count(String dbName, String collName, DBObject query) {
    	DBCollection collection = getCollection(dbName, collName);
    	if (collection != null) {
			return collection.count(query);
		}
        return -1;
    }
 
    /**
     * 条件查询, 单条
     * @param dbName
     * @param query
     * @param fields
     * @return
     */
    public static DBObject findOne(String dbName, String collName, DBObject query, DBObject fields) {
    	DBCollection collection = getCollection(dbName, collName);
    	if (collection != null) {
			return collection.findOne(query, fields);
		}
        return null;
    }
 
    /**
     * 更新
     * @param dbName
     * @param collName
     * @param query
     * @param update
     * @param upsert
     * @param multi
     */
    public static void update(String dbName, String collName, DBObject query, DBObject update, boolean upsert, boolean multi) {
    	getCollection(dbName, collName).update(query, update, upsert, multi);
    }
 
    /**
     * 查询出key字段,去除重复，返回值是{_id:value}形式的list
     * @param dbName
     * @param collName
     * @param key
     * @param query
     * @return
     */
    @SuppressWarnings("rawtypes")
	public List distinct(String dbName, String collName, String key, DBObject query) {
        return getCollection(dbName, collName).distinct(key, query);
    }
	
	public static void main(String[] args) {
		// 新建一张表
		DBCollection db = addCollection(defauleDbName, "test", null);
		System.out.println(db != null);

		// 新增一条记录
		DBObject dbObject = new BasicDBObject();
		dbObject.put("name", "jack");
		save(defauleDbName, "test", dbObject);
		
		// 查询该记录
		DBObject query = new BasicDBObject();
		query.put("name", "jack");
		DBObject fields = new BasicDBObject();
		DBObject result = findOne(defauleDbName, "test", query, fields);
		
		System.out.println(result!=null?result.get("name"):"查询失败");
		
	}
}
