CREATE TABLE account_types (
    id SERIAL PRIMARY KEY,
    account_nm VARCHAR(200) NOT NULL UNIQUE
);

-- financial_statements 테이블에 account_type_id FK 추가 및 account_nm 컬럼 제거
-- 1. 기존 account_nm 데이터를 account_types 테이블로 이관
INSERT INTO account_types (account_nm)
SELECT DISTINCT account_nm FROM financial_statements
ON CONFLICT (account_nm) DO NOTHING;

-- 2. account_type_id 컬럼 추가
ALTER TABLE financial_statements ADD COLUMN account_type_id INTEGER;

-- 3. 기존 데이터 매핑
UPDATE financial_statements fs
SET account_type_id = at.id
FROM account_types at
WHERE fs.account_nm = at.account_nm;

-- 4. NOT NULL 제약조건 추가
ALTER TABLE financial_statements ALTER COLUMN account_type_id SET NOT NULL;

-- 5. FK 제약조건 추가
ALTER TABLE financial_statements
ADD CONSTRAINT fk_financial_statements_account_type
FOREIGN KEY (account_type_id) REFERENCES account_types(id);

-- 6. 기존 account_nm 컬럼 제거
ALTER TABLE financial_statements DROP COLUMN account_nm;

-- 7. 인덱스 추가
CREATE INDEX idx_financial_statements_account_type ON financial_statements(account_type_id);
