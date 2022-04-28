package ru.clevertec.ecl.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GiftCertificateDTO {

    private Integer id;
    private String name;
    private String description;
    private Float price;
    private Integer duration;
    @EqualsAndHashCode.Exclude
    private LocalDateTime createDate;
    @EqualsAndHashCode.Exclude
    private LocalDateTime lastUpdateDate;

    private List<TagDTO> tags;

}