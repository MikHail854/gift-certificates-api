package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.entty.Tag;

import java.util.List;

@Mapper
public interface TagMapper {

    TagDTO createTag(Tag tag);

    List<TagDTO> createTagList(List<Tag> tags);

}
