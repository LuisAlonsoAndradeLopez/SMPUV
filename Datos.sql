USE SMPUV;

INSERT INTO Marcas (nombre) VALUES ('AMD'), ('Intel'), ('Gigabyte'), ('Asus'), ('Samsung'), ('EVGA'), ('MSI'), ('ASRock'), ('HP'), ('Zotac'), ('XPG'), ('Cooler Master'), ('Corsair'), ('AeroCool'), ('Balam Rush'), ('Cougar');

INSERT INTO Refacciones (NumSerie, nombre, stock, tipo, precio) VALUES
('90MB1DG0-M0EAY0','Tarjeta Madre ASUS Micro-ATX TUF GAMING B760M-PLUS WIFI D4, S-1700, Intel B760, HDMI, 128GB DDR4 para Intel',4,'Tarjeta Madre',3659.00),
('ZT-A30500H-10M','Tarjeta de Video Zotac NVIDIA GeForce RTX 3050 Twin, 8GB 128-bit GDDR6, PCI Express x8 4.0',10,'Tarjeta de video',5149.00),
('100-100000926WOF','Procesador AMD Ryzen 7 5700X, S-AM4, 3.40GHz, 8-Core, 32MB L3 Cache - no incluye Disipador',1,'Procesador',2779.00),
('GP-GSTFS31240GNTD','SSD Gigabyte GP-GSTFS31240GNTD, 240GB, SATA III, 2.5'', 7mm',3,'Almacenamiento',303.00),
('7EH68AA','Memoria RAM HP 7EH68AA DDR4, 3200MHz, 16GB, Non-ECC, CL16',9,'RAM',609.00),
('220-GT-0850-Y1','Fuente de Poder EVGA SuperNOVA 850 GT 80 PLUS Gold, 24-pin ATX, 850W',6,'Fuente de poder',2259.00),
('90DC0040-B49000','Gabinete ASUS TUF Gaming GT301 con Ventana, Midi-Tower, ATX/Micro-ATX/Mini-ATX, USB 3.2, sin Fuente',4,'Gabinete',2219.00),
('GP-AR120RFAN','Ventilador AORUS 120 ARGB FAN, 120mm, 800 - 1700RPM, Negro',3,'Ventilacion',423.00);

INSERT INTO EquiposComputo VALUES
('5', 1, 'SFX', 'Core i5-10600KF', 'GeForce RTX 3070 Ti', 'DDR4', 'Mini-ITX', 'Seagate Barracuda 2 TB', '2023-05-31', 'En Mantenimiento', 'Escritorio'),
('54-d_e03', 1, 'SFX', 'Core i5-10600KF', 'GeForce RTX 3070 Ti', 'DDR4', 'Mini-ITX', 'Seagate Barracuda 2 TB', '2023-06-01', 'Activo', 'Escritorio'),
('74823', 1, 'TFX', 'Core i5-10600KF', 'NVIDIA RTX A3000', 'DDR4', 'Mini-ITX', 'SEAGATE IronWolf 125', '2023-06-22', 'Activo', 'Escritorio');

INSERT INTO Técnicos (NumPersonal, contraseña) VALUES (123456789, SHA2('basquetbol', 256));


/*SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE Marcas;
TRUNCATE Refacciones;
TRUNCATE EquiposComputo;
TRUNCATE Técnicos;
TRUNCATE Servicios;
SET FOREIGN_KEY_CHECKS = 1;*/
