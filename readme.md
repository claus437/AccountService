# ApplicationService

Build with JDK 17 and Maven 3.8.6 on Windows 10

#### Building:
`mvn clean install`

#### Running:
`java -jar target\account-service-1.0-SNAPSHOT.jar`

#### Testing:

Create a new account \
`curl -X POST http://localhost:8080/create`

Add a transaction to account #1 of 101.00\ 
`curl -H "Content-Type: application/json" -X POST localhost:8080/transaction/add/1 -d 101`

Get the balance of account #1\
`curl -X GET localhost:8080/balance/1`

List 10 latest transactions of account #1
`curl -X GET localhost:8080/transaction/list/1`

### TODO'S

* Setup Maven/FailSafe to run the tests, as these type of tests belong in the integration cycle not the unit testing cycle.
* AccountServiceException should be split to more specific, e.g. NoSuchAccountException, NotEnoughMoneyException.
* Returning entity classes all the way from the db layer to the client (Transaction) is almost always a bad idea, would have been better to map it in the controller.
* Adding swagger ui... perhaps eureka or similar... and all the other cool stuff. 
