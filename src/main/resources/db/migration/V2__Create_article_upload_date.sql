
CREATE TABLE article_upload_date (
        id bigint NOT NULL AUTO_INCREMENT,
        day varchar(50) NOT NULL,
        series_id bigint NOT NULL,
        PRIMARY KEY (id)
);

ALTER TABLE series DROP COLUMN upload_date;
