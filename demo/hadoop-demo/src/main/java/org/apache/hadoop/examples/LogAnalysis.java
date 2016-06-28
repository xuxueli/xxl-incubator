package org.apache.hadoop.examples;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class LogAnalysis {

  public static class TokenizerMapper 
       extends Mapper<Object, Text, Text, IntWritable>{
    
/*    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();*/
      
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
     
    	String val = value.toString();
    	System.out.println(val);
    	Text outKey = new Text(val);
    	IntWritable outVal = new IntWritable(1);
    	
    	context.write(outKey, outVal);

    	
    }
  }
  
  public static class IntSumReducer 
       extends Reducer<Text,IntWritable,Text,IntWritable> {
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
    conf.set("mapred.jar", "D://my.jar");
    args = new String[] {"hdfs://master:9000/user/hadoop/input/ticket.log","hdfs://master:9000/user/hadoop/outlog2"};
    String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
    if (otherArgs.length != 2) {
      System.err.println("Usage: wordcount <in> <out>");
      System.exit(2);
    }
    Job job = new Job(conf, "analysis");
    job.setJarByClass(LogAnalysis.class);
    job.setMapperClass(TokenizerMapper.class);
    job.setCombinerClass(IntSumReducer.class);
    job.setReducerClass(IntSumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
    FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
