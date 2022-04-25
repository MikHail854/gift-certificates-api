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
import static org.mockito.Mockito.doReturn;
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
    GiftCertificateServiceImpl giftCertificateService;


    @Test
    public void testFindById() {
        final GiftCertificate giftCertificate = createGiftCertificateObject();
        giftCertificate.setId(1);
        final GiftCertificateDTO giftCertificateDTO = createGiftCertificateDTOObject();
        giftCertificateDTO.setId(1);

        doReturn(Optional.of(giftCertificate)).when(giftCertificateRepository).findById(giftCertificate.getId());
        doReturn(giftCertificateDTO).when(mapper).giftCertificateToGiftCertificateDTO(giftCertificate);

        assertEquals(giftCertificateDTO, giftCertificateService.findById(giftCertificate.getId()));
    }

    @Test
    public void testFindByIdThrowsEntityNotFoundException() {
        doReturn(Optional.empty()).when(giftCertificateRepository).findById(1);

        assertThrows(EntityNotFoundException.class, () -> giftCertificateService.findById(1));
    }


    @Test
    public void testFindGiftCertificateByTagName() {
        String tagName = "tagName";
        final Tag tag = createTagObject();
        final TagDTO tagDTO = createTagDTOObject();

        when(tagRepository.findTagByName(tagName)).thenReturn(Optional.of(tag));
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
        when(tagRepository.findTagByName(null)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> giftCertificateService.findGiftCertificateByTagName(null));
    }

    @Test
    public void testFindGiftCertificateByTagNameWithIllegalArgument() {
        String tagName = "123";
        when(tagRepository.findTagByName(tagName)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> giftCertificateService.findGiftCertificateByTagName(tagName));
    }

    @Test
    public void testFindGiftCertificateByDescription() {
        String description = "description";
        final GiftCertificate giftCertificate = createGiftCertificateObject();
        List<GiftCertificate> giftCertificates = new ArrayList<GiftCertificate>() {{
            add(giftCertificate);
        }};

        final GiftCertificateDTO giftCertificateDTO = createGiftCertificateDTOObject();

        List<GiftCertificateDTO> giftCertificatesDTO = new ArrayList<GiftCertificateDTO>() {{
            add(giftCertificateDTO);
        }};

        when(giftCertificateRepository.findByDescription(description)).thenReturn(giftCertificates);
        when(mapper.giftCertificateToGiftCertificateDTO(giftCertificate)).thenReturn(giftCertificateDTO);

        assertEquals(giftCertificatesDTO, giftCertificateService.findGiftCertificateByDescription(description));
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

    @Test
    public void testSaveNewGiftCertificateWithTag() {
        int id = 1;
        final GiftCertificate giftCertificate = createGiftCertificateObject();
        giftCertificate.setId(id);
        giftCertificate.setTags(new ArrayList<Tag>() {{
            add(createTagObject());
        }});

        when(giftCertificateRepository.findById(id)).thenReturn(Optional.of(giftCertificate));

        final GiftCertificate giftCertificateFromDB = createGiftCertificateObject();
        giftCertificateFromDB.setId(id);

        final GiftCertificateDTO giftCertificateDTO = createGiftCertificateDTOObject();

        when(giftCertificateRepository.save(giftCertificate)).thenReturn(giftCertificateFromDB);
        when(mapper.giftCertificateToGiftCertificateDTO(giftCertificateFromDB)).thenReturn(giftCertificateDTO);

        assertEquals(giftCertificateDTO, giftCertificateService.save(giftCertificate));
    }

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
