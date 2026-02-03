CREATE SCHEMA IF NOT EXISTS gfi;
SET search_path TO gfi;

CREATE TABLE IF NOT EXISTS e_repository_1
(
    id          BIGSERIAL PRIMARY KEY,
    created     TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    source_id   VARCHAR(255)             NOT NULL UNIQUE,
    title       VARCHAR(255)             NOT NULL,
    url         VARCHAR(255)             NOT NULL,
    stars       INTEGER                  NOT NULL,
    description TEXT,
    language    VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS e_repository_2
(
    id          BIGSERIAL PRIMARY KEY,
    created     TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    source_id   VARCHAR(255)             NOT NULL UNIQUE,
    title       VARCHAR(255)             NOT NULL,
    url         VARCHAR(255)             NOT NULL,
    stars       INTEGER                  NOT NULL,
    description TEXT,
    language    VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS e_issue_1
(
    id            BIGSERIAL PRIMARY KEY,
    created       TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    source_id     VARCHAR(255)             NOT NULL UNIQUE,
    title         TEXT                     NOT NULL,
    url           VARCHAR(255)             NOT NULL,
    updated_at    TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at    TIMESTAMP WITH TIME ZONE NOT NULL,
    repository_id BIGINT                   NOT NULL,
    labels        VARCHAR[],
    CONSTRAINT fk_issue_repository FOREIGN KEY (repository_id)
        REFERENCES e_repository_1 (id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS e_issue_2
(
    id            BIGSERIAL PRIMARY KEY,
    created       TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    source_id     VARCHAR(255)             NOT NULL UNIQUE,
    title         TEXT                     NOT NULL,
    url           VARCHAR(255)             NOT NULL,
    updated_at    TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at    TIMESTAMP WITH TIME ZONE NOT NULL,
    repository_id BIGINT                   NOT NULL,
    labels        VARCHAR[],
    CONSTRAINT fk_issue_repository FOREIGN KEY (repository_id)
        REFERENCES e_repository_2 (id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS e_event
(
    id               BIGSERIAL PRIMARY KEY,
    source           VARCHAR(255)             NOT NULL UNIQUE,
    last_update_dttm TIMESTAMP WITH TIME ZONE NOT NULL,
    created          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE VIEW issue_v as select * from e_issue_1;
CREATE VIEW repository_v as select * from e_repository_1;

CREATE TABLE IF NOT EXISTS gfi.e_log
(
    id             BIGSERIAL PRIMARY KEY,
    created        TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    url            TEXT                     NOT NULL,
    http_method    VARCHAR(10)              NOT NULL,
    request_body   JSONB,
    country        VARCHAR(20),
    os             VARCHAR(50),
    browser_family VARCHAR(50),
    device_type    VARCHAR(20)
);