package Domain.DAOs;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ExtractableResponse;
import org.json.JSONObject;
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
                .queryParam("Name","Name")
                .get("/authors");

        PageResultDTO<AuthorCreateUpdateDTO> pageResultDTO = response.as(PageResultDTO.class);
        assertEquals(1, pageResultDTO.getTotalElements());

//        JSONObject result = given()
//                .contentType(MediaType.APPLICATION_JSON)
//                .get("/authors")
//                .prettyPeek()
//                .then()
//                .statusCode(Response.Status.OK.getStatusCode())
//                .extract().body().as(JSONObject.class);
//
//        System.out.println(result);
//        JSONObject name = (JSONObject) result.get("stream");
//        System.out.println(name);
//        PageResultDTO<AuthorCreateUpdateDTO> p = response.body().
//
//        //assertEquals(createdAuthor.getName(), result.getName());

    }


}
