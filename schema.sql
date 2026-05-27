-- 1. Crear la base de datos si no existe y usarla
CREATE DATABASE IF NOT EXISTS tienda_funkos;
USE tienda_funkos;

-- 2. Eliminar tablas previas en orden inverso por si acaso
DROP TABLE IF EXISTS pedidos;
DROP TABLE IF EXISTS clientes;
DROP TABLE IF EXISTS funkos;

-- 3. Tabla de Funko Pops
CREATE TABLE funkos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    franquicia VARCHAR(100) NOT NULL,
    precio DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL
);

-- 4. Tabla de Clientes
CREATE TABLE clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
);

-- 5. Tabla de Pedidos (Relaciona Clientes y Funkos)
CREATE TABLE pedidos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    id_funko INT NOT NULL,
    cantidad INT NOT NULL,
    fecha DATE NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES clientes(id) ON DELETE CASCADE,
    FOREIGN KEY (id_funko) REFERENCES funkos(id) ON DELETE CASCADE
);

-- 6. Insertar datos de prueba para que la tienda no empiece vacía
INSERT INTO funkos (nombre, franquicia, precio, stock) VALUES
('Luffy Gear 5', 'One Piece', 19.99, 10),
('Naruto Modo Sabio', 'Naruto', 17.50, 5),
('Goku Ultrainstinto', 'Dragon Ball', 22.00, 8),
('Batman Dark Knight', 'DC Comics', 15.99, 12),
('Spider-Man No Way Home', 'Marvel', 16.00, 15);

INSERT INTO clientes (nombre, email) VALUES
('Juan Pérez', 'juan@gmail.com'),
('María López', 'maria@hotmail.com');

INSERT INTO pedidos (id_cliente, id_funko, cantidad, fecha) VALUES
(1, 1, 2, CURDATE()),
(2, 3, 1, CURDATE());