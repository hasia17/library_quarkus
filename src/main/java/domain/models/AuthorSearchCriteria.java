package domain.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class AuthorSearchCriteria {

    private String name;

    private String lastName;

    private String age;

    private Integer pageNumber;

    private Integer pageSize;
}
