package ru.clevertec.ecl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.ecl.dto.SequenceIdDTO;
import ru.clevertec.ecl.service.SequenceOrderService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sequence")
public class SequenceController {

    private final SequenceOrderService sequenceService;

    /**
     * @return sequence заказа
     */
    @GetMapping("/orders")
    public Integer getSequenceOrder() {
        return sequenceService.getSequence();
    }

    /**
     * @param sequenceIdDTO - значние sequence, которое неоюходимо установить
     */
    @PutMapping("/orders")
    public void setSequenceOrder(@RequestBody SequenceIdDTO sequenceIdDTO) {
        sequenceService.setSequence(sequenceIdDTO.getId());
    }

}
