package ru.artsec.cch.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

/**
 * Created by Norbinarius on 13.02.2018.
 */

public class ServerProviderHelper {

    public static String getErrorMsg() {
        return errorMsg;
    }

    public static void setErrorMsg(String errorMsg) {
        ServerProviderHelper.errorMsg = errorMsg;
    }

    private static String errorMsg;

    public static boolean isDeviceConnectedToWeb(Activity act){
        ConnectivityManager connectivityManager = (ConnectivityManager)act.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        }
        return false;
    }

    public static void errorException(){
        if (errorMsg != null) {
            if (errorMsg.contains("failed to connect")) {
                setErrorMsg("Не удалось подключится к серверу, проверьте настройки адресов ресурсов");
            }
            if (errorMsg.contains("Сервер не распознал значение заголовка HTTP SOAPAction")) {
                setErrorMsg("Ошибка пространства имен, проверьте настройки пространств имен ресурсов");
            }
            if (errorMsg.contains("Неверно указана точка прохода")) {
                setErrorMsg("Ошибка точки прохода, проверьте настройки точки прохода");
            }
            if (errorMsg.contains("illegal property: KeyByActionProps")) {
                setErrorMsg("Билет с такими данными не найден");
            }
        }
    }

    public static void addProperty(SoapObject req, String name, Object value, Object type){
        PropertyInfo prop = new PropertyInfo();
        prop.setName(name);
        prop.setValue(value);
        prop.setType(type);

        req.addProperty(prop);
    }

    public static void envelopeSetUpParams(SoapSerializationEnvelope envelope, SoapObject req){
        envelope.setAddAdornments(false);
        envelope.implicitTypes = true;
        envelope.dotNet = true;
        envelope.encodingStyle = SoapSerializationEnvelope.XSD;
        envelope.setOutputSoapObject(req);
    }

}
