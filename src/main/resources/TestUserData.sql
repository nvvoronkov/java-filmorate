INSERT INTO users (login, user_name, email, birthday)
VALUES ('TestUserLogin1', 'TestUserName1', 'TestUserEmail1@ru.ru', '2011-10-11');

INSERT INTO users (login, user_name, email, birthday)
VALUES ('TestUserLogin2', 'TestUserName2', 'TestUserEmail2@ru.ru', '2012-10-12');

INSERT INTO users (login, user_name, email, birthday)
VALUES ('TestUserLogin3', 'TestUserName3', 'TestUserEmail3@ru.ru', '2013-10-13');

INSERT INTO users (login, user_name, email, birthday)
VALUES ('TestUserLogin4', 'TestUserName4', 'TestUserEmail4@ru.ru', '2014-10-14');

DELETE
FROM users
WHERE user_id > 4;

DELETE
FROM users_friends;