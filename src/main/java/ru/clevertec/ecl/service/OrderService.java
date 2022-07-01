package ru.clevertec.ecl.service;

import ru.clevertec.ecl.dto.InputDataOrderDTO;
import ru.clevertec.ecl.dto.OrderDTO;
import ru.clevertec.ecl.dto.OrderListDTO;


public interface OrderService {

    OrderDTO createOrder(InputDataOrderDTO inputDataOrder, Boolean saveToCommitLog);

    OrderListDTO findOrdersByUserId(Integer userId);

    OrderDTO findOrderByIdAndUserId(Integer userId, Integer orderId);

    OrderDTO findById(int id);

}
