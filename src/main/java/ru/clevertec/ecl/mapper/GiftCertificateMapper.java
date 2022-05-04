package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.ecl.dto.GiftCertificateDTO;
import ru.clevertec.ecl.dto.GiftCertificateFilter;
import ru.clevertec.ecl.entty.GiftCertificate;

@Mapper(uses = {TagMapper.class}, componentModel = "spring")
public interface GiftCertificateMapper {

    GiftCertificateDTO toGiftCertificateDTO(GiftCertificate giftCertificate);

    GiftCertificate toGiftCertificate(GiftCertificateDTO giftCertificateDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "lastUpdateDate", ignore = true)
    GiftCertificate toGiftCertificate(GiftCertificateFilter filter);

}
