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
import ru.clevertec.ecl.config.Cluster;
import ru.clevertec.ecl.dto.*;
import ru.clevertec.ecl.entty.GiftCertificate;
import ru.clevertec.ecl.entty.Tag;
import ru.clevertec.ecl.entty.User;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaCommitLogListener {

    @Value("${server.port}")
    private final String localPort;

    private final Cluster cluster;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @KafkaListener(topics = "${kafka.order-commit-log-topic}", autoStartup = "${kafka.auto-startup}")
    public void commitLogListenerOrder(CommitLogDTO commitLog) {
        if (!localPort.equals(commitLog.getPortInitiatorLog())
                && HttpMethod.POST.equals(commitLog.getMethod())
                && checkServer(commitLog.getId())) {
            restTemplate.postForObject(String.format(commitLog.getUrl(), localPort),
                    objectMapper.readValue(commitLog.getBody(), InputDataOrderDTO.class), Object.class);
        }
    }

    private boolean checkServer(Integer id) {
        return Objects.nonNull(cluster.getNodes().get(id % cluster.getNodes().size()).getReplicas().stream()
                .filter(replica -> localPort.equals(replica.getPort()))
                .findFirst().orElse(null));
    }


    /**
     * считывает из топика (очереди)
     *
     * @param commitLog
     */
    @SneakyThrows
    @KafkaListener(topics = "${kafka.commit-log-topic}", autoStartup = "${kafka.auto-startup}")
    public void commitLogListener(CommitLogDTO commitLog) {
        log.info(">>> KafkaCommitLogListener.commitLogListener commitLogDTO: {}", commitLog);
        if (!localPort.equals(commitLog.getPortInitiatorLog())) {
            if (TypeObject.GIFT.equals(commitLog.getTypeObject())) {
                giftCertificateProcess(commitLog);
            } else if (TypeObject.TAG.equals(commitLog.getTypeObject())) {
                tagProcess(commitLog);
            } else if (TypeObject.USER.equals(commitLog.getTypeObject())) {
                userProcess(commitLog);
            }
        }
    }

    @SneakyThrows
    private void giftCertificateProcess(CommitLogDTO commitLog) {
        if (HttpMethod.POST.equals(commitLog.getMethod())) {
            log.info("before: HttpMethod:POST, commitLog: {}", commitLog);
            final Object object = restTemplate.postForObject(String.format(commitLog.getUrl(), localPort),
                    objectMapper.readValue(commitLog.getBody(), GiftCertificate.class), Object.class);
            log.info("after: HttpMethod:POST, object: {}", object);
        } else if (HttpMethod.PUT.equals(commitLog.getMethod())) {
            log.info("before: HttpMethod:PUT, commitLog: {}", commitLog);
            restTemplate.put(String.format(commitLog.getUrl(), localPort, commitLog.getId()),
                    objectMapper.readValue(commitLog.getBody(), GiftCertificateDTO.class));
            log.info("after: HttpMethod:PUT");
        } else if (HttpMethod.DELETE.equals(commitLog.getMethod())) {
            log.info("before: HttpMethod:DELETE");
            restTemplate.delete(String.format(commitLog.getUrl(), localPort, commitLog.getId()));
            log.info("after: HttpMethod:DELETE");
        }
    }


    @SneakyThrows
    private void tagProcess(CommitLogDTO commitLog) {
        if (HttpMethod.POST.equals(commitLog.getMethod())) {
            log.info("before: HttpMethod:POST, commitLog: {}", commitLog);
            final Object object = restTemplate.postForObject(String.format(commitLog.getUrl(), localPort),
                    objectMapper.readValue(commitLog.getBody(), Tag.class), Object.class);
            log.info("after: HttpMethod:POST, object: {}", object);
        } else if (HttpMethod.PUT.equals(commitLog.getMethod())) {
            log.info("before: HttpMethod:PUT, commitLog: {}", commitLog);
            restTemplate.put(String.format(commitLog.getUrl(), localPort, commitLog.getId()),
                    objectMapper.readValue(commitLog.getBody(), TagDTO.class));
            log.info("after: HttpMethod:PUT");
        } else if (HttpMethod.DELETE.equals(commitLog.getMethod())) {
            log.info("before: HttpMethod:DELETE");
            restTemplate.delete(String.format(commitLog.getUrl(), localPort, commitLog.getId()));
            log.info("after: HttpMethod:DELETE");
        }
    }

    @SneakyThrows
    private void userProcess(CommitLogDTO commitLog) {
        if (HttpMethod.POST.equals(commitLog.getMethod())) {
            log.info("before: HttpMethod:POST, commitLog: {}", commitLog);
            final Object object = restTemplate.postForObject(String.format(commitLog.getUrl(), localPort),
                    objectMapper.readValue(commitLog.getBody(), User.class), Object.class);
            log.info("after: HttpMethod:POST, object: {}", object);
        } else if (HttpMethod.DELETE.equals(commitLog.getMethod())) {
            log.info("before: HttpMethod:DELETE");
            restTemplate.delete(String.format(commitLog.getUrl(), localPort, commitLog.getId()));
            log.info("after: HttpMethod:DELETE");
        }
    }

}
