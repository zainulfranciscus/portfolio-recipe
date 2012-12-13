DROP TABLE IF EXISTS Ingredient;

CREATE TABLE Ingredient(

ingredientId int not null AUTO_INCREMENT,
ingredientType int not null NOT NULL,
amount varchar (255),
metric varchar (255),
version int not null default 0,
PRIMARY KEY (ingredientId),

CONSTRAINT ingredientTypeFK
    FOREIGN KEY (ingredientType)
    REFERENCES IngredientType (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);
