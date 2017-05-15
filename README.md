# Car Adverts web service

REST-full web service written in Scala using Play Framework.

The application is deployed on AWS.

## Constraints
* Scala as a programming
* Play 2.4.x as a web framework
* DynamoDB for a persistence storage

## Assumptions
* The application is a prototype
    * Not necessary to produce high quality documentation, it's enough just to have a list of sample requests that show how the system works
    * Not necessary to have a possibility to scale the application horizontally, one server is enough
    * Not necessary to use a strict coding style
* The application is to be used internally by a small group of people: 
    * No Authentication and Authorization is required
* The application should not be designed for high load and high data volumes, therefore it's:
    * Not necessary to use async communication with a persistence storage
    * Not necessary to use server-side caching
    * Data can be sorted in memory on the server

## Non-functional requirements
* Since it's a prototype there're no defined requirements for performance (latency and throughput), availability, modifiability.

## Features

### Current features

* has functionality to return list of all car adverts:
```
curl http://ec2-52-57-155-217.eu-central-1.compute.amazonaws.com:9000/adverts
```

* optional sorting by any field specified by query parameter:
```
    curl http://ec2-52-57-155-217.eu-central-1.compute.amazonaws.com:9000/adverts?sortBy=id
    curl http://ec2-52-57-155-217.eu-central-1.compute.amazonaws.com:9000/adverts?sortBy=title
    curl http://ec2-52-57-155-217.eu-central-1.compute.amazonaws.com:9000/adverts?sortBy=price
    curl http://ec2-52-57-155-217.eu-central-1.compute.amazonaws.com:9000/adverts?sortBy=mileage
    curl http://ec2-52-57-155-217.eu-central-1.compute.amazonaws.com:9000/adverts?sortBy=fuel
    curl http://ec2-52-57-155-217.eu-central-1.compute.amazonaws.com:9000/adverts?sortBy=firstRegistration
```

* has functionality to return data for single car advert by id:
```
curl http://ec2-52-57-155-217.eu-central-1.compute.amazonaws.com:9000/adverts/1
```

* if advert not exist 404 status code will be returned:
```
curl -v http://ec2-52-57-155-217.eu-central-1.compute.amazonaws.com:9000/adverts/2
```

* has functionality to add car advert:
```
curl -v -X POST http://ec2-52-57-155-217.eu-central-1.compute.amazonaws.com:9000/adverts -H 'content-type: application/json' -d '{"id":33,"title":"Opel Astra","fuel":"diesel","price":25000,"isNew":true}'
```

* has functionality to modify car advert by id:
```
curl -v -X PUT http://ec2-52-57-155-217.eu-central-1.compute.amazonaws.com:9000/adverts/33 -H 'content-type: application/json' -d '{"id":33,"title":"Opel Astra","fuel":"diesel","price":24999,"isNew":true}'
```

* has functionality to delete car advert by id:
```
curl -v -X DELETE http://ec2-52-57-155-217.eu-central-1.compute.amazonaws.com:9000/adverts/33
```

* has validation
* accepts and returns data in ISO 8601 UTC
* data is stored in the DynamoDB table

### Possible future features
* Pagination for the GET /adverts endpoint. Would be nice to introduce optional parameters: page size and page number
* Descending order sorting for the GET /adverts endpoint
* Local caching for the GET requests 
* [HATEOS](https://en.wikipedia.org/wiki/HATEOAS) 
* Clean the codebase
    * Implement Unit of work pattern to support transactions
    * Impletemt the rest of tests
    * Configure Sonarqube and run the analysis of the codebase

## How to run locally

1. Clone the repository.
2. Install latest versions of JDK and SBT
3. Create a file with AWS credentials as described [here](http://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html).
Please contact me to get the values for "aws_access_key_id" and "aws_secret_access_key".
4. Open the directory with the application
5. Run:
```
sbt run
```
