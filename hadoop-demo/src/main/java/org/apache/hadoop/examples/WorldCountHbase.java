package org.apache.hadoop.examples;

import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;


public class WorldCountHbase {


	
	public static class Map extends Mapper<LongWritable, Text, Text, IntWritable>{
		
		private final static IntWritable one = new IntWritable(1);
		private Text text = new Text();
		
		public void map(LongWritable key, Text value, Context context
                ) throws IOException, InterruptedException {
			//StringTokenizer默认会将每一行文本分割成一个个单词
			StringTokenizer token = new StringTokenizer(value.toString());
			
			while(token.hasMoreTokens()){
				text.set(token.nextToken());
				context.write(text, one);
			}
		}
		
	}
	
	public static class Reduce extends TableReducer<Text, IntWritable,NullWritable>{
		
		 public void reduce(Text key, Iterable<IntWritable> values, 
                 Context context
                 ) throws IOException, InterruptedException {
			 int sum = 0;
			 
			 Iterator<IntWritable> iterator = values.iterator();
			 
			 while(iterator.hasNext()){
				 sum += iterator.next().get();
			 }
		 
			 
			 Put put = new Put(Bytes.toBytes(key.toString()));
			 put.add(Bytes.toBytes("content"), Bytes.toBytes("count"), Bytes.toBytes(sum+""));
			 
			 context.write(NullWritable.get(),put);
		 }
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		HBaseJavaApi api = new HBaseJavaApi();
		
		try {
			api.createTable("worldcount", new String[]{"content"});
			
			Configuration conf = new Configuration();
			conf.set("mapred.job.tracker","master:9001");
			conf.set("hbase.zookeeper.quorum", "slave1,slave2,slave3");
			conf.set("hbase.zookeeper.property.cliebtPort","2181");
			conf.set(TableOutputFormat.OUTPUT_TABLE, "worldcount");
			conf.set("mapred.jar", "D:/my.jar");
			
			Job job = new Job(conf, "worldCount");
			job.setJarByClass(WorldCountHbase.class);
			
			job.setMapperClass(Map.class);
			job.setReducerClass(Reduce.class);
			
			job.setMapOutputKeyClass(Text.class);
			job.setMapOutputValueClass(IntWritable.class);
			
			job.setInputFormatClass(org.apache.hadoop.mapreduce.lib.input.TextInputFormat.class);
			job.setOutputFormatClass(TableOutputFormat.class);
			
			FileInputFormat.addInputPath(job, new Path("hdfs://master:9000/hbaseinput"));
			System.exit(job.waitForCompletion(true)?0:1);
		} catch (ZooKeeperConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


}
