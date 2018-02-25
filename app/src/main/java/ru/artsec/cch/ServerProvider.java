package ru.artsec.cch;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import ru.artsec.cch.model.Event;
import ru.artsec.cch.model.FailPassLog;
import ru.artsec.cch.model.PassLogTicket;
import ru.artsec.cch.model.SaleTicket;
import ru.artsec.cch.model.Ticket;
import ru.artsec.cch.util.Formatter;
import ru.artsec.cch.model.PairTicketProps;
import ru.artsec.cch.util.ServerProviderHelper;
import ru.artsec.cch.util.SharedPrefsUtil;

/**
 * Created by Norbinarius on 31.01.2018.
 */

public class ServerProvider {

    public static String getVersion(Activity act){
        String result = null;
        SoapObject request = new SoapObject(SharedPrefsUtil.LoadString(act, "NamespaceSKUD", Config.NAMESPACE_SKUD), Config.GET_VERSION);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(SharedPrefsUtil.LoadString(act, "SourceSKUD", Config.URL_SKUD));
        if (ServerProviderHelper.isDeviceConnectedToWeb(act)) {
            try {
                androidHttpTransport.call(SharedPrefsUtil.LoadString(act, "NamespaceSKUD", Config.NAMESPACE_SKUD)+ "/" + Config.GET_VERSION , envelope);
                SoapPrimitive resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
                result = resultsRequestSOAP.toString();
                ServerProviderHelper.setErrorMsg(null);
            } catch (Exception e) {
                ServerProviderHelper.setErrorMsg(e.toString());
                Log.wtf("MYTAG", ServerProviderHelper.getErrorMsg());
            }
        } else {
            ServerProviderHelper.setErrorMsg("Ошибка сети, проверьте подключение к сети");
        }
        return result;
    }

    public static ArrayList<Event> getActiveEvents(Activity act){
        SoapObject request = new SoapObject(SharedPrefsUtil.LoadString(act, "NamespaceSKUD", Config.NAMESPACE_SKUD), Config.GET_EVENTS);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(SharedPrefsUtil.LoadString(act, "SourceSKUD", Config.URL_SKUD));
        ArrayList mainList = new ArrayList();
        if (ServerProviderHelper.isDeviceConnectedToWeb(act)) {
            try {
                androidHttpTransport.call(SharedPrefsUtil.LoadString(act, "NamespaceSKUD", Config.NAMESPACE_SKUD) + "/" + Config.GET_EVENTS, envelope);
                SoapObject resultRequestSOAP = (SoapObject) envelope.bodyIn;
                SoapObject root = (SoapObject) resultRequestSOAP.getProperty(0);
                SoapObject s_deals = (SoapObject) root.getProperty("List");

                for (int i = 0; i < s_deals.getPropertyCount(); i++) {
                    Object property = s_deals.getProperty(i);
                    if (property instanceof SoapObject) {
                        SoapObject list = (SoapObject) property;
                        if (Formatter.isEventActive(Formatter.timeToStr(list.getProperty("TimeEnd").toString()))) {
                            Event event = new Event();
                            event.setId(list.getProperty("ID").toString());
                            event.setName(list.getProperty("Name").toString());
                            event.setComment(list.getProperty("Comment").toString());
                            event.setTimeEnd(list.getProperty("TimeEnd").toString());
                            event.setTimeStart(list.getProperty("TimeStart").toString());
                            event.setAttributes(Formatter.findNumeric(list.getProperty("Attributes").toString()));
                            mainList.add(event);
                        }
                    }
                }
                ServerProviderHelper.setErrorMsg(null);
            } catch (Exception e) {
                ServerProviderHelper.setErrorMsg(e.toString());
                Log.wtf("MYTAG", ServerProviderHelper.getErrorMsg());
            }
        } else {
            ServerProviderHelper.setErrorMsg("Ошибка сети, проверьте подключение к сети");
        }
        return mainList;
    }

    public static ArrayList<Event> getPassedEvents(Activity act){
        SoapObject request = new SoapObject(SharedPrefsUtil.LoadString(act, "NamespaceSKUD", Config.NAMESPACE_SKUD), Config.GET_EVENTS);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(SharedPrefsUtil.LoadString(act, "SourceSKUD", Config.URL_SKUD));
        ArrayList mainList = new ArrayList();
        if (ServerProviderHelper.isDeviceConnectedToWeb(act)) {
            try {
                androidHttpTransport.call(SharedPrefsUtil.LoadString(act, "NamespaceSKUD", Config.NAMESPACE_SKUD) + "/"  + Config.GET_EVENTS, envelope);
                SoapObject resultRequestSOAP = (SoapObject) envelope.bodyIn;
                SoapObject root = (SoapObject) resultRequestSOAP.getProperty(0);
                SoapObject s_deals = (SoapObject) root.getProperty("List");

                for (int i = 0; i < s_deals.getPropertyCount(); i++) {
                    Object property = s_deals.getProperty(i);
                    if (property instanceof SoapObject) {
                        SoapObject list = (SoapObject) property;
                        if(!Formatter.isEventActive(Formatter.timeToStr(list.getProperty("TimeEnd").toString()))) {
                            Event event = new Event();
                            event.setId(list.getProperty("ID").toString());
                            event.setName(list.getProperty("Name").toString());
                            event.setComment(list.getProperty("Comment").toString());
                            event.setTimeEnd(list.getProperty("TimeEnd").toString());
                            event.setTimeStart(list.getProperty("TimeStart").toString());
                            event.setAttributes(Formatter.findNumeric(list.getProperty("Attributes").toString()));
                            mainList.add(event);
                        }
                    }
                }
                ServerProviderHelper.setErrorMsg(null);
            } catch (Exception e) {
                ServerProviderHelper.setErrorMsg(e.toString());
                Log.wtf("MYTAG", ServerProviderHelper.getErrorMsg());
            }
        } else {
            ServerProviderHelper.setErrorMsg("Ошибка сети, проверьте подключение к сети");
        }
        return mainList;
    }

    public static ArrayList<Event> getEventProps(Activity act, Integer actionID){
        SoapObject request = new SoapObject(SharedPrefsUtil.LoadString(act, "NamespaceSKUD", Config.NAMESPACE_SKUD), Config.GET_EVENT_BY_ID);
        ServerProviderHelper.addProperty(request, "ActionID", actionID, Integer.class);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        ServerProviderHelper.envelopeSetUpParams(envelope, request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(SharedPrefsUtil.LoadString(act, "SourceSKUD", Config.URL_SKUD));
        ArrayList mainList = new ArrayList();
        if (ServerProviderHelper.isDeviceConnectedToWeb(act)) {
            try {
                androidHttpTransport.call(SharedPrefsUtil.LoadString(act, "NamespaceSKUD", Config.NAMESPACE_SKUD) + "/"  + Config.GET_EVENT_BY_ID, envelope);
                SoapObject resultRequestSOAP = (SoapObject) envelope.bodyIn;
                SoapObject root = (SoapObject) resultRequestSOAP.getProperty(0);
                SoapObject s_deals = (SoapObject) root.getProperty("List");

                for (int i = 0; i < s_deals.getPropertyCount(); i++) {
                    Object property = s_deals.getProperty(i);
                    if (property instanceof SoapObject) {
                        SoapObject list = (SoapObject) property;
                            Event event = new Event();
                            event.setId(list.getProperty("ID").toString());
                            event.setName(list.getProperty("Name").toString());
                            event.setComment(list.getProperty("Comment").toString());
                            event.setTimeEnd(list.getProperty("TimeEnd").toString());
                            event.setTimeStart(list.getProperty("TimeStart").toString());
                            event.setAttributes(Formatter.findNumeric(list.getProperty("Attributes").toString()));
                            mainList.add(event);
                    }
                }
                ServerProviderHelper.setErrorMsg(null);
            } catch (Exception e) {
                ServerProviderHelper.setErrorMsg(e.toString());
                Log.wtf("MYTAG", ServerProviderHelper.getErrorMsg());
            }
        } else {
            ServerProviderHelper.setErrorMsg("Ошибка сети, проверьте подключение к сети");
        }
        return mainList;
    }

    public static ArrayList<PairTicketProps> getTicketProps(Activity act, String id){
        SoapObject request = new SoapObject(SharedPrefsUtil.LoadString(act, "NamespaceGate", Config.NAMESPACE_GATES), Config.GET_TICKET);

        ServerProviderHelper.addProperty(request, "DoorID", SharedPrefsUtil.LoadInt(act,"DoorID", 101), Integer.class);
        ServerProviderHelper.addProperty(request, "KeyValue", id, String.class);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        ServerProviderHelper.envelopeSetUpParams(envelope, request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(SharedPrefsUtil.LoadString(act, "SourceGate", Config.URL_GATES));
        androidHttpTransport.debug = true;
        ArrayList ticketValues = new ArrayList();
        ArrayList ticketSales = new ArrayList();
        if (ServerProviderHelper.isDeviceConnectedToWeb(act)) {
            try {
                androidHttpTransport.call(SharedPrefsUtil.LoadString(act, "NamespaceGate", Config.NAMESPACE_GATES) + "/"  + Config.GET_TICKET, envelope);
                //TICKET VALUES
                SoapObject resultRequestSOAP = (SoapObject) envelope.bodyIn;
                Ticket tk = new Ticket();
                    SoapObject root = (SoapObject) resultRequestSOAP.getProperty(0);
                    tk.setKeyValue(root.getProperty("KeyValue").toString());
                        SoapObject data = (SoapObject) root.getProperty("Data");
                            SoapObject keyProps = (SoapObject) data.getProperty("KeyByActionProps");
                                SoapObject action = (SoapObject) keyProps.getProperty("Action");
                                tk.setIdAction(action.getProperty("ID").toString());
                                tk.setNameAction(action.getProperty("Name").toString());

                                SoapObject key = (SoapObject) keyProps.getProperty("Key");
                                tk.setKeyData(key.getProperty("KeyData").toString());
                                tk.setKeyEnabled(key.getProperty("Enabled").toString());
                                tk.setKeyAttributes(Formatter.findNumeric(key.getProperty("KeyAttributes").toString()));
                ticketValues.add(tk);

                SoapObject sales = (SoapObject) keyProps.getProperty("Sales");

                for (int i = 0; i < sales.getPropertyCount(); i++) {
                    Object property = sales.getProperty(i);
                    if (property instanceof SoapObject) {
                        SoapObject list = (SoapObject) property;
                        SaleTicket sale = new SaleTicket();
                        sale.setTransactionID(list.getProperty("TransactionID").toString());
                        sale.setTransactionType(list.getProperty("TransactionType").toString());
                        sale.setTransactionTime(list.getProperty("TransactionTime").toString());
                        sale.setTransactionComment(list.getProperty("TransactionComment").toString());
                        sale.setKeyData(list.getProperty("KeyData").toString());
                        sale.setKeyAttributes(Formatter.findNumeric(list.getProperty("KeyAttributes").toString()));
                        ticketSales.add(sale);
                    }
                }
                ServerProviderHelper.setErrorMsg(null);
            } catch (Exception e) {
                ServerProviderHelper.setErrorMsg(e.toString());
                if (ServerProviderHelper.getErrorMsg().contains("cannot be cast to")) {
                    ServerProviderHelper.setErrorMsg(androidHttpTransport.responseDump);
                }
                Log.wtf("MYTAG", ServerProviderHelper.getErrorMsg());
            }
        } else {
            ServerProviderHelper.setErrorMsg("Ошибка сети, проверьте подключение к сети");
        }
        ArrayList<PairTicketProps> group = new ArrayList<PairTicketProps>();
        PairTicketProps pair = new PairTicketProps();
        pair.setTicketValues(ticketValues);
        pair.setTicketSales(ticketSales);
        group.add(pair);
        return group;
    }

    public static ArrayList<PassLogTicket> getTicketPassLog(Activity act, Integer ActionID, String KeyID){
        SoapObject request = new SoapObject(SharedPrefsUtil.LoadString(act, "NamespaceSKUD", Config.NAMESPACE_SKUD), Config.GET_TICKET_PASS_LOG);

        ServerProviderHelper.addProperty(request, "ActionID", ActionID, Integer.class);
        ServerProviderHelper.addProperty(request, "Key", KeyID, String.class);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        ServerProviderHelper.envelopeSetUpParams(envelope, request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(SharedPrefsUtil.LoadString(act, "SourceSKUD", Config.URL_SKUD));
        ArrayList passLog = new ArrayList();
        if (ServerProviderHelper.isDeviceConnectedToWeb(act)) {
            try {
                androidHttpTransport.call(SharedPrefsUtil.LoadString(act, "NamespaceSKUD", Config.NAMESPACE_SKUD) + "/" + Config.GET_TICKET_PASS_LOG, envelope);
                SoapObject resultRequestSOAP = (SoapObject) envelope.bodyIn;
                Log.wtf("MYTAG", resultRequestSOAP.toString());

                SoapObject root = (SoapObject) resultRequestSOAP.getProperty(0);
                SoapObject s_deals = (SoapObject) root.getProperty("List");

                for (int i = 0; i < s_deals.getPropertyCount(); i++) {
                    Object property = s_deals.getProperty(i);
                    if (property instanceof SoapObject) {
                        SoapObject list = (SoapObject) property;
                        PassLogTicket ticketPass = new PassLogTicket();
                        ticketPass.setDoorID(list.getProperty("DoorID").toString());
                        ticketPass.setDoorName(list.getProperty("DoorName").toString());
                        ticketPass.setPlaceID(list.getProperty("PlaceID").toString());
                        ticketPass.setPlaceName(list.getProperty("PlaceName").toString());
                        ticketPass.setPassTime(list.getProperty("PassTime").toString());
                        ticketPass.setIsEnter(list.getProperty("IsEnter").toString());
                        passLog.add(ticketPass);
                    }
                }
                ServerProviderHelper.setErrorMsg(null);
            } catch (Exception e) {
                ServerProviderHelper.setErrorMsg(e.toString());
                Log.wtf("MYTAG", ServerProviderHelper.getErrorMsg());
            }
        } else {
            ServerProviderHelper.setErrorMsg("Ошибка сети, проверьте подключение к сети");
        }
        return passLog;
    }

    public static ArrayList<FailPassLog> getFailPassLog(Activity act, Integer ActionID, Integer StartID){
        SoapObject request = new SoapObject(SharedPrefsUtil.LoadString(act, "NamespaceGate", Config.NAMESPACE_GATES), Config.GET_FAIL_PASS_LOG);

        ServerProviderHelper.addProperty(request, "ActionID", ActionID, Integer.class);
        ServerProviderHelper.addProperty(request, "StartID", StartID, Integer.class);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        ServerProviderHelper.envelopeSetUpParams(envelope, request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(SharedPrefsUtil.LoadString(act, "SourceGate", Config.URL_GATES));
        ArrayList failLog = new ArrayList();
        if (ServerProviderHelper.isDeviceConnectedToWeb(act)) {
            try {
                androidHttpTransport.call(SharedPrefsUtil.LoadString(act, "NamespaceGate", Config.NAMESPACE_GATES) + "/" + Config.GET_FAIL_PASS_LOG, envelope);
                SoapObject resultRequestSOAP = (SoapObject) envelope.bodyIn;
                Log.wtf("MYTAG", resultRequestSOAP.toString());

                SoapObject s_deals = (SoapObject) resultRequestSOAP.getProperty(0);

                for (int i = 0; i < s_deals.getPropertyCount(); i++) {
                    Object property = s_deals.getProperty(i);
                    if (property instanceof SoapObject) {
                        SoapObject list = (SoapObject) property;
                        FailPassLog fpl = new FailPassLog();
                        fpl.setTime(list.getProperty("CurTime").toString());
                        fpl.setKey(list.getProperty("KeyValue").toString());
                        fpl.setID(list.getProperty("ID").toString());
                        fpl.setReason(list.getProperty("Reason").toString());
                            SoapObject door = (SoapObject) list.getProperty("Door");
                            fpl.setDoorID(door.getProperty("ID").toString());
                            fpl.setDoorName(door.getProperty("Name").toString());
                            SoapObject place = (SoapObject) list.getProperty("Place");
                            fpl.setPlaceID(place.getProperty("ID").toString());
                            fpl.setPlaceName(place.getProperty("Name").toString());
                        failLog.add(fpl);
                    }
                }
                ServerProviderHelper.setErrorMsg(null);
            } catch (Exception e) {
                ServerProviderHelper.setErrorMsg(e.toString());
                Log.wtf("MYTAG", ServerProviderHelper.getErrorMsg());
            }
        } else {
            ServerProviderHelper.setErrorMsg("Ошибка сети, проверьте подключение к сети");
        }
        return failLog;
    }

}
