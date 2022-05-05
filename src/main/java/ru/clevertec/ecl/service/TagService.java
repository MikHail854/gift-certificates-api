package ru.clevertec.ecl.service;

import org.springframework.data.domain.Page;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.entty.Tag;
import org.springframework.data.domain.Pageable;


public interface TagService {

    Page<TagDTO> findAll(Pageable pageable);

    TagDTO findById(int id);

    TagDTO save(Tag tag);

    TagDTO update(int id, TagDTO tagDTO);

    void delete(int id);

    TagDTO findTheMostWidelyUsedTag();

}
