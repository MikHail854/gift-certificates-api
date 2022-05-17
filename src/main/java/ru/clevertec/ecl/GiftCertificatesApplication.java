package ru.clevertec.ecl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
//@EnableFeignClients
@SpringBootApplication
//@ServletComponentScan(value = "ru.clevertec.ecl.filter")
public class GiftCertificatesApplication {

    public static void main(String[] args) {
        SpringApplication.run(GiftCertificatesApplication.class, args);
    }

}
