package com.payneteasy.dengisend.receiptdetails;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.common.base.Strings;
import com.payneteasy.dengisend.AppModule;
import com.payneteasy.dengisend.DengisendApplication;
import com.payneteasy.dengisend.data.source.ReceiptsDataSource;
import com.payneteasy.dengisend.data.source.ReceiptsRepository;
import com.payneteasy.dengisend.domain.model.Receipt;
import com.payneteasy.dengisend.transfer.TransferContract;
import com.payneteasy.dengisend.transfer.TransferModule;

import javax.inject.Inject;

import com.payneteasy.android.model.CardsIdsResponse;

/**
 * Dengisend
 * <p>
 * Created by Alex Oleynyak on 03/09/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

public class ReceiptDetailsPresenter implements ReceiptDetailsContract.Presenter {

    @Inject
    TransferContract.Service transferService;

    @Inject
    ReceiptsRepository mReceiptsRepository;

    private ReceiptDetailsContract.View mReceiptDetailsView;

    @Nullable
    String mReceiptId;

    ReceiptDetailsPresenter(Context context) {

        DaggerReceiptDetailsComponent.builder()
                .appModule(new AppModule((DengisendApplication) context))
                .transferModule(new TransferModule())
                .build()
                .inject(this);
    }

    @Override
    public void start()
    {
        openReceipt();
    }

    @Override
    public void setReceiptDetailsView(ReceiptDetailsContract.View receiptDetailsView) {
        mReceiptDetailsView = receiptDetailsView;
    }

    @Override
    public void setReceiptId(String receiptId) {
        mReceiptId = receiptId;
    }

    private void openReceipt() {
        if (Strings.isNullOrEmpty(mReceiptId)) {
            mReceiptDetailsView.showMissingReceipt();
            return;
        }

        mReceiptsRepository.getReceipt(mReceiptId, new ReceiptsDataSource.GetReceiptCallback() {
            @Override
            public void onReceiptLoaded(Receipt receipt) {
                // The view may not be able to handle UI updates anymore
                if (!mReceiptDetailsView.isActive()) {
                    return;
                }

                if (null == receipt) {
                    mReceiptDetailsView.showMissingReceipt();
                } else {
                    Log.d("getReceipt", receipt.toString());
                    mReceiptDetailsView.showReceipt(receipt);
                }
            }

            @Override
            public void onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!mReceiptDetailsView.isActive()) {
                    return;
                }
                mReceiptDetailsView.showMissingReceipt();
            }
        });
    }

    @Override
    public void deleteReceipt() {
        if (Strings.isNullOrEmpty(mReceiptId)) {
            mReceiptDetailsView.showMissingReceipt();
            return;
        }
        mReceiptsRepository.deleteReceipt(mReceiptId);
        mReceiptDetailsView.showReceiptDeleted();
    }

    @Override
    public void repeatTransfer(final Receipt receipt) {
        if (receipt != null) {
            mReceiptDetailsView.showProgress();

            // This is a first repeat of the transfer
            if (Strings.isNullOrEmpty(receipt.getSourceCardId()) || Strings.isNullOrEmpty(receipt.getDestCardId())) {

                // We need to get cards ids
                transferService.getCardsIdsForInvoiceId(receipt.getInvoiceId(), new TransferContract.RequestCardsIdsCallback() {

                    @Override
                    public void onFailure(String errorMessage) {
                        Log.d("TRANSFER REPEAT", "Cards ids fetching ERROR: " + errorMessage);

                        // TODO: show error message to the customer
                        mReceiptDetailsView.hideProgress();
                    }

                    @Override
                    public void onSuccess(CardsIdsResponse result) {
                        mReceiptDetailsView.hideProgress();

                        receipt.setSourceCardId(result.getDestinationCardRefId());
                        receipt.setDestCardId(result.getSourceCardRefId());

                        // Successfully fetched cards ids. Persist data in DB
                        mReceiptsRepository.saveReceiptCardsIds(receipt);

                        repeatTransfer(receipt);
                    }
                });
            }
            // We have cards ids so can do the repeat
            else {
                mReceiptDetailsView.showRepeatTransferUI(receipt);
            }
        } else {
            Log.d("TRANSFER REPEAT", "receipt is NULL");
            mReceiptDetailsView.hideProgress();
        }
    }
}
