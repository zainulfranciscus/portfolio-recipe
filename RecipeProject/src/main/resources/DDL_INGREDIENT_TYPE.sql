DROP TABLE IF EXISTS IngredientType;

CREATE TABLE IngredientType(

Id int not null AUTO_INCREMENT,
name longtext NOT NULL,
version int not null default 0,
PRIMARY KEY (Id)
);
