# Car Adverts web service

REST-full web service written in Scala using Play Framework.

The application is deployed on AWS.

## Features

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

## Next steps
* Pagination
* Descending order for GET requests
* Caching
* [HATEOS](https://en.wikipedia.org/wiki/HATEOAS)
* Code cleaning
