-- =========================
-- ROOMS
-- =========================
INSERT INTO room (id, name, number) VALUES
                                               (1, 'miller' ,1),
                                               (2, 'maestro',2),
                                               (3, 'junior', 3),
                                               (4, 'senior', 4),
                                               (5, 'master', 5),
                                               (6, 'demon', 6),
                                               (7, 'analyzer', 7),
                                               (8, 'visual', 8);

-- =========================
-- SHELVES
-- =========================
INSERT INTO shelf (id, letter, colour, room_id) VALUES
-- Room 1
(1, 'A', '#FFADAD', 1),
(9, 'I', '#FFB6AD', 1),

-- Room 2
(2, 'B', '#FFD6A5', 2),
(10, 'J', '#FFE6A5', 2),

-- Room 3
(3, 'C', '#FDFFB6', 3),

-- Room 4
(4, 'D', '#CAFFBF', 4),

-- Room 5
(5, 'E', '#9BF6FF', 5),

-- Room 6
(6, 'F', '#A0C4FF', 6),

-- Room 7
(7, 'G', '#BDB2FF', 7),

-- Room 8
(8, 'H', '#FFC6FF', 8);

-- =========================
-- LEVELS
-- =========================
INSERT INTO level (shelf_id, number)
SELECT s.id, l.level_id
FROM shelf s
         JOIN (
    SELECT 1 level_id UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL
    SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7
) l
WHERE
-- variable level counts per shelf
    (s.id IN (1,9,11,12,13,14,15,16) AND l.level_id <= 3)
   OR (s.id IN (2) AND l.level_id <= 5)
   OR (s.id IN (3) AND l.level_id <= 7)
   OR (s.id IN (4) AND l.level_id <= 4)
   OR (s.id IN (5) AND l.level_id <= 2)
   OR (s.id IN (6) AND l.level_id <= 6)
   OR (s.id IN (7) AND l.level_id <= 1)
   OR (s.id IN (8) AND l.level_id <= 7)
   OR (s.id IN (10) AND l.level_id <= 3);

-- =========================
-- SECTIONS
-- =========================
INSERT INTO section (shelf_id, level_id, id, is_full) VALUES
-- Shelf A (1)
(1,1,1,false),(1,1,2,true),
(1,2,1,false),(1,2,2,true),
(1,3,1,false),(1,3,2,false),

-- Shelf I (9)
(9,1,1,false),(9,1,2,false),(9,1,3,false),
(9,2,1,false),(9,2,2,false),(9,2,3,false),
(9,3,1,false),(9,3,2,false),(9,3,3,false),

-- Shelf B (2)
(2,1,1,false),
(2,2,1,true),
(2,3,1,false),
(2,4,1,false),
(2,5,1,false),

-- Shelf J (10)
(10,1,1,false),(10,1,2,false),(10,1,3,false),
(10,2,1,false),(10,2,2,false),(10,2,3,false),
(10,3,1,false),(10,3,2,false),(10,3,3,false),

-- Shelf C (3)
(3,1,1,false),(3,1,2,true),(3,1,3,false),
(3,2,1,false),(3,2,2,false),(3,2,3,false),
(3,3,1,true),(3,3,2,false),(3,3,3,true),
(3,4,1,false),(3,4,2,false),(3,4,3,false),
(3,5,1,false),(3,5,2,false),(3,5,3,true),
(3,6,1,true),(3,6,2,false),(3,6,3,false),
(3,7,1,false),(3,7,2,false),(3,7,3,false),

-- Shelf D (4)
(4,1,1,false),(4,1,2,false),(4,1,3,true),(4,1,4,false),
(4,2,1,false),(4,2,2,true),(4,2,3,false),(4,2,4,false),
(4,3,1,false),(4,3,2,false),(4,3,3,false),(4,3,4,false),
(4,4,1,false),(4,4,2,true),(4,4,3,false),(4,4,4,false),

-- Shelf E (5)
(5,1,1,false),(5,1,2,false),(5,1,3,true),
(5,2,1,false),(5,2,2,false),(5,2,3,true),

-- Shelf F (6)
(6,1,1,false),(6,1,2,false),
(6,2,1,false),(6,2,2,true),
(6,3,1,false),(6,3,2,false),
(6,4,1,true),(6,4,2,false),
(6,5,1,false),(6,5,2,false),
(6,6,1,false),(6,6,2,false),

-- Shelf G (7)
(7,1,1,false),(7,1,2,false),(7,1,3,true),(7,1,4,false),

-- Shelf H (8)
(8,1,1,false),
(8,2,1,true),
(8,3,1,false),
(8,4,1,true),
(8,5,1,false),
(8,6,1,false),
(8,7,1,false);