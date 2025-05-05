CREATE DATABASE SMPUV;
USE SMPUV;

CREATE TABLE Administradores (
  IdAdministrador int NOT NULL AUTO_INCREMENT,
  nombre varchar(40),
  apellidoPaterno varchar(40),
  apellidoMaterno varchar(40),
  contraseña varchar(70),
  PRIMARY KEY (IdAdministrador)
);

CREATE TABLE Clientes (
  IdCliente int NOT NULL AUTO_INCREMENT,
  nombre varchar(40),
  apellidoPaterno varchar(40),
  apellidoMaterno varchar(40),
  contraseña varchar(70),
  PRIMARY KEY (IdCliente)
);

CREATE TABLE Marcas (
  IdMarca int NOT NULL AUTO_INCREMENT,
  nombre varchar(50),
  PRIMARY KEY (IdMarca)
);

CREATE TABLE EquiposComputo (
  NumSerie varchar(50) NOT NULL,
  IdMarca int,
  fuentePoder varchar(50),
  cpu varchar(50),
  gpu varchar(50),
  ram varchar(50),
  motherboard varchar(50),
  disco varchar(50),
  fechaAdquisición date,
  estado varchar(30),
  tipo varchar(15),
  PRIMARY KEY (NumSerie),
  KEY FK_IdMarca_EquiposComputo (IdMarca),
  CONSTRAINT FK_IdMarca_EquiposComputo FOREIGN KEY (IdMarca) REFERENCES Marcas (IdMarca) ON DELETE CASCADE
);

CREATE TABLE Refacciones (
  NumSerie varchar(50) NOT NULL,
  IdMarca int,
  nombre varchar(125),
  tipo varchar(30),
  precio decimal(16,2),
  stock int,
  PRIMARY KEY (NumSerie),
  KEY FK_IdMarca_Refacciones (IdMarca),
  CONSTRAINT FK_IdMarca_Refacciones FOREIGN KEY (IdMarca) REFERENCES Marcas (IdMarca) ON DELETE CASCADE
);

CREATE TABLE Servicios (
  IdServicio int NOT NULL AUTO_INCREMENT,
  NumSerie varchar(50),
  IdCliente int,
  fechaInicio date,
  fechaFin date,
  tipo varchar(30),
  costo decimal(16,2),
  descripción text,
  estado varchar(30),
  PRIMARY KEY (IdServicio),
  KEY FK_IdCliente_Servicios (IdCliente),
  KEY FK_NumSerie_Servicios (NumSerie),
  CONSTRAINT FK_IdCliente_Servicios FOREIGN KEY (IdCliente) REFERENCES Clientes (IdCliente) ON DELETE CASCADE,
  CONSTRAINT FK_NumSerie_Servicios FOREIGN KEY (NumSerie) REFERENCES EquiposComputo (NumSerie) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Técnicos (
  NumPersonal int NOT NULL,
  nombre varchar(40),
  apellidoPaterno varchar(40),
  apellidoMaterno varchar(40),
  contraseña varchar(70),
  PRIMARY KEY (NumPersonal)
);
