package com.payneteasy.dengisend.settings;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.payneteasy.dengisend.MainContract;
import com.payneteasy.dengisend.R;
import com.payneteasy.dengisend.utils.Customizer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Dengisend
 * <p>
 * Created by Alex Oleynyak on 08/08/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

public class SettingsFragment extends Fragment implements SettingsContract.View {

    private MainContract.Activity mainActivity;

    private SettingsContract.Presenter mPresenter;

    TextView rulesTV;
    TextView ratesTV;
    TextView offer1TV;
    TextView offer2TV;

    String homeAddress;
    String rulesAddress;
    String ratesAddress;
    String offer1Address;
    String offer2Address;

    public SettingsFragment() {
        // Requires empty public constructor
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        onViewVisible();

        mPresenter.start();
    }

    @Override
    public void onViewVisible(){
        mainActivity.setActionBarTitle(mainActivity.getContext().getResources().getString(R.string.title_settings));
        mainActivity.hideActionBarLogo();

    }

    @Override
    public void setMainActivity(@NonNull MainContract.Activity activity) {
        mainActivity = checkNotNull(activity);
    }

    @Override
    public void setPresenter(@NonNull SettingsContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.settings_frag, container, false);

        // Set up view
        TextView offersHeader = (TextView) root.findViewById(R.id.offers_title);

        offer1TV = (TextView) root.findViewById(R.id.offer1);
        offer2TV = (TextView) root.findViewById(R.id.offer2);

        rulesTV = (TextView) root.findViewById(R.id.settings_rules);
        ratesTV = (TextView) root.findViewById(R.id.settings_rates);

        TextView feedbackTV = (TextView) root.findViewById(R.id.settings_feedback);
        TextView websiteTV = (TextView) root.findViewById(R.id.settings_website);

        TextView mDivider = (TextView) root.findViewById(R.id.divider);
        View mDivider2 = root.findViewById(R.id.divider2);

        Drawable drawable = getContext().getResources().getDrawable(R.drawable.greydivider).mutate();
        drawable.setColorFilter(new PorterDuffColorFilter(0xff000000, PorterDuff.Mode.MULTIPLY));

        offersHeader.setBackgroundDrawable(drawable);
        mDivider.setBackgroundDrawable(drawable);
        mDivider2.setBackgroundDrawable(drawable);

        websiteTV.setOnClickListener(websiteItemClickListener);
        feedbackTV.setOnClickListener(feedbackItemClickListener);

        // CUSTOMIZATION
        customizeUI();

        return root;
    }

    @Override
    public void customizeUI(){

        Customizer customizer = Customizer.getInstance(getActivity());

        homeAddress = customizer.getStringFromObject("url", "home");
        rulesAddress = customizer.getStringFromObject("url", "rules");
        ratesAddress = customizer.getStringFromObject("url", "rates");
        offer1Address = customizer.getStringFromObject("url", "offer1");
        offer2Address = customizer.getStringFromObject("url", "offer2");

        if (rulesAddress != null) {
            rulesTV.setOnClickListener(new mOnClickListener(rulesTV, rulesAddress));
        }

        if (ratesAddress != null) {
            ratesTV.setOnClickListener(new mOnClickListener(ratesTV, ratesAddress));
        }

        if (offer1Address != null) {
            offer1TV.setText(customizer.getStringFromObject("name", "offer1"));
            offer1TV.setOnClickListener(new mOnClickListener(offer1TV, offer1Address));
        } else {
            offer1TV.setVisibility(View.GONE);
        }

        if (offer2Address != null) {
            offer2TV.setText(customizer.getStringFromObject("name", "offer2"));
            offer2TV.setOnClickListener(new mOnClickListener(offer2TV, offer2Address));
        } else {
            offer2TV.setVisibility(View.GONE);
        }
    }

    private final View.OnClickListener websiteItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (homeAddress != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(homeAddress));
                startActivity(intent);
            }
        }
    };

    private final View.OnClickListener feedbackItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mainActivity.showFeedbackFragment(null);
        }
    };

    private class mOnClickListener implements View.OnClickListener {
        TextView mView;
        String mUrl;

        public mOnClickListener(View v, String url) {
            this.mView = (TextView) v;
            this.mUrl = url;
        }

        @Override
        public void onClick(View v) {
            mainActivity.setActionBarTitle(this.mView.getText().toString());
            mainActivity.hideActionBarLogo();
            mainActivity.showHomeButton();
            mainActivity.showWebViewFragment(this.mUrl, true);
        }
    }
}
