package ru.clevertec.ecl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.ecl.dto.*;
import ru.clevertec.ecl.entty.GiftCertificate;
import ru.clevertec.ecl.service.GiftCertificateService;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/gifts")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    /**
     * Поиск всех подарочных сертификатов
     *
     * @param filter   фильтрация по имени и/или описанию (фильтр может отсутствовать)
     * @param pageable постраничный вывод
     * @return все найденные подарояные сертификаты
     */
    @GetMapping
    @ResponseBody
    public PageResponse<GiftCertificateDTO> getAllGiftCertificates(GiftCertificateFilter filter, Pageable pageable) {
        final Page<GiftCertificateDTO> page = giftCertificateService.findAll(filter, pageable);
        return PageResponse.of(page);
    }

    /**
     * Поиск подарочного сертификата по id
     *
     * @param id уникальный идентификатор подарочного сертификата
     * @return подарояный сертификат
     */
    @GetMapping("/{id}")
    public GiftCertificateDTO getGiftCertificateById(@PathVariable("id") int id) {
        return giftCertificateService.findById(id);
    }

    /**
     * Поиск подарочных сертификатов по имени тегов.
     *
     * @param tagName имя тега
     * @return список подарочных сертификатов
     */
    @GetMapping("/tags")
    public List<GiftCertificateDTO> findGiftCertificateByTagName(@RequestParam("tag_name") String tagName) {
        return giftCertificateService.findGiftCertificateByTagName(tagName);
    }

    /**
     * Создание нового подарочного сертификата
     *
     * @param giftCertificate подарочный сертификат
     * @return сохраненный подарочный сертификат
     */
    @PostMapping("/create")
    public GiftCertificateDTO createGiftCertificate(@RequestBody @Valid GiftCertificate giftCertificate,
                                   @RequestParam(value = "save_to_commit_log", required = false) Boolean saveToCommitLog) {
        return giftCertificateService.save(giftCertificate, saveToCommitLog);
    }

    /**
     * Обновление данных существующего подарочного сертификата
     *
     * @param id  уникальный идентификатор подарочного сертификата
     * @param dto новые значения
     * @return обновленный подарочный сертификат
     */
    @PutMapping("/update/{id}")
    public GiftCertificateDTO updateGiftCertificate(@PathVariable("id") int id, @RequestBody @Valid GiftCertificateDTO dto,
                                     @RequestParam(value = "save_to_commit_log", required = false) Boolean saveToCommitLog) {
        return giftCertificateService.update(id, dto, saveToCommitLog);
    }

    /**
     * Обновление стоимости подарочного сертификата
     *
     * @param id  уникальный идентификатор подарочного сертификата
     * @param dto новые значения
     * @return обновленный подарочный сертификат
     */
    @PutMapping("/update/{id}/price")
    public GiftCertificateDTO updateGiftCertificatePrice(@PathVariable("id") int id, @RequestBody @Valid GiftCertificatePriceDTO dto,
                                          @RequestParam(value = "save_to_commit_log", required = false) Boolean saveToCommitLog) {
        return giftCertificateService.updatePrice(id, dto, saveToCommitLog);
    }

    /**
     * Обновление срока действия подарочного сертификата
     *
     * @param id  уникальный идентификатор подарочного сертификата
     * @param dto новые значения
     * @return обновленный подарочный сертификат
     */
    @PutMapping("/update/{id}/duration")
    public GiftCertificateDTO updateGiftCertificateDuration(@PathVariable("id") int id, @RequestBody @Valid GiftCertificateDurationDTO dto,
                                             @RequestParam(value = "save_to_commit_log", required = false) Boolean saveToCommitLog) {
        return giftCertificateService.updateDuration(id, dto, saveToCommitLog);
    }

    /**
     * Удаление существующего подарочного сертификата
     *
     * @param id уникальный идентификатор подарочного сертификата
     * @return статус 200, если удаление произведено успешно
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteGiftCertificate(@PathVariable("id") int id,
                                    @RequestParam(value = "save_to_commit_log", required = false) Boolean saveToCommitLog) {
        giftCertificateService.delete(id, saveToCommitLog);
        return ResponseEntity.ok("Gift certificate deleted successfully");
    }

    /**
     * Проверка наличия сертификата в базе данных
     *
     * @param id уникальный идентификатор подарочного сертификата
     * @return true если сертификат найден в базе данных
     */
    @GetMapping("/check")
    public Boolean checkGiftCertificate(@RequestParam("id") int id) {
        return giftCertificateService.checkGift(id);
    }

}

