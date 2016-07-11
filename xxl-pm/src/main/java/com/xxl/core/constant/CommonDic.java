package com.xxl.core.constant;

import com.xxl.core.result.ReturnT;


/**
 * 通用字典类 (静态对象只可以存在顶级类或者内部静态类,静态基本类型对象不限)
 * @author xuxueli
 */
public class CommonDic {
	
	/**
	 * activiti工作流字典
	 */
	public static class AvtivitiDic {
		
		// 流程变量：状态判断
		public static final String status = "status";
		// 流程变量：接收人
		public static final String assignee_to = "assignee_to";
		
		// 流程定义: dev_task
		public static final String dev_task = "dev_task";
		// 任务节点：dev_task
		public enum DevTaskDefinitionKey{
			dev("开发提测"),
			test("测试"),
			audit("审核");
			private String desc;
			private DevTaskDefinitionKey(String desc){
				this.desc = desc;
			}
			public String desc(){
				return this.desc;
			}
		}
	}
	
	/**
	 * Controller,通用视图
	 */
	public class CommonViewName {
		public static final String COMMON_RESULT = "common.result";			// 通用返回
		public static final String COMMON_EXCEPTION = "common.exception"; 	// 通用错误页
	}
	
	/**
	 * HttpApplication上下文
	 */
	public class HttpApplicationDic {
	}

	/**
	 * HttpSession上下文
	 */
	public class HttpSessionKeyDic {
	}
	
	/**
	 * 返回信息字典
	 */
	public static class ReturnTDic {
		public static final ReturnT<String> success = new ReturnT<String>();
	}
	
	/**
	 * 返回码
	 */
	public enum ReturnCodeEnum {
		SUCCESS("S"),
		FAIL("E");
		private String code;
		private ReturnCodeEnum(String code){
			this.code = code;
		}
		public String code(){
			return this.code;
		}
	}
	
}
