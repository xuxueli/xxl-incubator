/*
Navicat MySQL Data Transfer

Source Server         : meme-127.0.0.1
Source Server Version : 50544
Source Host           : 127.0.0.1:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50544
File Encoding         : 65001

Date: 2016-02-18 20:57:36
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `ID` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `category_name` varchar(200) CHARACTER SET utf8 NOT NULL,
  `post_id` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of category
-- ----------------------------

-- ----------------------------
-- Table structure for options
-- ----------------------------
DROP TABLE IF EXISTS `options`;
CREATE TABLE `options` (
  `ID` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `option_name` varchar(64) CHARACTER SET utf8 NOT NULL,
  `option_value` varchar(64) CHARACTER SET utf8 NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of options
-- ----------------------------
INSERT INTO `options` VALUES ('2', 'user', '21232f297a57a5a743894a0e4a801fc3');

-- ----------------------------
-- Table structure for posts
-- ----------------------------
DROP TABLE IF EXISTS `posts`;
CREATE TABLE `posts` (
  `ID` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `post_title` text CHARACTER SET utf8 NOT NULL,
  `post_content` longtext CHARACTER SET utf8 NOT NULL,
  `post_markdown` longtext CHARACTER SET utf8 NOT NULL,
  `post_date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of posts
-- ----------------------------
INSERT INTO `posts` VALUES ('12', 'Test', '<h1>Test</h1>\n\n<h2>Test</h2>\n\n<ul><li>test1</li><li>test2</li></ul>', '# Test\r\n\r\n## Test\r\n\r\n- test1\r\n- test2', '2015-06-19 13:52:24');
INSERT INTO `posts` VALUES ('13', 'Test', '<h1>Test</h1>\n\n<h2>Test</h2>\n\n<ul><li>test1</li><li>test2saaaaaa</li></ul>', '# Test\r\n\r\n## Test\r\n\r\n- test1\r\n- test2saaaaaa', '2015-06-19 13:52:33');
INSERT INTO `posts` VALUES ('11', 'markdown test', '<h1>这是标题</h1>\n\n<p>=======</p>\n\n<h3>这是小标题</h3>\n\n<blockquote><p>这是列表\n列表</p></blockquote>', '# 这是标题\r\n=======\r\n\r\n### 这是小标题\r\n\r\n> 这是列表\r\n> 列表', '2015-06-19 10:32:44');

-- ----------------------------
-- Table structure for tags
-- ----------------------------
DROP TABLE IF EXISTS `tags`;
CREATE TABLE `tags` (
  `ID` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `tags_name` varchar(200) CHARACTER SET utf8 NOT NULL,
  `post_id` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of tags
-- ----------------------------
