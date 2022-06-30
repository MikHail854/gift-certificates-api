package ru.clevertec.ecl.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.clevertec.ecl.dto.CommitLogDTO;
import ru.clevertec.ecl.dto.TypeObject;
import ru.clevertec.ecl.entty.GiftCertificate;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaCommitLogListener {

    @Value("${server.port}")
    private final String localPort;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    //todo нужно ли вызывать этот метод? как часто он срабатывает? - спринг сам отслеживает. кто подписан на евент
    /**
     * считывает из топика (очереди)
     *
     * @param commitLog
     */
    @SneakyThrows
    @KafkaListener(topics = "${kafka.commit-log-topic}", autoStartup = "${kafka.auto-startup}")
    public void commitLogListener(CommitLogDTO commitLog) {
        log.info(">>> KafkaCommitLogListener.commitLogListener commitLogDTO: {}", commitLog);
        if (HttpMethod.POST.equals(commitLog.getMethod()) && TypeObject.GIFT.equals(commitLog.getTypeObject())){
            restTemplate.postForObject(String.format(commitLog.getUrl(), localPort),
                    objectMapper.readValue(commitLog.getBody(), GiftCertificate.class), Object.class);
        }

    }

}
