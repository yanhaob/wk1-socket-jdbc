# Task 1: Design database table structure and CRUD SQL instructions
CREATE DATABASE mysqlnews;
USE mysqlnews;
CREATE TABLE Users (
    uid INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20),
    avatar_url VARCHAR(255),
    nickname VARCHAR(50),
    favorites TEXT,
    news_preferences TEXT,
    browsing_history TEXT,
    comment_history TEXT,
    last_login TIMESTAMP
);
CREATE TABLE News (
    news_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    cover_image_url VARCHAR(255),
    publish_datetime DATETIME,
    author VARCHAR(100),
    content TEXT,
    view_count INT DEFAULT 0,
    favorite_count INT DEFAULT 0,
    share_count INT DEFAULT 0,
    is_promoted BOOLEAN DEFAULT FALSE
);
CREATE TABLE Comments (
    comment_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    news_id INT,
    content TEXT,
    comment_datetime DATETIME,
    referenced_comment_id INT,
    like_count INT DEFAULT 0,
    dislike_count INT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES Users(uid),
    FOREIGN KEY (news_id) REFERENCES News(news_id),
    FOREIGN KEY (referenced_comment_id) REFERENCES Comments(comment_id)
);
CREATE TABLE Advertisements (
    ad_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    content TEXT,
    image_url VARCHAR(255),
    placement VARCHAR(50),
    advertiser_id INT,
    valid_from DATETIME,
    valid_to DATETIME,
    region_restriction VARCHAR(255),
    device_restriction VARCHAR(255),
    click_count INT DEFAULT 0,
    view_count INT DEFAULT 0,
    weight INT
);
# CRUD SQL Instructions
# Create users through email
INSERT INTO Users (email) VALUES ('test@example.com');
# Users edit their nicknames and other account information
UPDATE Users
SET nickname = 'NewNickname', avatar_url = 'http://example.com/avatar.jpg', phone = '1234567890', news_preferences = 'technology,sports'
WHERE uid = 1;
# Users login
SELECT * FROM Users WHERE email = 'test@example.com';
# Users get homepage news list
SELECT * FROM News ORDER BY publish_datetime DESC LIMIT 10;
# Users browse news list by partition
SELECT * FROM News WHERE category = 'technology' ORDER BY publish_datetime DESC LIMIT 10;
# Users search news list by title and content
SELECT * FROM News WHERE title LIKE '%keyword%' OR content LIKE '%keyword%';
# Users access news and load corresponding comments under news
SELECT * FROM News WHERE news_id = 1;
SELECT * FROM Comments WHERE news_id = 1 ORDER BY comment_datetime ASC;
# Users collect and share news
UPDATE News
SET favorite_count = favorite_count + 1
WHERE news_id = 1;
UPDATE News
SET share_count = share_count + 1
WHERE news_id = 1;
# Users comment on news
INSERT INTO News (title, cover_image_url, publish_datetime, author, content, is_promoted)
VALUES ('Example News', 'http://example.com/cover.jpg', NOW(), 'Author Name', 'This is the content of the news.', FALSE);
INSERT INTO Comments (user_id, news_id, content, comment_datetime, referenced_comment_id)
VALUES (1, 1, 'This is a comment.', NOW(), NULL);
# Users like/dislike comments
UPDATE Comments
SET like_count = like_count + 1
WHERE comment_id = 1;
UPDATE Comments
SET dislike_count = dislike_count + 1
WHERE comment_id = 1;
# Users delete their own comments
DELETE FROM Comments WHERE comment_id = 1 AND user_id = 1;
# Users view their own comment history on their personal page
SELECT * FROM Comments WHERE user_id = 1 ORDER BY comment_datetime DESC;
# Users view their browsing history on their personal page
SELECT * FROM News WHERE news_id IN (SELECT JSON_EXTRACT(browsing_history, '$[*].news_id') FROM Users WHERE uid = 1);
# (Mock) News delivery
INSERT INTO News (title, cover_image_url, publish_datetime, author, content, is_promoted)
VALUES ('New News Title', 'http://example.com/cover.jpg', NOW(), 'Author Name', 'This is the content of the news.', FALSE);
# Get advertisements by content type
SELECT * FROM Advertisements WHERE placement = 'homepage' AND valid_from <= NOW() AND valid_to >= NOW() ORDER BY weight DESC;
INSERT INTO Advertisements (title, content, image_url, placement, advertiser_id, valid_from, valid_to, region_restriction, device_restriction, weight) # Deliver ads
VALUES ('Ad Title', 'This is the ad content.', 'http://example.com/ad.jpg', 'homepage', 1, NOW(), '2024-12-31 23:59:59', 'Global', 'All', 10);