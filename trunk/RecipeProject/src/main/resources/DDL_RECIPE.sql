DROP TABLE IF EXISTS Recipe;

CREATE TABLE Recipe (

id int not null AUTO_INCREMENT,
name varchar (255) NOT NULL,
author varchar (255) NOT NULL,
ingredient varchar (255) NOT NULL,
amount int NOT NULL default 0,
metric varchar (45),
diet varchar (45) NOT NULL,
authorLink varchar (255),
picture varchar (255) null,
version int not null default 0,
PRIMARY KEY (ID)
);

CREATE INDEX IngredientIdx ON Recipe(ingredient);