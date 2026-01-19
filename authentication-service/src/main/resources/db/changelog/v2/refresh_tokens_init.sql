CREATE TABLE IF NOT EXISTS Security.Refresh_tokens
(
    id          UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    token       TEXT        NOT NULL UNIQUE,
    user_id     UUID        NOT NULL REFERENCES Security.Users (id) ON DELETE CASCADE,
    device_id   UUID        NOT NULL,
    client      TEXT        NOT NULL,
    os          TEXT        NOT NULL,
    device_name TEXT        NOT NULL,
    country     TEXT        NOT NULL,
    city        TEXT        NOT NULL,
    expires_at  TIMESTAMPTZ NOT NULL,
    revoked     BOOLEAN     NOT NULL DEFAULT FALSE,
    revoked_at  TIMESTAMPTZ,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

COMMENT ON TABLE Security.Refresh_tokens IS 'Таблица хранения refresh токенов';
COMMENT ON COLUMN Security.Refresh_tokens.id IS 'Идентификатор';
COMMENT ON COLUMN Security.Refresh_tokens.token IS 'Токен';
COMMENT ON COLUMN Security.Refresh_tokens.user_id IS 'Идентификатор пользователя';
COMMENT ON COLUMN Security.Refresh_tokens.device_id IS 'Идентификатор устройства';
COMMENT ON COLUMN Security.Refresh_tokens.client IS 'Клиент';
COMMENT ON COLUMN Security.Refresh_tokens.os IS 'Операционная система';
COMMENT ON COLUMN Security.Refresh_tokens.device_name IS 'Наименование устройства';
COMMENT ON COLUMN Security.Refresh_tokens.city IS 'Город';
COMMENT ON COLUMN Security.Refresh_tokens.country IS 'Страна';
COMMENT ON COLUMN Security.Refresh_tokens.expires_at IS 'Время истечения токена';
COMMENT ON COLUMN Security.Refresh_tokens.revoked IS 'Флаг аннулированного токена';
COMMENT ON COLUMN Security.Refresh_tokens.revoked_at IS 'Время аннулирования токена';
COMMENT ON COLUMN Security.Refresh_tokens.created_at IS 'Время создания записи';
