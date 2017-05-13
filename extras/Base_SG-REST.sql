-- Created by Vertabelo (http://vertabelo.com)
-- Last modification date: 2017-05-13 22:31:44.651

-- tables
-- Table: Cargo_Empleado
CREATE TABLE Cargo_Empleado (
    id_cargo serial  NOT NULL,
    cargo varchar(15)  NOT NULL,
    CONSTRAINT Cargo_Empleado_pk PRIMARY KEY (id_cargo)
);
INSERT INTO Cargo_empleado (cargo) Values ('Gerente');
INSERT INTO Cargo_empleado (cargo) Values ('Cajero');
INSERT INTO Cargo_empleado (cargo) Values ('Mesero');

-- Table: Categoria_Producto
CREATE TABLE Categoria_Producto (
    id_categoria serial  NOT NULL,
    nombre_categoria varchar(20)  NOT NULL,
    CONSTRAINT Categoria_Producto_pk PRIMARY KEY (id_categoria)
);
INSERT INTO Categoria_Producto (nombre_categoria) VALUES ('Almuerzo-Cena');
INSERT INTO Categoria_Producto (nombre_categoria) VALUES ('Brunch-Desayunos');
INSERT INTO Categoria_Producto (nombre_categoria) VALUES ('Bebidas');
INSERT INTO Categoria_Producto (nombre_categoria) VALUES ('Helados');

-- Table: Empleado
CREATE TABLE Empleado (
    identificacion varchar(10)  NOT NULL,
    password varchar(50)  NOT NULL,
    nombre varchar(30)  NOT NULL,
    apelllido varchar(30)  NOT NULL,
    cargo int  NOT NULL,
    estado boolean  NOT NULL,
    horario varchar(100)  NOT NULL,
    foto bytea  NULL,
    direccion varchar(30)  NULL,
    telefono_fijo varchar(7)  NULL,
    telefono_celular varchar(10)  NOT NULL,
    email varchar(50)  NULL,
    CONSTRAINT Empleado_pk PRIMARY KEY (identificacion)
);

-- Table: Estado_Pedido
CREATE TABLE Estado_Pedido (
    id_estado_pedido serial  NOT NULL,
    estado varchar(15)  NOT NULL,
    CONSTRAINT Estado_Pedido_pk PRIMARY KEY (id_estado_pedido)
);
INSERT INTO Estado_Pedido (estado) VALUES ('pediente');
INSERT INTO Estado_Pedido (estado) VALUES ('entregado');
INSERT INTO Estado_Pedido (estado) VALUES ('finalizado');


-- Table: Factura
CREATE TABLE Factura (
    id_factura serial  NOT NULL,
    num_pedido serial  NOT NULL,
    valor_total decimal(10)  NOT NULL,
    id_pago int  NOT NULL,
    id_empleado varchar(10)  NOT NULL,
    fecha_hora timestamp  NOT NULL,
    descuento decimal(10)  NOT NULL,
    propina decimal(10)  NOT NULL,
    impuestos decimal(10)  NOT NULL,
    cedula_cliente varchar(10)  NOT NULL,
    CONSTRAINT Factura_pk PRIMARY KEY (id_factura)
);

-- Table: Mesa
CREATE TABLE Mesa (
    num_mesa serial  NOT NULL,
    mesa varchar(10)  NOT NULL,
    CONSTRAINT Mesa_pk PRIMARY KEY (num_mesa)
);
INSERT INTO Mesa (mesa) VALUES ('mesa 1');
INSERT INTO Mesa (mesa) VALUES ('mesa 2');
INSERT INTO Mesa (mesa) VALUES ('mesa 3');
INSERT INTO Mesa (mesa) VALUES ('mesa 4');
INSERT INTO Mesa (mesa) VALUES ('mesa 5');
INSERT INTO Mesa (mesa) VALUES ('mesa 6');
INSERT INTO Mesa (mesa) VALUES ('mesa 7');
INSERT INTO Mesa (mesa) VALUES ('mesa 8');
INSERT INTO Mesa (mesa) VALUES ('mesa 9');
INSERT INTO Mesa (mesa) VALUES ('mesa 10');
INSERT INTO Mesa (mesa) VALUES ('mesa 11');
INSERT INTO Mesa (mesa) VALUES ('mesa 12');
INSERT INTO Mesa (mesa) VALUES ('mesa 13');
INSERT INTO Mesa (mesa) VALUES ('mesa 14');
INSERT INTO Mesa (mesa) VALUES ('mesa 15');

-- Table: Pago
CREATE TABLE Pago (
    id_pago serial  NOT NULL,
    id_tipo int  NOT NULL,
    num_tarjetas int  NOT NULL,
    dinero_efectivo decimal(10)  NOT NULL,
    dinero_tarjetas decimal(10)  NOT NULL,
    CONSTRAINT Pago_pk PRIMARY KEY (id_pago)
);

-- Table: Pedido
CREATE TABLE Pedido (
    num_pedido serial  NOT NULL,
    num_mesa int  NOT NULL,
    hora_inicio_pedido timestamp  NOT NULL,
    hora_final_pedido timestamp  NOT NULL,
    id_empleado varchar(10)  NOT NULL,
    id_estado_pedido int  NOT NULL,
    CONSTRAINT Pedido_pk PRIMARY KEY (num_pedido)
);

-- Table: Producto
CREATE TABLE Producto (
    id serial  NOT NULL,
    nombre varchar(20)  NOT NULL,
    fotografia bytea  NULL,
    precio decimal(10)  NOT NULL,
    descripcion varchar(70)  NULL,
    id_categoria int  NOT NULL,
    estado boolean  NOT NULL,
    CONSTRAINT Producto_pk PRIMARY KEY (id)
);

-- Table: Producto_Pedido
CREATE TABLE Producto_Pedido (
    Pedido_num_pedido int  NOT NULL,
    Producto_id int  NOT NULL,
    CONSTRAINT Producto_Pedido_pk PRIMARY KEY (Pedido_num_pedido,Producto_id)
);

-- Table: Tipo_Pago
CREATE TABLE Tipo_Pago (
    id_tipo serial  NOT NULL,
    nombre_tipo varchar(10)  NOT NULL,
    CONSTRAINT Tipo_Pago_pk PRIMARY KEY (id_tipo)
);
INSERT INTO Tipo_pago (nombre_tipo) Values ('Efectivo');
INSERT INTO Tipo_pago (nombre_tipo) Values ('Tarjeta');
INSERT INTO Tipo_pago (nombre_tipo) Values ('Mixto');


-- Table: producto_factura
CREATE TABLE producto_factura (
    id_factura int  NOT NULL,
    id_producto int  NOT NULL,
    precio decimal(10)  NOT NULL,
    cantidad int  NOT NULL,
    CONSTRAINT producto_factura_pk PRIMARY KEY (id_factura,id_producto)
);

-- foreign keys
-- Reference: Cargo_Empleado_Empleado (table: Empleado)
ALTER TABLE Empleado ADD CONSTRAINT Cargo_Empleado_Empleado
    FOREIGN KEY (cargo)
    REFERENCES Cargo_Empleado (id_cargo)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: Estado_Pedido_Pedido (table: Pedido)
ALTER TABLE Pedido ADD CONSTRAINT Estado_Pedido_Pedido
    FOREIGN KEY (id_estado_pedido)
    REFERENCES Estado_Pedido (id_estado_pedido)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: Factura_Pedido (table: Factura)
ALTER TABLE Factura ADD CONSTRAINT Factura_Pedido
    FOREIGN KEY (num_pedido)
    REFERENCES Pedido (num_pedido)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: Mesa_Pedido (table: Pedido)
ALTER TABLE Pedido ADD CONSTRAINT Mesa_Pedido
    FOREIGN KEY (num_mesa)
    REFERENCES Mesa (num_mesa)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: Pago_Tipo_Pago (table: Pago)
ALTER TABLE Pago ADD CONSTRAINT Pago_Tipo_Pago
    FOREIGN KEY (id_tipo)
    REFERENCES Tipo_Pago (id_tipo)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: Pedido_Empleado (table: Pedido)
ALTER TABLE Pedido ADD CONSTRAINT Pedido_Empleado
    FOREIGN KEY (id_empleado)
    REFERENCES Empleado (identificacion)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: Producto_Categoria_Producto (table: Producto)
ALTER TABLE Producto ADD CONSTRAINT Producto_Categoria_Producto
    FOREIGN KEY (id_categoria)
    REFERENCES Categoria_Producto (id_categoria)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: Table_17_Pedido (table: Producto_Pedido)
ALTER TABLE Producto_Pedido ADD CONSTRAINT Table_17_Pedido
    FOREIGN KEY (Pedido_num_pedido)
    REFERENCES Pedido (num_pedido)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: Table_17_Producto (table: Producto_Pedido)
ALTER TABLE Producto_Pedido ADD CONSTRAINT Table_17_Producto
    FOREIGN KEY (Producto_id)
    REFERENCES Producto (id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: Venta_Pago (table: Factura)
ALTER TABLE Factura ADD CONSTRAINT Venta_Pago
    FOREIGN KEY (id_pago)
    REFERENCES Pago (id_pago)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: producto_factura_Factura (table: producto_factura)
ALTER TABLE producto_factura ADD CONSTRAINT producto_factura_Factura
    FOREIGN KEY (id_factura)
    REFERENCES Factura (id_factura)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- End of file.

