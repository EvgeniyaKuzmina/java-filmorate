# java-filmorate
Spring Boot, Maven, H2, Lombok, Docker

## DataBase
Link to application database: https://dbdiagram.io/d/6280e1207f945876b61eae22

DataBase has several tables:
1. user — stores all users. Include next fields:
   * id — primary key
   * email
   * login
   * name
   * birthday
 
2. friends — stores information about friendship between a couple of users. Include next fields:
   * user_id
   * friend_id
   * status: 
     - true — the application to friend is confirmed
     - false — the application to friend  not confirmed

3. film — stores all films. Include next fields:
   * id — primary key
   * name 
   * description 
   * duration
   * releaseDate
  
4. likes — stores information about users like films.  Include next fields:
   * user_id 
   * film_id

5. genre — stores information about different genres. Include next fields:
   * id
   * genre

6. film_genre — stores information about which  film has which genres. Include next fields:
   * film_id
   * genre_id
  
7. rating — stores information about ratings Motion Picture Association of America. Include next fields:
   * id
   * rating

8. film_rating — stores information about which film has which rating. Include next fields:
   * film_id
   * rating_id

## Application has next actions: 
  ### about User
- make registration new User
- change personal information
- delete account
  ### about Film
- add new film in film table
- change information about existing film
- delete Film

  ### about action between Users and Films
- User can add friend and can ask to make a friendship to another user
- User can add like to film or delete like from film
- Get common Friends with another User
- Get list of Friends by User id
- Get most popular films (You can indicate how many popular films you want to see, or not indicate — in this way you get 10 most popular films)



