CREATE TABLE gift_certificate_tag
(
    gift_certificate_id INT,
    tag_id              INT
);

CREATE TABLE gift_certificate
(
    id               SERIAL NOT NULL,
    name             VARCHAR(25),
    description      VARCHAR(100),
    price            NUMERIC(10, 2),
    duration         INT,
    create_date      TIMESTAMP WITHOUT TIME ZONE,
    last_update_date TIMESTAMP WITHOUT TIME ZONE,
    PRIMARY KEY (id)
);

CREATE TABLE tag
(
    id   SERIAL NOT NULL,
    name VARCHAR(25) UNIQUE,
    PRIMARY KEY (id)
);