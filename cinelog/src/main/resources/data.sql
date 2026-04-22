-- Genres
INSERT IGNORE INTO genres (id, name, description) VALUES
    (1, 'Action', 'High-octane thrills and intense sequences'),
    (2, 'Drama',  'Character-driven stories with emotional depth'),
    (3, 'Sci-Fi', 'Speculative fiction exploring science and the future');

-- Movies
INSERT IGNORE INTO movies (id, title, status, rating, watched_at, created_at, genre_id) VALUES
    (1,  'The Dark Knight',   'watched',       10,   '2024-01-15', NOW(), 1),
    (2,  'Inception',         'watched',        9,   '2024-02-20', NOW(), 3),
    (3,  'The Godfather',     'watching',    NULL,          NULL,  NOW(), 2),
    (4,  'Interstellar',      'want_to_watch', NULL,         NULL,  NOW(), 3),
    (5,  'Parasite',          'watched',        8,   '2024-03-10', NOW(), 2),
    (6,  'Mad Max Fury Road', 'watching',    NULL,          NULL,  NOW(), 1),
    (7,  'Arrival',           'want_to_watch', NULL,         NULL,  NOW(), 3),
    (8,  'Spirited Away',     'dropped',     NULL,          NULL,  NOW(), 2),
    (9,  'The Matrix',        'watched',        9,   '2024-04-01', NOW(), 3),
    (10, 'Heat',              'want_to_watch', NULL,         NULL,  NOW(), 1);
