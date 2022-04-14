package ru.clevertec.ecl.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.ecl.constants.Constants;
import ru.clevertec.ecl.dto.ErrorMsg;
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

    @GetMapping()
    public ResponseEntity<?> findAll(@RequestParam(value = "tag_name", required = false, defaultValue = "") String tagName,
                                     @RequestParam(value = "description", required = false, defaultValue = "") String description) {
        if (!tagName.isEmpty()) {
            final GiftCertificateDTO giftCertificateByTagName = giftCertificateService.findGiftCertificateByTagName(tagName);
            return ResponseEntity.ok(giftCertificateByTagName != null ? giftCertificateByTagName : new ErrorMsg()
                    .setError(Constants.NOT_FOUND_MSG));
        } else if (!description.isEmpty()) {
            final List<GiftCertificateDTO> giftCertificateByDescription = giftCertificateService.findGiftCertificateByDescription(description);
            return ResponseEntity.ok(!giftCertificateByDescription.isEmpty() ? giftCertificateByDescription : new ErrorMsg()
                    .setError(Constants.NOT_FOUND_MSG).setErrorDescription(Constants.NOT_FOUND_MSG));
        } else {
            final List<GiftCertificateDTO> giftCertificates = giftCertificateService.findAll(tagName);
            return ResponseEntity.ok(!giftCertificates.isEmpty() ? giftCertificates : new ErrorMsg()
                    .setError(Constants.NOT_FOUND_MSG).setErrorDescription(Constants.DATABASE_IS_EMPTY_MSG));
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") int id) {
        final GiftCertificateDTO giftCertificate = giftCertificateService.findById(id);
        return ResponseEntity.ok(giftCertificate != null ? giftCertificate : new ErrorMsg()
                .setError(Constants.NOT_FOUND_MSG).setErrorDescription("Gift certificate with id = " + id + " not exist!"));
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody GiftCertificate giftCertificate) {
        final GiftCertificateDTO save = giftCertificateService.save(giftCertificate);
        log.info("POST giftCertificate: {}", giftCertificate);
        return new ResponseEntity<>(save, HttpStatus.OK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody GiftCertificateDTO giftCertificate) {
        try {
            final GiftCertificateDTO update = giftCertificateService.update(id, giftCertificate);
            log.info("Updated giftCertificate with id: {}, giftCertificate: {}", id, giftCertificate);
            return ResponseEntity.ok(update != null ? update : new ErrorMsg()
                    .setError(Constants.NOT_FOUND_MSG).setErrorDescription(Constants.NOT_FOUND_MSG));
        } catch (Exception e) {
            log.error("Exception while updating giftCertificate with id: {}, giftCertificate: {}. Exception: {}", id, giftCertificate, e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id) {
        try {
            giftCertificateService.delete(id);
            log.info("removed gift certificate with id = {}", id);
            return ResponseEntity.ok("Gift certificate deleted successfully");
        } catch (Exception e) {
            log.error("Gift certificate with id {} not exist!", id);
            return new ResponseEntity<>(Constants.NOT_REMOVED_MSG, HttpStatus.BAD_REQUEST);
        }

    }

}

