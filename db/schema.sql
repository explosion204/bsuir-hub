CREATE DATABASE  IF NOT EXISTS `bsuirhub` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `bsuirhub`;
-- MySQL dump 10.13  Distrib 8.0.25, for Win64 (x86_64)
--
-- Host: localhost    Database: bsuirhub
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
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_gts_group_idx` (`id_group`),
  KEY `fk_gts_teachers_idx` (`id_teacher`),
  KEY `fk_gts_subject_idx` (`id_subject`),
  CONSTRAINT `fk_gts_group` FOREIGN KEY (`id_group`) REFERENCES `groups` (`id`),
  CONSTRAINT `fk_gts_subject` FOREIGN KEY (`id_subject`) REFERENCES `subjects` (`id`),
  CONSTRAINT `fk_gts_teacher` FOREIGN KEY (`id_teacher`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assignments`
--

LOCK TABLES `assignments` WRITE;
/*!40000 ALTER TABLE `assignments` DISABLE KEYS */;
/*!40000 ALTER TABLE `assignments` ENABLE KEYS */;
UNLOCK TABLES;

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
  `text` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `creation_time` timestamp NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_comment_grade_idx` (`id_grade`),
  KEY `fk_comment_user_idx` (`id_user`),
  CONSTRAINT `fk_comment_grade` FOREIGN KEY (`id_grade`) REFERENCES `grades` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_comment_user` FOREIGN KEY (`id_user`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comments`
--

LOCK TABLES `comments` WRITE;
/*!40000 ALTER TABLE `comments` DISABLE KEYS */;
/*!40000 ALTER TABLE `comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `departments`
--

DROP TABLE IF EXISTS `departments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `departments` (
  `id` smallint unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `short_name` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `is_archived` tinyint(1) NOT NULL,
  `id_faculty` smallint unsigned NOT NULL,
  `specialty_alias` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_department_faculty_idx` (`id_faculty`),
  CONSTRAINT `fk_departments_faculties` FOREIGN KEY (`id_faculty`) REFERENCES `faculties` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `departments`
--

LOCK TABLES `departments` WRITE;
/*!40000 ALTER TABLE `departments` DISABLE KEYS */;
INSERT INTO `departments` VALUES (1,'Кафедра инженерной и компьютерной графики','КафИКГ',0,1,'Инженерная геометрия и компьютерная графика'),(2,'Кафедра инженерной психологии и эргономики','КафИПЭ',0,1,'Инженерно-психологическое обеспечение информационных технологий'),(3,'Кафедра электронной техники и технологии','КафЭТТ',0,1,'Программно-управляемые электронно-оптические системы'),(4,'Кафедра интеллектуальных информационных технологий','КафИИТ',0,2,'Искусственный интеллект'),(5,'Кафедра вычислительных методов и программирования','КафВМП',0,2,'Информационные системы и технологии в игровой индустрии'),(6,'Кафедра информационных технологий автомат. систем','КафИТАС',0,2,'Автоматизированные системы обработки и отображения информации'),(7,'Кафедра систем управления','КафСУ',0,2,'Информационные технологии и управление в технических системах'),(8,'Кафедра теоретических основ электротехники','КафТОЭ',0,2,'Промышленная электроника'),(9,'Кафедра информатики','КафИнф',0,4,'Информатика и технологии программирования'),(10,'Кафедра программного обеспечения инф. технологий','КафПОИТ',0,4,'Программное обеспечение информационных технологий'),(11,'Кафедра электронных вычислительных машин','КафЭВМ',0,4,'Вычислительные машины, системы и сети'),(12,'Кафедра электронных вычислительных средств','КафЭВС',0,4,'Электронные вычислительные средства');
/*!40000 ALTER TABLE `departments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `faculties`
--

DROP TABLE IF EXISTS `faculties`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `faculties` (
  `id` smallint unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `short_name` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `is_archived` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `faculties`
--

LOCK TABLES `faculties` WRITE;
/*!40000 ALTER TABLE `faculties` DISABLE KEYS */;
INSERT INTO `faculties` VALUES (1,'Факультет компьютерного проектирования','ФКП',0),(2,'Факультет информационных технологий и управления','ФИТУ',0),(3,'Факультет радиотехники и электроники','ФРЭ',0),(4,'Факультет компьютерных систем и сетей','ФКСиС',0),(5,'Факультет инфокоммуникаций','ФИК',0),(6,'Инженерно-экономический факультет','ИЭФ',0),(7,'Военный факультет','ВФ',0);
/*!40000 ALTER TABLE `faculties` ENABLE KEYS */;
UNLOCK TABLES;

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
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_grade_teacher_idx` (`id_teacher`),
  KEY `fk_grade_student_idx` (`id_student`),
  KEY `fk_grade_subject_idx` (`id_subject`),
  CONSTRAINT `fk_grade_student` FOREIGN KEY (`id_student`) REFERENCES `users` (`id`),
  CONSTRAINT `fk_grade_subject` FOREIGN KEY (`id_subject`) REFERENCES `subjects` (`id`),
  CONSTRAINT `fk_grade_teacher` FOREIGN KEY (`id_teacher`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grades`
--

LOCK TABLES `grades` WRITE;
/*!40000 ALTER TABLE `grades` DISABLE KEYS */;
/*!40000 ALTER TABLE `grades` ENABLE KEYS */;
UNLOCK TABLES;

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
  `name` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `is_archived` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_group_headman_idx` (`id_headman`),
  KEY `fk_group_curator_idx` (`id_curator`),
  KEY `fk_groups_department_idx` (`id_department`),
  CONSTRAINT `fk_groups_curator` FOREIGN KEY (`id_curator`) REFERENCES `users` (`id`),
  CONSTRAINT `fk_groups_department` FOREIGN KEY (`id_department`) REFERENCES `departments` (`id`),
  CONSTRAINT `fk_groups_headman` FOREIGN KEY (`id_headman`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groups`
--

LOCK TABLES `groups` WRITE;
/*!40000 ALTER TABLE `groups` DISABLE KEYS */;
INSERT INTO `groups` VALUES (1,9,5,1,'853501',0);
/*!40000 ALTER TABLE `groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` tinyint unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'STUDENT'),(2,'TEACHER'),(3,'ADMIN');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `statuses`
--

DROP TABLE IF EXISTS `statuses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `statuses` (
  `id` tinyint unsigned NOT NULL,
  `name` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `statuses`
--

LOCK TABLES `statuses` WRITE;
/*!40000 ALTER TABLE `statuses` DISABLE KEYS */;
INSERT INTO `statuses` VALUES (1,'NOT_CONFIRMED'),(2,'CONFIRMED'),(3,'DELETED');
/*!40000 ALTER TABLE `statuses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subjects`
--

DROP TABLE IF EXISTS `subjects`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subjects` (
  `id` smallint unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `short_name` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `is_archived` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subjects`
--

LOCK TABLES `subjects` WRITE;
/*!40000 ALTER TABLE `subjects` DISABLE KEYS */;
INSERT INTO `subjects` VALUES (1,'Мематика. Математический анализ','ММА',0),(2,'Иностранный язык','ИнЯз',0),(3,'Основы алгоритмизации и программирования','ОАиП',0),(4,'Физика','Физ',0),(5,'Физическая культура','ФизКульт',0),(6,'Основы компьютерной графики','ОКГ',0),(7,'Аналитическая геометрия и линейная алгебра','АГиЛА',0),(8,'Белорусский язык','БелЯз',0),(9,'Математическая логика','МЛ',0),(10,'Дискретная математика','ДМ',0),(11,'Программирование','Прог',0),(12,'Основы высшей алгебры','ОВА',0),(13,'Логика','Лог',0),(14,'Политология','Полит',0),(15,'История','Ист',0),(16,'Инструменты и средства программирования','ИСП',0),(17,'Математика, геометрия и алгебра','МГиА',0),(18,'Теория вероятностей и математическая статистика','ТВиМС',0),(19,'Безопасность жизнедеятельности человека','БЖЧ',0),(20,'Философия','Фил',0),(21,'Спецглавы дискретной математики','СпецДМ',0),(22,'Объектно-ориентированное программирование','ООП',0),(23,'Методы численного анализа','МЧА',0),(24,'Алгоритмы и структуры данных','АиСД',0),(25,'Инженерная компьютерная графика','ИКГ',0),(26,'Архитектура вычислительных систем','АВС',0);
/*!40000 ALTER TABLE `subjects` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `login` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `email` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `password_hash` char(64) COLLATE utf8_unicode_ci NOT NULL,
  `salt` char(16) COLLATE utf8_unicode_ci NOT NULL,
  `id_role` tinyint unsigned NOT NULL,
  `id_status` tinyint unsigned NOT NULL,
  `first_name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `patronymic` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `last_name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `profile_image` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `id_group` smallint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_user_role_idx` (`id_role`),
  KEY `fk_users_status_idx` (`id_status`),
  KEY `fk_users_group_idx` (`id_group`),
  CONSTRAINT `fk_user_role` FOREIGN KEY (`id_role`) REFERENCES `roles` (`id`),
  CONSTRAINT `fk_users_group` FOREIGN KEY (`id_group`) REFERENCES `groups` (`id`),
  CONSTRAINT `fk_users_status` FOREIGN KEY (`id_status`) REFERENCES `statuses` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'explosion204','dzmitriy20magic@gmail.com','85b0c9ef6f0617507b4e8e6b3dc482e4b0f6115f55ea2a25ff906ad08215b82d','HKqDOCL3WhO5fvie',3,2,'Дмитрий','Георгиевич','Карнышов','default_profile.jpg',NULL),(2,'user0001','test1@email.com','e1e19281efd97d51b86251f11e01bd66338bb600aac66be074f55728180d1620','wkFLCV4acFWHjpXN',2,2,'Елена','Максимовна','Eфремова','default_profile.jpg',NULL),(3,'user0002','test2@email.com','7c3e9b409ac6af1266f08836131457293890a4a259137a3444064eb34a5b047d','qjIC9bgXLHkHTAlm',2,2,'Константин','Артемович','Федоров','default_profile.jpg',NULL),(4,'user0003','test3@email.com','99ddea3c328c0aa8104a2724be6cd0a0d18c3887f72860af334cf52916c6a383','aGk8phgmdvWX6sBj',2,2,'Полина','Кирилловна','Михайлова','default_profile.jpg',NULL),(5,'user0004','test4@email.com','8412ed54d6bdf6671d0a56e69fa50c4e975d76dab84e3dc64e4b9af5bacd4647','tMlVbKQnuLDXTy3y',1,2,'Анна','Романовна','Воробьева','default_profile.jpg',1),(6,'user0005','test5@email.com','26d79ee7b3454af6b1dae47692d2a913191b94edfd544e7cb516a8374851caa4','mqSRABswIKVm0CTt',1,2,'Даниил','Дмитриевич','Жилин','default_profile.jpg',1),(7,'user0006','test6@email.com','e70b230318ae42ac21dd87aac08e2cc1e2f10343653d6d18d2dc2e173cda9361','4NEwcAZjyE0J6pLG',1,2,'Владимир','Артурович','Греков','default_profile.jpg',1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-08-17 13:15:10
