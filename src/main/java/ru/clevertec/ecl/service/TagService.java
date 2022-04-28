package ru.clevertec.ecl.service;

import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.entty.Tag;

import java.util.List;

public interface TagService {

    List<TagDTO> findAll();

    TagDTO findById(int id);

    TagDTO save(Tag tag);

    TagDTO update(int id, TagDTO tagDTO);

    void delete(int id);

}
