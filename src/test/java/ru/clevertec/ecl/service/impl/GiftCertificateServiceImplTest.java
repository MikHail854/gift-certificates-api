package ru.clevertec.ecl.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.ecl.dao.GiftCertificateRepository;
import ru.clevertec.ecl.dao.TagRepository;
import ru.clevertec.ecl.dto.GiftCertificateDTO;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.entty.GiftCertificate;
import ru.clevertec.ecl.entty.Tag;
import ru.clevertec.ecl.mapper.Mapper;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GiftCertificateServiceImplTest {

    @Mock
    private GiftCertificateRepository giftCertificateRepository;
    @Mock
    private TagRepository tagRepository;
    @Mock
    private Mapper mapper;
    @InjectMocks
    private GiftCertificateServiceImpl giftCertificateService;


    @Test
    public void testFindById() {
        final GiftCertificate giftCertificate = createGiftCertificateObject();
        giftCertificate.setId(1);
        final GiftCertificateDTO giftCertificateDTO = createGiftCertificateDTOObject();
        giftCertificateDTO.setId(1);

        when(giftCertificateRepository.findById(giftCertificate.getId())).thenReturn(Optional.of(giftCertificate));
        when(mapper.giftCertificateToGiftCertificateDTO(giftCertificate)).thenReturn(giftCertificateDTO);

        assertEquals(giftCertificateDTO, giftCertificateService.findById(giftCertificate.getId()));
    }

    @Test
    public void testFindByIdThrowsEntityNotFoundException() {
        when(giftCertificateRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> giftCertificateService.findById(1));
    }


    @Test
    public void testFindGiftCertificateByTagName() {
        String tagName = "tagName";
        final Tag tag = createTagObject();
        final TagDTO tagDTO = createTagDTOObject();

        when(tagRepository.findByNameIgnoreCase(tagName)).thenReturn(Optional.of(tag));
        when(mapper.tagToTagDTO(tag)).thenReturn(tagDTO);


        final GiftCertificate giftCertificate = createGiftCertificateObject();
        List<GiftCertificate> giftCertificates = new ArrayList<GiftCertificate>() {{
            add(giftCertificate);
        }};

        final GiftCertificateDTO giftCertificateDTO = createGiftCertificateDTOObject();
        List<GiftCertificateDTO> giftCertificatesDTO = new ArrayList<GiftCertificateDTO>() {{
            add(giftCertificateDTO);
        }};

        when(giftCertificateRepository.findByTagsName(tagName)).thenReturn(giftCertificates);
        when(mapper.giftCertificateToGiftCertificateDTO(giftCertificate)).thenReturn(giftCertificateDTO);

        assertEquals(giftCertificatesDTO, giftCertificateService.findGiftCertificateByTagName(tagName));
    }


    @Test
    public void testFindGiftCertificateByTagNameWithArgumentNull() {
        when(tagRepository.findByNameIgnoreCase(null)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> giftCertificateService.findGiftCertificateByTagName(null));
    }

    @Test
    public void testFindGiftCertificateByTagNameWithIllegalArgument() {
        String tagName = "123";
        when(tagRepository.findByNameIgnoreCase(tagName)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> giftCertificateService.findGiftCertificateByTagName(tagName));
    }

    @Test
    public void testSaveNewGiftCertificateWithoutTag() {
        int id = 1;
        final GiftCertificate giftCertificate = createGiftCertificateObject();
        giftCertificate.setId(id);

        when(giftCertificateRepository.findById(id)).thenReturn(Optional.empty());

        final GiftCertificate giftCertificateFromDB = createGiftCertificateObject();
        giftCertificateFromDB.setId(id);

        final GiftCertificateDTO giftCertificateDTO = createGiftCertificateDTOObject();

        when(giftCertificateRepository.save(giftCertificate)).thenReturn(giftCertificateFromDB);
        when(mapper.giftCertificateToGiftCertificateDTO(giftCertificateFromDB)).thenReturn(giftCertificateDTO);

        assertEquals(giftCertificateDTO, giftCertificateService.save(giftCertificate));
    }

//    @Test
//    public void testSaveNewGiftCertificateWithTag() {
//        int id = 1;
//        Tag tag = Tag.builder()
//                .id(1)
//                .name("name1")
//                .build();
//        final GiftCertificate inputGiftCertificate = createGiftCertificateObject();
//        inputGiftCertificate.setId(id);
//        inputGiftCertificate.setTags(new ArrayList<Tag>() {{
//            add(tag);
//        }});
//
////        when(giftCertificateRepository.findById(giftCertificate.getId()).isPresent()).thenReturn(true);
//        when(giftCertificateRepository.findById(inputGiftCertificate.getId())).thenReturn(Optional.of(inputGiftCertificate));
//
//        TagDTO tagDTO = TagDTO.builder()
//                .id(2)
//                .name("name2")
//                .build();
//        final GiftCertificateDTO giftCertificateDTOFromDB = createGiftCertificateDTOObject();
//        giftCertificateDTOFromDB.setId(id);
//        giftCertificateDTOFromDB.setTags(new ArrayList<TagDTO>(){{
//            add(tagDTO);
//        }});
//
//
//        final GiftCertificate giftCertificateFromDB = createGiftCertificateObject();
//        giftCertificateFromDB.setId(id);
//        giftCertificateFromDB.setTags(new ArrayList<Tag>() {{
//            add(tag);
//        }});
//
////        doReturn(giftCertificateDTO).when(giftCertificateService).findById(giftCertificate.getId());
//        when(giftCertificateRepository.findById(id)).thenReturn(Optional.of(giftCertificateFromDB));
//        when(mapper.giftCertificateDTOToGiftCertificate(giftCertificateDTOFromDB)).thenReturn(giftCertificateFromDB);
////
//        when(giftCertificateService.findById(inputGiftCertificate.getId())).thenReturn(giftCertificateDTOFromDB);
//        when(mapper.giftCertificateDTOToGiftCertificate(giftCertificateDTOFromDB)).thenReturn(giftCertificateFromDB);//
//
//        when(giftCertificateRepository.save(inputGiftCertificate)).thenReturn(inputGiftCertificate);
//
//        when(mapper.giftCertificateToGiftCertificateDTO(inputGiftCertificate)).thenReturn(giftCertificateDTOFromDB);
//
//        assertEquals(2, giftCertificateService.save(inputGiftCertificate).getTags().size());
//    }

    @Test
    public void testUpdate() {
        int id = 1;
        final GiftCertificate giftCertificate = createGiftCertificateObject();
        final GiftCertificateDTO giftCertificateDTO = createGiftCertificateDTOObject();

        when(giftCertificateRepository.findById(id)).thenReturn(Optional.of(giftCertificate));
        when(mapper.giftCertificateToGiftCertificateDTO(giftCertificate)).thenReturn(giftCertificateDTO);

        assertEquals(giftCertificateDTO, giftCertificateService.update(id, giftCertificateDTO));
    }

    @Test
    public void testUpdateThrowsEntityNotFoundException() {
        int id = 1;
        when(giftCertificateRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> giftCertificateService.update(id, createGiftCertificateDTOObject()));
    }

    @Test
    public void testDelete() {
        assertDoesNotThrow(() -> giftCertificateService.delete(1));
    }


        private GiftCertificate createGiftCertificateObject() {
        return GiftCertificate.builder()
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
                .name("someNameCertificate")
                .description("someDescription")
                .price(1.2f)
                .duration(3)
                .createDate(LocalDateTime.now())
                .lastUpdateDate(LocalDateTime.now())
                .build();
    }

    private Tag createTagObject() {
        return Tag.builder()
                .name("someNameTag")
                .build();
    }

    private TagDTO createTagDTOObject() {
        return TagDTO.builder()
                .name("someNameTag")
                .build();
    }


}
