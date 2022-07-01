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

    GiftCertificateDTO save(GiftCertificate giftCertificate, Boolean saveToCommitLog);

    GiftCertificateDTO update(int id, GiftCertificateDTO giftCertificateDTO, Boolean saveToCommitLog);

    GiftCertificateDTO updatePrice(int id, GiftCertificatePriceDTO giftCertificatePriceAndDurationDTO, Boolean saveToCommitLog);

    GiftCertificateDTO updateDuration(int id, GiftCertificateDurationDTO giftCertificateDurationDTO, Boolean saveToCommitLog);

    void delete(int id, Boolean saveToCommitLog);

    List<GiftCertificateDTO> findGiftCertificateByTagName(String tagName);

    boolean checkGift(int id);

}
