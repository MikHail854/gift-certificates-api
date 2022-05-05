package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dao.TagRepository;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.entty.Tag;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.service.TagService;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Objects;

import static ru.clevertec.ecl.constants.Constants.EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT;
import static ru.clevertec.ecl.constants.Constants.EXCEPTION_MESSAGE_SOMETHING_WENT_WRONG;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Override
    public Page<TagDTO> findAll(Pageable pageable) {
        return tagRepository.findAll(pageable).map(tagMapper::toTagDTO);
    }

    @Override
    public TagDTO findById(int id) {
        final TagDTO dto = tagRepository.findById(id).map(tagMapper::toTagDTO)
                .orElseThrow(() -> new EntityNotFoundException(String.format(EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT, "tag", id)));
        log.info("found tag - {}", dto);
        return dto;
    }

    @Override
    @Transactional
    public TagDTO save(Tag tag) {
        log.info("tag to save to database - {}", tag);
        if (Objects.nonNull(tag.getId()) && tagRepository.findById(tag.getId()).isPresent()) {
            final Tag tagFromDB = tagMapper.toTag(findById(tag.getId()));
            if (!Objects.nonNull(tag.getGiftCertificates())) {
                tag.setGiftCertificates(new ArrayList<>());
            }
            tag.getGiftCertificates().addAll(tagFromDB.getGiftCertificates());
        }
        final TagDTO saved = tagMapper.toTagDTO(tagRepository.save(tag));
        log.info("successful saving of the tag in the database - {}", saved);
        return saved;
    }

    @Override
    @Transactional
    public TagDTO update(int id, TagDTO tagDTO) {
        log.info("tag for updating in the database - {}", tagDTO);
        final TagDTO updated = tagRepository.findById(id)
                .map(tag -> updateTagFromTagDTO(tag, tagDTO))
                .map(tagMapper::toTagDTO)
                .orElseThrow(() -> new EntityNotFoundException(String.format(EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT, "tag", id)));
        log.info("successful update of the tag in the database - {}", updated);
        return updated;
    }


    @Override
    @Transactional
    public void delete(int id) {
        tagRepository.deleteById(id);
        log.info("tag with id = {} deleted successfully", id);
    }

    @Override
    public TagDTO findTheMostWidelyUsedTag() {
        final TagDTO dto = tagRepository.findTheMostWidelyUsedTag()
                .map(tagMapper::toTagDTO)
                .orElseThrow(() -> new EntityNotFoundException(EXCEPTION_MESSAGE_SOMETHING_WENT_WRONG));
        log.info("found the most widely used tag - {}", dto);
        return dto;
    }

    private Tag updateTagFromTagDTO(Tag tag, TagDTO dto) {
        if (Objects.nonNull(dto.getName())) {
            tag.setName(dto.getName());
        }
        return tag;
    }

}
