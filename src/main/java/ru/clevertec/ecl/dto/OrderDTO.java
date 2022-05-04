package ru.clevertec.ecl.dto;

import lombok.*;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;


@Value
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
    LocalDateTime purchaseDate;

}
