# Tenpo-challenge
BE challenge!
## Requirement and Design Decisions (In spanish)
[Challenge.pdf](docs%2FChallenge.pdf)

## How to Run docker containers
`docker-compose --profile dev up`

If you want to run only the infra: db and cache for run this command:  
`docker-compose up`
It is useful to debug svc-challenge 

## Swagger:
http://localhost:8080/api/swagger-ui/index.html

## Postman collection:
[Challenge.postman_collection.json](docs%2FChallenge.postman_collection.json)

### Public docker hub

https://hub.docker.com/repository/docker/lsecotaro/svc-challenge

### If you need to build this service container
`docker build --tag=lsecotaro/svc-challenge:v1 .`

### Compile service
`mvn clean install`
