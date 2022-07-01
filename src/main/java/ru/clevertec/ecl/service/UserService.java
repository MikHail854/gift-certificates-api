package ru.clevertec.ecl.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.UserDTO;
import ru.clevertec.ecl.entty.User;

public interface UserService {

    Page<UserDTO> findAll(Pageable pageable);

    UserDTO findById(int userId);

    UserDTO save(User user, Boolean saveToCommitLog);

    void delete(int id, Boolean saveToCommitLog);
}
