package com.payneteasy.dengisend.receiptdetails;

import android.support.annotation.NonNull;

import com.payneteasy.dengisend.BasePresenter;
import com.payneteasy.dengisend.BaseView;
import com.payneteasy.dengisend.MainContract;
import com.payneteasy.dengisend.domain.model.Receipt;

/**
 * Dengisend
 * <p>
 * Created by Alex Oleynyak on 03/09/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

public interface ReceiptDetailsContract {

    interface View extends BaseView<Presenter> {

        void setMainActivity(@NonNull MainContract.Activity activity);

        void showReceipt(Receipt receipt);

        void showMissingReceipt();

        void showReceiptDeleted();

        void showRepeatTransferUI(Receipt receipt);

        void showProgress();

        void hideProgress();

        boolean isActive();

        void customizeUI();
    }

    interface Presenter extends BasePresenter {

        void setReceiptDetailsView(ReceiptDetailsContract.View receiptDetailsView);

        void setReceiptId(String receiptId);

        void deleteReceipt();

        void repeatTransfer(Receipt receipt);
    }
}
