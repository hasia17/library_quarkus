package rs.internal.mappers;

import domain.models.Book;
import domain.models.BookSearchCriteria;
import org.mapstruct.Mapper;
import org.tkit.quarkus.jpa.daos.PageResult;
import org.tkit.quarkus.rs.mappers.OffsetDateTimeMapper;
import org.tkit.quarkus.rs.models.PageResultDTO;
import rs.internal.DTOs.*;


@Mapper(componentModel = "cdi", uses = OffsetDateTimeMapper.class)
public interface BookMapper {

    BookDTO map(Book model);

    Book map(BookCreateUpdateDTO dto);

    BookSearchCriteria map(BookSearchCriteriaDTO dto);

    PageResultDTO<BookDTO> map(PageResult<Book> page);


}
