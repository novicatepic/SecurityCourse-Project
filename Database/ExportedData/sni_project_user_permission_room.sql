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
-- Table structure for table `user_permission_room`
--

DROP TABLE IF EXISTS `user_permission_room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_permission_room` (
  `user_id` int NOT NULL,
  `room_id` int NOT NULL,
  `can_create` bit(1) NOT NULL,
  `can_delete` bit(1) NOT NULL,
  `can_update` bit(1) NOT NULL,
  PRIMARY KEY (`room_id`,`user_id`),
  KEY `FKseltinqdtkdqnuc41gbmncyrh` (`user_id`),
  CONSTRAINT `FKqoy8r4541nvlqxktg5jap2fj9` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`),
  CONSTRAINT `FKseltinqdtkdqnuc41gbmncyrh` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_permission_room`
--

LOCK TABLES `user_permission_room` WRITE;
/*!40000 ALTER TABLE `user_permission_room` DISABLE KEYS */;
INSERT INTO `user_permission_room` VALUES (2,1,_binary '',_binary '',_binary ''),(3,1,_binary '',_binary '',_binary ''),(5,1,_binary '',_binary '\0',_binary ''),(9,1,_binary '',_binary '',_binary ''),(12,1,_binary '\0',_binary '\0',_binary '\0'),(14,1,_binary '',_binary '',_binary ''),(17,1,_binary '',_binary '\0',_binary ''),(21,1,_binary '',_binary '\0',_binary ''),(23,1,_binary '',_binary '',_binary ''),(24,1,_binary '\0',_binary '\0',_binary '\0'),(2,2,_binary '',_binary '',_binary ''),(3,2,_binary '',_binary '',_binary ''),(5,2,_binary '',_binary '',_binary ''),(9,2,_binary '',_binary '',_binary ''),(12,2,_binary '\0',_binary '\0',_binary '\0'),(14,2,_binary '',_binary '',_binary ''),(17,2,_binary '',_binary '\0',_binary '\0'),(21,2,_binary '',_binary '',_binary ''),(23,2,_binary '',_binary '',_binary ''),(24,2,_binary '\0',_binary '\0',_binary '\0'),(2,3,_binary '',_binary '',_binary ''),(3,3,_binary '\0',_binary '\0',_binary '\0'),(5,3,_binary '\0',_binary '\0',_binary ''),(9,3,_binary '',_binary '',_binary ''),(12,3,_binary '\0',_binary '\0',_binary '\0'),(14,3,_binary '',_binary '',_binary ''),(17,3,_binary '\0',_binary '\0',_binary '\0'),(21,3,_binary '\0',_binary '\0',_binary '\0'),(23,3,_binary '',_binary '',_binary ''),(24,3,_binary '\0',_binary '\0',_binary '\0'),(2,4,_binary '',_binary '',_binary ''),(3,4,_binary '',_binary '',_binary ''),(5,4,_binary '',_binary '',_binary ''),(9,4,_binary '',_binary '',_binary ''),(12,4,_binary '\0',_binary '\0',_binary '\0'),(14,4,_binary '',_binary '',_binary ''),(17,4,_binary '\0',_binary '\0',_binary '\0'),(21,4,_binary '',_binary '\0',_binary '\0'),(23,4,_binary '',_binary '',_binary ''),(24,4,_binary '\0',_binary '\0',_binary '\0');
/*!40000 ALTER TABLE `user_permission_room` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-02-02 14:10:50
