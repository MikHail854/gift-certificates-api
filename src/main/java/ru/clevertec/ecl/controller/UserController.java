package ru.clevertec.ecl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.ecl.dto.PageResponse;
import ru.clevertec.ecl.dto.UserDTO;
import ru.clevertec.ecl.service.UserService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    @ResponseBody
    public PageResponse<UserDTO> find(Pageable pageable){
        final Page<UserDTO> page = userService.findAll(pageable);
        return PageResponse.of(page);
    }

    @GetMapping("/{id}")
    public UserDTO findById(@PathVariable("id") int id){
        return userService.findById(id);
    }

}
