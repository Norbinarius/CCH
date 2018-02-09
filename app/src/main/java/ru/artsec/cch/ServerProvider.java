package ru.artsec.cch;

import android.app.Activity;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

import ru.artsec.cch.model.Event;
import ru.artsec.cch.model.PassLogTicket;
import ru.artsec.cch.model.SaleTicket;
import ru.artsec.cch.model.Ticket;
import ru.artsec.cch.util.Formatter;
import ru.artsec.cch.model.PairTicketProps;
import ru.artsec.cch.util.SharedPrefsUtil;

/**
 * Created by Norbinarius on 31.01.2018.
 */

public class ServerProvider {

    public static String getVersion(){
        String result = null;
        SoapObject request = new SoapObject(Config.NAMESPACE, Config.GET_VERSION);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(Config.URL);
        try {
            androidHttpTransport.call(Config.SOAP_ACTION, envelope);
            SoapPrimitive resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
            result = resultsRequestSOAP.toString();
        } catch (Exception e) {
            result = e.toString();
        }
        return result;
    }

    public static ArrayList getActiveEvents(){
        SoapObject request = new SoapObject(Config.NAMESPACE, Config.GET_EVENTS);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(Config.URL);
        ArrayList mainList = new ArrayList();
        try {
            androidHttpTransport.call(Config.SOAP_ACTION + Config.GET_EVENTS, envelope);
            SoapObject resultRequestSOAP = (SoapObject) envelope.bodyIn;
            SoapObject root = (SoapObject) resultRequestSOAP.getProperty(0);
            SoapObject s_deals = (SoapObject) root.getProperty("List");

            for (int i = 0; i < s_deals.getPropertyCount(); i++) {
                Object property = s_deals.getProperty(i);
                if (property instanceof SoapObject) {
                    SoapObject list = (SoapObject) property;
                    if(Formatter.isEventActive(Formatter.timeToStr(list.getProperty("TimeEnd").toString()))) {
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
        } catch (Exception e) {
            Log.wtf("MYTAG",e.toString());
        }
        return mainList;
    }

    public static ArrayList getPassedEvents(){
        SoapObject request = new SoapObject(Config.NAMESPACE, Config.GET_EVENTS);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(Config.URL);
        ArrayList mainList = new ArrayList();
        try {
            androidHttpTransport.call(Config.SOAP_ACTION + Config.GET_EVENTS, envelope);
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
        } catch (Exception e) {
            Log.wtf("MYTAG",e.toString());
        }
        return mainList;
    }

    public static ArrayList getEventProps(){
        SoapObject request = new SoapObject(Config.NAMESPACE, Config.GET_EVENT_BY_ID);
        addProperty(request, "ActionID", 1, Integer.class);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelopeSetUpParams(envelope, request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(Config.URL);
        ArrayList mainList = new ArrayList();
        try {
            androidHttpTransport.call(Config.SOAP_ACTION + Config.GET_EVENT_BY_ID, envelope);
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
        } catch (Exception e) {
            Log.wtf("MYTAG",e.toString());
        }
        return mainList;
    }

    public static ArrayList getTicketProps(Activity act, String id){
        SoapObject request = new SoapObject(Config.NAMESPACE_GATES, Config.GET_TICKET);

        addProperty(request, "DoorID", SharedPrefsUtil.LoadInt(act,"DoorID", 0), Integer.class);
        addProperty(request, "KeyValue", id, String.class);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelopeSetUpParams(envelope, request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(Config.URL_GATES);
        ArrayList ticketValues = new ArrayList();
        ArrayList ticketSales = new ArrayList();
        try {
            androidHttpTransport.call(Config.SOAP_ACTION_GATES + Config.GET_TICKET, envelope);
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
        } catch (Exception e) {
            Log.wtf("MYTAG",e.toString());
        }
        ArrayList<PairTicketProps> group = new ArrayList<PairTicketProps>();
        PairTicketProps pair = new PairTicketProps();
        pair.setTicketValues(ticketValues);
        pair.setTicketSales(ticketSales);
        group.add(pair);
        return group;
    }

    public static ArrayList getTicketPassLog(Activity act, Integer ActionID, String KeyID){
        SoapObject request = new SoapObject(Config.NAMESPACE, Config.GET_TICKET_PASS_LOG);

        addProperty(request, "ActionID", ActionID, Integer.class);
        addProperty(request, "Key", KeyID, String.class);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelopeSetUpParams(envelope, request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(Config.URL);
        ArrayList passLog = new ArrayList();
        try {
            androidHttpTransport.call(Config.SOAP_ACTION + Config.GET_TICKET_PASS_LOG, envelope);
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
        } catch (Exception e) {
            Log.wtf("MYTAG",e.toString());
        }
        return passLog;
    }

    private static void addProperty(SoapObject req, String name, Object value, Object type){
        PropertyInfo prop = new PropertyInfo();
        prop.setName(name);
        prop.setValue(value);
        prop.setType(type);

        req.addProperty(prop);
    }

    private static void envelopeSetUpParams(SoapSerializationEnvelope envelope, SoapObject req){
        envelope.setAddAdornments(false);
        envelope.implicitTypes = true;
        envelope.dotNet = true;
        envelope.encodingStyle = SoapSerializationEnvelope.XSD;
        envelope.setOutputSoapObject(req);
    }

}
