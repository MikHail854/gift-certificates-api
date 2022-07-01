package ru.clevertec.ecl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.ecl.dto.PageResponse;
import ru.clevertec.ecl.dto.UserDTO;
import ru.clevertec.ecl.entty.User;
import ru.clevertec.ecl.service.UserService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    /**
     * Поиск всех пользователей
     *
     * @param pageable постраничный вывод
     * @return все найденные пользователи
     */
    @GetMapping
    @ResponseBody
    public PageResponse<UserDTO> findAllUsers(Pageable pageable) {
        final Page<UserDTO> page = userService.findAll(pageable);
        return PageResponse.of(page);
    }

    /**
     * Поиск пользователя по id
     *
     * @param id уникальный идентификатор пользователя
     * @return пользователь
     */
    @GetMapping("/{id}")
    public UserDTO findUserById(@PathVariable("id") int id) {
        return userService.findById(id);
    }

    /**
     * Создание нового пользователя
     *
     * @param user
     * @param saveToCommitLog
     * @return
     */
    @PostMapping("/create")
    public UserDTO createUser(@RequestBody User user,
                              @RequestParam(value = "save_to_commit_log", required = false) Boolean saveToCommitLog) {
        return userService.save(user, saveToCommitLog);
    }

    /**
     * Удаление существующего пользователя
     *
     * @param id
     * @param saveToCommitLog
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") int id,
                                        @RequestParam(value = "save_to_commit_log", required = false) Boolean saveToCommitLog) {
        userService.delete(id, saveToCommitLog);
        return ResponseEntity.ok("User deleted successfully");

    }

}
