package com.xxl.util.core.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TableInjectUtil {
	private static transient Logger logger = LoggerFactory.getLogger(TableInjectUtil.class);
	private static final String FILE_NAME = "config.txt";
	static {
		fresh();
	}
	public static void fresh(){
		init();
	}
	private static void init() {
		InputStream ins = null;
		BufferedReader reader = null;
		try {
			File file = new File(TableInjectUtil.class.getClassLoader().getResource(FILE_NAME).getPath());
			ins = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(ins, "UTF-8"));
			if (reader != null) {
				table = new ArrayList<TableInjectUtil.RowData>();
				String content = null;
				while ((content = reader.readLine()) != null) {
					if (content != null && content.trim().length() > 0 && content.indexOf("#") == -1) {
						RowData rowData = new RowData();
						Field[] filedArr = rowData.getClass().getDeclaredFields();
						String[] valueArr = content.split("\t");
						if (filedArr.length == valueArr.length) {
							for (int i = 0; i < filedArr.length; i++) {
								filedArr[i].setAccessible(true);
								try {
									if (filedArr[i].getType() == int.class || filedArr[i].getType() == Integer.class) {
										filedArr[i].set(rowData, Integer.parseInt(valueArr[i]));
									} else if (filedArr[i].getType() == float.class || filedArr[i].getType() == Float.class) {
										filedArr[i].set(rowData, Float.parseFloat(valueArr[i]));
									} else {
										filedArr[i].set(rowData, valueArr[i]);
									}
								} catch (IllegalArgumentException e) {
									logger.error("[TableInjectUtil init field value illegal argument exception.]", e);
								} catch (IllegalAccessException e) {
									logger.error("[TableInjectUtil init field value illegal access exception.]", e);
								}
							}
						}
						table.add(rowData);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ins != null) {
				try {
					ins.close();
				} catch (IOException e) {
					logger.error("[TableInjectUtil load lobby.wheel.config.txt io exception...]", e);
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					logger.error("[TableInjectUtil load lobby.wheel.config.txt io exception...]", e);
				}
			}
		}
		
	}
	
	public static List<RowData> table;
	public static class RowData{
		private int type;
		private int rate;
		private int priceType;
		private int priceNum;
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		public int getRate() {
			return rate;
		}
		public void setRate(int rate) {
			this.rate = rate;
		}
		public int getPriceType() {
			return priceType;
		}
		public void setPriceType(int priceType) {
			this.priceType = priceType;
		}
		public int getPriceNum() {
			return priceNum;
		}
		public void setPriceNum(int priceNum) {
			this.priceNum = priceNum;
		}
		@Override
		public String toString() {
			return "RowData [type=" + type + ", rate=" + rate + ", priceType=" + priceType + ", priceNum=" + priceNum
					+ "]";
		}
	}
	
	public static void main(String[] args) {
		fresh();
		for (RowData row : table) {
			System.out.println(row);
		}
	}
	
}

