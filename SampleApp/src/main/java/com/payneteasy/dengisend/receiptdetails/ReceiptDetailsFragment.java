package com.payneteasy.dengisend.receiptdetails;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.payneteasy.dengisend.Config;
import com.payneteasy.dengisend.MainContract;
import com.payneteasy.dengisend.R;
import com.payneteasy.dengisend.domain.model.Receipt;
import com.payneteasy.dengisend.utils.ActivityUtils;
import com.payneteasy.dengisend.utils.Customizer;
import com.payneteasy.dengisend.utils.Strings;
import com.payneteasy.dengisend.utils.TransferStatus;

import java.io.File;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Dengisend
 * <p>
 * Created by Alex Oleynyak on 03/09/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

public class ReceiptDetailsFragment extends Fragment implements ReceiptDetailsContract.View {

    private ReceiptDetailsContract.Presenter mPresenter;

    private MainContract.Activity mainActivity;

    private LinearLayout screenshotRoot;

    private ProgressBar mProgress;

    private ScrollView mDetailsScrollView;

    private LinearLayout mDetailsList;

    private TextView mNoReceipt;

    private Receipt mReceipt;

    Button btnRepeatTransfer;
    Button btnShareReceipt;
    Button btnDeleteReceipt;
    Button btnReceiptFeedback;

    private String fieldNameColor;
    private String fieldValueColor;

    private String colorApproved = Config.STATUS_COLOR_APPROVED;
    private String colorDeclined = Config.STATUS_COLOR_DECLINED;

    private String receiptsDirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Dengisend";

    public static ReceiptDetailsFragment newInstance() {
        return new ReceiptDetailsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivity.setActionBarTitle(mainActivity.getContext().getResources().getString(R.string.title_receipt));
        mainActivity.showHomeButton();
        mainActivity.hideActionBarLogo();
        mPresenter.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setPresenter(@NonNull ReceiptDetailsContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void setMainActivity(@NonNull MainContract.Activity activity) {
        mainActivity = checkNotNull(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.receipt_details_frag, container, false);

        screenshotRoot = (LinearLayout) root.findViewById(R.id.screenshot_area);

        mDetailsList = (LinearLayout) root.findViewById(R.id.receipt_details_list);

        mDetailsScrollView = (ScrollView) root.findViewById(R.id.receipt_details_layout);
        mNoReceipt = (TextView) root.findViewById(R.id.receipt_not_found);
        mProgress = (ProgressBar) root.findViewById(R.id.receipt_progress);

        btnRepeatTransfer = (Button) root.findViewById(R.id.button_repeat_transfer);
        btnShareReceipt = (Button) root.findViewById(R.id.button_receipt_share);
        btnDeleteReceipt = (Button) root.findViewById(R.id.button_delete_receipt);
        btnReceiptFeedback = (Button) root.findViewById(R.id.button_receipt_feedback);

        // REPEAT button clicked
        btnRepeatTransfer.setOnClickListener(repeatTransferBtnClickListener);

        // SHARE button clicked
        btnShareReceipt.setOnClickListener(shareBtnClickListener);

        // FEEDBACK button clicked
        btnReceiptFeedback.setOnClickListener(feedbackBtnClickListener);

        // DELETE button clicked
        btnDeleteReceipt.setOnClickListener(deleteReceiptBtnClickListener);

        customizeUI();

        return root;
    }

    @Override
    public void customizeUI() {
        Customizer customizer = Customizer.getInstance(this.getContext());

        fieldNameColor = customizer.getStringFromObject("rgb", "receipt.textName.textColor");
        fieldValueColor = customizer.getStringFromObject("rgb", "receipt.textValue.textColor");

        String approvedColor = customizer.getStringFromObject("rgb", "approvedColor");
        String declinedColor = customizer.getStringFromObject("rgb", "declinedColor");

        String backgroundColor = customizer.getStringFromObject("rgb", "backgroundColor");
        if (backgroundColor != null) {
            mDetailsScrollView.setBackgroundColor(Color.parseColor("#" + backgroundColor));
        }

        if (approvedColor != null) {
            this.colorApproved = approvedColor;
        }

        if (declinedColor != null) {
            this.colorDeclined = declinedColor;
        }

        String buttonRepeatTextColor = customizer.getStringFromObject("rgb", "tableView.positiveColor");
        if (buttonRepeatTextColor != null) {
            btnRepeatTransfer.setTextColor(Color.parseColor("#" + buttonRepeatTextColor));
        }


        String buttonDeleteTextColor = customizer.getStringFromObject("rgb", "tableView.negativeColor");
        if (buttonDeleteTextColor != null) {
            btnDeleteReceipt.setTextColor(Color.parseColor("#" + buttonDeleteTextColor));
        }

        String buttonFeedbackTextColor = customizer.getStringFromObject("rgb", "tableView.textColor");
        if (buttonFeedbackTextColor != null) {
            btnReceiptFeedback.setTextColor(Color.parseColor("#" + buttonFeedbackTextColor));
            btnShareReceipt.setTextColor(Color.parseColor("#" + buttonFeedbackTextColor));
        }

        // TODO
        // bankName = [configUI get_StringForPath:@"name"];
    }

    @Override
    public void showReceipt(Receipt receipt) {

        mReceipt = receipt;

        parseReceiptDetails(receipt);

        mDetailsScrollView.setVisibility(View.VISIBLE);
        mNoReceipt.setVisibility(View.GONE);
    }

    @Override
    public void showProgress() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDetailsScrollView.setVisibility(View.GONE);
                mProgress.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgress() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDetailsScrollView.setVisibility(View.VISIBLE);
                mProgress.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void showMissingReceipt() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDetailsScrollView.setVisibility(View.GONE);
                mNoReceipt.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void showReceiptDeleted() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainActivity.hideHomeButton();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    @Override
    public void showRepeatTransferUI(Receipt receipt) {
        mainActivity.showRepeatTransferFragment(receipt);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    private void parseReceiptDetails(Receipt receipt) {
        mDetailsList.removeAllViews();

        LayoutInflater inflater = getActivity().getLayoutInflater();

        for (int i = 0; i < 8; i++) {

            if (i == 0) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200);
                lp.setMargins(80, 32, 80, 0);

                ImageView imageView = new ImageView(getActivity());
                imageView.setImageResource(R.drawable.logo);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setLayoutParams(lp);

                mDetailsList.addView(imageView);

                TextView textView = new TextView(getActivity());
                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setPadding(0, 8, 0, 10);
                textView.setText(R.string.title_receipt_p2p);
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                mDetailsList.addView(textView);
            }

            View rowView = inflater.inflate(R.layout.receipt_details_list_item, mDetailsList, false);

            TextView fieldName = (TextView) rowView.findViewById(R.id.fieldName);
            TextView fieldValue = (TextView) rowView.findViewById(R.id.fieldValue);

            if (fieldNameColor != null) {
                fieldName.setTextColor(Color.parseColor("#" + fieldNameColor));
            }

            if (fieldValueColor != null) {
                fieldValue.setTextColor(Color.parseColor("#" + fieldValueColor));
            }

            switch (i) {
                case 0:
                    fieldName.setText("Статус перевода");
                    fieldValue.setText(TransferStatus.localizedString(receipt.getStatus()));
                    fieldValue.setTextColor(TransferStatus.valueOf(receipt.getStatus()) == TransferStatus.APPROVED
                            ? Color.parseColor("#" + colorApproved)
                            : Color.parseColor("#" + colorDeclined));
                    break;
                case 1:
                    fieldName.setText("Время операции");
                    fieldValue.setText(Strings.longStringFromDateTime(receipt.getDate()));
                    break;
                case 2:
                    fieldName.setText("Номер операции");
                    fieldValue.setText(receipt.getOrderId());
                    break;
                case 3:
                    fieldName.setText("Номер карты отправителя");
                    fieldValue.setText(Strings.cardNumberMaskedForDisplay(receipt.getSourceCard()));
                    break;
                case 4:
                    fieldName.setText("Номер карты получателя");
                    fieldValue.setText(Strings.cardNumberMaskedForDisplay(receipt.getDestCard()));
                    break;
                case 5:
                    fieldName.setText("Сумма перевода");
                    fieldValue.setText(getResources().getString(R.string.amount_rub, receipt.getAmountCentis() / 100));
                    break;
                case 6:
                    fieldName.setText("Комиссия");
                    fieldValue.setText(getResources().getString(R.string.amount_rub, receipt.getCommissionCentis() / 100));
                    break;
                case 7:
                    fieldName.setText("Итого с комиссией");
                    fieldValue.setText(getResources().getString(R.string.amount_rub,
                            (receipt.getAmountCentis() + receipt.getCommissionCentis()) / 100));
                    break;
                default:
                    fieldName.setText("");
                    fieldValue.setText("");
            }

            mDetailsList.addView(rowView);
        }
    }

    private final View.OnClickListener repeatTransferBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPresenter.repeatTransfer(mReceipt);
        }
    };

    private final View.OnClickListener shareBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // Check if we have all necessary permissions
            if (ActivityCompat.checkSelfPermission(getContext(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(getContext(),
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        Config.PERMISSIONS_STORAGE);
            } else {
                // Permissions already granted
                shareReceiptScreenshot();
            }
        }
    };

    private final View.OnClickListener feedbackBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mainActivity.showFeedbackFragment(mReceipt);
        }
    };

    private final View.OnClickListener deleteReceiptBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            new AlertDialog.Builder(getContext())
                    .setTitle("Удалить операцию из истории?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            mPresenter.deleteReceipt();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .show();
        }
    };

    private void shareReceiptScreenshot() {

        boolean result = false;

        Bitmap screenshot = ActivityUtils.getScreenshot(screenshotRoot);

        if (screenshot != null) {
            File screenshotFile = ActivityUtils.storeScreenshot(screenshot, "receipt-" + mReceipt.getOrderId() + ".png", receiptsDirPath);

            if (screenshotFile != null) {
                result = ActivityUtils.shareImage(screenshotFile, ReceiptDetailsFragment.this);
            }
        }

        if (!result) {
            mainActivity.showErrorMessage(Config.ERROR_MESSAGE_COMMON);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        switch (requestCode) {
            case Config.PERMISSIONS_STORAGE: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Permissions granted
                    shareReceiptScreenshot();

                } else {

                    Log.d("ReceiptDetailsFragment", "permission denied");
                }
            }
        }
    }
}
