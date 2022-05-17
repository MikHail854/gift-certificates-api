package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.service.SequenceService;

@Slf4j
@Service
@RequiredArgsConstructor
public class SequenceServiceImpl implements SequenceService {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Integer getSequence() {
        return jdbcTemplate.queryForObject("SELECT nextval('order_data_id_seq');", Integer.class);
    }

    @Override
    public void setSequence(Integer id) {
        final Integer sequence = jdbcTemplate.queryForObject(String.format("SELECT setval('order_data_id_seq', %s);", id), Integer.class);
        log.info("order_data_id_seq sequence = {}",sequence);
    }

}