/*
SQLyog Professional v12.09 (64 bit)
MySQL - 8.0.28 : Database - blog
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`blog` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `blog`;

/*Table structure for table `article` */

DROP TABLE IF EXISTS `article`;

CREATE TABLE `article` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '文章编号',
  `title` varchar(256) NOT NULL COMMENT '文章标题',
  `content` longtext NOT NULL COMMENT '文章内容',
  `summary` varchar(1024) NOT NULL COMMENT '文章摘要',
  `category_id` bigint unsigned NOT NULL COMMENT '文章分类编号',
  `thumbnail` varchar(256) DEFAULT NULL COMMENT '缩略图',
  `is_top` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '是否置顶（0否 1是）',
  `status` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '文章状态（0正常 1草稿）',
  `view_count` int unsigned NOT NULL DEFAULT '0' COMMENT '文章访问量',
  `is_comment` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '是否允许评论（0否 1是）',
  `create_by` bigint unsigned NOT NULL DEFAULT '1' COMMENT '创建用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `fk_article_create_by` (`create_by`),
  KEY `fk_article_category_id` (`category_id`),
  CONSTRAINT `fk_article_category_id` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_article_create_by` FOREIGN KEY (`create_by`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `article` */

insert  into `article`(`id`,`title`,`content`,`summary`,`category_id`,`thumbnail`,`is_top`,`status`,`view_count`,`is_comment`,`create_by`,`create_time`,`update_time`,`is_deleted`) values (1,'Hello World','Welcome to [blog](https://github.com/shironekoa3/blog)!\n\nThis is your very first post. \n\nIf you get any problems when using blog, you can ask me on [GitHub](https://github.com/shironekoa3/blog/issues).','Welcome to [blog](https://github.com/shironekoa3/blog)!\n\nThis is your very first post. \n\nIf you get any problems when using blog, you can ask me on [GitHub](https://github.com/shironekoa3/blog/issues).',2,'',0,0,0,0,0,DEFAULT,DEFAULT,0);

/*Table structure for table `article_tag` */

DROP TABLE IF EXISTS `article_tag`;

CREATE TABLE `article_tag` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '映射编号',
  `article_id` bigint unsigned NOT NULL COMMENT '文章编号',
  `tag_id` bigint unsigned NOT NULL COMMENT '标签编号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `fk_article_tag_article_id` (`article_id`),
  KEY `fk_article_tag_tag_id` (`tag_id`),
  CONSTRAINT `fk_article_tag_article_id` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_article_tag_tag_id` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `article_tag` */

/*Table structure for table `category` */

DROP TABLE IF EXISTS `category`;

CREATE TABLE `category` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '分类编号',
  `name` varchar(128) NOT NULL COMMENT '分类名称',
  `description` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '默认分类描述' COMMENT '分类描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `category` */

insert  into `category`(`id`,`name`,`description`,`create_time`,`update_time`,`is_deleted`) values (1,'笔记','默认分类描述',DEFAULT,DEFAULT,0),(2,'教程','默认分类描述',DEFAULT,DEFAULT,0),(3,'文章','默认分类描述',DEFAULT,DEFAULT,0),(4,'日常','默认分类描述',DEFAULT,DEFAULT,0),(5,'图片','默认分类描述',DEFAULT,DEFAULT,0);

/*Table structure for table `comment` */

DROP TABLE IF EXISTS `comment`;

CREATE TABLE `comment` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '评论编号',
  `type` tinyint unsigned DEFAULT '0' COMMENT '评论类型（0文章 1友链）',
  `article_id` bigint unsigned NOT NULL COMMENT '评论文章编号',
  `root_id` bigint unsigned DEFAULT NULL COMMENT '评论父编号',
  `avatar` varchar(300) DEFAULT NULL COMMENT '评论者头像URL',
  `nick` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '评论者用户名',
  `email` varchar(100) DEFAULT NULL COMMENT '评论者邮箱',
  `website` varchar(200) DEFAULT NULL COMMENT '评论者网站',
  `content` varchar(512) NOT NULL COMMENT '评论内容',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `fk_comment_article` (`article_id`),
  KEY `fk_comment_root` (`root_id`),
  CONSTRAINT `fk_comment_article` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_comment_root` FOREIGN KEY (`root_id`) REFERENCES `comment` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `comment` */

/*Table structure for table `link` */

DROP TABLE IF EXISTS `link`;

CREATE TABLE `link` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '友链编号',
  `name` varchar(256) NOT NULL COMMENT '友链名称',
  `logo` varchar(256) DEFAULT NULL COMMENT '友链LOGO',
  `description` varchar(512) DEFAULT NULL COMMENT '友链描述',
  `address` varchar(128) NOT NULL COMMENT '友链地址',
  `status` tinyint unsigned NOT NULL COMMENT '友链状态（0正常 1未审核）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `link` */

/*Table structure for table `logininfor` */

DROP TABLE IF EXISTS `logininfor`;

CREATE TABLE `logininfor` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '编号',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `ipaddr` varchar(20) NOT NULL COMMENT 'IP地址',
  `location` varchar(50) DEFAULT NULL COMMENT '位置',
  `browser` varchar(50) DEFAULT NULL COMMENT '浏览器',
  `os` varchar(30) DEFAULT NULL COMMENT '系统',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否登出',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `logininfor` */

/*Table structure for table `option` */

DROP TABLE IF EXISTS `option`;

CREATE TABLE `option` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '配置编号',
  `key` varchar(256) NOT NULL COMMENT '配置项',
  `value` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '配置值',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `option` */

insert  into `option`(`id`,`key`,`value`,`create_time`,`update_time`,`is_deleted`) values (1,'title','向日葵的个人博客',DEFAULT,DEFAULT,0),(2,'description','这里是向日葵的个人博客~',DEFAULT,DEFAULT,0),(3,'headerImg','/images/global/background.webp',DEFAULT,DEFAULT,0),(4,'isHeaderHidden','false',DEFAULT,DEFAULT,0),(5,'author','向日葵',DEFAULT,DEFAULT,0),(6,'authorStatus','毕设项目ing...',DEFAULT,DEFAULT,0),(7,'notice','博客系统开发中...',DEFAULT,DEFAULT,0),(8,'avatar','/images/global/gravatar.jpg',DEFAULT,DEFAULT,0),(9,'viewCount','0',DEFAULT,DEFAULT,0),(10,'articleCount','0',DEFAULT,DEFAULT,0),(11,'nav','{\"首页 || home\":\"/\",\"分类 || category-management\":\"/\",\"关于 || like\":\"/\"}',DEFAULT,DEFAULT,0),(12,'footer','2020-2023丨备案号\n本站由 SpringBoot2 + Vue 3 开发\n作者：向日葵',DEFAULT,DEFAULT,0),(13,'link','http://127.0.0.1:5173',DEFAULT,DEFAULT,0),(14,'tagCount','0',DEFAULT,DEFAULT,0),(15,'categoryCount','1',DEFAULT,DEFAULT,0);

/*Table structure for table `tag` */

DROP TABLE IF EXISTS `tag`;

CREATE TABLE `tag` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '标签编号',
  `name` varchar(128) NOT NULL COMMENT '标签名称',
  `description` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '默认标签描述' COMMENT '标签描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `tag` */

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '用户编号',
  `username` varchar(20) NOT NULL COMMENT '用户名',
  `password` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户密码',
  `identity` varchar(50) NOT NULL COMMENT '用户身份',
  `nickname` varchar(50) DEFAULT NULL COMMENT '用户昵称',
  `email` varchar(100) DEFAULT NULL COMMENT '用户邮箱',
  `avatar` varchar(1024) DEFAULT NULL COMMENT '用户头像',
  `profile` varchar(1024) DEFAULT NULL COMMENT '用户简介',
  `status` tinyint unsigned DEFAULT NULL COMMENT '用户状态（0正常，1封禁）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
