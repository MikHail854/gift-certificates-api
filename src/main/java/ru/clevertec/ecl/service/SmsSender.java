package ru.clevertec.ecl.service;

public interface SmsSender {
    void sendSms(String phone, String text, String sender);
}
