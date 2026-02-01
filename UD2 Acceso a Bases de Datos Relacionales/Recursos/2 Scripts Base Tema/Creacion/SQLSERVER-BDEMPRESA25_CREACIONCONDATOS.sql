-- ######################################################################
-- 1. XESTIÓN DA BASE DE DATOS (CREATE/DROP)
-- ######################################################################

-- Define o nome da base de datos
DECLARE @dbName NVARCHAR(128)
SET @dbName = N'BDEMPRESA25';


-- Comproba se a base de datos existe e bórraa (Usando a vista de catálogo sys.databases)
IF EXISTS (SELECT name FROM sys.databases WHERE name = @dbName)
BEGIN
    -- Forzar a desconexión de todos os usuarios para permitir o borrado
    ALTER DATABASE EMPRESA25 SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE EMPRESA25;
    PRINT 'Base de datos BDEMPRESA25 borrada con éxito.';
END
GO

-- Creación da base de datos
CREATE DATABASE BDEMPRESA25;
GO

-- Usar a base de datos
USE BDEMPRESA25;
GO

-- ######################################################################
-- 2. XESTIÓN DAS TABOAS (DROP IF EXISTS)
-- -- ######################################################################

IF OBJECT_ID('EMPREGADO_PROXECTO', 'U') IS NOT NULL DROP TABLE EMPREGADO_PROXECTO;
IF OBJECT_ID('EMPREGADOFIXO', 'U') IS NOT NULL DROP TABLE EMPREGADOFIXO;
IF OBJECT_ID('EMPREGADOTEMPORAL', 'U') IS NOT NULL DROP TABLE EMPREGADOTEMPORAL;
IF OBJECT_ID('PROXECTO', 'U') IS NOT NULL DROP TABLE PROXECTO;
IF OBJECT_ID('LUGAR', 'U') IS NOT NULL DROP TABLE LUGAR;
-- EMPREGADO debe borrarse antes que DEPARTAMENTO
IF OBJECT_ID('EMPREGADO', 'U') IS NOT NULL DROP TABLE EMPREGADO;
IF OBJECT_ID('DEPARTAMENTO', 'U') IS NOT NULL DROP TABLE DEPARTAMENTO;
GO

-- ######################################################################
-- 3. CREACIÓN DE TABOAS CON PK e UNIQUE
-- ######################################################################

-- TÁBOA DEPARTAMENTO
CREATE TABLE DEPARTAMENTO (
    NumDepartamento INT NOT NULL,
    NomeDepartamento VARCHAR(25) NOT NULL,
    NSSDirector VARCHAR(15) NOT NULL,
    CONSTRAINT PK_DEPARTAMENTO PRIMARY KEY (NumDepartamento),
    CONSTRAINT U_DEPARTAMENTO UNIQUE (NomeDepartamento)
);

-- TÁBOA EMPREGADO
CREATE TABLE EMPREGADO (
    Nome VARCHAR(25) NOT NULL,
    Apelido1 VARCHAR(25) NOT NULL,
    Apelido2 VARCHAR(25) NULL,
    NSS VARCHAR(15) NOT NULL,
    Rua VARCHAR(30) NULL,
    Numero_Calle INT NULL,
    Piso VARCHAR(4) NULL,
    CP CHAR(5) NULL,
    Localidade VARCHAR(25) NULL,
    Provincia VARCHAR(15) NULL,
    DataNacemento DATE NULL,
    Sexo CHAR(1) DEFAULT 'M' NULL,
    NSSSupervisa VARCHAR(15) NULL,
    NumDepartamentoPertenece INT NULL,
    CONSTRAINT PK_EMPLEADO PRIMARY KEY (NSS)
);

-- TÁBOA PROXECTO
CREATE TABLE PROXECTO (
    NumProxecto INT NOT NULL,
    NomeProxecto VARCHAR(25) NOT NULL,
    Lugar VARCHAR(25) NOT NULL,
    NumDepartControla INT NOT NULL,
    CONSTRAINT PK_PROYECTO PRIMARY KEY (NumProxecto),
    CONSTRAINT U_PROYECTO UNIQUE (NomeProxecto)
);

-- TÁBOA EMPREGADO_PROXECTO (N:M)
CREATE TABLE EMPREGADO_PROXECTO (
    NSSEmpregado VARCHAR(15) NOT NULL,
    NumProxecto INT NOT NULL,
    Horas INT NULL,
    CONSTRAINT PK_EMPLEADO_PROYECTO PRIMARY KEY (NSSEmpregado, NumProxecto)
);

-- TÁBOA EMPREGADOFIXO (Subclase de EMPREGADO)
CREATE TABLE EMPREGADOFIXO (
    NSS VARCHAR(15) NOT NULL,
    Salario DECIMAL(10, 2) NULL,
    DataAlta DATE NULL,
    Categoria VARCHAR(20) NULL,
    CONSTRAINT PK_EMPREGADOFIXO PRIMARY KEY (NSS)
);

-- TÁBOA EMPREGADOTEMPORAL (Subclase de EMPREGADO)
CREATE TABLE EMPREGADOTEMPORAL (
    NSS VARCHAR(15) NOT NULL,
    DataInicio DATE NULL,
    DataFin DATE NULL,
    CosteHora DECIMAL(10, 2) NULL,
    NumHoras DECIMAL(10, 2) NULL,
    CONSTRAINT PK_EMPREGADOTEMP PRIMARY KEY (NSS)
);

-- TÁBOA LUGAR (Lugar do Departamento)
CREATE TABLE LUGAR (
    ID INT IDENTITY(1,1) NOT NULL,
    Num_departamento INT NOT NULL,
    Lugar VARCHAR(15) NOT NULL,
    CONSTRAINT PK_IDLUGAR PRIMARY KEY (ID),
    CONSTRAINT UK_NUMDPTO UNIQUE (Num_departamento, Lugar)
);
GO

-- ######################################################################
-- 4. DEFINICIÓN DAS RESTRICCIÓNS E CLAVES ALLEAS (FOREIGN KEYS e CHECK)
-- ######################################################################

-- RESTRICCIÓNS DE EMPREGADO
ALTER TABLE EMPREGADO ADD CONSTRAINT CK_SEXO
    CHECK (Sexo IN ('H', 'M'));

ALTER TABLE EMPREGADO ADD CONSTRAINT FK_EMPLEADO_DEPARTAMENTO
    FOREIGN KEY (NumDepartamentoPertenece) REFERENCES DEPARTAMENTO (NumDepartamento);

ALTER TABLE EMPREGADO ADD CONSTRAINT FK_EMPLEADO_EMPLEADO
    FOREIGN KEY (NSSSupervisa) REFERENCES EMPREGADO (NSS);

-- RESTRICCIÓNS DE DEPARTAMENTO (FK do director, depende de EMPREGADO)
ALTER TABLE DEPARTAMENTO ADD CONSTRAINT FK_DEPARTAMENTO_EMPLEADO
    FOREIGN KEY (NSSDirector) REFERENCES EMPREGADO (NSS);

-- RESTRICCIÓNS DE EMPREGADO_PROXECTO
ALTER TABLE EMPREGADO_PROXECTO ADD CONSTRAINT FK_EMPLEADO_PROYECTO_EMPLEADO
    FOREIGN KEY (NSSEmpregado) REFERENCES EMPREGADO (NSS);

ALTER TABLE EMPREGADO_PROXECTO ADD CONSTRAINT FK__EMPLEADO_PROYECTO_PROYECTO
    FOREIGN KEY (NumProxecto) REFERENCES PROXECTO (NumProxecto);

-- RESTRICCIÓNS DE EMPREGADOFIXO (Depende de EMPREGADO)
ALTER TABLE EMPREGADOFIXO ADD CONSTRAINT FK_EMPREGADOFIXO
    FOREIGN KEY (NSS) REFERENCES EMPREGADO (NSS);

-- RESTRICCIÓNS DE EMPREGADOTEMPORAL (Depende de EMPREGADO)
ALTER TABLE EMPREGADOTEMPORAL ADD CONSTRAINT FK_EMPREGADOTEMP
    FOREIGN KEY (NSS) REFERENCES EMPREGADO (NSS);

-- RESTRICCIÓNS DE LUGAR
ALTER TABLE LUGAR ADD CONSTRAINT FK_NUMDPTOLUGAR
    FOREIGN KEY (Num_departamento) REFERENCES DEPARTAMENTO (NumDepartamento);

-- RESTRICCIÓNS DE PROXECTO
ALTER TABLE PROXECTO ADD CONSTRAINT FK_PROYECT_DEPARTAMENTO
    FOREIGN KEY (NumDepartControla) REFERENCES DEPARTAMENTO (NumDepartamento);
GO

-- ######################################################################
-- 5. INSERCIÓN DE DATOS
-- ######################################################################
---
--- DESACTIVACIÓN TEMPORAL DE RESTRICCIÓNS
---

-- Desactiva a comprobación de TODAS as restricións (incluídas FKs e CHECKs) para permitir a inserción
PRINT 'Desactivando restricións para a inserción de datos...';
ALTER TABLE EMPREGADO NOCHECK CONSTRAINT ALL;
ALTER TABLE DEPARTAMENTO NOCHECK CONSTRAINT ALL;
ALTER TABLE EMPREGADO_PROXECTO NOCHECK CONSTRAINT ALL;
ALTER TABLE EMPREGADOFIXO NOCHECK CONSTRAINT ALL;
ALTER TABLE EMPREGADOTEMPORAL NOCHECK CONSTRAINT ALL;
ALTER TABLE PROXECTO NOCHECK CONSTRAINT ALL;
ALTER TABLE LUGAR NOCHECK CONSTRAINT ALL;
GO
-- INSERTS EMPREGADO 
INSERT INTO EMPREGADO (Nome, Apelido1, Apelido2, NSS, Rua, Numero_Calle, Piso, CP, Localidade, Provincia, DataNacemento, Sexo, NSSSupervisa, NumDepartamentoPertenece) VALUES ('Pepe', 'López', 'López', '1111111', 'Olmo', 23, '4', '27003', 'Lugo', NULL, '1969-07-07', 'H', NULL, 1);
INSERT INTO EMPREGADO (Nome, Apelido1, Apelido2, NSS, Rua, Numero_Calle, Piso, CP, Localidade, Provincia, DataNacemento, Sexo, NSSSupervisa, NumDepartamentoPertenece) VALUES ('Diego', 'Lamela', 'Bello', '1010001', 'Camelias', 123, '4-A', '36211', 'Vigo', NULL, '1959-04-15', 'H', '1111111', 1);
INSERT INTO EMPREGADO (Nome, Apelido1, Apelido2, NSS, Rua, Numero_Calle, Piso, CP, Localidade, Provincia, DataNacemento, Sexo, NSSSupervisa, NumDepartamentoPertenece) VALUES ('José Manuel', 'García', 'Graña', '2525252', 'Illas Canarias', 101, '2-B', NULL, 'Vigo', NULL, '1966-09-02', 'H', '1111111', 2);
INSERT INTO EMPREGADO (Nome, Apelido1, Apelido2, NSS, Rua, Numero_Calle, Piso, CP, Localidade, Provincia, DataNacemento, Sexo, NSSSupervisa, NumDepartamentoPertenece) VALUES ('Duarte', 'Xil', 'Torres', '2221111', 'Sol', 44, '1-A', '27002', 'Lugo', NULL, '1965-03-29', 'H', '1111111', 3);
INSERT INTO EMPREGADO (Nome, Apelido1, Apelido2, NSS, Rua, Numero_Calle, Piso, CP, Localidade, Provincia, DataNacemento, Sexo, NSSSupervisa, NumDepartamentoPertenece) VALUES ('Agostiño', 'Cerviño', 'Seoane', '8888889', 'Montero Ríos', 120, '4-D', '36208', 'Vigo', NULL, '1954-10-12', 'H', '1111111', 4);
INSERT INTO EMPREGADO (Nome, Apelido1, Apelido2, NSS, Rua, Numero_Calle, Piso, CP, Localidade, Provincia, DataNacemento, Sexo, NSSSupervisa, NumDepartamentoPertenece) VALUES ('Sara', 'Plaza', 'Marín', '4444444', 'Ciruela', 10, '6-B', '15705', 'Santiago', NULL, '1969-07-29', 'M', '1111111', 5);
INSERT INTO EMPREGADO (Nome, Apelido1, Apelido2, NSS, Rua, Numero_Calle, Piso, CP, Localidade, Provincia, DataNacemento, Sexo, NSSSupervisa, NumDepartamentoPertenece) VALUES ('Ana María', 'Ramilo', 'Barreiro', '7777777', 'Virxe da cerca', 23, NULL, NULL, 'Santiago', NULL, '1962-03-04', 'M', '1111111', 6);
INSERT INTO EMPREGADO (Nome, Apelido1, Apelido2, NSS, Rua, Numero_Calle, Piso, CP, Localidade, Provincia, DataNacemento, Sexo, NSSSupervisa, NumDepartamentoPertenece) VALUES ('Rocio', 'López', 'Ferreiro', '0010010', 'Montero Ríos', 145, '6-G', '36208', 'Vigo', NULL, '1975-05-21', 'M', '1010001', 1);
INSERT INTO EMPREGADO (Nome, Apelido1, Apelido2, NSS, Rua, Numero_Calle, Piso, CP, Localidade, Provincia, DataNacemento, Sexo, NSSSupervisa, NumDepartamentoPertenece) VALUES ('Javier', 'Quintero', 'Alvarez', '0110010', 'Montevideo', 10, '2-F', '36209', 'Vigo', NULL, '1972-09-23', 'H', '1010001', 1);
INSERT INTO EMPREGADO (Nome, Apelido1, Apelido2, NSS, Rua, Numero_Calle, Piso, CP, Localidade, Provincia, DataNacemento, Sexo, NSSSupervisa, NumDepartamentoPertenece) VALUES ('Germán', 'Gómez', 'Rodríguez', '0999900', 'Sanjurjo Badía', 98, '3-D', '36212', 'Vigo', NULL, '1965-08-14', 'H', '8888889', 4);
INSERT INTO EMPREGADO (Nome, Apelido1, Apelido2, NSS, Rua, Numero_Calle, Piso, CP, Localidade, Provincia, DataNacemento, Sexo, NSSSupervisa, NumDepartamentoPertenece) VALUES ('Felix', 'Barreiro', 'Valiña', '1100222', 'Rinlo', 5, NULL, '27709', 'Ribadeo', NULL, '1968-10-01', 'H', '7777777', 6);
INSERT INTO EMPREGADO (Nome, Apelido1, Apelido2, NSS, Rua, Numero_Calle, Piso, CP, Localidade, Provincia, DataNacemento, Sexo, NSSSupervisa, NumDepartamentoPertenece) VALUES ('Xiao', 'Vecino', 'Vecino', '1122331', 'Brasil', 10, '2', NULL, 'Vigo', NULL, '1959-12-10', 'H', '2525252', 2);
INSERT INTO EMPREGADO (Nome, Apelido1, Apelido2, NSS, Rua, Numero_Calle, Piso, CP, Localidade, Provincia, DataNacemento, Sexo, NSSSupervisa, NumDepartamentoPertenece) VALUES ('Eligio', 'Rodrigo', NULL, '1231231', 'Espiño', 3, '', '15708', 'Santiago', NULL, '1973-01-02', 'H', '4444444', 5);
INSERT INTO EMPREGADO (Nome, Apelido1, Apelido2, NSS, Rua, Numero_Calle, Piso, CP, Localidade, Provincia, DataNacemento, Sexo, NSSSupervisa, NumDepartamentoPertenece) VALUES ('Mariña', 'Bello', 'Arias', '1341431', 'Gran Vía', 23, '4-D', NULL, 'Vigo', NULL, '1970-11-01', 'M', '2525252', 2);
INSERT INTO EMPREGADO (Nome, Apelido1, Apelido2, NSS, Rua, Numero_Calle, Piso, CP, Localidade, Provincia, DataNacemento, Sexo, NSSSupervisa, NumDepartamentoPertenece) VALUES ('Javier', 'Lamela', 'López', '3330000', 'Avda de Vigo', 120, '4-C', NULL, 'Pontevedra', NULL, '1959-09-20', 'H', '2221111', 3);
INSERT INTO EMPREGADO (Nome, Apelido1, Apelido2, NSS, Rua, Numero_Calle, Piso, CP, Localidade, Provincia, DataNacemento, Sexo, NSSSupervisa, NumDepartamentoPertenece) VALUES ('Beatríz', 'Mallo', 'López', '6000006', 'Cardenal Quiroga', 10, '2-A', '27400', 'Monforte', NULL, '1965-03-10', 'M', '2221111', 3);
INSERT INTO EMPREGADO (Nome, Apelido1, Apelido2, NSS, Rua, Numero_Calle, Piso, CP, Localidade, Provincia, DataNacemento, Sexo, NSSSupervisa, NumDepartamentoPertenece) VALUES ('Paula', 'Mariño', 'López', '3338883', 'Piñeira', 10, NULL, '27400', 'Monforte', NULL, '1969-01-01', 'M', '6000006', 3);
INSERT INTO EMPREGADO (Nome, Apelido1, Apelido2, NSS, Rua, Numero_Calle, Piso, CP, Localidade, Provincia, DataNacemento, Sexo, NSSSupervisa, NumDepartamentoPertenece) VALUES ('Rosa', 'Mariño', 'Rivera', '4044443', 'Piñeira', 25, NULL, '27400', 'Monforte', NULL, '1972-03-02', 'M', '6000006', 3);
INSERT INTO EMPREGADO (Nome, Apelido1, Apelido2, NSS, Rua, Numero_Calle, Piso, CP, Localidade, Provincia, DataNacemento, Sexo, NSSSupervisa, NumDepartamentoPertenece) VALUES ('Antonia', 'Romero', 'Boo', '4444999', 'Olmedo', 10, NULL, NULL, 'Santiago', NULL, '1967-09-10', 'M', '8888889', 4);
INSERT INTO EMPREGADO (Nome, Apelido1, Apelido2, NSS, Rua, Numero_Calle, Piso, CP, Localidade, Provincia, DataNacemento, Sexo, NSSSupervisa, NumDepartamentoPertenece) VALUES ('Uxío', 'Cabado', 'Penalta', '5000000', 'Nueva', 20, '3-C', NULL, 'Santiago', NULL, '1970-02-19', 'H', '2221111', 3);
INSERT INTO EMPREGADO (Nome, Apelido1, Apelido2, NSS, Rua, Numero_Calle, Piso, CP, Localidade, Provincia, DataNacemento, Sexo, NSSSupervisa, NumDepartamentoPertenece) VALUES ('Anxos', 'Loures', 'Freire', '5555000', 'Rosalía de Castro', 105, '4-F', NULL, 'Santiago', NULL, '1982-12-16', 'M', '5000000', 3);
INSERT INTO EMPREGADO (Nome, Apelido1, Apelido2, NSS, Rua, Numero_Calle, Piso, CP, Localidade, Provincia, DataNacemento, Sexo, NSSSupervisa, NumDepartamentoPertenece) VALUES ('Carme', 'Jurado', 'Vega', '6000600', 'Oliva', 10, '2', NULL, 'Pontevedra', NULL, '1964-05-11', 'M', '3330000', 3);
INSERT INTO EMPREGADO (Nome, Apelido1, Apelido2, NSS, Rua, Numero_Calle, Piso, CP, Localidade, Provincia, DataNacemento, Sexo, NSSSupervisa, NumDepartamentoPertenece) VALUES ('Valeriano', 'Boo', 'Boo', '6565656', 'Marina', 23, '2', NULL, 'Ribadeo', NULL, '1973-05-06', 'H', '1100222', 6);
INSERT INTO EMPREGADO (Nome, Apelido1, Apelido2, NSS, Rua, Numero_Calle, Piso, CP, Localidade, Provincia, DataNacemento, Sexo, NSSSupervisa, NumDepartamentoPertenece) VALUES ('Alex', 'Méndez', 'García', '7000007', 'Peregrina', 3, '1', NULL, 'Pontevedra', NULL, '1965-12-01', 'H', '3330000', 3);
INSERT INTO EMPREGADO (Nome, Apelido1, Apelido2, NSS, Rua, Numero_Calle, Piso, CP, Localidade, Provincia, DataNacemento, Sexo, NSSSupervisa, NumDepartamentoPertenece) VALUES ('Rubén', 'Guerra', 'Vázquez', '8888877', 'Preguntoiro', 11, '1', NULL, 'Santiago', NULL, '1969-05-29', 'H', '7777777', 6);
INSERT INTO EMPREGADO (Nome, Apelido1, Apelido2, NSS, Rua, Numero_Calle, Piso, CP, Localidade, Provincia, DataNacemento, Sexo, NSSSupervisa, NumDepartamentoPertenece) VALUES ('Angeles', 'López', 'Arias', '9876567', 'San Telmo', 5, '2-C', '36680', 'A Estrada', NULL, '1957-05-04', 'M', '4444444', 5);
INSERT INTO EMPREGADO (Nome, Apelido1, Apelido2, NSS, Rua, Numero_Calle, Piso, CP, Localidade, Provincia, DataNacemento, Sexo, NSSSupervisa, NumDepartamentoPertenece) VALUES ('Breixo', 'Pereiro', 'Lamela', '9900000', 'Sar', 29, '1', NULL, 'Santiago', NULL, '1980-02-19', 'H', '4444999', 4);
INSERT INTO EMPREGADO (Nome, Apelido1, Apelido2, NSS, Rua, Numero_Calle, Piso, CP, Localidade, Provincia, DataNacemento, Sexo, NSSSupervisa, NumDepartamentoPertenece) VALUES ('Celia', 'Bueno', 'Valiña', '9990009', 'Montero Ríos', 120, '4-D', '36208', 'Vigo', NULL, '1973-07-20', 'M', '1010001', 1);
INSERT INTO EMPREGADO (Nome, Apelido1, Apelido2, NSS, Rua, Numero_Calle, Piso, CP, Localidade, Provincia, DataNacemento, Sexo, NSSSupervisa, NumDepartamentoPertenece) VALUES ('Paulo', 'Máximo', 'Guerra', '9998888', 'Montero Ríos', 29, '2-A', NULL, 'Santiago', NULL, '1968-04-27', 'H', '7777777', 6);
GO

-- INSERTS DEPARTAMENTO ( os directores (NSSDirector) xa están en EMPREGADO)
INSERT INTO DEPARTAMENTO (NumDepartamento, NomeDepartamento, NSSDirector) VALUES (1, 'PERSOAL', '1111111');
INSERT INTO DEPARTAMENTO (NumDepartamento, NomeDepartamento, NSSDirector) VALUES (2, 'CONTABILIDAD', '2525252');
INSERT INTO DEPARTAMENTO (NumDepartamento, NomeDepartamento, NSSDirector) VALUES (3, 'TÉCNICO', '2221111');
INSERT INTO DEPARTAMENTO (NumDepartamento, NomeDepartamento, NSSDirector) VALUES (4, 'INFORMÁTICA', '8888889');
INSERT INTO DEPARTAMENTO (NumDepartamento, NomeDepartamento, NSSDirector) VALUES (5, 'ESTADÍSTICA', '4444444'); 
INSERT INTO DEPARTAMENTO (NumDepartamento, NomeDepartamento, NSSDirector) VALUES (6, 'INNOVACIÓN', '7777777');
GO

-- INSERTS PROXECTO
INSERT INTO PROXECTO (NumProxecto, NomeProxecto, Lugar, NumDepartControla) VALUES (1, 'XESTION DE PERSOAL', 'VIGO', 4);
INSERT INTO PROXECTO (NumProxecto, NomeProxecto, Lugar, NumDepartControla) VALUES (2, 'PORTAL', 'SANTIAGO', 4);
INSERT INTO PROXECTO (NumProxecto, NomeProxecto, Lugar, NumDepartControla) VALUES (3, 'APLICACIÓN CONTABLE', 'VIGO', 4);
INSERT INTO PROXECTO (NumProxecto, NomeProxecto, Lugar, NumDepartControla) VALUES (4, 'INFORME ESTADISTICO ANUAL', 'A ESTRADA', 5);
INSERT INTO PROXECTO (NumProxecto, NomeProxecto, Lugar, NumDepartControla) VALUES (5, 'PRODUCIÓN NOVO PRODUTO', 'RIBADEO', 6);
INSERT INTO PROXECTO (NumProxecto, NomeProxecto, Lugar, NumDepartControla) VALUES (6, 'DESEÑO NOVO CPD LUGO', 'LUGO', 3);
INSERT INTO PROXECTO (NumProxecto, NomeProxecto, Lugar, NumDepartControla) VALUES (7, 'MELLORAS SOCIAIS', 'VIGO', 1);
INSERT INTO PROXECTO (NumProxecto, NomeProxecto, Lugar, NumDepartControla) VALUES (8, 'DESEÑO NOVA TENDA VIGO', 'MONFORTE', 3);
INSERT INTO PROXECTO (NumProxecto, NomeProxecto, Lugar, NumDepartControla) VALUES (9, 'PROXECTO X', 'SANTIAGO', 5);
INSERT INTO PROXECTO (NumProxecto, NomeProxecto, Lugar, NumDepartControla) VALUES (10, 'PROXECTO Y', 'PONTEVEDRA', 3);
INSERT INTO PROXECTO (NumProxecto, NomeProxecto, Lugar, NumDepartControla) VALUES (11, 'Proyecto Z', 'Badajoz', 1);

-- INSERTS EMPREGADO_PROXECTO
INSERT INTO EMPREGADO_PROXECTO (NSSEmpregado, NumProxecto, Horas) VALUES ('0010010', 8, 20);
INSERT INTO EMPREGADO_PROXECTO (NSSEmpregado, NumProxecto, Horas) VALUES ('0110010', 7, 20);
INSERT INTO EMPREGADO_PROXECTO (NSSEmpregado, NumProxecto, Horas) VALUES ('0999900', 1, 25);
INSERT INTO EMPREGADO_PROXECTO (NSSEmpregado, NumProxecto, Horas) VALUES ('0999900', 3, 20);
INSERT INTO EMPREGADO_PROXECTO (NSSEmpregado, NumProxecto, Horas) VALUES ('0999900', 11, 25);
INSERT INTO EMPREGADO_PROXECTO (NSSEmpregado, NumProxecto, Horas) VALUES ('1010001', 1, 25);
INSERT INTO EMPREGADO_PROXECTO (NSSEmpregado, NumProxecto, Horas) VALUES ('1010001', 7, 15);
INSERT INTO EMPREGADO_PROXECTO (NSSEmpregado, NumProxecto, Horas) VALUES ('1010001', 11, 25);
INSERT INTO EMPREGADO_PROXECTO (NSSEmpregado, NumProxecto, Horas) VALUES ('1100222', 5, 30);
INSERT INTO EMPREGADO_PROXECTO (NSSEmpregado, NumProxecto, Horas) VALUES ('1111111', 2, NULL);
INSERT INTO EMPREGADO_PROXECTO (NSSEmpregado, NumProxecto, Horas) VALUES ('1122331', 8, 35);
INSERT INTO EMPREGADO_PROXECTO (NSSEmpregado, NumProxecto, Horas) VALUES ('1231231', 4, 20);
INSERT INTO EMPREGADO_PROXECTO (NSSEmpregado, NumProxecto, Horas) VALUES ('1231231', 9, 20);
INSERT INTO EMPREGADO_PROXECTO (NSSEmpregado, NumProxecto, Horas) VALUES ('1341431', 3, 15);
INSERT INTO EMPREGADO_PROXECTO (NSSEmpregado, NumProxecto, Horas) VALUES ('2221111', 6, 20);
INSERT INTO EMPREGADO_PROXECTO (NSSEmpregado, NumProxecto, Horas) VALUES ('2221111', 8, 10);
INSERT INTO EMPREGADO_PROXECTO (NSSEmpregado, NumProxecto, Horas) VALUES ('3330000', 10, 25);
INSERT INTO EMPREGADO_PROXECTO (NSSEmpregado, NumProxecto, Horas) VALUES ('3338883', 8, 30);
INSERT INTO EMPREGADO_PROXECTO (NSSEmpregado, NumProxecto, Horas) VALUES ('4044443', 8, 15);
INSERT INTO EMPREGADO_PROXECTO (NSSEmpregado, NumProxecto, Horas) VALUES ('4444999', 2, NULL);
INSERT INTO EMPREGADO_PROXECTO (NSSEmpregado, NumProxecto, Horas) VALUES ('6000006', 8, 25);
INSERT INTO EMPREGADO_PROXECTO (NSSEmpregado, NumProxecto, Horas) VALUES ('7000007', 10, 40);
INSERT INTO EMPREGADO_PROXECTO (NSSEmpregado, NumProxecto, Horas) VALUES ('8888889', 1, 25);
INSERT INTO EMPREGADO_PROXECTO (NSSEmpregado, NumProxecto, Horas) VALUES ('8888889', 2, NULL);
INSERT INTO EMPREGADO_PROXECTO (NSSEmpregado, NumProxecto, Horas) VALUES ('8888889', 7, 5);
INSERT INTO EMPREGADO_PROXECTO (NSSEmpregado, NumProxecto, Horas) VALUES ('8888889', 11, 25);
INSERT INTO EMPREGADO_PROXECTO (NSSEmpregado, NumProxecto, Horas) VALUES ('9876567', 4, 35);
INSERT INTO EMPREGADO_PROXECTO (NSSEmpregado, NumProxecto, Horas) VALUES ('9876567', 9, 10);
INSERT INTO EMPREGADO_PROXECTO (NSSEmpregado, NumProxecto, Horas) VALUES ('9900000', 2, NULL);
INSERT INTO EMPREGADO_PROXECTO (NSSEmpregado, NumProxecto, Horas) VALUES ('9990009', 1, 6);
INSERT INTO EMPREGADO_PROXECTO (NSSEmpregado, NumProxecto, Horas) VALUES ('9990009', 2, NULL);
INSERT INTO EMPREGADO_PROXECTO (NSSEmpregado, NumProxecto, Horas) VALUES ('9990009', 7, 20);

-- INSERTS EMPREGADOFIXO
INSERT INTO EMPREGADOFIXO (NSS, Salario, DataAlta, Categoria) VALUES ('0010010', 1500.00, '2014-02-12', 'Categoria 1');
INSERT INTO EMPREGADOFIXO (NSS, Salario, DataAlta, Categoria) VALUES ('0110010', 1000.00, '2012-01-11', 'Categoria 2');
INSERT INTO EMPREGADOFIXO (NSS, Salario, DataAlta, Categoria) VALUES ('0999900', 2500.00, '2000-03-22', 'Categoria 1');
INSERT INTO EMPREGADOFIXO (NSS, Salario, DataAlta, Categoria) VALUES ('1010001', 3500.00, '2013-12-12', 'Categoria 1');
INSERT INTO EMPREGADOFIXO (NSS, Salario, DataAlta, Categoria) VALUES ('1100222', 5500.00, '2014-05-11', 'Categoria 1');
INSERT INTO EMPREGADOFIXO (NSS, Salario, DataAlta, Categoria) VALUES ('1111111', 600.00, '2013-03-12', 'Categoria 4');
INSERT INTO EMPREGADOFIXO (NSS, Salario, DataAlta, Categoria) VALUES ('1122331', 1000.00, '2012-01-11', 'Categoria 2');
INSERT INTO EMPREGADOFIXO (NSS, Salario, DataAlta, Categoria) VALUES ('1231231', 2500.00, '2000-03-22', 'Categoria 1');
INSERT INTO EMPREGADOFIXO (NSS, Salario, DataAlta, Categoria) VALUES ('2525252', 1100.00, '2000-12-12', 'Categoria 2');
INSERT INTO EMPREGADOFIXO (NSS, Salario, DataAlta, Categoria) VALUES ('3330000', 1100.00, '2001-06-07', 'Categoria 2');
INSERT INTO EMPREGADOFIXO (NSS, Salario, DataAlta, Categoria) VALUES ('6000006', 2600.00, '2001-06-07', 'Categoria 1');
INSERT INTO EMPREGADOFIXO (NSS, Salario, DataAlta, Categoria) VALUES ('6565656', 3500.00, '2000-12-12', 'Categoria 1');
INSERT INTO EMPREGADOFIXO (NSS, Salario, DataAlta, Categoria) VALUES ('7777777', 1200.00, '2000-03-10', 'Categoria 3');
INSERT INTO EMPREGADOFIXO (NSS, Salario, DataAlta, Categoria) VALUES ('8888889', 1000.00, '2012-07-17', 'Categoria 2');
INSERT INTO EMPREGADOFIXO (NSS, Salario, DataAlta, Categoria) VALUES ('9900000', 2600.00, '2014-05-11', 'Categoria 1');
INSERT INTO EMPREGADOFIXO (NSS, Salario, DataAlta, Categoria) VALUES ('9998888', 3500.00, '2013-12-12', 'Categoria 1');

-- INSERTS EMPREGADOTEMPORAL
INSERT INTO EMPREGADOTEMPORAL (NSS, DataInicio, DataFin, CosteHora, NumHoras) VALUES ('1341431', '2007-02-23', '2025-02-13', 10.00, 25.00);
INSERT INTO EMPREGADOTEMPORAL (NSS, DataInicio, DataFin, CosteHora, NumHoras) VALUES ('2221111', '2006-06-07', '2020-02-02', 15.00, 20.00);
INSERT INTO EMPREGADOTEMPORAL (NSS, DataInicio, DataFin, CosteHora, NumHoras) VALUES ('3338883', '2006-06-07', '2024-02-02', 18.00, 32.00);
INSERT INTO EMPREGADOTEMPORAL (NSS, DataInicio, DataFin, CosteHora, NumHoras) VALUES ('4044443', '2007-02-13', '2024-06-12', 11.00, 20.00);
INSERT INTO EMPREGADOTEMPORAL (NSS, DataInicio, DataFin, CosteHora, NumHoras) VALUES ('4444444', '2007-06-17', '2017-02-02', 11.00, 25.00);
INSERT INTO EMPREGADOTEMPORAL (NSS, DataInicio, DataFin, CosteHora, NumHoras) VALUES ('4444999', '2008-06-17', '2019-05-05', 12.00, 32.00);
INSERT INTO EMPREGADOTEMPORAL (NSS, DataInicio, DataFin, CosteHora, NumHoras) VALUES ('5000000', '2003-06-21', '2020-12-11', 10.00, 30.00);
INSERT INTO EMPREGADOTEMPORAL (NSS, DataInicio, DataFin, CosteHora, NumHoras) VALUES ('5555000', '2006-06-07', '2020-02-02', 15.00, 20.00);
INSERT INTO EMPREGADOTEMPORAL (NSS, DataInicio, DataFin, CosteHora, NumHoras) VALUES ('6000600', '2007-02-13', '2020-02-02', 11.00, 20.00);
INSERT INTO EMPREGADOTEMPORAL (NSS, DataInicio, DataFin, CosteHora, NumHoras) VALUES ('7000007', '2007-01-30', '2017-02-02', 12.00, 35.00);
INSERT INTO EMPREGADOTEMPORAL (NSS, DataInicio, DataFin, CosteHora, NumHoras) VALUES ('8888877', '2008-06-17', '2019-05-05', 12.00, 30.00);
INSERT INTO EMPREGADOTEMPORAL (NSS, DataInicio, DataFin, CosteHora, NumHoras) VALUES ('9876567', '2003-06-21', '2020-12-11', 10.00, 30.00);
INSERT INTO EMPREGADOTEMPORAL (NSS, DataInicio, DataFin, CosteHora, NumHoras) VALUES ('9990009', '2001-06-07', '2020-02-02', 10.00, 30.00);

-- INSERTS LUGAR
SET IDENTITY_INSERT LUGAR OFF;
INSERT INTO LUGAR (Num_departamento, Lugar) VALUES (1, 'VIGO');
INSERT INTO LUGAR (Num_departamento, Lugar) VALUES (2, 'VIGO');
INSERT INTO LUGAR (Num_departamento, Lugar) VALUES (3, 'LUGO');
INSERT INTO LUGAR (Num_departamento, Lugar) VALUES (3, 'MONFORTE');
INSERT INTO LUGAR (Num_departamento, Lugar) VALUES (3, 'PONTEVEDRA');
INSERT INTO LUGAR (Num_departamento, Lugar) VALUES (3, 'SANTIAGO');
INSERT INTO LUGAR (Num_departamento, Lugar) VALUES (4, 'SANTIAGO');
INSERT INTO LUGAR (Num_departamento, Lugar) VALUES (4, 'VIGO');
INSERT INTO LUGAR (Num_departamento, Lugar) VALUES (5, 'A ESTRADA');
INSERT INTO LUGAR (Num_departamento, Lugar) VALUES (5, 'SANTIAGO');
INSERT INTO LUGAR (Num_departamento, Lugar) VALUES (6, 'RIBADEO');
INSERT INTO LUGAR (Num_departamento, Lugar) VALUES (6, 'SANTIAGO');
GO
---
---- ACTIVACIÓN E VALIDACIÓN DE RESTRICCIÓNS
---

-- Reactiva e valida todas as restricións. Se algunha regra foi violada durante a inserción, fallará aquí.
PRINT 'Activando e validando restricións (FKs, CHECKs)...';
ALTER TABLE EMPREGADO WITH CHECK CHECK CONSTRAINT ALL;
ALTER TABLE DEPARTAMENTO WITH CHECK CHECK CONSTRAINT ALL;
ALTER TABLE EMPREGADO_PROXECTO WITH CHECK CHECK CONSTRAINT ALL;
ALTER TABLE EMPREGADOFIXO WITH CHECK CHECK CONSTRAINT ALL;
ALTER TABLE EMPREGADOTEMPORAL WITH CHECK CHECK CONSTRAINT ALL;
ALTER TABLE PROXECTO WITH CHECK CHECK CONSTRAINT ALL;
ALTER TABLE LUGAR WITH CHECK CHECK CONSTRAINT ALL;
GO
PRINT 'Script completado. A base de datos BDEMPRESA25 está lista.';