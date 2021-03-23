package Domain.DAOs;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ExtractableResponse;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import rs.internal.DTOs.AuthorCreateUpdateDTO;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class AuthorRestControllerUpdateTest {

    private String getAuthorID(AuthorCreateUpdateDTO authorCreateUpdateDTO) throws AuthorCreateUpdateDTO.InvalidArgumentException {

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

        JSONObject jo = new JSONObject(response.asString());
        String id = jo.getString("id");
        return id;
    }

    private AuthorCreateUpdateDTO getUpdatedAuthorCreateUpdateDTO() throws AuthorCreateUpdateDTO.InvalidArgumentException {
        AuthorCreateUpdateDTO authorCreateUpdateDTO = new AuthorCreateUpdateDTO();
        authorCreateUpdateDTO.setName("UpdatedName");
        authorCreateUpdateDTO.setLastName("UpdatedSurname");
        authorCreateUpdateDTO.setAge(50);
        return authorCreateUpdateDTO;
    }

    @Test
    public void shouldUpdateAuthorByID() throws AuthorCreateUpdateDTO.InvalidArgumentException {
        AuthorCreateUpdateDTO authorCreateUpdateDTO = new AuthorCreateUpdateDTO();
        String id = getAuthorID(authorCreateUpdateDTO);

        AuthorCreateUpdateDTO updatedAuthor = getUpdatedAuthorCreateUpdateDTO();

        AuthorCreateUpdateDTO result = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(updatedAuthor)
                .put("/authors/" + id)
                .prettyPeek()
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().body().as(AuthorCreateUpdateDTO.class);

        Assertions.assertEquals(updatedAuthor.getName(), result.getName());
        Assertions.assertEquals(updatedAuthor.getLastName(), result.getLastName());
        Assertions.assertEquals(updatedAuthor.getAge(), result.getAge());
    }

    @Test
    public void shouldNotUpdateAuthorByWrongID() throws AuthorCreateUpdateDTO.InvalidArgumentException {
        AuthorCreateUpdateDTO authorCreateUpdateDTO = new AuthorCreateUpdateDTO();
        String id = getAuthorID(authorCreateUpdateDTO);

        AuthorCreateUpdateDTO updatedAuthor = getUpdatedAuthorCreateUpdateDTO();

        ExtractableResponse response = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(updatedAuthor)
                .put("/authors/" + "123123-wrongID")
                .prettyPeek()
                .then()
                .extract();

        Integer statusCode = response.statusCode();
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), statusCode);

    }
}
