package com.xxl.util.core.skill.logappeng;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * store trigger log in each log-file
 *
	<appender name="xxl-job" class="com.xxl.job.core.log.XxlJobFileAppender">
		 <param name="filePath" value="/data/applogs/xxl-job/jobhandler/"/>
		 <!--<param name="append" value="true"/>-->
		 <!--<param name="encoding" value="UTF-8"/>-->
		 <layout class="org.apache.log4j.PatternLayout">
		 <param name="ConversionPattern" value="%-d{yyyy-MM-dd HH:mm:ss} xxl-job-executor-example [%c]-[%t]-[%M]-[%L]-[%p] %m%n"/>
		 </layout>
	</appender>

 * @author xuxueli 2016-3-12 19:25:12
 */
public class XxlJobFileAppender extends AppenderSkeleton {
	
	// for JobThread
	public static ThreadLocal<String> contextHolder = new ThreadLocal<String>();
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	// trogger log file path
	public static volatile String filePath;
	public void setFilePath(String filePath) {
		XxlJobFileAppender.filePath = filePath;
	}

	/**
	 * log filename: yyyy-MM-dd/9999.log
	 *
	 * @param triggerDate
	 * @param logId
	 * @return
	 */
	public static String makeLogFileName(Date triggerDate, int logId) {

        // filePath/
        File filePathDir = new File(filePath);
        if (!filePathDir.exists()) {
            filePathDir.mkdirs();
        }

        // filePath/yyyy-MM-dd/
        String nowFormat = sdf.format(new Date());
        File filePathDateDir = new File(filePathDir, nowFormat);
        if (!filePathDateDir.exists()) {
            filePathDateDir.mkdirs();
        }

        // filePath/yyyy-MM-dd/9999.log
		String logFileName = XxlJobFileAppender.sdf.format(triggerDate).concat("/").concat(String.valueOf(logId)).concat(".log");
		return logFileName;
	}

	@Override
	protected void append(LoggingEvent event) {

		String logFileName = contextHolder.get();
		if (logFileName==null || logFileName.trim().length()==0) {
			return;
		}
		File logFile = new File(filePath, logFileName);

		if (!logFile.exists()) {
			try {
				logFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
		
		// append file content
		try {
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(logFile, true);
				fos.write(layout.format(event).getBytes("utf-8"));
				if (layout.ignoresThrowable()) {
					String[] throwableInfo = event.getThrowableStrRep();
					if (throwableInfo != null) {
						for (int i = 0; i < throwableInfo.length; i++) {
							fos.write(throwableInfo[i].getBytes("utf-8"));
							fos.write(Layout.LINE_SEP.getBytes("utf-8"));
						}
					}
				}
				fos.flush();
			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean requiresLayout() {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * support read log-file
	 * @param logFileName
	 * @return log content
	 */
	public static LogResult readLog(String logFileName, int fromLineNum){

		// valid log file
		if (logFileName==null || logFileName.trim().length()==0) {
            return new LogResult(fromLineNum, 0, "readLog fail, logFile not found", true);
		}
		File logFile = new File(filePath, logFileName);

		if (!logFile.exists()) {
            return new LogResult(fromLineNum, 0, "readLog fail, logFile not exists", true);
		}

		// read file
		StringBuffer logContentBuffer = new StringBuffer();
		int toLineNum = 0;
		LineNumberReader reader = null;
		try {
			reader = new LineNumberReader(new FileReader(logFile));
			String line = null;

			while ((line = reader.readLine())!=null) {
				toLineNum = reader.getLineNumber();		// [from, to], start as 1
				if (toLineNum >= fromLineNum) {
					logContentBuffer.append(line).append("\n");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		// result
		LogResult logResult = new LogResult(fromLineNum, toLineNum, logContentBuffer.toString(), false);
		return logResult;

		/*
        // it will return the number of characters actually skipped
        reader.skip(Long.MAX_VALUE);
        int maxLineNum = reader.getLineNumber();
        maxLineNum++;	// 最大行号
        */
	}

	/**
	 * read log data
	 * @param logFile
	 * @return log line content
	 */
	public static String readLines(File logFile){
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(logFile), "utf-8"));
			if (reader != null) {
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line).append("\n");
				}
				return sb.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} 
		return null;
	}

	public static class LogResult implements Serializable {
		private static final long serialVersionUID = 42L;

		public LogResult(int fromLineNum, int toLineNum, String logContent, boolean isEnd) {
			this.fromLineNum = fromLineNum;
			this.toLineNum = toLineNum;
			this.logContent = logContent;
			this.isEnd = isEnd;
		}

		private int fromLineNum;
		private int toLineNum;
		private String logContent;
		private boolean isEnd;

		public int getFromLineNum() {
			return fromLineNum;
		}

		public void setFromLineNum(int fromLineNum) {
			this.fromLineNum = fromLineNum;
		}

		public int getToLineNum() {
			return toLineNum;
		}

		public void setToLineNum(int toLineNum) {
			this.toLineNum = toLineNum;
		}

		public String getLogContent() {
			return logContent;
		}

		public void setLogContent(String logContent) {
			this.logContent = logContent;
		}

		public boolean isEnd() {
			return isEnd;
		}

		public void setEnd(boolean end) {
			isEnd = end;
		}
	}

}
