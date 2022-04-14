package ru.clevertec.ecl.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.ecl.constants.Constants;
import ru.clevertec.ecl.dto.ErrorMsg;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.entty.Tag;
import ru.clevertec.ecl.service.TagService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/tag")
public class TagController {

    private final TagService tagService;

    @GetMapping
    public ResponseEntity<?> fidAll() {
        final List<TagDTO> tags = tagService.findAll();
        return ResponseEntity.ok(!tags.isEmpty() ? tags : new ErrorMsg()
                .setError(Constants.NOT_FOUND_MSG).setErrorDescription(Constants.DATABASE_IS_EMPTY_MSG));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") int id) {
        final TagDTO tag = tagService.findById(id);
        return ResponseEntity.ok(tag != null ? tag : new ErrorMsg()
                .setError(Constants.NOT_FOUND_MSG).setErrorDescription("Tag entity with id = " + id + " not exist!"));
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Tag tag) {
        tagService.save(tag);
        log.info("POST tag: {}", tag);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody TagDTO tag) {

        try {
            final TagDTO update = tagService.update(id, tag);
            log.info("Updated tag with id: {}, tag: {}", id, tag);
            return ResponseEntity.ok(update != null ? update : new ErrorMsg()
                    .setError(Constants.NOT_FOUND_MSG).setErrorDescription(Constants.NOT_FOUND_MSG));
        } catch (Exception e) {
            log.error("Exception while updating tag with id: {}, tag: {}. Exception: {}", id, tag, e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id) {
        try {
            tagService.delete(id);
            log.info("removed tag with id = {}", id);
            return ResponseEntity.ok("Tag deleted successfully");
        } catch (Exception e) {
            log.error("Tag entity with id {} not exist!", id);
            return new ResponseEntity<>(Constants.NOT_REMOVED_MSG, HttpStatus.BAD_REQUEST);
        }
    }


}
