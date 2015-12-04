DROP TABLE POSTS CASCADE CONSTRAINTS;
DROP TABLE COMMENTS CASCADE CONSTRAINTS;
DROP SEQUENCE posts_seq;
DROP SEQUENCE comments_seq;


CREATE TABLE POSTS (
  id                  INT PRIMARY KEY,
  post_id             VARCHAR(500) UNIQUE,
  link                VARCHAR(500),
  type                VARCHAR(500),
  user_id             VARCHAR(500),
  whenCreated         TIMESTAMP,
  caption_id          VARCHAR(500),
  caption_text        CLOB,
  caption_whenCreated TIMESTAMP,
  status              INT
);

CREATE SEQUENCE posts_seq;
CREATE OR REPLACE TRIGGER posts_trg
BEFORE INSERT ON POSTS
FOR EACH ROW
  BEGIN
    SELECT posts_seq.NEXTVAL
    INTO :new.id
    FROM dual;
  END;

CREATE TABLE COMMENTS (
  id          INT PRIMARY KEY,
  comment_id  VARCHAR(500) UNIQUE,
  post_id     VARCHAR(50),
  text        CLOB,
  user_id     VARCHAR(500),
  whenCreated TIMESTAMP,
  FOREIGN KEY (post_id) REFERENCES POSTS (post_id)
);

CREATE SEQUENCE comments_seq;
CREATE OR REPLACE TRIGGER comments_trg
BEFORE INSERT ON COMMENTS
FOR EACH ROW
  BEGIN
    SELECT comments_seq.NEXTVAL
    INTO :new.id
    FROM dual;
  END;