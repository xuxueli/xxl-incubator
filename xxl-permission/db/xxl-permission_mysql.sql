/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 50631
 Source Host           : 127.0.0.1
 Source Database       : xxl-permission

 Target Server Type    : MySQL
 Target Server Version : 50631
 File Encoding         : utf-8

 Date: 04/13/2017 19:27:45 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `xxl_permission_menu`
-- ----------------------------
DROP TABLE IF EXISTS `xxl_permission_menu`;
CREATE TABLE `xxl_permission_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `parent_id` int(11) NOT NULL COMMENT '父节点ID',
  `order` int(11) NOT NULL COMMENT '序号',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `permession_url` varchar(50) DEFAULT NULL COMMENT '权限URL',
  `permession_id` int(11) NOT NULL DEFAULT '0' COMMENT '权限ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `xxl_permission_role`
-- ----------------------------
DROP TABLE IF EXISTS `xxl_permission_role`;
CREATE TABLE `xxl_permission_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `xxl_permission_role_menu`
-- ----------------------------
DROP TABLE IF EXISTS `xxl_permission_role_menu`;
CREATE TABLE `xxl_permission_role_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL,
  `menu_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `xxl_permission_user`
-- ----------------------------
DROP TABLE IF EXISTS `xxl_permission_user`;
CREATE TABLE `xxl_permission_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `user_token` varchar(50) DEFAULT NULL,
  `real_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_name_unique` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=5014 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `xxl_permission_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `xxl_permission_user_role`;
CREATE TABLE `xxl_permission_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
