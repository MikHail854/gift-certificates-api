package ru.clevertec.ecl.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.ecl.repositories.GiftCertificateRepository;
import ru.clevertec.ecl.repositories.OrderRepository;
import ru.clevertec.ecl.repositories.UserRepository;
import ru.clevertec.ecl.dto.OrderDTO;
import ru.clevertec.ecl.entty.GiftCertificate;
import ru.clevertec.ecl.entty.Order;
import ru.clevertec.ecl.entty.User;
import ru.clevertec.ecl.mapper.OrderMapper;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private GiftCertificateRepository giftCertificateRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    public void testFindById() {
        final Order order = createOrderObject();
        final OrderDTO orderDTO = createOrderDTOObject();

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(orderMapper.toOrderDTO(order)).thenReturn(orderDTO);

        assertEquals(orderDTO, orderService.findById(order.getId()));
    }

    @Test
    public void testFindByIdThrowsEntityNotFoundException() {
        when(orderRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.findById(1));
    }

    @Test
    public void testFindOrderByIdAndUserId() {
        final Order order = createOrderObject();
        final OrderDTO orderDTO = createOrderDTOObject();
        final User user = createUserObject();

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(orderRepository.findByIdAndUserId(1, 1)).thenReturn(Optional.of(order));
        when(orderMapper.toOrderDTO(order)).thenReturn(orderDTO);

        assertEquals(orderDTO, orderService.findOrderByIdAndUserId(1, 1));
    }

    @Test
    public void testFindOrderByIdAndUserIdWhenOrderThrowsEntityNotFoundException() {
        when(userRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> orderService.findOrderByIdAndUserId(999, 1));
    }

    @Test
    public void testFindOrderByIdAndUserIdWhenUserThrowsEntityNotFoundException() {
        final User user = createUserObject();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(orderRepository.findByIdAndUserId(999, 1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.findOrderByIdAndUserId(1, 999));
    }

    @Test
    public void testFindOrdersByUserId() {
        final Order order = createOrderObject();
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        orders.add(order);
        orders.add(order);

        final OrderDTO orderDTO = createOrderDTOObject();

        final User user = createUserObject();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(orderRepository.findByUserId(1)).thenReturn(orders);
        when(orderMapper.toOrderDTO(order)).thenReturn(orderDTO);

        List<OrderDTO> orderDTOList = new ArrayList<>();
        orderDTOList.add(orderDTO);
        orderDTOList.add(orderDTO);
        orderDTOList.add(orderDTO);

        assertEquals(orderDTOList, orderService.findOrdersByUserId(1));
    }

    @Test
    public void tesFindOrdersByUserIdThrowsEntityNotFoundException() {
        when(userRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> orderService.findOrdersByUserId(999));
    }

//    @Test
//    public void testCreateOrder() {
//        final User user = createUserObject();
//        final GiftCertificate giftCertificate = createGiftCertificateObject();
//
//        when(userRepository.findById(1)).thenReturn(Optional.of(user));
//        when(giftCertificateRepository.findById(1)).thenReturn(Optional.of(giftCertificate));
//
//        final Order orderObject = createOrderObject();
//        final OrderDTO orderDTO = createOrderDTOObject();
//
//        when(orderRepository.saveAndFlush(Mockito.any(Order.class))).thenReturn(orderObject);
//        when(orderMapper.toOrderDTO(Mockito.any(Order.class))).thenReturn(orderDTO);
//
//        assertEquals(orderDTO, orderService.createOrder(1, 1, false));
//    }

//    @Test
//    public void testCreateOrderWhenUserThrowsEntityNotFoundException() {
//        final User user = createUserObject();
//        when(userRepository.findById(1)).thenReturn(Optional.of(user));
//
//        when(giftCertificateRepository.findById(999)).thenReturn(Optional.empty());
//        assertThrows(EntityNotFoundException.class, () -> orderService.createOrder(1, 999, false));
//    }
//
//    @Test
//    public void testCreateOrderWhenOrderThrowsEntityNotFoundException() {
//        when(userRepository.findById(999)).thenReturn(Optional.empty());
//        assertThrows(EntityNotFoundException.class, () -> orderService.createOrder(999, 1, false));
//    }

        private User createUserObject() {
        return User.builder()
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

    private GiftCertificate createGiftCertificateObject() {
        return GiftCertificate.builder()
                .id(1)
                .name("someNameCertificate")
                .description("someDescription")
                .price(1.2f)
                .duration(3)
                .createDate(LocalDateTime.now())
                .lastUpdateDate(LocalDateTime.now())
                .build();
    }

}
