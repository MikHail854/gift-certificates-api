package ru.clevertec.ecl.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.UserDTO;

public interface UserService {

    Page<UserDTO> findAll(Pageable pageable);

    UserDTO findById(int userId);

}
