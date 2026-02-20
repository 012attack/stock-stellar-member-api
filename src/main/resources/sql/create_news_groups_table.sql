CREATE TABLE news_groups (
    id SERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    member_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_news_groups_member FOREIGN KEY (member_id) REFERENCES member(id)
);

CREATE INDEX idx_news_groups_member ON news_groups(member_id);
