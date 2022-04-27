package ru.clevertec.ecl.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.GiftCertificateDTO;
import ru.clevertec.ecl.dto.GiftCertificateFilter;
import ru.clevertec.ecl.dto.GiftCertificatePriceAndDurationDTO;
import ru.clevertec.ecl.entty.GiftCertificate;

import java.util.List;

public interface GiftCertificateService {

    Page<GiftCertificateDTO> findAll(GiftCertificateFilter filter, Pageable pageable);

    GiftCertificateDTO findById(int id);

    GiftCertificateDTO save(GiftCertificate giftCertificate);

    GiftCertificateDTO update(int id, GiftCertificateDTO giftCertificateDTO);

    GiftCertificateDTO update(int id, GiftCertificatePriceAndDurationDTO giftCertificatePriceAndDurationDTO);

    void delete(int id);

    List<GiftCertificateDTO> findGiftCertificateByTagName(String tagName);

}
