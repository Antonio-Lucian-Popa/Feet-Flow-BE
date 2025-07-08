--liquibase formatted sql

--changeset antonio:seed-users
INSERT INTO users (id, first_name, last_name, keycloak_id, email, role, bio, profile_picture_url, coverImageUrl)
SELECT '00000000-0000-0000-0000-000000000001'::uuid, 'Ana', 'Sole', uuid_generate_v4(), 'ana@feet.com', 'CREATOR', 'Elegant feet',
       'http://localhost:8080/uploads/images/00000000-0000-0000-0000-000000000001/profile.jpg',
       'http://localhost:8080/uploads/images/00000000-0000-0000-0000-000000000001/cover.jpg'
UNION ALL
SELECT '00000000-0000-0000-0000-000000000002'::uuid, 'Lara', 'Steps', uuid_generate_v4(), 'lara@feet.com', 'CREATOR', 'Gym feet',
       'http://localhost:8080/uploads/images/00000000-0000-0000-0000-000000000002/profile.jpg',
       'http://localhost:8080/uploads/images/00000000-0000-0000-0000-000000000002/cover.jpg'
UNION ALL
SELECT '00000000-0000-0000-0000-000000000003'::uuid, 'Eva', 'Toes', uuid_generate_v4(), 'eva@feet.com', 'CREATOR', 'Casual barefoot model',
       '/uploads/images/00000000-0000-0000-0000-000000000003/profile.jpg',
       '/uploads/images/00000000-0000-0000-0000-000000000003/cover.jpg'
UNION ALL
SELECT '00000000-0000-0000-0000-000000000004'::uuid, 'Mihai', 'Viewer', uuid_generate_v4(), 'mihai@users.com', 'USER', null, null, null
UNION ALL
SELECT '00000000-0000-0000-0000-000000000005'::uuid, 'Radu', 'Watcher', uuid_generate_v4(), 'radu@users.com', 'USER', null, null, null;

--changeset antonio:seed-posts
INSERT INTO posts (id, creator_id, title, description, is_public, created_at) VALUES
  ('10000000-0000-0000-0000-000000000001'::uuid, '00000000-0000-0000-0000-000000000001'::uuid, 'Soft Soles Showcase', 'Relaxing barefoot session 👣', true, now() - interval '3 days'),
  ('10000000-0000-0000-0000-000000000002'::uuid, '00000000-0000-0000-0000-000000000002'::uuid, 'Gym Barefoot Routine', 'Post-workout feet', true, now() - interval '2 days'),
  ('10000000-0000-0000-0000-000000000003'::uuid, '00000000-0000-0000-0000-000000000003'::uuid, 'Lazy Sunday Chill', 'Feet up on the couch', true, now() - interval '1 day');

--changeset antonio:seed-post-media
INSERT INTO post_media (id, post_id, media_url, media_type, thumbnail_url, order_index) VALUES
  (uuid_generate_v4(), '10000000-0000-0000-0000-000000000001'::uuid, 'http://localhost:8080/uploads/images/10000000-0000-0000-0000-000000000001/soft1.jpeg', 'photo', null, 0),
  (uuid_generate_v4(), '10000000-0000-0000-0000-000000000001'::uuid, 'http://localhost:8080/uploads/images/10000000-0000-0000-0000-000000000001/soft2.jpeg', 'photo', null, 1),
  (uuid_generate_v4(), '10000000-0000-0000-0000-000000000002'::uuid, 'http://localhost:8080/uploads/images/10000000-0000-0000-0000-000000000002/soft3.jpeg', 'photo', null, 0),
  (uuid_generate_v4(), '10000000-0000-0000-0000-000000000002'::uuid, 'http://localhost:8080/uploads/images/10000000-0000-0000-0000-000000000002/soft4.jpeg', 'photo', null, 1),
  (uuid_generate_v4(), '10000000-0000-0000-0000-000000000002'::uuid, 'http://localhost:8080/uploads/images/10000000-0000-0000-0000-000000000002/soft5.jpeg', 'photo', null, 2),
  (uuid_generate_v4(), '10000000-0000-0000-0000-000000000003'::uuid, 'http://localhost:8080/uploads/images/10000000-0000-0000-0000-000000000003/soft6.jpeg', 'photo', null, 0),
  (uuid_generate_v4(), '10000000-0000-0000-0000-000000000003'::uuid, 'http://localhost:8080/uploads/images/10000000-0000-0000-0000-000000000003/soft7.jpeg', 'photo', null, 1),
  (uuid_generate_v4(), '10000000-0000-0000-0000-000000000003'::uuid, 'http://localhost:8080/uploads/images/10000000-0000-0000-0000-000000000003/soft8.jpeg', 'photo', null, 2),
  (uuid_generate_v4(), '10000000-0000-0000-0000-000000000003'::uuid, 'http://localhost:8080/uploads/images/10000000-0000-0000-0000-000000000003/soft9.jpeg', 'photo', null, 3);

--changeset antonio:seed-votes
INSERT INTO votes (id, user_id, post_id, value) VALUES
  (uuid_generate_v4(), '00000000-0000-0000-0000-000000000004'::uuid, '10000000-0000-0000-0000-000000000001'::uuid, 1),
  (uuid_generate_v4(), '00000000-0000-0000-0000-000000000005'::uuid, '10000000-0000-0000-0000-000000000002'::uuid, -1);

--changeset antonio:seed-follows
INSERT INTO follows (id, follower_id, target_id, created_at) VALUES
  (uuid_generate_v4(), '00000000-0000-0000-0000-000000000004'::uuid, '00000000-0000-0000-0000-000000000001'::uuid, now()),
  (uuid_generate_v4(), '00000000-0000-0000-0000-000000000005'::uuid, '00000000-0000-0000-0000-000000000002'::uuid, now());

--changeset antonio:seed-subscriptions
INSERT INTO subscriptions (id, subscriber_id, creator_id, start_date, end_date, is_active) VALUES
  (uuid_generate_v4(), '00000000-0000-0000-0000-000000000004'::uuid, '00000000-0000-0000-0000-000000000001'::uuid, current_date, current_date + INTERVAL '30 days', true),
  (uuid_generate_v4(), '00000000-0000-0000-0000-000000000005'::uuid, '00000000-0000-0000-0000-000000000002'::uuid, current_date, current_date + INTERVAL '30 days', true);

--changeset antonio:seed-comments
INSERT INTO comments (id, user_id, post_id, content) VALUES
  (uuid_generate_v4(), '00000000-0000-0000-0000-000000000004'::uuid, '10000000-0000-0000-0000-000000000001'::uuid, 'Love this set 🔥'),
  (uuid_generate_v4(), '00000000-0000-0000-0000-000000000005'::uuid, '10000000-0000-0000-0000-000000000002'::uuid, 'Could use more angles 😉');
