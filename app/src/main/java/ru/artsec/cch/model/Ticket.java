package ru.artsec.cch.model;

/**
 * Created by Norbinarius on 31.01.2018.
 */

public class Ticket {

    public String getIdAction() {
        return idAction;
    }

    public void setIdAction(String idAction) {
        this.idAction = idAction;
    }

    public String getNameAction() {
        return nameAction;
    }

    public void setNameAction(String nameAction) {
        this.nameAction = nameAction;
    }

    public String getKeyData() {
        return keyData;
    }

    public void setKeyData(String keyData) {
        this.keyData = keyData;
    }

    public String[] getKeyAttributes() {
        return keyAttributes;
    }

    public void setKeyAttributes(String[] keyAttributes) {
        this.keyAttributes = keyAttributes;
    }

    public String getKeyEnabled() {
        return keyEnabled;
    }

    public void setKeyEnabled(String keyEnabled) {
        this.keyEnabled = keyEnabled;
    }

    private String idAction;
    private String nameAction;

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    private String keyValue;
    private String keyData;
    private String[] keyAttributes;
    private String keyEnabled;

}
