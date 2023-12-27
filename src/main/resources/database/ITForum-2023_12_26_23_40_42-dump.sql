-- MySQL dump 10.13  Distrib 8.2.0, for Win64 (x86_64)
--
-- Host: 178.128.109.119    Database: ITForum
-- ------------------------------------------------------
-- Server version	8.0.35-0ubuntu0.20.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `authentications`
--

DROP TABLE IF EXISTS `authentications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `authentications` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `refresh_token` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  CONSTRAINT `authentications_users_username_fk` FOREIGN KEY (`username`) REFERENCES `users` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authentications`
--

LOCK TABLES `authentications` WRITE;
/*!40000 ALTER TABLE `authentications` DISABLE KEYS */;
INSERT INTO `authentications` VALUES (1,'user0002','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMDAwMiIsImlhdCI6MTcwMjc5NjE0MCwiZXhwIjoxNzA1Mzg4MTQwfQ.NHW7ZG4XUDI7WANcPSybvz0bRUwcIEKRLVKsu9wkrUs'),(2,'user0003','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMDAwMyIsImlhdCI6MTcwMjgwMTE0OCwiZXhwIjoxNzA1MzkzMTQ4fQ.okkINw5mS-Z7TB_aQw8-iE7BK-7AmnX-E-KXjeA2bx8'),(3,'user0001','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMDAwMSIsImlhdCI6MTcwMjkwNDczNCwiZXhwIjoxNzA1NDk2NzM0fQ.g2a0sUqdxJ27M_i7vbB1i9wvTRtPSwiaY-PIqyiGQTA'),(4,'user0004','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMDAwNCIsImlhdCI6MTcwMzQzMzM2NCwiZXhwIjoxNzA2MDI1MzY0fQ.zw_Xoku2pqu7oKJVhr2A2JrjA4aoSb5Sk8SHc1sO2Qs');
/*!40000 ALTER TABLE `authentications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bookmark_posts`
--

DROP TABLE IF EXISTS `bookmark_posts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bookmark_posts` (
  `bookmark_id` int NOT NULL,
  `target_id` int NOT NULL,
  `type` bit(1) NOT NULL,
  PRIMARY KEY (`bookmark_id`,`target_id`,`type`),
  CONSTRAINT `bookmark_posts_bookmarks_id_fk` FOREIGN KEY (`bookmark_id`) REFERENCES `bookmarks` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookmark_posts`
--

LOCK TABLES `bookmark_posts` WRITE;
/*!40000 ALTER TABLE `bookmark_posts` DISABLE KEYS */;
/*!40000 ALTER TABLE `bookmark_posts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bookmarks`
--

DROP TABLE IF EXISTS `bookmarks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bookmarks` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `bookmarks_pk` (`username`),
  CONSTRAINT `bookmarks_users_username_fk99` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookmarks`
--

LOCK TABLES `bookmarks` WRITE;
/*!40000 ALTER TABLE `bookmarks` DISABLE KEYS */;
/*!40000 ALTER TABLE `bookmarks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment_details`
--

DROP TABLE IF EXISTS `comment_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment_details` (
  `id` int NOT NULL AUTO_INCREMENT,
  `comment_id` int NOT NULL,
  `created_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` tinytext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `left` int NOT NULL,
  `right` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `comment_details_comments_id_fk` (`comment_id`),
  KEY `comment_details_users_username_fk` (`created_by`),
  CONSTRAINT `comment_details_comments_id_fk` FOREIGN KEY (`comment_id`) REFERENCES `comments` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `comment_details_users_username_fk` FOREIGN KEY (`created_by`) REFERENCES `users` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment_details`
--

LOCK TABLES `comment_details` WRITE;
/*!40000 ALTER TABLE `comment_details` DISABLE KEYS */;
INSERT INTO `comment_details` VALUES (3,1,'user0003','ttt','2023-12-26 12:56:29',1,2),(4,1,'user0003','sfasd!![Tux, the Linux mascot](http://localhost:8888/api/images/7.png)','2023-12-26 13:16:38',3,4);
/*!40000 ALTER TABLE `comment_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comments` (
  `id` int NOT NULL AUTO_INCREMENT,
  `target_id` int NOT NULL,
  `type` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comments`
--

LOCK TABLES `comments` WRITE;
/*!40000 ALTER TABLE `comments` DISABLE KEYS */;
INSERT INTO `comments` VALUES (1,24,_binary '\0'),(2,13,_binary '');
/*!40000 ALTER TABLE `comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `follows`
--

DROP TABLE IF EXISTS `follows`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `follows` (
  `follower` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `followed` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`follower`,`followed`),
  KEY `follows_users_username_fk` (`followed`),
  CONSTRAINT `follows_users_username_fk` FOREIGN KEY (`followed`) REFERENCES `users` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `follows_users_username_fk2` FOREIGN KEY (`follower`) REFERENCES `users` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `follows`
--

LOCK TABLES `follows` WRITE;
/*!40000 ALTER TABLE `follows` DISABLE KEYS */;
INSERT INTO `follows` VALUES ('user0003','user0002'),('user0001','user0003');
/*!40000 ALTER TABLE `follows` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `images`
--

DROP TABLE IF EXISTS `images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `images` (
  `id` int NOT NULL AUTO_INCREMENT,
  `extension` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` tinyint(1) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `images`
--

LOCK TABLES `images` WRITE;
/*!40000 ALTER TABLE `images` DISABLE KEYS */;
INSERT INTO `images` VALUES (1,'jpg',0,'2023-12-26 04:55:43'),(2,'jpg',0,'2023-12-26 04:57:33'),(3,'jpg',0,'2023-12-26 04:59:18'),(4,'jpg',0,'2023-12-26 13:13:07'),(5,'jpg',0,'2023-12-26 13:13:21'),(6,'jpg',0,'2023-12-26 13:15:18'),(7,'png',1,'2023-12-26 13:15:52');
/*!40000 ALTER TABLE `images` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifications` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` tinytext NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_read` bit(1) NOT NULL DEFAULT b'0',
  `type` varchar(10) NOT NULL,
  `target_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `notifications_users_username_fk` (`username`),
  CONSTRAINT `notifications_users_username_fk` FOREIGN KEY (`username`) REFERENCES `users` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
/*!40000 ALTER TABLE `notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post_tags`
--

DROP TABLE IF EXISTS `post_tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post_tags` (
  `post_id` int NOT NULL,
  `tag_id` int NOT NULL,
  PRIMARY KEY (`post_id`,`tag_id`),
  KEY `post_tags_tags_id_fk` (`tag_id`),
  CONSTRAINT `post_tags_posts_id_fk` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `post_tags_tags_id_fk` FOREIGN KEY (`tag_id`) REFERENCES `tags` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_tags`
--

LOCK TABLES `post_tags` WRITE;
/*!40000 ALTER TABLE `post_tags` DISABLE KEYS */;
INSERT INTO `post_tags` VALUES (5,1),(6,1),(12,1),(14,1),(18,1),(21,1),(22,1),(23,1),(8,2),(9,2),(10,2),(14,2),(15,2),(24,2),(9,3),(10,3),(16,3),(17,3),(6,4),(15,4),(20,5),(21,6),(19,7),(22,8);
/*!40000 ALTER TABLE `post_tags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `posts`
--

DROP TABLE IF EXISTS `posts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `posts` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `content` tinytext COLLATE utf8mb4_unicode_ci NOT NULL,
  `score` int NOT NULL DEFAULT '0',
  `is_private` bit(1) NOT NULL DEFAULT b'0',
  `comment_count` int NOT NULL DEFAULT '0',
  `created_by` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `posts_users_username_fk` (`created_by`),
  CONSTRAINT `posts_users_username_fk` FOREIGN KEY (`created_by`) REFERENCES `users` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `posts`
--

LOCK TABLES `posts` WRITE;
/*!40000 ALTER TABLE `posts` DISABLE KEYS */;
INSERT INTO `posts` VALUES (5,'Cuộc sống thật là đẹp phải không?','Tôi là wjbu',0,_binary '\0',0,'user0003','2023-12-17 13:51:26'),(6,'Flutter có phù hợp cho phát triển ứng dụng Web?','Lag lắm',0,_binary '',0,'user0003','2023-12-17 13:53:55'),(8,'NodeJs for newbie','Là như này',0,_binary '',0,'user0003','2023-12-17 13:59:54'),(9,'Yêu em 3000','Tôi yêu người, người cứ gian dối',0,_binary '',0,'user0003','2023-12-20 17:39:53'),(10,'Yêu em 3000','Tôi yêu người, người cứ gian dối1',0,_binary '\0',0,'user0003','2023-12-20 18:59:23'),(12,'Đã bao lâu rồi không về?','Miền Trung thăm vợ con',0,_binary '\0',0,'user0003','2023-12-23 21:13:24'),(14,'Mery chitMet','Hello các bạn',0,_binary '\0',0,'user0001','2023-12-25 14:39:36'),(15,'Merry ChitMet 2','Hello các bạn 2',0,_binary '\0',0,'user0001','2023-12-25 14:43:44'),(16,'Merry ChitMet 3','Hello các bạn 3',0,_binary '\0',0,'user0002','2023-12-25 14:46:05'),(17,'Su ca na','Su ca na',0,_binary '\0',0,'user0002','2023-12-25 15:02:43'),(18,'OMG','OMG',12,_binary '\0',0,'user0002','2023-12-25 15:18:43'),(19,'Kiến trúc Client Server','Khách hàng và người phục vụ',0,_binary '\0',0,'user0003','2023-12-26 00:56:13'),(20,'Trình tự xử lý của 1 câu lệnh SQL','FROM -> JOIN -> WHERE -> SELECT -> GROUPBY -> SORT',0,_binary '',0,'user0003','2023-12-26 01:20:37'),(21,'Code Java nên dùng IDE nào?','Băn khoăn giữa việc chọn Jetbrain Intellij hay Eclipse',0,_binary '\0',0,'user0003','2023-12-26 01:23:49'),(22,'Ứng dụng Blockchain trong những bài toán nào?','Coin củng',0,_binary '\0',0,'user0003','2023-12-26 01:28:02'),(23,'Siuuuuuu','Siuuuu cua Ronaldo La Gi?',0,_binary '\0',0,'user0004','2023-12-26 10:15:59'),(24,'okkk','okkk',0,_binary '\0',2,'user0003','2023-12-26 10:51:26');
/*!40000 ALTER TABLE `posts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `series`
--

DROP TABLE IF EXISTS `series`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `series` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `content` tinytext NOT NULL,
  `score` int NOT NULL DEFAULT '0',
  `is_private` tinyint(1) NOT NULL DEFAULT '0',
  `comment_count` int NOT NULL DEFAULT '0',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `series_users_username_fk` (`created_by`),
  CONSTRAINT `series_users_username_fk` FOREIGN KEY (`created_by`) REFERENCES `users` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `series`
--

LOCK TABLES `series` WRITE;
/*!40000 ALTER TABLE `series` DISABLE KEYS */;
INSERT INTO `series` VALUES (1,'Những kẻ khờ mộng mơ','Là như này',0,0,0,'2023-12-17 17:33:43','user0003'),(2,'Những kẻ khờ mộng mơ 2','Là như này 2',0,1,0,'2023-12-17 17:33:59','user0003'),(4,'abc','123',0,0,0,'2023-12-20 19:56:44','user0003'),(5,'Hello Vietnam','Bad rabbits',0,0,0,'2023-12-23 17:01:06','user0003'),(6,'Hello','Vietnam',0,0,0,'2023-12-23 17:01:28','user0003'),(7,'123','123333',0,0,0,'2023-12-23 20:00:33','user0003'),(8,'ba','aa',0,1,0,'2023-12-23 20:08:04','user0003'),(10,'123','333',0,0,0,'2023-12-23 20:14:46','user0003'),(11,'123','3333',0,0,0,'2023-12-23 20:16:35','user0003'),(12,'we','123',0,0,0,'2023-12-23 20:22:48','user0003'),(13,'Series flutter','flutter ....',0,0,0,'2023-12-26 15:51:30','user0002');
/*!40000 ALTER TABLE `series` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `series_posts`
--

DROP TABLE IF EXISTS `series_posts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `series_posts` (
  `series_id` int NOT NULL,
  `post_id` int NOT NULL,
  PRIMARY KEY (`post_id`,`series_id`),
  KEY `series_posts_series_id_fk` (`series_id`),
  CONSTRAINT `series_posts_posts_id_fk` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`),
  CONSTRAINT `series_posts_series_id_fk` FOREIGN KEY (`series_id`) REFERENCES `series` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `series_posts`
--

LOCK TABLES `series_posts` WRITE;
/*!40000 ALTER TABLE `series_posts` DISABLE KEYS */;
INSERT INTO `series_posts` VALUES (1,5),(1,6),(4,5),(8,5),(13,17);
/*!40000 ALTER TABLE `series_posts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tags`
--

DROP TABLE IF EXISTS `tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tags` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `tags_pk` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tags`
--

LOCK TABLES `tags` WRITE;
/*!40000 ALTER TABLE `tags` DISABLE KEYS */;
INSERT INTO `tags` VALUES (1,'HoiDap','Câu hỏi/Thắc mắc cần được giải đáp'),(2,'NodeJs','Kiến thức/Câu hỏi về NodeJs'),(3,'ViecLam','Kinh nghiệm/Thắc mắc về thị trường tuyển dụng IT'),(4,'Flutter','Kiến thức/Câu hỏi về Flutter'),(5,'SQL','Kiến thức/Câu hỏi về SQL'),(6,'PhanMem','Chia sẻ/Bàn luận về các phần mềm trên máy tính'),(7,'Mang','Kiến thức/Chia sẻ về mạng'),(8,'Blockchain','Cập nhật kiến thức, công nghệ Blockchain');
/*!40000 ALTER TABLE `tags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `birthdate` date DEFAULT NULL,
  `display_name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `bio` tinytext COLLATE utf8mb4_unicode_ci,
  `gender` bit(1) DEFAULT NULL,
  `role` enum('ROLE_member','ROLE_admin') COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (6,'user0001','$2a$10$mypkPfsvYYpl/u.lcSIbPOTcI7KsW13s3N2XiCVHQdGaMkKTZP2iO','user0001@gmail.com',NULL,'Nguyễn Thành Quốc',NULL,NULL,'ROLE_member'),(7,'user0002','$2a$10$JtwVm/USNU.NWaBCivDFReTTfZqjY1g1FFO1xdjs9pnwmVM2m89MS','user0002@gmail.com',NULL,'Nguyễn Văn Lương',NULL,NULL,'ROLE_member'),(8,'user0003','$2a$10$9LA9OX9OpKsyWd9EYRGo8OQ9FTIH88AKmXvJ/eEry/hLhuyP8N7Ou','user0003@gmail.com',NULL,'Phạm Văn Vinh',NULL,NULL,'ROLE_admin'),(9,'user0004','$2a$10$h04jYDLZ8hk6oC4LgHrK1e6lc6yqRbTe1AyroUhwzqgBKuF5z7Xyq','user0004@gmail.com',NULL,'Tang Nhat Hung',NULL,NULL,'ROLE_member');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `votes`
--

DROP TABLE IF EXISTS `votes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `votes` (
  `target_id` int NOT NULL,
  `type` tinyint(1) NOT NULL,
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`target_id`,`username`,`type`),
  KEY `FK_VOTES_USERNAME` (`username`),
  CONSTRAINT `FK_VOTES_USERNAME` FOREIGN KEY (`username`) REFERENCES `users` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `votes`
--

LOCK TABLES `votes` WRITE;
/*!40000 ALTER TABLE `votes` DISABLE KEYS */;
/*!40000 ALTER TABLE `votes` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-12-26 23:40:59
