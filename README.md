Usage

Navigate to project root directory after cloning and issue the following command:

./gradlew bootRun

Then hit the endpoint of the API with the following format:

http://localhost:8080/movies?api={apiName}&searchTerm={searchString}

where apiName = ["omdb","moviedb"]

where searchString is a string of characters

Example

http://localhost:8080/movies?searchTerm=the golden years&api=omdb

http://localhost:8080/movies?searchTerm=man&api=omdb

Assumptions

Only the first page of movies resulting from the initial free text search are processed.

Unit tests are not present for the following reasons:

1. There is very little business logic specific to this microservice. Any tests that would be written would more likely test java syntax, spring functionality, or another end point.
    
    a) In the controller MoviesController, there is log statement, switch statement, and a error being thrown. A given test would make sure the correct api is called based on the request param, or that an error is thrown. Considering there are only 3 cases, I didn't feel a test was warranted. Tests need to be maintained over time by multiple people, and if a given test at some point is deemed to be not worthwhile, it's maintenance would cease.
    
    b) In the services is where the limited logic to this microservice is present. While the service is specifically only returning the title, year, and director for the given movie, it is simply extracting this data from the current version of the API. The API version is meant to be static and should not change. If I wrote a that injected mock data assuming the API wouldn't change, I would be testing my ability to extract a given field from a json response.   
    
2. The majority of the logic visible in the services is tied to the response from the enclosing service, which would have it's own unit tests. If and when those were to change/break - it would result in the relevant http error code being thrown from our service.


