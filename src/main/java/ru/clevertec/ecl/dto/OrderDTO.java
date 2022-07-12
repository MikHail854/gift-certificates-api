package ru.clevertec.ecl.dto;

import lombok.*;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    @Positive
    private Integer id;
    @Positive
    private Integer userId;
    @Positive
    private Integer certificateId;
    @Positive
    private Float price;
    @EqualsAndHashCode.Exclude
    private LocalDateTime purchaseDate;

}
