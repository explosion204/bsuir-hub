-- MySQL dump 10.13  Distrib 8.0.25, for Win64 (x86_64)
--
-- Host: localhost    Database: bsuirhub_test
-- ------------------------------------------------------
-- Server version	8.0.25

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
-- Table structure for table `assignments`
--

DROP TABLE IF EXISTS `assignments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `assignments` (
                               `id` int unsigned NOT NULL AUTO_INCREMENT,
                               `id_teacher` int unsigned NOT NULL,
                               `id_subject` smallint unsigned NOT NULL,
                               `id_group` smallint unsigned NOT NULL,
                               PRIMARY KEY (`id`),
                               UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comments` (
                            `id` int unsigned NOT NULL AUTO_INCREMENT,
                            `id_grade` int unsigned NOT NULL,
                            `id_user` int unsigned NOT NULL,
                            `text` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                            `creation_time` timestamp NOT NULL,
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `departments`
--

DROP TABLE IF EXISTS `departments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `departments` (
                               `id` smallint unsigned NOT NULL AUTO_INCREMENT,
                               `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                               `short_name` varchar(15) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                               `is_archived` tinyint(1) NOT NULL,
                               `id_faculty` smallint unsigned NOT NULL,
                               `specialty_alias` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
                               PRIMARY KEY (`id`),
                               UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `faculties`
--

DROP TABLE IF EXISTS `faculties`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `faculties` (
                             `id` smallint unsigned NOT NULL AUTO_INCREMENT,
                             `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                             `short_name` varchar(15) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                             `is_archived` tinyint(1) NOT NULL,
                             PRIMARY KEY (`id`),
                             UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `grades`
--

DROP TABLE IF EXISTS `grades`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `grades` (
                          `id` int unsigned NOT NULL AUTO_INCREMENT,
                          `value` tinyint NOT NULL,
                          `id_teacher` int unsigned NOT NULL,
                          `id_student` int unsigned NOT NULL,
                          `id_subject` smallint unsigned NOT NULL,
                          `date` date NOT NULL,
                          PRIMARY KEY (`id`),
                          UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `groups`
--

DROP TABLE IF EXISTS `groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `groups` (
                          `id` smallint unsigned NOT NULL AUTO_INCREMENT,
                          `id_department` smallint unsigned NOT NULL,
                          `id_headman` int unsigned DEFAULT NULL,
                          `id_curator` int unsigned NOT NULL,
                          `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                          `is_archived` tinyint(1) DEFAULT NULL,
                          PRIMARY KEY (`id`),
                          UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
                         `id` tinyint unsigned NOT NULL AUTO_INCREMENT,
                         `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `statuses`
--

DROP TABLE IF EXISTS `statuses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `statuses` (
                            `id` tinyint unsigned NOT NULL,
                            `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `subjects`
--

DROP TABLE IF EXISTS `subjects`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subjects` (
                            `id` smallint unsigned NOT NULL AUTO_INCREMENT,
                            `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                            `short_name` varchar(15) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                            `is_archived` tinyint(1) NOT NULL,
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
                         `id` int unsigned NOT NULL AUTO_INCREMENT,
                         `login` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                         `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                         `password_hash` char(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                         `salt` char(16) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                         `id_role` tinyint unsigned NOT NULL,
                         `id_status` tinyint unsigned NOT NULL,
                         `first_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                         `patronymic` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
                         `last_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                         `profile_image` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
                         `id_group` smallint unsigned DEFAULT NULL,
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-08-18 15:36:33
