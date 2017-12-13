package com.payneteasy.dengisend.utils;

/**
 * Dengisend
 * <p>
 * Created by Alex Oleynyak on 11/08/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

public class CardValidator {

    public static boolean isNumberValid(String cardNumber) {

        CardType cardType = detectCardType(cardNumber);
        CardInfo cardInfo = CardInfo.getCardInfoForCardType(cardType);

        cardNumber = cardNumber.replace(" ", "");

        return (cardInfo.cardType != CardType.UNKNOWN) &&
                (cardNumber.length() >= cardInfo.minLength) &&
                (cardNumber.length() <= cardInfo.maxLength) &&
                isLuhnValid(cardNumber);
    }

    public static CardType detectCardType(String cardNumber) {

        int prefix_1 = digitalPrefixWithLength(cardNumber, 1);
        int prefix_2 = digitalPrefixWithLength(cardNumber, 2);
        int prefix_4 = digitalPrefixWithLength(cardNumber, 4);

        if (prefix_1 == 4)
            return CardType.VISA;

        if ((prefix_1 == 6) ||
                (prefix_2 == 50) ||
                (prefix_2 >= 51 && prefix_2 <= 55) ||
                (prefix_2 >= 56 && prefix_2 <= 58) ||
                (prefix_4 >= 2221 && prefix_4 <= 2720))
            return CardType.MASTERCARD;

        if ((prefix_4 >= 2200 && prefix_4 <= 2204))
            return CardType.MIR;

        return CardType.UNKNOWN;
    }

    private static boolean isLuhnValid(String cardNumber) {
        int sum = 0;
        boolean alternate = false;

        // Delete any spacing characters
        cardNumber = cardNumber.replace(" ", "");

        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(cardNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }

        return (sum % 10 == 0);
    }

    private static int digitalPrefixWithLength(String str, int length) {
        if (str.length() >= length) {
            return Integer.parseInt(str.substring(0, length));
        }
        return 0;
    }
}