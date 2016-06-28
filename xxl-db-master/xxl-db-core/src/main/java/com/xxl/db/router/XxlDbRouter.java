package com.xxl.db.router;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.Assert;

/**
 * xxl-db router datasource, for master-slave datasource, return LookUpKey of
 * datasources
 * 
 * @author xuxueli 2016-2-23 14:59:52
 */
public class XxlDbRouter extends AbstractRoutingDataSource {

	private static Logger logger = LoggerFactory.getLogger(XxlDbRouter.class);
	
	private static ThreadLocal<String> contextHolder = new ThreadLocal<String>();
	public enum DB_TYPE {
		WRITE, READ;
	};
	
	/**
	 * set db type
	 * @param type
	 */
	public static void setDbType(DB_TYPE type) {
		contextHolder.remove();
		contextHolder.set(type.name());
	}
	
	/**
	 * datasources, for read
	 */
	private String[] readDataSourcesIds; 
	
	@Override
	public void setTargetDataSources(Map<Object, Object> targetDataSources) {
		super.setTargetDataSources(targetDataSources);
		// datasources, for read
		if (targetDataSources!=null && targetDataSources.size()>0) {
			Object[] targetDataSourcesKeys = targetDataSources.keySet().toArray();
			readDataSourcesIds = new String[targetDataSourcesKeys.length];
			for (int i = 0; i < targetDataSourcesKeys.length; i++) {
				readDataSourcesIds[i] = (String) targetDataSourcesKeys[i];
			}
		}
	}
	
	@Override
	public void setDefaultTargetDataSource(Object defaultTargetDataSource) {
		super.setDefaultTargetDataSource(defaultTargetDataSource);
		// default datasource, for write
		Assert.notNull(defaultTargetDataSource, ">>>>>>>>>>> xxl-db, defaultTargetDataSource can not be null.");
	}
	
	/**
	 * counter for read datasources invoke
	 */
	private AtomicInteger readInvokeCounter = new AtomicInteger(0);
	
	@Override
	protected Object determineCurrentLookupKey() {
		String datasourceId = null;
		String db = contextHolder.get();
		if (db==null) {
			db = DB_TYPE.WRITE.name();
		}
		if (db.equals(DB_TYPE.READ.name())) {
			if (readDataSourcesIds!=null && readDataSourcesIds.length>0) {
				if (readInvokeCounter.intValue() > 10000) {
					readInvokeCounter.set(0);
				}
				// datasourceId = readDataSourcesIds[new Random().nextInt(readDataSourcesIds.length)];					// random
				datasourceId = readDataSourcesIds[readInvokeCounter.incrementAndGet()%readDataSourcesIds.length];		// rotation
			}
			logger.debug(">>>>>>>>>>> xxl-db router choose random read datasource, id = {}", datasourceId);
		} else {
			logger.debug(">>>>>>>>>>> xxl-db router choose default write datasource");
		}
		return datasourceId;
	}

}
