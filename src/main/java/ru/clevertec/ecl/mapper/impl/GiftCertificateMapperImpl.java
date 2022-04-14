package ru.clevertec.ecl.mapper.impl;

import org.springframework.stereotype.Component;
import ru.clevertec.ecl.dto.GiftCertificateDTO;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.entty.GiftCertificate;
import ru.clevertec.ecl.mapper.GiftCertificateMapper;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GiftCertificateMapperImpl implements GiftCertificateMapper {

    @Override
    public GiftCertificateDTO createGiftCertificate(GiftCertificate giftCertificate) {
        if (giftCertificate == null) {
            return null;
        }

        if (giftCertificate.getTags() == null) {
            return GiftCertificateDTO.builder()
                    .id(giftCertificate.getId())
                    .name(giftCertificate.getName())
                    .description(giftCertificate.getDescription())
                    .price(giftCertificate.getPrice())
                    .duration(giftCertificate.getDuration())
                    .createDate(giftCertificate.getCreateDate())
                    .lastUpdateDate(giftCertificate.getLastUpdateDate())
                    .build();
        }

        return GiftCertificateDTO.builder()
                .id(giftCertificate.getId())
                .name(giftCertificate.getName())
                .description(giftCertificate.getDescription())
                .price(giftCertificate.getPrice())
                .duration(giftCertificate.getDuration())
                .createDate(giftCertificate.getCreateDate())
                .lastUpdateDate(giftCertificate.getLastUpdateDate())
                .tags(giftCertificate.getTags().stream().map(e ->
                        TagDTO.builder()
                                .id(e.getId())
                                .name(e.getName())
                                .build()
                ).collect(Collectors.toList()))
                .build();
    }

    @Override
    public List<GiftCertificateDTO> createGiftCertificateList(List<GiftCertificate> giftCertificates) {
        if (giftCertificates == null) {
            return null;
        }

        return giftCertificates.stream().map(e -> GiftCertificateDTO
                .builder()
                .id(e.getId())
                .name(e.getName())
                .description(e.getDescription())
                .price(e.getPrice())
                .duration(e.getDuration())
                .createDate(e.getCreateDate())
                .lastUpdateDate(e.getLastUpdateDate())
                .tags(e.getTags().stream().map(item ->
                        TagDTO.builder()
                                .id(item.getId())
                                .name(item.getName())
                                .build()
                ).collect(Collectors.toList()))
                .build()).collect(Collectors.toList());
    }
}
