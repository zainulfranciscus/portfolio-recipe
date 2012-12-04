

DROP TABLE IF EXISTS Account;

CREATE  TABLE Account (

  email VARCHAR(50) NOT NULL ,
  password VARCHAR(50) NOT NULL ,
  username VARCHAR(50) NULL ,
  picture LONGBLOB NULL ,
  bio BLOB NULL ,
  location VARCHAR(45) NULL ,
  twitter VARCHAR(45) NULL ,
  enabled TINYINT default 1 ,
  authority VARCHAR(50) NULL default 'user',
  version int default 0,
  PRIMARY KEY (email) );

