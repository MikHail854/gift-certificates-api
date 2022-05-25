package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.repositories.GiftCertificateRepository;
import ru.clevertec.ecl.repositories.OrderRepository;
import ru.clevertec.ecl.repositories.UserRepository;
import ru.clevertec.ecl.dto.OrderDTO;
import ru.clevertec.ecl.dto.OrderListDTO;
import ru.clevertec.ecl.entty.GiftCertificate;
import ru.clevertec.ecl.entty.Order;
import ru.clevertec.ecl.entty.User;
import ru.clevertec.ecl.mapper.OrderMapper;
import ru.clevertec.ecl.service.OrderService;

import javax.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.clevertec.ecl.constants.Constants.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final GiftCertificateRepository giftCertificateRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderDTO createOrder(int userId, int certificateId) {
        final User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(String.format(EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT, "user", userId)));

        final GiftCertificate giftCertificate = giftCertificateRepository.findById(certificateId).orElseThrow(
                () -> new EntityNotFoundException(String.format(EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT, "giftCertificate", certificateId)));

        final Order order = Order.builder()
                .certificateId(giftCertificate.getId())
                .price(giftCertificate.getPrice())
                .purchaseDate(LocalDateTime.now())
                .userId(user.getId())
                .build();

        orderRepository.saveAndFlush(order);
        final OrderDTO orderDTO = orderMapper.toOrderDTO(order);
        log.info("create order: {}", orderDTO);
        return orderDTO;
    }

    @Override
    public OrderListDTO findOrdersByUserId(Integer userId) {
        if (userRepository.findById(userId).isPresent()) {
            final List<OrderDTO> dto = orderRepository.findByUserId(userId).stream()
                    .map(orderMapper::toOrderDTO)
                    .collect(Collectors.toList());
            log.info("found orders - {}", dto);
            return OrderListDTO.builder().orderDTOList(dto).build();
        }
        throw new EntityNotFoundException(String.format(EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT, "user", userId));
    }

    @Override
    public OrderDTO findOrderByIdAndUserId(Integer userId, Integer orderId) {
        if (userRepository.findById(userId).isPresent()) {
            final OrderDTO dto = orderRepository.findByIdAndUserId(orderId, userId).map(orderMapper::toOrderDTO)
                    .orElseThrow(() -> new EntityNotFoundException(String.format(EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT, "order", orderId)));
            log.info("found orders - {}", dto);
            return dto;
        }
        throw new EntityNotFoundException(String.format(EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT, "user", userId));
    }

    @Override
    public OrderDTO findById(int id) {
        final OrderDTO dto = orderRepository.findById(id).map(orderMapper::toOrderDTO)
                .orElseThrow(() -> new EntityNotFoundException(String.format(EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT, "order", id)));
        log.info("found order - {}", dto);
        return dto;
    }

}
