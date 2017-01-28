ALTER TABLE venues ADD COLUMN canonical_url VARCHAR(250);
ALTER TABLE venues ADD COLUMN verified INT2;
ALTER TABLE venues ADD COLUMN stats_checkins_count INTEGER;
ALTER TABLE venues ADD COLUMN stats_users_count INTEGER;
ALTER TABLE venues ADD COLUMN stats_tip_count INTEGER;
ALTER TABLE venues ADD COLUMN stats_visits_count INTEGER;
ALTER TABLE venues ADD COLUMN price_tier INTEGER;
ALTER TABLE venues ADD COLUMN price_message VARCHAR(50);
ALTER TABLE venues ADD COLUMN price_currency VARCHAR(10);
ALTER TABLE venues ADD COLUMN likes_count INTEGER;
ALTER TABLE venues ADD COLUMN likes_summary VARCHAR(50);
ALTER TABLE venues ADD COLUMN dislike INT2;
ALTER TABLE venues ADD COLUMN short_url VARCHAR(100);
ALTER TABLE venues ADD COLUMN time_zone VARCHAR(100);
ALTER TABLE venues ADD COLUMN best_photo_id VARCHAR(100);

CREATE TABLE photos (
    id VARCHAR(100) NOT NULL,
    venue_id VARCHAR(100) NOT NULL,
    created_at BIGINT NOT NULL,
    prefix VARCHAR(200) NOT NULL,
    suffix VARCHAR(200) NOT NULL,
    width INTEGER NOT NULL,
    height INTEGER NOT NULL,
    visibility VARCHAR(50) NOT NULL
);