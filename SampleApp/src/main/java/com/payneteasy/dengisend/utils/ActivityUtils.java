package com.payneteasy.dengisend.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.payneteasy.dengisend.Config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.UUID;


/**
 * This provides methods to help Activities load their UI.
 */
public class ActivityUtils {

    public static String getSerial(Context context) {

        SharedPreferences prefs = context.getSharedPreferences(Config.SHARED_PREF_FILE, 0);
        String id = prefs.getString(Config.SHARED_PREF_PARAM_DEVICE_ID, null);

        if (id == null) {
            final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

            try {
                UUID uuid_1 = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
                id = uuid_1.toString();
            } catch (UnsupportedEncodingException e) {
                id = UUID.randomUUID().toString();
            }

            prefs.edit()
                    .putString(Config.SHARED_PREF_PARAM_DEVICE_ID, id)
                    .apply();
        }

        return id;
    }

    @Nullable
    public static Bitmap getScreenshot(View screenshotView) {

        try {
            screenshotView.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(screenshotView.getDrawingCache());
            screenshotView.setDrawingCacheEnabled(false);
            return bitmap;
        } catch (Exception e) {
            Log.e("ActivityUtils", e.getMessage(), e);
        }

        return null;
    }

    @Nullable
    public static File storeScreenshot(Bitmap bm, String fileName, String filePath) {

        File dir = new File(filePath);

        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.d("ActivityUtils", "storeScreenshot: mkdir failed");
                return null;
            }
        }

        File file = new File(filePath, fileName);

        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e) {
            Log.e("ActivityUtils", e.getMessage(), e);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!file.exists()) {
            Log.d("ActivityUtils", "storeScreenshot: file not exist");
            return null;
        }

        return file;
    }

    public static boolean shareImage(File file, Fragment fragment) {

        Uri uri = Uri.fromFile(file);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            fragment.startActivity(Intent.createChooser(intent, "Share Screenshot"));

            return true;

        } catch (ActivityNotFoundException e) {
            Log.e("ActivityUtils", e.getMessage(), e);

            return false;
        }
    }
}
