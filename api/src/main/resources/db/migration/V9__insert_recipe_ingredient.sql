-- ============================
-- Recette 1 : Pâtes au chorizo
-- id recette = 1
-- ============================

INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity_per_person, unit) VALUES
(1, 1, 80, 'g'),        -- Pâtes
(1, 2, 50, 'g'),        -- Chorizo
(1, 3, 0.5, 'pièce'),   -- Oignon blanc
(1, 4, 50, 'ml'),       -- Crème fraîche
(1, 5, 20, 'g');        -- Cheddar râpé


-- ============================
-- Recette 2 : Riz bœuf coréen
-- id recette = 2
-- ============================

INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity_per_person, unit) VALUES
(2, 6, 100, 'g'),       -- Riz
(2, 7, 120, 'g'),       -- Bœuf haché
(2, 3, 0.5, 'pièce'),   -- Oignon blanc
(2, 8, 5, 'g'),         -- Épices coréennes
(2, 9, 5, 'g'),         -- Sucre
(2, 10, 10, 'ml');      -- Sauce soja


-- ============================
-- Recette 3 : Salade thon patate
-- id recette = 3
-- ============================

INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity_per_person, unit) VALUES
(3, 11, 200, 'g'),      -- Pommes de terre
(3, 12, 100, 'g'),      -- Thon en boîte
(3, 13, 0.5, 'pièce'),  -- Oignon rouge
(3, 14, 30, 'g'),       -- Cornichons
(3, 15, 20, 'g');       -- Mayonnaise
