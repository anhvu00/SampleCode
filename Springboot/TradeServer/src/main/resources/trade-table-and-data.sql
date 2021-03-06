-- MySQL dump 10.13  Distrib 8.0.23, for Win64 (x86_64)
--
-- Host: localhost    Database: sakila
-- ------------------------------------------------------
-- Server version	8.0.23

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
-- Table structure for table `trade`
--

DROP TABLE IF EXISTS `trade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trade` (
  `id` int NOT NULL AUTO_INCREMENT,
  `symbol` varchar(10) NOT NULL,
  `quantity` int NOT NULL,
  `buy_date` date NOT NULL,
  `buy_price` decimal(10,2) NOT NULL,
  `buy_total` decimal(10,2) DEFAULT NULL,
  `sell_quantity` int DEFAULT NULL,
  `sell_date` date DEFAULT NULL,
  `sell_price` decimal(10,2) DEFAULT NULL,
  `sell_total` decimal(10,2) DEFAULT NULL,
  `gain_loss` decimal(10,2) DEFAULT NULL,
  `gain_loss_pct` decimal(10,2) DEFAULT NULL,
  `notes` mediumtext,
  `target` decimal(10,2) DEFAULT NULL,
  `broker` varchar(25) DEFAULT NULL,
  `account` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='not normalized yet. wait for version 2.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trade`
--

LOCK TABLES `trade` WRITE;
/*!40000 ALTER TABLE `trade` DISABLE KEYS */;
INSERT INTO `trade` VALUES (1,'ACRX',500,'2021-01-13',2.05,1025.00,500,'2021-01-13',1.91,955.00,-70.00,-0.07,NULL,7.00,'ETRADE','BROKERAGE'),(2,'ACRX',500,'2021-01-13',2.05,1025.00,500,'2021-01-27',2.32,1160.00,135.00,0.13,NULL,7.00,'ETRADE','BROKERAGE'),(3,'COCP',500,'2020-12-21',1.56,780.00,500,'2021-01-22',2.11,1055.00,275.00,0.35,NULL,4.75,'ETRADE','BROKERAGE'),(4,'COCP',500,'2020-12-21',1.56,780.00,500,'2021-01-25',1.80,900.00,120.00,0.15,NULL,4.75,'ETRADE','BROKERAGE'),(5,'MBIO',500,'2020-12-22',3.34,1670.00,500,'2021-01-22',5.03,2515.00,845.00,0.51,NULL,11.00,'ETRADE','BROKERAGE'),(6,'MBIO',1000,'2020-12-22',3.34,3340.00,1000,'2021-01-25',4.70,4700.00,1360.00,0.41,NULL,11.00,'ETRADE','BROKERAGE'),(7,'MBIO',500,'2020-12-22',3.34,1670.00,500,'2021-01-27',4.35,2175.00,505.00,0.30,NULL,11.00,'ETRADE','BROKERAGE'),(8,'NCNA',500,'2021-01-13',6.25,3125.00,500,'2021-01-27',5.85,2925.00,-200.00,-0.06,NULL,17.00,'ETRADE','BROKERAGE'),(9,'OPEN',200,'2021-01-26',27.80,5560.00,200,'2021-01-26',28.53,5706.00,146.00,0.03,NULL,32.50,'ETRADE','BROKERAGE'),(10,'PRQR',500,'2021-01-21',5.37,2685.00,500,'2021-01-27',5.24,2620.00,-65.00,-0.02,NULL,20.00,'ETRADE','BROKERAGE'),(13,'PTN',500,'2020-12-22',0.55,275.00,500,'2021-01-22',1.05,525.00,250.00,0.91,NULL,2.50,'ETRADE','BROKERAGE'),(14,'PTN',1500,'2020-12-22',0.55,825.00,1500,'2021-01-27',0.90,1350.00,525.00,0.64,NULL,2.50,'ETRADE','BROKERAGE'),(15,'RKT',50,'2020-11-11',22.28,1114.00,50,'2021-01-21',20.05,1002.50,-111.50,-0.10,NULL,24.50,'ETRADE','BROKERAGE'),(16,'SNDL',4000,'2021-01-28',0.89,3560.00,4000,'2021-01-29',0.85,3400.00,-160.00,-0.04,NULL,0.30,'ETRADE','BROKERAGE'),(17,'SNDL',1000,'2021-01-28',0.89,890.00,4000,'2021-02-01',0.92,3680.00,120.00,0.03,NULL,0.30,'ETRADE','BROKERAGE'),(18,'TBLT',2500,'2021-01-29',1.51,3775.00,2500,'2021-02-02',1.28,3200.00,-575.00,-0.15,NULL,20.00,'ETRADE','BROKERAGE'),(19,'VRCA',100,'2020-12-30',12.04,1204.00,100,'2021-01-27',11.30,1130.00,-74.00,-0.06,NULL,19.50,'ETRADE','BROKERAGE'),(20,'VRCA',100,'2020-12-30',12.04,1204.00,100,'2021-01-27',11.27,1127.00,-77.00,-0.06,NULL,19.50,'ETRADE','BROKERAGE'),(21,'XXII',200,'2020-12-02',1.59,318.00,200,'2021-01-27',2.60,520.00,202.00,0.64,NULL,4.00,'ETRADE','BROKERAGE');
/*!40000 ALTER TABLE `trade` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-02-11 11:50:29
