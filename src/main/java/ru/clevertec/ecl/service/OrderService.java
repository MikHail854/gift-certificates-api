package ru.clevertec.ecl.service;

import ru.clevertec.ecl.dto.OrderDTO;

import java.util.List;

public interface OrderService {

    OrderDTO createOrder(int userId, int certificateId);

    List<OrderDTO> findOrdersByUserId(Integer userId);

    OrderDTO findOrderByIdAndUserId(Integer userId, Integer orderId);

    OrderDTO findById(int id);

}
