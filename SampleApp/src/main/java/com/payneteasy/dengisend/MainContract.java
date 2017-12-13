package com.payneteasy.dengisend;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.payneteasy.dengisend.domain.model.Receipt;
import com.payneteasy.dengisend.transfer.TransferContract;

/**
 * Dengisend
 * <p>
 * Created by Alex Oleynyak on 04/09/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

public interface MainContract {

    interface Activity {

        void selectFragment(MenuItem item);

        void pushFragment(Fragment selectedFragment, boolean addToBackStack, String addOrReplace);

        void showSettingsFragment();

        void showRepeatTransferFragment(Receipt receipt);

        void showReceiptDetailsFragment(String receiptId);

        void showFeedbackFragment(@Nullable Receipt receipt);

        void showWebViewFragment(final String url, boolean setInitialScale);

        void closeWebViewFragment();

        void startCardScan(String cardNature);

        void selectReceiptsTabAndShowReceiptDetails(String receiptId);

        void showHomeButton();

        void hideHomeButton();

        void disableBottomNavigationMenu();

        void enableBottomNavigationMenu();

        void showErrorMessage(String errorMessage);

        void showActionBarLogo();

        void hideActionBarLogo();

        void setActionBarTitle(String title);

        Context getContext();
    }
}