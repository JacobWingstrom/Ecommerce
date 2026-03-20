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
(1,  'alice01',    'WO+4ibxbvsoFCf9FiDWEf1f2hh6TKgoR04asRI9I3kQ=', '5Cm/cySyxOotmaKQjxAxJQ==', 'Phoenix', 'token_1'),
(2,  'bob02',      'mew4D5h8RYn/lBTpRu2bPMp5eFFcCrSbJszrITqWvH8=', 'I3RvIJOg62YgCtmwEX3sag==', 'Tucson', 'token_2'),
(3,  'charlie03',  'u8MD8PJel1Tb99DzdcSHXQgKLyyAYlMUzrpc2zAPHZ0=', '7WtDCiCafuup+IUXQoNflA==', 'Mesa', 'token_3'),
(4,  'dana04',     '8lV5xR1Gk0ZqscgASImqdE2SmsA2BZY1+gMmdtssFiM=', 'sWOZjVl0HejBxC3A0ORfAA==', 'Tempe', 'token_4'),
(5,  'ethan05',    'zeVO6H7rjEpOfyvLlecd8aTE7TpFb58zlqCLw4vjfL8=', 'VC30mgRpGa9TmtMSi/krFg==', 'Scottsdale', 'token_5'),
(6,  'fiona06',    'uFtKM2/GpGRsinBaEajwy9l6CQiBd+TLLRvXHSH5Vl8=', '58wRvAVw0UGra4dYq6aYcQ==', 'Yuma', 'token_6'),
(7,  'george07',   'BICQiWa4DM5Bn6Dk2V7vBBX+2p4n82mRqgHD1euAZIw=', 'fSMX4ND/KanBSF5DjasIlg==', 'Chandler', 'token_7'),
(8,  'hannah08',   '9gacnlegGuJmk0fBzTFOHIlV2KyaFrb6zDPWpH139iM=', 'D1W2iXlz6H4ZJHh6n9pYvQ==', 'Glendale', 'token_8'),
(9,  'isaac09',    'k3zDBj9RR7gthnsIu8kUtgv4g+GndTWM2+41Xc3MaN0=', '5+l814dBXVX93+eFy0xOQg==', 'Gilbert', 'token_9'),
(10, 'julia10',    'EUoLnnZaShXbqi3Uzxa8R+42uGnyHfJoFKuG6Ox2AaQ=', 'IBOFQQz2zC3f5greqiXfUA==', 'Flagstaff', 'token_10'),
(11, 'kevin11',    'q3nwBhayS2yI8Mk419kjubvebU7RjpmOh+xjKweHVyU=', 'x0pRB9dB/Pa7wUO/jvUYrA==', 'Phoenix', 'token_11'),
(12, 'laura12',    '6zFPyBQx8E9svxcs8fiygm94sobRaVvFAkOuhCUNb0M=', 'uAYn9vlA6rPkn2wieoOr+Q==', 'Tucson', 'token_12'),
(13, 'mike13',     'O8ZrMlKkuod9EVDobDDQb0dAia8qGqGzGvi/Z6XxKcg=', '+yaYKCVOLO3wZNzpZb74dQ==', 'Mesa', 'token_13'),
(14, 'nina14',     'ifiVgPB8oiX023Ld36wn3BxEZ6TG412WlsT2ik0Apao=', 'LfJHh2ch0/s6oiEB2qdmcw==', 'Tempe', 'token_14'),
(15, 'oscar15',    'OvMcn/sUeF1U00RDoRnIyXK005GbY8M5RgGRJjdQHsA=', 'N25e3Dn8QRv9Hv1lRosH6Q==', 'Sedona', 'token_15'),
(16, 'paula16',    'WFrnNOU+g/2JSv/eNi92+A/XxhTAq8JulmE7iwBR77g=', 'Im9KoawSNzvqhy15uEyz4w==', 'Prescott', 'token_16'),
(17, 'quinn17',    'ea0XW4eZrqSC/agyKQgEwAPvwmcRzu/krt8pyRyV3RM=', 'U1D0gHEezEnk22NpxfThEg==', 'Surprise', 'token_17'),
(18, 'rachel18',   '1bnUl/JgmOY10qAAxq4jWoX5ZVMx6DyG1U/nEyL/kQQ=', 'e9JthJubvYTasQiKFC2dUQ==', 'Casa Grande', 'token_18'),
(19, 'sam19',      'XnmqRl6WxxX2DFGtDrr32zcw+aFjJoY3781ajwRH1Mc=', 'K0vHY0sc/kOZDbJsB32VTg==', 'Kingman', 'token_19'),
(20, 'tina20',     'GnIqsB+kYTntNMMlrEtXz43MPagswKTxY8YWxxiJOIU=', 'qLdSUWMPefk3fjld8qlJpg==', 'Lake Havasu', 'token_20');
