package ru.clevertec.ecl.constants;

import java.util.Locale;

public class Constants {

    public static final String EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT = "Entity %s with id = %s is not exist";
    public static final String EXCEPTION_MESSAGE_SOMETHING_WENT_WRONG = "SOMETHING WENT WRONG";
    public static final Locale LOCALE = new Locale("ru");

    public static final int ERROR_CODE_ENTITY_NOT_FOUND_EXCEPTION = 40001;
    public static final int ERROR_CODE_ILLEGAL_ARGUMENT_EXCEPTION = 40002;
    public static final int ERROR_CODE_METHOD_ARGUMENT_NOT_VALID_EXCEPTION = 40003;
    public static final int ERROR_CODE_DATA_INTEGRITY_VIOLATION_EXCEPTION = 40004;

    public static final String URL_LOCALHOST = "http://localhost:";
//    public static final String PATH_FIND_BY_ID = "findById";
//    public static final String ORDERS = "/orders/";

    public static final String URL_ORDERS_FIND_BY_ID = "http://localhost:%s/orders/%s";
    public static final String URL_ORDERS_FIND_ORDER_BY_ID_AND_USER_ID = "http://localhost:%s/orders/find?user_id=%s&order_id=%s";
    public static final String URL_ORDERS_FIND_ORDERS_BY_USER_ID = "http://localhost:%s/orders?user_id=%s";


    public static final String URL_ORDERS_GET_SEQUENCE = "http://localhost:%s/sequence";




}
