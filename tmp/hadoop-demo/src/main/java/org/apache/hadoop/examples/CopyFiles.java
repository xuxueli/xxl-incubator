package org.apache.hadoop.examples;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class CopyFiles {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		uploadFile("D://logs/ticket.log", "/user/hadoop/input/");
	}
	
	public static void uploadFile(String inpath,String outpath){
		Configuration conf = new Configuration();
		conf.set("df.default.name","hdfs://master:9000/");
	    conf.set("hadoop.job.ugi","hadoop,hadoop");
	    conf.set("mapred.job.tracker","master:9001");
	    try {
	    	FileSystem fs = FileSystem.get(conf);
	    	Path src = new Path(inpath);
	    	Path dest = new Path(outpath);
	    	fs.copyFromLocalFile(src, dest);
	    	System.out.println("upload to:"+conf.get("fs.default.name"));
	    	FileStatus[] status = fs.listStatus(dest);
	    	for (int i = 0; i < status.length; i++) {
				System.out.println(status[i].getPath());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
