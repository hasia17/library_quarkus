package rs.internal.DTOs;


import domain.models.Category;
import lombok.Getter;
import lombok.Setter;
import org.tkit.quarkus.rs.models.TraceableDTO;

import java.util.List;

@Getter
@Setter
public class BookDTO extends TraceableDTO {

    private String isbn;
    private String title;
    private int pages;
    private Category category;
    private AuthorDTO authorDTO;



}
