package ru.clevertec.ecl.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangeDTO {
    private Integer id;
    private String sql;
}
