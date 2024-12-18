CREATE TABLE IF NOT EXISTS artists (
  id int AUTO_INCREMENT not null,
  artist_id varchar(40) primary key not null,
  artist_handle varchar(15) unique,
  first_name varchar(50) not null,
  last_name varchar(50),
  slug varchar(120),
  email varchar(100) not null unique,
  phone_number varchar(15) unique,
  country varchar(200),
  state varchar(200),
  city varchar(200),
  gender varchar(10),
  profile_pic_url varchar(255),
  timezone varchar(5),
  bio varchar(512),
  age integer,
  date_of_birth timestamp,
  country_iso varchar(20),
  country_dial varchar(10),
  new_user boolean,
  test_user boolean,
  profile_complete tinyint DEFAULT '0',
  referral_code varchar(10) DEFAULT NULL,
  last_active timestamp default current_timestamp,
  created_at timestamp DEFAULT CURRENT_TIMESTAMP ,
  updated_at timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  key(id)
);

CREATE INDEX  slug_index ON artists (slug);
CREATE INDEX artist_ref_code ON artists (referral_code);

CREATE TABLE IF NOT EXISTS artist_preferences (
  id int AUTO_INCREMENT,
  artist_id varchar(50),
  setting_name varchar(20),
  setting_values blob,
  key(id),
  unique key(artist_id, setting_name)
);

CREATE TABLE IF NOT EXISTS artist_samples (
  id int AUTO_INCREMENT,
  artist_id varchar(50),
  original_url varchar(50) unique,
  thumbnail_url varchar(50) unique,
  caption varchar(1000),
  file_type varchar(10),
  created_at timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  key(id)
);

CREATE INDEX artist_index ON artist_samples (artist_id);

CREATE TABLE IF NOT EXISTS art_categories (
  id int AUTO_INCREMENT,
  art_name varchar(50),
  description varchar(255),
  approved boolean,
  slug varchar(70),
  key(id),
  primary key(art_name)
);

CREATE INDEX art_category_index ON art_categories (slug);

CREATE TABLE IF NOT EXISTS artist_categories (
  id int AUTO_INCREMENT,
  artist_id varchar(50),
  art_id int,
  key(id)
);


CREATE TABLE IF NOT EXISTS collab_requests (   
  sender_id varchar(50) DEFAULT NULL,   
  receiver_id varchar(50) DEFAULT NULL,   
  request_data json DEFAULT NULL,   
  scheduled_at timestamp NULL DEFAULT NULL,   
  status varchar(20) DEFAULT NULL,   
  created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,   
  updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,   
  id varchar(50) NOT NULL,   
  artist_profile_pic varchar(255) DEFAULT NULL,   
  sender_name varchar(255) DEFAULT NULL,   
  receiver_name varchar(255) DEFAULT NULL,   
  sender_slug varchar(150) DEFAULT NULL,   
  receiver_slug varchar(150) DEFAULT NULL,   
  sender_profile_pic_url varchar(255) DEFAULT NULL,   
  receiver_profile_pic_url varchar(255) DEFAULT NULL,   
   PRIMARY KEY (id),   KEY sender_id (sender_id),   KEY receiver_id (receiver_id) 
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE collab_conversation_read_status (
  id int(11) NOT NULL AUTO_INCREMENT,
  collab_id varchar(50) DEFAULT NULL,
  artist_id varchar(50) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY collab_artist (collab_id,artist_id)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;


CREATE TABLE IF NOT EXISTS collab_reviews (
    id int AUTO_INCREMENT,
    request_id varchar(50),
    artist_id varchar(50),
    rating int,
    review varchar(512),
    key(id),
    primary key(request_id)
);

CREATE TABLE IF NOT EXISTS notifications (
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

CREATE TABLE IF NOT EXISTS artist_scratchpads (
    id int AUTO_INCREMENT,
    artist_id varchar(50),
    content varchar(65535),
    created_at timestamp,
    updated_at timestamp,
    deleted_at timestamp,
    key(id),
    primary key(artist_id)
 );

CREATE TABLE IF NOT EXISTS social_platforms (
    id int AUTO_INCREMENT,
    name varchar(50),
    base_url varchar(100),
    description varchar(50),
    approved bool,
    created_at timestamp,
    updated_at timestamp,
    key(id)
 );

CREATE TABLE IF NOT EXISTS artist_social_prospectus (
    id int AUTO_INCREMENT,
    social_platform_id int,
    artist_id varchar(50),
    handle varchar(50),
    description varchar(50),
    up_for_collab varchar(50),
    created_at timestamp,
    updated_at timestamp,
    key(id)
 );
 
 CREATE TABLE collab_conversations (
  id int(11) NOT NULL AUTO_INCREMENT,
  collab_id varchar(50) NOT NULL,
  artist_id varchar(50) NOT NULL,
  content varchar(2000) DEFAULT NULL,
  created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS contests (
    id int AUTO_INCREMENT,
    contest_slug varchar(50),
    title varchar(200),
    description varchar(2000),
    start_date timestamp NULL DEFAULT NULL,
    end_date timestamp NULL DEFAULT NULL,
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    primary key(id), KEY contest_slug (contest_slug)
 ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS contest_submissions (
    id int AUTO_INCREMENT,
    artist_id varchar(50),
    contest_slug varchar(50),
    artwork_url varchar(200),
    artwork_thumb_url varchar(200),
    description varchar(500),
    created_at timestamp,
    updated_at timestamp,
    key(id),
   unique key(artist_id, contest_slug)
 ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
 
 CREATE TABLE IF NOT EXISTS contest_submission_vote (
    id int AUTO_INCREMENT,
    contest_slug varchar(50),
    submission_id int,
    artist_id varchar(50),
    vote boolean,
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    key(id), 
    unique key(contest_slug, submission_id, artist_id)
)

CREATE TABLE email_enum_history (
  email_enum varchar(50) NOT NULL,
  last_sent timestamp NULL DEFAULT NULL,
  PRIMARY KEY (email_enum)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

insert into email_enum_history values("ADMINS", now());
insert into email_enum_history values("INCOMPLETE_PROFILE", now());

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
 
 insert into art_categories values(21, "Creative Journaling", "All creative journalers", 1, "creative-journaling");
 insert into art_categories values(22, "Scrapbooking", "All scrapbooking", 1, "scrapbooking");
 insert into art_categories values(23, "Bullet Journaling", "All bullet journaling", 1, "bullet-journaling");
 insert into art_categories values(24, "Calligraphy", "All calligraphy", 1, "calligraphy");
 insert into art_categories values(25, "Doodling", "All doodling", 1, "doodling");
 insert into art_categories values(26, "Art Journaling", "All art journaling", 1, "art-journaling");
 insert into art_categories values(27, "Collage Making", "All collage making", 1, "collage-making");
 
 insert into art_categories values(28, "Sketching", "All sketching", 1, "sketching");
 insert into art_categories values(29, "Journaling", "All journalers", 1, "journaling");
 insert into art_categories values(30, "Hand Lettering", "All hand letterers", 1, "hand-lettering");
 
 
 
 
insert into social_platforms values(1, "Facebook", "https://facebook.com", "Facebook social platform", 1, now(), now());
insert into social_platforms values(2, "Instagram", "https://instagram.com", "Instagram social platform", 1, now(), now());
insert into social_platforms values(3, "Youtube", "https://youtube.com", "Youtube social platform", 1, now(), now());
insert into social_platforms values(4, "Tik Tok", "https://tiktok.com", "Tik Tok social platform", 1, now(), now());


create table total_points (artist_id varchar(100) primary key not null, total_points int);

CREATE TABLE rewards_activity (
  created_at timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  artist_id varchar(50) primary key not null,
  action varchar(20) DEFAULT NULL,
  points int DEFAULT NULL,
  added tinyint DEFAULT '1',
  details blob
);

CREATE TABLE `word_to_category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `word` varchar(50) DEFAULT NULL,
  `category_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `word_index` (`word`));