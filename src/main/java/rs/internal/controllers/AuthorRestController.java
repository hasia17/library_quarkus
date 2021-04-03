package rs.internal.controllers;

import domain.DAOs.AuthorDAO;

import domain.models.Author;

import domain.models.AuthorSearchCriteria;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.tkit.quarkus.jpa.daos.PageResult;
import org.tkit.quarkus.rs.models.PageResultDTO;
import rs.internal.DTOs.AuthorCreateUpdateDTO;

import rs.internal.DTOs.AuthorDTO;
import rs.internal.DTOs.AuthorSearchCriteriaDTO;
import rs.internal.mappers.AuthorMapper;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Objects;

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
    @Operation(operationId = "getAuthorById", description = "Gets author by ID")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Ok",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = AuthorDTO.class))),
            @APIResponse(responseCode = "404", description = "Not Found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON)),
            @APIResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON)),
    })
    public Response getAuthorByID(@PathParam("id") String id) {
        Author author = authorDAO.findById(id);
        return Response.ok(mapper.map(author)).build();
    }

    @POST
    @Transactional
    @Operation(operationId = "createAuthor", description = "Create author")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Created",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = AuthorDTO.class)),
                    headers = {@Header(name = "Location", description = "URL for the create author resource")}),
                    @APIResponse(responseCode = "404", description = "Not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON)),
            @APIResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON)),
    })
    public Response createAuthor(@Valid AuthorCreateUpdateDTO authorDTO) {

        Author author = mapper.map(authorDTO);
        return Response.ok(mapper.map(authorDAO.create(author))).status(201).build();
    }

    @PUT
    @Transactional
    @Path("{id}")
    @Operation(operationId = "updateAuthor", description = "Update the author")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Updated author resource",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = AuthorDTO.class))),
            @APIResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON)),
            @APIResponse(responseCode = "404", description = "Not Found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON)),
            @APIResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON)),
    })
    public Response updateAuthor(@PathParam("id") String id, @Valid AuthorCreateUpdateDTO authorDTO) {
        Author author = authorDAO.findById(id);
        if(Objects.nonNull(author)) {
            author.setLastName(authorDTO.getLastName());
            author.setName(authorDTO.getName());
            author.setAge(authorDTO.getAge());
            authorDAO.update(author);
            return Response.ok(mapper.map(author)).build();
        }
        return Response.noContent().status(404).build();
    }

    @DELETE
    @Transactional
    @Path("{id}")
    @Operation(operationId = "deleteAuthor", description = "Delete the author")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Deleted author resource",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = AuthorDTO.class))),
            @APIResponse(responseCode = "404", description = "Not Found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON)),
            @APIResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON)),
    })
    public Response deleteAuthor(@PathParam("id") String id) {
        Author author = authorDAO.findById(id);
        if(Objects.nonNull(author)) {
            authorDAO.delete(author);
            return Response.ok(mapper.map(author)).build();
        }
        return Response.noContent().status(404).build();
    }

    @GET
    @Operation(operationId = "getAuthorsByCriteria", description = "Gets authors by criteria")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "The corresponding authors resources",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = PageResultDTO.class))),
            @APIResponse(responseCode = "500", description = "Internal Server Error, please check Problem Details",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON)),
    })
    public Response getAuthorsByCriteria(@BeanParam AuthorSearchCriteriaDTO dto) {
        AuthorSearchCriteria criteria = mapper.map(dto);
        PageResult<Author> authors = authorDAO.searchByCriteria(criteria);
        return Response.ok(mapper.map(authors)).build();
    }


}
