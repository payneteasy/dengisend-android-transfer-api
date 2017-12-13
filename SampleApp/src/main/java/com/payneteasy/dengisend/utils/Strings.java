package com.payneteasy.dengisend.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Dengisend
 * <p>
 * Created by Alex Oleynyak on 01/09/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

public class Strings {

    public static String amountForDisplay(String amount) {
        int pos = 0;
        String result = "";
        for (int i = amount.length()-1; i >= 0; i--) {
            char ch = amount.charAt(i);
            if (ch == ' ')
                continue;
            if (pos == 3) {
                result = " " + result;
                pos = 0;
            }
            pos++;
            result = ch + result;
        }
        return result;
    }

    public static String cardNumberMasked(String cardNum) {
        String result = "";
        for (int i = 0; i < cardNum.length(); i++) {
            char ch = cardNum.charAt(i);
            if (i > 5 && i < cardNum.length() - 4)
                ch = '*';
            result += ch;
        }
        return result;
    }

    public static String cardNumberForDisplay(String cardNum) {
        int pos = 0;
        String result = "";
        for (int i = 0; i < cardNum.length(); i++) {
            char ch = cardNum.charAt(i);
            if (ch == ' ')
                continue;
            if (pos == 4) {
                result += " ";
                pos = 0;
            }
            pos++;
            result += ch;
        }
        return result;
    }

    public static String cardNumberMaskedForDisplay(String cardNum) {
        return cardNumberForDisplay(cardNumberMasked(cardNum));
    }

    public static String longStringFromDateTime(String dateTime) {

        Date parsed;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSSZ", java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat("dd MMM yyyy, hh:mm", java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(dateTime);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
            Log.d("Strings", "ParseException - dateFormat");
        }

        return outputDate;
    }

    public static String monthYearFromDateTime(String dateTime) {

        Date parsed;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSSZ", java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat("MMMM yyyy", java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(dateTime);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
            Log.d("Strings", "ParseException - dateFormat");
        }

        return outputDate;
    }
}
