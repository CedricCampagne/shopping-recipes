-- 1. Convertir toute la colonne en minuscule
UPDATE ingredient SET unit = LOWER(unit);

-- 2. Normaliser les accents
UPDATE ingredient SET unit = 'pièce' WHERE unit IN ('piece', 'pièce', 'PIECE');
UPDATE ingredient SET unit = 'tranche' WHERE unit IN ('tranche', 'TRANCHE');

-- 3. Normaliser les unités simples
UPDATE ingredient SET unit = 'g' WHERE unit IN ('g', 'G');
UPDATE ingredient SET unit = 'kg' WHERE unit IN ('kg', 'KG');
UPDATE ingredient SET unit = 'ml' WHERE unit IN ('ml', 'ML');
UPDATE ingredient SET unit = 'l' WHERE unit IN ('l', 'L');
