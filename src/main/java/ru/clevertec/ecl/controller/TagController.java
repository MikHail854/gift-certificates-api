package ru.clevertec.ecl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.entty.Tag;
import ru.clevertec.ecl.service.TagService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    @GetMapping
    public List<TagDTO> fidAll() {
        return tagService.findAll();
    }

    @GetMapping("/{id}")
    public TagDTO getById(@PathVariable("id") int id) {
        return tagService.findById(id);
    }

    @PostMapping
    public TagDTO save(@RequestBody Tag tag) {
        return tagService.save(tag);
    }

    @PutMapping("/{id}")
    public TagDTO update(@PathVariable("id") int id, @RequestBody TagDTO tag) {
        return tagService.update(id, tag);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id) {
        tagService.delete(id);
        return ResponseEntity.ok("Tag deleted successfully");
    }

    @GetMapping("/super_tag")
    public TagDTO getTheMostWidelyUsedTag() {
        return tagService.findTheMostWidelyUsedTag();
    }

}
