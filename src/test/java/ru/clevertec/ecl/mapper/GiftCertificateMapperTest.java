package ru.clevertec.ecl.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.test.util.ReflectionTestUtils;
import ru.clevertec.ecl.dto.GiftCertificateDTO;
import ru.clevertec.ecl.dto.GiftCertificateFilter;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.entty.GiftCertificate;
import ru.clevertec.ecl.entty.Tag;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GiftCertificateMapperTest {

    GiftCertificateMapper giftCertificateMapper = Mappers.getMapper(GiftCertificateMapper.class);

    @Test
    public void testGiftCertificateToGiftCertificateDTOWithoutTag() {
        final GiftCertificateDTO giftCertificateDTOFromMapper = giftCertificateMapper.toGiftCertificateDTO(createGiftCertificateObject());
        final GiftCertificateDTO giftCertificateDTO = createGiftCertificateDTOObject();
        assertEquals(giftCertificateDTO, giftCertificateDTOFromMapper);
    }

    @Test
    public void testGiftCertificateToGiftCertificateDTOWithTag() {
        TagMapper tagMapper = Mappers.getMapper(TagMapper.class);
        ReflectionTestUtils.setField(giftCertificateMapper, "tagMapper", tagMapper);

        final GiftCertificate giftCertificate = createGiftCertificateObject();
        giftCertificate.setTags(new ArrayList<Tag>() {{
            add(createTagObject());
        }});

        final GiftCertificateDTO giftCertificateDTOFromMapper = giftCertificateMapper.toGiftCertificateDTO(giftCertificate);

        final GiftCertificateDTO giftCertificateDTO = createGiftCertificateDTOObject();
        giftCertificateDTO.setTags(new ArrayList<TagDTO>() {{
            add(createTagDTOObject());
        }});

        assertEquals(giftCertificateDTO, giftCertificateDTOFromMapper);
    }

    @Test
    public void testGiftCertificateDTOToGiftCertificateWithoutTag() {
        final GiftCertificate giftCertificateFromMapper = giftCertificateMapper.toGiftCertificate(createGiftCertificateDTOObject());
        final GiftCertificate giftCertificate = createGiftCertificateObject();
        assertEquals(giftCertificate, giftCertificateFromMapper);
    }

    @Test
    public void testGiftCertificateDTOToGiftCertificateWithTag() {
        TagMapper tagMapper = Mappers.getMapper(TagMapper.class);
        ReflectionTestUtils.setField(giftCertificateMapper, "tagMapper", tagMapper);

        final GiftCertificateDTO giftCertificateDTO = createGiftCertificateDTOObject();
        giftCertificateDTO.setTags(new ArrayList<TagDTO>() {{
            add(createTagDTOObject());
        }});

        final GiftCertificate giftCertificateFromMapper = giftCertificateMapper.toGiftCertificate(giftCertificateDTO);

        final GiftCertificate giftCertificate = createGiftCertificateObject();
        giftCertificate.setTags(new ArrayList<Tag>() {{
            add(createTagObject());
        }});

        assertEquals(giftCertificate, giftCertificateFromMapper);
    }

    @Test
    public void testGiftCertificateFilterToGiftCertificate() {
        final GiftCertificateFilter giftCertificateFilter = createGiftCertificateFilterObject();
        final GiftCertificate giftCertificateFromMapper = giftCertificateMapper.toGiftCertificate(giftCertificateFilter);

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

    private GiftCertificateFilter createGiftCertificateFilterObject() {
        return GiftCertificateFilter.builder()
                .name("na")
                .description("script")
                .build();
    }

}
