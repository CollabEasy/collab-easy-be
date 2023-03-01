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
  date_of_birth timestamp,
  country_iso varchar(20),
  country_dial varchar(10),
  new_user boolean,
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
  unique key(artist_id, setting_name)
);

CREATE TABLE IF NOT EXISTS `artist_samples` (
  id int AUTO_INCREMENT,
  artist_id varchar(50),
  original_url varchar(50) unique,
  thumbnail_url varchar(50) unique,
  caption varchar(1000),
  file_type varchar(10),
  created_at timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  key(id)
);

CREATE INDEX `artist_index` ON `artist_samples` (artist_id);

CREATE TABLE IF NOT EXISTS `art_categories` (
  id int AUTO_INCREMENT,
  art_name varchar(50),
  description varchar(255),
  approved boolean,
  slug varchar(70),
  key(id),
  primary key(art_name)
);

CREATE INDEX `art_category_index` ON `art_categories` (slug);

CREATE TABLE IF NOT EXISTS `artist_categories` (
  id int AUTO_INCREMENT,
  artist_id varchar(50),
  art_id int,
  key(id)
);

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
  key(sender_id),
  key(receiver_id)
);

CREATE TABLE IF NOT EXISTS `collab_reviews` (
    id int AUTO_INCREMENT,
    request_id varchar(50),
    artist_id varchar(50),
    rating int,
    review varchar(512),
    key(id),
    primary key(request_id)
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
    key(artist_id)
 );

CREATE TABLE IF NOT EXISTS `artist_scratchpads` (
    id int AUTO_INCREMENT,
    artist_id varchar(50),
    content varchar(1024),
    created_at timestamp,
    updated_at timestamp,
    deleted_at timestamp,
    key(id),
    primary key(artist_id)
 );

CREATE TABLE IF NOT EXISTS `social_platforms` (
    id int AUTO_INCREMENT,
    name varchar(50),
    base_url varchar(100),
    description varchar(50),
    approved bool,
    created_at timestamp,
    updated_at timestamp,
    key(id)
 );

CREATE TABLE IF NOT EXISTS `artist_social_prospectus` (
    id int AUTO_INCREMENT,
    social_platform_id int,
    artist_id varchar(50),
    handle varchar(50),
    description varchar(50),
    created_at timestamp,
    updated_at timestamp,
    key(id)
 );
 
 insert into art_categories values(1, "Dancer", "All Dancers", 1, "dancer");
 insert into art_categories values(2, "Choreographer", "All Choreographer", 1, "choreographer");
 insert into art_categories values(3, "Writer", "All Writer", 1, "writer");
 insert into art_categories values(4, "Creative Writer", "All creative writer", 1, "creative-writer");
 insert into art_categories values(5, "Poet", "All poets", 1, "poet");
 insert into art_categories values(6, "Spoken Words", "All spoken words", 1, "spoken-words");
 insert into art_categories values(7, "Song Writer", "All song writer", 1, "song-writer");
 insert into art_categories values(8, "Lyricist", "All lyricist", 1, "lyricist");
 insert into art_categories values(9, "Singer", "All Singer", 1, "singer"); 
 insert into art_categories values(10, "Vocalist", "All Vocalist", 1, "vocalist"); 
 insert into art_categories values(11, "Photographer", "All Photographer", 1, "photographer"); 
 insert into art_categories values(12, "Illustrator", "All Illustrators", 1, "illustrator"); 
 insert into art_categories values(13, "Graphic Designer", "All Graphic Designers", 1, "graphic-designer");
 insert into art_categories values(14, "Video Editor", "All Video Editors", 1, "video-editor");
 insert into art_categories values(15, "Film Maker", "All Film Makers", 1, "film-maker");
 insert into art_categories values(16, "Musician", "All Musicians", 1, "musician");
 insert into art_categories values(17, "Sound Engineer", "All Sound Engineers", 1, "sound-engineer");
 insert into art_categories values(18, "Music Director", "All Music Directors", 1, "music-director");
 insert into art_categories values(19, "Painter", "All Painters", 1, "painter");
 insert into art_categories values(20, "Caricaturist", "All Caricaturists", 1, "caricaturist");
 
 
insert into social_platforms values(1, "Facebook", "https://facebook.com", "Facebook social platform", 1, now(), now());
insert into social_platforms values(2, "Instagram", "https://instagram.com", "Instagram social platform", 1, now(), now());
insert into social_platforms values(3, "Youtube", "https://youtube.com", "Youtube social platform", 1, now(), now());
