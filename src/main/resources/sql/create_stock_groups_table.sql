CREATE TABLE stock_groups (
    id SERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    member_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_stock_groups_member FOREIGN KEY (member_id) REFERENCES member(id)
);

CREATE INDEX idx_stock_groups_member ON stock_groups(member_id);
