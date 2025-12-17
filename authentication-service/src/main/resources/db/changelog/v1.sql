CREATE SCHEMA IF NOT EXISTS Security;

COMMENT ON SCHEMA Security IS 'Безопастность';

CREATE TABLE IF NOT EXISTS Security.Users
(
    id            UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    email         TEXT UNIQUE NOT NULL,
    phone         TEXT UNIQUE NOT NULL,
    nickname      TEXT UNIQUE NOT NULL,
    password      TEXT        NOT NULL,
    role          SMALLINT    NOT NULL,
    last_login_at TIMESTAMPTZ,
    enabled       BOOLEAN     NOT NULL DEFAULT TRUE,
    expired       BOOLEAN     NOT NULL DEFAULT FALSE,
    deleted       BOOLEAN     NOT NULL DEFAULT FALSE,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ
);

COMMENT ON TABLE Security.Users IS 'Пользователи';
COMMENT ON COLUMN Security.Users.id IS 'Идентификатор';
COMMENT ON COLUMN Security.Users.email IS 'Электронная почта';
COMMENT ON COLUMN Security.Users.phone IS 'Номер телефона';
COMMENT ON COLUMN Security.Users.password IS 'Пароль';
COMMENT ON COLUMN Security.Users.role IS 'Роль (0 - администратор, 1 - пользователь)';
COMMENT ON COLUMN Security.Users.last_login_at IS 'Время последнего входа в приложение';
COMMENT ON COLUMN Security.Users.enabled IS 'Флаг, показывающий разрешен ли пользователь';
COMMENT ON COLUMN Security.Users.expired IS 'Флаг, показывающий истек ли срок действия аккаунта';
COMMENT ON COLUMN Security.Users.deleted IS 'Флаг, показывающий удален ли пользователь';
COMMENT ON COLUMN Security.Users.created_at IS 'Время создания записи';
COMMENT ON COLUMN Security.Users.created_at IS 'Время обновления записи';