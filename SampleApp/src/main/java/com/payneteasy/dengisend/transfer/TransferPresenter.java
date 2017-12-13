package com.payneteasy.dengisend.transfer;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import com.payneteasy.dengisend.AppModule;
import com.payneteasy.dengisend.Config;
import com.payneteasy.dengisend.DengisendApplication;
import com.payneteasy.dengisend.data.source.ReceiptsRepository;
import com.payneteasy.dengisend.domain.model.Rate;
import com.payneteasy.dengisend.domain.model.Receipt;
import com.payneteasy.dengisend.domain.model.SourceCard;
import com.payneteasy.dengisend.domain.model.DestinationCard;
import com.payneteasy.dengisend.domain.model.Transfer;
import com.payneteasy.dengisend.utils.CardValidator;

import java.util.Calendar;

import javax.inject.Inject;

import com.payneteasy.android.model.RatesResponse;
import com.payneteasy.android.model.Transaction;

/**
 * Dengisend
 * <p>
 * Created by Alex Oleynyak on 08/08/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

public class TransferPresenter implements TransferContract.Presenter {

    private final TransferContract.View mTransferView;

    @Inject
    TransferContract.Service transferService;

    @Inject
    ReceiptsRepository receiptsRepository;

    private Rate rate;

    // If and when the rate were fetched
    private Long rateUpdateTimestamp;

    private String _redirectUrl;

    private boolean isWebViewStarted = false;

    private boolean repeatTransferMode = false;

    @Inject
    TransferPresenter(TransferContract.View transferView, Context context) {
        mTransferView = transferView;

        DaggerTransferComponent.builder()
                .appModule(new AppModule((DengisendApplication) context))
                .transferModule(new TransferModule())
                .build()
                .inject(this);
    }

    /**
     * Method injection is used here to safely reference {@code this} after the object is created.
     * For more information, see Java Concurrency in Practice.
     */
    @Inject
    void setupListeners() {
        mTransferView.setPresenter(this);
    }

    @Override
    public void start() {

        // 10 minutes ago
        Long timestampThreshold = System.currentTimeMillis() / 1000 - 600;

        if (rate == null || rateUpdateTimestamp == null || rateUpdateTimestamp < timestampThreshold ) {

            transferService.clearSession();

            // get access token and commission
            transferService.requestCommission(new TransferContract.RequestCommissionCallback() {
                @Override
                public void onFailure(String errorMessage) {
                    // Hide progress, show button
                    mTransferView.stopProgress();
                    mTransferView.showErrorMessage(Config.ERROR_MESSAGE_COMMISSION_UPD);
                    mTransferView.hideMainButton();
                }

                @Override
                public void onSuccess(RatesResponse result) {
                    rate = new Rate(result.getRateInterest(), result.getRateMin(), result.getRateMax(),
                            result.getLimitMin(), result.getLimitMax());

                    mTransferView.setRate(rate);
                    mTransferView.updateCommission();
                    mTransferView.showMainButton();

                    rateUpdateTimestamp = System.currentTimeMillis()/1000;
                }
            });

        } else {
            Log.d("CURRENT RATE", rate.toString());
        }
    }

    @Override
    public boolean validateTransferForm(EditText sourceCardNo, EditText sourceCardExp,
                                        EditText sourceCardCvc, EditText destCardNo,
                                        ImageView sourceCardTypeLogo, ImageView destCardTypeLogo) {

        boolean result = true;

        sourceCardNo.setTextColor(Color.BLACK);
        sourceCardExp.setTextColor(Color.BLACK);
        sourceCardCvc.setTextColor(Color.BLACK);
        destCardNo.setTextColor(Color.BLACK);

        if (!repeatTransferMode && !validateCardNum(sourceCardNo)) {
            result = false;
            if(sourceCardNo.getText().toString().length()>0){
                sourceCardNo.setTextColor(Color.RED);
            }
        }

        if (!repeatTransferMode && !validateCardExp(sourceCardExp)) {
            result = false;
            if(sourceCardExp.getText().toString().length()>0){
                sourceCardExp.setTextColor(Color.RED);
            }
        }

        if (!repeatTransferMode && !validateCardCvc(sourceCardCvc)) {
            result = false;
            if(sourceCardCvc.getText().toString().length()>0){
                sourceCardCvc.setTextColor(Color.RED);
            }
        }

        if (!repeatTransferMode && !validateCardNum(destCardNo)) {
            result = false;
            if(destCardNo.getText().toString().length()>0){
                destCardNo.setTextColor(Color.RED);
            }
        }

        if (mTransferView != null) {
            mTransferView.setCardTypeLogo(sourceCardTypeLogo, CardValidator.detectCardType(sourceCardNo.getText().toString()));
            mTransferView.setCardTypeLogo(destCardTypeLogo, CardValidator.detectCardType(destCardNo.getText().toString()));
        }

        return result;
    }

    @Override
    public boolean validateCardNum(EditText cardNo) {
        return CardValidator.isNumberValid(cardNo.getText().toString());
    }

    @Override
    public boolean validateCardExp(EditText cardExp) {

        String strExp = cardExp.getText().toString();

        if (strExp.length() < 7 || !strExp.matches("[0-9]{2}\\s{1}/\\s{1}[0-9]{2}")){
            return false;
        }

        int month = Integer.parseInt(strExp.substring(0, 2));
        int year = 2000 + Integer.parseInt(strExp.substring(5, 7));

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);

        return (month >= 1 && month <= 12) &&
                ((year > currentYear) || (year == currentYear && month >= currentMonth));
    }

    @Override
    public boolean validateCardCvc(EditText cardCvc) {

        String cvc = cardCvc.getText().toString();

        return (cvc.length() == 3 && TextUtils.isDigitsOnly(cvc));
    }

    @Override
    public void prepareTransferForRepeat(Receipt receipt){

        repeatTransferMode = true;

        Transaction transaction = new Transaction();
        transaction.setInvoiceId(receipt.getInvoiceId());
        transaction.setAmountCentis(receipt.getAmountCentis().longValue());
        transaction.setCommissionCentis(receipt.getCommissionCentis().longValue());
        transaction.setCurrency(receipt.getCurrency());

        SourceCard sourceCard = new SourceCard(receipt.getSourceCard(), receipt.getSourceCardId());

        DestinationCard destinationCard = new DestinationCard(receipt.getDestCard(), receipt.getDestCardId());

        Receipt newReceipt = new Receipt();
        newReceipt.setSourceCard(receipt.getSourceCard());
        newReceipt.setSourceCardId(receipt.getSourceCardId());
        newReceipt.setDestCard(receipt.getDestCard());
        newReceipt.setDestCardId(receipt.getDestCardId());
        newReceipt.setInvoiceId(receipt.getInvoiceId());
        newReceipt.setCurrency(receipt.getCurrency());

        Transfer transfer = new Transfer(transaction, sourceCard, destinationCard);
        transfer.setReceipt(newReceipt);

        mTransferView.setTransferToRepeat(transfer);
    }

    @Override
    public void cancelTransferRepeat(){
        repeatTransferMode = false;
        mTransferView.resetTransferUI();
    }

    @Override
    public void startTransfer(final Transfer transfer) {

        mTransferView.setReadOnlyTransferUI(true);

        transferService.initiateTransfer(transfer, new TransferContract.ProcessTransferCallback() {
            @Override
            public void onInitiateTransferFailure(String errorMessage) {
                // Hide progress, show button
                mTransferView.stopProgress();
                mTransferView.setReadOnlyTransferUI(false);
                mTransferView.showErrorMessage(Config.ERROR_MESSAGE_COMMON);
            }

            @Override
            public void onInitiateTransferSuccess() { }

            @Override
            public void onPerformTransferFailure(String errorMessage) {
                // Hide progress, show button
                mTransferView.stopProgress();
                mTransferView.setReadOnlyTransferUI(false);
                mTransferView.showErrorMessage(Config.ERROR_MESSAGE_COMMON);
            }

            @Override
            public void onPerformTransferSuccess() { }

            @Override
            public void onCheckTransferStatusFailure(String errorMessage) {
                // Hide progress, show button
                mTransferView.stopProgress();
                mTransferView.setReadOnlyTransferUI(false);
                mTransferView.showErrorMessage(Config.ERROR_MESSAGE_COMMON);
            }

            @Override
            public void onCheckTransferStatusSuccess() { }

            @Override
            public void onTransferProcessing() {

                if (isWebViewStarted) {

                    Log.d("TRANSFER STATUS OK", "going to stop webview");

                    mTransferView.startProgress();
                    mTransferView.stopWebView();
                    mTransferView.scrollToBottom();

                    isWebViewStarted = false;
                }
            }

            @Override
            public void onTransferRedirect(String redirectUrl) {
                if (_redirectUrl == null || !_redirectUrl.equals(redirectUrl)) {

                    Log.d("TRANSFER STATUS OK", "going to redirect to " + redirectUrl);

                    _redirectUrl = redirectUrl;

                    isWebViewStarted = true;

                    // start WebViewController with redirectUrl
                    mTransferView.startWebView(redirectUrl);
                }
            }

            @Override
            public void onTransferApproved(Receipt receipt) {
                mTransferView.stopProgress();
                mTransferView.resetTransferUI();

                receiptsRepository.saveReceipt(receipt);

                // Reset rate
                rate = null;

                // Reset session
                transferService.clearSession();

                // Show receipt
                mTransferView.showTransferReceipt(receipt.getOrderId());
            }

            @Override
            public void onTransferDeclined(Receipt receipt) {
                mTransferView.stopProgress();
                mTransferView.resetTransferUI();

                receiptsRepository.saveReceipt(receipt);

                // Reset rate
                rate = null;

                // Reset session
                transferService.clearSession();

                // Show receipt
                mTransferView.showTransferReceipt(receipt.getOrderId());
            }
        });
    }
}