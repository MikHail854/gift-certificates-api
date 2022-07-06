package ru.clevertec.ecl.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.entty.Tag;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TagMapperTest {

    TagMapper tagMapper = Mappers.getMapper(TagMapper.class);

    @Test
    public void testTagToTagDTOWithoutGiftCertificate() {
        final TagDTO tagDTOFromMapper = tagMapper.toTagDTO(createTagObject());
        TagDTO tagDTO = createTagDTOObject();
        assertEquals(tagDTO, tagDTOFromMapper);
    }

    @Test
    public void testTagToTagDTOWithGiftCertificate() {
        Tag tag = createTagObject();
        final TagDTO tagDTOFromMapper = tagMapper.toTagDTO(tag);
        TagDTO tagDTO = createTagDTOObject();
        assertEquals(tagDTO, tagDTOFromMapper);
    }

    @Test
    public void testTagDTOToTagWithoutGiftCertificate() {
        final Tag tagFromMapper = tagMapper.toTag(createTagDTOObject());
        Tag tag = createTagObject();
        assertEquals(tag, tagFromMapper);
    }

    @Test
    public void testTagDTOToTagWithGiftCertificate() {
        TagDTO tagDTO = createTagDTOObject();
        final Tag tagFromMapper = tagMapper.toTag(tagDTO);
        Tag tag = createTagObject();

        assertEquals(tag, tagFromMapper);
    }

    private Tag createTagObject() {
        return Tag.builder()
                .id(1)
                .name("someNameTag")
                .build();
    }

    private TagDTO createTagDTOObject() {
        return TagDTO.builder()
                .id(1)
                .name("someNameTag")
                .build();
    }

}
