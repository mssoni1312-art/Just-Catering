-- Align client notes column with Convert Client screen (max 300 characters).
ALTER TABLE clients
    ALTER COLUMN notes TYPE VARCHAR(300);
