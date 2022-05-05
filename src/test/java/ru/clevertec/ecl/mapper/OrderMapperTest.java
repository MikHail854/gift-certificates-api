package ru.clevertec.ecl.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.clevertec.ecl.dto.OrderDTO;
import ru.clevertec.ecl.entty.Order;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderMapperTest {

    OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);

    @Test
    public void testOrderToOrderDTO(){
        final OrderDTO orderDTOFromMapper = orderMapper.toOrderDTO(createOrderObject());
        final OrderDTO orderDTO = createOrderDTOObject();
        assertEquals(orderDTO, orderDTOFromMapper);
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
