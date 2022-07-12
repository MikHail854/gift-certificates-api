package ru.clevertec.ecl.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dto.*;
import ru.clevertec.ecl.entty.GiftCertificate;
import ru.clevertec.ecl.entty.Order;
import ru.clevertec.ecl.entty.User;
import ru.clevertec.ecl.mapper.OrderMapper;
import ru.clevertec.ecl.repositories.GiftCertificateRepository;
import ru.clevertec.ecl.repositories.OrderRepository;
import ru.clevertec.ecl.repositories.UserRepository;
import ru.clevertec.ecl.service.CommitLogService;
import ru.clevertec.ecl.service.OrderService;
import ru.clevertec.ecl.service.SmsSender;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.clevertec.ecl.constants.Constants.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    @Value("${server.port}")
    private final String localPort;

    private final SmsSender smsSender;
    private final ObjectMapper mapper;
    private final OrderMapper orderMapper;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final CommitLogService commitLogService;
    private final GiftCertificateRepository giftCertificateRepository;

    @Override
    @Transactional
    public OrderDTO createOrder(InputDataOrderDTO inputDataOrder, Boolean saveToCommitLog) {
        final User user = userRepository.findById(inputDataOrder.getUserId()).orElseThrow(
                () -> new EntityNotFoundException(String.format(EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT, USER, inputDataOrder.getUserId())));

        final GiftCertificate giftCertificate = giftCertificateRepository.findById(inputDataOrder.getCertificateId()).orElseThrow(
                () -> new EntityNotFoundException(String.format(EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT, GIFT_CERTIFICATE, inputDataOrder.getCertificateId())));

        final Order order = Order.builder()
                .certificateId(giftCertificate.getId())
                .price(giftCertificate.getPrice())
                .purchaseDate(LocalDateTime.now())
                .userId(user.getId())
                .build();

        orderRepository.saveAndFlush(order);
        final OrderDTO orderDTO = orderMapper.toOrderDTO(order);
        log.info("create order: {}", orderDTO);
        if (Objects.isNull(saveToCommitLog) || saveToCommitLog) {
            sendToCommitLogSave(orderDTO.getId(), inputDataOrder);
            smsSender.sendSms(user.getPhone(), String.format(ORDER_SUCCESSFULLY_GENERATED, giftCertificate.getPrice()), SMS_SENDER);
        }
        return orderDTO;
    }

    @SneakyThrows
    private void sendToCommitLogSave(Integer id, InputDataOrderDTO inputDataOrder) {
        commitLogService.sendOrderToCommitLog(CommitLogDTO.builder()
                .id(id)
                .url(URL_CREATE_ORDER)
                .method(HttpMethod.POST)
                .body(mapper.writeValueAsString(inputDataOrder))
                .typeObject(TypeObject.ORDER)
                .portInitiatorLog(localPort)
                .build());
    }

    @Override
    @Cacheable(value = ORDER, sync = true)
    public OrderListDTO findOrdersByUserId(Integer userId) {
        if (userRepository.findById(userId).isPresent()) {
            final List<OrderDTO> dto = orderRepository.findByUserId(userId).stream()
                    .map(orderMapper::toOrderDTO)
                    .collect(Collectors.toList());
            log.info("found orders - {}", dto);
            return OrderListDTO.builder().orderDTOList(dto).build();
        }
        throw new EntityNotFoundException(String.format(EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT, USER, userId));
    }

    @Override
    @Cacheable(value = ORDER, sync = true)
    public OrderDTO findOrderByIdAndUserId(Integer userId, Integer orderId) {
        if (userRepository.findById(userId).isPresent()) {
            final OrderDTO dto = orderRepository.findByIdAndUserId(orderId, userId).map(orderMapper::toOrderDTO)
                    .orElseThrow(() -> new EntityNotFoundException(String.format(EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT, ORDER, orderId)));
            log.info("found orders - {}", dto);
            return dto;
        }
        throw new EntityNotFoundException(String.format(EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT, USER, userId));
    }

    @Override
    @Cacheable(value = ORDER, sync = true)
    public OrderDTO findById(int id) {
        final OrderDTO dto = orderRepository.findById(id).map(orderMapper::toOrderDTO)
                .orElseThrow(() -> new EntityNotFoundException(String.format(EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT, ORDER, id)));
        log.info("found order - {}", dto);
        return dto;
    }

}