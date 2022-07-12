package ru.clevertec.ecl.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Pattern;

@Value
@Builder
public class GiftCertificateFilter {

    @Pattern(regexp = "^[-a-zA-Zа-яА-Я0-9 ]*$")
    String name;

    @Pattern(regexp = "^[-a-zA-Zа-яА-Я0-9 ]*$")
    String description;

}
