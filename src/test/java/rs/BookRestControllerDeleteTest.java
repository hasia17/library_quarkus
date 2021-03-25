package rs;

import domain.models.Category;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ExtractableResponse;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import rs.internal.DTOs.AuthorCreateUpdateDTO;
import rs.internal.DTOs.BookCreateUpdateDTO;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class BookRestControllerDeleteTest {

    private String getBookID(BookCreateUpdateDTO bookCreateUpdateDTO) throws BookCreateUpdateDTO.InvalidArgumentException, AuthorCreateUpdateDTO.InvalidArgumentException {

        AuthorCreateUpdateDTO authorCreateUpdateDTO = new AuthorCreateUpdateDTO();
        authorCreateUpdateDTO.setName("Name");
        authorCreateUpdateDTO.setLastName("Surname");
        authorCreateUpdateDTO.setAge(30);

        ExtractableResponse response1 = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(authorCreateUpdateDTO)
                .post("/authors")
                .prettyPeek()
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .extract();

        JSONObject jo1 = new JSONObject(response1.asString());
        String authorID = jo1.getString("id");

        bookCreateUpdateDTO.setTitle("Title");
        bookCreateUpdateDTO.setIsbn("ISBN1");
        bookCreateUpdateDTO.setPages(63);
        bookCreateUpdateDTO.setCategory(Category.HORROR);
        bookCreateUpdateDTO.setAuthorID(authorID);


        ExtractableResponse response = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(bookCreateUpdateDTO)
                .post("/books")
                .prettyPeek()
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .extract();

        JSONObject jo = new JSONObject(response.asString());
        String id = jo.getString("id");
        return id;
    }

    @Test
    public void shouldDeleteBookByID() throws BookCreateUpdateDTO.InvalidArgumentException, AuthorCreateUpdateDTO.InvalidArgumentException {

        BookCreateUpdateDTO bookCreateUpdateDTO = new BookCreateUpdateDTO();
        String id = getBookID(bookCreateUpdateDTO);

        BookCreateUpdateDTO result = given()
                .contentType(MediaType.APPLICATION_JSON)
                .delete("/books/" + id)
                .prettyPeek()
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().body().as(BookCreateUpdateDTO.class);

        Assertions.assertEquals(bookCreateUpdateDTO.getTitle(), result.getTitle());
        Assertions.assertEquals(bookCreateUpdateDTO.getPages(), result.getPages());
        Assertions.assertEquals(bookCreateUpdateDTO.getIsbn(), result.getIsbn());
        Assertions.assertEquals(bookCreateUpdateDTO.getCategory(), result.getCategory());
    }

    @Test
    public void shouldNotDeleteBookByWrongID() throws BookCreateUpdateDTO.InvalidArgumentException {

        ExtractableResponse response = given()
                .contentType(MediaType.APPLICATION_JSON)
                .delete("/books/" + "123123123-wrongID")
                .prettyPeek()
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode())
                .extract();

        Integer statusCode = response.statusCode();
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), statusCode);
    }
}
