package domain.models;


import lombok.Getter;
import lombok.Setter;
import org.tkit.quarkus.jpa.models.TraceableEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name="AUTHOR")
public class Author extends TraceableEntity {

    @Column(name="NAME", nullable = false)
    private String name;

    @Column(name="LAST_NAME", nullable = false)
    private String lastName;

    @Column(name="AGE")
    private Integer age;

}
