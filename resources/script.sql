create database user;
use user;

CREATE TABLE users (
   id int AUTO_INCREMENT primary key,
   Name VARCHAR(25) NOT NULL,
   Date_Of_Birth DATE NOT NULL,
   PhoneNumber VARCHAR(10) NOT NULL
);

insert into users(id, Name, Date_Of_Birth, PhoneNumber) values ("1" , "Nguyen Ba Trong Tin", "2005-11-20", "0123456789");