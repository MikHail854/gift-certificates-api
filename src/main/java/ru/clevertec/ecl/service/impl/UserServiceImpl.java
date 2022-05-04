package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dao.UserRepository;
import ru.clevertec.ecl.dto.UserDTO;
import ru.clevertec.ecl.mapper.UserMapper;
import ru.clevertec.ecl.service.UserService;

import javax.persistence.EntityNotFoundException;

import static ru.clevertec.ecl.constants.Constants.EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Page<UserDTO> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toUserDTO);
    }

    @Override
    public UserDTO findById(int userId) {
        final UserDTO userDTO = userRepository.findById(userId).map(userMapper::toUserDTO)
                .orElseThrow(() -> new EntityNotFoundException(String.format(EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT, "user", userId)));
        log.info("found user - {}", userDTO);
        return userDTO;
    }

}
