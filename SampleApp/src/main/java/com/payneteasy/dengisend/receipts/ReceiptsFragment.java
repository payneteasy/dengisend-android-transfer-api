/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.payneteasy.dengisend.receipts;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.payneteasy.dengisend.Config;
import com.payneteasy.dengisend.MainContract;
import com.payneteasy.dengisend.R;
import com.payneteasy.dengisend.domain.model.Receipt;
import com.payneteasy.dengisend.utils.Customizer;
import com.payneteasy.dengisend.utils.Strings;
import com.payneteasy.dengisend.utils.TransferStatus;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Display a grid of {@link Receipt}s. User can choose to view all, active or completed receipts.
 */
public class ReceiptsFragment extends Fragment implements ReceiptsContract.View {

    private MainContract.Activity mainActivity;

    private ReceiptsContract.Presenter mPresenter;

    private ReceiptsAdapter mListAdapter;

    private View mNoReceiptsView;

    private ImageView mNoReceiptIcon;

    private TextView mNoReceiptMainView;

    private LinearLayout mReceiptsView;

    private String showReceiptWithId;

    private String cellBackgroundColor;

    private String colorApproved = Config.STATUS_COLOR_APPROVED;
    private String colorDeclined = Config.STATUS_COLOR_DECLINED;

    public ReceiptsFragment() {
        // Requires empty public constructor
    }

    public static ReceiptsFragment newInstance() {
        return new ReceiptsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new ReceiptsAdapter(new ArrayList<Receipt>(0), mItemListener);
    }

    @Override
    public void onResume() {
        super.onResume();

        mainActivity.setActionBarTitle(mainActivity.getContext().getResources().getString(R.string.title_history));
        mainActivity.hideActionBarLogo();

        mPresenter.start();

        if (!com.google.common.base.Strings.isNullOrEmpty(showReceiptWithId)) {
            mainActivity.showReceiptDetailsFragment(showReceiptWithId);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.receipts_frag, container, false);

        // Set up receipts view
        ListView listView = (ListView) root.findViewById(R.id.receipts_list);
        listView.setAdapter(mListAdapter);
        mReceiptsView = (LinearLayout) root.findViewById(R.id.receiptsLL);

        // Set up  no receipts view
        mNoReceiptsView = root.findViewById(R.id.noReceipts);
        mNoReceiptIcon = (ImageView) root.findViewById(R.id.noReceiptsIcon);
        mNoReceiptMainView = (TextView) root.findViewById(R.id.noReceiptsMain);

        customizeUI();

        return root;
    }

    @Override
    public void customizeUI() {
        Customizer customizer = Customizer.getInstance(this.getContext());

        String backgroundColor = customizer.getStringFromObject("rgb", "backgroundColor");
        String cellBackgroundColor = customizer.getStringFromObject("rgb", "tableView.backgroundColor");
        String approvedColor = customizer.getStringFromObject("rgb", "approvedColor");
        String declinedColor = customizer.getStringFromObject("rgb", "declinedColor");

        if (backgroundColor != null) {
            mReceiptsView.setBackgroundColor(Color.parseColor("#" + backgroundColor));
            //mNoReceiptsView.setBackgroundColor(Color.parseColor("#" + backgroundColor));
        }

        if (cellBackgroundColor != null) {
            this.cellBackgroundColor = cellBackgroundColor;
        }

        if (approvedColor != null) {
            this.colorApproved = approvedColor;
        }

        if (declinedColor != null) {
            this.colorDeclined = declinedColor;
        }
    }

    @Override
    public void setMainActivity(@NonNull MainContract.Activity activity) {
        mainActivity = checkNotNull(activity);
    }

    @Override
    public void setPresenter(@NonNull ReceiptsContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    /**
     * Listener for clicks on receipts in the ListView.
     */
    ReceiptItemListener mItemListener = new ReceiptItemListener() {
        @Override
        public void onReceiptClick(Receipt clickedReceipt) {
            mPresenter.openReceiptDetails(clickedReceipt);
        }
    };

    @Override
    public void setLoadingIndicator(final boolean active) {

        if (getView() == null) {
            return;
        }

        //TODO: SwipeRefreshLayout
    }

    @Override
    public void prepareToShowReceiptDetails(String receiptId) {
        showReceiptWithId = receiptId;
    }

    @Override
    public void showReceipts(List<Receipt> receipts) {
        mListAdapter.replaceData(receipts);

        mReceiptsView.setVisibility(View.VISIBLE);
        mNoReceiptsView.setVisibility(View.GONE);
    }

    @Override
    public void showNoReceipts() {
        mReceiptsView.setVisibility(View.GONE);
        mNoReceiptsView.setVisibility(View.VISIBLE);

        mNoReceiptMainView.setText(getResources().getString(R.string.no_receipts));
        mNoReceiptIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_circle_24dp));
    }

    @Override
    public void showReceiptDetailsUi(String receiptId) {
        mainActivity.showReceiptDetailsFragment(receiptId);
    }

    @Override
    public void showLoadingReceiptsError() {
        showMessage(getString(R.string.loading_receipts_error));
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    private class ReceiptsAdapter extends BaseAdapter {

        private final String ORDER_ID_SEPARATOR = "separator";

        private final int VIEW_TYPE_SEPARATOR = 0;
        private final int VIEW_TYPE_RECEIPT = 1;

        private List<Receipt> mReceipts;
        private ReceiptItemListener mItemListener;

        public ReceiptsAdapter(List<Receipt> receipts, ReceiptItemListener itemListener) {
            setList(receipts);
            mItemListener = itemListener;
        }

        public void replaceData(List<Receipt> receipts) {
            setList(receipts);
            notifyDataSetChanged();
        }

        private void setList(List<Receipt> receipts) {
            mReceipts = checkNotNull(receipts);

            List<Receipt> newReceipts = new ArrayList<>();

            String month = "";

            for (Receipt receipt : mReceipts) {

                String m = Strings.monthYearFromDateTime(receipt.getDate());

                if (!month.equals(m)) {
                    month = m;

                    Receipt separatorReceipt = new Receipt();
                    separatorReceipt.setOrderId(ORDER_ID_SEPARATOR);
                    separatorReceipt.setComment(m);

                    newReceipts.add(separatorReceipt);
                }

                newReceipts.add(receipt);
            }

            mReceipts = newReceipts;
        }

        @Override
        public int getCount() {
            return mReceipts.size();
        }

        @Override
        public Receipt getItem(int i) {
            return mReceipts.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public int getItemViewType(int position) {

            if (mReceipts.get(position).getOrderId().equals(ORDER_ID_SEPARATOR)) {
                return VIEW_TYPE_SEPARATOR;
            } else {
                return VIEW_TYPE_RECEIPT;
            }
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {

            final Receipt receipt = getItem(position);

            int type = getItemViewType(position);

            View rowView;

            if(type == VIEW_TYPE_SEPARATOR){
                rowView = getMonthYearView(receipt.getComment(), convertView, viewGroup);
            } else {
                rowView = getReceiptItemView(receipt, convertView, viewGroup);
            }

            return rowView;
        }

        private View getReceiptItemView(final Receipt receipt, View view, ViewGroup viewGroup) {

            View rowView = view;

            if (rowView == null) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                rowView = inflater.inflate(R.layout.receipts_list_item, viewGroup, false);
            }

            if (cellBackgroundColor != null) {
                rowView.setBackgroundColor(Color.parseColor("#" + cellBackgroundColor));
            }

            TextView date = (TextView) rowView.findViewById(R.id.transferDate);
            date.setText(Strings.longStringFromDateTime(receipt.getDate()));

            TextView status = (TextView) rowView.findViewById(R.id.transferStatus);
            status.setText(TransferStatus.localizedString(receipt.getStatus()));
            status.setTextColor(TransferStatus.valueOf(receipt.getStatus()) == TransferStatus.APPROVED
                    ? Color.parseColor("#" + colorApproved)
                    : Color.parseColor("#" + colorDeclined));

            TextView destCardNo = (TextView) rowView.findViewById(R.id.destCardNo);
            destCardNo.setText(Strings.cardNumberMaskedForDisplay(receipt.getDestCard()));

            int amountInt = receipt.getAmountCentis() / 100;
            String amountString = getResources().getString(R.string.amount_rub, amountInt);

            TextView amountTV = (TextView) rowView.findViewById(R.id.transferAmount);
            amountTV.setText(amountString);

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemListener.onReceiptClick(receipt);
                }
            });

            return rowView;
        }

        private View getMonthYearView(String month, View view, ViewGroup viewGroup) {

            View rowView = view;

            if (rowView == null) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                rowView = inflater.inflate(R.layout.receipts_list_month_year, viewGroup, false);
            }

            TextView monthYear = (TextView) rowView.findViewById(R.id.monthYear);
            monthYear.setText(month);

            return rowView;
        }
    }

    public interface ReceiptItemListener {

        void onReceiptClick(Receipt clickedReceipt);
    }
}