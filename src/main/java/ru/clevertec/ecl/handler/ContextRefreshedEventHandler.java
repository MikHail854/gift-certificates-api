package ru.clevertec.ecl.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.clevertec.ecl.config.Cluster;
import ru.clevertec.ecl.dto.ChangeDTO;
import ru.clevertec.ecl.dto.CommitLogDTO;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static ru.clevertec.ecl.constants.Constants.URL_COMMIT_LOG;

@Slf4j
@Component
@RequiredArgsConstructor
public class ContextRefreshedEventHandler {

    @Value("${server.port}")
    private final String localPort;

    private final Cluster cluster;
    private final RestTemplate restTemplate;

//    @EventListener
    public void handleContextRefreshEvent(ContextRefreshedEvent ctxStartEvt) {
        setIsAliveTrue();
        log.info("localPort: {}", localPort);
//        final Cluster.Node replica_not_found = cluster.getNodes().values().stream()
//                .filter(node -> node.getReplicas().stream()
//                        .anyMatch(replica -> localPort.equals(replica.getPort()))
//                )
//                .findFirst()
//                .orElseThrow(() -> new RuntimeException("no data to updating22"));
//        log.info("replica_not_found: {}", replica_not_found);
        final CommitLogDTO commitLog = getCommitLog();
        if (Objects.nonNull(commitLog)){

        }


        log.info(localPort);
        log.info("Context Start Event received.");
    }

    private CommitLogDTO getCommitLog(){

        final CommitLogDTO commitLog = cluster.getNodes().values().stream()
                .filter(node -> node.getReplicas().stream()
                        .anyMatch(replica -> localPort.equals(replica.getPort()))
                )
                .map(node -> node.getReplicas().stream()
                        .filter(Cluster.Replica::getIsAlive)
                        .filter(replica -> !localPort.equals(replica.getPort()))
                        .map(replica -> CompletableFuture.supplyAsync(
                                () -> restTemplate.getForObject(getUrl(replica.getPort()), CommitLogDTO.class)))
                        .map(CompletableFuture::join)
                        .filter(commitLogDTO -> commitLogDTO.getId() > 0)
                        .max(Comparator.comparing(CommitLogDTO::getId))
                        .orElse(null)
                )
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
        log.info("commitLog: {}", commitLog);
        return commitLog;
    }


    private String getUrl(String port) {
        return String.format(URL_COMMIT_LOG, port);
    }

    private void setIsAliveTrue() {
        cluster.getNodes().values()
                .forEach(node -> node.getReplicas().stream()
                        .filter(replica -> localPort.equals(replica.getPort()))
                        .forEach(replica -> replica.setIsAlive(true))
                );
    }

}
