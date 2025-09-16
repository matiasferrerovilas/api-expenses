--liquibase formatted sql
--changeset splitStatements:true

DROP TABLE IF EXISTS gastos;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS app_users;
DROP TABLE IF EXISTS currency;

CREATE TABLE currency (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          symbol VARCHAR(5) NOT NULL UNIQUE,
                          description VARCHAR(20) NOT NULL UNIQUE,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
INSERT INTO currency(symbol, description) VALUES ("ARS", "Peso Argentino"),
                                                 ("USD", "Dolar Americano"),
                                                 ("EUR", "Euro"),
                                                 ("CHF", "Franco Suizo");
CREATE TABLE category (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            description VARCHAR(50) NOT NULL UNIQUE,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO category (description) VALUES
                                         ('Hogar'),
                                         ('Regalos'),
                                         ('Restaurante'),
                                         ('Ropa'),
                                         ('Servicios'),
                                         ('Streaming'),
                                         ('Supermercado'),
                                         ('Tecnologia'),
                                         ('Transporte'),
                                         ('Viaje');

-- =====================================================
-- Tabla: gastos
-- Descripción: Tabla principal de gastos con herencia Single Table
-- Tipos: CREDITO, DEBITO
-- =====================================================
CREATE TABLE gastos (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        tipo_gasto VARCHAR(20) NOT NULL,
                        date_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        amount DECIMAL(15,2) NOT NULL,
                        description VARCHAR(255),
                        category_id BIGINT NULL,
                        user VARCHAR(50) NOT NULL,
                        currency_id BIGINT NOT NULL,
                        cuota_actual INT,
                        cuotas_totales INT,
                        year INT NOT NULL,
                        month INT NOT NULL,
                        bank VARCHAR(30) NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                        CONSTRAINT fk_gastos_category
                            FOREIGN KEY (category_id) REFERENCES category(id)
                                ON DELETE RESTRICT ON UPDATE CASCADE,

                        CONSTRAINT fk_gastos_currency
                            FOREIGN KEY (currency_id) REFERENCES currency(id)
                                ON DELETE RESTRICT ON UPDATE CASCADE,

                        CONSTRAINT chk_debito_cuotas_null
                            CHECK (
                                (tipo_gasto != 'DEBITO') OR
                                (cuota_actual IS NULL AND cuotas_totales IS NULL)
                                ),

                        INDEX idx_gastos_tipo (tipo_gasto),
                        INDEX idx_gastos_category (category_id),
                        INDEX idx_gastos_user_category (user, category_id),
                        INDEX idx_gastos_amount (amount)
)