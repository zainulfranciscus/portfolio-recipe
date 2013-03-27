DROP TABLE IF EXISTS Recipe;

CREATE TABLE Recipe (

id int not null AUTO_INCREMENT,
name varchar (255) NOT NULL,
author varchar (255) NOT NULL,
diet varchar (45),
authorLink varchar (255),
picture varchar (255) null,
version int not null default 0,
PRIMARY KEY (ID)
);

