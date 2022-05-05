package ru.clevertec.ecl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.ecl.dto.PageResponse;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.entty.Tag;
import ru.clevertec.ecl.service.TagService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    /**
     * Поиск всех тегов
     *
     * @param pageable постраничный вывод
     * @return все найденные теги
     */
    @GetMapping
    public PageResponse<TagDTO> fidAll(Pageable pageable) {
        final Page<TagDTO> page = tagService.findAll(pageable);
        return PageResponse.of(page);
    }

    /**
     * Поиск тега по id
     *
     * @param id уникальный идентификатор тега
     * @return тег
     */
    @GetMapping("/{id}")
    public TagDTO getById(@PathVariable("id") int id) {
        return tagService.findById(id);
    }

    /**
     * Сохранение тега
     *
     * @param tag тег
     * @return сохраненный тег
     */
    @PostMapping
    public TagDTO save(@RequestBody @Valid Tag tag) {
        return tagService.save(tag);
    }

    /**
     * Обновление данных существующего тега
     *
     * @param id  уникальный идентификатор тега
     * @param tag новые значения
     * @return обновленный тег
     */
    @PutMapping("/{id}")
    public TagDTO update(@PathVariable("id") int id, @RequestBody @Valid TagDTO tag) {
        return tagService.update(id, tag);
    }

    /**
     * Удаление существующего тега
     *
     * @param id муникальный идентификатор тега
     * @return статус 200, если удаление произведено успешно
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id) {
        tagService.delete(id);
        return ResponseEntity.ok("Tag deleted successfully");
    }

    /**
     * Поиск самого частоиспользуемого тега пользовтеля, у которого сумма всех заказов самая большая
     *
     * @return самый частоиспользуемый тег
     */
    @GetMapping("/super_tag")
    public TagDTO getTheMostWidelyUsedTag() {
        return tagService.findTheMostWidelyUsedTag();
    }

}
