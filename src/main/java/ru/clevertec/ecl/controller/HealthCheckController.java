package ru.clevertec.ecl.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health-check")
public class HealthCheckController {

    /**
     * @return статус 200 если сервис запущен
     */
    @GetMapping
    public HttpStatus healthCheck() {
        return HttpStatus.OK;
    }

}
