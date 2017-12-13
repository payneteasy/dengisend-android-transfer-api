/*
 * Dengisend
 * <p>
 * Created by Alex Oleynyak on 02/08/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

package com.payneteasy.dengisend.data.source;

import android.support.annotation.NonNull;

import com.payneteasy.dengisend.domain.model.Receipt;

import java.util.List;

/**
 * Main entry point for accessing tasks data.
 * <p>
 * For simplicity, only getReceipts() and getReceipt() have callbacks. Consider adding callbacks to other
 * methods to inform the user of network/database errors or successful operations.
 * For example, when a new task is created, it's synchronously stored in cache but usually every
 * operation on database or network should be executed in a different thread.
 */
public interface ReceiptsDataSource {

    interface LoadReceiptsCallback {

        void onReceiptsLoaded(List<Receipt> receipts);

        void onEmptyReceiptsList();

        void onDataNotAvailable();
    }

    interface GetReceiptCallback {

        void onReceiptLoaded(Receipt receipt);

        void onDataNotAvailable();
    }

    void getReceipts(@NonNull LoadReceiptsCallback callback);

    void getReceipt(@NonNull String receiptId, @NonNull GetReceiptCallback callback);

    void saveReceipt(@NonNull Receipt receipt);

    void saveReceiptCardsIds(@NonNull Receipt receipt);

    void refreshReceipts();

    void deleteAllReceipts();

    void deleteReceipt(@NonNull String receiptId);
}