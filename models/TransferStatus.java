package com.payneteasy.dengisend.domain.model;

public enum TransferStatus {
    UNKNOWN("UNKNOWN"),
    APPROVED("APPROVED"),
    DECLINED("DECLINED"),
    CANCELLED("CANCELLED"),
    ERROR("ERROR");

    private String value;

    TransferStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static String localizedString(String status) {

        TransferStatus ts = TransferStatus.valueOf(status);

        String localizedStatus = "";

        switch (ts) {
            case UNKNOWN:
                localizedStatus = "Неизвестен";
                break;
            case APPROVED:
                localizedStatus = "Исполнен";
                break;
            case DECLINED:
                localizedStatus = "Отклонен";
                break;
            case CANCELLED:
                localizedStatus = "Отмененен";
                break;
            case ERROR:
                localizedStatus = "Ошибка";
        }

        return localizedStatus;
    }
}
