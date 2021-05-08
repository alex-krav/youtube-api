-- DROP TABLE users; -- IF EXISTS;
-- DROP TABLE videos; -- IF EXISTS;

CREATE TABLE users (
  id         INTEGER IDENTITY PRIMARY KEY AUTO_INCREMENT,
  channel_id VARCHAR_IGNORECASE(30),
  token      VARCHAR(30)
);

CREATE TABLE videos (
  id         INTEGER IDENTITY PRIMARY KEY AUTO_INCREMENT,
  user_id    INTEGER NOT NULL,
  video_id   VARCHAR_IGNORECASE(30),
  file_name  VARCHAR(255),
  upload_status VARCHAR(30)
);

ALTER TABLE videos ADD CONSTRAINT fk_videos_users FOREIGN KEY (user_id) REFERENCES users (id);




