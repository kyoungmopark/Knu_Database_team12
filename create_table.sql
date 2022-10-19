CREATE TABLE BOOK 
(
    ISBN    CHAR(13)    NOT NULL, 
    Title   VARCHAR(15)     NOT NULL,
    Summary VARCHAR(100)    NOT NULL, 
    Language VARCHAR(15)    NOT NULL,
    Price   INT     NOT NULL,
    State   BOOL    NOT NULL,
    Page    INT     NOT NULL,
    PRIMARY KEY (ISBN)
);
CREATE TABLE LOCATION
(
    Location_index INT NOT NULL,
	Library VARCHAR(15) NOT NULL,
    Floor  INT     NOT NULL,
    Shelf_number INT NOT NULL,
    PRIMARY KEY (Location_index)
);

CREATE TABLE RATING
(
	Rating_index INT NOT NULL,
    Rating  INT     NOT NULL,
    Review VARCHAR(100),
    PRIMARY KEY (Rating_index)
);
CREATE TABLE ACCOUNT
(
    ID      VARCHAR(15)     NOT NULL,
    Password    VARCHAR(15)     NOT NULL,
    Name    VARCHAR(15)     NOT NULL,
    Email   VARCHAR(30)     NOT NULL,
    Phone   VARCHAR(15),
    PRIMARY KEY (ID) 
);
CREATE TABLE ADMIN
(
    ID      VARCHAR(15)     NOT NULL,
    Password    VARCHAR(15)     NOT NULL,
    Name    VARCHAR(15)     NOT NULL,
    Email   VARCHAR(30)     NOT NULL,
    Phone   VARCHAR(15),
    PRIMARY KEY (ID),
);
CREATE TABLE GENRE
(
    Genre_ID    VARCHAR(15)     NOT NULL,
    Genres  VARCHAR(15),
    PRIMARY KEY (Genre_ID)
);
CREATE TABLE PUBLISHER
(
    Pub_ID  VARCHAR(15)     NOT NULL,
    Name    VARCHAR(15)     NOT NULL,
    Address     VARCHAR(30)     NOT NULL,
    PRIMARY KEY (Pub_ID)
);
CREATE TABLE TRANSLATOR
(
    Translator_ID  VARCHAR(15)     NOT NULL,
    Name    VARCHAR(15)     NOT NULL,
    Language     VARCHAR(15)     NOT NULL,
    PRIMARY KEY (Translator_ID),
);
CREATE TABLE AUTHOR
(
    Author_ID   VARCHAR(15)     NOT NULL,
    Name    VARCHAR(15)     NOT NULL,
    Brith_year  DATE    NOT NULL,
    Death_year  DATE,
    Nationality VARCHAR(15)     NOT NULL,
    PRIMARY KEY (Author_ID),
);

