INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');

INSERT into users (username, email, password) VALUES ('Alice', 'alice@gamil.com', '$2a$12$9CHk.045yPPmn82kA6L5H.o5JqZkhySM7genfaE6WcD86mYIpB/pm');

INSERT INTO user_roles(user_id, role_id) VALUES (1, 1);
INSERT INTO user_roles(user_id, role_id) VALUES (1, 2);