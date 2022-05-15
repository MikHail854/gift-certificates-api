package ru.clevertec.ecl.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.ecl.entty.Order;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    Optional<Order> findByIdAndUserId(Integer orderId, @NotNull Integer userId);

    List<Order> findByUserId(@NotNull Integer userId);

}
