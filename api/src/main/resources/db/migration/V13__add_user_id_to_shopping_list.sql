ALTER TABLE shopping_list
ADD COLUMN user_id BIGINT;

ALTER TABLE shopping_list
ADD CONSTRAINT fk_shopping_list_user
FOREIGN KEY (user_id) REFERENCES users(id);
