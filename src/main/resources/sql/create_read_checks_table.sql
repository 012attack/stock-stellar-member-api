CREATE TABLE read_checks (
    id SERIAL PRIMARY KEY,
    target_type VARCHAR(20) NOT NULL,
    target_id INT NOT NULL,
    member_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_read_check_member FOREIGN KEY (member_id) REFERENCES member(id),
    CONSTRAINT uk_read_check_member_target UNIQUE (member_id, target_type, target_id)
);

CREATE INDEX idx_read_checks_member ON read_checks(member_id);
CREATE INDEX idx_read_checks_target ON read_checks(target_type, target_id);
