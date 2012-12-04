
DROP TABLE IF EXISTS authorities;
DROP TABLE IF EXISTS Account;

CREATE  TABLE Account (

  email VARCHAR(45) NOT NULL ,
  password VARCHAR(45) NOT NULL ,
  username VARCHAR(45) NULL ,
  picture LONGBLOB NULL ,
  bio BLOB NULL ,
  location VARCHAR(45) NULL ,
  twitter VARCHAR(45) NULL ,
  enabled TINYINT default 1 ,
  version int default 0,
  PRIMARY KEY (email) );


CREATE  TABLE authorities (

  email VARCHAR(50) NOT NULL ,
  authority VARCHAR(50) NULL ,
  PRIMARY KEY (email) ,
  
  CONSTRAINT fk_authorities_users
    FOREIGN KEY (email)
    REFERENCES account (email)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);
    
CREATE INDEX ix_auth_username ON authorities (email ASC, authority ASC);
CREATE INDEX fk_authorities_users ON authorities (email ASC);

