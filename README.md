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

Tests

With more tests, would have elaborated on null values in received data, dirty data