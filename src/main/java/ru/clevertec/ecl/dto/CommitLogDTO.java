package ru.clevertec.ecl.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommitLogDTO {
    private Integer id;
    private String url;
    private String body;
    private HttpMethod method;
    private TypeObject typeObject;
    private String portInitiatorLog;
}
