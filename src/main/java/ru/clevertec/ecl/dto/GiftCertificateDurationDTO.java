package ru.clevertec.ecl.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Value
@Builder
public class GiftCertificateDurationDTO {
    @NotNull
    @Positive
    Integer duration;
}
