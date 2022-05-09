package ru.clevertec.ecl.intagration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestConstructor;
import ru.clevertec.ecl.dto.OrderDTO;
import ru.clevertec.ecl.service.OrderService;

import javax.persistence.EntityNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
public class OrderServiceIntegrationTest implements BaseIntegrationTest {

    private final OrderService orderService;

    @Test
    public void testFindById() {
        final OrderDTO orderDTO = orderService.findById(1);
        assertAll(
                () -> assertEquals(1, orderDTO.getUserId()),
                () -> assertEquals(2, orderDTO.getCertificateId()),
                () -> assertEquals(321, orderDTO.getPrice())
        );
    }

    @Test
    public void testFindByIdThrowsEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> orderService.findById(9999));
    }

    @Test
    public void testFindOrderByIdAndUserId(){
        final OrderDTO orderDTO = orderService.findOrderByIdAndUserId(1, 2);
        assertAll(
                () -> assertEquals(1, orderDTO.getUserId()),
                () -> assertEquals(1, orderDTO.getCertificateId()),
                () -> assertEquals(128.3f, orderDTO.getPrice())
        );
    }


    @Test
    public void testFindOrderByIdAndUserIdWhenOrderThrowsEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> orderService.findOrderByIdAndUserId(1, 999));
    }

    @Test
    public void testFindOrderByIdAndUserIdWhenUserThrowsEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> orderService.findOrderByIdAndUserId(999, 1));
    }

    @Test
    public void testFindOrdersByUserId(){
        final List<OrderDTO> ordersByUserId = orderService.findOrdersByUserId(1);
        assertAll(
                () -> assertEquals(2, ordersByUserId.size()),
                () -> assertEquals(2, ordersByUserId.get(0).getCertificateId()),
                () -> assertEquals(1, ordersByUserId.get(1).getCertificateId())
        );
    }

    @Test
    public void tesFindOrdersByUserIdThrowsEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> orderService.findOrdersByUserId(999));
    }


    @Test
    public void testCreateOrder(){
        final OrderDTO order = orderService.createOrder(4, 1);
        final OrderDTO byId = orderService.findById(order.getId());

        assertAll(
                () -> assertEquals(128.3f, byId.getPrice()),
                () -> assertEquals(4, byId.getUserId()),
                () -> assertEquals(1, byId.getCertificateId())
        );
    }

    @Test
    public void testCreateOrderWhenUserThrowsEntityNotFoundException(){
        assertThrows(EntityNotFoundException.class, () -> orderService.createOrder(999, 1));
    }

    @Test
    public void testCreateOrderWhenOrderThrowsEntityNotFoundException(){
        assertThrows(EntityNotFoundException.class, () -> orderService.createOrder(1, 999));
    }

}
