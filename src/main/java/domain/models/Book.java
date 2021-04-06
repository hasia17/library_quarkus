package domain.models;

import lombok.Getter;
import lombok.Setter;
import org.tkit.quarkus.jpa.models.TraceableEntity;

import javax.persistence.*;


@Getter
@Setter
@Entity
@Table(name="BOOK")
public class Book extends TraceableEntity {

    @Column(name="TITLE", nullable = false)
    private String title;

    @Column(name="ISBN", nullable = false, unique = true)
    private String isbn;

    @Column(name="PAGES")
    private Integer pages;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="AUTHOR_GUID", nullable = false)
    private Author author;

    @Column(name="CATEGORY")
    @Enumerated(EnumType.STRING)
    private Category category;

}
