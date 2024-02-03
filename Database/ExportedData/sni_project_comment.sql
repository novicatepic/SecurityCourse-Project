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
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `content` varchar(2000) NOT NULL,
  `date_created` date NOT NULL,
  `enabled` bit(1) NOT NULL,
  `forbidden` bit(1) NOT NULL,
  `room_id` int NOT NULL,
  `title` varchar(45) NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8kcum44fvpupyw6f5baccx25c` (`user_id`),
  CONSTRAINT `FK8kcum44fvpupyw6f5baccx25c` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (4,'APDEJT TEST SADRZAj','2024-01-01',_binary '',_binary '\0',1,'APDEJT TEST',3),(5,'Contentt456','2024-01-01',_binary '',_binary '\0',1,'Titlee4',3),(6,'Contentt5','2024-01-01',_binary '',_binary '\0',1,'Titlee5',3),(7,'Contentt6','2024-01-01',_binary '',_binary '\0',1,'Titlee6',3),(8,'Contentt7','2024-01-01',_binary '',_binary '\0',1,'Titlee7',3),(9,'Contentt8','2024-01-01',_binary '',_binary '\0',1,'Titlee8',3),(10,'Contentt9','2024-01-01',_binary '',_binary '\0',1,'Titlee9',3),(11,'Contentt10','2024-01-01',_binary '',_binary '\0',1,'Titlee10',3),(12,'Contentt11','2024-01-01',_binary '',_binary '\0',1,'Titlee11',3),(13,'Contentt12','2024-01-01',_binary '',_binary '\0',1,'Titlee12',3),(14,'Contentt13','2024-01-01',_binary '',_binary '\0',1,'Titlee13',3),(15,'Contentt14','2024-01-01',_binary '',_binary '\0',1,'Titlee14',3),(16,'Contentt15','2024-01-01',_binary '',_binary '\0',1,'Titlee15',3),(17,'Contentt16','2024-01-01',_binary '',_binary '\0',1,'Titlee16',3),(18,'Contentt17','2024-01-01',_binary '',_binary '\0',1,'Titlee17',3),(19,'Contentt18','2024-01-01',_binary '',_binary '\0',1,'Titlee18',3),(20,'Contentt19','2024-01-01',_binary '',_binary '\0',1,'Titlee19',3),(21,'Contentt20','2024-01-01',_binary '',_binary '\0',1,'Titlee20',3),(22,'proba','2024-01-03',_binary '',_binary '\0',1,'test',3),(23,'asgas!','2024-01-03',_binary '',_binary '\0',1,'asf',3),(24,'abccdef','2024-01-04',_binary '\0',_binary '',1,'commmm',3),(25,'avasasfsa','2024-01-04',_binary '\0',_binary '',1,'novi komentarr',3),(26,'12345678!','2024-01-04',_binary '',_binary '\0',1,'kommmmmm!',3),(29,'password111!','2024-01-31',_binary '\0',_binary '',1,'username1111 science has failed our mother ea',2),(30,'kulturno','2024-01-07',_binary '',_binary '\0',2,'kultura',2),(31,'efg','2024-01-07',_binary '',_binary '\0',2,'gcd',3),(33,'afasfsagaga ...','2024-01-07',_binary '',_binary '\0',1,'asfasf',2),(34,'Content Room id 2 1','2024-01-01',_binary '',_binary '\0',2,'Title Room id 2 1',3),(35,'Content Room id 2 2','2024-01-01',_binary '',_binary '\0',2,'Title Room id 2 2',3),(36,'Content Room id 2 3','2024-01-01',_binary '',_binary '\0',2,'Title Room id 2 3',3),(37,'Content Room id 2 4','2024-01-01',_binary '',_binary '\0',2,'Title Room id 2 4',3),(38,'Content Room id 2 5','2024-01-01',_binary '',_binary '\0',2,'Title Room id 2 5',3),(39,'Content Room id 2 6','2024-01-01',_binary '',_binary '\0',2,'Title Room id 2 6',3),(40,'Content Room id 2 7','2024-01-01',_binary '',_binary '\0',2,'Title Room id 2 7',3),(41,'Content Room id 2 8','2024-01-01',_binary '',_binary '\0',2,'Title Room id 2 8',3),(42,'Content Room id 2 9','2024-01-01',_binary '',_binary '\0',2,'Title Room id 2 9',3),(43,'Content Room id 2 10','2024-01-01',_binary '',_binary '\0',2,'Title Room id 2 10',3),(44,'Content Room id 2 11','2024-01-01',_binary '',_binary '\0',2,'Title Room id 2 11',3),(45,'Content Room id 2 12','2024-01-01',_binary '',_binary '\0',2,'Title Room id 2 12',3),(46,'Content Room id 2 13','2024-01-01',_binary '',_binary '\0',2,'Title Room id 2 13',3),(47,'Content Room id 2 14','2024-01-01',_binary '',_binary '\0',2,'Title Room id 2 14',3),(48,'Content Room id 2 15','2024-01-01',_binary '',_binary '\0',2,'Title Room id 2 15',3),(49,'Content Room id 2 16','2024-01-01',_binary '',_binary '\0',2,'Title Room id 2 16',3),(50,'Content Room id 2 17','2024-01-01',_binary '',_binary '\0',2,'Title Room id 2 17',3),(51,'Content Room id 2 18','2024-01-01',_binary '',_binary '\0',2,'Title Room id 2 18',3),(52,'Content Room id 2 19','2024-01-01',_binary '',_binary '\0',2,'Title Room id 2 19',3),(53,'Content Room id 2 20','2024-01-01',_binary '',_binary '\0',2,'Title Room id 2 20',3),(54,'Content Room id 3 1','2024-01-01',_binary '',_binary '\0',3,'Title Room id 3 1',3),(55,'Content Room id 3 2','2024-01-01',_binary '',_binary '\0',3,'Title Room id 3 2',3),(56,'Content Room id 3 3','2024-01-01',_binary '',_binary '\0',3,'Title Room id 3 3',3),(57,'Content Room id 3 4','2024-01-01',_binary '',_binary '\0',3,'Title Room id 3 4',3),(58,'Content Room id 3 5','2024-01-01',_binary '',_binary '\0',3,'Title Room id 3 5',3),(59,'Content Room id 3 6','2024-01-01',_binary '',_binary '\0',3,'Title Room id 3 6',3),(60,'Content Room id 3 7','2024-01-01',_binary '',_binary '\0',3,'Title Room id 3 7',3),(61,'Content Room id 3 8','2024-01-01',_binary '',_binary '\0',3,'Title Room id 3 8',3),(62,'Content Room id 3 9','2024-01-01',_binary '',_binary '\0',3,'Title Room id 3 9',3),(63,'Content Room id 3 10','2024-01-01',_binary '',_binary '\0',3,'Title Room id 3 10',3),(64,'Content Room id 3 11','2024-01-01',_binary '',_binary '\0',3,'Title Room id 3 11',3),(65,'Content Room id 3 12','2024-01-01',_binary '',_binary '\0',3,'Title Room id 3 12',3),(66,'Content Room id 3 13','2024-01-01',_binary '',_binary '\0',3,'Title Room id 3 13',3),(67,'Content Room id 3 14','2024-01-01',_binary '',_binary '\0',3,'Title Room id 3 14',3),(68,'Content Room id 3 15','2024-01-01',_binary '',_binary '\0',3,'Title Room id 3 15',3),(69,'Content Room id 3 16','2024-01-01',_binary '',_binary '\0',3,'Title Room id 3 16',3),(70,'Content Room id 3 17','2024-01-01',_binary '',_binary '\0',3,'Title Room id 3 17',3),(71,'Content Room id 3 18','2024-01-01',_binary '',_binary '\0',3,'Title Room id 3 18',3),(72,'Content Room id 3 19','2024-01-01',_binary '',_binary '\0',3,'Title Room id 3 19',3),(73,'Content Room id 3 20','2024-01-01',_binary '',_binary '\0',3,'Title Room id 3 20',3),(74,'Content Room id 4 1 izmijenjen','2024-01-31',_binary '',_binary '\0',4,'Title Room id 4 1 izmijenjen music',3),(75,'Content Room id 4 2','2024-01-01',_binary '',_binary '\0',4,'Title Room id 4 2',3),(76,'Content Room id 4 3','2024-01-01',_binary '',_binary '\0',4,'Title Room id 4 3',3),(77,'Content Room id 4 4','2024-01-01',_binary '',_binary '\0',4,'Title Room id 4 4',3),(78,'Content Room id 4 5','2024-01-01',_binary '',_binary '\0',4,'Title Room id 4 5',3),(79,'Content Room id 4 6','2024-01-01',_binary '',_binary '\0',4,'Title Room id 4 6',3),(80,'Content Room id 4 7','2024-01-01',_binary '',_binary '\0',4,'Title Room id 4 7',3),(81,'Content Room id 4 8','2024-01-01',_binary '',_binary '\0',4,'Title Room id 4 8',3),(82,'Content Room id 4 9','2024-01-01',_binary '',_binary '\0',4,'Title Room id 4 9',3),(83,'Content Room id 4 10','2024-01-01',_binary '',_binary '\0',4,'Title Room id 4 10',3),(84,'Content Room id 4 11','2024-01-01',_binary '',_binary '\0',4,'Title Room id 4 11',3),(85,'Content Room id 4 12','2024-01-01',_binary '',_binary '\0',4,'Title Room id 4 12',3),(86,'Content Room id 4 13','2024-01-01',_binary '',_binary '\0',4,'Title Room id 4 13',3),(87,'Content Room id 4 14','2024-01-01',_binary '',_binary '\0',4,'Title Room id 4 14',3),(88,'Content Room id 4 15','2024-01-01',_binary '',_binary '\0',4,'Title Room id 4 15',3),(89,'Content Room id 4 16','2024-01-01',_binary '',_binary '\0',4,'Title Room id 4 16',3),(90,'Content Room id 4 17','2024-01-01',_binary '',_binary '\0',4,'Title Room id 4 17',3),(91,'Content Room id 4 18','2024-01-01',_binary '',_binary '\0',4,'Title Room id 4 18',3),(92,'Content Room id 4 19','2024-01-01',_binary '',_binary '\0',4,'Title Room id 4 19',3),(93,'Content Room id 4 20','2024-01-01',_binary '',_binary '\0',4,'Title Room id 4 20',3),(94,'!!!','2024-01-22',_binary '\0',_binary '',1,'novi komm',2),(96,'moderator dodao :)!!!','2024-01-26',_binary '',_binary '\0',1,'moderator dodao',17),(97,'user1 take1','2024-01-26',_binary '\0',_binary '',1,'user1 take1',2),(98,'fasfsaf!!!','2024-01-26',_binary '',_binary '\0',1,'sffasf!!!',17),(99,'muzika content','2024-01-31',_binary '\0',_binary '',4,'muzika title',2),(100,'17-23 31-jan-2024','2024-01-31',_binary '',_binary '\0',1,'17-23 31-jan-2024',21),(101,':)','2024-01-31',_binary '\0',_binary '',1,'science has failed our mother earth',21),(102,'radimo ko budale','2024-02-02',_binary '\0',_binary '',1,'nauka nas je iznevjerila',2);
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
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
