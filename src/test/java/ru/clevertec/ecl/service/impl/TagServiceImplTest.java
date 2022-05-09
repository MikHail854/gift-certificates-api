package ru.clevertec.ecl.service.impl;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.clevertec.ecl.dao.TagRepository;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.entty.Tag;
import ru.clevertec.ecl.mapper.TagMapper;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TagServiceImplTest {

    @Mock
    private TagRepository tagRepository;

    @Mock
    private TagMapper tagMapper;

    @InjectMocks
    private TagServiceImpl tagService;

    @Test
    public void testFindAll(){
        final Tag tag = createTagObject();
        final TagDTO tagDTO = createTagDTOObject();
        ArrayList<Tag> list = new ArrayList<>();
        list.add(tag);
        list.add(tag);
        list.add(tag);

        final Pageable pageable = getPageable(5, 0, Sort.by("name").ascending());
        Page<Tag> returnPage =  new PageImpl<>(list, pageable, list.size());

        when(tagRepository.findAll(pageable)).thenReturn(returnPage );
        when(tagMapper.toTagDTO(tag)).thenReturn(tagDTO);

        assertEquals(3, tagService.findAll(pageable).getTotalElements());
    }


    @Test
    public void testFindById() {
        final Tag tag = createTagObject();
        tag.setId(1);
        final TagDTO tagDTO = createTagDTOObject();
        tagDTO.setId(1);

        when(tagRepository.findById(tag.getId())).thenReturn(Optional.of(tag));
        when(tagMapper.toTagDTO(tag)).thenReturn(tagDTO);

        assertEquals(tagDTO, tagService.findById(tag.getId()));
    }


    @Test
    public void testFindByIdThrowsEntityNotFoundException() {
        when(tagRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> tagService.findById(1));
    }

    @Test
    public void testSaveNewTagWithoutGiftCertificate() {
        final TagDTO tagDTO = createTagDTOObject();
        final Tag tag = createTagObject();

        when(tagMapper.toTagDTO(tagRepository.save(tag))).thenReturn(tagDTO);

        assertEquals(tagDTO, tagService.save(tag));
    }

    @Test
    public void testUpdate() {
        final TagDTO tagDTO = createTagDTOObject();
        final Tag tag = createTagObject();
        tag.setId(1);

        when(tagRepository.findById(1)).thenReturn(Optional.of(tag));
        when(tagMapper.toTagDTO(tag)).thenReturn(tagDTO);

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

    @Test
    public void testFindTheMostWidelyUsedTag() {
        final Tag tag = createTagObject();
        final TagDTO tagDTO = createTagDTOObject();

        when(tagRepository.findTheMostWidelyUsedTag()).thenReturn(Optional.of(tag));
        when(tagMapper.toTagDTO(tag)).thenReturn(tagDTO);

        assertEquals(tagDTO, tagService.findTheMostWidelyUsedTag());
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
