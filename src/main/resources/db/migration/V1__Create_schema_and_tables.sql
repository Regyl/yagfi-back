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
    language    VARCHAR(255),
    source      VARCHAR(255)             NOT NULL
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
    language    VARCHAR(255),
    source      VARCHAR(255)             NOT NULL
);

CREATE TABLE IF NOT EXISTS e_issue_1 (
    id BIGSERIAL PRIMARY KEY,
    created TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    source_id VARCHAR(255) NOT NULL UNIQUE,
    title TEXT NOT NULL,
    url VARCHAR(255) NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    repository_id BIGINT NOT NULL,
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
    CONSTRAINT fk_issue_repository FOREIGN KEY (repository_id)
        REFERENCES e_repository_2 (id)
        ON DELETE CASCADE
);

CREATE VIEW issue_v as select * from e_issue_1;
CREATE VIEW repository_v as select * from e_repository_1;