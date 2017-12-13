package com.payneteasy.dengisend;

import android.content.Context;

import com.payneteasy.dengisend.data.source.ReceiptsRepositoryModule;
import com.payneteasy.dengisend.feedback.FeedbackModule;
import com.payneteasy.dengisend.receiptdetails.ReceiptDetailsPresenterModule;
import com.payneteasy.dengisend.receipts.ReceiptsPresenterModule;
import com.payneteasy.dengisend.settings.SettingsPresenterModule;
import com.payneteasy.dengisend.transfer.TransferModule;
import com.payneteasy.dengisend.transfer.TransferPresenterModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {
                AppModule.class,
                ReceiptsRepositoryModule.class,
                ReceiptsPresenterModule.class,
                ReceiptDetailsPresenterModule.class,
                TransferModule.class,
                TransferPresenterModule.class,
                SettingsPresenterModule.class,
                FeedbackModule.class
        }
)
public interface AppComponent {

    void inject(MainActivity activity);

    Context context();
}