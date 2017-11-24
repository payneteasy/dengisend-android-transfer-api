package com.payneteasy.dengisend.domain.model;


public class Receipt {

    private String orderId;

    private String invoiceId;

    private String date;

    private String sourceCard;

    private String sourceCardId;

    private String destCard;

    private String destCardId;

    private Integer amountCentis;

    private Integer commissionCentis;

    private String currency;

    private String comment;

    private String status;

    public Receipt() {
    }

    public Receipt(String orderId, String invoiceId, String date, String sourceCard,
                   String sourceCardId, String destCard, String destCardId, Integer amountCentis,
                   Integer commissionCentis, String currency, String comment, String status) {
        this.orderId = orderId;
        this.invoiceId = invoiceId;
        this.date = date;
        this.sourceCard = sourceCard;
        this.sourceCardId = sourceCardId;
        this.destCard = destCard;
        this.destCardId = destCardId;
        this.amountCentis = amountCentis;
        this.commissionCentis = commissionCentis;
        this.currency = currency;
        this.comment = comment;
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSourceCard() {
        return sourceCard;
    }

    public void setSourceCard(String sourceCard) {
        this.sourceCard = sourceCard;
    }

    public String getSourceCardId() {
        return sourceCardId;
    }

    public void setSourceCardId(String sourceCardId) {
        this.sourceCardId = sourceCardId;
    }

    public String getDestCard() {
        return destCard;
    }

    public void setDestCard(String destCard) {
        this.destCard = destCard;
    }

    public String getDestCardId() {
        return destCardId;
    }

    public void setDestCardId(String destCardId) {
        this.destCardId = destCardId;
    }

    public Integer getAmountCentis() {
        return amountCentis;
    }

    public void setAmountCentis(Integer amountCentis) {
        this.amountCentis = amountCentis;
    }

    public Integer getCommissionCentis() {
        return commissionCentis;
    }

    public void setCommissionCentis(Integer commissionCentis) {
        this.commissionCentis = commissionCentis;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Receipt)) return false;

        Receipt receipt = (Receipt) o;

        if (orderId != null ? !orderId.equals(receipt.orderId) : receipt.orderId != null)
            return false;
        if (invoiceId != null ? !invoiceId.equals(receipt.invoiceId) : receipt.invoiceId != null)
            return false;
        if (date != null ? !date.equals(receipt.date) : receipt.date != null) return false;
        if (sourceCard != null ? !sourceCard.equals(receipt.sourceCard) : receipt.sourceCard != null)
            return false;
        if (sourceCardId != null ? !sourceCardId.equals(receipt.sourceCardId) : receipt.sourceCardId != null)
            return false;
        if (destCard != null ? !destCard.equals(receipt.destCard) : receipt.destCard != null)
            return false;
        if (destCardId != null ? !destCardId.equals(receipt.destCardId) : receipt.destCardId != null)
            return false;
        if (amountCentis != null ? !amountCentis.equals(receipt.amountCentis) : receipt.amountCentis != null)
            return false;
        if (commissionCentis != null ? !commissionCentis.equals(receipt.commissionCentis) : receipt.commissionCentis != null)
            return false;
        if (currency != null ? !currency.equals(receipt.currency) : receipt.currency != null)
            return false;
        if (comment != null ? !comment.equals(receipt.comment) : receipt.comment != null)
            return false;
        return status != null ? status.equals(receipt.status) : receipt.status == null;

    }

    @Override
    public int hashCode() {
        int result = orderId != null ? orderId.hashCode() : 0;
        result = 31 * result + (invoiceId != null ? invoiceId.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (sourceCard != null ? sourceCard.hashCode() : 0);
        result = 31 * result + (sourceCardId != null ? sourceCardId.hashCode() : 0);
        result = 31 * result + (destCard != null ? destCard.hashCode() : 0);
        result = 31 * result + (destCardId != null ? destCardId.hashCode() : 0);
        result = 31 * result + (amountCentis != null ? amountCentis.hashCode() : 0);
        result = 31 * result + (commissionCentis != null ? commissionCentis.hashCode() : 0);
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "orderId='" + orderId + '\'' +
                ", invoiceId='" + invoiceId + '\'' +
                ", date='" + date + '\'' +
                ", sourceCard='" + sourceCard + '\'' +
                ", sourceCardId='" + sourceCardId + '\'' +
                ", destCard='" + destCard + '\'' +
                ", destCardId='" + destCardId + '\'' +
                ", amountCentis=" + amountCentis +
                ", commissionCentis=" + commissionCentis +
                ", currency='" + currency + '\'' +
                ", comment='" + comment + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}