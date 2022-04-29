package ru.clevertec.ecl.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.GiftCertificateDTO;
import ru.clevertec.ecl.dto.GiftCertificateDurationDTO;
import ru.clevertec.ecl.dto.GiftCertificateFilter;
import ru.clevertec.ecl.dto.GiftCertificatePriceDTO;
import ru.clevertec.ecl.entty.GiftCertificate;

import java.util.List;

public interface GiftCertificateService {

    Page<GiftCertificateDTO> findAll(GiftCertificateFilter filter, Pageable pageable);

    GiftCertificateDTO findById(int id);

    GiftCertificateDTO save(GiftCertificate giftCertificate);

    GiftCertificateDTO update(int id, GiftCertificateDTO giftCertificateDTO);

    GiftCertificateDTO updatePrice(int id, GiftCertificatePriceDTO giftCertificatePriceAndDurationDTO);

    GiftCertificateDTO updateDuration(int id, GiftCertificateDurationDTO giftCertificateDurationDTO);

    void delete(int id);

    List<GiftCertificateDTO> findGiftCertificateByTagName(String tagName);

}
