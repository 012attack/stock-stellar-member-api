CREATE TABLE opinions (
    id SERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    target_type VARCHAR(20) NOT NULL,
    target_id INT NOT NULL,
    member_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_opinion_member FOREIGN KEY (member_id) REFERENCES member(id)
);

CREATE INDEX idx_opinions_target ON opinions(target_type, target_id);
CREATE INDEX idx_opinions_member ON opinions(member_id);
