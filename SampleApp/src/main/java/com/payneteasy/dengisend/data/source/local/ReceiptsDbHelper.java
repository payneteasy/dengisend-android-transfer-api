/*
 * Dengisend
 *
 * Created by Alex Oleynyak on 02/08/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

package com.payneteasy.dengisend.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ReceiptsDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "Dengisend.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String INTEGER_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ReceiptsPersistenceContract.ReceiptEntry.TABLE_NAME + " (" +
                    ReceiptsPersistenceContract.ReceiptEntry.COLUMN_NAME_ORDER_ID + TEXT_TYPE + " PRIMARY KEY," +
                    ReceiptsPersistenceContract.ReceiptEntry.COLUMN_NAME_INVOICE_ID + TEXT_TYPE + COMMA_SEP +
                    ReceiptsPersistenceContract.ReceiptEntry.COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
                    ReceiptsPersistenceContract.ReceiptEntry.COLUMN_NAME_SOURCE_CARD + TEXT_TYPE + COMMA_SEP +
                    ReceiptsPersistenceContract.ReceiptEntry.COLUMN_NAME_SOURCE_CARD_ID + TEXT_TYPE + COMMA_SEP +
                    ReceiptsPersistenceContract.ReceiptEntry.COLUMN_NAME_DEST_CARD + TEXT_TYPE + COMMA_SEP +
                    ReceiptsPersistenceContract.ReceiptEntry.COLUMN_NAME_DEST_CARD_ID + TEXT_TYPE + COMMA_SEP +
                    ReceiptsPersistenceContract.ReceiptEntry.COLUMN_NAME_AMOUNT_CENTIS + INTEGER_TYPE + COMMA_SEP +
                    ReceiptsPersistenceContract.ReceiptEntry.COLUMN_NAME_COMMISSION_CENTIS + INTEGER_TYPE + COMMA_SEP +
                    ReceiptsPersistenceContract.ReceiptEntry.COLUMN_NAME_CURRENCY + TEXT_TYPE + COMMA_SEP +
                    ReceiptsPersistenceContract.ReceiptEntry.COLUMN_NAME_COMMENT + TEXT_TYPE + COMMA_SEP +
                    ReceiptsPersistenceContract.ReceiptEntry.COLUMN_NAME_STATUS + INTEGER_TYPE +
                    " )";

    public ReceiptsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }
}