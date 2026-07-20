-- Add NEW to allowed client_stage values (used when converting leads to clients)

ALTER TABLE clients DROP CONSTRAINT chk_clients_stage;

ALTER TABLE clients ADD CONSTRAINT chk_clients_stage CHECK (client_stage IN (
    'NEW', 'INTERESTED', 'IN_PROGRESS', 'ACTIVE', 'ON_HOLD', 'COMPLETED', 'CHURNED'
));
