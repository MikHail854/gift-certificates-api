package ru.clevertec.ecl.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    Integer id;
    String firstName;
    String lastName;
    List<OrderDTO> orders;

}
