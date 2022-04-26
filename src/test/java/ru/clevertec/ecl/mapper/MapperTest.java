package ru.clevertec.ecl.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.clevertec.ecl.dto.GiftCertificateDTO;
import ru.clevertec.ecl.dto.GiftCertificateFilter;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.entty.GiftCertificate;
import ru.clevertec.ecl.entty.Tag;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class MapperTest {

    Mapper mapper = Mappers.getMapper(Mapper.class);

    @Test
    public void testTagToTagDTOWithoutGiftCertificate() {
        final TagDTO tagDTOFromMapper = mapper.tagToTagDTO(createTagObject());
        TagDTO tagDTO = createTagDTOObject();
        assertEquals(tagDTO, tagDTOFromMapper);
    }

    @Test
    public void testTagToTagDTOWithGiftCertificate() {
        Tag tag = createTagObject();
        tag.setGiftCertificates(new ArrayList<GiftCertificate>() {{
            add(createGiftCertificateObject());
        }});

        final TagDTO tagDTOFromMapper = mapper.tagToTagDTO(tag);
        TagDTO tagDTO = createTagDTOObject();

        assertEquals(tagDTO, tagDTOFromMapper);
    }

    @Test
    public void testTagDTOToTagWithoutGiftCertificate() {
        final Tag tagFromMapper = mapper.tagDTOToTag(createTagDTOObject());
        Tag tag = createTagObject();
        assertEquals(tag, tagFromMapper);
    }

    @Test
    public void testTagDTOToTagWithGiftCertificate() {
        TagDTO tagDTO = createTagDTOObject();
        tagDTO.setGiftCertificates(new ArrayList<GiftCertificateDTO>() {{
            add(createGiftCertificateDTOObject());
        }});

        final Tag tagFromMapper = mapper.tagDTOToTag(tagDTO);
        Tag tag = createTagObject();

        assertEquals(tag, tagFromMapper);
    }


    @Test
    public void testGiftCertificateToGiftCertificateDTOWithoutTag() {
        final GiftCertificateDTO giftCertificateDTOFromMapper = mapper.giftCertificateToGiftCertificateDTO(createGiftCertificateObject());
        final GiftCertificateDTO giftCertificateDTO = createGiftCertificateDTOObject();
        assertEquals(giftCertificateDTO, giftCertificateDTOFromMapper);
    }

    @Test
    public void testGiftCertificateToGiftCertificateDTOWithTag() {
        final GiftCertificate giftCertificate = createGiftCertificateObject();
        giftCertificate.setTags(new ArrayList<Tag>() {{
            add(createTagObject());
        }});

        final GiftCertificateDTO giftCertificateDTOFromMapper = mapper.giftCertificateToGiftCertificateDTO(giftCertificate);

        final GiftCertificateDTO giftCertificateDTO = createGiftCertificateDTOObject();
        giftCertificateDTO.setTags(new ArrayList<TagDTO>() {{
            add(createTagDTOObject());
        }});

        assertEquals(giftCertificateDTO, giftCertificateDTOFromMapper);
    }

    @Test
    public void testGiftCertificateDTOToGiftCertificateWithoutTag() {
        final GiftCertificate giftCertificateFromMapper = mapper.giftCertificateDTOToGiftCertificate(createGiftCertificateDTOObject());
        final GiftCertificate giftCertificate = createGiftCertificateObject();
        assertEquals(giftCertificate, giftCertificateFromMapper);
    }

    @Test
    public void testGiftCertificateDTOToGiftCertificateWithTag() {
        final GiftCertificateDTO giftCertificateDTO = createGiftCertificateDTOObject();
        giftCertificateDTO.setTags(new ArrayList<TagDTO>() {{
            add(createTagDTOObject());
        }});

        final GiftCertificate giftCertificateFromMapper = mapper.giftCertificateDTOToGiftCertificate(giftCertificateDTO);

        final GiftCertificate giftCertificate = createGiftCertificateObject();
        giftCertificate.setTags(new ArrayList<Tag>() {{
            add(createTagObject());
        }});

        assertEquals(giftCertificate, giftCertificateFromMapper);
    }


    @Test
    public void testTagToTagDTOWithGiftCertificateTwoWayCommunication() {
        Tag tag = createTagObject();
        tag.setGiftCertificates(new ArrayList<GiftCertificate>() {{
            add(createGiftCertificateObject());
        }});

        final TagDTO tagDTOFromMapper = mapper.tagToTagDTO(tag);
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

        final Tag tagFromMapper = mapper.tagDTOToTag(tagDTO);
        Tag tag = createTagObject();
        tag.setGiftCertificates(new ArrayList<GiftCertificate>() {{
            add(createGiftCertificateObject());
        }});

        assertNotEquals(tag, tagFromMapper);
    }

    @Test
    public void testGiftCertificateFilterToGiftCertificate() {
        final GiftCertificateFilter giftCertificateFilter = createGiftCertificateFilterObject();
        final GiftCertificate giftCertificateFromMapper = mapper.giftCertificateFilterToGiftCertificate(giftCertificateFilter);

        GiftCertificate giftCertificate = GiftCertificate.builder()
                .name("na")
                .description("script")
                .build();

        assertEquals(giftCertificate, giftCertificateFromMapper);
    }


    private Tag createTagObject() {
        return Tag.builder()
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

    private TagDTO createTagDTOObject() {
        return TagDTO.builder()
                .id(1)
                .name("someNameTag")
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

    private GiftCertificateFilter createGiftCertificateFilterObject() {
        return GiftCertificateFilter.builder()
                .name("na")
                .description("script")
                .build();
    }

}
