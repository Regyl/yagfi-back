SET search_path TO gfi;

ALTER TABLE e_log ADD COLUMN IF NOT EXISTS utm_source VARCHAR(255);
