-- 1. 스키마 생성 및 사용
CREATE SCHEMA IF NOT EXISTS ryan;
SET search_path TO ryan;

-- 2. 테이블 생성
CREATE TABLE IF NOT EXISTS customer (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMPTZ,
    CONSTRAINT uq_customer_email UNIQUE (email)
    );

COMMENT ON TABLE customer IS '고객 정보';
COMMENT ON COLUMN customer.id IS '고유 키';
COMMENT ON COLUMN customer.email IS '이메일';
COMMENT ON COLUMN customer.password IS '비밀번호';
COMMENT ON COLUMN customer.created_at IS '생성일';
COMMENT ON COLUMN customer.updated_at IS '수정일';
COMMENT ON COLUMN customer.deleted_at IS '삭제일';

------------------------------------------------------------

CREATE TABLE IF NOT EXISTS products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    thumbnail VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMPTZ,
    CONSTRAINT uq_products_name UNIQUE (name)
    );

COMMENT ON TABLE products IS '상품 정보';
COMMENT ON COLUMN products.id IS '고유 키';
COMMENT ON COLUMN products.name IS '이름';
COMMENT ON COLUMN products.thumbnail IS '썸네일';
COMMENT ON COLUMN products.price IS '가격';
COMMENT ON COLUMN products.created_at IS '생성일';
COMMENT ON COLUMN products.updated_at IS '수정일';
COMMENT ON COLUMN products.deleted_at IS '삭제일';

------------------------------------------------------------

CREATE TABLE IF NOT EXISTS drawer (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    thumbnail VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMPTZ,
    CONSTRAINT uq_drawer UNIQUE (customer_id, name, deleted_at),
    CONSTRAINT fk_drawer_customer FOREIGN KEY (customer_id) REFERENCES customer (id) ON DELETE RESTRICT ON UPDATE RESTRICT
    );

COMMENT ON TABLE drawer IS '서랍 정보';
COMMENT ON COLUMN drawer.id IS '고유 키';
COMMENT ON COLUMN drawer.customer_id IS '고객 고유 키';
COMMENT ON COLUMN drawer.name IS '이름';
COMMENT ON COLUMN drawer.thumbnail IS '썸네일';
COMMENT ON COLUMN drawer.created_at IS '생성일';
COMMENT ON COLUMN drawer.updated_at IS '수정일';
COMMENT ON COLUMN drawer.deleted_at IS '삭제일';

------------------------------------------------------------

CREATE TABLE IF NOT EXISTS favorite (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    drawer_id BIGINT NOT NULL,
    products_id BIGINT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_favorite UNIQUE (customer_id, drawer_id, products_id),
    CONSTRAINT fk_favorite_customer FOREIGN KEY (customer_id) REFERENCES customer (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_favorite_drawer FOREIGN KEY (drawer_id) REFERENCES drawer (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_favorite_products FOREIGN KEY (products_id) REFERENCES products (id) ON DELETE RESTRICT ON UPDATE RESTRICT
    );

COMMENT ON TABLE favorite IS '즐겨찾기';
COMMENT ON COLUMN favorite.id IS '고유 키';
COMMENT ON COLUMN favorite.customer_id IS '고객 고유 키';
COMMENT ON COLUMN favorite.drawer_id IS '서랍 고유 키';
COMMENT ON COLUMN favorite.products_id IS '상품 고유 키';
COMMENT ON COLUMN favorite.created_at IS '생성일';

------------------------------------------------------------
