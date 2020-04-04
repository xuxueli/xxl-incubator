/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50542
Source Host           : localhost:3306
Source Database       : 5i_activiti

Target Server Type    : MYSQL
Target Server Version : 50542
File Encoding         : 65001

Date: 2015-06-30 19:44:46
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for dev_task
-- ----------------------------
DROP TABLE IF EXISTS `dev_task`;
CREATE TABLE `dev_task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `title` varchar(100) NOT NULL,
  `process_instance_id` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dev_task
-- ----------------------------

-- ----------------------------
-- Table structure for user_main
-- ----------------------------
DROP TABLE IF EXISTS `user_main`;
CREATE TABLE `user_main` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(100) NOT NULL,
  `password` varchar(50) NOT NULL,
  `real_name` varchar(50) NOT NULL,
  `role` varchar(50) NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_main
-- ----------------------------
INSERT INTO `user_main` VALUES ('9', '931591021@qq.com', 'e10adc3949ba59abbe56e057f20f883e', '开发者', 'dev');
INSERT INTO `user_main` VALUES ('10', 'test@qq.com', 'e10adc3949ba59abbe56e057f20f883e', '测试人', 'test');
INSERT INTO `user_main` VALUES ('11', 'audit@qq.com', 'e10adc3949ba59abbe56e057f20f883e', '审核人', 'audit');
