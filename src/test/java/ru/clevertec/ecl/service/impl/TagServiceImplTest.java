package ru.clevertec.ecl.service.impl;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.ecl.dao.TagRepository;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.entty.Tag;
import ru.clevertec.ecl.mapper.Mapper;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TagServiceImplTest {

    @Mock
    private TagRepository tagRepository;
    @Mock
    private Mapper mapper;
    @InjectMocks
    private TagServiceImpl tagService;


    @Test
    public void testFindById() {
        final Tag tag = createTagObject();
        tag.setId(1);
        final TagDTO tagDTO = createTagDTOObject();
        tagDTO.setId(1);

        doReturn(Optional.of(tag)).when(tagRepository).findById(tag.getId());
        doReturn(tagDTO).when(mapper).tagToTagDTO(tag);

        assertEquals(tagDTO, tagService.findById(tag.getId()));
    }


    @Test
    public void testFindByIdThrowsEntityNotFoundException() {
        doReturn(Optional.empty()).when(tagRepository).findById(1);

        assertThrows(EntityNotFoundException.class, () -> tagService.findById(1));
    }

    @Test
    public void testSaveNewTagWithoutGiftCertificate() {
        final TagDTO tagDTO = createTagDTOObject();
        final Tag tag = createTagObject();

        when(mapper.tagToTagDTO(tagRepository.save(tag))).thenReturn(tagDTO);

        assertEquals(tagDTO, tagService.save(tag));
    }

    @Test
    public void testUpdate() {
        final TagDTO tagDTO = createTagDTOObject();
        final Tag tag = createTagObject();
        tag.setId(1);

        when(tagRepository.findById(1)).thenReturn(Optional.of(tag));
        when(mapper.tagToTagDTO(tag)).thenReturn(tagDTO);

        assertEquals(tagDTO, tagService.update(1, tagDTO));
    }

    @Test
    public void testUpdateThrowsEntityNotFoundException() {
        final TagDTO tagDTO = createTagDTOObject();
        when(tagRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> tagService.update(1, tagDTO));
    }

    @Test
    public void testDelete() {
        assertDoesNotThrow(() -> tagService.delete(1));
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
