BEGIN;

INSERT INTO authz_authorities(id, authority_name) VALUES
(uuidv7(), 'organizations:update'),
(uuidv7(), 'organizations:delete'),
(uuidv7(), 'roles:create'),
(uuidv7(), 'roles:update'),
(uuidv7(), 'roles:delete'),
(uuidv7(), 'roles:view'),
(uuidv7(), 'accounts:update'),
(uuidv7(), 'accounts:create'),
(uuidv7(), 'accounts:view'),
(uuidv7(), 'journals:update'),
(uuidv7(), 'journals:create'),
(uuidv7(), 'journals:view'),
(uuidv7(), 'currencies:update'),
(uuidv7(), 'currencies:create'),
(uuidv7(), 'currencies:view');

COMMIT;