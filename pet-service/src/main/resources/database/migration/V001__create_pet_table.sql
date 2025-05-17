CREATE TABLE pets
(
    id         UUID PRIMARY KEY,
    owner_id   UUID,
    "name"     VARCHAR(255)                NOT NULL,
    description VARCHAR(255),
    specie     VARCHAR(255)                NOT NULL,
    breed      VARCHAR(255)                NOT NULL,
    "size"     VARCHAR(255)                NOT NULL,
    status     VARCHAR(255)                NOT NULL,
    gender     VARCHAR(255)                NOT NULL,
    birth_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE
);
