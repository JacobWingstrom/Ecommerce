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
    user_id INT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hashed VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    area VARCHAR(50)
);

ALTER TABLE users ADD COLUMN salt VARCHAR(255) NOT NULL;

-- =====================================================
-- Items Table
-- Auction items
-- approved_flag = 1 means admin approved
-- highest_bidder_id may be NULL
-- =====================================================
CREATE TABLE IF NOT EXISTS items (
    item_id INT PRIMARY KEY,
    seller_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    curr_price DECIMAL(10,2) DEFAULT 0,
    highest_bidder_id INT,
    end_time DATETIME,
    approved_flag TINYINT DEFAULT 0,
    tag VARCHAR(50),

    FOREIGN KEY (seller_id) REFERENCES users(user_id),
    FOREIGN KEY (highest_bidder_id) REFERENCES users(user_id)
);

-- =====================================================
-- Bids Table
-- =====================================================
CREATE TABLE IF NOT EXISTS bids (
    bid_id INT PRIMARY KEY,
    bidder_id INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    timestamp DATETIME,

    FOREIGN KEY (bidder_id) REFERENCES users(user_id)
);

-- =====================================================
-- Availability Table
-- Tracks seller availability blocks
-- =====================================================
CREATE TABLE IF NOT EXISTS availability (
    avail_id INT PRIMARY KEY,
    user_id INT NOT NULL,
    start_time TIME,
    end_time TIME,
    day VARCHAR(20),

    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
