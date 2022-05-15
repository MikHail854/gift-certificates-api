package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.clevertec.ecl.config.ServerProperties;
import ru.clevertec.ecl.service.HealthCheckService;

import java.util.Objects;


@Slf4j
@Service
@RequiredArgsConstructor
public class HealthCheckServiceImpl implements HealthCheckService {

    private final RestTemplate restTemplate;
    private final ServerProperties serverProperties;

    @Override
    @Scheduled(cron = "* * * * * ?")
    public void nodeHealthCheck() {
        for (int i = 0; i < serverProperties.getPorts().size(); i++) {
            try {
                String url = "http://localhost:" + serverProperties.getPorts().get(i) + "/health-check";
                final HttpStatus httpStatus = restTemplate.getForObject(url, HttpStatus.class);
                if (Objects.equals(httpStatus, HttpStatus.OK)) {
                    log.info("node on port: {} is running", serverProperties.getPorts().get(i));
                } else {
                    log.error("node on port: {} is down. HttpStatus: {}", serverProperties.getPorts().get(i), httpStatus);
                }
            } catch (Exception e) {
                log.error("node on port: {} is down. {}", serverProperties.getPorts().get(i), String.valueOf(e));
            }
        }
    }

}
