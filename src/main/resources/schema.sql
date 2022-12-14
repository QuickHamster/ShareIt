DROP TABLE IF EXISTS comments, bookings, items, requests, users;

CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(255)                            NOT NULL,
    email VARCHAR(512)                            NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS requests
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    description  VARCHAR(1024)                           NOT NULL,
    requestor_id BIGINT,
    created      TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT fk_requests_to_users FOREIGN KEY (requestor_id) REFERENCES users (id),
    CONSTRAINT pk_request PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS items
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name         VARCHAR(255)  NOT NULL,
    description  VARCHAR(1024) NOT NULL,
    is_available BOOL,
    owner_id     BIGINT,
    request_id   BIGINT,
    CONSTRAINT fk_items_to_users FOREIGN KEY (owner_id) REFERENCES users (id),
    CONSTRAINT fk_items_to_requests FOREIGN KEY (request_id) REFERENCES requests (id),
    UNIQUE (id, request_id)
);

CREATE TABLE IF NOT EXISTS bookings
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    start_date TIMESTAMP WITHOUT TIME ZONE,
    end_time   TIMESTAMP WITHOUT TIME ZONE,
    item_id    BIGINT,
    booker_id  BIGINT,
    status     VARCHAR(50)                             NOT NULL,
    CONSTRAINT fk_bookings_to_items FOREIGN KEY (item_id) REFERENCES items (id),
    CONSTRAINT fk_bookings_to_users FOREIGN KEY (booker_id) REFERENCES users (id),
    CONSTRAINT pk_booking PRIMARY KEY (id)
);



CREATE TABLE IF NOT EXISTS comments
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text      VARCHAR(1024)                           NOT NULL,
    item_id   BIGINT,
    author_id BIGINT,
    created   TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT fk_comments_to_items FOREIGN KEY (item_id) REFERENCES items (id),
    CONSTRAINT fk_comments_to_users FOREIGN KEY (author_id) REFERENCES users (id),
    CONSTRAINT pk_comment PRIMARY KEY (id)
);
