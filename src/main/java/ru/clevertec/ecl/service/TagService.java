package ru.clevertec.ecl.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.entty.Tag;


public interface TagService {

    Page<TagDTO> findAll(Pageable pageable);

    TagDTO findById(int id);

    TagDTO save(Tag tag, Boolean saveToCommitLog);

    TagDTO update(int id, TagDTO tagDTO, Boolean saveToCommitLog);

    void delete(int id, Boolean saveToCommitLog);

    TagDTO findTheMostWidelyUsedTag();

}
