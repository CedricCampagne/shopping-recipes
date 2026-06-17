CREATE TABLE recipe_ingredient (
    id SERIAL PRIMARY KEY,
    recipe_id BIGINT NOT NULL,
    ingredient_id BIGINT NOT NULL,
    quantity_per_person DECIMAL(10,2) NOT NULL,
    unit VARCHAR(50) NOT NULL,

    CONSTRAINT fk_recipe
        FOREIGN KEY (recipe_id)
        REFERENCES recipe(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_ingredient
        FOREIGN KEY (ingredient_id)
        REFERENCES ingredient(id)
        ON DELETE CASCADE
);
