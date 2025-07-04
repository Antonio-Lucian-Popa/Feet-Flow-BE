-- USERS
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    keycloak_id UUID NOT NULL UNIQUE,
    email VARCHAR(100) UNIQUE NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('USER', 'CREATOR', 'ADMIN')),
    bio TEXT,
    profile_picture_url TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- POSTS
CREATE TABLE posts (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    creator_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title VARCHAR(150),
    description TEXT,
    is_public BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- POST MEDIA (poze & video multiple per postare)
CREATE TABLE post_media (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    post_id UUID NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
    media_url TEXT NOT NULL,
    media_type VARCHAR(10) CHECK (media_type IN ('photo', 'video')),
    thumbnail_url TEXT,
    order_index INTEGER DEFAULT 0
);

-- VOTES (like/dislike)
CREATE TABLE votes (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    post_id UUID NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
    value INTEGER CHECK (value IN (1, -1)),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, post_id)
);

-- SUBSCRIPTIONS (pentru faza următoare)
CREATE TABLE subscriptions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    subscriber_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    creator_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    UNIQUE(subscriber_id, creator_id)
);

-- COMMENTS (opțional)
CREATE TABLE comments (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    post_id UUID NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- FOLLOWS (cu target_id pentru a respecta codul)
CREATE TABLE follows (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    follower_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    target_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(follower_id, target_id)
);

-- INDEXES
CREATE INDEX idx_posts_creator_id ON posts(creator_id);
CREATE INDEX idx_post_media_post_id ON post_media(post_id);
CREATE INDEX idx_votes_post_id ON votes(post_id);
CREATE INDEX idx_votes_user_id ON votes(user_id);
CREATE INDEX idx_comments_post_id ON comments(post_id);
CREATE INDEX idx_comments_user_id ON comments(user_id);
CREATE INDEX idx_subscriptions_subscriber ON subscriptions(subscriber_id);
CREATE INDEX idx_subscriptions_creator ON subscriptions(creator_id);
CREATE INDEX idx_follows_follower_id ON follows(follower_id);
CREATE INDEX idx_follows_target_id ON follows(target_id);

-- AGGREGATION HELPERS
-- Număr de postări pentru un user
CREATE OR REPLACE FUNCTION count_posts_by_creator(uid UUID)
RETURNS INTEGER AS $$
SELECT COUNT(*) FROM posts WHERE creator_id = uid;
$$ LANGUAGE SQL;

-- Număr de voturi pozitive pe postările userului
CREATE OR REPLACE FUNCTION count_likes_for_creator(uid UUID)
RETURNS INTEGER AS $$
SELECT COUNT(*) FROM votes v
JOIN posts p ON p.id = v.post_id
WHERE p.creator_id = uid AND v.value = 1;
$$ LANGUAGE SQL;

-- Număr de subscriberi
CREATE OR REPLACE FUNCTION count_subscribers(uid UUID)
RETURNS INTEGER AS $$
SELECT COUNT(*) FROM subscriptions WHERE creator_id = uid AND is_active = true;
$$ LANGUAGE SQL;

-- Număr de followers
CREATE OR REPLACE FUNCTION count_followers(uid UUID)
RETURNS INTEGER AS $$
SELECT COUNT(*) FROM follows WHERE target_id = uid;
$$ LANGUAGE SQL;

-- Număr de following
CREATE OR REPLACE FUNCTION count_following(uid UUID)
RETURNS INTEGER AS $$
SELECT COUNT(*) FROM follows WHERE follower_id = uid;
$$ LANGUAGE SQL;
