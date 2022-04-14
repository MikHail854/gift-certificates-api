package ru.clevertec.ecl.mapper.impl;

import org.springframework.stereotype.Component;
import ru.clevertec.ecl.dto.GiftCertificateDTO;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.entty.Tag;
import ru.clevertec.ecl.mapper.TagMapper;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TagMapperImpl implements TagMapper {

    @Override
    public TagDTO createTag(Tag tag) {
        if (tag == null) {
            return null;
        }

        if (tag.getGiftCertificates() == null) {
            return TagDTO.builder()
                    .id(tag.getId())
                    .name(tag.getName())
                    .build();
        }

        return TagDTO.builder()
                .id(tag.getId())
                .name(tag.getName())
                .giftCertificates(tag.getGiftCertificates().stream().map(e ->
                        GiftCertificateDTO.builder()
                                .id(e.getId())
                                .name(e.getName())
                                .description(e.getDescription())
                                .price(e.getPrice())
                                .duration(e.getDuration())
                                .createDate(e.getCreateDate())
                                .lastUpdateDate(e.getLastUpdateDate())
                                .build()
                ).collect(Collectors.toList()))
                .build();
    }

    @Override
    public List<TagDTO> createTagList(List<Tag> tags) {
        if (tags == null) {
            return null;
        }

        return tags.stream().map(e -> TagDTO
                .builder()
                .id(e.getId())
                .name(e.getName())
                .giftCertificates(e.getGiftCertificates().stream().map(item ->
                        GiftCertificateDTO.builder()
                                .id(item.getId())
                                .name(item.getName())
                                .description(item.getDescription())
                                .price(item.getPrice())
                                .duration(item.getDuration())
                                .createDate(item.getCreateDate())
                                .lastUpdateDate(item.getLastUpdateDate())
                                .build()).collect(Collectors.toList()))
                .build()).collect(Collectors.toList());
    }
}
