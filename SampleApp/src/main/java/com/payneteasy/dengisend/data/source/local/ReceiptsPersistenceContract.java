/*
 * Dengisend
 *
 * Created by Alex Oleynyak on 02/08/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

package com.payneteasy.dengisend.data.source.local;

import android.provider.BaseColumns;

/**
 * The contract used for the db to save the receipts locally.
 */
public final class ReceiptsPersistenceContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private ReceiptsPersistenceContract() {
    }

    /* Inner class that defines the table contents */
    public static abstract class ReceiptEntry implements BaseColumns {

        public static final String TABLE_NAME = "receipt";
        public static final String COLUMN_NAME_ORDER_ID = "orderId";
        public static final String COLUMN_NAME_INVOICE_ID = "invoiceId";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_SOURCE_CARD = "sourceCard";
        public static final String COLUMN_NAME_SOURCE_CARD_ID = "sourceCardId";
        public static final String COLUMN_NAME_DEST_CARD = "destCard";
        public static final String COLUMN_NAME_DEST_CARD_ID = "destCardId";
        public static final String COLUMN_NAME_AMOUNT_CENTIS = "amountCentis";
        public static final String COLUMN_NAME_COMMISSION_CENTIS = "commissionCentis";
        public static final String COLUMN_NAME_CURRENCY = "currency";
        public static final String COLUMN_NAME_COMMENT = "comment";
        public static final String COLUMN_NAME_STATUS = "status";
    }
}