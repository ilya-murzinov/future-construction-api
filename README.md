# future-construction-api

[![Build Status](https://travis-ci.org/ilya-murzinov/future-construction-api.svg?branch=master)](https://travis-ci.org/ilya-murzinov/future-construction-api)

Service to fetch and process satellite data in order to turn the single-channel 
image data into standard RGB images that a human surveyor can look over.

### Requirements:

Java 11

### Testing:

To run all tests (unit + integration)

```
./gradlew clean test
```

### Building:

```
./gradlew clean build
```

### Running

```
java -jar build/libs/future-construction-api-0.1.0-SNAPSHOT.jar
```

By default data is taken from test resources, it will only work when run from project 
root, as the path is relative.

To specify different path `data.path` parameter can be used: 

```
java -jar -Ddata.path=/path/to/data build/libs/future-construction-api-0.1.0-SNAPSHOT.jar 
```

### API

#### POST `/generate-images`

Generates image according to parameters passed in the request.

Example request:

```
{
  "utmZone": 33,
  "latitudeBand": "U",
  "gridSquare": "UP",
  "date": "2018-08-05",
  "channelMap": "visible|vegetation|waterVapor"
}
```

All parameters are required.

In case image can be generated, it will be returned in the response:

```bash
curl -o /tmp/image.jpg -d \
'{"utmZone": 33, "latitudeBand": "U", "gridSquare": "UP", "date": "2018-08-04", "channelMap": "visible"}' \
-H 'Content-Type: application/json' localhost:8080/generate-images
```

In case image can not be generated error message will be returned:

```bash
curl -o /tmp/image.jpg -d \
'{"utmZone": 33, "latitudeBand": "U", "gridSquare": "UP", "date": "2018-08-05", "channelMap": "visible"}' \
-H 'Content-Type: application/json' localhost:8080/generate-images
```

```bash
{
  "message": "Image can not be generated using specified parameters"
}
```

or

```bash
curl -o /tmp/image.jpg -d \
'{"utmZone": 33, "latitudeBand": "U", "gridSquare": "UP", "date": "2018-08-05", "channelMap": "visible1"}' \
-H 'Content-Type: application/json' localhost:8080/generate-images
```

```
{
  "timestamp": "2019-06-23T19:37:57.027+0000",
  "path": "/generate-images",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Invalid channelMap: visible1"
}
```