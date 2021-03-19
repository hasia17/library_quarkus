package rs.internal.DTOs;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AuthorCreateUpdateDTO {

    @NotNull
    private String name;

    @NotNull
    private String lastName;

    private String age;
}
