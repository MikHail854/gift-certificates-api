package ru.clevertec.ecl.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GiftCertificatePriceDTO {
    @NotNull
    @Positive
    Float price;
}
