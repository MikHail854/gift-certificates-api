package ru.clevertec.ecl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.ecl.dto.SequenceIdDTO;
import ru.clevertec.ecl.service.SequenceGiftCertificateService;
import ru.clevertec.ecl.service.SequenceOrderService;
import ru.clevertec.ecl.service.SequenceTagService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sequence")
public class SequenceController {

    private final SequenceOrderService sequenceService;
    private final SequenceTagService sequenceTagService;
    private final SequenceGiftCertificateService sequenceGiftCertificateService;

    @GetMapping("/orders")
    public Integer getSequenceOrder() {
        return sequenceService.getSequence();
    }

    @PutMapping("/orders")
    public void setSequenceOrder(@RequestBody SequenceIdDTO sequenceIdDTO) {
        sequenceService.setSequence(sequenceIdDTO.getId());
    }

    @GetMapping("/gifts")
    public Integer getSequenceGiftCertificate() {
        return sequenceGiftCertificateService.getSequence();
    }

    @PutMapping("/gifts")
    public void setSequenceGiftCertificate(@RequestBody SequenceIdDTO sequenceIdDTO) {
        sequenceGiftCertificateService.setSequence(sequenceIdDTO.getId());
    }

    @GetMapping("/tag")
    public Integer getSequenceTag() {
        return sequenceTagService.getSequence();
    }

    @PutMapping("/tag")
    public void setSequenceTag(@RequestBody SequenceIdDTO sequenceIdDTO) {
        sequenceTagService.setSequence(sequenceIdDTO.getId());
    }

}
