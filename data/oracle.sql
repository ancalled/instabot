DROP TABLE POSTS CASCADE CONSTRAINTS;
DROP TABLE ORDERS CASCADE CONSTRAINTS;
DROP TABLE PAYMENTS CASCADE CONSTRAINTS;
DROP SEQUENCE posts_seq;
DROP SEQUENCE orders_seq;
DROP SEQUENCE payments_seq;

CREATE TABLE POSTS (
  id                  INT PRIMARY KEY,
  post_id             VARCHAR(500) UNIQUE,
  link                VARCHAR(500),
  user_id             VARCHAR(500),
  user_name           VARCHAR(500),
  whenCreated         TIMESTAMP,
  caption_id          VARCHAR(500),
  caption_text        CLOB,
  caption_whenCreated TIMESTAMP,
  product_name        VARCHAR(500),
  price               DECIMAL(10, 2),
  qty                 INT,
  leaves_qty          INT,
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

CREATE TABLE ORDERS (
  id          INT PRIMARY KEY,
  comment_id  VARCHAR(500) UNIQUE,
  post_id     VARCHAR(500),
  qty         INT,
  text        CLOB,
  user_id     VARCHAR(500),
  user_name   VARCHAR(500),
  whenCreated TIMESTAMP,
  FOREIGN KEY (post_id) REFERENCES POSTS (post_id)
);

CREATE SEQUENCE orders_seq;
CREATE OR REPLACE TRIGGER orders_trg
BEFORE INSERT ON ORDERS
FOR EACH ROW
  BEGIN
    SELECT orders_seq.NEXTVAL
    INTO :new.id
    FROM dual;
  END;

CREATE TABLE PAYMENTS (
  id             INT PRIMARY KEY,
  payment_id     INT UNIQUE,
  customer       VARCHAR(500),
  merchant       VARCHAR(500),
  discount_price DECIMAL(10, 2),
  payment_status INT,
  status         INT,
  whenCreated    TIMESTAMP
);

CREATE SEQUENCE payments_seq;
CREATE OR REPLACE TRIGGER payments_trg
BEFORE INSERT ON PAYMENTS
FOR EACH ROW
  BEGIN
    SELECT payments_seq.NEXTVAL
    INTO :new.id
    FROM dual;
  END;