CREATE TABLE USER_ACCOUNT
(
    ID       uuid DEFAULT uuid_generate_v4(),
    EMAIL    VARCHAR(80),
    NICKNAME VARCHAR(40),
    PRIMARY KEY (ID)
);
