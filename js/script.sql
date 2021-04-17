CREATE TABLE words
(
    word character varying NOT NULL
);

CREATE INDEX words_word_index ON words (word);

CREATE TABLE words2
(
    word character varying NOT NULL
);

CREATE INDEX words2_word_index ON words2 (word);