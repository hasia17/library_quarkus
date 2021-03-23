package rs.internal.controllers;

import domain.DAOs.AuthorDAO;
import domain.DAOs.BookDAO;
import domain.models.Book;
import domain.models.BookSearchCriteria;
import org.tkit.quarkus.jpa.daos.PageResult;
import rs.internal.DTOs.BookCreateUpdateDTO;
import rs.internal.DTOs.BookSearchCriteriaDTO;
import rs.internal.mappers.BookMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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

    @GET
    @Path("{id}")
    public Response getBookByID(@PathParam("id") String id) {
        Book book = bookDAO.findById(id);
        return Response.ok(mapper.map(book)).build();
    }

    @POST
    @Transactional
    public Response createBook(@Valid BookCreateUpdateDTO bookDTO)  {

        Book book = mapper.map(bookDTO);
        book.setAuthor(authorDAO.findById(bookDTO.getAuthorID()));
        return Response.ok(mapper.map(bookDAO.create(book))).status(201).build();
    }

    @PUT
    @Transactional
    @Path("{id}")
    public Response updateBook(@PathParam("id") String id, @Valid BookCreateUpdateDTO bookDTO) {
        Book book = bookDAO.findById(id);
        book.setTitle(bookDTO.getTitle());
        book.setCategory(bookDTO.getCategory());
        book.setIsbn(bookDTO.getIsbn());
        book.setPages(bookDTO.getPages());
        book.setAuthor(authorDAO.findById(bookDTO.getAuthorID()));
        bookDAO.update(book);
        return Response.ok(mapper.map(book)).build();
    }

    @DELETE
    @Transactional
    @Path("{id}")
    public Response removeBook(@PathParam("id") String id) {
        Book book = bookDAO.findById(id);
        bookDAO.delete(book);
        return Response.ok(mapper.map(book)).build();
    }

    @GET
    public Response getBooks(@BeanParam BookSearchCriteriaDTO dto) {
        BookSearchCriteria criteria = mapper.map(dto);
        PageResult<Book> books = bookDAO.searchByCriteria(criteria);
        return Response.ok(mapper.map(books)).build();
    }

}
