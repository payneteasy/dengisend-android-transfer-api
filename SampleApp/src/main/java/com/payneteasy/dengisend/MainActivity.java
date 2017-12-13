package com.payneteasy.dengisend;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.payneteasy.dengisend.domain.model.Receipt;
import com.payneteasy.dengisend.feedback.FeedbackContract;
import com.payneteasy.dengisend.feedback.FeedbackFragment;
import com.payneteasy.dengisend.feedback.FeedbackModule;
import com.payneteasy.dengisend.receiptdetails.ReceiptDetailsContract;
import com.payneteasy.dengisend.receiptdetails.ReceiptDetailsFragment;
import com.payneteasy.dengisend.receiptdetails.ReceiptDetailsPresenterModule;
import com.payneteasy.dengisend.receipts.ReceiptsContract;
import com.payneteasy.dengisend.receipts.ReceiptsFragment;
import com.payneteasy.dengisend.receipts.ReceiptsPresenterModule;
import com.payneteasy.dengisend.settings.SettingsContract;
import com.payneteasy.dengisend.settings.SettingsFragment;
import com.payneteasy.dengisend.settings.SettingsPresenterModule;
import com.payneteasy.dengisend.transfer.TransferContract;
import com.payneteasy.dengisend.transfer.TransferFragment;
import com.payneteasy.dengisend.transfer.TransferPresenterModule;
import com.payneteasy.dengisend.utils.Customizer;
import com.payneteasy.dengisend.webview.WebViewFragment;

import java.util.ArrayList;
import java.util.List;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements MainContract.Activity {

    ActionBar actionBar;

    Drawable upArrow;

    BottomNavigationView bottomNavigationView;

    Menu bottomNavigationMenu;

    ReceiptsFragment receiptsFragment;

    TransferFragment transferFragment;

    SettingsFragment settingsFragment;

    WebViewFragment webViewFragment;

    BroadcastReceiver mReceiver;

    TextView actionBarTitle;

    ImageView actionBarLogo;

    String actionBarTitleColor;

    List<Fragment> fragmentList;

    @Inject
    ReceiptsContract.Presenter mReceiptsPresenter;

    @Inject
    ReceiptDetailsContract.Presenter receiptDetailsPresenter;

    @Inject
    TransferContract.Presenter mTransferPresenter;

    @Inject
    SettingsContract.Presenter mSettingsPresenter;

    @Inject
    FeedbackContract.Presenter feedbackPresenter;

    private static final int SOURCE_CARD_SCAN_REQUEST_CODE = 101;
    private static final int DESTINATION_CARD_SCAN_REQUEST_CODE = 102;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            selectFragment(item);

            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        actionBar = getSupportActionBar();

        View view = getLayoutInflater().inflate(R.layout.actionbar, null);
        ActionBar.LayoutParams layout = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBarTitle = (TextView) view.findViewById(R.id.toolbar_title);
        actionBarLogo = (ImageView) view.findViewById(R.id.toolbar_logo);

        upArrow = ContextCompat.getDrawable(getContext(), R.drawable.ic_arrow_back_black_24dp);

        actionBar.setHomeAsUpIndicator(upArrow);
        actionBar.setCustomView(view, layout);
        actionBar.setDisplayShowCustomEnabled(true);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Create Receipts fragment
        if (receiptsFragment == null) {
            receiptsFragment = ReceiptsFragment.newInstance();
        }

        // Create Transfer fragment
        if (transferFragment == null) {
            transferFragment = TransferFragment.newInstance();
        }

        // Create Settings fragment
        if (settingsFragment == null) {
            settingsFragment = SettingsFragment.newInstance();
        }

        // Create presenter
        DaggerAppComponent.builder()
                .appModule(new AppModule((DengisendApplication) getApplication()))
                .receiptsPresenterModule(new ReceiptsPresenterModule(receiptsFragment))
                .transferPresenterModule(new TransferPresenterModule(transferFragment))
                .settingsPresenterModule(new SettingsPresenterModule(settingsFragment))
                .receiptDetailsPresenterModule(new ReceiptDetailsPresenterModule())
                .feedbackModule(new FeedbackModule())
                .build()
                .inject(this);

        receiptsFragment.setMainActivity(this);
        receiptsFragment.setPresenter(mReceiptsPresenter);

        transferFragment.setMainActivity(this);
        transferFragment.setPresenter(mTransferPresenter);

        settingsFragment.setMainActivity(this);
        settingsFragment.setPresenter(mSettingsPresenter);

        // Select second menu item by default and show Fragment accordingly.
        bottomNavigationMenu = bottomNavigationView.getMenu();
        selectFragment(bottomNavigationMenu.getItem(1));

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent intent) {

                Log.d("MainActivity", "onReceive INTENT_STOP_WEBVIEW");

                String action = intent.getAction();
                if (action.equals(Config.INTENT_STOP_WEBVIEW)) {
                    closeWebViewFragment();
                }
            }
        };

        registerReceiver(mReceiver, new IntentFilter(Config.INTENT_STOP_WEBVIEW));

        fragmentList = new ArrayList<>();

        getSupportFragmentManager()
                .registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
                    @Override
                    public void onFragmentAttached(FragmentManager fm, Fragment f, Context context) {
                        super.onFragmentAttached(fm, f, context);

                        fragmentList.add(f);
                    }

                    @Override
                    public void onFragmentDetached(FragmentManager fm, Fragment f) {
                        super.onFragmentDetached(fm, f);

                        fragmentList.remove(f);

                        int i = fragmentList.size();

                        if (i > 0) {
                            Fragment fragment = fragmentList.get(i - 1);

                            if (fragment.isVisible() && fragment instanceof SettingsContract.View) {
                                ((SettingsContract.View) fragment).onViewVisible();
                            }
                        }

                    }
                }, true);

        // CUSTOMIZATION
        customizeUI();
    }

    @Override
    public void onPause(){
        super.onPause();

        try {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);

            // ... just so we can grab a window token from it
            View view = actionBarTitle.getRootView();
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            Log.d("MainActivity", e.getMessage());
        }
    }

    private void customizeUI() {

        Customizer customizer = Customizer.getInstance(this.getContext());

        // Bottom Navigation View
        if (bottomNavigationView != null) {

            String tabBarBackgroundColor = customizer.getStringFromObject("rgb", "tabBar.backgroundColor");
            String tabBarTextNormalColor = customizer.getStringFromObject("rgb", "tabBar.textNormalColor");
            String tabBarTextSelectedColor = customizer.getStringFromObject("rgb", "tabBar.textSelectedColor");

            int disabledColor = 0;

            if (tabBarBackgroundColor != null) {
                int tabBarBackgroundClr = Color.parseColor("#" + tabBarBackgroundColor);

                bottomNavigationView.setBackgroundColor(tabBarBackgroundClr);

                disabledColor = (tabBarBackgroundClr > -1)
                        ? customizer.manipulateColor(tabBarBackgroundClr, 1.3f)
                        : customizer.manipulateColor(tabBarBackgroundClr, 0.7f);
            }

            if (tabBarTextNormalColor != null && tabBarTextSelectedColor != null) {

                int[][] states = new int[][]{
                        new int[]{android.R.attr.state_pressed},  // pressed
                        new int[]{android.R.attr.state_checked},  // checked
                        new int[]{-android.R.attr.state_enabled}, // disabled
                        new int[]{}
                };

                int[] colors = new int[]{
                        Color.parseColor("#" + tabBarTextSelectedColor),
                        Color.parseColor("#" + tabBarTextSelectedColor),
                        disabledColor,
                        Color.parseColor("#" + tabBarTextNormalColor)
                };

                bottomNavigationView.setItemTextColor(new ColorStateList(states, colors));
                bottomNavigationView.setItemIconTintList(new ColorStateList(states, colors));
            }
        }

        // ACTIONBAR
        if (actionBar != null) {

            String actionBarBackgroundColor = customizer.getStringFromObject("rgb", "toolBar.backgroundColor");
            String actionBarTitleColor = customizer.getStringFromObject("rgb", "toolBar.titleColor");
            String actionBarLogoColor = customizer.getStringFromObject("rgb", "toolBar.logoColor");

            if (actionBarBackgroundColor != null) {
                actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#" + actionBarBackgroundColor)));
            }

            if (actionBarTitleColor != null) {
                this.actionBarTitleColor = actionBarTitleColor;
            }

            if (actionBarLogoColor != null) {
                actionBarLogo.setColorFilter(Color.parseColor("#" + actionBarLogoColor), PorterDuff.Mode.SRC_ATOP);
            }

            if (upArrow != null) {
                upArrow.setColorFilter(Color.parseColor("#" + actionBarTitleColor), PorterDuff.Mode.SRC_ATOP);
            }

            if (BuildConfig.BANK_ID.equals("rusnarbank")) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(70, 58, 0, 0);
                actionBarLogo.setLayoutParams(params);
            }
        }
    }

    @Override
    public void onDestroy() {
        try {
            if (mReceiver != null)
                unregisterReceiver(mReceiver);
        } catch (Exception e) {
            // log
        }
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void setActionBarTitle(String titleText) {

        actionBarTitle.setText(titleText);
        actionBarTitle.setVisibility(View.VISIBLE);

        if (actionBarTitleColor != null) {
            actionBarTitle.setTextColor(Color.parseColor("#" + actionBarTitleColor));
        }
    }

    @Override
    public void showActionBarLogo() {
        actionBarLogo.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideActionBarLogo() {
        actionBarLogo.setVisibility(View.GONE);
    }

    @Override
    public void showHomeButton() {
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void hideHomeButton() {
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public void disableBottomNavigationMenu() {
        if (bottomNavigationMenu != null) {
            int bnmSize = bottomNavigationMenu.size();

            for (int i = 0; i<bnmSize; i++) {
                bottomNavigationMenu.getItem(i).setEnabled(false);
            }
        }
    }

    @Override
    public void enableBottomNavigationMenu() {
        if (bottomNavigationMenu != null) {
            int bnmSize = bottomNavigationMenu.size();

            for (int i = 0; i<bnmSize; i++) {
                bottomNavigationMenu.getItem(i).setEnabled(true);
            }
        }
    }

    @Override
    public void showErrorMessage(String errorMessage) {
        if (errorMessage.equals(Config.ERROR_MESSAGE_COMMON)) {
            errorMessage = getResources().getString(R.string.error_occurred);
        }
        if (errorMessage.equals(Config.ERROR_MESSAGE_COMMISSION_UPD)) {
            errorMessage = getResources().getString(R.string.error_get_commission);
        }
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showRepeatTransferFragment(Receipt receipt) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                selectFragment(bottomNavigationMenu.getItem(1));
            }
        });

        mTransferPresenter.prepareTransferForRepeat(receipt);
    }

    @Override
    public void selectReceiptsTabAndShowReceiptDetails(String receiptId) {
        receiptsFragment.prepareToShowReceiptDetails(receiptId);
        selectFragment(bottomNavigationMenu.getItem(0));
    }

    @Override
    public void showSettingsFragment() {
        selectFragment(bottomNavigationMenu.getItem(2));
    }

    @Override
    public void showReceiptDetailsFragment(String receiptId) {

        // 1) clear receipt id for the next start (we might not need it then)
        receiptsFragment.prepareToShowReceiptDetails(null);

        // 2) preparing to start receipt details fragment
        ReceiptDetailsFragment receiptDetailsFragment = ReceiptDetailsFragment.newInstance();

        receiptDetailsPresenter.setReceiptDetailsView(receiptDetailsFragment);
        receiptDetailsPresenter.setReceiptId(receiptId);

        receiptDetailsFragment.setPresenter(receiptDetailsPresenter);
        receiptDetailsFragment.setMainActivity(this);

        // 3) start receipt details fragment
        pushFragment(receiptDetailsFragment, true, "replace");
    }

    @Override
    public void showFeedbackFragment(@Nullable Receipt receipt) {
        FeedbackFragment feedbackFragment = FeedbackFragment.newInstance();

        feedbackPresenter.setFeedbackView(feedbackFragment);
        feedbackPresenter.setReceipt(receipt);

        feedbackFragment.setMainActivity(this);
        feedbackFragment.setPresenter(feedbackPresenter);

        pushFragment(feedbackFragment, true, "replace");
    }

    @Override
    public void showWebViewFragment(String url, boolean setInitialScale) {

        Log.d("MainActivity", "showWebViewFragment with url: " + url);

        webViewFragment = WebViewFragment.newInstance();
        webViewFragment.setMainActivity(this);
        webViewFragment.setUrl(url);
        webViewFragment.setInitialScale(setInitialScale);

        pushFragment(webViewFragment, true, "add");
    }

    @Override
    public void closeWebViewFragment() {
        Log.d("MainActivity", "closeWebViewFragment");

        if (webViewFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(webViewFragment).commit();
        }
    }

    @Override
    public void startCardScan(String cardNature) {
        Intent scanIntent = new Intent(this, CardIOActivity.class);

        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false);
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false);
        scanIntent.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true);
        scanIntent.putExtra(CardIOActivity.EXTRA_USE_PAYPAL_ACTIONBAR_ICON, false);
        scanIntent.putExtra(CardIOActivity.EXTRA_LANGUAGE_OR_LOCALE, "ru");
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, true);

        if (cardNature.equals(Config.CARD_NATURE_SOURCE)) {
            scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true);
            startActivityForResult(scanIntent, SOURCE_CARD_SCAN_REQUEST_CODE);
        }

        if (cardNature.equals(Config.CARD_NATURE_DESTINATION)) {
            scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, false);
            startActivityForResult(scanIntent, DESTINATION_CARD_SCAN_REQUEST_CODE);
        }
    }

    /**
     * Perform action when bottom navigation menu item is selected.
     *
     * @param item Item that is selected.
     */
    @Override
    public void selectFragment(MenuItem item) {

        item.setChecked(true);

        hideHomeButton();

        switch (item.getItemId()) {
            case R.id.navigation_home:
                pushFragment(receiptsFragment, false, "replace");
                break;
            case R.id.navigation_transfer:
                pushFragment(transferFragment, false, "replace");
                break;
            case R.id.navigation_settings:
                pushFragment(settingsFragment, false, "replace");
                break;
        }
    }

    /**
     * Push any fragment into given id.
     *
     * @param selectedFragment An instance of Fragment to show into the given id.
     */
    @Override
    public void pushFragment(Fragment selectedFragment, boolean addToBackStack, String addOrReplace) {

        if (selectedFragment == null) {
            return;
        }

        if (!addToBackStack) {
            getSupportFragmentManager().popBackStack();
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (transaction != null) {

            if (addToBackStack) {
                transaction.addToBackStack(null);
            }

            if (addOrReplace.equals("add")) {
                transaction.add(R.id.content, selectedFragment);
            } else {
                transaction.replace(R.id.content, selectedFragment);
            }

            transaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            hideHomeButton();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
            CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

            if (requestCode == SOURCE_CARD_SCAN_REQUEST_CODE) {
                transferFragment.setSourceCard(scanResult);
            }

            if (requestCode == DESTINATION_CARD_SCAN_REQUEST_CODE) {
                transferFragment.setDestinationCard(scanResult);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (fragmentList != null && fragmentList.size() > 0) {
            for (Fragment fragment : fragmentList) {
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }
}