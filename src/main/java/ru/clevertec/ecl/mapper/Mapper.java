package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapping;
import ru.clevertec.ecl.dto.GiftCertificateDTO;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.entty.GiftCertificate;
import ru.clevertec.ecl.entty.Tag;


@org.mapstruct.Mapper(componentModel = "spring")
public interface Mapper {

    @Mapping(target = "giftCertificates", ignore = true)
    TagDTO tagToTagDTO(Tag tag);

    @Mapping(target = "giftCertificates", ignore = true)
    Tag tagDTOToTag(TagDTO tagDTO);

    GiftCertificateDTO giftCertificateToGiftCertificateDTO(GiftCertificate giftCertificate);

    GiftCertificate giftCertificateDTOToGiftCertificate(GiftCertificateDTO giftCertificateDTO);

}
