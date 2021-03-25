package domain.models;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
public class BookSearchCriteria {

    private String title;

    private String isbn;

    private Integer pages;

    private Category category;

    private String authorID;

    private Integer pageNumber;

    private Integer pageSize;
}
