package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dao.TagRepository;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.entty.GiftCertificate;
import ru.clevertec.ecl.entty.Tag;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.service.TagService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Override
    public List<TagDTO> findAll() {
        return tagMapper.createTagList(tagRepository.findAll());
    }

    @Override
    public TagDTO findById(int id) {
        return tagMapper.createTag(tagRepository.findById(id).orElse(null));
    }

    @Override
    @Transactional
    public TagDTO save(Tag tag) {

        if (tagRepository.findById(tag.getId()).isPresent()) {
            final List<GiftCertificate> giftCertificates = tagRepository.findById(tag.getId()).get().getGiftCertificates();
            tag.getGiftCertificates().addAll(giftCertificates);
        }

        return tagMapper.createTag(tagRepository.save(tag));
    }

    @Override
    public TagDTO update(int id, TagDTO tagDTO) {
        final Tag tagFromDB = tagRepository.findById(id).orElse(null);
        TagDTO saved = null;
        if (tagFromDB != null) {
            if (tagDTO.getName() != null) {
                tagFromDB.setName(tagDTO.getName());
            }
            saved = tagMapper.createTag(tagRepository.save(tagFromDB));
        }

        return saved;
    }


    @Override
    public void delete(int id) {
        tagRepository.deleteById(id);
    }
}
