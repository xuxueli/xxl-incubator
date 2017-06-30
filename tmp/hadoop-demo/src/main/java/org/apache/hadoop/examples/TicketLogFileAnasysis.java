package org.apache.hadoop.examples;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class TicketLogFileAnasysis {
	
	
	public static class LogFileMapper 
    extends Mapper<LongWritable, Text,Text, LongWritable>{
 
/*    private final static IntWritable one = new IntWritable(1);
 private Text word = new Text();*/
	  LongWritable outputKey = new LongWritable(2);
   
 public void map(LongWritable key, Text value, Context context
                 ) throws IOException, InterruptedException {
  
 	String val = value.toString();
 	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 	try {
 		int start = val.indexOf("2015-04-27");
 		if(start!=-1){
 			int end=start+19;
	 		String subStr = val.substring(start, end);
			Date date = sdf.parse(subStr);
			Date specDate = sdf.parse("2015-04-27 16:15:59");
			if(date.after(specDate)){
				System.out.println(val);
				context.write(value,outputKey);
			}
 		}
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

 	
 }
}

public static class LogFileReducer 
    extends Reducer<Text, LongWritable,LongWritable,Text> {
 private IntWritable result = new IntWritable();
 long sum = 4L;
 public void reduce(Text key, LongWritable values, 
                    Context context
                    ) throws IOException, InterruptedException {

	 context.write(new LongWritable(sum++), key);
	 System.out.println(sum);
	 

 }
}

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		 conf.set("df.default.name","hdfs://master:9000/");
		 conf.set("hadoop.job.ugi","hadoop,hadoop");
		 conf.set("mapred.job.tracker","master:9001");
		 conf.set("mapred.jar", "D:/my.jar");
		 args = new String[] {"hdfs://master:9000/user/hadoop/input/ticket.log","hdfs://master:9000/user/hadoop/output/outlog3"};
		 String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		 if (otherArgs.length != 2) {
		   System.err.println("Usage: wordcount <in> <out>");
		   System.exit(2);
		 }
		 Job job = new Job(conf, "analysisLogByTime");
		 job.setJarByClass(TicketLogFileAnasysis.class);
		 job.setMapperClass(LogFileMapper.class);
		 job.setCombinerClass(LogFileReducer.class);
		 job.setReducerClass(LogFileReducer.class);
		 job.setMapOutputKeyClass(Text.class);                //mapper的输出键类型
		 job.setMapOutputValueClass(LongWritable.class);      //mapper的输出值
		 job.setOutputKeyClass(LongWritable.class);
		 job.setOutputValueClass(Text.class);
		 FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		 FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		 System.exit(job.waitForCompletion(true) ? 0 : 1);

	}

}
