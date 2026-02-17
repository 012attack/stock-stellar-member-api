CREATE TABLE favorites (
    id SERIAL PRIMARY KEY,
    target_type VARCHAR(20) NOT NULL,
    target_id INT NOT NULL,
    member_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_favorite_member FOREIGN KEY (member_id) REFERENCES member(id),
    CONSTRAINT uk_favorite_member_target UNIQUE (member_id, target_type, target_id)
);

CREATE INDEX idx_favorites_member ON favorites(member_id);
CREATE INDEX idx_favorites_target ON favorites(target_type, target_id);
