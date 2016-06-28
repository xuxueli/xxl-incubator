package org.apache.hadoop.examples;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;

public class HBaseJavaApi {
	
	public static Configuration conf = null;
	static{
		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", "slave1,slave2,slave3");
		conf.set("hbase.zookeeper.property.cliebtPort","2181");
	}

	public void createTable(String tableName, String[] columnFamilies) throws Exception, ZooKeeperConnectionException{
		HBaseAdmin ad = new HBaseAdmin(conf);
		if(ad.tableExists(tableName)){ 
			System.out.println("table:"+tableName+" is existed already!");
			System.exit(0);
		}else{
			HTableDescriptor htd = new HTableDescriptor(tableName);
			for (String family : columnFamilies) {
				htd.addFamily(new HColumnDescriptor(family));
			}
			ad.createTable(htd);
			System.out.println("create table:"+tableName+" successfully!!");
		}
		
	}
	
	public void deleteTable(String tableName) throws Exception, ZooKeeperConnectionException{
		HBaseAdmin ad = new HBaseAdmin(conf);
		if(ad.tableExists(tableName)){
			ad.disableTableAsync(tableName);
			ad.deleteTable(tableName);
			System.out.println("delete table:"+tableName+" successfully!!");
		}else{
			System.out.println("the table:"+tableName+" was deleted unsuccessfully!!");
			System.exit(0);
		}
	}
	
	public void addRecord(String tableName, String row, 
			String columnFamily,String column,String value) throws Exception{
		HTable tab = new HTable(conf, tableName);
		Put put = new Put(row.getBytes());
		put.add(columnFamily.getBytes(), column.getBytes(), value.getBytes());
		tab.put(put );
	}
	
	public void getRowRecord(String tableName, String row) throws Exception{
		HTable tab = new HTable(conf, tableName);
		Get get = new Get(row.getBytes());
		Result result = tab.get(get);
		KeyValue[] raw = result.raw();
		for (KeyValue kv : raw) {
			System.out.print("行名："+new String(kv.getRow())+" ");
			System.out.print("时间戳："+kv.getTimestamp()+" ");
			System.out.print("列族："+new String(kv.getFamily())+" ");
			System.out.print("列名："+new String(kv.getQualifier())+" ");
			System.out.print("值："+new String(kv.getValue())+" ");
		}
	}
	
	public void getAllRecords(String tableName) throws Exception{
		HTable tab = new HTable(conf, tableName);
		Scan scan = new Scan();
		ResultScanner scanner = tab.getScanner(scan);
		for (Result result : scanner) {
			for (KeyValue kv : result.raw()) {
				System.out.print("行名："+new String(kv.getRow())+" ");
				System.out.print("时间戳："+kv.getTimestamp()+" ");
				System.out.print("列族："+new String(kv.getFamily())+" ");
				System.out.print("列名："+new String(kv.getQualifier())+" ");
				System.out.print("值："+new String(kv.getValue())+" ");
			}
		}
	}
	
	public void deleteRecord(String tableName,String row) throws Exception{
		HTable tab = new HTable(conf, tableName);
		Delete del = new Delete(row.getBytes());
		tab.delete(del);
	}
	
	public void deleteMultiRecords(String tableName,String[] rows) throws Exception{
		HTable tab = new HTable(conf, tableName);
		List<Delete> list = new ArrayList<Delete>();
		for (String row : rows) {
			list.add(new Delete(row.getBytes()));
		}
		tab.delete(list);
	}

}
