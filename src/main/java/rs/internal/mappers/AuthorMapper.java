package rs.internal.mappers;

import domain.models.Author;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.tkit.quarkus.rs.mappers.OffsetDateTimeMapper;
import rs.internal.DTOs.AuthorCreateUpdateDTO;
import rs.internal.DTOs.AuthorDTO;

import java.time.OffsetDateTime;


@Mapper(componentModel = "cdi", uses = OffsetDateTimeMapper.class)
public interface AuthorMapper {

    AuthorDTO map(Author model);

    Author map(AuthorCreateUpdateDTO dto);



}
