package com.payneteasy.dengisend;


public class Config {

    public static final String PAYNET_BASE_ADDRESS = BuildConfig.PAYNET_BASE_URL;
    public static final String MERCHANT_BASE_ADDRESS = BuildConfig.MERCHANT_BASE_URL;

    public static final String DENGISEND_IDENTIFIER = BuildConfig.BANK_ID + "-android";

    public static final String SHARED_PREF_FILE = "dengisend";
    public static final String SHARED_PREF_PARAM_DEVICE_ID = "device_id";
    public static final String SHARED_PREF_PARAM_USER_EMAIL = "user_email";

    public static final String PAYMENT_CURRENCY = "RUB";

    public static final String INTENT_STOP_WEBVIEW = "stopWebView";

    public static final String STATUS_COLOR_APPROVED = "189b24";

    public static final String STATUS_COLOR_DECLINED = "ff0000";

    public static final String ERROR_MESSAGE_COMMON = "common";
    public static final String ERROR_MESSAGE_COMMISSION_UPD = "error_get_commission";

    public static final String CARD_NATURE_SOURCE = "sourceCard";
    public static final String CARD_NATURE_DESTINATION = "destinationCard";

    public static final int PERMISSIONS_STORAGE = 1;
}