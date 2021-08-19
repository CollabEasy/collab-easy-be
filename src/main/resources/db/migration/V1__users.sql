CREATE TABLE IF NOT EXISTS artists (
  id int AUTO_INCREMENT not null,
  artist_id varchar(40) primary key not null,
  artist_handle varchar(15) unique,
  first_name varchar(50) not null,
  last_name varchar(50),
  email varchar(100) not null unique,
  phone_number varchar(15) unique,
<<<<<<< HEAD
  county varchar(20),
=======
  country varchar(20),
  gender varchar(10),
>>>>>>> apis
  profile_pic_url varchar(255),
  timezone varchar(5),
  bio varchar(512),
  age integer,
  last_active timestamp default current_timestamp,
  created_at timestamp DEFAULT CURRENT_TIMESTAMP ,
  updated_at timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  key(id)
)'

<<<<<<< HEAD
CREATE TABLE IF NOT EXISTS `artist-preferences` (
=======
CREATE INDEX IF NOT EXISTS `slug_index` ON `artists` (slug);

CREATE TABLE IF NOT EXISTS `artist_preferences` (
>>>>>>> apis
  id int AUTO_INCREMENT,
  artist_id varchar(50),
  setting_name varchar(20),
  setting_values blob,
  key(id),
<<<<<<< HEAD
  primary key(artist_id)
=======
  unique key(artist_id, setting_name),
>>>>>>> apis
  foreign key(artist_id) references artists(artist_id)
)

CREATE TABLE IF NOT EXISTS `artist-samples` (
  id int AUTO_INCREMENT,
  artist_id varchar(50),
  url varchar(20),
  updated_at timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  key(id),
  primary key(artist_id)
  foreign key(artist_id) references artists(artist_id)
)

<<<<<<< HEAD
CREATE TABLE IF NOT EXISTS `art-categories` (
  id int AUTO_INCREMENT,
  name varchar(50),
  description varchar(255),
  approved boolean,
  key(id),
  primary key(name)
)

CREATE TABLE IF NOT EXISTS `artist-categories` (
=======
CREATE TABLE IF NOT EXISTS `art_categories` (
  id int AUTO_INCREMENT,
  art_name varchar(50),
  description varchar(255),
  approved boolean,
  key(id),
  primary key(art_name)
)

CREATE TABLE IF NOT EXISTS `artist_categories` (
>>>>>>> apis
  id int AUTO_INCREMENT,
  artist_id varchar(50),
  art_id int,
  key(id),
  foreign key(artist_id) references artists(artist_id),
<<<<<<< HEAD
  foreign key(art_id) references art-categories(id)
)

CREATE TABLE IF NOT EXISTS `collab-requests` (
=======
  foreign key(art_id) references art_categories(id)
)

CREATE TABLE IF NOT EXISTS `collab_requests` (
>>>>>>> apis
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
<<<<<<< HEAD
  foreign key(sender_id) references artists(artist_id)
)

CREATE TABLE IF NOT EXISTS `collab-reviews` (
=======
  key(sender_id),
  key(receiver_id),
  foreign key(sender_id) references artists(artist_id)
)

CREATE TABLE IF NOT EXISTS `collab_reviews` (
>>>>>>> apis
    id int AUTO_INCREMENT,
    request_id varchar(50),
    artist_id varchar(50),
    rating int,
    review varchar(512),
    key(id),
    primary key(request_id),
    foreign key(artist_id) references artists(artist_id)
)

CREATE TABLE IF NOT EXISTS `notifications` (
    id int AUTO_INCREMENT,
    notif_id varchar(50),
    artist_id varchar(50),
    notif_type varchar(50),
    redirect_id int,
    notification_data blob,
    notif_read bool,
    notif_view_type varchar(25),
    created_at timestamp
    key(id),
    primary key(notif_id),
<<<<<<< HEAD
=======
    key(artist_id),
>>>>>>> apis
    foreign key(artist_id) references artists(artist_id)
 )



