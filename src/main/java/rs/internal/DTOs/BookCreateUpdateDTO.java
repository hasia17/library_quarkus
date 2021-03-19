package rs.internal.DTOs;

import domain.models.Category;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BookCreateUpdateDTO {

    @NotNull
    private String title;

    @NotNull
    private String isbn;

    private int pages;

    @NotNull
    private String authorID;

    private Category category;
}
