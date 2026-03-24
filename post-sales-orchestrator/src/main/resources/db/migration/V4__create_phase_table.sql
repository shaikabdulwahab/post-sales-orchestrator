CREATE TABLE phase (
    id BIGINT NOT NULL AUTO_INCREMENT,
    project_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL UNIQUE,
    rera_type VARCHAR(20),
    oc VARCHAR(20),
    rera_status VARCHAR(20),
    PRIMARY KEY (id),
    CONSTRAINT fk_phase_project FOREIGN KEY (project_id) REFERENCES project (id) ON DELETE CASCADE
);