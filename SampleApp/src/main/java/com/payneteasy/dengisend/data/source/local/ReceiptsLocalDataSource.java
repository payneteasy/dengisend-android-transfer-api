/*
 * Dengisend
 *
 * Created by Alex Oleynyak on 02/08/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

package com.payneteasy.dengisend.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.payneteasy.dengisend.domain.model.Receipt;
import com.payneteasy.dengisend.data.source.ReceiptsDataSource;
import com.payneteasy.dengisend.data.source.local.ReceiptsPersistenceContract.ReceiptEntry;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Concrete implementation of a data source as a db.
 */
@Singleton
public class ReceiptsLocalDataSource implements ReceiptsDataSource {

    private ReceiptsDbHelper mDbHelper;

    @Inject
    public ReceiptsLocalDataSource(@NonNull Context context) {
        checkNotNull(context);
        mDbHelper = new ReceiptsDbHelper(context);
    }

    /**
     * Note: {@link LoadReceiptsCallback#onDataNotAvailable()} is fired if the database doesn't exist
     * or the table is empty.
     */
    @Override
    public void getReceipts(@NonNull LoadReceiptsCallback callback) {
        List<Receipt> receipts = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                ReceiptEntry.COLUMN_NAME_ORDER_ID,
                ReceiptEntry.COLUMN_NAME_INVOICE_ID,
                ReceiptEntry.COLUMN_NAME_DATE,
                ReceiptEntry.COLUMN_NAME_SOURCE_CARD,
                ReceiptEntry.COLUMN_NAME_SOURCE_CARD_ID,
                ReceiptEntry.COLUMN_NAME_DEST_CARD,
                ReceiptEntry.COLUMN_NAME_DEST_CARD_ID,
                ReceiptEntry.COLUMN_NAME_AMOUNT_CENTIS,
                ReceiptEntry.COLUMN_NAME_COMMISSION_CENTIS,
                ReceiptEntry.COLUMN_NAME_CURRENCY,
                ReceiptEntry.COLUMN_NAME_COMMENT,
                ReceiptEntry.COLUMN_NAME_STATUS
        };

        Cursor c = db.query(
                ReceiptEntry.TABLE_NAME, projection, null, null, null, null, ReceiptEntry.COLUMN_NAME_ORDER_ID + " DESC");

        if (c != null && c.getCount() > 0) {
            try {
                while (c.moveToNext()) {
                    String orderId = c.getString(c.getColumnIndexOrThrow(ReceiptEntry.COLUMN_NAME_ORDER_ID));
                    String invoiceId = c.getString(c.getColumnIndexOrThrow(ReceiptEntry.COLUMN_NAME_INVOICE_ID));
                    String date = c.getString(c.getColumnIndexOrThrow(ReceiptEntry.COLUMN_NAME_DATE));
                    String sourceCard = c.getString(c.getColumnIndexOrThrow(ReceiptEntry.COLUMN_NAME_SOURCE_CARD));
                    String sourceCardId = c.getString(c.getColumnIndexOrThrow(ReceiptEntry.COLUMN_NAME_SOURCE_CARD_ID));
                    String destCard = c.getString(c.getColumnIndexOrThrow(ReceiptEntry.COLUMN_NAME_DEST_CARD));
                    String destCardId = c.getString(c.getColumnIndexOrThrow(ReceiptEntry.COLUMN_NAME_DEST_CARD_ID));
                    Integer amountCentis = c.getInt(c.getColumnIndexOrThrow(ReceiptEntry.COLUMN_NAME_AMOUNT_CENTIS));
                    Integer commissionCentis = c.getInt(c.getColumnIndexOrThrow(ReceiptEntry.COLUMN_NAME_COMMISSION_CENTIS));
                    String currency = c.getString(c.getColumnIndexOrThrow(ReceiptEntry.COLUMN_NAME_CURRENCY));
                    String comment = c.getString(c.getColumnIndexOrThrow(ReceiptEntry.COLUMN_NAME_COMMENT));
                    String status = c.getString(c.getColumnIndexOrThrow(ReceiptEntry.COLUMN_NAME_STATUS));

                    Receipt receipt = new Receipt(orderId, invoiceId, date, sourceCard, sourceCardId, destCard, destCardId, amountCentis, commissionCentis, currency, comment, status);
                    receipts.add(receipt);
                }
            } finally {
                c.close();
            }
        }

        db.close();

        if (receipts.isEmpty()) {
            // This will be called if the table is new or just empty.
            callback.onEmptyReceiptsList();
        } else {
            callback.onReceiptsLoaded(receipts);
        }
    }

    /**
     * Note: {@link GetReceiptCallback#onDataNotAvailable()} is fired if the {@link Receipt} isn't
     * found.
     */
    @Override
    public void getReceipt(@NonNull String orderId, @NonNull GetReceiptCallback callback) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                ReceiptEntry.COLUMN_NAME_INVOICE_ID,
                ReceiptEntry.COLUMN_NAME_DATE,
                ReceiptEntry.COLUMN_NAME_SOURCE_CARD,
                ReceiptEntry.COLUMN_NAME_SOURCE_CARD_ID,
                ReceiptEntry.COLUMN_NAME_DEST_CARD,
                ReceiptEntry.COLUMN_NAME_DEST_CARD_ID,
                ReceiptEntry.COLUMN_NAME_AMOUNT_CENTIS,
                ReceiptEntry.COLUMN_NAME_COMMISSION_CENTIS,
                ReceiptEntry.COLUMN_NAME_CURRENCY,
                ReceiptEntry.COLUMN_NAME_COMMENT,
                ReceiptEntry.COLUMN_NAME_STATUS
        };

        String selection = ReceiptEntry.COLUMN_NAME_ORDER_ID + " = ?";
        String[] selectionArgs = { orderId };

        Cursor c = db.query(
                ReceiptEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        Receipt receipt = null;

        if (c != null && c.getCount() > 0) {

            try {
                c.moveToFirst();

                String invoiceId = c.getString(c.getColumnIndexOrThrow(ReceiptEntry.COLUMN_NAME_INVOICE_ID));
                String date = c.getString(c.getColumnIndexOrThrow(ReceiptEntry.COLUMN_NAME_DATE));
                String sourceCard = c.getString(c.getColumnIndexOrThrow(ReceiptEntry.COLUMN_NAME_SOURCE_CARD));
                String sourceCardId = c.getString(c.getColumnIndexOrThrow(ReceiptEntry.COLUMN_NAME_SOURCE_CARD_ID));
                String destCard = c.getString(c.getColumnIndexOrThrow(ReceiptEntry.COLUMN_NAME_DEST_CARD));
                String destCardId = c.getString(c.getColumnIndexOrThrow(ReceiptEntry.COLUMN_NAME_DEST_CARD_ID));
                Integer amountCentis = c.getInt(c.getColumnIndexOrThrow(ReceiptEntry.COLUMN_NAME_AMOUNT_CENTIS));
                Integer commissionCentis = c.getInt(c.getColumnIndexOrThrow(ReceiptEntry.COLUMN_NAME_COMMISSION_CENTIS));
                String currency = c.getString(c.getColumnIndexOrThrow(ReceiptEntry.COLUMN_NAME_CURRENCY));
                String comment = c.getString(c.getColumnIndexOrThrow(ReceiptEntry.COLUMN_NAME_COMMENT));
                String status = c.getString(c.getColumnIndexOrThrow(ReceiptEntry.COLUMN_NAME_STATUS));

                receipt = new Receipt(orderId, invoiceId, date, sourceCard, sourceCardId, destCard, destCardId, amountCentis, commissionCentis, currency, comment, status);
            } finally {
                c.close();
            }
        }

        db.close();

        if (receipt != null) {
            callback.onReceiptLoaded(receipt);
        } else {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void saveReceipt(@NonNull Receipt receipt) {
        checkNotNull(receipt);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ReceiptEntry.COLUMN_NAME_ORDER_ID, receipt.getOrderId());
        values.put(ReceiptEntry.COLUMN_NAME_INVOICE_ID, receipt.getInvoiceId());
        values.put(ReceiptEntry.COLUMN_NAME_DATE, receipt.getDate());
        values.put(ReceiptEntry.COLUMN_NAME_SOURCE_CARD, receipt.getSourceCard());
        values.put(ReceiptEntry.COLUMN_NAME_SOURCE_CARD_ID, receipt.getSourceCardId());
        values.put(ReceiptEntry.COLUMN_NAME_DEST_CARD, receipt.getDestCard());
        values.put(ReceiptEntry.COLUMN_NAME_DEST_CARD_ID, receipt.getDestCardId());
        values.put(ReceiptEntry.COLUMN_NAME_AMOUNT_CENTIS, receipt.getAmountCentis());
        values.put(ReceiptEntry.COLUMN_NAME_COMMISSION_CENTIS, receipt.getCommissionCentis());
        values.put(ReceiptEntry.COLUMN_NAME_CURRENCY, receipt.getCurrency());
        values.put(ReceiptEntry.COLUMN_NAME_COMMENT, receipt.getComment());
        values.put(ReceiptEntry.COLUMN_NAME_STATUS, receipt.getStatus());

        db.insert(ReceiptEntry.TABLE_NAME, null, values);

        db.close();
    }

    @Override
    public void saveReceiptCardsIds(@NonNull Receipt receipt) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ReceiptEntry.COLUMN_NAME_SOURCE_CARD_ID, receipt.getSourceCardId());
        values.put(ReceiptEntry.COLUMN_NAME_DEST_CARD_ID, receipt.getDestCardId());

        String selection = ReceiptEntry.COLUMN_NAME_ORDER_ID + " = ?";
        String[] selectionArgs = { receipt.getOrderId() };

        db.update(ReceiptEntry.TABLE_NAME, values, selection, selectionArgs);

        db.close();
    }

    @Override
    public void refreshReceipts() {
        // Not required because the {@link ReceiptsRepository} handles the logic of refreshing the
        // receipts from all the available data sources.
    }

    @Override
    public void deleteAllReceipts() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.delete(ReceiptEntry.TABLE_NAME, null, null);

        db.close();
    }

    @Override
    public void deleteReceipt(@NonNull String receiptId) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String selection = ReceiptEntry.COLUMN_NAME_ORDER_ID + " LIKE ?";
        String[] selectionArgs = { receiptId };

        db.delete(ReceiptEntry.TABLE_NAME, selection, selectionArgs);

        db.close();
    }
}
