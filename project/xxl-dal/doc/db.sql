/*
Navicat MySQL Data Transfer

Source Server         : meme-127.0.0.1
Source Server Version : 50544
Source Host           : 127.0.0.1:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50544
File Encoding         : 65001

Date: 2016-02-23 17:55:54
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for xxl_db_demo
-- ----------------------------
DROP TABLE IF EXISTS `xxl_db_demo`;
CREATE TABLE `xxl_db_demo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of xxl_db_demo
-- ----------------------------
INSERT INTO `xxl_db_demo` VALUES ('1', 'jack', '19');
