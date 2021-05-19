# Youtube API POC

[Swagger API](https://app.swaggerhub.com/apis/YouTubeClone/YouTube/v1)

## H2 database

- on first run two tables will be created. Users table will be populated with two tokens: 'secure1234' and 'token56789'
- for subsequent runs comment out 'spring.sql.init.schema-locations' property in application.properties
- to access H2 console open http://localhost:8082/, use credentials: admin/admin

## Usage

### Prerequisites
- Java 8 

### Build 
```
mvn clean install
```

### Run 
```
mvn spring-boot:run
```

## Endpoints

### POST http://localhost:8080/api/v1/videos

Headers:
- Authorization: secure1234 (or token56789)

Body:
- key: video_file
- value: video binary 

Response JSON will return video ID. Use it in following endpoints.

### GET http://localhost:8080/api/v1/videos/{video-id}/status

Path param:
- use video ID returned by POST request

Headers:
- Authorization: secure1234 (or token56789)

On first call status is "In progress", from second call status will be "Complete".

### GET http://localhost:8080/api/v1/videos/{video-id}

Path param:
- use video ID returned by POST request

If video status is "In progress", a 503 error is returned. From second call video will be streamed.
