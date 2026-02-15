CREATE TABLE news_themes (
    news_id INT NOT NULL,
    theme_id INT NOT NULL,
    PRIMARY KEY (news_id, theme_id),
    CONSTRAINT fk_news_themes_news FOREIGN KEY (news_id) REFERENCES news(id),
    CONSTRAINT fk_news_themes_theme FOREIGN KEY (theme_id) REFERENCES themes(id)
);

CREATE INDEX idx_news_themes_news ON news_themes(news_id);
CREATE INDEX idx_news_themes_theme ON news_themes(theme_id);
