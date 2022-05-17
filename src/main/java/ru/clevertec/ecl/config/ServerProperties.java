package ru.clevertec.ecl.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "server.properties")
public class ServerProperties {

    private Map<Integer, String> ports;

}
