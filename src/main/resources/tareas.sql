CREATE DATABASE tareas;
USE tareas;

CREATE TABLE USUARIO (
    NIF CHAR(9) PRIMARY KEY,
    NOMBRE VARCHAR(30),
    APELLIDOS VARCHAR(50),
    PW VARCHAR(200),
    ACTIVO tinyint
);

CREATE TABLE ROLES (
    id int auto_increment primary key,
    nif char(9),
    foreign key (nif) references usuario(nif),
    ROL varchar(50) NOT NULL
);

CREATE TABLE tarea (
	ID INT PRIMARY KEY auto_increment,
    NOMBRE VARCHAR(50),
    DESCRIPCION VARCHAR(100),
    NIF VARCHAR(9),
    ESTADO TINYINT,
    foreign key (NIF) REFERENCES USUARIO(NIF)
);

INSERT INTO usuario
values ('11111111A', 'Amelia', 'Lopez', '$2a$10$MOynvigRuUw3RX.1/1VkVeeXnIANlQMDpppV/6fPdfJp7WfMpREk2', 1);

insert into ROLES (NIF, ROL)
  (SELECT NIF, 'ADMINISTRADOR' FROM USUARIO);




	