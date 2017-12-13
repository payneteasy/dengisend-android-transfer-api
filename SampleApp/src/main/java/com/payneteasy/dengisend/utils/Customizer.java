package com.payneteasy.dengisend.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;

import com.payneteasy.dengisend.transfer.TransferService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Dengisend
 * <p>
 * Created by Alex Oleynyak on 28/09/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

public class Customizer {

    private static Customizer INSTANCE = null;

    private JSONObject jsonConfig;

    private Customizer(Context context) {
        try {
            InputStream is = context.getAssets().open("config.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            jsonConfig = new JSONObject(json);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param context the Context
     * @return the {@link TransferService} instance
     */
    public static Customizer getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new Customizer(context);
        }
        return INSTANCE;
    }

    @Nullable
    private JSONObject getJSONObject(String name, JSONObject fromObject) {
        JSONObject obj = null;

        if (fromObject != null) {
            try {
                obj = fromObject.getJSONObject(name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return obj;
    }

    @Nullable
    public String getStringFromObject(String stringName, String objectName) {
        String resultString = null;
        JSONObject obj = jsonConfig;

        String[] objectNames = objectName.split("\\.");

        if (objectNames.length > 0) {
            for (String objName : objectNames) {
                obj = getJSONObject(objName, obj);
            }
        }

        if (obj != null) {
            try {
                resultString = obj.getString(stringName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return resultString;
    }

    public int manipulateColor(int color, float factor) {
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.argb(a,
                Math.min(r,255),
                Math.min(g,255),
                Math.min(b,255));
    }
}
