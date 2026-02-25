CREATE TABLE stock_news (
    stock_id INT NOT NULL,
    news_id INT NOT NULL,
    PRIMARY KEY (stock_id, news_id),
    CONSTRAINT fk_stock_news_stock FOREIGN KEY (stock_id) REFERENCES stocks(id),
    CONSTRAINT fk_stock_news_news FOREIGN KEY (news_id) REFERENCES news(id)
);

CREATE INDEX idx_stock_news_stock ON stock_news(stock_id);
CREATE INDEX idx_stock_news_news ON stock_news(news_id);
