CREATE TABLE clients (
    id BIGINT NOT NULL AUTO_INCREMENT,
    domain_url VARCHAR(255) UNIQUE,
    email VARCHAR(255),
    phone VARCHAR(50),
    PRIMARY KEY (id)
);
