-- Table: public.t_user

-- DROP TABLE IF EXISTS public.t_user;

create TABLE IF NOT EXISTS public.t_user
(
    id integer NOT NULL DEFAULT nextval('t_user_id_seq'::regclass),
    username character varying(255) COLLATE pg_catalog."default",
    passwd character varying(255) COLLATE pg_catalog."default",
    first_name character varying(255) COLLATE pg_catalog."default",
    middle_name character varying(255) COLLATE pg_catalog."default",
    last_name character varying(255) COLLATE pg_catalog."default",
    email character varying(255) COLLATE pg_catalog."default",
    phone character varying(32) COLLATE pg_catalog."default",
    status smallint NOT NULL DEFAULT 1,
    is_deleted "char" NOT NULL DEFAULT 'N'::"char",
    creator character varying(255) COLLATE pg_catalog."default" NOT NULL DEFAULT 'system'::character varying,
    modifier character varying(255) COLLATE pg_catalog."default" NOT NULL DEFAULT 'system'::character varying,
    created_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    modified_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    x_token character varying(64) COLLATE pg_catalog."default",
    token_expire date,
    passwd_salt character varying(20) COLLATE pg_catalog."default",
    email_verified smallint DEFAULT 0,
    picture character varying(1024) COLLATE pg_catalog."default",
    from_client character varying(1024) COLLATE pg_catalog."default",
    third_user_id character varying(1024) COLLATE pg_catalog."default",
    user_from character varying(1024) COLLATE pg_catalog."default",
    CONSTRAINT t_user_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

alter table IF EXISTS public.t_user
    OWNER to postgres;

comment on table public.t_user
    is 'user';

comment on column public.t_user.x_token
    is '登录凭证';

comment on column public.t_user.token_expire
    is 'token expire time';

comment on column public.t_user.passwd_salt
    is 'private salt';

comment on column public.t_user.picture
    is '头像';

comment on column public.t_user.user_from
    is '1:google';
-- Index: idx_token_expire

-- DROP INDEX IF EXISTS public.idx_token_expire;

create index IF NOT EXISTS idx_token_expire
    ON public.t_user USING btree
    (x_token COLLATE pg_catalog."default" ASC NULLS LAST, token_expire ASC NULLS LAST)
    TABLESPACE pg_default;
-- Index: idx_username

-- DROP INDEX IF EXISTS public.idx_username;

create index IF NOT EXISTS idx_username
    ON public.t_user USING hash
    (username COLLATE pg_catalog."default")
    TABLESPACE pg_default;