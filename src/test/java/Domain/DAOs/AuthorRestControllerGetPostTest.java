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
public class AuthorRestControllerGetPostTest {

    @Test
    public void shouldCreateAuthorCreateUpdateDTO() throws AuthorCreateUpdateDTO.InvalidArgumentException {
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

        Integer statusCode = response.statusCode();

        assertEquals(Response.Status.CREATED.getStatusCode(), statusCode);
        JSONObject jo = new JSONObject(response.asString());
        String id = jo.getString("id");

        AuthorCreateUpdateDTO result = given()
                .contentType(MediaType.APPLICATION_JSON)
                .get("/authors/" + id)
                .prettyPeek()
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().body().as(AuthorCreateUpdateDTO.class);

        Assertions.assertEquals(authorCreateUpdateDTO.getName(), result.getName());
        Assertions.assertEquals(authorCreateUpdateDTO.getLastName(), result.getLastName());
        Assertions.assertEquals(authorCreateUpdateDTO.getAge(), result.getAge());
    }

    @Test
    public void shouldThrowExceptionOnWrongAge() {
        AuthorCreateUpdateDTO authorCreateUpdateDTO = new AuthorCreateUpdateDTO();
        authorCreateUpdateDTO.setName("Name");
        authorCreateUpdateDTO.setLastName("Surname");

        Assertions.assertThrows(AuthorCreateUpdateDTO.InvalidArgumentException.class, () -> {
            authorCreateUpdateDTO.setAge(-1);
        });
    }
}