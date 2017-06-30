package org.apache.hadoop.examples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class WordCount {

  public static class TokenizerMapper 
       extends Mapper<Object, Text, Text, IntWritable>{
    
    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();
      
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
     /* StringTokenizer itr = new StringTokenizer(value.toString());
      while (itr.hasMoreTokens()) {
        word.set(itr.nextToken());
        context.write(word, one);
      }*/
    	String val = value.toString();
    	System.out.println(val);
    	Text outKey = new Text(val);
    	IntWritable outVal = new IntWritable(1);
    	
    	context.write(outKey, outVal);
    }
  }
  
  public static class IntSumReducer 
       extends Reducer<Text,IntWritable,Text,IntWritable> {
    /*private IntWritable result = new IntWritable();

    public void reduce(Text key, Iterable<IntWritable> values, 
                       Context context
                       ) throws IOException, InterruptedException {
      int sum = 0;
      for (IntWritable val : values) {
        sum += val.get();
      }
      result.set(sum);
      context.write(key, result);
    }*/
	  private IntWritable result = new IntWritable();
	    int sum = 0;
	    public void reduce(Text key, IntWritable values, 
	                       Context context
	                       ) throws IOException, InterruptedException {
	      
	        sum += values.get();
	      result.set(sum);
	      context.write(key, result);
	    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    conf.set("df.default.name","hdfs://master:9000/");
    conf.set("hadoop.job.ugi","hadoop,hadoop");
    conf.set("mapred.job.tracker","master:9001");
    //args = new String[] {"hdfs://master:9000/test/access.log","hdfs://master:9000/home/out"};
    args = new String[] {"hdfs://master:9000/user/hadoop/input/ticket.log","hdfs://master:9000/user/hadoop/output/outlog2"};
    String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
    if (otherArgs.length != 2) {
      System.err.println("Usage: wordcount <in> <out>");
      System.exit(2);
    }
    Job job = new Job(conf, "analysis");
    job.setJarByClass(WordCount.class);
    job.setMapperClass(TokenizerMapper.class);
    job.setCombinerClass(IntSumReducer.class);
    job.setReducerClass(IntSumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
    FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
    
    FileSystem fs = FileSystem.get(conf);
    Path path = new Path("");
    
    //readAllFile(fs, path);
	
    
  }

  private static void readAllFile(FileSystem fs, Path path) throws IOException {
	  	if(fs.exists(path)){
    	    FileStatus[] list = fs.listStatus(path );
    	
	    	for(FileStatus status: list){
	    		if(status.isDir()){
	    			Path dir = status.getPath();
	    			readAllFile(fs,dir);
	    		}else{
	    			FSDataInputStream fds = fs.open(status.getPath());
	    			BufferedReader br = new BufferedReader(new InputStreamReader(fds,"utf-8"));
	    			String line ;
	    			while(null != (line = br.readLine())){
	    				StringUtils.isNotEmpty(line);
	    			}
	    		}
	    	}
    	}
    }
}
