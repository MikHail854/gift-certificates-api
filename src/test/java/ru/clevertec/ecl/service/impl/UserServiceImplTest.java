package ru.clevertec.ecl.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import ru.clevertec.ecl.dao.UserRepository;
import ru.clevertec.ecl.dto.UserDTO;
import ru.clevertec.ecl.entty.User;
import ru.clevertec.ecl.mapper.UserMapper;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testFindById() {
        final User user = createUserObject();
        final UserDTO userDTO = createUserDTOObject();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userMapper.toUserDTO(user)).thenReturn(userDTO);

        assertEquals(userDTO, userService.findById(user.getId()));
    }

    @Test
    public void testFindByIdThrowsEntityNotFoundException() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.findById(1));
    }

    @Test
    public void testFindAll(){
        final User user = createUserObject();
        final UserDTO userDTO = createUserDTOObject();
        ArrayList<User> list = new ArrayList<>();
        list.add(user);
        list.add(user);
        list.add(user);

        final Pageable pageable = getPageable(5, 0, Sort.by("firstName").ascending());
        Page<User> returnPage =  new PageImpl<>(list, pageable, list.size());

        when(userRepository.findAll(pageable)).thenReturn(returnPage );
        when(userMapper.toUserDTO(user)).thenReturn(userDTO);

        assertEquals(3, userService.findAll(pageable).getTotalElements());
    }

    private User createUserObject() {
        return User.builder()
                .id(1)
                .firstName("someFirstName")
                .lastName("someLastName")
                .build();
    }

    private UserDTO createUserDTOObject() {
        return UserDTO.builder()
                .id(1)
                .firstName("someFirstName")
                .lastName("someLastName")
                .build();
    }

    private Pageable getPageable(int size, int page, Sort sort) {
        return new Pageable() {
            @Override
            public int getPageNumber() {
                return page;
            }

            @Override
            public int getPageSize() {
                return size;
            }

            @Override
            public long getOffset() {
                return 0;
            }

            @Override
            public Sort getSort() {
                return sort;
            }

            @Override
            public Pageable next() {
                return null;
            }

            @Override
            public Pageable previousOrFirst() {
                return null;
            }

            @Override
            public Pageable first() {
                return null;
            }

            @Override
            public Pageable withPage(int pageNumber) {
                return null;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }
        };
    }

}
