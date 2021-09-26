CREATE TABLE IF NOT EXISTS artists (
  id int AUTO_INCREMENT not null,
  artist_id varchar(40) primary key not null,
  artist_handle varchar(15) unique,
  first_name varchar(50) not null,
  last_name varchar(50),
  slug varchar(120),
  email varchar(100) not null unique,
  phone_number varchar(15) unique,
  country varchar(20),
  gender varchar(10),
  profile_pic_url varchar(255),
  timezone varchar(5),
  bio varchar(512),
  age integer,
  last_active timestamp default current_timestamp,
  created_at timestamp DEFAULT CURRENT_TIMESTAMP ,
  updated_at timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  key(id)
);

CREATE INDEX  `slug_index` ON `artists` (slug);

CREATE TABLE IF NOT EXISTS `artist_preferences` (
  id int AUTO_INCREMENT,
  artist_id varchar(50),
  setting_name varchar(20),
  setting_values blob,
  key(id),
  unique key(artist_id, setting_name),
  foreign key(artist_id) references artists(artist_id)
);

CREATE TABLE IF NOT EXISTS `artist_samples` (
  id int AUTO_INCREMENT,
  artist_id varchar(50),
  original_url varchar(50) unique,
  thumbnail_url varchar(50) unique,
  created_at timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  key(id),
  foreign key(artist_id) references artists(artist_id)
);

CREATE INDEX `artist_index` ON `artist_samples` (artist_id);

CREATE TABLE IF NOT EXISTS `art_categories` (
  id int AUTO_INCREMENT,
  art_name varchar(50),
  description varchar(255),
  approved boolean,
  slug varchar(70);
  key(id),
  primary key(art_name)
);

CREATE INDEX `art_category_index` ON `art_categories` (slug);

CREATE TABLE IF NOT EXISTS `artist_categories` (
  id int AUTO_INCREMENT,
  artist_id varchar(50),
  art_id int,
  key(id),
  foreign key(artist_id) references artists(artist_id),
  foreign key(art_id) references art_categories(id)
);

--add indexing on sender_id and receiver_id
CREATE TABLE IF NOT EXISTS `collab_requests` (
  id int AUTO_INCREMENT,
  sender_id varchar(50),
  receiver_id varchar(50),
  request_data JSON,
  scheduled_at timestamp,
  status varchar(20),
  created_at timestamp,
  updated_at timestamp,
  key(id),
  primary key(request_id),
  key(sender_id),
  key(receiver_id),
  foreign key(sender_id) references artists(artist_id)
);

CREATE TABLE IF NOT EXISTS `collab_reviews` (
    id int AUTO_INCREMENT,
    request_id varchar(50),
    artist_id varchar(50),
    rating int,
    review varchar(512),
    key(id),
    primary key(request_id),
    foreign key(artist_id) references artists(artist_id)
);

CREATE TABLE IF NOT EXISTS `notifications` (
    id int AUTO_INCREMENT,
    artist_id varchar(50),
    notif_type varchar(50),
    redirect_id varchar(50),
    notification_data blob,
    notif_read bool,
    notif_view_type varchar(25),
    created_at timestamp,
    updated_at timestamp,
    key(id),
    primary key(notif_id),
    key(artist_id),
    foreign key(artist_id) references artists(artist_id)
 );


