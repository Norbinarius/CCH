package ru.artsec.cch.model;

/**
 * Created by Norbinarius on 31.01.2018.
 */

public class SaleTicket {

    private String transactionID;
    private String transactionType;
    private String transactionTime;
    private String transactionComment;

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionTypea) {
        this.transactionType = transactionTypea;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getTransactionComment() {
        return transactionComment;
    }

    public void setTransactionComment(String transactionComment) {
        this.transactionComment = transactionComment;
    }

    public String[] getKeyAttributes() {
        return keyAttributes;
    }

    public void setKeyAttributes(String[] keyAttributes) {
        this.keyAttributes = keyAttributes;
    }

    public String getKeyData() {
        return keyData;
    }

    public void setKeyData(String keyData) {
        this.keyData = keyData;
    }

    private String[] keyAttributes;
    private String keyData;

}