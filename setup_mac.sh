echo "Installing MYSQL"
brew list mysql || brew install mysql

echo "Starting mysql server"
mysql.server start 

echo "Creating databases"
mysql -h localhost -u root -e 'create database if not exists wondor'
echo "DBs created"


echo "Following DBs exist"
mysql -h localhost -u root -e 'show databases'

echo "following tables exist"
mysql -h localhost -u root -e 'use wondor; show tables'
echo "Create user tables"
mysql -h localhost -u root -e 'CREATE TABLE IF NOT EXISTS `wondor`.`user` (
  id int AUTO_INCREMENT not null,
  user_id varchar(40) primary key not null,
  user_handle varchar(15) unique,
  first_name varchar(50) not null,
  last_name varchar(50),
  email varchar(100) not null unique,
  phone_number varchar(15) unique,
  county varchar(20),
  profile_pic_url varchar(255),
  timezone varchar(5),
  bio varchar(512),
  age integer,
  last_active timestamp default current_timestamp,
  created_at timestamp DEFAULT CURRENT_TIMESTAMP ,
  updated_at timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  key(id)
)'

mysql -h localhost -u root -e 'CREATE TABLE IF NOT EXISTS `wondor`.`user-preferences` (
  id int AUTO_INCREMENT,
  user_id varchar(50),
  setting_name varchar(20),
  setting_values blob,
  key(id),
  primary key(user_id),
  foreign key(user_id) references `wondor`.`users`(user_id)
)'

mysql -h localhost -u root -e 'CREATE TABLE IF NOT EXISTS `wondor`.`user-samples` (
  id int AUTO_INCREMENT,
  user_id varchar(50),
  url varchar(20),
  updated_at timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  key(id),
  primary key(user_id),
  foreign key(user_id) references `wondor`.`users`(user_id)
)'

mysql -h localhost -u root -e 'CREATE TABLE IF NOT EXISTS `wondor`.`art-categories` (
  id int AUTO_INCREMENT,
  name varchar(50),
  description varchar(255),
  approved boolean,
  key(id),
  primary key(name)
)'

mysql -h localhost -u root -e 'CREATE TABLE IF NOT EXISTS `wondor`.`user-art-categories` (
  id int AUTO_INCREMENT,
  user_id varchar(50),
  art_id int,
  key(id),
  foreign key(user_id) references `wondor`.`users`(user_id),
  foreign key(art_id) references `wondor`.`art-categories`(id)
)'

mysql -h localhost -u root -e 'CREATE TABLE IF NOT EXISTS `wondor`.`collab-requests` (
  id int AUTO_INCREMENT,
  request_id varchar(50),
  sender_id varchar(50),
  receiver_id varchar(50),
  request_data blob,
  scheduled_at timestamp,
  accepted bool,
  created_at timestamp,
  updated_at timestamp,
  key(id),
  primary key(request_id),
  foreign key(sender_id) references `wondor`.`users`(user_id)
)'

mysql -h localhost -u root -e 'CREATE TABLE IF NOT EXISTS `wondor`.`collab-reviews` (
    id int AUTO_INCREMENT,
    request_id varchar(50),
    user_id varchar(50),
    rating int,
    review varchar(512),
    key(id),
    primary key(request_id),
    foreign key(user_id) references `wondor`.`users`(user_id)
)'

mysql -h localhost -u root -e 'CREATE TABLE IF NOT EXISTS `wondor`.`notifications` (
    id int AUTO_INCREMENT,
    notif_id varchar(50),
    user_id varchar(50),
    notif_type varchar(50),
    redirect_id int,
    notification_data blob,
    notif_read bool,
    notif_view_type varchar(25),
    created_at timestamp,
    key(id),
    primary key(notif_id),
    foreign key(user_id) references `wondor`.`users`(user_id)
 )'

rm -rf collab-easy-be
git clone https://github.com/CollabEasy/collab-easy-be.git
cd collab-easy-be
brew list maven || brew install maven
mvn clean install 
mv target/*.jar ~/server.jar
cd ..
rm -rf collab-easy-be
cd ~/
TODO java -jar server.jar


