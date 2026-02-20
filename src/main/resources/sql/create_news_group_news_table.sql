CREATE TABLE news_group_news (
    news_group_id INT NOT NULL,
    news_id INT NOT NULL,
    PRIMARY KEY (news_group_id, news_id),
    CONSTRAINT fk_news_group_news_group FOREIGN KEY (news_group_id) REFERENCES news_groups(id),
    CONSTRAINT fk_news_group_news_news FOREIGN KEY (news_id) REFERENCES news(id)
);

CREATE INDEX idx_news_group_news_group ON news_group_news(news_group_id);
CREATE INDEX idx_news_group_news_news ON news_group_news(news_id);
