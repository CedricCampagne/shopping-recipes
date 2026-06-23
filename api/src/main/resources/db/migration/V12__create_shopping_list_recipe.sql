CREATE TABLE shopping_list_recipe (
    id BIGSERIAL PRIMARY KEY,
    recipe_id BIGINT NOT NULL,
    shopping_list_id BIGINT NOT NULL,
    servings INT NOT NULL,

    CONSTRAINT fk_recipe
        FOREIGN KEY (recipe_id) REFERENCES recipe(id),

    CONSTRAINT fk_recipe_list
        FOREIGN KEY (shopping_list_id) REFERENCES shopping_list(id)
);
