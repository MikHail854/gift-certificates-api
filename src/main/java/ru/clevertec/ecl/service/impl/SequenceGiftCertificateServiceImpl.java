package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.service.SequenceGiftCertificateService;

@Slf4j
@Service
@RequiredArgsConstructor
public class SequenceGiftCertificateServiceImpl implements SequenceGiftCertificateService {

    private final JdbcTemplate jdbcTemplate;
    private static final String NEXT_VAL = "SELECT nextval('gift_certificate_id_seq');";
    private static final String SET_VAL = "SELECT setval('gift_certificate_id_seq', %s);";

    @Override
    public Integer getSequence() {
        return jdbcTemplate.queryForObject(NEXT_VAL, Integer.class);
    }

    @Override
    public void setSequence(Integer id) {
        final Integer sequence = jdbcTemplate.queryForObject(String.format(SET_VAL, id), Integer.class);
        log.info("sequence = {} set for gift_certificate_id_seq",sequence);
    }

}
