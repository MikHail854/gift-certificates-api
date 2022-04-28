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


    @PostMapping
    public OrderDTO createOrder(@RequestParam("user_id") int userId, @RequestParam("certificate_id") int certificateId) {
        return orderService.createOrder(userId, certificateId);
    }

    @GetMapping()
    public List<OrderDTO> findOrdersByUserId(@RequestParam("user_id") Integer userId, @RequestParam(value = "order_id", required = false) Integer orderId) {
        return orderService.findOrdersByUserId(userId, orderId);
    }

    @GetMapping("/{id}")
    public OrderDTO findById(@PathVariable("id") int id){
        return orderService.findById(id);
    }


}
