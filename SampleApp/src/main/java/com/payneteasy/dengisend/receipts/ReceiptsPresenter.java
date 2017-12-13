/*
 * Dengisend
 *
 * Created by Alex Oleynyak on 03/08/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

package com.payneteasy.dengisend.receipts;

import android.content.Context;
import android.support.annotation.NonNull;

import com.payneteasy.dengisend.AppModule;
import com.payneteasy.dengisend.DengisendApplication;
import com.payneteasy.dengisend.data.source.ReceiptsDataSource;
import com.payneteasy.dengisend.data.source.ReceiptsRepository;
import com.payneteasy.dengisend.domain.model.Receipt;

import java.util.List;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Listens to user actions from the UI ({@link ReceiptsFragment}), retrieves the data and updates the
 * UI as required.
 * <p />
 * By marking the constructor with {@code @Inject}, Dagger injects the dependencies required to
 * create an instance of the ReceiptsPresenter (if it fails, it emits a compiler error).  It uses
 * {@link ReceiptsPresenterModule} to do so.
 * <p/>
 * Dagger generated code doesn't require public access to the constructor or class, and
 * therefore, to ensure the developer doesn't instantiate the class manually and bypasses Dagger,
 * it's good practice minimise the visibility of the class/constructor as much as possible.
 **/
public class ReceiptsPresenter implements ReceiptsContract.Presenter {

    @Inject
    ReceiptsRepository mReceiptsRepository;

    private final ReceiptsContract.View mReceiptsView;

    private boolean mFirstLoad = true;

    /**
     * Dagger strictly enforces that arguments not marked with {@code @Nullable} are not injected
     * with {@code @Nullable} values.
     */
    @Inject
    ReceiptsPresenter(ReceiptsContract.View receiptsView, Context context) {
        mReceiptsView = receiptsView;

        DaggerReceiptsComponent
                .builder()
                .appModule(new AppModule((DengisendApplication) context))
                .build()
                .inject(this);
    }

    /**
     * Method injection is used here to safely reference {@code this} after the object is created.
     * For more information, see Java Concurrency in Practice.
     */
    @Inject
    void setupListeners() {
        mReceiptsView.setPresenter(this);
    }

    @Override
    public void start() {
        loadReceipts(true);
    }

    @Override
    public void loadReceipts(boolean forceUpdate) {
        // Simplification for sample: a network reload will be forced on first load.
        loadReceipts(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    /**
     * @param forceUpdate   Pass in true to refresh the data in the {@link ReceiptsDataSource}
     * @param showLoadingUI Pass in true to display a loading icon in the UI
     */
    private void loadReceipts(boolean forceUpdate, final boolean showLoadingUI) {

        if (showLoadingUI) {
            mReceiptsView.setLoadingIndicator(true);
        }

        if (forceUpdate) {
            mReceiptsRepository.refreshReceipts();
        }

        mReceiptsRepository.getReceipts(new ReceiptsDataSource.LoadReceiptsCallback() {
            @Override
            public void onReceiptsLoaded(List<Receipt> receipts) {

                // The view may not be able to handle UI updates anymore
                if (!mReceiptsView.isActive()) {
                    return;
                }

                processReceipts(receipts);
            }

            @Override
            public void onEmptyReceiptsList() {
                mReceiptsView.showNoReceipts();
            }

            @Override
            public void onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!mReceiptsView.isActive()) {
                    return;
                }
                mReceiptsView.showLoadingReceiptsError();
            }
        });
    }

    private void processReceipts(List<Receipt> receipts) {
        if (receipts.isEmpty()) {
            // Show a message indicating there are no receipts
            mReceiptsView.showNoReceipts();
        } else {
            // Show the list of receipts
            mReceiptsView.showReceipts(receipts);
        }
    }

    @Override
    public void openReceiptDetails(@NonNull Receipt requestedReceipt) {
        checkNotNull(requestedReceipt, "requestedReceipt cannot be null!");
        mReceiptsView.showReceiptDetailsUi(requestedReceipt.getOrderId());
    }
}