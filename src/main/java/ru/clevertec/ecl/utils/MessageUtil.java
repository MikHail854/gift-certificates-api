package ru.clevertec.ecl.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.constants.Constants;


@Service
@RequiredArgsConstructor
public class MessageUtil {

    private final MessageSource messageSource;

    public String getMessage(String code, Object... objects){
        return messageSource.getMessage(code, objects, Constants.LOCALE);
    }

}
