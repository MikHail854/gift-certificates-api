package ru.clevertec.ecl.intagration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestConstructor;
import ru.clevertec.ecl.dto.GiftCertificateDTO;
import ru.clevertec.ecl.dto.GiftCertificateFilter;
import ru.clevertec.ecl.entty.GiftCertificate;
import ru.clevertec.ecl.entty.Tag;
import ru.clevertec.ecl.service.GiftCertificateService;

import javax.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
public class GiftCertificateServiceIntegrationTest implements BaseIntegrationTest {

    private final GiftCertificateService giftCertificateService;

    @Test
    @DisplayName("the gift certificate will be returned if a gift certificate with this id exists in the database")
    public void testFindById() {
        assertEquals("name1", giftCertificateService.findById(1).getName());
    }

    @Test
    public void testFindByIdThrowsEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> giftCertificateService.findById(9999));
    }

    @Test
    @DisplayName("a gift certificate will be returned if a tag with that name exists in the database and is linked to this gift certificate")
    public void testFindGiftCertificateByTagName() {
        final List<GiftCertificateDTO> giftCertificateByTagName = giftCertificateService.findGiftCertificateByTagName("tag1");
        assertAll(
                () -> assertEquals(2, giftCertificateByTagName.size()),
                () -> assertEquals(1, giftCertificateByTagName.get(0).getId()),
                () -> assertEquals(3, giftCertificateByTagName.get(1).getId())
        );
    }

    @Test
    public void testFindGiftCertificateByTagNameThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> giftCertificateService.findGiftCertificateByTagName("someTagName"));
    }

    @Test
    public void testFindAllWithPageSizeMoreThanContentAndSearchByPartOfNameAndAscendingSortByName() {
        GiftCertificateFilter filter = GiftCertificateFilter.builder()
                .name("na")
                .build();

        final Pageable pageable = getPageable(5, 0, Sort.by("name").ascending());
        final List<GiftCertificateDTO> content = giftCertificateService.findAll(filter, pageable).getContent();
        assertAll(
                () -> assertEquals(3, content.size()),
                () -> assertEquals("name1", content.get(0).getName()),
                () -> assertEquals("name3", content.get(content.size() - 1).getName())
        );
    }

    @Test
    public void testFindAllWithPageSizeSmallThanContentAndSearchByPartOfNameAndAscendingSortByName() {
        GiftCertificateFilter filter = GiftCertificateFilter.builder()
                .name("na")
                .build();

        final Pageable pageable = getPageable(1, 0, Sort.by("name").ascending());
        final List<GiftCertificateDTO> content = giftCertificateService.findAll(filter, pageable).getContent();
        assertAll(
                () -> assertEquals(1, content.size()),
                () -> assertEquals("name1", content.get(0).getName())
        );
    }

    @Test
    public void testFindAllWithPageSizeMoreThanContentAndSearchByPartOfDescriptionAndDescendingSortByName() {

        GiftCertificateFilter filter = GiftCertificateFilter.builder()
                .description("scr")
                .build();

        final Pageable pageable = getPageable(5, 0, Sort.by("name").descending());
        final List<GiftCertificateDTO> content = giftCertificateService.findAll(filter, pageable).getContent();
        assertAll(
                () -> assertEquals(3, content.size()),
                () -> assertEquals("name3", content.get(0).getName()),
                () -> assertEquals("name1", content.get(content.size() - 1).getName())
        );
    }

        @Test
    @DisplayName("the gift certificate without tag will be found if it is saved in the table")
    public void testSaveNewGiftCertificateWithoutTag() {
            final GiftCertificate giftCertificate = createGiftCertificateObject();
            final GiftCertificateDTO saved = giftCertificateService.save(giftCertificate);
        assertDoesNotThrow(() -> giftCertificateService.findById(saved.getId()));
    }

    @Test
    @DisplayName("the gift certificate with tag will be found if it is saved in the table")
    public void testSaveNewGiftCertificateWithTag() {
        final GiftCertificate giftCertificate = createGiftCertificateObject();
        giftCertificate.setId(100);
        giftCertificate.setTags(new ArrayList<Tag>() {{
            add(Tag.builder()
                    .name("someNameTag")
                    .build());
        }});
        final GiftCertificateDTO saved = giftCertificateService.save(giftCertificate);
        assertAll(
                () -> assertDoesNotThrow(() -> giftCertificateService.findById(saved.getId())),
                () -> assertEquals("someNameTag", giftCertificateService.findById(saved.getId()).getTags().get(0).getName())
        );
    }

    @Test
    @DisplayName("the gift certificate will be updated if a tag with this id exists in the database")
    public void testUpdate() {
        final GiftCertificateDTO giftCertificateDTO = createGiftCertificateDTOObject();
        final GiftCertificateDTO updated = giftCertificateService.update(1, giftCertificateDTO);
        final GiftCertificateDTO byIdFromDB = giftCertificateService.findById(updated.getId());
        assertEquals("someNameCertificateDTO", byIdFromDB.getName());
    }

    @Test
    @DisplayName("updating a gift certificate whose ID is not in the database throws an Entity Not Found Exception")
    public void testUpdateThrowsEntityNotFoundException() {
        final GiftCertificateDTO giftCertificateDTO = createGiftCertificateDTOObject();
        assertThrows(EntityNotFoundException.class, () -> giftCertificateService.update(999, giftCertificateDTO));
    }

    @Test
    public void testUpdateWithNegativeIdThrowsEntityNotFoundException() {
        final GiftCertificateDTO giftCertificateDTO = createGiftCertificateDTOObject();
        assertThrows(EntityNotFoundException.class, () -> giftCertificateService.update(-999, giftCertificateDTO));
    }

    @Test
    @DisplayName("the gift certificate will be deleted if a tag with this id exists in the database")
    public void testDelete() {
        assertDoesNotThrow(() -> giftCertificateService.delete(1));
    }

    @Test
    public void testDeleteThrowsEmptyResultDataAccessException() {
        assertThrows(EmptyResultDataAccessException.class, () -> giftCertificateService.delete(100));
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
                .name("someNameCertificateDTO")
                .description("someDescriptionDTO")
                .price(1.2f)
                .duration(3)
                .createDate(LocalDateTime.now())
                .lastUpdateDate(LocalDateTime.now())
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
