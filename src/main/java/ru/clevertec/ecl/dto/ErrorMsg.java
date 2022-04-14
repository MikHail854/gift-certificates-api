package ru.clevertec.ecl.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ErrorMsg {
    private String error;
    private String errorDescription;
}
