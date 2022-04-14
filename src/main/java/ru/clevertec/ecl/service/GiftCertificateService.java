package ru.clevertec.ecl.service;

import ru.clevertec.ecl.dto.GiftCertificateDTO;
import ru.clevertec.ecl.entty.GiftCertificate;

import java.util.List;

public interface GiftCertificateService {

    List<GiftCertificateDTO> findAll(String tagName);

    GiftCertificateDTO findById(int id);

    GiftCertificateDTO save(GiftCertificate giftCertificate);

    GiftCertificateDTO update(int id, GiftCertificateDTO giftCertificateDTO);

    void delete(int id);

    GiftCertificateDTO findGiftCertificateByTagName(String tagName);

    List<GiftCertificateDTO> findGiftCertificateByDescription(String description);

}
