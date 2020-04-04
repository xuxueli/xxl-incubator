package com.xxl.groovy.core;

import com.xxl.glue.core.GlueFactory;
import com.xxl.glue.core.handler.GlueHandler;
import com.xxl.glue.core.loader.GlueLoader;

public class GlueFactoryTest {
	public static void main(String[] args) throws Exception {
		GlueFactory glueFactory = new GlueFactory();
		glueFactory.setGlueLoader(new GlueLoader() {
			@Override
			public String load(String name) {
				String classCode = "package com.xxl.groovy.core;"+
					"import com.xxl.glue.core.handler.GlueHandler;" +
					"public class DemoImpl implements GlueHandler {"+
						"public Object handle(Map<String, Object> params) {"+
							"return 999;"+
						"}"+
					"}"+
					";";
				return classCode;
			}
		});
		
		GlueHandler service = (GlueHandler) glueFactory.loadNewInstance("ssss");
		System.out.println(service);
		System.out.println(service.handle(null));
	}
}

