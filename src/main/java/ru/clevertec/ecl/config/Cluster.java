package ru.clevertec.ecl.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Data
@Configuration
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "config")
public class Cluster {

    private Map<Integer, Node> nodes;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Node {
        private List<Replica> replicas;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Replica {
        private String port;
        private Boolean isAlive = false;
    }

}