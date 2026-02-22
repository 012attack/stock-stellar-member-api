CREATE TABLE importances (
    id SERIAL PRIMARY KEY,
    target_type VARCHAR(20) NOT NULL,
    target_id INT NOT NULL,
    score NUMERIC(3,1) NOT NULL,
    member_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_importance_member FOREIGN KEY (member_id) REFERENCES member(id),
    CONSTRAINT uk_importance_member_target UNIQUE (member_id, target_type, target_id),
    CONSTRAINT chk_importance_score CHECK (score >= 0 AND score <= 10 AND (score * 2) = FLOOR(score * 2))
);

CREATE INDEX idx_importances_member ON importances(member_id);
CREATE INDEX idx_importances_target ON importances(target_type, target_id);
