DROP TABLE IF EXISTS Account;

CREATE  TABLE Account (

  email VARCHAR(45) NOT NULL ,
  password VARCHAR(45) NOT NULL ,
  userName VARCHAR(45) NOT NULL ,
  picture LONGBLOB NULL ,
  bio BLOB NULL ,
  location VARCHAR(45) NULL ,
  twitter VARCHAR(45) NULL ,
  version int default 0,
  PRIMARY KEY (userName) );

