package ru.clevertec.ecl.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dto.*;
import ru.clevertec.ecl.entty.GiftCertificate;
import ru.clevertec.ecl.mapper.GiftCertificateMapper;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.repositories.GiftCertificateRepository;
import ru.clevertec.ecl.repositories.TagRepository;
import ru.clevertec.ecl.service.CommitLogService;
import ru.clevertec.ecl.service.GiftCertificateService;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.clevertec.ecl.constants.Constants.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GiftCertificateServiceImpl implements GiftCertificateService {

    @Value("${server.port}")
    private final String localPort;

    private final ObjectMapper mapper;
    private final TagMapper tagMapper;
    private final TagRepository tagRepository;
    private final CommitLogService commitLogService;
    private final GiftCertificateMapper giftCertificateMapper;
    private final GiftCertificateRepository giftCertificateRepository;


    @Override
    @Cacheable(value = "certificate", sync = true)
    public Page<GiftCertificateDTO> findAll(GiftCertificateFilter filter, Pageable pageable) {
        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withMatcher("name", match -> match.contains().ignoreCase())
                .withMatcher("description", match -> match.contains().ignoreCase());
        return giftCertificateRepository.findAll(
                Example.of(giftCertificateMapper.toGiftCertificate(filter), matcher), pageable)
                .map(giftCertificateMapper::toGiftCertificateDTO);
    }

    @Override
    @Cacheable(value = "certificate", sync = true)
    public GiftCertificateDTO findById(int id) {
        final GiftCertificateDTO dto = giftCertificateRepository.findById(id).map(giftCertificateMapper::toGiftCertificateDTO)
                .orElseThrow(() -> new EntityNotFoundException(String.format(EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT, "gift certificate", id)));
        log.info("found giftCertificate - {}", dto);
        return dto;
    }

    @Override
    @Cacheable(value = "certificate", sync = true)
    public List<GiftCertificateDTO> findGiftCertificateByTagName(String tagName) {
        final Optional<TagDTO> tagDTO = tagRepository.findByNameIgnoreCase(tagName).map(tagMapper::toTagDTO);
        if (tagDTO.isPresent()) {
            final List<GiftCertificateDTO> collect = giftCertificateRepository.findByTagsName(tagName).stream()
                    .map(giftCertificateMapper::toGiftCertificateDTO)
                    .collect(Collectors.toList());
            log.info("Found list of gift certificates with tag name = {}. Gift certificate list - {}", tagName, collect);
            return collect;
        }
        throw new IllegalArgumentException();
    }

    @Override
    @Transactional
    public GiftCertificateDTO save(GiftCertificate giftCertificate, Boolean saveToCommitLog) {
        log.info("gift certificate to save to database - {}", giftCertificate);
        if (Objects.nonNull(giftCertificate.getId()) && giftCertificateRepository.findById(giftCertificate.getId()).isPresent()) {
            final GiftCertificate giftCertificateFromDB = giftCertificateMapper.toGiftCertificate(findById(giftCertificate.getId()));
            if (!Objects.nonNull(giftCertificate.getTags())) {
                giftCertificate.setTags(new ArrayList<>());
            }
            giftCertificate.getTags().addAll(giftCertificateFromDB.getTags());
            giftCertificate.setCreateDate(giftCertificateFromDB.getCreateDate());
        } else {
            giftCertificate.setCreateDate(LocalDateTime.now());
        }
        giftCertificate.setLastUpdateDate(LocalDateTime.now());
        final GiftCertificateDTO saved = giftCertificateMapper.toGiftCertificateDTO(giftCertificateRepository.saveAndFlush(giftCertificate));
        log.info("successful saving of the gift certificate in the database - {}", saved);
        if (Objects.isNull(saveToCommitLog) || saveToCommitLog) {
            sendToCommitLogSave(giftCertificate);
        }
        return saved;
    }

    @SneakyThrows
    private void sendToCommitLogSave(GiftCertificate giftCertificate) {
        commitLogService.sendToCommitLog(CommitLogDTO.builder()
                .url(URL_CREATE_GIFT_CERTIFICATE)
                .method(HttpMethod.POST)
                .body(mapper.writeValueAsString(giftCertificate))
                .typeObject(TypeObject.GIFT)
                .portInitiatorLog(localPort)
                .build());
    }

    @Override
    @Transactional
    public GiftCertificateDTO update(int id, GiftCertificateDTO dto, Boolean saveToCommitLog) {
        log.info("gift certificate for updating in the database - {}", dto);
        final GiftCertificateDTO updated = giftCertificateRepository.findById(id)
                .map(giftCertificate -> updateGiftCertificateFromGiftCertificateDTO(giftCertificate, dto))
                .map(giftCertificateRepository::saveAndFlush)
                .map(giftCertificateMapper::toGiftCertificateDTO)
                .orElseThrow(() -> new EntityNotFoundException(String.format(EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT, "gift certificate", id)));
        log.info("successful update of the gift certificate in the database - {}", updated);
        if (Objects.isNull(saveToCommitLog) || saveToCommitLog) {
            sendToCommitLogUpdate(id, dto);
        }
        return updated;
    }

    @SneakyThrows
    private void sendToCommitLogUpdate(int id, GiftCertificateDTO giftCertificate) {
        commitLogService.sendToCommitLog(CommitLogDTO.builder()
                .id(id)
                .url(URL_UPDATE_GIFT_CERTIFICATE)
                .method(HttpMethod.PUT)
                .body(mapper.writeValueAsString(giftCertificate))
                .typeObject(TypeObject.GIFT)
                .portInitiatorLog(localPort)
                .build());
    }

    @Override
    @Transactional
    public GiftCertificateDTO updatePrice(int id, GiftCertificatePriceDTO dto, Boolean saveToCommitLog) {
        log.info("price for updating in the database - {}", dto);
        final GiftCertificateDTO updated = giftCertificateRepository.findById(id)
                .map(giftCertificate -> updateGiftCertificatePrice(giftCertificate, dto))
                .map(giftCertificateRepository::saveAndFlush)
                .map(giftCertificateMapper::toGiftCertificateDTO)
                .orElseThrow(() -> new EntityNotFoundException(String.format(EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT, "gift certificate", id)));
        log.info("successful update of the gift certificate price in the database - {}", updated);
        if (Objects.isNull(saveToCommitLog) || saveToCommitLog) {
            sendToCommitLogUpdatePrice(id, dto);
        }
        return updated;
    }

    @SneakyThrows
    private void sendToCommitLogUpdatePrice(int id, GiftCertificatePriceDTO giftCertificatePrice) {
        commitLogService.sendToCommitLog(CommitLogDTO.builder()
                .id(id)
                .url(URL_UPDATE_PRICE_GIFT_CERTIFICATE)
                .method(HttpMethod.PUT)
                .body(mapper.writeValueAsString(giftCertificatePrice))
                .typeObject(TypeObject.GIFT)
                .portInitiatorLog(localPort)
                .build());
    }

    @Override
    @Transactional
    public GiftCertificateDTO updateDuration(int id, GiftCertificateDurationDTO dto, Boolean saveToCommitLog) {
        log.info("duration for updating in the database - {}", dto);
        final GiftCertificateDTO updated = giftCertificateRepository.findById(id)
                .map(giftCertificate -> updateGiftCertificateDuration(giftCertificate, dto))
                .map(giftCertificateRepository::saveAndFlush)
                .map(giftCertificateMapper::toGiftCertificateDTO)
                .orElseThrow(() -> new EntityNotFoundException(String.format(EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT, "gift certificate", id)));
        log.info("successful update of the gift certificate duration in the database - {}", updated);
        if (Objects.isNull(saveToCommitLog) || saveToCommitLog) {
            sendToCommitLogUpdateDuration(id, dto);
        }
        return updated;
    }

    @SneakyThrows
    private void sendToCommitLogUpdateDuration(int id, GiftCertificateDurationDTO giftCertificateDuration) {
        commitLogService.sendToCommitLog(CommitLogDTO.builder()
                .id(id)
                .url(URL_UPDATE_DURATION_GIFT_CERTIFICATE)
                .method(HttpMethod.PUT)
                .body(mapper.writeValueAsString(giftCertificateDuration))
                .typeObject(TypeObject.GIFT)
                .portInitiatorLog(localPort)
                .build());
    }

    @Override
    @Transactional
    public void delete(int id, Boolean saveToCommitLog) {
        giftCertificateRepository.deleteById(id);
        log.info("gift certificate with id = {} deleted successfully", id);
        if (Objects.isNull(saveToCommitLog) || saveToCommitLog) {
            sendToCommitLogDelete(id);
        }
    }

    private void sendToCommitLogDelete(int id) {
        commitLogService.sendToCommitLog(CommitLogDTO.builder()
                .id(id)
                .url(URL_DELETE_GIFT_CERTIFICATE)
                .method(HttpMethod.DELETE)
                .typeObject(TypeObject.GIFT)
                .portInitiatorLog(localPort)
                .build());
    }

    @Override
    public boolean checkGift(int id) {
        return giftCertificateRepository.findById(id).isPresent();
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