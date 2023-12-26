CREATE TABLE USER_CREDENTIALS_DATA
(
    ID uuid DEFAULT uuid_generate_v4(),
    USER_USERNAME VARCHAR(30) NOT NULL,
    USER_PASSWORD VARCHAR(100) NOT NULL,
    USER_ACCOUNT_ID uuid,
    PRIMARY KEY (ID),
    CONSTRAINT FK_USER_ACCOUNT
        FOREIGN KEY (USER_ACCOUNT_ID) REFERENCES USER_ACCOUNT(ID)
);
