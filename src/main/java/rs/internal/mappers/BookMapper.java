package rs.internal.mappers;

import domain.models.Book;
import org.mapstruct.Mapper;
import org.tkit.quarkus.rs.mappers.OffsetDateTimeMapper;
import rs.internal.DTOs.BookCreateUpdateDTO;
import rs.internal.DTOs.BookDTO;


@Mapper(componentModel = "cdi", uses = OffsetDateTimeMapper.class)
public interface BookMapper {

    BookDTO map(Book model);

    Book map(BookCreateUpdateDTO dto);


}
