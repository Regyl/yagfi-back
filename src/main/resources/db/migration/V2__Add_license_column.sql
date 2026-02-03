ALTER TABLE gfi.e_repository_1
    ADD COLUMN IF NOT EXISTS license VARCHAR(255);

ALTER TABLE gfi.e_repository_2
    ADD COLUMN IF NOT EXISTS license VARCHAR(255);
