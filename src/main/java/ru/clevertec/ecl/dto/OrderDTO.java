package ru.clevertec.ecl.dto;

import lombok.*;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;


@Value
@Builder
@RequiredArgsConstructor
public class OrderDTO {

    @Positive
    Integer id;
    @Positive
    Integer userId;
    @Positive
    Integer certificateId;
    @Positive
    Float price;
    @EqualsAndHashCode.Exclude
    LocalDateTime purchaseDate;

}
