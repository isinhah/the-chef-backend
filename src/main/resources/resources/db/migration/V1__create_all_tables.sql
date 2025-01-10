CREATE TABLE restaurantes (
    id UUID PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    cpf_or_cnpj VARCHAR(20) UNIQUE NOT NULL,
    phone VARCHAR(20) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    table_quantity INT NOT NULL,
    waiter_commission DECIMAL(10, 2) NOT NULL
);

CREATE TABLE categorias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    restaurant_id BIGINT,
    FOREIGN KEY (restaurant_id) REFERENCES restaurantes(id) ON DELETE SET NULL
);

CREATE TABLE mesas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    table_number INT NOT NULL,
    restaurant_id UUID,
    FOREIGN KEY (restaurant_id) REFERENCES restaurantes(id) ON DELETE CASCADE
);

CREATE TABLE complementos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    quantity_in_stock INT NOT NULL,
    restaurant_id UUID,
    FOREIGN KEY (restaurant_id) REFERENCES restaurantes(id) ON DELETE CASCADE
);

CREATE TABLE pedidos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    waiter VARCHAR(255),
    start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10, 2) DEFAULT 0.00,
    table_id BIGINT,
    restaurant_id UUID,
    FOREIGN KEY (table_id) REFERENCES mesas(id) ON DELETE SET NULL,
    FOREIGN KEY (restaurant_id) REFERENCES restaurantes(id) ON DELETE CASCADE
);

CREATE TABLE produtos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    image_url VARCHAR(255),
    description TEXT,
    price DECIMAL(5, 2) NOT NULL,
    pdv_code VARCHAR(255),
    quantity_in_stock INT NOT NULL,
    category_id BIGINT,
    restaurant_id UUID,
    FOREIGN KEY (category_id) REFERENCES categorias(id) ON DELETE SET NULL,
    FOREIGN KEY (restaurant_id) REFERENCES restaurantes(id) ON DELETE CASCADE
);

CREATE TABLE itens_do_pedido (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_quantity INT NOT NULL,
    complements_quantity INT NOT NULL,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    FOREIGN KEY (order_id) REFERENCES pedidos(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES produtos(id) ON DELETE CASCADE
);