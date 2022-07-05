package ru.clevertec.ecl.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagDTO {

    @Positive
    private Integer id;

    @NotBlank
    @Size(min = 2, max = 25, message = "Name should be between 2 and 25 characters")
    @Pattern(regexp = "^[-a-zA-Zа-яА-Я0-9]*$")
    private String name;

}
