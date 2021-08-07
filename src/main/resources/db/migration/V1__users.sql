CREATE TABLE IF NOT EXISTS `users` (
  id int primary key AUTO_INCREMENT,
  first_name varchar(50),
  last_name varchar(50),
  user_uuid varchar(50),
  created timestamp DEFAULT CURRENT_TIMESTAMP ,
  updated timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)