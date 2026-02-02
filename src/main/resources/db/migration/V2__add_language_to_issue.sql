SET search_path TO gfi;

ALTER TABLE e_issue_1
    ADD COLUMN IF NOT EXISTS language VARCHAR(255);

ALTER TABLE e_issue_2
    ADD COLUMN IF NOT EXISTS language VARCHAR(255);
