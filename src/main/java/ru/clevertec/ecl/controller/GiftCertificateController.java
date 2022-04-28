package ru.clevertec.ecl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.ecl.dto.GiftCertificateDTO;
import ru.clevertec.ecl.dto.GiftCertificateFilter;
import ru.clevertec.ecl.dto.GiftCertificatePriceAndDurationDTO;
import ru.clevertec.ecl.dto.PageResponse;
import ru.clevertec.ecl.entty.GiftCertificate;
import ru.clevertec.ecl.service.GiftCertificateService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/gifts")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    @GetMapping
    @ResponseBody
    public PageResponse<GiftCertificateDTO> findAll(GiftCertificateFilter filter, Pageable pageable) {
        final Page<GiftCertificateDTO> page = giftCertificateService.findAll(filter, pageable);
        return PageResponse.of(page);
    }

    @GetMapping("/{id}")
    public GiftCertificateDTO findById(@PathVariable("id") int id) {
        return giftCertificateService.findById(id);
    }

    @GetMapping("/tags")
    public List<GiftCertificateDTO> findByTagName(@RequestParam("tag_name") String tagName){
        return giftCertificateService.findGiftCertificateByTagName(tagName);
    }

    @PostMapping
    public GiftCertificateDTO save(@RequestBody GiftCertificate giftCertificate) {
        return giftCertificateService.save(giftCertificate);
    }


    @PutMapping("/{id}")
    public GiftCertificateDTO update(@PathVariable("id") int id, @RequestBody GiftCertificateDTO giftCertificate) {
        return giftCertificateService.update(id, giftCertificate);
    }

    @PatchMapping("/{id}")
    public GiftCertificateDTO updatePriceAndDuration(@PathVariable("id") int id, @RequestBody GiftCertificatePriceAndDurationDTO giftCertificate){
        return giftCertificateService.update(id, giftCertificate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id) {
        giftCertificateService.delete(id);
        return ResponseEntity.ok("Gift certificate deleted successfully");
    }

}

