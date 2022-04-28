package ru.clevertec.ecl.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.ecl.entty.Order;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByIdAndUserId(Integer orderId, @NotNull Integer userId);

    List<Order> findByUserId(@NotNull Integer userId);

}
