package com.payneteasy.dengisend.transfer;

import android.support.annotation.NonNull;
import android.widget.EditText;
import android.widget.ImageView;

import com.payneteasy.dengisend.BasePresenter;
import com.payneteasy.dengisend.BaseView;
import com.payneteasy.dengisend.MainContract;
import com.payneteasy.dengisend.domain.model.Rate;
import com.payneteasy.dengisend.domain.model.Receipt;
import com.payneteasy.dengisend.domain.model.Transfer;
import com.payneteasy.dengisend.utils.CardType;

import io.card.payment.CreditCard;
import com.payneteasy.android.model.AccessTokenResponse;
import com.payneteasy.android.model.CardsIdsResponse;
import com.payneteasy.android.model.RatesResponse;

/**
 * Dengisend
 * <p>
 * Created by Alex Oleynyak on 07/08/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

public interface TransferContract {

    interface View extends BaseView<TransferContract.Presenter> {

        void setMainActivity(@NonNull MainContract.Activity activity);

        void setCardTypeLogo(ImageView imageView, CardType cType);

        void setSourceCard(CreditCard scanResult);

        void setDestinationCard(CreditCard scanResult);

        void setRate(Rate rate);

        void setReadOnlyTransferUI(boolean readonly);

        void setTransferToRepeat(Transfer transfer);

        void updateCommission();

        void showMainButton();

        void hideMainButton();

        void startProgress();

        void stopProgress();

        void startWebView(final String url);

        void stopWebView();

        boolean validateTransferForm();

        void resetTransferUI();

        void scrollToBottom();

        void showTransferReceipt(String receiptId);

        void showErrorMessage(String errorMessage);

        void customizeUI();
    }

    interface Presenter extends BasePresenter {

        boolean validateTransferForm(EditText sourceCardNo, EditText sourceCardExp,
                                     EditText sourceCardCvc, EditText destCardNo,
                                     ImageView sourceCardTypeLogo, ImageView destCardTypeLogo);

        boolean validateCardNum(EditText cardNo);

        boolean validateCardExp(EditText cardExp);

        boolean validateCardCvc(EditText cardCvc);

        void startTransfer(Transfer transfer);

        void prepareTransferForRepeat(Receipt receipt);

        void cancelTransferRepeat();
    }

    interface Service {

        void clearSession();

        void requestCommission(RequestCommissionCallback requestCommissionCallback);

        void initiateTransfer(Transfer transfer, TransferContract.ProcessTransferCallback processTransferCallback);

        void performTransfer(Transfer transfer, TransferContract.ProcessTransferCallback processTransferCallback);

        void checkTransferStatus(Transfer transfer, TransferContract.ProcessTransferCallback processTransferCallback);

        void getCardsIdsForInvoiceId(final String invoiceId, TransferContract.RequestCardsIdsCallback cardsIdsCallback);
    }

    interface RequestCommissionCallback {

        void onFailure(String errorMessage);

        void onSuccess(RatesResponse result);
    }

    interface RequestAccessTokenCallback {

        void onFailure(String errorMessage);

        void onSuccess(AccessTokenResponse result);
    }

    interface ProcessTransferCallback {

        void onInitiateTransferFailure(String errorMessage);

        void onInitiateTransferSuccess();

        void onPerformTransferFailure(String errorMessage);

        void onPerformTransferSuccess();

        void onCheckTransferStatusFailure(String errorMessage);

        void onCheckTransferStatusSuccess();

        void onTransferProcessing();

        void onTransferRedirect(String redirectUrl);

        void onTransferApproved(Receipt receipt);

        void onTransferDeclined(Receipt receipt);
    }

    interface RequestCardsIdsCallback {

        void onFailure(String errorMessage);

        void onSuccess(CardsIdsResponse result);
    }
}
