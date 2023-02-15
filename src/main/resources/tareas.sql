CREATE DATABASE tareas;

USE tareas;

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;

CREATE TABLE `usuario` (
    `NIF` char(9) NOT NULL,
    `NOMBRE` varchar(30) DEFAULT NULL,
    `APELLIDOS` varchar(50) DEFAULT NULL,
    `PW` varchar(200) DEFAULT NULL,
    `ACTIVO` tinyint DEFAULT NULL,
    PRIMARY KEY (`NIF`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `roles`;

/*!40101 SET @saved_cs_client     = @@character_set_client */;

/*!50503 SET character_set_client = utf8mb4 */;

CREATE TABLE `roles` (
    `id` int NOT NULL AUTO_INCREMENT,
    `nif` char(9) DEFAULT NULL,
    `ROL` varchar(50) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `nif` (`nif`),
    CONSTRAINT `roles_ibfk_1` FOREIGN KEY (`nif`) REFERENCES `usuario` (`NIF`)

) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `tarea`;

/*!40101 SET @saved_cs_client     = @@character_set_client */;

/*!50503 SET character_set_client = utf8mb4 */;

CREATE TABLE `tarea` (
    `ID` int NOT NULL AUTO_INCREMENT,
    `NOMBRE` varchar(200) DEFAULT NULL,
    `DESCRIPCION` varchar(1000) DEFAULT NULL,
    `ESTADO` tinyint DEFAULT NULL,
    `NIF` char(9) DEFAULT NULL,
    PRIMARY KEY (`ID`),
    KEY `NIF` (`NIF`),
    CONSTRAINT `tarea_ibfk_1` FOREIGN KEY (`NIF`) REFERENCES `usuario` (`NIF`)

) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


INSERT INTO usuario
values ('11111111A', 'Amelia', 'Lopez', '$2a$10$MOynvigRuUw3RX.1/1VkVeeXnIANlQMDpppV/6fPdfJp7WfMpREk2', 1);

insert into ROLES (NIF, ROL)
  (SELECT NIF, 'ADMINISTRADOR' FROM USUARIO);




	