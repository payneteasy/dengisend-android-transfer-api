/*
 * Dengisend
 *
 * Created by Alex Oleynyak on 02/08/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

package com.payneteasy.dengisend.data.source;

import static com.google.common.base.Preconditions.checkNotNull;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.payneteasy.dengisend.domain.model.Receipt;
import com.payneteasy.dengisend.utils.Strings;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Concrete implementation to load receipts from the data sources into a cache.
 * <p>
 * For simplicity, this implements a dumb synchronisation between locally persisted data and data
 * obtained from the server, by using the remote data source only if the local database doesn't
 * exist or is empty.
 */
@Singleton
public class ReceiptsRepository implements ReceiptsDataSource {

    private final ReceiptsDataSource mReceiptsLocalDataSource;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    Map<String, Receipt> mCachedReceipts;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    boolean mCacheIsDirty = false;

    /**
     * By marking the constructor with {@code @Inject}, Dagger will try to inject the dependencies
     * required to create an instance of the ReceiptsRepository. Because {@link ReceiptsDataSource} is an
     * interface, we must provide to Dagger a way to build those arguments, this is done in
     * {@link ReceiptsRepositoryModule}.
     * <p>
     * When two arguments or more have the same type, we must provide to Dagger a way to
     * differentiate them. This is done using a qualifier.
     * <p>
     * Dagger strictly enforces that arguments not marked with {@code @Nullable} are not injected
     * with {@code @Nullable} values.
     */
    @Inject
    ReceiptsRepository(ReceiptsDataSource receiptsLocalDataSource) {
        mReceiptsLocalDataSource = receiptsLocalDataSource;
    }

    /**
     * Gets receipts from cache, local data source (SQLite) or remote data source, whichever is
     * available first.
     * <p>
     * Note: {@link LoadReceiptsCallback#onDataNotAvailable()} is fired if all data sources fail to
     * get the data.
     */
    @Override
    public void getReceipts(@NonNull final LoadReceiptsCallback callback) {
        checkNotNull(callback);

        mReceiptsLocalDataSource.getReceipts(new LoadReceiptsCallback() {
            @Override
            public void onReceiptsLoaded(List<Receipt> receipts) {
                refreshCache(receipts);
                callback.onReceiptsLoaded(new ArrayList<>(mCachedReceipts.values()));
            }

            @Override
            public void onEmptyReceiptsList() {
                refreshCache();
                callback.onEmptyReceiptsList();
            }

            @Override
            public void onDataNotAvailable() {
                // getReceiptsFromRemoteDataSource(callback);
            }
        });
    }

    @Override
    public void saveReceipt(@NonNull Receipt receipt) {
        checkNotNull(receipt);

        receipt.setSourceCard(Strings.cardNumberMasked(receipt.getSourceCard()));
        receipt.setDestCard(Strings.cardNumberMasked(receipt.getDestCard()));

        mReceiptsLocalDataSource.saveReceipt(receipt);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedReceipts == null) {
            mCachedReceipts = new LinkedHashMap<>();
        }
        mCachedReceipts.put(receipt.getOrderId(), receipt);
    }

    @Override
    public void saveReceiptCardsIds(@NonNull Receipt receipt) {
        checkNotNull(receipt);
        mReceiptsLocalDataSource.saveReceiptCardsIds(receipt);
        refreshReceipts();
    }

    /**
     * Gets receipts from local data source (sqlite) unless the table is new or empty. In that case it
     * uses the network data source. This is done to simplify the sample.
     * <p>
     * Note: {@link LoadReceiptsCallback#onDataNotAvailable()} is fired if both data sources fail to
     * get the data.
     */
    @Override
    public void getReceipt(@NonNull final String receiptId, @NonNull final GetReceiptCallback callback) {
        checkNotNull(receiptId);
        checkNotNull(callback);

        Receipt cachedReceipt = getReceiptWithId(receiptId);

        // Respond immediately with cache if available
        if (cachedReceipt != null) {
            callback.onReceiptLoaded(cachedReceipt);
            return;
        }

        // Load from server/persisted if needed.

        // Is the receipt in the local data source? If not, query the network.
        mReceiptsLocalDataSource.getReceipt(receiptId, new GetReceiptCallback() {
            @Override
            public void onReceiptLoaded(Receipt receipt) {
                callback.onReceiptLoaded(receipt);
            }

            @Override
            public void onDataNotAvailable() {
                // mReceiptsRemoteDataSource.getReceipt ...
            }
        });
    }

    @Override
    public void refreshReceipts() {
        mCacheIsDirty = true;
    }

    @Override
    public void deleteAllReceipts() {
        mReceiptsLocalDataSource.deleteAllReceipts();

        if (mCachedReceipts == null) {
            mCachedReceipts = new LinkedHashMap<>();
        }
        mCachedReceipts.clear();
    }

    @Override
    public void deleteReceipt(@NonNull String receiptId) {
        mReceiptsLocalDataSource.deleteReceipt(checkNotNull(receiptId));

        if (mCachedReceipts != null) {
            mCachedReceipts.remove(receiptId);
        }
    }

    private void refreshCache() {
        if (mCachedReceipts == null) {
            mCachedReceipts = new LinkedHashMap<>();
        }
        mCachedReceipts.clear();
        mCacheIsDirty = false;
    }

    private void refreshCache(List<Receipt> receipts) {
        if (mCachedReceipts == null) {
            mCachedReceipts = new LinkedHashMap<>();
        }
        mCachedReceipts.clear();
        for (Receipt receipt : receipts) {
            mCachedReceipts.put(receipt.getOrderId(), receipt);
        }
        mCacheIsDirty = false;
    }

    @Nullable
    private Receipt getReceiptWithId(@NonNull String id) {
        checkNotNull(id);
        if (mCachedReceipts == null || mCachedReceipts.isEmpty()) {
            return null;
        } else {
            return mCachedReceipts.get(id);
        }
    }
}