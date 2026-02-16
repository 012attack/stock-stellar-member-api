CREATE TABLE stock_group_stocks (
    stock_group_id INT NOT NULL,
    stock_id INT NOT NULL,
    PRIMARY KEY (stock_group_id, stock_id),
    CONSTRAINT fk_stock_group_stocks_group FOREIGN KEY (stock_group_id) REFERENCES stock_groups(id),
    CONSTRAINT fk_stock_group_stocks_stock FOREIGN KEY (stock_id) REFERENCES stocks(id)
);

CREATE INDEX idx_stock_group_stocks_group ON stock_group_stocks(stock_group_id);
CREATE INDEX idx_stock_group_stocks_stock ON stock_group_stocks(stock_id);
