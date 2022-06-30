package ru.clevertec.ecl.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.dto.CommitLogDTO;
import ru.clevertec.ecl.entty.CommitLog;
import ru.clevertec.ecl.mapper.CommitLogMapper;
import ru.clevertec.ecl.repositories.CommitLogRepository;
import ru.clevertec.ecl.service.CommitLogService;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommitLogServiceImpl implements CommitLogService {

    private final ObjectMapper objectMapper;
    private static final String KAFKA_TOPIC = "commit_log";
    private final KafkaTemplate<String, String> kafkaTemplate;


    @Override
    @SneakyThrows
    public void sendToCommitLog(CommitLogDTO commitLogDTO) {
        kafkaTemplate.send(KAFKA_TOPIC, objectMapper.writeValueAsString(commitLogDTO));
    }

//
//    private final CommitLogRepository commitLogRepository;
//    private final CommitLogMapper commitLogMapper;
//
//    @Override
//    public void save(CommitLog commitLog) {
//        commitLogRepository.save(commitLog);
//    }
//
//    @Override
//    public CommitLogDTO getLastCommit() {
//        final CommitLogDTO commitLogDTO = commitLogMapper.toCommitLogDTO(commitLogRepository.getLastCommit());
//        if (Objects.nonNull(commitLogDTO)) {
//            return commitLogDTO;
//        } else {
//            return CommitLogDTO.builder().id(0).build();
//        }
//    }



}