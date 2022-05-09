package ru.clevertec.ecl.intagration;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestConstructor;
import ru.clevertec.ecl.dto.UserDTO;
import ru.clevertec.ecl.service.UserService;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
public class UserServiceIntegrationTest implements BaseIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    public void testFindAll() {
        final Pageable pageable = getPageable(5, 0, Sort.by("firstName").ascending());
        final List<UserDTO> content = userService.findAll(pageable).getContent();
        assertAll(
                () -> assertEquals(4, content.size()),
                () -> assertEquals("Alex", content.get(0).getFirstName()),
                () -> assertEquals("Popov", content.get(0).getLastName()),
                () -> assertEquals("Petr", content.get(content.size() -1).getFirstName()),
                () -> assertEquals("Petrov", content.get(content.size() -1).getLastName())
        );
    }

    @Test
    public void testFindById() {
        final UserDTO userDTO = userService.findById(1);
        assertAll(
                () -> assertEquals("Ivan", userDTO.getFirstName()),
                () -> assertEquals("Ivanov", userDTO.getLastName())
        );
    }

    @Test
    public void testFindByIdThrowsEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> userService.findById(9999));
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
