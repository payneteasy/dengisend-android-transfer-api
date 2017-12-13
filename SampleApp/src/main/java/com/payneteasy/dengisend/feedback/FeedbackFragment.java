package com.payneteasy.dengisend.feedback;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.payneteasy.dengisend.MainContract;
import com.payneteasy.dengisend.R;
import com.payneteasy.dengisend.utils.Customizer;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Dengisend
 * <p>
 * Created by Alex Oleynyak on 12/09/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

public class FeedbackFragment extends Fragment implements FeedbackContract.View {

    private MainContract.Activity mainActivity;

    private FeedbackContract.Presenter mPresenter;

    private TextView feedbackTitle;

    private EditText feedbackEmail;

    private EditText feedbackMessage;

    private Button sendFeedbackBtn;

    private ProgressBar feedbackProgress;

    public FeedbackFragment() {
        // Requires empty public constructor
    }

    public static FeedbackFragment newInstance() {
        return new FeedbackFragment();
    }

    @Override
    public void setMainActivity(@NonNull MainContract.Activity activity) {
        mainActivity = checkNotNull(activity);
    }

    @Override
    public void setPresenter(@NonNull FeedbackContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        mainActivity.setActionBarTitle(mainActivity.getContext().getResources().getString(R.string.title_feedback));
        mainActivity.hideActionBarLogo();
        mainActivity.showHomeButton();

        mPresenter.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.saveDraft();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.feedback_frag, container, false);

        // Set up receipts view
        feedbackTitle = (TextView) root.findViewById(R.id.feedback_title);
        feedbackEmail = (EditText) root.findViewById(R.id.feedback_email);
        feedbackMessage = (EditText) root.findViewById(R.id.feedback_message);

        sendFeedbackBtn = (Button) root.findViewById(R.id.btn_feedback_send);
        sendFeedbackBtn.setOnClickListener(sendFeedbackBtnClickListener);

        feedbackProgress = (ProgressBar) root.findViewById(R.id.feedback_progress);

        customizeUI();

        return root;
    }

    @Override
    public void customizeUI() {

        Customizer customizer = Customizer.getInstance(this.getContext());

        String backgroundColor = customizer.getStringFromObject("rgb", "backgroundColor");
        String labelTextColor = customizer.getStringFromObject("rgb", "textTitle.textColor");
        String fieldTextColor = customizer.getStringFromObject("rgb", "textField.textColor");
        String buttonBgColor = customizer.getStringFromObject("rgb", "submitButton.enabledColor");
        String buttonTextColor = customizer.getStringFromObject("rgb", "submitButton.textColor");

        if(backgroundColor!=null) {
            feedbackTitle.getRootView().setBackgroundColor(Color.parseColor("#" + backgroundColor));
        }

        if(labelTextColor!=null) {
            feedbackTitle.setTextColor(Color.parseColor("#" + labelTextColor));
        }

        if(fieldTextColor!=null) {
            feedbackEmail.setTextColor(Color.parseColor("#" + fieldTextColor));
            feedbackMessage.setTextColor(Color.parseColor("#" + fieldTextColor));
        }

        if (buttonBgColor != null) {
            sendFeedbackBtn.setBackgroundColor(Color.parseColor("#" + buttonBgColor));
        }

        if(buttonTextColor!=null) {
            sendFeedbackBtn.setTextColor(Color.parseColor("#" + buttonTextColor));
        }
    }

    private final View.OnClickListener sendFeedbackBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPresenter.sendFeedback();
        }
    };

    @Override
    public void setFeedbackTitleForReceipt(String orderId, String date) {
        feedbackTitle.setText(getResources().getString(R.string.feedback_title_for_receipt, orderId, date));
    }

    @Override
    public void setReadOnlyTransferUI(final boolean readonly) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                feedbackEmail.setEnabled(!readonly);
                feedbackMessage.setEnabled(!readonly);
            }
        });
    }

    @Override
    public void startProgress() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sendFeedbackBtn.setVisibility(View.GONE);
                feedbackProgress.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void stopProgress() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sendFeedbackBtn.setVisibility(View.VISIBLE);
                feedbackProgress.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public EditText getFeedbackEmail() {
        return feedbackEmail;
    }

    @Override
    public EditText getFeedbackMessage() {
        return feedbackMessage;
    }

    @Override
    public void showAlertDialog(final String message, final boolean goBackToReceipt) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(getContext())
                        .setTitle(message)
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            public void onDismiss(DialogInterface dialog) {
                                // Go back to receipt UI
                                if (goBackToReceipt) {
                                    feedbackMessage.setText(null);
                                    getActivity().getSupportFragmentManager().popBackStack();
                                }
                            }
                        })
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // OnDismissListener making the job if necessary
                            }
                        }).show();
            }
        });
    }
}
