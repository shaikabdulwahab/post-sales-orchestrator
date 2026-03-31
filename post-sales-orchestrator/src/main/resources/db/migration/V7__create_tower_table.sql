CREATE TABLE tower (
    id BIGINT NOT NULL AUTO_INCREMENT,
    phase_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    total_floors SMALLINT UNSIGNED,
    max_units_per_floor TINYINT UNSIGNED,
    oc BOOLEAN DEFAULT FALSE,
    rera_status VARCHAR(50),
    construction_status VARCHAR(50),
    inventory_type JSON,
    conditions JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_tower_phase FOREIGN KEY (phase_id) REFERENCES phase (id) ON DELETE CASCADE
);

CREATE TABLE block (
    id BIGINT NOT NULL AUTO_INCREMENT,
    phase_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    oc BOOLEAN DEFAULT FALSE,
    rera_status VARCHAR(50),
    construction_status VARCHAR(50),
    inventory_type JSON,
    conditions JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_block_phase FOREIGN KEY (phase_id) REFERENCES phase (id) ON DELETE CASCADE
);