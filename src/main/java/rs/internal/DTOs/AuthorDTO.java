package rs.internal.DTOs;


import lombok.Getter;
import lombok.Setter;
import org.tkit.quarkus.rs.models.TraceableDTO;

@Getter
@Setter
public class AuthorDTO extends TraceableDTO {

    private String name;
    private String lastName;
    private Integer age;
}
