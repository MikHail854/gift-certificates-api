package ru.clevertec.ecl.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpMethod;

@Data
@Builder
public class CommitLogDTO {
    private Integer id;
    private String url;
    private String body;
    private HttpMethod method;
    private TypeObject typeObject;
}
