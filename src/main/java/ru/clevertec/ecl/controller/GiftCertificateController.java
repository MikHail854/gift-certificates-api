package ru.clevertec.ecl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.ecl.dto.*;
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
    public GiftCertificateDTO update(@PathVariable("id") int id, @RequestBody GiftCertificateDTO dto) {
        return giftCertificateService.update(id, dto);
    }

    @PatchMapping("/{id}/price")
    public GiftCertificateDTO updatePrice(@PathVariable("id") int id, @RequestBody GiftCertificatePriceDTO dto){
        return giftCertificateService.updatePrice(id, dto);
    }

    @PatchMapping("/{id}/duration")
    public GiftCertificateDTO updateDuration(@PathVariable("id") int id, @RequestBody GiftCertificateDurationDTO dto){
        return giftCertificateService.updateDuration(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id) {
        giftCertificateService.delete(id);
        return ResponseEntity.ok("Gift certificate deleted successfully");
    }

}

