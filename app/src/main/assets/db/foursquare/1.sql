CREATE TABLE venues (
    id INTEGER NOT NULL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    location_lat DOUBLE NOT NULL,
    location_lng DOUBLE NOT NULL,
    location_cc VARCHAR(10) NOT NULL,
    location_country VARCHAR(20) NOT NULL,
);