BEGIN;

CREATE TABLE IF NOT EXISTS authz_authorities (
    id UUID PRIMARY KEY,
    authority_name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS authz_roles (
    id UUID PRIMARY KEY,
    organization_id UUID NOT NULL,
    role_name VARCHAR(255) NOT NULL,
    UNIQUE (organization_id, role_name)
);

CREATE TABLE IF NOT EXISTS authz_role_authority_mapping (
    role_id UUID NOT NULL,
    authority_id UUID NOT NULL,
    PRIMARY KEY (role_id, authority_id),
    CONSTRAINT fk_role_authority_mapping_role
        FOREIGN KEY (role_id)
            REFERENCES authz_roles(id) ON DELETE CASCADE,
    CONSTRAINT fk_role_authority_mapping_authority
        FOREIGN KEY (authority_id)
            REFERENCES authz_authorities(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS authz_user_roles (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    is_current BOOLEAN NOT NULL,
    UNIQUE(user_id, role_id),
    CONSTRAINT fk_user_role_role
        FOREIGN KEY (role_id)
            REFERENCES authz_roles(id) ON DELETE CASCADE
);

COMMIT;