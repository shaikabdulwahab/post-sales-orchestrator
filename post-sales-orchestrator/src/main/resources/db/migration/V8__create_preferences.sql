CREATE TABLE preference (
    id BIGINT NOT NULL AUTO_INCREMENT,
    tower_id BIGINT,
    block_id BIGINT,
    configuration VARCHAR(255),
    area_from DECIMAL(10, 2),
    area_to DECIMAL(10, 2),
    price_from_rate DECIMAL(10, 2),
    price_from_unit VARCHAR(10),
    price_to_rate DECIMAL(10, 2),
    price_to_unit VARCHAR(10),
    facing_preference BOOLEAN DEFAULT FALSE,
    floor_preference BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_preference_tower FOREIGN KEY (tower_id) REFERENCES tower (id) ON DELETE CASCADE,
    CONSTRAINT fk_preference_block FOREIGN KEY (block_id) REFERENCES block (id) ON DELETE CASCADE
);
