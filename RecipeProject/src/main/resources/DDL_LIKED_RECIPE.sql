DROP TABLE IF EXISTS LikedRecipe;

create table LikedRecipe (
 
id int not null AUTO_INCREMENT,
recipeId int not null, 
email VARCHAR(50) NOT NULL ,
primary key (id),

 CONSTRAINT emailFK
    FOREIGN KEY (email)
    REFERENCES account (email)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    
  CONSTRAINT recipeIdFK
    FOREIGN KEY (recipeId)
    REFERENCES recipe (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);