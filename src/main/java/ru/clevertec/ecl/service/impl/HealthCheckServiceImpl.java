package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.clevertec.ecl.config.Cluster;
import ru.clevertec.ecl.service.HealthCheckService;

import java.util.List;
import java.util.Map;
import java.util.Objects;


@Slf4j
@Service
@RequiredArgsConstructor
public class HealthCheckServiceImpl implements HealthCheckService {

    private final Cluster cluster;
    private final RestTemplate restTemplate;

    @Override
    @Scheduled(cron = "0 * * * * ?")
    public void nodeHealthCheck() {
        final Map<Integer, Cluster.Node> nodes = cluster.getNodes();
        for (int i = 0; i < nodes.size(); i++) {
            final List<Cluster.Replica> replicas = nodes.get(i).getReplicas();
            for (Cluster.Replica replica : replicas) {
                try {
                    String url = "http://localhost:" + replica.getPort() + "/health-check";
                    final HttpStatus httpStatus = restTemplate.getForObject(url, HttpStatus.class);
                    if (Objects.equals(httpStatus, HttpStatus.OK)) {
                        if (!replica.getIsAlive()) {
                            replica.setIsAlive(true);
                        }
                    }
                } catch (Exception e) {
                    replica.setIsAlive(false);
                }
            }
            printStatusNode(nodes, i);
        }
    }

    private void printStatusNode(Map<Integer, Cluster.Node> nodes, int i) {
        final int countOfLiveReplicas = (int) nodes.get(i).getReplicas().stream().filter(Cluster.Replica::getIsAlive).count();
        switch (countOfLiveReplicas) {
            case 3:
                log.info("node {} - green", i);
                break;
            case 2:
                log.warn("node {} - yellow", i);
                break;
            case 1:
                log.warn("node {} - red", i);
                break;
            case 0:
                log.error("node {} has no live replicas", i);
        }
    }

}