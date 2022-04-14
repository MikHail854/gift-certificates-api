package ru.clevertec.ecl.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GiftCertificateDTO {

    private int id;
    private String name;
    private String description;
    private float price;
    private int duration;
    private Date createDate;
    private Date lastUpdateDate;

    private List<TagDTO> tags;

}