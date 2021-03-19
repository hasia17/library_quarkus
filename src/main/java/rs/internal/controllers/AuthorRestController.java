package rs.internal.controllers;

import domain.DAOs.AuthorDAO;

import domain.models.Author;

import rs.internal.DTOs.AuthorCreateUpdateDTO;

import rs.internal.mappers.AuthorMapper;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("authors")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthorRestController {

    @Inject
    AuthorDAO authorDAO;

    @Inject
    AuthorMapper mapper;

    @GET
    @Path("{id}")
    public Response getAuthorByID(@PathParam("id") String id) {
        Author author = authorDAO.findById(id);
        return Response.ok(mapper.map(author)).build();
    }

    @POST
    @Transactional
    public Response createAuthor(@Valid AuthorCreateUpdateDTO authorDTO)  {

        Author author = mapper.map(authorDTO);
        return Response.ok(mapper.map(authorDAO.create(author))).build();
    }

    @PUT
    @Transactional
    @Path("{id}")
    public Response updateAuthor(@PathParam("id") String id, @Valid AuthorCreateUpdateDTO authorDTO) {
        Author author = authorDAO.findById(id);
        author.setLastName(authorDTO.getLastName());
        author.setName(authorDTO.getName());
        author.setAge(authorDTO.getAge());
        authorDAO.update(author);
        return Response.ok(mapper.map(author)).build();
    }

    @DELETE
    @Transactional
    @Path("{id}")
    public Response removeAuthor(@PathParam("id") String id) {
        Author author = authorDAO.findById(id);
        authorDAO.delete(author);
        return Response.ok(mapper.map(author)).build();
    }




}
