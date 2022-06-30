//package ru.clevertec.ecl.service;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.context.event.EventListener;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class MonitoringInfoProducer {
//
//    private static final String KAFKA_TOPIC = "commit_log";
//    private final KafkaTemplate<String, String> kafkaTemplate;
//    private final ObjectMapper objectMapper;
//
//    /**
//     * отправляет в кафку
//     *
//     * @param ctxStartEvt
//     * @throws InterruptedException
//     */
//    @EventListener
//    public void handleContextRefreshEvent(ContextRefreshedEvent ctxStartEvt) throws InterruptedException {
////        int i = 0;
////        while (true) {
////            Thread.sleep(500);
////
////            kafkaTemplate.send(KAFKA_TOPIC, objectMapper.writeValueAsString(monitoringInfo));
////            log.info("message {} send", i);
////        }
//    }
//}
