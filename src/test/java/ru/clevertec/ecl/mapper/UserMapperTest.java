package ru.clevertec.ecl.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.clevertec.ecl.dto.OrderDTO;
import ru.clevertec.ecl.dto.UserDTO;
import ru.clevertec.ecl.entty.Order;
import ru.clevertec.ecl.entty.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserMapperTest {

    UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    public void testUserToUserDTOWithoutOrder() {
        final UserDTO userDTOFromMapper = userMapper.toUserDTO(createUserObject());
        final UserDTO userDTO = createUserDTOObject();
        assertEquals(userDTO, userDTOFromMapper);
    }

    @Test
    public void testUserToUserDTOWithOrder() {
        User user = createUserObject();
        user.setOrders(new ArrayList<Order>() {{
            add(createOrderObject());
        }});

        final UserDTO userDTOFromMapper = userMapper.toUserDTO(user);
        UserDTO userDTO = createUserDTOObject();
        userDTO.setOrders(new ArrayList<OrderDTO>() {{
            add(createOrderDTOObject());
        }});

        assertEquals(userDTO, userDTOFromMapper);
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

    private Order createOrderObject() {
        return Order.builder()
                .id(1)
                .userId(1)
                .certificateId(1)
                .price(123f)
                .purchaseDate(LocalDateTime.now())
                .build();
    }

    private OrderDTO createOrderDTOObject() {
        return OrderDTO.builder()
                .id(1)
                .userId(1)
                .certificateId(1)
                .price(123f)
                .purchaseDate(LocalDateTime.now())
                .build();
    }

}
