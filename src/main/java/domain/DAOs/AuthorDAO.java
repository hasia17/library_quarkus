package domain.DAOs;

import domain.models.Author;
import domain.models.AuthorSearchCriteria;
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

import static org.tkit.quarkus.jpa.utils.QueryCriteriaUtil.wildcard;

@ApplicationScoped
public class AuthorDAO extends AbstractDAO<Author> {

    public PageResult<Author> searchByCriteria(AuthorSearchCriteria criteria) {
        if (criteria == null) {
            return null;
        }
        CriteriaQuery<Author> query = criteriaQuery();
        Root<Author> root = query.from(Author.class);
        List<Predicate> predicates = new ArrayList<>();
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();

        if(criteria.getName() != null && !criteria.getName().isEmpty()) {
            predicates.add(builder.like(root.get("name"), criteria.getName().toLowerCase() + "%"));
        }

        if(criteria.getLastName() != null && !criteria.getLastName().isEmpty()) {
            predicates.add(builder.like(root.get("lastName"), criteria.getLastName().toLowerCase() + "%"));
        }

        if(criteria.getAge() != null) {
            predicates.add(builder.like(root.get("age"), criteria.getAge()));
        }
        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(new Predicate[0]));
        }

        return createPageQuery(query, Page.of(criteria.getPageNumber(), criteria.getPageSize())).getPageResult();

    }




}
