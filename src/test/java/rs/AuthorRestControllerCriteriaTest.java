package rs;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tkit.quarkus.rs.models.PageResultDTO;
import rs.internal.DTOs.AuthorCreateUpdateDTO;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;


@QuarkusTest
public class AuthorRestControllerCriteriaTest {



    private AuthorCreateUpdateDTO createAuthor() throws AuthorCreateUpdateDTO.InvalidArgumentException {
        AuthorCreateUpdateDTO authorCreateUpdateDTO = new AuthorCreateUpdateDTO();
        authorCreateUpdateDTO.setName("Name");
        authorCreateUpdateDTO.setLastName("Surname");
        authorCreateUpdateDTO.setAge(30);

        ExtractableResponse response = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(authorCreateUpdateDTO)
                .post("/authors")
                .prettyPeek()
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .extract();

        return authorCreateUpdateDTO;
    }



    @Test
    @DisplayName("Gets all authors")
    public void shouldFindAllAuthorsTest() {

        ExtractableResponse response = given()
                .contentType(MediaType.APPLICATION_JSON)
                .get("/authors")
                .prettyPeek()
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract();

        PageResultDTO pageResultDTO = response.as(PageResultDTO.class);
        assertEquals(100, pageResultDTO.getSize());
        assertEquals(0, pageResultDTO.getNumber());
    }

    @Test
    @DisplayName("Find authors by name")
    public void shouldFindAuthorsByName() throws AuthorCreateUpdateDTO.InvalidArgumentException {
        AuthorCreateUpdateDTO createdAuthor = createAuthor();

        ExtractableResponse response = (ExtractableResponse) given()
                .when()
                .queryParam("name","Name")
                .get("/authors");

        PageResultDTO<AuthorCreateUpdateDTO> result1 = response.as(getAuthorCreateUpdateDtoTypeRef());
        AuthorCreateUpdateDTO result = result1.getStream().get(0);
        assertEquals(createdAuthor.getName(), result.getName());
        assertEquals(createdAuthor.getLastName(), result.getLastName());
        assertEquals(createdAuthor.getAge(), result.getAge());
    }

    @Test
    @DisplayName("Find authors by wrong name")
    public void shouldNotFindAuthorsByName() throws AuthorCreateUpdateDTO.InvalidArgumentException {

        ExtractableResponse response = (ExtractableResponse) given()
                .when()
                .queryParam("name","WrongName")
                .get("/authors");

        PageResultDTO<AuthorCreateUpdateDTO> result1 = response.as(getAuthorCreateUpdateDtoTypeRef());
        assertEquals(0, result1.getTotalElements());
    }

    @Test
    @DisplayName("Find authors by name and last name")
    public void shouldFindAuthorByNameAndLastName() throws AuthorCreateUpdateDTO.InvalidArgumentException {
        AuthorCreateUpdateDTO createdAuthor = createAuthor();
        io.restassured.response.Response response = given()
                .when()
                .queryParam("name", "Name")
                .queryParam("lastName", "Surname")
                .get("/authors");
        response.then()
                .statusCode(200);

        PageResultDTO<AuthorCreateUpdateDTO> result1 = response.as(getAuthorCreateUpdateDtoTypeRef());
        AuthorCreateUpdateDTO result = result1.getStream().get(0);
        assertEquals(createdAuthor.getName(), result.getName());
        assertEquals(createdAuthor.getLastName(), result.getLastName());
        assertEquals(createdAuthor.getAge(), result.getAge());
    }

    @Test
    @DisplayName("Find authors by age")
    public void shouldFindAuthorsByAge() throws AuthorCreateUpdateDTO.InvalidArgumentException {

        AuthorCreateUpdateDTO createdAuthor = createAuthor();

        ExtractableResponse response = (ExtractableResponse) given()
                .when()
                .queryParam("age",30)
                .get("/authors");

        PageResultDTO<AuthorCreateUpdateDTO> result1 = response.as(getAuthorCreateUpdateDtoTypeRef());
        AuthorCreateUpdateDTO result = result1.getStream().get(0);
        assertEquals(createdAuthor.getName(), result.getName());
        assertEquals(createdAuthor.getLastName(), result.getLastName());
        assertEquals(createdAuthor.getAge(), result.getAge());
    }

    private TypeRef<PageResultDTO<AuthorCreateUpdateDTO>> getAuthorCreateUpdateDtoTypeRef() {
        return new TypeRef<>() {
        };
   }

}
