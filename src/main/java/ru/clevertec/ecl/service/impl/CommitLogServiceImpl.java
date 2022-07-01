package ru.clevertec.ecl.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.dto.CommitLogDTO;
import ru.clevertec.ecl.service.CommitLogService;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommitLogServiceImpl implements CommitLogService {

    private final ObjectMapper objectMapper;
    private static final String KAFKA_TOPIC = "commit_log";
    private static final String KAFKA_ORDER_TOPIC = "order_commit_log";
    private final KafkaTemplate<String, String> kafkaTemplate;


    @Override
    @SneakyThrows
    public void sendToCommitLog(CommitLogDTO commitLogDTO) {
        log.info("called method sendToCommitLog. commitLogDTO: {}", commitLogDTO);
        kafkaTemplate.send(KAFKA_TOPIC, objectMapper.writeValueAsString(commitLogDTO));
    }

    @Override
    @SneakyThrows
    public void sendOrderToCommitLog(CommitLogDTO commitLogDTO) {
        log.info("called method sendOrderToCommitLog. commitLogDTO: {}", commitLogDTO);
        kafkaTemplate.send(KAFKA_ORDER_TOPIC, objectMapper.writeValueAsString(commitLogDTO));
    }


}