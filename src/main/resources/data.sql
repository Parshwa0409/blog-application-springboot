INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');

INSERT into users (username, email, password) VALUES ('Alice', 'alice@gamil.com', '$2a$12$9CHk.045yPPmn82kA6L5H.o5JqZkhySM7genfaE6WcD86mYIpB/pm');
INSERT into users (username, email, password) VALUES ('anooj', 'anooj@gamil.com', '$2a$12$9CHk.045yPPmn82kA6L5H.o5JqZkhySM7genfaE6WcD86mYIpB/pm');

INSERT INTO user_roles(user_id, role_id) VALUES (1, 1);
INSERT INTO user_roles(user_id, role_id) VALUES (1, 2);
INSERT INTO user_roles(user_id, role_id) VALUES (2, 1);

-- post (add created_at, updated_at)
INSERT INTO post (title, content, user_id, created_at, updated_at) VALUES(
    'Getting started with Spring Boot',
    'Learn to set up Spring Boot, create REST APIs, and run your app quickly using Spring Initializr and proper project structure.',
    1, 1672531100000, 1672531200000
);

INSERT INTO post (title, content, user_id, created_at, updated_at) VALUES(
    'Maven build tips',
    'Tips to optimize Maven builds: use dependency caching, parallel builds, skip tests when needed, and leverage Maven Wrapper.',
    2, 1672531210000, 1672531310000
);

INSERT INTO post (title, content, user_id, created_at, updated_at) VALUES(
    'JPA entity mappings',
    'Guide to one-to-many and many-to-many mappings in JPA, including annotations, cascading, and best practices for entities.',
    1, 1672531320000, 1672531420000
);

INSERT INTO post (title, content, user_id, created_at, updated_at) VALUES(
    'Bean validation basics',
    'Learn to use javax validation annotations like @NotNull, @Size, @Email in DTOs and entities to handle input validation.',
    2, 1672531430000, 1672531530000
);

INSERT INTO post (title, content, user_id, created_at, updated_at) VALUES(
    'Testing with JUnit 5',
    'Structuring unit and integration tests with JUnit 5, using @Test, @BeforeEach, @AfterEach, and Mockito for mocking.',
    1, 1672531540000, 1672531640000
);

INSERT INTO post (title, content, user_id, created_at, updated_at) VALUES(
    'Spring Security overview',
    'Intro to Spring Security: configure authentication, authorization, roles, and secure REST APIs, including JWT basics.',
    2, 1672531650000, 1672531750000
);

-- Additional posts to reach 20
INSERT INTO post (title, content, user_id, created_at, updated_at) VALUES ('REST API design','Best practices for designing REST APIs: resources, verbs, status codes.',1,1672531760000,1672531860000);
INSERT INTO post (title, content, user_id, created_at, updated_at) VALUES ('Dockerizing Spring Boot','Create Dockerfile, multi-stage builds, and compose.',2,1672531870000,1672531970000);
INSERT INTO post (title, content, user_id, created_at, updated_at) VALUES ('Kafka basics','Produce/consume messages, topics, partitions.',1,1672531980000,1672532080000);
INSERT INTO post (title, content, user_id, created_at, updated_at) VALUES ('Observability','Metrics, logs, traces with Micrometer.',2,1672532090000,1672532190000);
INSERT INTO post (title, content, user_id, created_at, updated_at) VALUES ('Caching strategies','@Cacheable, eviction, Redis.',1,1672532200000,1672532300000);
INSERT INTO post (title, content, user_id, created_at, updated_at) VALUES ('Error handling','ControllerAdvice, problem details.',2,1672532310000,1672532410000);
INSERT INTO post (title, content, user_id, created_at, updated_at) VALUES ('Pagination & sorting','Spring Data pageable.',1,1672532420000,1672532520000);
INSERT INTO post (title, content, user_id, created_at, updated_at) VALUES ('File uploads','Multipart handling and validation.',2,1672532530000,1672532630000);
INSERT INTO post (title, content, user_id, created_at, updated_at) VALUES ('WebSockets','Realtime updates with STOMP.',1,1672532640000,1672532740000);
INSERT INTO post (title, content, user_id, created_at, updated_at) VALUES ('GraphQL','Schema and resolvers overview.',2,1672532750000,1672532850000);
INSERT INTO post (title, content, user_id, created_at, updated_at) VALUES ('CI/CD pipelines','GitHub Actions basics.',1,1672532860000,1672532960000);
INSERT INTO post (title, content, user_id, created_at, updated_at) VALUES ('Testing REST','RestAssured examples.',2,1672532970000,1672533070000);
INSERT INTO post (title, content, user_id, created_at, updated_at) VALUES ('Security testing','OWASP and common vulns.',1,1672533080000,1672533180000);
INSERT INTO post (title, content, user_id, created_at, updated_at) VALUES ('Performance tuning','JVM, GC, profiling tips.',2,1672533190000,1672533290000);

-- comment (add created_at, updated_at; post_id assumes IDs auto-increment from 1..20)
INSERT INTO comment (content, post_id, user_id, created_at, updated_at) VALUES ('Nice overview, thanks!', 1, 2, 1672534000000, 1672534010000);
INSERT INTO comment (content, post_id, user_id, created_at, updated_at) VALUES ('Add sample repo?', 1, 1, 1672534020000, 1672534030000);
INSERT INTO comment (content, post_id, user_id, created_at, updated_at) VALUES ('Helpful guide.', 2, 1, 1672534040000, 1672534050000);
INSERT INTO comment (content, post_id, user_id, created_at, updated_at) VALUES ('Use -T for parallel builds.', 2, 2, 1672534060000, 1672534070000);
INSERT INTO comment (content, post_id, user_id, created_at, updated_at) VALUES ('Clear explanation on relationships.', 3, 2, 1672534080000, 1672534090000);
INSERT INTO comment (content, post_id, user_id, created_at, updated_at) VALUES ('Show cascade examples.', 3, 1, 1672534100000, 1672534110000);
INSERT INTO comment (content, post_id, user_id, created_at, updated_at) VALUES ('Validation groups would be useful.', 4, 1, 1672534120000, 1672534130000);
INSERT INTO comment (content, post_id, user_id, created_at, updated_at) VALUES ('Include custom validators.', 4, 2, 1672534140000, 1672534150000);
INSERT INTO comment (content, post_id, user_id, created_at, updated_at) VALUES ('Please cover mocking.', 5, 2, 1672534160000, 1672534170000);
INSERT INTO comment (content, post_id, user_id, created_at, updated_at) VALUES ('Parametrized tests FTW.', 5, 1, 1672534180000, 1672534190000);
-- More comments across later posts
INSERT INTO comment (content, post_id, user_id, created_at, updated_at) VALUES ('Great API design pointers.', 7, 1, 1672534200000, 1672534210000);
INSERT INTO comment (content, post_id, user_id, created_at, updated_at) VALUES ('Docker compose snippet?', 8, 2, 1672534220000, 1672534230000);
INSERT INTO comment (content, post_id, user_id, created_at, updated_at) VALUES ('What about consumer groups?', 9, 1, 1672534240000, 1672534250000);
INSERT INTO comment (content, post_id, user_id, created_at, updated_at) VALUES ('Micrometer tags example.', 10, 2, 1672534260000, 1672534270000);
INSERT INTO comment (content, post_id, user_id, created_at, updated_at) VALUES ('Cache invalidation is hard.', 11, 1, 1672534280000, 1672534290000);
INSERT INTO comment (content, post_id, user_id, created_at, updated_at) VALUES ('ProblemDetails RFC link?', 12, 2, 1672534300000, 1672534310000);
INSERT INTO comment (content, post_id, user_id, created_at, updated_at) VALUES ('Pageable tips appreciated.', 13, 1, 1672534320000, 1672534330000);
INSERT INTO comment (content, post_id, user_id, created_at, updated_at) VALUES ('Upload size limits?', 14, 2, 1672534340000, 1672534350000);
INSERT INTO comment (content, post_id, user_id, created_at, updated_at) VALUES ('STOMP subscriptions?', 15, 1, 1672534360000, 1672534370000);
INSERT INTO comment (content, post_id, user_id, created_at, updated_at) VALUES ('GraphQL vs REST debate.', 16, 2, 1672534380000, 1672534390000);
INSERT INTO comment (content, post_id, user_id, created_at, updated_at) VALUES ('Action workflow example?', 17, 1, 1672534400000, 1672534410000);
INSERT INTO comment (content, post_id, user_id, created_at, updated_at) VALUES ('RestAssured matcher tips.', 18, 2, 1672534420000, 1672534430000);
INSERT INTO comment (content, post_id, user_id, created_at, updated_at) VALUES ('Top 10 OWASP list?', 19, 1, 1672534440000, 1672534450000);
INSERT INTO comment (content, post_id, user_id, created_at, updated_at) VALUES ('JVM flags for perf?', 20, 2, 1672534460000, 1672534470000);

-- likes (user_id, post_id)
INSERT INTO likes (user_id, post_id, created_at) VALUES (1, 1, 1672531199000);
INSERT INTO likes (user_id, post_id, created_at) VALUES (1, 3, 1672531199000);
INSERT INTO likes (user_id, post_id, created_at) VALUES (1, 5, 1672531199000);
INSERT INTO likes (user_id, post_id, created_at) VALUES (2, 1, 1672531199000);
INSERT INTO likes (user_id, post_id, created_at) VALUES (2, 2, 1672531199000);
INSERT INTO likes (user_id, post_id, created_at) VALUES (2, 4, 1672531199000);

-- favorites (user_id, post_id)
INSERT INTO favorites (user_id, post_id, created_at) VALUES (1, 2, 1672531199000);
INSERT INTO favorites (user_id, post_id, created_at) VALUES (1, 4, 1672531199000);
INSERT INTO favorites (user_id, post_id, created_at) VALUES (2, 3, 1672531199000);
INSERT INTO favorites (user_id, post_id, created_at) VALUES (2, 5, 1672531199000);
