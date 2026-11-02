CREATE TABLE `location` (
                            `id` int PRIMARY KEY AUTO_INCREMENT,
                            `room` int NOT NULL,
                            `shelf` varchar(16) NOT NULL,
                            `level` int NOT NULL,
                            `section` int NOT NULL
);

CREATE TABLE `package` (
                           `id` int PRIMARY KEY AUTO_INCREMENT,
                           `location_id` int,
                           `package_information_id` int
);

CREATE TABLE `package_information` (
                                       `id` int PRIMARY KEY AUTO_INCREMENT,
                                       `recipient_name` varchar(255) NOT NULL,
                                       `verification` varchar(32) NOT NULL UNIQUE,
                                       `email` varchar(255) NOT NULL,
                                       `status` varchar(32) NOT NULL,
                                       `stored` boolean NOT NULL DEFAULT false,
                                       `pickup_date` datetime,
                                       `arrival_date` datetime
);

CREATE TABLE `user` (
                        `id` int PRIMARY KEY AUTO_INCREMENT,
                        `username` varchar(255) UNIQUE NOT NULL,
                        `email` varchar(255) UNIQUE NOT NULL,
                        `password_hash` varchar(255) NOT NULL,
                        `password_salt` varchar(255) NOT NULL,
                        `role` varchar(16) NOT NULL
);

ALTER TABLE `package` ADD FOREIGN KEY (`location_id`) REFERENCES `location` (`id`);

ALTER TABLE `package` ADD FOREIGN KEY (`package_information_id`) REFERENCES `package_information` (`id`);

ALTER TABLE package_information
    ADD UNIQUE (verification);

ALTER TABLE `user`
    ADD UNIQUE (username);

ALTER TABLE `user`
    ADD UNIQUE (email);