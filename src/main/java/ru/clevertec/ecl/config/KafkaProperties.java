package ru.clevertec.ecl.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "kafka")
public class KafkaProperties {
    private Boolean autoStartup;
    private String commitLogTopic;
    private String groupId;
    private String offsetReset;
    private String bootstrap;
}
