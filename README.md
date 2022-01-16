## StockApp

A simple SpringBoot application for following stock prices

<hr>

#### Dependencies

dev:

* [AuthApi](https://github.com/r-c-s/AuthApi)

runtime:
* [Auth](https://github.com/r-c-s/Auth) 
* [MongoDB](https://docs.mongodb.com/manual/installation/)

<hr>

##### Build

<pre>
mvn clean package
</pre>

##### Run unit tests

<pre>
mvn test
</pre>

##### Run integration tests 
MongoDB and Auth (and its dependencies) must be running. A user RCS_STOCKS_TEST_USERNAME with password RCS_STOCKS_TEST_PASSWORD must exist; follow the steps below to register.

<pre>
mvn clean test-compile failsafe:integration-test
</pre>

<br>

##### Run application

<pre>
java -jar target/StockApp-1.0-SNAPSHOT.jar
</pre>

or

<pre>
mvn spring-boot:run
</pre>

or

<pre>
docker-compose build --no-cache && docker-compose up web
</pre>

<br>

##### First, register on Auth service

<pre>
curl -X POST [AUTH_URL]/register -d "username=<USERNAME>&password=<PASSWORD>"
</pre>

##### Then login on Auth service to obtain the cookies

<pre>
curl -X POST [AUTH_URL]/login -d "username=USERNAME&password=PASSWORD" -c cookies
</pre>

##### Follow a stock

<pre>
curl -b cookies -X PUT host:port/my-stocks?stock=IBM
</pre>

##### Unfollow a stock

<pre>
curl -b cookies -X DELETE host:port/my-stocks?stock=IBM
</pre>

##### Get your stocks

<pre>
curl -b cookies -X GET host:port/my-stocks
</pre>

##### Search for stocks by symbol or description

<pre>
curl -b cookies -X GET host:port/stocks?search=MICRO&limit=10
</pre>