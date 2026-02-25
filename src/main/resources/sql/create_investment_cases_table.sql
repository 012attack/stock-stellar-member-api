CREATE TABLE investment_cases (
    id SERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    result_type VARCHAR(20) NOT NULL,
    member_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_investment_case_member FOREIGN KEY (member_id) REFERENCES member(id)
);

CREATE INDEX idx_investment_cases_member ON investment_cases(member_id);
CREATE INDEX idx_investment_cases_result_type ON investment_cases(result_type);
