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

    public static final String URL_SEQUENCE_ORDERS = "http://localhost:%s/sequence/orders";

    public static final String URL_COMMIT_LOG = "http://localhost:%s/commit_log?request_from_client=false";

    public static final String URL_CREATE_GIFT_CERTIFICATE = "http://localhost:%s/gifts/create?save_to_commit_log=false";
    public static final String URL_UPDATE_GIFT_CERTIFICATE = "http://localhost:%s/gifts/update/%s?save_to_commit_log=false";
    public static final String URL_UPDATE_PRICE_GIFT_CERTIFICATE = "http://localhost:%s/gifts/update/%s/price?save_to_commit_log=false";
    public static final String URL_UPDATE_DURATION_GIFT_CERTIFICATE = "http://localhost:%s/gifts/update/%s/duration?save_to_commit_log=false";
    public static final String URL_DELETE_GIFT_CERTIFICATE = "http://localhost:%s/gifts/delete/%s?save_to_commit_log=false";

    public static final String URL_CREATE_TAG = "http://localhost:%s/tags/create?save_to_commit_log=false";
    public static final String URL_UPDATE_TAG = "http://localhost:%s/tags/update/%s?save_to_commit_log=false";
    public static final String URL_DELETE_TAG = "http://localhost:%s/tags/delete/%s?save_to_commit_log=false";

    public static final String URL_CREATE_USER = "http://localhost:%s/users/create?save_to_commit_log=false";
    public static final String URL_DELETE_USER = "http://localhost:%s/users/delete/%s?save_to_commit_log=false";

}