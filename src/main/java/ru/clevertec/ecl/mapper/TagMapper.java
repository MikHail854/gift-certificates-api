package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.entty.Tag;

@Mapper(componentModel = "spring")
public interface TagMapper {

    TagDTO toTagDTO(Tag tag);

    Tag toTag(TagDTO tagDTO);

}
