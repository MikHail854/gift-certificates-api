package ru.clevertec.ecl.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.ecl.dto.GiftCertificateDTO;
import ru.clevertec.ecl.entty.GiftCertificate;
import ru.clevertec.ecl.service.GiftCertificateService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/gift")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    @GetMapping
    @ResponseBody
    public List<GiftCertificateDTO> findAll(@RequestParam(value = "tag_name", required = false, defaultValue = "") String tagName,
                                            @RequestParam(value = "description", required = false, defaultValue = "") String description) {
        if (!tagName.isEmpty()) {
            return giftCertificateService.findGiftCertificateByTagName(tagName);
        } else if (!description.isEmpty()) {
            return giftCertificateService.findGiftCertificateByDescription(description);
        }
        return giftCertificateService.findAll();
    }

    @GetMapping("/{id}")
    public GiftCertificateDTO findById(@PathVariable("id") int id) {
        return giftCertificateService.findById(id);
    }

    @PostMapping
    public GiftCertificateDTO save(@RequestBody GiftCertificate giftCertificate) {
        return giftCertificateService.save(giftCertificate);
    }


    @PutMapping("/{id}")
    public GiftCertificateDTO update(@PathVariable("id") int id, @RequestBody GiftCertificateDTO giftCertificate) {
        return giftCertificateService.update(id, giftCertificate);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id) {
        giftCertificateService.delete(id);
        return ResponseEntity.ok("Gift certificate deleted successfully");
    }

}

