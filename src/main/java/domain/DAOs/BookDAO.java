package domain.DAOs;

import domain.models.Book;
import org.tkit.quarkus.jpa.daos.AbstractDAO;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BookDAO extends AbstractDAO<Book>{

}
