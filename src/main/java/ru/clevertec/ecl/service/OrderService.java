package ru.clevertec.ecl.service;

import ru.clevertec.ecl.dto.OrderDTO;
import ru.clevertec.ecl.dto.OrderListDTO;


public interface OrderService {

    OrderDTO createOrder(int userId, int certificateId);

    OrderListDTO findOrdersByUserId(Integer userId);

    OrderDTO findOrderByIdAndUserId(Integer userId, Integer orderId);

    OrderDTO findById(int id);

}
