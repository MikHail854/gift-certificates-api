package ru.clevertec.ecl.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GiftCertificateDTO {

    @Positive
    private Integer id;

    @Pattern(regexp = "^[-a-zA-Zа-яА-Я]+(\\s+[-a-zA-Zа-яА-Я])*$")
    @Size(min = 2, max = 25, message = "Name should be between 2 and 25 characters")
    private String name;

    @Pattern(regexp = "^[-a-zA-Zа-яА-Я0-9]+(\\s+[-a-zA-Zа-яА-Я0-9])*$")
    private String description;

    @Positive
    private Float price;

    @Positive
    private Integer duration;

    @EqualsAndHashCode.Exclude
    private LocalDateTime createDate;

    @EqualsAndHashCode.Exclude
    private LocalDateTime lastUpdateDate;

    private List<TagDTO> tags;

}