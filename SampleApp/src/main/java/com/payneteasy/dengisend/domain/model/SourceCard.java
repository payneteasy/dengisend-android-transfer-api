package com.payneteasy.dengisend.domain.model;

import com.payneteasy.android.model.SourceOfFunds;
import com.payneteasy.android.model.SourceOfFundsCard;
import com.payneteasy.android.model.SourceOfFundsCardExpiry;
import com.payneteasy.android.model.SourceOfFundsCardHolder;
import com.payneteasy.android.model.SourceOfFundsReference;


/**
 * Dengisend
 * <p>
 * Created by Alex Oleynyak on 25/08/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

public class SourceCard extends SourceOfFunds {

    public SourceCard(String cardNo, String securityCode, Integer expMonth, Integer expYear,
                      String firstName, String lastName) {

        SourceOfFundsCardExpiry sourceOfFundsCardExpiry = new SourceOfFundsCardExpiry();
        sourceOfFundsCardExpiry.setMonth(expMonth);
        sourceOfFundsCardExpiry.setYear(2000 + expYear);

        SourceOfFundsCardHolder cardHolder = new SourceOfFundsCardHolder();
        cardHolder.setFirstName(firstName);
        cardHolder.setLastName(lastName);

        cardNo = cardNo.replace(" ", "").trim();

        SourceOfFundsCard sourceOfFundsCard = new SourceOfFundsCard();
        sourceOfFundsCard.setNumber(cardNo);
        sourceOfFundsCard.setSecurityCode(securityCode);
        sourceOfFundsCard.setExpiry(sourceOfFundsCardExpiry);
        sourceOfFundsCard.setHolder(cardHolder);

        this.setCard(sourceOfFundsCard);
    }

    public SourceCard(String cardNo, String cardRefId) {

        cardNo = cardNo.replace(" ", "").trim();

        SourceOfFundsCard sourceOfFundsCard = new SourceOfFundsCard();
        sourceOfFundsCard.setNumber(cardNo);

        SourceOfFundsReference sourceOfFundsReference = new SourceOfFundsReference();
        sourceOfFundsReference.clientCardId(cardRefId);

        this.setCard(sourceOfFundsCard);
        this.setReference(sourceOfFundsReference);
    }
}
