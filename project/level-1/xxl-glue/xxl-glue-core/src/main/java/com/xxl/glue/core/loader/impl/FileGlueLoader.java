package com.xxl.glue.core.loader.impl;

import com.xxl.glue.core.loader.GlueLoader;

import java.io.*;


/**
 * load glue in local file
 * @author xuxueli 2016-6-16 11:11:48
 */
public class FileGlueLoader implements GlueLoader {
	private static final String BASE_PATH = "config/glue";
	
	@Override
	public String load(String name) {
		// file name
		if (name == null || name.trim().length() == 0) {
			return null;
		}
		String glueFileName = name.concat(".groovy");
		
		try {
			// find path
			File basePath = new File(Thread.currentThread().getContextClassLoader().getResource(BASE_PATH).getPath());
			if (!basePath.exists()) {
				return null;
			}
			File glueFile = findGlueFile(basePath, glueFileName);
			if (glueFile.exists()) {
				return readGlueFile(glueFile);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * find file under base path
	 * @param basePath
	 * @param glueFileName
	 * @return
	 */
	private static File findGlueFile(File basePath, String glueFileName) {
		if (basePath.exists() && basePath.isDirectory()) {
			File[] files = basePath.listFiles();
			if (files.length == 0) {
				return null;
			} else {
				for (File file : files) {
					if (file.isDirectory()) {
						File childFile = findGlueFile(file, glueFileName);
						if (childFile!=null && glueFileName.equals(childFile.getName())){
							return childFile;
						}
					} else {
						if (glueFileName.equals(file.getName())) {
							return file;
						}
					}
				}
			}
		}
		return null;
	}
	
	private static String readGlueFile(File logFile){
		try {
			InputStream ins = null;
			BufferedReader reader = null;
			try {
				ins = new FileInputStream(logFile);
				reader = new BufferedReader(new InputStreamReader(ins, "utf-8"));
				if (reader != null) {
					String content = null;
					StringBuilder sb = new StringBuilder();
					while ((content = reader.readLine()) != null) {
						sb.append(content).append("\n");
					}
					return sb.toString();
				}
			} finally {
				if (ins != null) {
					try {
						ins.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
