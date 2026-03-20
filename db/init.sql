-- =====================================================
-- Ecommerce Database Initialization Script
-- Schema creation only (no seed data)
-- =====================================================

CREATE DATABASE IF NOT EXISTS ecommerce;
USE ecommerce;

-- =====================================================
-- Users Table
-- Stores login accounts
-- role examples: buyer | seller | admin
-- =====================================================
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hashed VARCHAR(255) NOT NULL,
    salt VARCHAR(255) NOT NULL,
    area VARCHAR(50), 
    token VARCHAR(255) NOT NULL
);

-- =====================================================
-- Items Table
-- Auction items
-- approved_flag = 1 means admin approved
-- highest_bidder_id may be NULL
-- =====================================================
CREATE TABLE IF NOT EXISTS items (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    seller_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    curr_price DECIMAL(10,2) DEFAULT 0,
    highest_bidder_id INT,
    end_time DATETIME,
    approved_flag BOOLEAN DEFAULT FALSE,
    tag VARCHAR(50),
    sold BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (seller_id) REFERENCES users(user_id),
    FOREIGN KEY (highest_bidder_id) REFERENCES users(user_id)
);

-- =====================================================
-- Bids Table
-- =====================================================
CREATE TABLE IF NOT EXISTS bids (
    bid_id INT AUTO_INCREMENT PRIMARY KEY,
    bidder_id INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    item_id INT NOT NULL,
    FOREIGN KEY (item_id) REFERENCES items(item_id),
    FOREIGN KEY (bidder_id) REFERENCES users(user_id)
);

-- =====================================================
-- Availability Table
-- Tracks seller availability blocks
-- =====================================================
CREATE TABLE IF NOT EXISTS availability (
    avail_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    start_time TIME,
    end_time TIME,
    day VARCHAR(20),

    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
-- =====================================================
-- Fake account insertions
-- Inserts fake accounts into the database for testing
-- Fake Account Plaintext Passwords (in order)

-- 1.  Password: DesertStorm1!
-- 2.  Password: SunDevil2025!
-- 3.  Password: CactusBloom7!
-- 4.  Password: TucsonRocks9!
-- 5.  Password: PhoenixHeat4!
-- 6.  Password: WildcatStrong8!
-- 7.  Password: SaguaroSky3!
-- 8.  Password: GrandCanyon6!
-- 9.  Password: CopperState2!
-- 10. Password: Roadrunner5!
-- 11. Password: ValleySun11!
-- 12. Password: MesaMountain12!
-- 13. Password: RedRock13!
-- 14. Password: DustDevil14!
-- 15. Password: HeatWave15!
-- 16. Password: CanyonTrail16!
-- 17. Password: SunsetBlaze17!
-- 18. Password: HorizonGlow18!
-- 19. Password: SolarFlare19!
-- 20. Password: DesertNight20!
-- =====================================================

INSERT INTO users (user_id, username, password_hashed, salt, area, token) VALUES
(1, 'user01', 'FV9kE9jYa3fllSszWu6Kyv/yHujP9swYtWjDo2t53tI=', 'aj+IvRWzl0PdDsoC32gJHg==', 'AreaHere', 'token_1'),
(2, 'user02', '4cmQvsss8pcTFuyplgYnxnbAUWeehV/suubXR9Hod64=', 'mC0gUM0QtYDSV+UCeS28pw==', 'AreaHere', 'token_2'),
(3, 'user03', 'vLK3Lv6DDI5pss6aENIjEnMOE9FBf5pKztLnCRAxgw0=', 'fQ4gkHPn80gCRzsvm2KwVQ==', 'AreaHere', 'token_3'),
(4, 'user04', 'ya7Odq85DiA/MxcHzVd/6g5ItVwYyWw4p+EvB7vSPdw=', 'uoJ5oGsOJQAxafpbD2iuMQ==', 'AreaHere', 'token_4'),
(5, 'user05', 'A5s59ZhyTLIggPBI1KjzGUbJiKb6xa/AtMP0lDBxX3M=', 'sdmwbj+tXE+4a7pHwi/gCg==', 'AreaHere', 'token_5'),
(6, 'user06', '4/G6brFZ1LJ9HQce+mVpH1sGtnMmyn/Fx/o76hl0I2E=', 'lqM9mfb56Voan0uC/zIFmA==', 'AreaHere', 'token_6'),
(7, 'user07', 'saHDGA9WnXUzcO0lW+GOpbACMS6csfCk66c6eahCS0E=', 'EJ5So3TqfaMp65AtZQSrPQ==', 'AreaHere', 'token_7'),
(8, 'user08', '7dctI4QdsMaKDQmeDRSu30TS30HDSZNMlD6ksHs+VQY=', 'Kx2ttviGpJuYa9ijCWhFgQ==', 'AreaHere', 'token_8'),
(9, 'user09', 'a7bA1pffZ+gdSvT9vlsN2TKQzGr1k+hzv4ZDJVFkquQ=', 'XbrlcChaOQvgLiQ0jI7LJw==', 'AreaHere', 'token_9'),
(10, 'user10', 'tn00m4oo2Q8MriuOrXdapD9s1Cq51PYHNcIgaEdxLgM=', 'Ch5dEpcyl8Sjd86AzDRiNA==', 'AreaHere', 'token_10'),
(11, 'user11', 'aoht7EVzEMFmb1Q97xxcPbAeakg0Qm536w0NDhyi56Y=', '5Sgw9+0SRyT7bmWHdYcBqA==', 'AreaHere', 'token_11'),
(12, 'user12', '2pp5o9w/w+89iRe5XLARl6qys4IE0YztCl02LyyhfNY=', '6zp/MnqPthiU03aaIz/zCQ==', 'AreaHere', 'token_12'),
(13, 'user13', 'AR8tn7TK72D5q4ykPJwdy+DbsBva/AJU8CWZ36DBD3Q=', 'hTRZzwgVT06fUpyY9aD69g==', 'AreaHere', 'token_13'),
(14, 'user14', 'U2sr9WXcPlN4otBxx+W/PYGJOyszMGadZd6L4UDYztg=', 'pf9ouWbeMY+63AecRb9rmw==', 'AreaHere', 'token_14'),
(15, 'user15', 'rzDBzFDltT9HRn3iePtY8phURtqCd3rpO2MjGRqTokU=', 'IIypcm6JUNqrEWmFGR95ug==', 'AreaHere', 'token_15'),
(16, 'user16', 'c775nbpvfOhizqth0Vlgmiz6fXrO88YauYb13gwi9us=', 'OUGlY6bOaZ6QBqTeG8jOAw==', 'AreaHere', 'token_16'),
(17, 'user17', 'UKBqPOgaWdHtfJ3UQ4oi9kGUUHi3riLr4Mugr3+EjMA=', '8ez2xN23NWv8qwos4OHmoA==', 'AreaHere', 'token_17'),
(18, 'user18', 'anDiHCF88LiPclGIlBawol0XbzgvEAQ2QidkHMPi6uc=', 'v8ea1TbB2sDFa1HgnnBqlQ==', 'AreaHere', 'token_18'),
(19, 'user19', 'HF/KtJmNGTTT5IciNDWLpIrHz6LbWKngXHKhwlN0bug=', 'bTMqa2KzhpnS+esBO1hBWw==', 'AreaHere', 'token_19'),
(20, 'user20', '0rZMmkRuwetRPl9GJAJJLxxSa2M1BOw9q3Uk3WMBvs8=', '5xPJ20eh7nxmAD4XdGIrqw==', 'AreaHere', 'token_20');
