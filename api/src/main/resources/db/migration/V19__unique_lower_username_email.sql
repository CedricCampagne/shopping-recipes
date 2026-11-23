-- Unicité insensible à la casse : Cedric et cedric ne peuvent pas coexister
CREATE UNIQUE INDEX uq_users_username_lower ON users (LOWER(username));
CREATE UNIQUE INDEX uq_users_email_lower ON users (LOWER(email));