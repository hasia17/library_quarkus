package domain.DAOs;

import domain.models.Author;
import org.tkit.quarkus.jpa.daos.AbstractDAO;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AuthorDAO extends AbstractDAO<Author> {
}
