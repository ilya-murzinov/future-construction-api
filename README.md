# future-construction-api

Service to fetch and process satellite data in order to turn the single-channel image data into standard RGB images that a human surveyor can look over.

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

By default data is taken from test resources.

To specify different path `data.path` parameter can be used: 

```
java -jar -Ddata.path=/path/to/data build/libs/future-construction-api-0.1.0-SNAPSHOT.jar 
```

```bash
curl -o /tmp/image.jpg -d \
'{"utmZone": 33, "latitudeBand": "U", "gridSquare": "UP", "date": "2018-08-04", "channelMap": "visible"}' \
-H 'Content-Type: application/json' localhost:8080/generate-images
```