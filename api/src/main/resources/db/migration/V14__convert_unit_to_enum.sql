-- Convertir la colonne pour être sûre qu'elle accepte l'ENUM en texte
ALTER TABLE ingredient
    ALTER COLUMN unit TYPE VARCHAR(20);

-- Convertir les anciennes valeurs en valeurs de l'ENUM
UPDATE ingredient SET unit = 'G' WHERE unit = 'g';
UPDATE ingredient SET unit = 'ML' WHERE unit = 'ml';
UPDATE ingredient SET unit = 'PIECE' WHERE unit = 'pièce';
