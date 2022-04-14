package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.dao.GiftCertificateRepository;
import ru.clevertec.ecl.dao.TagRepository;
import ru.clevertec.ecl.dto.GiftCertificateDTO;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.entty.GiftCertificate;
import ru.clevertec.ecl.entty.Tag;
import ru.clevertec.ecl.mapper.GiftCertificateMapper;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.service.GiftCertificateService;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateRepository giftCertificateRepository;
    private final GiftCertificateMapper giftCertificateMapper;
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;


    @Override
    public List<GiftCertificateDTO> findAll(String tagName) {
        return giftCertificateMapper.createGiftCertificateList(giftCertificateRepository.findAll());

    }

    @Override
    public GiftCertificateDTO findById(int id) {
        return giftCertificateMapper.createGiftCertificate(giftCertificateRepository.findById(id).orElse(null));
    }

    @Override
    public GiftCertificateDTO findGiftCertificateByTagName(String tagName) {
        final TagDTO tagByName = tagMapper.createTag(tagRepository.findTagByName(tagName));
        if (Objects.nonNull(tagByName)) {
            return giftCertificateMapper.createGiftCertificate(giftCertificateRepository.findByTagsName(tagName));
        }
        return null;
    }

    @Override
    public List<GiftCertificateDTO> findGiftCertificateByDescription(String description) {
        return giftCertificateMapper.createGiftCertificateList(giftCertificateRepository.findAllByDescription(description));
    }

    @Override
    public GiftCertificateDTO save(GiftCertificate giftCertificate) {

        if (giftCertificateRepository.findById(giftCertificate.getId()).isPresent()) {
            final List<Tag> tags = giftCertificateRepository.findById(giftCertificate.getId()).get().getTags();
            giftCertificate.getTags().addAll(tags);
        }

        return giftCertificateMapper.createGiftCertificate(giftCertificateRepository.save(giftCertificate));

    }

    @Override
    public GiftCertificateDTO update(int id, GiftCertificateDTO giftCertificateDTO) {
        final GiftCertificate giftCertificateFormDB = giftCertificateRepository.findById(id).orElse(null);
        GiftCertificateDTO saved = null;
        if (giftCertificateFormDB != null) {
            if (giftCertificateDTO.getName() != null) {
                giftCertificateFormDB.setName(giftCertificateDTO.getName());
            }
            if (giftCertificateDTO.getDescription() != null) {
                giftCertificateFormDB.setDescription(giftCertificateDTO.getDescription());
            }
            if (giftCertificateDTO.getPrice() != 0) {
                giftCertificateFormDB.setPrice(giftCertificateDTO.getPrice());
            }
            if (giftCertificateDTO.getDuration() != 0) {
                giftCertificateFormDB.setDuration(giftCertificateDTO.getDuration());
            }
            if (giftCertificateDTO.getCreateDate() != null) {
                giftCertificateFormDB.setCreateDate(giftCertificateDTO.getCreateDate());
            }
            if (giftCertificateDTO.getLastUpdateDate() != null) {
                giftCertificateFormDB.setLastUpdateDate(giftCertificateDTO.getLastUpdateDate());
            }

            saved = giftCertificateMapper.createGiftCertificate(giftCertificateRepository.save(giftCertificateFormDB));
        }
        return saved;
    }

    @Override
    public void delete(int id) {
        giftCertificateRepository.deleteById(id);
    }


}
