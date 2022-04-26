package ru.clevertec.ecl.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GiftCertificateFilter {

    String name;
    String description;
    Float price;
    Integer duration;

}
