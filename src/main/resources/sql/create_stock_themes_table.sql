CREATE TABLE stock_themes (
    stock_id INT NOT NULL,
    theme_id INT NOT NULL,
    PRIMARY KEY (stock_id, theme_id),
    CONSTRAINT fk_stock_themes_stock FOREIGN KEY (stock_id) REFERENCES stocks(id),
    CONSTRAINT fk_stock_themes_theme FOREIGN KEY (theme_id) REFERENCES themes(id)
);

CREATE INDEX idx_stock_themes_stock ON stock_themes(stock_id);
CREATE INDEX idx_stock_themes_theme ON stock_themes(theme_id);
