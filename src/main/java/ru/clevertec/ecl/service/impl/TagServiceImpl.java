package ru.clevertec.ecl.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dto.CommitLogDTO;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.dto.TypeObject;
import ru.clevertec.ecl.entty.Tag;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.repositories.TagRepository;
import ru.clevertec.ecl.service.CommitLogService;
import ru.clevertec.ecl.service.TagService;

import javax.persistence.EntityNotFoundException;
import java.util.Objects;

import static ru.clevertec.ecl.constants.Constants.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagServiceImpl implements TagService {

    @Value("${server.port}")
    private final String localPort;

    private final ObjectMapper mapper;
    private final TagMapper tagMapper;
    private final TagRepository tagRepository;
    private final CommitLogService commitLogService;

    @Override
    @Cacheable(value = TAG, sync = true)
    public Page<TagDTO> findAll(Pageable pageable) {
        return tagRepository.findAll(pageable).map(tagMapper::toTagDTO);
    }

    @Override
    @Cacheable(value = TAG, sync = true)
    public TagDTO findById(int id) {
        final TagDTO dto = tagRepository.findById(id).map(tagMapper::toTagDTO)
                .orElseThrow(() -> new EntityNotFoundException(String.format(EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT, TAG, id)));
        log.info("found tag - {}", dto);
        return dto;
    }

    @Override
    @Transactional
    public TagDTO save(Tag tag, Boolean saveToCommitLog) {
        log.info("tag to save to database - {}", tag);
        final Tag saved = tagRepository.findByNameIgnoreCase(tag.getName()).orElseGet(() -> saveTag(tag, saveToCommitLog));
        return tagMapper.toTagDTO(saved);
    }

    private Tag saveTag(Tag tag, Boolean saveToCommitLog) {
        final Tag saved = tagRepository.save(tag);
        log.info("successful saving of the tag in the database - {}", saved);
        if (Objects.isNull(saveToCommitLog) || saveToCommitLog) {
            sendToCommitLogSave(tag);
        }
        return saved;
    }

    @SneakyThrows
    private void sendToCommitLogSave(Tag tag) {
        commitLogService.sendToCommitLog(CommitLogDTO.builder()
                .url(URL_CREATE_TAG)
                .method(HttpMethod.POST)
                .body(mapper.writeValueAsString(tag))
                .typeObject(TypeObject.TAG)
                .portInitiatorLog(localPort)
                .build());
    }

    @Override
    @Transactional
    public TagDTO update(int id, TagDTO tagDTO, Boolean saveToCommitLog) {
        log.info("tag for updating in the database - {}", tagDTO);
        final TagDTO updated = tagRepository.findById(id)
                .map(tag -> updateTagFromTagDTO(tag, tagDTO))
                .map(tagMapper::toTagDTO)
                .orElseThrow(() -> new EntityNotFoundException(String.format(EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT, TAG, id)));
        log.info("successful update of the tag in the database - {}", updated);
        if (Objects.isNull(saveToCommitLog) || saveToCommitLog) {
            sendToCommitLogUpdate(id, tagDTO);
        }
        return updated;
    }

    @SneakyThrows
    private void sendToCommitLogUpdate(int id, TagDTO tag) {
        commitLogService.sendToCommitLog(CommitLogDTO.builder()
                .id(id)
                .url(URL_UPDATE_TAG)
                .method(HttpMethod.PUT)
                .body(mapper.writeValueAsString(tag))
                .typeObject(TypeObject.TAG)
                .portInitiatorLog(localPort)
                .build());
    }


    @Override
    @Transactional
    public void delete(int id, Boolean saveToCommitLog) {
        tagRepository.deleteById(id);
        log.info("tag with id = {} deleted successfully", id);
        if (Objects.isNull(saveToCommitLog) || saveToCommitLog) {
            sendToCommitLogDelete(id);
        }
    }

    private void sendToCommitLogDelete(int id) {
        commitLogService.sendToCommitLog(CommitLogDTO.builder()
                .id(id)
                .url(URL_DELETE_TAG)
                .method(HttpMethod.DELETE)
                .typeObject(TypeObject.TAG)
                .portInitiatorLog(localPort)
                .build());
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