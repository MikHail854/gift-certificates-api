package ru.clevertec.ecl.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dto.CommitLogDTO;
import ru.clevertec.ecl.dto.TypeObject;
import ru.clevertec.ecl.dto.UserDTO;
import ru.clevertec.ecl.entty.User;
import ru.clevertec.ecl.mapper.UserMapper;
import ru.clevertec.ecl.repositories.UserRepository;
import ru.clevertec.ecl.service.CommitLogService;
import ru.clevertec.ecl.service.UserService;

import javax.persistence.EntityNotFoundException;
import java.util.Objects;

import static ru.clevertec.ecl.constants.Constants.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    @Value("${server.port}")
    private final String localPort;

    private final ObjectMapper mapper;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final CommitLogService commitLogService;

    @Override
    public Page<UserDTO> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toUserDTO);
    }

    @Override
    public UserDTO findById(int userId) {
        final UserDTO userDTO = userRepository.findById(userId).map(userMapper::toUserDTO)
                .orElseThrow(() -> new EntityNotFoundException(String.format(EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT, USER, userId)));
        log.info("found user - {}", userDTO);
        return userDTO;
    }

    @Override
    @Transactional
    public UserDTO save(User user, Boolean saveToCommitLog) {
        log.info("user to save to database - {}", user);
        final UserDTO saved = userMapper.toUserDTO(userRepository.save(user));
        log.info("successful saving of the tag in the database - {}", saved);
        if (Objects.isNull(saveToCommitLog) || saveToCommitLog) {
            sendToCommitLogSave(user);
        }
        return saved;
    }

    @SneakyThrows
    private void sendToCommitLogSave(User user) {
        commitLogService.sendToCommitLog(CommitLogDTO.builder()
                .url(URL_CREATE_USER)
                .method(HttpMethod.POST)
                .body(mapper.writeValueAsString(user))
                .typeObject(TypeObject.USER)
                .portInitiatorLog(localPort)
                .build());
    }

    @Override
    @Transactional
    public void delete(int id, Boolean saveToCommitLog) {
        userRepository.deleteById(id);
        log.info("user with id = {} deleted successfully", id);
        if (Objects.isNull(saveToCommitLog) || saveToCommitLog) {
            sendToCommitLogDelete(id);
        }
    }

    private void sendToCommitLogDelete(int id) {
        commitLogService.sendToCommitLog(CommitLogDTO.builder()
                .id(id)
                .url(URL_DELETE_USER)
                .method(HttpMethod.DELETE)
                .typeObject(TypeObject.USER)
                .portInitiatorLog(localPort)
                .build());
    }

}
