-- Index sur les colonnes de clés étrangères (non indexées automatiquement par PostgreSQL)

CREATE INDEX idx_recipe_ingredient_recipe_id ON recipe_ingredient (recipe_id);
CREATE INDEX idx_recipe_ingredient_ingredient_id ON recipe_ingredient (ingredient_id);

CREATE INDEX idx_shopping_list_item_shopping_list_id ON shopping_list_item (shopping_list_id);
CREATE INDEX idx_shopping_list_item_ingredient_id ON shopping_list_item (ingredient_id);

CREATE INDEX idx_shopping_list_user_id ON shopping_list (user_id);

CREATE INDEX idx_shopping_list_recipe_recipe_id ON shopping_list_recipe (recipe_id);