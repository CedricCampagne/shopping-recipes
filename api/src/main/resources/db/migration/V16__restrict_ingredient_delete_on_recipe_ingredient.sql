ALTER TABLE recipe_ingredient
    DROP CONSTRAINT fk_ingredient;

ALTER TABLE recipe_ingredient
    ADD CONSTRAINT fk_ingredient
    FOREIGN KEY (ingredient_id) REFERENCES ingredient(id)
    ON DELETE RESTRICT;