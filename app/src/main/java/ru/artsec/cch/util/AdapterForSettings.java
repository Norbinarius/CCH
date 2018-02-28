package ru.artsec.cch.util;

/**
 * Created by Norbinarius on 28.02.2018.
 */

public class AdapterForSettings {
    public String namePreload;
    public String ipGatePreload;
    public String ipSKUDPreload;

    public AdapterForSettings(String namePreload, String ipGatePreload, String ipSKUDPreload, String namespaceGatePreload, String namespaceSKUDPreload) {
        this.namePreload = namePreload;
        this.ipGatePreload = ipGatePreload;
        this.ipSKUDPreload = ipSKUDPreload;
        this.namespaceGatePreload = namespaceGatePreload;
        this.namespaceSKUDPreload = namespaceSKUDPreload;
    }

    public String namespaceGatePreload;
    public String namespaceSKUDPreload;



    @Override
    public String toString() {
        return namePreload;
    }
}