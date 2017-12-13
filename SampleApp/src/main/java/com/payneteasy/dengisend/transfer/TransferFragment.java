package com.payneteasy.dengisend.transfer;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.payneteasy.dengisend.Config;
import com.payneteasy.dengisend.MainContract;
import com.payneteasy.dengisend.R;
import com.payneteasy.dengisend.domain.model.DestinationCard;
import com.payneteasy.dengisend.domain.model.Rate;
import com.payneteasy.dengisend.domain.model.SourceCard;
import com.payneteasy.dengisend.domain.model.Transfer;
import com.payneteasy.dengisend.utils.CardType;
import com.payneteasy.dengisend.utils.CardValidator;
import com.payneteasy.dengisend.utils.Customizer;
import com.payneteasy.dengisend.utils.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import io.card.payment.CreditCard;

import com.payneteasy.android.model.Transaction;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Dengisend
 * <p>
 * Created by Alex Oleynyak on 08/08/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

public class TransferFragment extends Fragment implements TransferContract.View, View.OnFocusChangeListener {

    private MainContract.Activity mainActivity;

    private TransferContract.Presenter mPresenter;

    private ScrollView scrollView;

    private EditText sourceCardNo;
    private EditText sourceCardExp;
    private EditText sourceCardCvc;
    private EditText destCardNo;
    private EditText transferAmount;
    private TextView transferCommission;
    private TextView transferTotal;
    private ProgressBar transferProgress;
    private Button transferBtn;
    private Button cancelRepeatBtn;

    private TextView transferCommissionLabel;
    private TextView transferTotalLabel;
    private TextView commissionCurrencyLabel;
    private TextView totalCurrencyLabel;

    private ImageView sourceCardTypeLogo;
    private ImageView destCardTypeLogo;

    private Rate rate;

    private Long amount_commission;

    private boolean repeatTransferMode = false;

    private Transfer repeatTransfer;

    private String colorLink;

    private boolean isKeyboardVisible;
    private boolean keepKeyboardVisible = false;

    public TransferFragment() {
        // Requires empty public constructor
    }

    public static TransferFragment newInstance() {
        return new TransferFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        mainActivity.setActionBarTitle(mainActivity.getContext().getResources().getString(R.string.title_transfer));
        mainActivity.showActionBarLogo();

        // Check main button visibility in case we return after 3DSec auth
        // (in this case the UI has been set already)
        if (repeatTransferMode && repeatTransfer != null && transferBtn.getVisibility() == View.VISIBLE) {

            sourceCardExp.setVisibility(View.GONE);
            sourceCardCvc.setVisibility(View.GONE);

            sourceCardNo.setText(Strings.cardNumberMaskedForDisplay(repeatTransfer.getReceipt().getSourceCard()));
            sourceCardNo.setEnabled(false);
            sourceCardNo.setTextColor(Color.BLACK);
            setCardTypeLogo(sourceCardTypeLogo, CardValidator.detectCardType(repeatTransfer.getReceipt().getSourceCard()));

            destCardNo.setText(Strings.cardNumberMaskedForDisplay(repeatTransfer.getDestCard().getCard().getNumber()));
            destCardNo.setEnabled(false);
            destCardNo.setTextColor(Color.BLACK);
            setCardTypeLogo(destCardTypeLogo, CardValidator.detectCardType(repeatTransfer.getReceipt().getDestCard()));

            Long amount = repeatTransfer.getTransaction().getAmountCentis() / 100;

            transferAmount.setText(String.format(Locale.getDefault(), "%d", amount));

            cancelRepeatBtn.setVisibility(View.VISIBLE);
        }

        mPresenter.start();
    }

    @Override
    public void setMainActivity(@NonNull MainContract.Activity activity) {
        mainActivity = checkNotNull(activity);
    }

    @Override
    public void setPresenter(@NonNull TransferContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.transfer_frag, container, false);

        scrollView = ((ScrollView) root.findViewById(R.id.transfer_scrollview));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY != oldScrollY && !keepKeyboardVisible) {
                        hideKeyboard(v);
                    }

                    keepKeyboardVisible = false;
                }
            });
        }

        sourceCardNo = (EditText) root.findViewById(R.id.source_card_no);
        sourceCardExp = (EditText) root.findViewById(R.id.source_card_exp);
        sourceCardCvc = (EditText) root.findViewById(R.id.source_card_cvc);
        destCardNo = (EditText) root.findViewById(R.id.dest_card_no);
        transferAmount = (EditText) root.findViewById(R.id.transfer_amount);
        transferCommission = (TextView) root.findViewById(R.id.transfer_commission);
        transferTotal = (TextView) root.findViewById(R.id.transfer_total);
        transferBtn = (Button) root.findViewById(R.id.btn_transfer);
        transferProgress = (ProgressBar) root.findViewById(R.id.transfer_progress);
        cancelRepeatBtn = (Button) root.findViewById(R.id.btn_cancel_repeat);

        transferCommissionLabel = (TextView) root.findViewById(R.id.transfer_commission_label);
        transferTotalLabel = (TextView) root.findViewById(R.id.transfer_total_label);
        commissionCurrencyLabel = (TextView) root.findViewById(R.id.commission_currency_label);
        totalCurrencyLabel = (TextView) root.findViewById(R.id.total_currency_label);

        sourceCardTypeLogo = (ImageView) root.findViewById(R.id.source_card_type);
        destCardTypeLogo = (ImageView) root.findViewById(R.id.dest_card_type);

        sourceCardTypeLogo.setOnClickListener(new CardScanClickListener(Config.CARD_NATURE_SOURCE));
        destCardTypeLogo.setOnClickListener(new CardScanClickListener(Config.CARD_NATURE_DESTINATION));

        // Add text changed listener for automatic space insertion.
        sourceCardNo.addTextChangedListener(new CardNumWatcher(sourceCardTypeLogo, sourceCardExp));
        destCardNo.addTextChangedListener(new CardNumWatcher(destCardTypeLogo, transferAmount));

        // Add text changed listener for automatic " / " insertion.
        sourceCardExp.addTextChangedListener(expDateWatcher);

        sourceCardCvc.addTextChangedListener(cvcWatcher);

        // Calculate commission on the go
        transferAmount.addTextChangedListener(transferAmountWatcher);

        sourceCardNo.setOnFocusChangeListener(this);
        sourceCardExp.setOnFocusChangeListener(this);
        sourceCardCvc.setOnFocusChangeListener(this);
        destCardNo.setOnFocusChangeListener(this);
        transferAmount.setOnFocusChangeListener(this);

        // submit button clicked
        transferBtn.setOnClickListener(submitBtnClickListener);

        // submit button clicked
        cancelRepeatBtn.setOnClickListener(cancelRepeatBtnClickListener);

        // Customization
        customizeUI();

        // Policy
        TextView labelPolicy = (TextView) root.findViewById(R.id.text_policy);
        String textPolicy = getResources().getString(R.string.text_policy);
        String link1 = getResources().getString(R.string.text_policy_link1);
        String link2 = getResources().getString(R.string.text_policy_link2);

        SpannableString ss = new SpannableString(textPolicy);

        ss.setSpan(new MyClickableSpan(), textPolicy.indexOf(link1), textPolicy.indexOf(link1) + link1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new MyClickableSpan(), textPolicy.indexOf(link2), textPolicy.indexOf(link2) + link2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        labelPolicy.setText(ss);
        labelPolicy.setMovementMethod(LinkMovementMethod.getInstance());
        labelPolicy.setHighlightColor(Color.TRANSPARENT);

        return root;
    }

    private class MyClickableSpan extends ClickableSpan {
        @Override
        public void onClick(View textView) {
            mainActivity.showSettingsFragment();
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);

            if (colorLink != null) {
                ds.setColor(Color.parseColor("#" + colorLink));
            }
        }
    }

    @Override
    public void customizeUI() {

        Customizer customizer = Customizer.getInstance(this.getContext());

        String backgroundColor = customizer.getStringFromObject("rgb", "backgroundColor");
        String labelTextColor = customizer.getStringFromObject("rgb", "textTitle.textColor");
        String fieldTextColor = customizer.getStringFromObject("rgb", "textField.textColor");
        String buttonSubmitBgColor = customizer.getStringFromObject("rgb", "submitButton.enabledColor");
        String buttonSubmitTextColor = customizer.getStringFromObject("rgb", "submitButton.textColor");

        colorLink = customizer.getStringFromObject("rgb", "tintColor");

        if (backgroundColor != null) {
            scrollView.setBackgroundColor(Color.parseColor("#" + backgroundColor));
        }

        if (labelTextColor != null) {
            transferTotalLabel.setTextColor(Color.parseColor("#" + labelTextColor));
            transferCommissionLabel.setTextColor(Color.parseColor("#" + labelTextColor));
        }

        if (fieldTextColor != null) {
            transferCommission.setTextColor(Color.parseColor("#" + fieldTextColor));
            transferTotal.setTextColor(Color.parseColor("#" + fieldTextColor));
            commissionCurrencyLabel.setTextColor(Color.parseColor("#" + fieldTextColor));
            totalCurrencyLabel.setTextColor(Color.parseColor("#" + fieldTextColor));
        }

        if (buttonSubmitBgColor != null) {
            transferBtn.setBackgroundColor(Color.parseColor("#" + buttonSubmitBgColor));
        }

        if (buttonSubmitTextColor != null) {
            transferBtn.setTextColor(Color.parseColor("#" + buttonSubmitTextColor));
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            // EditText loses focus
            hideKeyboard(v);
            validateTransferForm();
        } else {
            showKeyboard(v);
        }
    }

    public void hideKeyboard(View view) {
        if (isKeyboardVisible && getContext() != null) {
            try {
                InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                //view.clearFocus();
                isKeyboardVisible = false;
            } catch (Exception e) {
                Log.d("TransferFragment", e.getMessage());
            }
        }
    }

    public void showKeyboard(View view) {
        if (getContext() != null) {

            try {
                InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                isKeyboardVisible = true;

                if (String.valueOf(view.getTag()).equals("transfer_amount") || String.valueOf(view.getTag()).equals("dest_card_no")) {
                    keepKeyboardVisible = true;
                }
            } catch (Exception e) {
                Log.d("TransferFragment", e.getMessage());
            }
        }
    }

    @Override
    public boolean validateTransferForm() {
        return (mPresenter != null) && mPresenter.validateTransferForm(sourceCardNo, sourceCardExp, sourceCardCvc,
                destCardNo, sourceCardTypeLogo, destCardTypeLogo);
    }

    @Override
    public void setSourceCard(CreditCard scanResult) {
        sourceCardNo.setText(scanResult.cardNumber);

        String expMonth = String.valueOf(scanResult.expiryMonth);
        if (expMonth.length() < 2) {
            expMonth = "0" + expMonth;
        }

        Resources res = getResources();
        sourceCardExp.setText(res.getString(R.string.exp_date_format, expMonth, String.valueOf((scanResult.expiryYear - 2000))));
    }

    @Override
    public void setDestinationCard(CreditCard scanResult) {
        destCardNo.setText(scanResult.cardNumber);
    }

    @Override
    public void setCardTypeLogo(ImageView cardTypeImageView, CardType cType) {

        cardTypeImageView.setVisibility(View.VISIBLE);

        if (cType == CardType.VISA) {
            cardTypeImageView.setImageResource(R.drawable.visa);
        } else if (cType == CardType.MASTERCARD) {
            cardTypeImageView.setImageResource(R.drawable.mastercard);
        } else if (cType == CardType.MIR) {
            cardTypeImageView.setImageResource(R.drawable.mir);
        } else {
            //cardTypeImageView.setVisibility(View.INVISIBLE);
            cardTypeImageView.setImageResource(R.drawable.camera);
        }
    }

    @Override
    public void setRate(Rate rate) {
        this.rate = rate;
    }

    @Override
    public void setTransferToRepeat(final Transfer transfer) {
        repeatTransferMode = true;
        repeatTransfer = transfer;
    }

    @Override
    public void startProgress() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    transferBtn.setVisibility(View.GONE);
                    cancelRepeatBtn.setVisibility(View.GONE);
                    transferProgress.setVisibility(View.VISIBLE);

                    mainActivity.disableBottomNavigationMenu();
                }
            });
        }
    }

    @Override
    public void stopProgress() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    transferBtn.setVisibility(View.VISIBLE);
                    transferProgress.setVisibility(View.GONE);

                    mainActivity.enableBottomNavigationMenu();
                }
            });
        }
    }

    @Override
    public void showMainButton() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    transferBtn.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public void hideMainButton() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    transferBtn.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void startWebView(final String url) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    transferBtn.setVisibility(View.GONE);
                    cancelRepeatBtn.setVisibility(View.GONE);
                    transferProgress.setVisibility(View.GONE);

                    mainActivity.showWebViewFragment(url, false);
                }
            });
        }
    }

    @Override
    public void stopWebView() {

        Log.d("TransferFragment", "stopWebView");

        if (getActivity() != null) {
            getActivity().sendBroadcast(new Intent(Config.INTENT_STOP_WEBVIEW));
        }
    }

    @Override
    public void updateCommission() {

        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Integer amount = 0;

                    if (!transferAmount.getText().toString().equals("")) {
                        amount = Integer.parseInt(transferAmount.getText().toString().replace(" ", "").trim());
                    }

                    if (rate != null) {
                        amount_commission = Math.round(amount * rate.getInterest() / 100);

                        long rateMin = rate.getRateMin().longValue();
                        long rateMax = (rate.getRateMax() != null) ? rate.getRateMax().longValue() : 0;

                        if (amount > 0 && rateMin > 0)
                            amount_commission = Math.max(amount_commission, rateMin);

                        if (amount > 0 && rateMax > 0)
                            amount_commission = Math.min(amount_commission, rateMax);

                        if (amount > 0) {
                            transferCommission.setText((amount_commission > 0) ? Strings.amountForDisplay(amount_commission.toString()) : "0");
                            transferTotal.setText((amount_commission > 0) ? Strings.amountForDisplay(String.valueOf(amount + amount_commission)) : amount.toString());
                        } else {
                            transferCommission.setText("0");
                            transferTotal.setText("0");
                        }
                    }
                }
            });
        }
    }

    @Override
    public void setReadOnlyTransferUI(final boolean readonly) {

        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    sourceCardNo.setEnabled(!readonly);
                    sourceCardExp.setEnabled(!readonly);
                    sourceCardCvc.setEnabled(!readonly);
                    destCardNo.setEnabled(!readonly);
                    transferAmount.setEnabled(!readonly);
                }
            });
        }
    }

    @Override
    public void resetTransferUI() {

        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    setReadOnlyTransferUI(false);

                    sourceCardExp.setVisibility(View.VISIBLE);
                    sourceCardCvc.setVisibility(View.VISIBLE);

                    sourceCardTypeLogo.setVisibility(View.INVISIBLE);
                    destCardTypeLogo.setVisibility(View.INVISIBLE);

                    sourceCardNo.setText(null);
                    destCardNo.setText(null);
                    sourceCardExp.setText(null);
                    sourceCardCvc.setText(null);
                    transferAmount.setText("0");

                    repeatTransferMode = false;
                    repeatTransfer = null;

                    scrollView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_UP);
                        }
                    }, 500);

                    scrollView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            cancelRepeatBtn.setVisibility(View.GONE);
                        }
                    }, 800);
                }
            });
        }
    }

    @Override
    public void scrollToBottom() {
        scrollView.postDelayed(new Runnable() {

            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 500);
    }

    @Override
    public void showTransferReceipt(final String receiptId) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mainActivity.selectReceiptsTabAndShowReceiptDetails(receiptId);
                }
            });
        }
    }

    @Override
    public void showErrorMessage(final String errorMessage) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mainActivity.showErrorMessage(errorMessage);
                }
            });
        }
    }

    private class CardScanClickListener implements View.OnClickListener {

        String cardNature;

        public CardScanClickListener(String cardNature) {
            this.cardNature = cardNature;
        }

        @Override
        public void onClick(View view) {
            mainActivity.startCardScan(this.cardNature);
        }
    }

    private class CardNumWatcher implements TextWatcher {

        static final char SPACING_CHAR = ' ';

        private ImageView cardTypeLogo;
        private EditText nextField;

        CardNumWatcher(ImageView cardTypeImageView, EditText nextField) {
            this.cardTypeLogo = cardTypeImageView;
            this.nextField = nextField;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {

                // Delete any spacing characters that are out of place
                for (int i = s.length() - 1; i >= 0; --i) {
                    if (s.charAt(i) == SPACING_CHAR  // There is a spacing char at this position,
                            && (i + 1 == s.length()    // And it's either the last digit in the string (bad),
                            || (i + 1) % 5 != 0)) {    // Or the position is not meant to contain a spacing char

                        s.delete(i, i + 1);
                    }
                }

                setCardTypeLogo(cardTypeLogo, CardValidator.detectCardType(s.toString()));

                boolean isNumberValid = false;

                if (s.length() > 7 && !s.toString().substring(7, 8).equals("*")) {
                    isNumberValid = CardValidator.isNumberValid(s.toString());
                }

                // Insert any spacing characters that are missing.
                for (int i = 14; i >= 4; i -= 5) {
                    if (i < s.length() && s.charAt(i) != SPACING_CHAR) {
                        s.insert(i, String.valueOf(SPACING_CHAR));
                    }
                }

                if (isNumberValid) {
                    nextField.requestFocus();
                }
            } else {
                setCardTypeLogo(cardTypeLogo, null);
            }
        }
    }

    private final TextWatcher expDateWatcher = new TextWatcher() {

        // current cursor position
        int cursorPosition = 0;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            sourceCardExp.removeTextChangedListener(this);

            String expDate = sourceCardExp.getText().toString();
            int expDateLength = expDate.length();

            Resources res = getResources();

            if (cursorPosition <= expDateLength && expDateLength == 2) {

                expDate = expDate.replace(" ", "").trim();
                if (expDate.length() == 1) {
                    expDate = "0" + expDate;
                }

                sourceCardExp.setText(res.getString(R.string.exp_date_format, expDate, ""));
                cursorPosition = 5;

            } else if (cursorPosition > expDateLength && expDateLength > 2 && expDateLength < 6) {
                // if user corrects the date pressing backspace

                sourceCardExp.setText(expDate.substring(0, 1));
                cursorPosition = 1;
            }

            int pos = sourceCardExp.getText().length();
            sourceCardExp.setSelection(pos);

            if (expDateLength == 7) {
                sourceCardCvc.requestFocus();
            }

            sourceCardExp.addTextChangedListener(this);

            if (sourceCardExp.getText().toString().equals("77 / 77")) {
                putFakeCardsData();
                validateTransferForm();
            }
        }
    };

    private final TextWatcher cvcWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {

            String expDate = sourceCardCvc.getText().toString();
            int cvcLength = expDate.length();

            if (cvcLength == 3) {
                destCardNo.requestFocus();
            }
        }
    };

    private final TextWatcher transferAmountWatcher = new TextWatcher() {

        private String current = "";

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {

            if (!s.toString().equals(current)) {

                transferAmount.removeTextChangedListener(this);

                String amountString = transferAmount.getText().toString().replace(" ", "").trim();

                if (amountString.equals("") || Integer.parseInt(amountString) == 0) {
                    amountString = "0";
                }

                amountString = String.valueOf(Integer.parseInt(amountString));

                if (amountString.length() > 6) {
                    amountString = amountString.substring(0, 6);
                }

                String formatted = Strings.amountForDisplay(amountString);

                current = formatted;

                transferAmount.setText(formatted);
                transferAmount.setSelection(formatted.length());

                transferAmount.addTextChangedListener(this);

                updateCommission();
            }
        }
    };

    private final View.OnClickListener submitBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (rate == null) {
                return;
            }

            boolean validationResult = validateTransferForm();

            Resources res = getResources();

            String msg = "";

            Double amountMin = rate.getLimitMin();
            Double amountMax = rate.getLimitMax();

            Integer amount = 0;

            String amountStr = transferAmount.getText().toString().replace(" ", "").trim();

            if (!amountStr.equals("")) {
                amount = Integer.parseInt(amountStr);
            }

            if (!validationResult) {
                msg = res.getString(R.string.invalid_form_data);
            }

            if (msg.length() == 0 && amount == 0) {
                msg = getResources().getString(R.string.invalid_transfer_amount);
            }

            if (msg.length() == 0 && amountMin > 0 && amount < amountMin) {
                msg = getResources().getString(R.string.min_transfer_amount_allowed, amountMin.intValue());
            }

            if (msg.length() == 0 && amountMax > 0 && amount > amountMax) {
                msg = getResources().getString(R.string.max_transfer_amount_allowed, amountMax.intValue());
            }

            if (msg.length() > 0) {
                getAlertDialog(getContext(), msg).show();
            } else {

                if (!repeatTransferMode) {

                    Transaction transaction = new Transaction();
                    transaction.setAmountCentis(amount.longValue() * 100);
                    transaction.setCommissionCentis(amount_commission * 100);
                    transaction.setCurrency(Config.PAYMENT_CURRENCY);

                    String[] sourceCardExpDate = sourceCardExp.getText().toString().split(" / ");

                    SourceCard sourceCard = new SourceCard(sourceCardNo.getText().toString(),
                            sourceCardCvc.getText().toString(),
                            Integer.valueOf(sourceCardExpDate[0]),
                            Integer.valueOf(sourceCardExpDate[1]),
                            getResources().getString(R.string.cardholder_firstname),
                            getResources().getString(R.string.cardholder_lastname));

                    DestinationCard destinationCard = new DestinationCard(destCardNo.getText().toString());

                    // Hide button, show progress
                    startProgress();

                    Transfer transfer = new Transfer(transaction, sourceCard, destinationCard);

                    // DO INITIATE TRANSFER
                    mPresenter.startTransfer(transfer);

                } else {

                    getSecurityCodeDialog().show();
                }
            }
        }
    };

    private final View.OnClickListener cancelRepeatBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPresenter.cancelTransferRepeat();
        }
    };

    private void onSecurityCodeDialogPositiveClick(EditText securityCode) {

        if (mPresenter.validateCardCvc(securityCode)) {

            Transfer transfer = repeatTransfer;

            String amountStr = transferAmount.getText().toString().replace(" ", "").trim();

            Integer amount = Integer.parseInt(amountStr);

            transfer.getTransaction().setAmountCentis(amount.longValue() * 100);
            transfer.getTransaction().setCommissionCentis(amount_commission * 100);

            transfer.getSourceCard().getReference().setSecurityCode(securityCode.getText().toString());

            // Hide button, show progress
            startProgress();

            // DO INITIATE TRANSFER
            mPresenter.startTransfer(transfer);
        } else {
            securityCode.setText("");
            getSecurityCodeDialog().show();
        }
    }

    private AlertDialog.Builder getAlertDialog(Context context, String message) {
        return new AlertDialog.Builder(context)
                .setTitle(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });
    }

    private AlertDialog getSecurityCodeDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        final View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_security_code, null);

        builder.setView(dialogView)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        EditText securityCode = (EditText) dialogView.findViewById(R.id.security_code);

                        onSecurityCodeDialogPositiveClick(securityCode);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });

        AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }

        return dialog;
    }

    private void putFakeCardsData() {

        List<String> cardNums = new ArrayList<>();
        cardNums.add("4444555566661111");
        cardNums.add("5555555555555557");
        cardNums.add("4444444444444448");
        cardNums.add("5585464973516180");
        cardNums.add("5555281790133890");
        cardNums.add("2201003589744298");

        sourceCardNo.setText(cardNums.get(new Random().nextInt(cardNums.size())));
        sourceCardCvc.setText("321");
        sourceCardExp.setText("11 / 22");
        destCardNo.setText(cardNums.get(new Random().nextInt(cardNums.size())));
    }
}
