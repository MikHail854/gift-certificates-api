package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dao.GiftCertificateRepository;
import ru.clevertec.ecl.dao.TagRepository;
import ru.clevertec.ecl.dto.*;
import ru.clevertec.ecl.entty.GiftCertificate;
import ru.clevertec.ecl.mapper.GiftCertificateMapper;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.service.GiftCertificateService;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.clevertec.ecl.constants.Constants.EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateRepository giftCertificateRepository;
    private final TagRepository tagRepository;
    private final GiftCertificateMapper giftCertificateMapper;
    private final TagMapper tagMapper;


    @Override
    public Page<GiftCertificateDTO> findAll(GiftCertificateFilter filter, Pageable pageable) {
        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withMatcher("name", match -> match.contains().ignoreCase())
                .withMatcher("description", match -> match.contains().ignoreCase());
        return giftCertificateRepository.findAll(
                Example.of(giftCertificateMapper.giftCertificateFilterToGiftCertificate(filter), matcher), pageable)
                .map(giftCertificateMapper::giftCertificateToGiftCertificateDTO);
    }

    @Override
    public GiftCertificateDTO findById(int id) {
        final GiftCertificateDTO dto = giftCertificateRepository.findById(id).map(giftCertificateMapper::giftCertificateToGiftCertificateDTO)
                .orElseThrow(() -> new EntityNotFoundException(String.format(EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT, "gift certificate", id)));
        log.info("found giftCertificate - {}", dto);
        return dto;
    }

    @Override
    public List<GiftCertificateDTO> findGiftCertificateByTagName(String tagName) {
        final Optional<TagDTO> tagDTO = tagRepository.findByNameIgnoreCase(tagName).map(tagMapper::tagToTagDTO);
        if (tagDTO.isPresent()) {
            final List<GiftCertificateDTO> collect = giftCertificateRepository.findByTagsName(tagName).stream()
                    .map(giftCertificateMapper::giftCertificateToGiftCertificateDTO)
                    .collect(Collectors.toList());
            log.info("Found list of gift certificates with tag name = {}. Gift certificate list - {}", tagName, collect);
            return collect;
        }
        throw new IllegalArgumentException();
    }

    @Override
    @Transactional
    public GiftCertificateDTO save(GiftCertificate giftCertificate) {
        log.info("gift certificate to save to database - {}", giftCertificate);
        if (Objects.nonNull(giftCertificate.getId()) && giftCertificateRepository.findById(giftCertificate.getId()).isPresent()) {
            final GiftCertificate giftCertificateFromDB = giftCertificateMapper.giftCertificateDTOToGiftCertificate(findById(giftCertificate.getId()));
            giftCertificate.getTags().addAll(giftCertificateFromDB.getTags());
        }
        giftCertificate.setCreateDate(LocalDateTime.now());
        giftCertificate.setLastUpdateDate(LocalDateTime.now());
        final GiftCertificateDTO saved = giftCertificateMapper.giftCertificateToGiftCertificateDTO(giftCertificateRepository.save(giftCertificate));
        log.info("successful saving of the gift certificate in the database - {}", saved);
        return saved;
    }

    @Override
    @Transactional
    public GiftCertificateDTO update(int id, GiftCertificateDTO dto) {
        log.info("gift certificate for updating in the database - {}", dto);
        final GiftCertificateDTO updated = giftCertificateRepository.findById(id)
                .map(giftCertificate -> updateGiftCertificateFromGiftCertificateDTO(giftCertificate, dto))
                .map(giftCertificateRepository::saveAndFlush)
                .map(giftCertificateMapper::giftCertificateToGiftCertificateDTO)
                .orElseThrow(() -> new EntityNotFoundException(String.format(EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT, "gift certificate", id)));
        log.info("successful update of the gift certificate in the database - {}", updated);
        return updated;
    }

    @Override
    @Transactional
    public GiftCertificateDTO updatePrice(int id, GiftCertificatePriceDTO dto) {
        log.info("price for updating in the database - {}", dto);
        final GiftCertificateDTO updated = giftCertificateRepository.findById(id)
                .map(giftCertificate -> updateGiftCertificatePrice(giftCertificate, dto))
                .map(giftCertificateRepository::saveAndFlush)
                .map(giftCertificateMapper::giftCertificateToGiftCertificateDTO)
                .orElseThrow(() -> new EntityNotFoundException(String.format(EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT, "gift certificate", id)));
        log.info("successful update of the gift certificate price in the database - {}", updated);
        return updated;
    }

    @Override
    @Transactional
    public GiftCertificateDTO updateDuration(int id, GiftCertificateDurationDTO dto) {
        log.info("duration for updating in the database - {}", dto);
        final GiftCertificateDTO updated = giftCertificateRepository.findById(id)
                .map(giftCertificate -> updateGiftCertificateDuration(giftCertificate, dto))
                .map(giftCertificateRepository::saveAndFlush)
                .map(giftCertificateMapper::giftCertificateToGiftCertificateDTO)
                .orElseThrow(() -> new EntityNotFoundException(String.format(EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT, "gift certificate", id)));
        log.info("successful update of the gift certificate duration in the database - {}", updated);
        return updated;
    }

    @Override
    @Transactional
    public void delete(int id) {
        giftCertificateRepository.deleteById(id);
        log.info("gift certificate with id = {} deleted successfully", id);
    }

    private GiftCertificate updateGiftCertificateFromGiftCertificateDTO(GiftCertificate certificate, GiftCertificateDTO dto) {
        if (Objects.nonNull(dto.getName())) {
            certificate.setName(dto.getName());
        }
        if (Objects.nonNull(dto.getDescription())) {
            certificate.setDescription(dto.getDescription());
        }
        if (Objects.nonNull(dto.getPrice())) {
            certificate.setPrice(dto.getPrice());
        }
        if (Objects.nonNull(dto.getDuration())) {
            certificate.setDuration(dto.getDuration());
        }
        if (Objects.nonNull(dto.getCreateDate())) {
            certificate.setCreateDate(dto.getCreateDate());
        }

        certificate.setLastUpdateDate(LocalDateTime.now());
        return certificate;
    }

    private GiftCertificate updateGiftCertificatePrice(GiftCertificate certificate, GiftCertificatePriceDTO dto) {
        if (Objects.nonNull(dto.getPrice())) {
            certificate.setPrice(dto.getPrice());
            certificate.setLastUpdateDate(LocalDateTime.now());
        }
        return certificate;
    }

    private GiftCertificate updateGiftCertificateDuration(GiftCertificate certificate, GiftCertificateDurationDTO dto) {
        if (Objects.nonNull(dto.getDuration())) {
            certificate.setDuration(dto.getDuration());
            certificate.setLastUpdateDate(LocalDateTime.now());
        }
        return certificate;
    }

}
