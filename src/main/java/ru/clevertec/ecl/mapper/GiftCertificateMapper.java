package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.ecl.dto.GiftCertificateDTO;
import ru.clevertec.ecl.entty.GiftCertificate;

import java.util.List;

@Mapper
public interface GiftCertificateMapper {

    GiftCertificateDTO createGiftCertificate(GiftCertificate giftCertificate);

    List<GiftCertificateDTO> createGiftCertificateList(List<GiftCertificate> giftCertificates);

}
