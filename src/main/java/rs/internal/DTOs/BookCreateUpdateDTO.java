package rs.internal.DTOs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import domain.models.Book;
import domain.models.Category;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookCreateUpdateDTO {

    @NotNull
    private String title;

    @NotNull
    private String isbn;

    private int pages;

    @NotNull
    private String authorID;

    private Category category;

    public void setPages(Integer pages) throws BookCreateUpdateDTO.InvalidArgumentException {
        this.validatePages(pages);
        this.pages = pages;
    }

    private void validatePages(Integer pages) throws BookCreateUpdateDTO.InvalidArgumentException {
        if(pages < 0){
            throw new BookCreateUpdateDTO.InvalidArgumentException("Number of pages must be bigger than 0!");
        }
    }

    public class InvalidArgumentException extends Exception {
        private InvalidArgumentException(String message){
            super(message);
        }
    }
}
