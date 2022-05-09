package ru.clevertec.ecl.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GiftCertificateDurationDTO {
    @NotNull
    @Positive
    Integer duration;
}
