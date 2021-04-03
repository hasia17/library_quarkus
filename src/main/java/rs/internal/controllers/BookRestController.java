package rs.internal.controllers;

import domain.DAOs.AuthorDAO;
import domain.DAOs.BookDAO;
import domain.models.Author;
import domain.models.Book;
import domain.models.BookSearchCriteria;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.tkit.quarkus.jpa.daos.PageResult;
import org.tkit.quarkus.rs.models.PageResultDTO;
import rs.internal.DTOs.AuthorDTO;
import rs.internal.DTOs.BookCreateUpdateDTO;
import rs.internal.DTOs.BookDTO;
import rs.internal.DTOs.BookSearchCriteriaDTO;
import rs.internal.mappers.AuthorMapper;
import rs.internal.mappers.BookMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Objects;

@ApplicationScoped
@Path("books")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BookRestController {

    @Inject
    BookDAO bookDAO;

    @Inject
    AuthorDAO authorDAO;

    @Inject
    BookMapper mapper;

    @Inject
    AuthorMapper authorMapper;

    @GET
    @Path("{id}")
    @Operation(operationId = "getBookById", description = "Gets book by ID")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Ok",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = BookDTO.class))),
            @APIResponse(responseCode = "404", description = "Not Found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON)),
            @APIResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON)),
    })
    public Response getBookByID(@PathParam("id") String id) {
        Book book = bookDAO.findById(id);
        return Response.ok(mapper.map(book)).build();
    }

    @POST
    @Transactional
    @Operation(operationId = "createBook", description = "Create book")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Created",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = BookDTO.class)),
                    headers = {@Header(name = "Location", description = "URL for the create author resource")}),
            @APIResponse(responseCode = "404", description = "Not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON)),
            @APIResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON)),
    })
    public Response createBook(@Valid BookCreateUpdateDTO bookDTO)  {

        Book book = mapper.map(bookDTO);
        book.setAuthor(authorDAO.findById(bookDTO.getAuthorID()));
        return Response.ok(mapper.map(bookDAO.create(book))).status(201).build();
    }

    @PUT
    @Transactional
    @Path("{id}")
    @Operation(operationId = "updateBook", description = "Update the book")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Updated book resource",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = BookDTO.class))),
            @APIResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON)),
            @APIResponse(responseCode = "404", description = "Not Found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON)),
            @APIResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON)),
    })
    public Response updateBook(@PathParam("id") String id, @Valid BookCreateUpdateDTO bookDTO) {
        Book book = bookDAO.findById(id);
        if(Objects.nonNull(book)) {
            book.setTitle(bookDTO.getTitle());
            book.setCategory(bookDTO.getCategory());
            book.setIsbn(bookDTO.getIsbn());
            book.setPages(bookDTO.getPages());
            book.setAuthor(authorDAO.findById(bookDTO.getAuthorID()));
            bookDAO.update(book);
            return Response.ok(mapper.map(book)).build();
        }
        return Response.noContent().status(404).build();
    }

    @DELETE
    @Transactional
    @Path("{id}")
    @Operation(operationId = "deleteBook", description = "Delete the book")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Deleted book resource",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = BookDTO.class))),
            @APIResponse(responseCode = "404", description = "Not Found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON)),
            @APIResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON)),
    })
    public Response deleteBook(@PathParam("id") String id) {
        Book book = bookDAO.findById(id);
        if(Objects.nonNull(book)) {
            bookDAO.delete(book);
            return Response.ok(mapper.map(book)).build();
        }
        return Response.noContent().status(404).build();

    }

    @GET
    @Operation(operationId = "getBooksByCriteria", description = "Gets books by criteria")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "The corresponding books resources",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = PageResultDTO.class))),
            @APIResponse(responseCode = "500", description = "Internal Server Error, please check Problem Details",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON)),
    })
    public Response getBooksByCriteria(@BeanParam BookSearchCriteriaDTO dto) {
        BookSearchCriteria criteria = mapper.map(dto);
        PageResult<Book> books = bookDAO.searchByCriteria(criteria);
        return Response.ok(mapper.map(books)).build();
    }

//    private void setAuthor(@Valid BookCreateUpdateDTO bookDTO, Book book){
//        if (bookDTO.getAuthor().getId()!= null) {
//            Author author = authorDAO.findById(bookDTO.getAuthor().getId());
//            if(Objects.isNull(author)) {
//                author = authorDAO.create(authorMapper.map(bookDTO.getAuthor()));
//            }
//            book.setAuthor(author);
//        }else{
//            book.setAuthor(authorDAO.create(authorMapper.map(bookDTO.getAuthor())));
//        }
//    }

}
