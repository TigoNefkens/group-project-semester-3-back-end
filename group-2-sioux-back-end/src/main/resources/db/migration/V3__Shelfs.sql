CREATE TABLE `room` (
                         `id` int PRIMARY KEY AUTO_INCREMENT,
                         `name` varchar(64) UNIQUE NOT NULL,
                         `number` int UNIQUE NOT NULL

);

CREATE TABLE `shelf` (
                        `id` int PRIMARY KEY AUTO_INCREMENT,
                        `letter` varchar(32) UNIQUE NOT NULL,
                        `colour` varchar(16) NOT NULL DEFAULT '#000000',
                        `room_id` int NOT NULL

);

CREATE TABLE `level` (
                         `number` int NOT NULL,
                         `shelf_id` int NOT NULL
);

CREATE TABLE `section` (
                         `id` int NOT NULL,
                         `is_full` boolean NOT NULL DEFAULT false,
                         `level_id` int NOT NULL,
                         `shelf_id` int NOT NULL
);

ALTER TABLE shelf
    ADD UNIQUE (`room_id`, `letter`);

ALTER TABLE shelf
    ADD FOREIGN KEY (room_id) REFERENCES room(id);

ALTER TABLE level
    ADD PRIMARY KEY (shelf_id, number);

ALTER TABLE level
    ADD FOREIGN KEY (shelf_id) REFERENCES shelf(id);


ALTER TABLE section
    ADD PRIMARY KEY (shelf_id, level_id, id);

ALTER TABLE section
    ADD FOREIGN KEY (shelf_id, level_id)
        REFERENCES level (shelf_id, number);