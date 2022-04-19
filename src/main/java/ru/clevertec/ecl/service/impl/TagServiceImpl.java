package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dao.TagRepository;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.entty.GiftCertificate;
import ru.clevertec.ecl.entty.Tag;
import ru.clevertec.ecl.mapper.Mapper;
import ru.clevertec.ecl.service.TagService;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.clevertec.ecl.constants.Constants.EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final Mapper mapper;

    @Override
    public List<TagDTO> findAll() {
        return tagRepository.findAll().stream().map(mapper::tagToTagDTO).collect(Collectors.toList());
    }

    @Override
    public TagDTO findById(int id) {
        return tagRepository.findById(id).map(mapper::tagToTagDTO)
                .orElseThrow(() -> new EntityNotFoundException(String.format(EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT, id)));
    }

    @Override
    @Transactional
    public TagDTO save(Tag tag) {
        if (tagRepository.findById(tag.getId()).isPresent()) {
            final List<GiftCertificate> giftCertificates = tagRepository.findById(tag.getId()).get().getGiftCertificates();
            tag.getGiftCertificates().addAll(giftCertificates);
        }
        return mapper.tagToTagDTO(tagRepository.save(tag));
    }

    @Override
    public TagDTO update(int id, TagDTO tagDTO) {
        return tagRepository.findById(id)
                .map(tag -> updateTagFromTagDTO(tag, tagDTO))
                .map(mapper::tagToTagDTO)
                .orElseThrow(() -> new EntityNotFoundException(String.format(EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT, id)));
    }


    @Override
    public void delete(int id) {
        tagRepository.deleteById(id);
    }

    private Tag updateTagFromTagDTO(Tag tag, TagDTO dto) {
        if (Objects.nonNull(dto.getName())) {
            tag.setName(dto.getName());
        }
        return tag;
    }

}
