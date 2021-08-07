CREATE TABLE IF NOT EXISTS `users` (
  id int primary key AUTO_INCREMENT,
  first_name varchar(50),
  last_name varchar(50),
  user_uuid varchar(50),
  created_at timestamp DEFAULT CURRENT_TIMESTAMP ,
  updated_at timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)

CREATE TABLE IF NOT EXISTS `user-preferences` (
  id int primary key AUTO_INCREMENT,
  user_id varchar(50),
  setting_name varchar(20),
  setting_values blob
)

CREATE TABLE IF NOT EXISTS `user-samples` (
  id int primary key AUTO_INCREMENT,
  user_id varchar(50),
  url varchar(20),
  updated_at timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)

