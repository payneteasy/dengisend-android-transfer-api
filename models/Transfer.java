package com.payneteasy.dengisend.domain.model;

import android.support.annotation.NonNull;

import com.payneteasy.android.model.Transaction;

public class Transfer {

    @NonNull
    private final Transaction transaction;

    @NonNull
    private final SourceCard sourceCard;

    @NonNull
    private final DestinationCard destCard;

    @NonNull
    private Receipt receipt;

    public Transfer(@NonNull Transaction transaction, @NonNull SourceCard sourceCard, @NonNull DestinationCard destCard) {
        this.transaction = transaction;
        this.sourceCard = sourceCard;
        this.destCard = destCard;

        this.receipt = new Receipt();
    }

    @NonNull
    public Transaction getTransaction() {
        return transaction;
    }

    @NonNull
    public SourceCard getSourceCard() {
        return sourceCard;
    }

    @NonNull
    public DestinationCard getDestCard() {
        return destCard;
    }

    @NonNull
    public Receipt getReceipt() {
        return receipt;
    }

    public void setReceipt(@NonNull Receipt receipt) {
        this.receipt = receipt;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "transaction=" + transaction +
                ", sourceCard=" + sourceCard +
                ", destCard=" + destCard +
                ", receipt=" + receipt +
                '}';
    }
}
