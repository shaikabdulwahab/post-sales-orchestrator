CREATE TABLE project (
    id BIGINT NOT NULL AUTO_INCREMENT,
    client_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    project_logo VARCHAR(255),
    banner_logo VARCHAR(255),
    strategic_partner_logo VARCHAR(255),
    PRIMARY KEY (id),
    CONSTRAINT fk_project_client FOREIGN KEY (client_id) REFERENCES clients (id) ON DELETE CASCADE
);