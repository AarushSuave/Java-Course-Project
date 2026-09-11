-- ====================================================================
-- RESCUENET – DISASTER RESPONSE MANAGEMENT SYSTEM
-- MySQL Database Setup Script
-- Course: Second Year Engineering Java OOP Mini Project
-- ====================================================================

-- 1. Create Database
CREATE DATABASE IF NOT EXISTS `rescuenet`;
USE `rescuenet`;

-- 2. Drop existing tables if recreating
DROP TABLE IF EXISTS `victims`;
DROP TABLE IF EXISTS `teams`;
DROP TABLE IF EXISTS `shelters`;
DROP TABLE IF EXISTS `disasters`;
DROP TABLE IF EXISTS `users`;

-- 3. Users Table (Authentication)
CREATE TABLE `users` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(50) NOT NULL UNIQUE,
    `password` VARCHAR(50) NOT NULL,
    `role` VARCHAR(30) NOT NULL DEFAULT 'Administrator'
);

-- Insert Default Admin User
INSERT INTO `users` (`username`, `password`, `role`) VALUES
('admin', 'admin123', 'Administrator');

-- 4. Disasters Table
CREATE TABLE `disasters` (
    `disaster_id` INT PRIMARY KEY,
    `type` VARCHAR(50) NOT NULL,
    `location` VARCHAR(100) NOT NULL,
    `severity` VARCHAR(30) NOT NULL
);

-- Insert Sample Disasters
INSERT INTO `disasters` (`disaster_id`, `type`, `location`, `severity`) VALUES
(101, 'Flood', 'River Valley Zone B', 'High'),
(102, 'Earthquake', 'North District Sector 4', 'Critical'),
(103, 'Urban Fire', 'Industrial Area Gate 2', 'Medium');

-- 5. Rescue Teams Table
CREATE TABLE `teams` (
    `team_id` INT PRIMARY KEY,
    `team_name` VARCHAR(100) NOT NULL,
    `member_count` INT NOT NULL,
    `is_available` BOOLEAN NOT NULL DEFAULT TRUE,
    `vehicle_type` VARCHAR(50) NOT NULL
);

-- Insert Sample Rescue Teams
INSERT INTO `teams` (`team_id`, `team_name`, `member_count`, `is_available`, `vehicle_type`) VALUES
(1, 'Alpha Rapid Response', 6, TRUE, 'Ambulance (ICU Equipped)'),
(2, 'Bravo Evacuation Squad', 8, TRUE, 'Rescue Van (Flood Equipped)'),
(3, 'Delta Marine Unit', 5, FALSE, 'Rescue Van (Heavy Duty)');

-- 6. Shelters Table
CREATE TABLE `shelters` (
    `shelter_id` INT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `location` VARCHAR(100) NOT NULL,
    `capacity` INT NOT NULL,
    `occupied` INT NOT NULL DEFAULT 0
);

-- Insert Sample Shelters
INSERT INTO `shelters` (`shelter_id`, `name`, `location`, `capacity`, `occupied`) VALUES
(1, 'City Central Community Hall', 'Downtown', 10, 4),
(2, 'St. Mary School Relief Camp', 'North Hill', 8, 8),
(3, 'Sports Complex Shelter', 'East Wing', 15, 2);

-- 7. Victims Table
CREATE TABLE `victims` (
    `victim_id` INT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `age` INT NOT NULL,
    `phone` VARCHAR(20),
    `location` VARCHAR(100) NOT NULL,
    `medical_status` VARCHAR(50) DEFAULT 'Normal',
    `rescue_status` VARCHAR(50) DEFAULT 'Pending',
    `assigned_team_id` INT DEFAULT 0,
    `allocated_shelter_id` INT DEFAULT 0
);

-- Insert Sample Victims
INSERT INTO `victims` (`victim_id`, `name`, `age`, `phone`, `location`, `medical_status`, `rescue_status`, `assigned_team_id`, `allocated_shelter_id`) VALUES
(1001, 'Aarav Patel', 28, '9123456780', 'River Valley Zone B', 'Minor Injury', 'Pending', 0, 0),
(1002, 'Sunita Devi', 54, '9123456781', 'North District Sector 4', 'Critical', 'Assigned', 1, 0),
(1003, 'Rohan Sen', 19, '9123456782', 'Industrial Area Gate 2', 'Normal', 'Rescued', 2, 0),
(1004, 'Meera Nair', 42, '9123456783', 'Downtown', 'Normal', 'Shelter Reached', 0, 1);
