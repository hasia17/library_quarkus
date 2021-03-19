package rs.internal.DTOs;

import lombok.Getter;
import lombok.Setter;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

@Getter
@Setter
public class AuthorSearchCriteriaDTO {
    @QueryParam("name")
    private String name;

    @QueryParam("lastName")
    private String lastName;

    @QueryParam("age")
    private String age;

    @QueryParam("page")
    @DefaultValue("0")
    private int pageNumber = 0;

    @QueryParam("size")
    @DefaultValue("100")
    private int pageSize = 100;
}
