# Basiq dev assignment

### Assignment task:
Create application which will integrate with Basiq http api. [Basiq api refference](https://api.basiq.io/reference),
Subtasks:
*  Register on [Basiq](https://basiq.io/) website - to get api key
*  Use acquired api key to obtain "access_token" which will be used in all api actions
* Create user
* Create connection with bank institution with following credentials:
```json
{
  "loginId": "gavinBelson",
  "password": "hooli2016",
  "institution":{
    "id":"AU00000"
  }
}
```
* Fetch all transactions for that user and calculate average amount for all transactions with "direction" = "debit"

### Assignment solution application:

This application is written in Java programming language, Http requests are executed with Unirest http client. Json to java POJOS conversion is managed with GSON. Junit testing framework is used for writting tests.

### Technologies used

java app uses a following open source projects in background:

* [Unirest](https://github.com/Kong/unirest-java) - Simple HTTP client for Java!
* [GSON](https://github.com/google/gson) - Gson is a Java library for converting Java Objects into their JSON representation and vice versa.
* [Junit](https://github.com/junit-team) - Tesging framework

### How can I use this application:
  - Clone repository
```sh
$ git clone git@github.com:milanbojovic/unirest_basiq_playground.git
```
  - Import in editor of your choice and run with parameter api key as parameter to main method

### Example output:
![Example program output](https://github.com/milanbojovic/unirest_basiq_playground/blob/master/images/Example_output.png)

**Have fun !**

License
----

MIT
**Free Software**
