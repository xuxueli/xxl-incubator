
CREATE TABLE `xxl_glue_project` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '分组',
  `appname` varchar(100) NOT NULL COMMENT '项目AppName',
  `name` varchar(100) NOT NULL COMMENT '项目名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `xxl_glue_glueinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` int(11) NOT NULL COMMENT '项目ID',
  `name` varchar(128) NOT NULL,
  `about` varchar(128) NOT NULL,
  `source` text,
  `add_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `i_name` (`name`) USING BTREE,
  KEY `u_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `xxl_glue_codelog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `glue_id` int(11) NOT NULL COMMENT 'GLUE主键ID',
  `remark` varchar(128) NOT NULL,
  `source` text,
  `add_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO `xxl_glue_project` VALUES ('1', 'demo_project', '示例项目');

INSERT INTO `xxl_glue_glueinfo` VALUES ('1', '1', 'demo_project.DemoGlueHandler01', '示例场景01：托管 “配置信息”', 'package com.xxl.glue.example.handler;\n\nimport com.xxl.glue.core.handler.GlueHandler;\n\nimport java.util.HashSet;\nimport java.util.Map;\n\n/**\n * 示例场景01：托管 “配置信息”\n *\n * 优点：\n * 		1、在线编辑；推送更新；\n * 		2、该场景下，相较于同类型配置管理系统，支持数据类型更加丰富，不仅支持基础类型，甚至支持复杂对象；\n * 		3、该场景下，配置信息的操作和展示，更加直观；\n *\n * @author xuxueli 2016-4-14 15:36:37\n */\npublic class DemoGlueHandler01 implements GlueHandler {\n\n	@Override\n	public Object handle(Map<String, Object> params) {\n		\n		/*\n		// 【基础类型配置】，例如：活动开关、短信发送次数阀值、redis地址等；\n		boolean activitySwitch = true;													// 活动开关：true=开、false=关\n		int smsLimitCount = 3;															// 短信发送次数阀值\n		String brokerURL = \"failover:(tcp://127.0.0.1:61616,tcp://127.0.0.2:61616)\";	// redis地址等\n\n		// 【对象类型配置……】\n		*/\n\n		// 【列表配置】\n		HashSet<String> blackTelephones = new HashSet<String>();						// 手机号码黑名单列表\n		blackTelephones.add(\"15000000000\");\n		blackTelephones.add(\"15000000001\");\n		blackTelephones.add(\"15000000002\");\n\n		return blackTelephones;\n	}\n	\n}\n', '2017-05-31 22:39:32', '2017-05-31 23:00:50'), ('2', '1', 'demo_project.DemoGlueHandler02', '示例场景02：托管 “静态方法”', 'package com.xxl.glue.example.handler;\n\nimport com.xxl.glue.core.handler.GlueHandler;\n\nimport java.util.HashSet;\nimport java.util.Map;\n\n/**\n * 示例场景02：托管 “静态方法”\n *\n * 优点：\n * 		1、在线编辑；推送更新；\n * 		2、该场景下，托管公共组件，方便组件统一维护和升级；\n *\n * @author xuxueli 2016-4-14 16:07:03\n */\npublic class DemoGlueHandler02 implements GlueHandler {\n\n	// 手机号码黑名单列表\n	private static HashSet<String> blackTelephones = new HashSet<String>();\n	static {\n		blackTelephones.add(\"15000000000\");\n		blackTelephones.add(\"15000000001\");\n		blackTelephones.add(\"15000000002\");\n	}\n\n	/**\n	 * 手机号码黑名单校验Util\n	 *\n	 * @param telephone\n	 * @return\n	 */\n	private boolean isBlackTelephone(String telephone) {\n		if (telephone!=null && blackTelephones.contains(telephone)) {\n			return true;\n		}\n		return false;\n	}\n	\n	@Override\n	public Object handle(Map<String, Object> params) {\n		String telephone = (params!=null)? (String) params.get(\"telephone\") :null;\n		return isBlackTelephone(telephone);\n	}\n	\n}\n', '2017-05-31 22:39:47', '2017-05-31 23:02:20'), ('3', '1', 'demo_project.DemoGlueHandler03', '示例场景03：托管 “动态服务”', 'package com.xxl.glue.example.handler;\n\nimport com.xxl.glue.core.handler.GlueHandler;\n\nimport java.util.Map;\n\n/**\n * 示例场景03：托管 “动态服务”\n *\n * 优点：\n * 		1、在线编辑；推送更新；\n * 		2、该场景下，服务内部可以灵活组装和调用其他Service服务, 扩展服务的动态特性。\n *\n * @author xuxueli 2016-4-14 16:07:03\n */\npublic class DemoGlueHandler03 implements GlueHandler {\n	private static final String SHOPID = \"shopid\";\n	\n	/*\n	@Resource\n	private UserPhoneService userPhoneService;	// 手机号码黑名单Service，此处仅作为示例\n	*/\n	\n	/**\n	 * 商户黑名单判断\n	 */\n	@Override\n	public Object handle(Map<String, Object> params) {\n		\n		/*\n		String telephone = (params!=null)? (String) params.get(\"telephone\") :null;\n\n		boolean isBlackTelephone = userPhoneService.isBlackTelephone(telephone);\n		return isBlackTelephone;\n		*/\n		\n		return true;\n	}\n	\n}\n', '2017-05-31 22:40:03', '2017-05-31 23:03:09');
