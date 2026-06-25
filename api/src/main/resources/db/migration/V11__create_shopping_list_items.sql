CREATE TABLE shopping_list_item (
    id BIGSERIAL PRIMARY KEY,
    ingredient_id BIGINT NOT NULL,
    shopping_list_id BIGINT NOT NULL,
    total_quantity DOUBLE PRECISION NOT NULL,
    unit VARCHAR(50) NOT NULL,

    CONSTRAINT fk_item_ingredient
        FOREIGN KEY (ingredient_id) REFERENCES ingredient(id),

    CONSTRAINT fk_item_list
        FOREIGN KEY (shopping_list_id) REFERENCES shopping_list(id)
);
