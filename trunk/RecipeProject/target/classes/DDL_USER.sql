
DROP TABLE IF EXISTS authorities;
DROP TABLE IF EXISTS users;

CREATE  TABLE users (
  username VARCHAR(50) NOT NULL ,
  password VARCHAR(50) NULL ,
  enabled TINYINT NULL ,
  PRIMARY KEY (username) );


CREATE  TABLE authorities (

  username VARCHAR(50) NOT NULL ,
  authority VARCHAR(50) NULL ,
  PRIMARY KEY (username) ,
  
  CONSTRAINT fk_authorities_users
    FOREIGN KEY (username)
    REFERENCES users (username)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);
    
CREATE INDEX ix_auth_username ON authorities (username ASC, authority ASC);
CREATE INDEX fk_authorities_users ON authorities (username ASC);

