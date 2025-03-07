# bible-app
This is a simple Spring Boot bible application with a UI and a REST API. It offers the possibility of reading a chapter or searching for a term in the text of the Bible. Provided bible translations are Luther 1912, Elberfelder 1905, Menge 1939 and other translations in german, english, french, russian and chinese. It is also possible to __count words__ in a bible section specified by the user. In addition, the Hebrew and Greek word roots of the original text can be viewed using the index created by Strong for the translation of Luther 1912.

__A highlight of this application is to count words__ for a user specified section. For example, it is possible to count all words of the bible given the translation of Luther 1912. As a result, 22.416 different words are counted from Genesis 1:1 to Revelation 22:21. Tables show the number for each word, i.e., how often it was actually counted or ignored. A word can be ignored or counted interactively.

After cloning the repository you can build the project with `mvn install` and run it with `java -jar target/bible-app-x.y.z .jar`. You can access the UI via `http://localhost:10000/` and the Swagger UI via `http://localhost:10000/swagger-ui/index.html`.

You can access this bible web application at https://www.123-bibel.de.