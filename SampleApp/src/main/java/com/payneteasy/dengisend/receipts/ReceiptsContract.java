/*
 * Dengisend
 * <p>
 * Created by Alex Oleynyak on 03/08/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

package com.payneteasy.dengisend.receipts;

import android.support.annotation.NonNull;

import com.payneteasy.dengisend.BaseView;
import com.payneteasy.dengisend.BasePresenter;
import com.payneteasy.dengisend.MainContract;
import com.payneteasy.dengisend.domain.model.Receipt;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface ReceiptsContract {

    interface View extends BaseView<Presenter> {

        void setMainActivity(@NonNull MainContract.Activity activity);

        void setLoadingIndicator(boolean active);

        void prepareToShowReceiptDetails(String receiptId);

        void showReceipts(List<Receipt> receipts);

        void showReceiptDetailsUi(String receiptId);

        void showLoadingReceiptsError();

        void showNoReceipts();

        boolean isActive();

        void customizeUI();
    }

    interface Presenter extends BasePresenter {

        void loadReceipts(boolean forceUpdate);

        void openReceiptDetails(@NonNull Receipt requestedReceipt);
    }
}