DROP TABLE IF EXISTS LikedRecipe;

create table LikedRecipe (
 
id int not null AUTO_INCREMENT,
recipeId int not null, 
userName VARCHAR(45) NOT NULL ,
primary key (id),

 CONSTRAINT emailFK
    FOREIGN KEY (userName)
    REFERENCES account (userName)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    
  CONSTRAINT recipeIdFK
    FOREIGN KEY (recipeId)
    REFERENCES recipe (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);