INSERT INTO gift_certificate (name, description, price, duration, create_date, last_update_date)
VALUES ('name1', 'description1', '128.3', '12', '2022-04-07T13:06:35.76', '2022-04-07T13:06:35.76');

INSERT INTO gift_certificate (name, description, price, duration, create_date, last_update_date)
VALUES ('name2', 'description2', '321', '2', '2022-03-03T13:03:35.76', '2022-04-05T13:06:35.76');

INSERT INTO gift_certificate (name, description, price, duration, create_date, last_update_date)
VALUES ('name3', 'description3', '12345.3', '12', '2021-01-07T15:06:35.76', '2021-05-07T17:07:35.76');

INSERT INTO tag(name)
VALUES ('tag1');

INSERT INTO tag(name)
VALUES ('tag2');

INSERT INTO tag(name)
VALUES ('tag3');


INSERT INTO gift_certificate_tag(gift_certificate_id, tag_id)
VALUES (1, 1);

INSERT INTO gift_certificate_tag(gift_certificate_id, tag_id)
VALUES (1, 2);

INSERT INTO gift_certificate_tag(gift_certificate_id, tag_id)
VALUES (1, 3);

INSERT INTO gift_certificate_tag(gift_certificate_id, tag_id)
VALUES (2, 3);

INSERT INTO gift_certificate_tag(gift_certificate_id, tag_id)
VALUES (3, 3);

INSERT INTO gift_certificate_tag(gift_certificate_id, tag_id)
VALUES (3, 1);


INSERT INTO user_data (first_name, last_name)
VALUES ('Ivanov', 'Ivan');

INSERT INTO user_data (first_name, last_name)
VALUES ('Petrov', 'Petr');

INSERT INTO user_data (first_name, last_name)
VALUES ('Popov', 'Alex');

INSERT INTO user_data (first_name, last_name)
VALUES ('Morozov', 'Dmitriy');


