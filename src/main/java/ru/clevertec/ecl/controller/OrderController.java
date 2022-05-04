package ru.clevertec.ecl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.ecl.dto.OrderDTO;
import ru.clevertec.ecl.service.OrderService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    /**
     * Создание нового заказа
     *
     * @param userId        уникальный идентификатор пользователя
     * @param certificateId уникальный идентификатор подарочного сертификата
     * @return созданный заказ
     */
    @PostMapping
    public OrderDTO createOrder(@RequestParam("user_id") int userId, @RequestParam("certificate_id") int certificateId) {
        return orderService.createOrder(userId, certificateId);
    }

    /**
     * Поиск всех заказов по одному конкретному пользователю
     *
     * @param userId уникальный идентификатор пользователя
     * @return список заказов одного пользователя
     */
    @GetMapping
    public List<OrderDTO> findOrdersByUserId(@RequestParam("user_id") Integer userId) {
        return orderService.findOrdersByUserId(userId);
    }

    /**
     * Поиск конкретного заказа по одному конкретному пользователю
     *
     * @param userId  уникальный идентификатор пользователя
     * @param orderId уникальный идентификатор заказа
     * @return найденный заказ пользовтеля
     */
    @GetMapping("/find")
    public OrderDTO findOrderByIdAndUserId(@RequestParam("user_id") Integer userId, @RequestParam(value = "order_id") Integer orderId) {
        return orderService.findOrderByIdAndUserId(userId, orderId);
    }

    /**
     * Поиск заказа одного заказа
     *
     * @param id уникальный идентификатор заказа
     * @return найденный заказ
     */
    @GetMapping("/{id}")
    public OrderDTO findById(@PathVariable("id") int id) {
        return orderService.findById(id);
    }


}
