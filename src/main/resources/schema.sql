 CREATE TABLE IF NOT EXISTS users (
     id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
     email varchar not NULL,
     login varchar not NULL,
     name varchar not NULL,
     birthday date
);


CREATE TABLE IF NOT EXISTS films (
   id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
   name varchar not NULL,
   description varchar(200),
   duration double not NULL,
   releaseDate date
);

Create Table IF NOT EXISTS friends (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id INTEGER REFERENCES users (id) ON DELETE RESTRICT,
    friend_id INTEGER REFERENCES users (id) ON DELETE CASCADE
);

Create Table IF NOT EXISTS likes (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id INTEGER REFERENCES users (id) ON DELETE RESTRICT,
    film_id INTEGER REFERENCES films (id) ON DELETE CASCADE
);

Create Table IF NOT EXISTS genre(
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    genre varchar
);

Create Table IF NOT EXISTS film_genre(
     id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
     genre_id INTEGER REFERENCES genre(id) ON DELETE CASCADE,
     film_id INTEGER REFERENCES films(id) ON DELETE CASCADE
);

Create Table IF NOT EXISTS rating(
     id_r INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
     rating varchar
);

Create Table IF NOT EXISTS film_rating(
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    rating_id INTEGER REFERENCES genre(id) ON DELETE CASCADE,
    film_id INTEGER REFERENCES films(id) ON DELETE CASCADE
);

