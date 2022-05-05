package ru.clevertec.ecl.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.clevertec.ecl.dto.GiftCertificateDTO;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.entty.GiftCertificate;
import ru.clevertec.ecl.entty.Tag;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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
        tag.setGiftCertificates(new ArrayList<GiftCertificate>() {{
            add(createGiftCertificateObject());
        }});

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
        tagDTO.setGiftCertificates(new ArrayList<GiftCertificateDTO>() {{
            add(createGiftCertificateDTOObject());
        }});

        final Tag tagFromMapper = tagMapper.toTag(tagDTO);
        Tag tag = createTagObject();

        assertEquals(tag, tagFromMapper);
    }

    @Test
    public void testTagToTagDTOWithGiftCertificateTwoWayCommunication() {
        Tag tag = createTagObject();
        tag.setGiftCertificates(new ArrayList<GiftCertificate>() {{
            add(createGiftCertificateObject());
        }});

        final TagDTO tagDTOFromMapper = tagMapper.toTagDTO(tag);
        TagDTO tagDTO = createTagDTOObject();
        tagDTO.setGiftCertificates(new ArrayList<GiftCertificateDTO>() {{
            add(createGiftCertificateDTOObject());
        }});

        assertNotEquals(tagDTO, tagDTOFromMapper);
    }

    @Test
    public void testTagDTOToTagWithGiftCertificateTwoWayCommunication() {
        TagDTO tagDTO = createTagDTOObject();
        tagDTO.setGiftCertificates(new ArrayList<GiftCertificateDTO>() {{
            add(createGiftCertificateDTOObject());
        }});

        final Tag tagFromMapper = tagMapper.toTag(tagDTO);
        Tag tag = createTagObject();
        tag.setGiftCertificates(new ArrayList<GiftCertificate>() {{
            add(createGiftCertificateObject());
        }});

        assertNotEquals(tag, tagFromMapper);
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

    private GiftCertificate createGiftCertificateObject() {
        return GiftCertificate.builder()
                .id(1)
                .name("someNameCertificate")
                .description("someDescription")
                .price(1.2f)
                .duration(3)
                .createDate(LocalDateTime.now())
                .lastUpdateDate(LocalDateTime.now())
                .build();
    }

    private GiftCertificateDTO createGiftCertificateDTOObject() {
        return GiftCertificateDTO.builder()
                .id(1)
                .name("someNameCertificate")
                .description("someDescription")
                .price(1.2f)
                .duration(3)
                .createDate(LocalDateTime.now())
                .lastUpdateDate(LocalDateTime.now())
                .build();
    }
}
