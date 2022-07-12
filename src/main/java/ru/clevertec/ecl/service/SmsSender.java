package ru.clevertec.ecl.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsSender {

    @Value("${sms.name}")
    private final String name;

    @Value("${sms.password}")
    private final String password;

    @Value("${sms.host}")
    private final String host;

    @SneakyThrows
    public void sendSms(String phone, String text, String sender) {
        String authString = name + ":" + password;
        String authStringEnc = Base64.getEncoder().encodeToString(authString.getBytes());

        URL url = new URL("http", host, 80, "/messages/v2/send/?phone=%2B" + phone + "&text=" + URLEncoder.encode(text, "UTF-8") + "&sender=" + sender);
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty("Authorization", authStringEnc);
        InputStream is = urlConnection.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);

        int numCharsRead;
        char[] charArray = new char[1024];
        StringBuffer sb = new StringBuffer();
        while ((numCharsRead = isr.read(charArray)) > 0) {
            sb.append(charArray, 0, numCharsRead);
        }
        String result = sb.toString();
        log.info("sms result: {}", result);
    }

}
