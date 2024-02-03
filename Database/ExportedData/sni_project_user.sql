CREATE DATABASE  IF NOT EXISTS `sni_project` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `sni_project`;
-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: localhost    Database: sni_project
-- ------------------------------------------------------
-- Server version	8.0.32

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `email` varchar(200) NOT NULL,
  `password` varchar(500) NOT NULL,
  `role` enum('ROLE_UNDEFINED','ROLE_ADMIN','ROLE_MODERATOR','ROLE_FORUM') DEFAULT NULL,
  `username` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ob8kqyqqgmefl0aco34akdtpe` (`email`),
  UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (2,_binary '','novica.tepic@student.etf.unibl.org','$2a$10$Oar2Rue9LNr0T9HLWOQjGeKiTTDLn1ZWdOejKZDbiLkdK8Gre8ZG.','ROLE_ADMIN','username1'),(3,_binary '','tepicnovic@gmail.com','$2a$10$Hbf7u/dOiVbeK/v.W4N27ekS7Knkb7kt.J4bTmboTQflCXzfxN60y','ROLE_MODERATOR','username2'),(5,_binary '','a@mail.com','$2a$10$sN/7qY1dyWHckCixjGDXCeDJ2eksRj/X2I4.kVMpWu2j3LDbH9WWW','ROLE_FORUM','a'),(9,_binary '','registracijanova@mail.com','$2a$10$Lkp2cy8Jo8mUmSjzWz5f8uAJuEMTxg.6Htggzf7UU7TfS2xdtx49m','ROLE_MODERATOR','registracijanova'),(12,_binary '','acivon2@outlook.com','$2a$10$oBKi5YlgCNggUMF/Fhdvvem7d4dQLPzD.g1WuRUyA4kIVwADhGeAW','ROLE_ADMIN','acivon'),(14,_binary '','acivon3@outlook.com','$2a$10$h2buBhNivYiyoDifAPzI7.QJt3QU7VYqkAflilTHn0e3fUEg2U4u.','ROLE_ADMIN','abcdefgh'),(17,_binary '','acivonsafsafa@outlook.com','github_user','ROLE_FORUM','novicatepic2123'),(21,_binary '','acivon4@outlook.com','github_user','ROLE_MODERATOR','novicatepic12'),(23,_binary '','acivon@outlook.com','github_user','ROLE_ADMIN','novicatepic'),(24,_binary '','tepicnovica@gmail.com','$2a$10$YIiq98JS/D86yjyv7N1TPeefRPO8QoPOqmqBv19CCzK06N/yBkFFe','ROLE_MODERATOR','novicanalog');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-02-02 14:10:49
