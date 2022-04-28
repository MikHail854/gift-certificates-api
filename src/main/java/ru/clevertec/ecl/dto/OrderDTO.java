package ru.clevertec.ecl.dto;

import lombok.*;

import java.time.LocalDateTime;


@Value
@RequiredArgsConstructor
public class OrderDTO {

    Integer id;
    Integer userId;
    Integer certificateId;
    Float price;
    LocalDateTime purchaseDate;

}
