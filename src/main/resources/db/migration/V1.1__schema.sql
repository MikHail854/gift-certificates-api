CREATE TABLE gift_certificate_tag
(
    gift_certificate_id INT,
    tag_id              INT
);

CREATE TABLE gift_certificate
(
    id               SERIAL UNIQUE NOT NULL,
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
    id   SERIAL UNIQUE NOT NULL,
    name VARCHAR(25) UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE user_data
(
    id         SERIAL UNIQUE NOT NULL,
    first_name VARCHAR(30)   NOT NULL,
    last_name  VARCHAR(30)   NOT NULL
);

CREATE TABLE order_data
(
    id             SERIAL UNIQUE  NOT NULL,
    user_id        INTEGER        NOT NULL,
    certificate_id INTEGER        NOT NULL,
    price          NUMERIC(10, 2) NOT NULL,
    purchase_date  TIMESTAMP WITHOUT TIME ZONE,
    FOREIGN KEY (user_id) references user_data (id),
    FOREIGN KEY (certificate_id) references gift_certificate (id)
);