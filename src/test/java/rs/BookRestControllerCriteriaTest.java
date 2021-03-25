package rs;


import domain.models.Category;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tkit.quarkus.rs.models.PageResultDTO;
import rs.internal.DTOs.AuthorCreateUpdateDTO;
import rs.internal.DTOs.BookCreateUpdateDTO;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class BookRestControllerCriteriaTest {

    @Test
    @DisplayName("Gets all books")
    public void shouldFindAllBooksTest() {

        ExtractableResponse response = given()
                .contentType(MediaType.APPLICATION_JSON)
                .get("/books")
                .prettyPeek()
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract();

        PageResultDTO pageResultDTO = response.as(PageResultDTO.class);
        assertEquals(100, pageResultDTO.getSize());
        assertEquals(0, pageResultDTO.getNumber());
    }

    @Test
    @DisplayName("Find books by title")
    public void shouldFindBooksByTitle() throws BookCreateUpdateDTO.InvalidArgumentException, AuthorCreateUpdateDTO.InvalidArgumentException {
        BookCreateUpdateDTO createdBook = createBook("ISBN_title");

        ExtractableResponse response = (ExtractableResponse) given()
                .when()
                .queryParam("title", "Title")
                .get("/books");

        PageResultDTO<BookCreateUpdateDTO> result1 = response.as(getBookCreateUpdateDtoTypeRef());
        BookCreateUpdateDTO result = result1.getStream().get(0);
        assertEquals(createdBook.getTitle(), result.getTitle());
        assertEquals(createdBook.getIsbn(), result.getIsbn());
        assertEquals(createdBook.getPages(), result.getPages());
        assertEquals(createdBook.getCategory(), result.getCategory());
    }

    @Test
    @DisplayName("Find books by ISBN")
    public void shouldFindBooksByISBN() throws BookCreateUpdateDTO.InvalidArgumentException, AuthorCreateUpdateDTO.InvalidArgumentException {
        BookCreateUpdateDTO createdBook = createBook("ISBN_isbn");

        ExtractableResponse response = (ExtractableResponse) given()
                .when()
                .queryParam("isbn", "ISBN_isbn")
                .get("/books");

        PageResultDTO<BookCreateUpdateDTO> result1 = response.as(getBookCreateUpdateDtoTypeRef());
        BookCreateUpdateDTO result = result1.getStream().get(0);
        assertEquals(createdBook.getTitle(), result.getTitle());
        assertEquals(createdBook.getIsbn(), result.getIsbn());
        assertEquals(createdBook.getPages(), result.getPages());
        assertEquals(createdBook.getCategory(), result.getCategory());
        //assertEquals(createdBook.getAuthorID(), result.getAuthorID());
    }

//    @Test
//    @DisplayName("Find books by category")
//    public void shouldFindBooksByCategory() throws BookCreateUpdateDTO.InvalidArgumentException, AuthorCreateUpdateDTO.InvalidArgumentException {
//        BookCreateUpdateDTO createdBook = createBook("ISBN_category");
//
//        ExtractableResponse response = (ExtractableResponse) given()
//                .when()
//                .queryParam("category", "COMEDY")
//                .get("/books");
//
//        PageResultDTO<BookCreateUpdateDTO> result1 = response.as(getBookCreateUpdateDtoTypeRef());
//        BookCreateUpdateDTO result = result1.getStream().get(0);
//        assertEquals(createdBook.getTitle(), result.getTitle());
//        assertEquals(createdBook.getIsbn(), result.getIsbn());
//        assertEquals(createdBook.getPages(), result.getPages());
//        assertEquals(createdBook.getCategory(), result.getCategory());
//        //assertEquals(createdBook.getAuthorID(), result.getAuthorID());
//    }


    private TypeRef<PageResultDTO<BookCreateUpdateDTO>> getBookCreateUpdateDtoTypeRef() {
        return new TypeRef<>() {
        };
    }

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

    private BookCreateUpdateDTO createBook(String isbn) throws BookCreateUpdateDTO.InvalidArgumentException, AuthorCreateUpdateDTO.InvalidArgumentException {
        String authorID = getAuthorID();

        BookCreateUpdateDTO bookCreateUpdateDTO = new BookCreateUpdateDTO();
        bookCreateUpdateDTO.setTitle("Title");
        bookCreateUpdateDTO.setPages(15);
        bookCreateUpdateDTO.setIsbn(isbn);
        bookCreateUpdateDTO.setCategory(Category.valueOf("COMEDY"));
        bookCreateUpdateDTO.setAuthorID(authorID);

        ExtractableResponse response = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(bookCreateUpdateDTO)
                .post("/books")
                .prettyPeek()
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .extract();

        return bookCreateUpdateDTO;
    }
}