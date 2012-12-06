DROP TABLE IF EXISTS RecipeIngredient;

CREATE TABLE RecipeIngredient (

id int not null AUTO_INCREMENT,
ingredientId int not null,
recipeId int not null,
PRIMARY KEY (ID),

CONSTRAINT recipeFK
    FOREIGN KEY (recipeId)
    REFERENCES Recipe (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    
  CONSTRAINT IngredientFK
    FOREIGN KEY (ingredientId)
    REFERENCES ingredient (ingredientId)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);