package com.payneteasy.dengisend.utils;

/**
 * Dengisend
 * <p>
 * Created by Alex Oleynyak on 11/08/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

public class CardInfo {

    public final CardType cardType;
    public final int minLength;
    public final int maxLength;
    public final int startLuhnCheck;

    public CardInfo(CardType cardType, int minLength, int maxLength, int startLuhnCheck) {
        this.cardType = cardType;
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.startLuhnCheck = startLuhnCheck;
    }

    public static CardInfo getCardInfoForCardType(CardType cardType){

        switch (cardType) {
            case VISA:
                return new CardInfo(CardType.VISA, 13, 19, 16);

            case MASTERCARD:
                return new CardInfo(CardType.MASTERCARD, 16, 19, 16);

            case MIR:
                return new CardInfo(CardType.MIR, 16, 16, 16);
        }

        return new CardInfo(CardType.UNKNOWN, 13, 19, 16);
    }
}