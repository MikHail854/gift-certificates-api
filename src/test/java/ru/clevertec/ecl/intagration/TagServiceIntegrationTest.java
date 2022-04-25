package ru.clevertec.ecl.intagration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.TestConstructor;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.entty.Tag;
import ru.clevertec.ecl.service.TagService;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
public class TagServiceIntegrationTest implements BaseIntegrationTest {

    private final TagService tagService;

    @Test
    @DisplayName("the tag will be returned if a tag with this id exists in the database")
    public void testFindById() {
        assertEquals("tag1", tagService.findById(1).getName());
    }

    @Test
    public void testFindByIdThrowsEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> tagService.findById(9999));
    }

    @Test
    @DisplayName("the tag will be found if it is saved in the table")
    public void testSaveNewTagWithoutGiftCertificate() {
        Tag tag = Tag.builder()
                .name("someName")
                .build();
        final TagDTO saved = tagService.save(tag);
        assertDoesNotThrow(() -> tagService.findById(saved.getId()));
    }

    @Test
    @DisplayName("the tag will be updated if a tag with this id exists in the database")
    public void testUpdate() {
        TagDTO tagDTO = TagDTO.builder()
                .name("new name")
                .build();
        final TagDTO updated = tagService.update(1, tagDTO);
        final TagDTO byIdFromDB = tagService.findById(updated.getId());
        assertEquals("new name", byIdFromDB.getName());
    }

    @Test
    @DisplayName("updating a tag whose ID is not in the database throws an Entity Not Found Exception")
    public void testUpdateThrowsEntityNotFoundException() {
        TagDTO tagDTO = TagDTO.builder()
                .name("someName")
                .build();
        assertThrows(EntityNotFoundException.class, () -> tagService.update(999, tagDTO));
    }

    @Test
    public void testUpdateWithNegativeIdThrowsEntityNotFoundException() {
        TagDTO tagDTO = TagDTO.builder()
                .name("someName")
                .build();
        assertThrows(EntityNotFoundException.class, () -> tagService.update(-999, tagDTO));
    }

    @Test
    @DisplayName("the tag will be deleted if a tag with this id exists in the database")
    public void testDelete() {
        assertDoesNotThrow(() -> tagService.delete(1));
    }

    @Test
    public void testDeleteThrowsEmptyResultDataAccessException() {
        assertThrows(EmptyResultDataAccessException.class, () -> tagService.delete(100));
    }

    @Test
    public void testFindAll(){
        assertDoesNotThrow(tagService::findAll);
    }

}
