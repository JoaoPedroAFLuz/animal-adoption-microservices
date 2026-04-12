CREATE INDEX idx_pets_owner_id ON pets (owner_id);

CREATE INDEX idx_pets_status_specie ON pets (status, specie);

CREATE INDEX idx_pets_featured_created_at ON pets (featured, created_at) WHERE featured = true;

CREATE OR REPLACE FUNCTION immutable_unaccent(text)
RETURNS text AS $$
    SELECT unaccent($1);
$$ LANGUAGE sql IMMUTABLE PARALLEL SAFE;

CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE INDEX idx_pets_name_trgm ON pets USING gin (immutable_unaccent(lower(name)) gin_trgm_ops);
