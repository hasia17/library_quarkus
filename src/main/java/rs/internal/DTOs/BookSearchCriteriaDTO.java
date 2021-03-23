package rs.internal.DTOs;

import domain.models.Author;
import domain.models.Category;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

@Getter
@Setter
public class BookSearchCriteriaDTO {

    @QueryParam("title")
    private String title;

    @QueryParam("isbn")
    private String isbn;

    @QueryParam("pages")
    private Integer pages;

    @QueryParam("category")
    private Category category;

    @QueryParam("page")
    @DefaultValue("0")
    private int pageNumber = 0;

    @QueryParam("size")
    @DefaultValue("100")
    private int pageSize = 100;
}
