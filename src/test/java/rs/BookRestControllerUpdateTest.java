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
public class BookRestControllerUpdateTest {

    private String getAuthorID() throws AuthorCreateUpdateDTO.InvalidArgumentException {

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
        return authorID;
    }

    private String getBookID(BookCreateUpdateDTO bookCreateUpdateDTO, String isbn) throws BookCreateUpdateDTO.InvalidArgumentException, AuthorCreateUpdateDTO.InvalidArgumentException {

        AuthorCreateUpdateDTO authorCreateUpdateDTO = new AuthorCreateUpdateDTO();
        String authorID = getAuthorID();

        bookCreateUpdateDTO.setTitle("Title");
        bookCreateUpdateDTO.setIsbn(isbn);
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

        System.out.println(response.asString());
        JSONObject jo = new JSONObject(response.asString());
        String id = jo.getString("id");
        System.out.println(id);
        return id;
    }

    private BookCreateUpdateDTO getUpdatedBookCreateUpdateDTO() throws BookCreateUpdateDTO.InvalidArgumentException, AuthorCreateUpdateDTO.InvalidArgumentException {
        AuthorCreateUpdateDTO authorCreateUpdateDTO = new AuthorCreateUpdateDTO();
        String authorID = getAuthorID();

        BookCreateUpdateDTO bookCreateUpdateDTO = new BookCreateUpdateDTO();
        bookCreateUpdateDTO.setTitle("UpdatedTitle");
        bookCreateUpdateDTO.setPages(15);
        bookCreateUpdateDTO.setIsbn("UpdatedISBN");
        bookCreateUpdateDTO.setCategory(Category.COMEDY);
        bookCreateUpdateDTO.setAuthorID(authorID);
        return bookCreateUpdateDTO;
    }

    @Test
    public void shouldUpdateBookByID() throws BookCreateUpdateDTO.InvalidArgumentException, AuthorCreateUpdateDTO.InvalidArgumentException {
        BookCreateUpdateDTO bookCreateUpdateDTO = new BookCreateUpdateDTO();
        String id = getBookID(bookCreateUpdateDTO, "ISBN1");

        BookCreateUpdateDTO updatedBook = getUpdatedBookCreateUpdateDTO();

        BookCreateUpdateDTO result = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(updatedBook)
                .put("/books/" + id)
                .prettyPeek()
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().body().as(BookCreateUpdateDTO.class);

        Assertions.assertEquals(updatedBook.getTitle(), result.getTitle());
        Assertions.assertEquals(updatedBook.getPages(), result.getPages());
        Assertions.assertEquals(updatedBook.getIsbn(), result.getIsbn());
        Assertions.assertEquals(updatedBook.getCategory(), result.getCategory());
    }

    @Test
    public void shouldNotUpdateBookByWrongID() throws BookCreateUpdateDTO.InvalidArgumentException, AuthorCreateUpdateDTO.InvalidArgumentException {
        BookCreateUpdateDTO bookCreateUpdateDTO = new BookCreateUpdateDTO();
        String id = getBookID(bookCreateUpdateDTO, "ISBN2");

        BookCreateUpdateDTO updatedBook = getUpdatedBookCreateUpdateDTO();

        ExtractableResponse response = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(updatedBook)
                .put("/books/" + "123123-wrongID")
                .prettyPeek()
                .then()
                .extract();

        Integer statusCode = response.statusCode();
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), statusCode);

    }
}
