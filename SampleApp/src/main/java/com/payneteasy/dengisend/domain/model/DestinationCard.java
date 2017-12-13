package com.payneteasy.dengisend.domain.model;

import com.payneteasy.android.model.DestinationOfFunds;
import com.payneteasy.android.model.DestinationOfFundsCard;
import com.payneteasy.android.model.DestinationOfFundsReference;

/**
 * Dengisend
 * <p>
 * Created by Alex Oleynyak on 25/08/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

public class DestinationCard extends DestinationOfFunds {

    public DestinationCard(String cardNo) {

        cardNo = cardNo.replace(" ", "").trim();

        DestinationOfFundsCard destinationOfFundsCard = new DestinationOfFundsCard();
        destinationOfFundsCard.setNumber(cardNo);

        this.setCard(destinationOfFundsCard);
    }

    public DestinationCard(String cardNo, String cardRefId) {

        cardNo = cardNo.replace(" ", "").trim();

        DestinationOfFundsCard destinationOfFundsCard = new DestinationOfFundsCard();
        destinationOfFundsCard.setNumber(cardNo);

        DestinationOfFundsReference destinationOfFundsReference = new DestinationOfFundsReference();
        destinationOfFundsReference.clientCardId(cardRefId);

        this.setCard(destinationOfFundsCard);
        this.setReference(destinationOfFundsReference);
    }
}
