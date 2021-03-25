package domain.DAOs;

import domain.models.Author;
import domain.models.AuthorSearchCriteria;
import domain.models.Book;
import domain.models.BookSearchCriteria;
import org.tkit.quarkus.jpa.daos.AbstractDAO;
import org.tkit.quarkus.jpa.daos.Page;
import org.tkit.quarkus.jpa.daos.PageResult;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class BookDAO extends AbstractDAO<Book>{

    public PageResult<Book> searchByCriteria(BookSearchCriteria criteria) {
        if (criteria == null) {
            return null;
        }
        CriteriaQuery<Book> query = criteriaQuery();
        Root<Book> root = query.from(Book.class);
        List<Predicate> predicates = new ArrayList<>();
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();

        if(criteria.getTitle() != null && !criteria.getTitle().isEmpty()) {
            predicates.add(builder.like(root.get("title"), criteria.getTitle() + "%"));
        }

        if(criteria.getIsbn() != null && !criteria.getIsbn().isEmpty()) {
            predicates.add(builder.like(root.get("isbn"), criteria.getIsbn() + "%"));
        }

        if(criteria.getPages() != null) {
            predicates.add(builder.equal(root.get("pages"), criteria.getPages()));
        }

        if(criteria.getCategory() != null) {
            predicates.add(builder.like(root.get("category"), criteria.getCategory() + "%"));
        }

//        if(criteria.getCategory() != null) {
//            predicates.add(builder.like(root.get("author").get("name"), criteria.getCategory() + "%"));
//        }

        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(new Predicate[0]));
        }

        return createPageQuery(query, Page.of(criteria.getPageNumber(), criteria.getPageSize())).getPageResult();

    }

}
