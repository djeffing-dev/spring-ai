-- Script pour initialiser les rôles dans la base de données
-- INSERT INTO roles (name) VALUES ('USER');
-- INSERT INTO roles (name) VALUES ('MODERATOR');
-- INSERT INTO roles (name) VALUES ('ADMIN');

ALTER TABLE email_generator ALTER COLUMN objectif TYPE TEXT
ALTER TABLE email_generator ALTER COLUMN context TYPE TEXT
