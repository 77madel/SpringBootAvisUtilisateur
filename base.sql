-- --------------------------------------------------------
-- Hôte:                         127.0.0.1
-- Version du serveur:           8.0.30 - MySQL Community Server - GPL
-- SE du serveur:                Win64
-- HeidiSQL Version:             12.1.0.6537
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Listage de la structure de la base pour position
CREATE DATABASE IF NOT EXISTS `position` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `position`;

-- Listage de la structure de table position. avis
CREATE TABLE IF NOT EXISTS `avis` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `message` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `utilisateur_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKblk72jlw8skbs4g9u1xibrpxh` (`utilisateur_id`),
  CONSTRAINT `FKblk72jlw8skbs4g9u1xibrpxh` FOREIGN KEY (`utilisateur_id`) REFERENCES `utilisateur` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Listage des données de la table position.avis : ~1 rows (environ)
DELETE FROM `avis`;
INSERT INTO `avis` (`id`, `message`, `status`, `utilisateur_id`) VALUES
	(1, 'Tres belle formation de spring book', NULL, NULL),
	(2, 'Cours Spring tres Cool', NULL, NULL),
	(3, 'Cours Spring tres Cool', NULL, 8);

-- Listage de la structure de table position. customers
CREATE TABLE IF NOT EXISTS `customers` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Listage des données de la table position.customers : ~4 rows (environ)
DELETE FROM `customers`;
INSERT INTO `customers` (`id`, `email`) VALUES
	(74, 'acceuil@gmail.com'),
	(73, 'madou@gmail.com'),
	(76, 'mata@gmail.com'),
	(75, 'saly@gmail.com');

-- Listage de la structure de table position. role
CREATE TABLE IF NOT EXISTS `role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `libelle` enum('ADMINISTRATEUR','UTILISATEUR') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Listage des données de la table position.role : ~1 rows (environ)
DELETE FROM `role`;
INSERT INTO `role` (`id`, `libelle`) VALUES
	(6, 'UTILISATEUR'),
	(7, 'UTILISATEUR');

-- Listage de la structure de table position. utilisateur
CREATE TABLE IF NOT EXISTS `utilisateur` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `actif` bit(1) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `mot_de_passe` varchar(255) DEFAULT NULL,
  `nom` varchar(255) DEFAULT NULL,
  `role_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKg7tx9d1p09c00xnl7u2g1p6yp` (`role_id`),
  CONSTRAINT `FKaqe8xtajee4k0wlqrvh2pso4l` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Listage des données de la table position.utilisateur : ~1 rows (environ)
DELETE FROM `utilisateur`;
INSERT INTO `utilisateur` (`id`, `actif`, `email`, `mot_de_passe`, `nom`, `role_id`) VALUES
	(7, b'1', 'kone@gmail.com', '$2a$10$qq/Frspo39QL2gCT5Dy22u.8Kwl/JPyec1HURhm1RRobqPYNoDGbe', 'Madou KONE', 6),
	(8, b'1', 'madou@gmail.com', '$2a$10$q2qiNlxiakz40c.sVugU3.xCkQAu00Dth3TxC4QRPdGuEBCqvZ5Vu', 'Madou KONE', 7);

-- Listage de la structure de table position. validation
CREATE TABLE IF NOT EXISTS `validation` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `activation` datetime(6) DEFAULT NULL,
  `creation` datetime(6) DEFAULT NULL,
  `expire` datetime(6) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `utilisateur_id` bigint DEFAULT NULL,
  `expiration` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKlbpw0tm5eu215mqoagm7wte1c` (`utilisateur_id`),
  CONSTRAINT `FKg0vmxkmj7wfai4s41fytetn9n` FOREIGN KEY (`utilisateur_id`) REFERENCES `utilisateur` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Listage des données de la table position.validation : ~1 rows (environ)
DELETE FROM `validation`;
INSERT INTO `validation` (`id`, `activation`, `creation`, `expire`, `code`, `utilisateur_id`, `expiration`) VALUES
	(5, NULL, '2024-05-30 15:54:08.215710', NULL, '483501', 7, '2024-05-30 16:04:08.215710'),
	(6, NULL, '2024-05-30 16:42:48.331385', NULL, '191568', 8, '2024-05-30 16:52:48.331385');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
