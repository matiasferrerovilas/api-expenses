--liquibase formatted sql
--changeset splitStatements:true
CREATE TABLE budgets (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         user VARCHAR(50) NOT NULL,
                         year INT NOT NULL,
                         month INT NOT NULL,
                         category_id BIGINT NULL,
                         amount NUMERIC(12,2) NOT NULL,

                         CONSTRAINT fk_budget_category
                         FOREIGN KEY (category_id) REFERENCES category(id)
                            ON DELETE RESTRICT ON UPDATE CASCADE
);
