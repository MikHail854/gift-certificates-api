package ru.clevertec.ecl.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import ru.clevertec.ecl.dao.GiftCertificateRepository;
import ru.clevertec.ecl.dao.TagRepository;
import ru.clevertec.ecl.dto.*;
import ru.clevertec.ecl.entty.GiftCertificate;
import ru.clevertec.ecl.entty.Tag;
import ru.clevertec.ecl.mapper.GiftCertificateMapper;
import ru.clevertec.ecl.mapper.TagMapper;

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
    private GiftCertificateMapper giftCertificateMapper;

    @Mock
    private TagMapper tagMapper;

    @InjectMocks
    private GiftCertificateServiceImpl giftCertificateService;

    @Test
    public void testFindAll() {
        final GiftCertificate giftCertificate = createGiftCertificateObject();
        final GiftCertificateDTO giftCertificateDTO = createGiftCertificateDTOObject();
        ArrayList<GiftCertificate> list = new ArrayList<>();
        list.add(giftCertificate);
        list.add(giftCertificate);
        list.add(giftCertificate);

        final Pageable pageable = getPageable(5, 0, Sort.by("name").ascending());
        Page<GiftCertificate> returnPage = new PageImpl<>(list, pageable, list.size());

        GiftCertificateFilter filter = GiftCertificateFilter.builder()
                .name("some")
                .build();

        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withMatcher("name", match -> match.contains().ignoreCase())
                .withMatcher("description", match -> match.contains().ignoreCase());

        when(giftCertificateMapper.toGiftCertificateDTO(giftCertificate)).thenReturn(giftCertificateDTO);
        when(giftCertificateRepository.findAll(Example.of(giftCertificate, matcher), pageable)).thenReturn(returnPage);
        when(giftCertificateMapper.toGiftCertificate(filter)).thenReturn(giftCertificate);

        assertEquals(3, giftCertificateService.findAll(filter, pageable).getTotalElements());
    }

    @Test
    public void testFindById() {
        final GiftCertificate giftCertificate = createGiftCertificateObject();
        giftCertificate.setId(1);
        final GiftCertificateDTO giftCertificateDTO = createGiftCertificateDTOObject();
        giftCertificateDTO.setId(1);

        when(giftCertificateRepository.findById(giftCertificate.getId())).thenReturn(Optional.of(giftCertificate));
        when(giftCertificateMapper.toGiftCertificateDTO(giftCertificate)).thenReturn(giftCertificateDTO);

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
        when(tagMapper.toTagDTO(tag)).thenReturn(tagDTO);


        final GiftCertificate giftCertificate = createGiftCertificateObject();
        List<GiftCertificate> giftCertificates = new ArrayList<GiftCertificate>() {{
            add(giftCertificate);
        }};

        final GiftCertificateDTO giftCertificateDTO = createGiftCertificateDTOObject();
        List<GiftCertificateDTO> giftCertificatesDTO = new ArrayList<GiftCertificateDTO>() {{
            add(giftCertificateDTO);
        }};

        when(giftCertificateRepository.findByTagsName(tagName)).thenReturn(giftCertificates);
        when(giftCertificateMapper.toGiftCertificateDTO(giftCertificate)).thenReturn(giftCertificateDTO);

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
        when(giftCertificateMapper.toGiftCertificateDTO(giftCertificateFromDB)).thenReturn(giftCertificateDTO);

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
//        when(giftCertificateMapper.toGiftCertificate(giftCertificateDTOFromDB)).thenReturn(giftCertificateFromDB);
////
//        when(giftCertificateService.findById(inputGiftCertificate.getId())).thenReturn(giftCertificateDTOFromDB);
//        when(giftCertificateMapper.toGiftCertificate(giftCertificateDTOFromDB)).thenReturn(giftCertificateFromDB);//
//
//        when(giftCertificateRepository.save(inputGiftCertificate)).thenReturn(inputGiftCertificate);
//
//        when(giftCertificateMapper.toGiftCertificateDTO(inputGiftCertificate)).thenReturn(giftCertificateDTOFromDB);
//
//        assertEquals(2, giftCertificateService.save(inputGiftCertificate).getTags().size());
//    }

    @Test
    public void testUpdate() {
        int id = 1;
        final GiftCertificate giftCertificate = createGiftCertificateObject();
        final GiftCertificateDTO giftCertificateDTO = createGiftCertificateDTOObject();

        when(giftCertificateRepository.findById(id)).thenReturn(Optional.of(giftCertificate));
        when(giftCertificateRepository.saveAndFlush(giftCertificate)).thenReturn(giftCertificate);
        when(giftCertificateMapper.toGiftCertificateDTO(giftCertificate)).thenReturn(giftCertificateDTO);

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

    @Test
    public void testUpdatePrice() {
        int id = 1;
        final GiftCertificate giftCertificate = createGiftCertificateObject();
        final GiftCertificateDTO giftCertificateDTO = createGiftCertificateDTOObject();

        when(giftCertificateRepository.findById(id)).thenReturn(Optional.of(giftCertificate));
        when(giftCertificateRepository.saveAndFlush(giftCertificate)).thenReturn(giftCertificate);
        when(giftCertificateMapper.toGiftCertificateDTO(giftCertificate)).thenReturn(giftCertificateDTO);

        GiftCertificatePriceDTO price = GiftCertificatePriceDTO.builder()
                .price(123.45f)
                .build();
        giftCertificateDTO.setPrice(123.45f);

        assertEquals(giftCertificateDTO.getPrice(), giftCertificateService.updatePrice(id, price).getPrice());
    }

    @Test
    public void testUpdatePriceThrowsEntityNotFoundException() {
        int id = 1;
        when(giftCertificateRepository.findById(id)).thenReturn(Optional.empty());

        GiftCertificatePriceDTO price = GiftCertificatePriceDTO.builder()
                .price(123.45f)
                .build();

        assertThrows(EntityNotFoundException.class, () -> giftCertificateService.updatePrice(id, price));
    }

    @Test
    public void testUpdateDuration() {
        int id = 1;
        final GiftCertificate giftCertificate = createGiftCertificateObject();
        final GiftCertificateDTO giftCertificateDTO = createGiftCertificateDTOObject();

        when(giftCertificateRepository.findById(id)).thenReturn(Optional.of(giftCertificate));
        when(giftCertificateRepository.saveAndFlush(giftCertificate)).thenReturn(giftCertificate);
        when(giftCertificateMapper.toGiftCertificateDTO(giftCertificate)).thenReturn(giftCertificateDTO);

        GiftCertificateDurationDTO duration = GiftCertificateDurationDTO.builder()
                .duration(123)
                .build();
        giftCertificateDTO.setDuration(123);

        assertEquals(giftCertificateDTO.getDuration(), giftCertificateService.updateDuration(id, duration).getDuration());
    }

    @Test
    public void testUpdateDurationThrowsEntityNotFoundException() {
        int id = 1;
        when(giftCertificateRepository.findById(id)).thenReturn(Optional.empty());

        GiftCertificateDurationDTO duration = GiftCertificateDurationDTO.builder()
                .duration(123)
                .build();

        assertThrows(EntityNotFoundException.class, () -> giftCertificateService.updateDuration(id, duration));
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

    private Pageable getPageable(int size, int page, Sort sort) {
        return new Pageable() {
            @Override
            public int getPageNumber() {
                return page;
            }

            @Override
            public int getPageSize() {
                return size;
            }

            @Override
            public long getOffset() {
                return 0;
            }

            @Override
            public Sort getSort() {
                return sort;
            }

            @Override
            public Pageable next() {
                return null;
            }

            @Override
            public Pageable previousOrFirst() {
                return null;
            }

            @Override
            public Pageable first() {
                return null;
            }

            @Override
            public Pageable withPage(int pageNumber) {
                return null;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }
        };
    }


}
