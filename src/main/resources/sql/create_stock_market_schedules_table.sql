CREATE TABLE stock_market_schedules (
    id SERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    schedule_date DATE NOT NULL,
    member_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_stock_market_schedule_member FOREIGN KEY (member_id) REFERENCES member(id)
);

CREATE INDEX idx_stock_market_schedules_member ON stock_market_schedules(member_id);
CREATE INDEX idx_stock_market_schedules_date ON stock_market_schedules(schedule_date);
