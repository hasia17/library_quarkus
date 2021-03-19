package rs.internal.mappers;

import domain.models.Author;
import domain.models.AuthorSearchCriteria;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.tkit.quarkus.jpa.daos.PageResult;
import org.tkit.quarkus.rs.mappers.OffsetDateTimeMapper;
import org.tkit.quarkus.rs.models.PageResultDTO;
import rs.internal.DTOs.AuthorCreateUpdateDTO;
import rs.internal.DTOs.AuthorDTO;
import rs.internal.DTOs.AuthorSearchCriteriaDTO;

import java.time.OffsetDateTime;


@Mapper(componentModel = "cdi", uses = OffsetDateTimeMapper.class)
public interface AuthorMapper {

    AuthorDTO map(Author model);

    Author map(AuthorCreateUpdateDTO dto);

    AuthorSearchCriteria map(AuthorSearchCriteriaDTO dto);

    PageResultDTO<AuthorDTO> map(PageResult<Author> page);

}
