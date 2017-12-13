package com.payneteasy.dengisend.domain.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Dengisend
 *
 * Created by Alex Oleynyak on 01/08/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

public class Rate {

    @NonNull
    private final Double interest;

    @NonNull
    private final Double rateMin;

    @Nullable
    private final Double rateMax;

    @NonNull
    private final Double limitMin;

    @NonNull
    private final Double limitMax;

    public Rate(@NonNull Double interest, @NonNull Double rateMin, @Nullable Double rateMax, @NonNull Double limitMin, @NonNull Double limitMax) {
        this.interest = interest;
        this.rateMin = rateMin;
        this.rateMax = rateMax;
        this.limitMin = limitMin;
        this.limitMax = limitMax;
    }

    @NonNull
    public Double getInterest() {
        return interest;
    }

    @NonNull
    public Double getRateMin() {
        return rateMin;
    }

    @Nullable
    public Double getRateMax() {
        return rateMax;
    }

    @NonNull
    public Double getLimitMin() {
        return limitMin;
    }

    @NonNull
    public Double getLimitMax() {
        return limitMax;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rate)) return false;

        Rate rate = (Rate) o;

        if (!interest.equals(rate.interest)) return false;
        if (!rateMin.equals(rate.rateMin)) return false;
        if (rateMax != null ? !rateMax.equals(rate.rateMax) : rate.rateMax != null) return false;
        if (!limitMin.equals(rate.limitMin)) return false;
        return limitMax.equals(rate.limitMax);

    }

    @Override
    public int hashCode() {
        int result = interest.hashCode();
        result = 31 * result + rateMin.hashCode();
        result = 31 * result + (rateMax != null ? rateMax.hashCode() : 0);
        result = 31 * result + limitMin.hashCode();
        result = 31 * result + limitMax.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Rate{" +
                "interest=" + interest +
                ", rateMin=" + rateMin +
                ", rateMax=" + rateMax +
                ", limitMin=" + limitMin +
                ", limitMax=" + limitMax +
                '}';
    }
}