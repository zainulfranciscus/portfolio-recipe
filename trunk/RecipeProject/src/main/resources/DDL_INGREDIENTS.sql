DROP TABLE IF EXISTS Ingredient;

CREATE TABLE Ingredient(

ingredientId int not null AUTO_INCREMENT,
ingredient varchar (255) NOT NULL,
amount int NOT NULL default 0,
metric varchar (45),
version int not null default 0,
PRIMARY KEY (ingredientId)
);
