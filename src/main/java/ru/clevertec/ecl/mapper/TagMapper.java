package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.entty.Tag;

@Mapper(componentModel = "spring")
public interface TagMapper {

    @Mapping(target = "giftCertificates", ignore = true)
    TagDTO toTagDTO(Tag tag);

    @Mapping(target = "giftCertificates", ignore = true)
    Tag toTag(TagDTO tagDTO);

}
