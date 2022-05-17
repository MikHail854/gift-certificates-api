package ru.clevertec.ecl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.ecl.dto.SequenceIdDTO;
import ru.clevertec.ecl.service.SequenceService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sequence")
public class SequenceController {

    private final SequenceService sequenceService;


    @GetMapping
    public Integer getSequenceOrder() {
        return sequenceService.getSequence();
    }

    @PatchMapping
//    public void setSequenceOrder(@RequestBody int id /*@PathVariable("id") int id*/){
    public void setSequenceOrder(@RequestBody SequenceIdDTO sequenceIdDTO /*@PathVariable("id") int id*/){
        sequenceService.setSequence(sequenceIdDTO.getId());
    }

}
