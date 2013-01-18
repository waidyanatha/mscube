/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ms.cube;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.Ticker;
import javax.microedition.midlet.*;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.ms.cube.OPD.OPDPatientDetails;
import org.ms.cube.admissioin.BedHeadTicket;
import org.ms.cube.lab.LabPatientDetails;
import org.ms.cube.noncommunicable.NonCommunicablePatientDetails;

/**
 * @author Madhuka
 */
public class MsCube extends MIDlet implements CommandListener {

    public Display display;
    public List list;
    private Command cmExit;
    private Ticker ticker;
    public static boolean isLogin = false;
    public static String Username = "";
    public static String Hospital = "";
    public RecordStore sign_rs, sym_rs, dia_rs, rec_rs, loc_rs, pan_rs, pro_rs, ward_rs, status_rs, loctype_rs, age_rs, gender_rs;

    // Create the Commands
    public MsCube() {
        list = new List("mS-cube Main Menu", Choice.IMPLICIT);

    }

    public void startApp() {
        display = Display.getDisplay(this);

        System.out.println("Start");
        cmExit = new Command("Exit", Command.EXIT, 1);
        ticker = new Ticker("Powered by Spot On Solutions");






        list.append("Application tools", null);
        list.append("OPD(Outpatient Dept.)", null);
        list.append("Admissioins", null);
        list.append("Special Surveillance", null);
        list.append("Laboratory (Investigation)", null);
        list.append("NCD Clinics (Non-communicable)", null);
        list.append("Immunization", null);
        list.append("LogOut", null);
        try {
            startRMS();
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        list.addCommand(cmExit);
        list.setCommandListener(this);
        list.setTicker(ticker);



        if (isLogin == true) {
            display.setCurrent(list);
        } else {
            Login login = new Login(this);
            display.setCurrent(login.form);
        }
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public void commandAction(Command c, Displayable s) {
        // If an implicit list generated the event
        if (c == List.SELECT_COMMAND) {
            switch (list.getSelectedIndex()) {
                case 0:
                    AppToolList createAccount = new AppToolList(this);
                    display.setCurrent(createAccount.listAppTools);
                    break;

                case 1:
                    testBaseData();
                    OPDPatientDetails oPDPatientDetails = new OPDPatientDetails(this);
                    display.setCurrent(oPDPatientDetails.form);
                    break;

                case 2:
                    BedHeadTicket bedHeadTicket = new BedHeadTicket(this);
                    display.setCurrent(bedHeadTicket.form);
                    break;


                case 4:
                    LabPatientDetails labPatientDetails = new LabPatientDetails(this);
                    display.setCurrent(labPatientDetails.form);
                    break;

                case 5:
                    NonCommunicablePatientDetails nonCommunicablePatientDetails = new NonCommunicablePatientDetails(this);
                    display.setCurrent(nonCommunicablePatientDetails.form);
                    break;
                case 7:
                    isLogin = false;
                    Username = "";
                    Hospital = "";
                    Login login = new Login(this);
                    display.setCurrent(login.form);
                    break;

                default:
                    System.out.println("H544");
            }
        } else if (c == cmExit) {
            destroyApp(false);
            notifyDestroyed();
        }
    }

    public void alertBox(String msg, String info, AlertType type) {
        Alert alert = new Alert(info, msg, null, type);
        alert.setTimeout(3000);
        display.setCurrent(alert);
    }

    public void startRMS() throws JSONException {
        try {
            //opening all rms
            loc_rs = RecordStore.openRecordStore("Loc", true);
            pro_rs = RecordStore.openRecordStore("Prof", true);
            loctype_rs = RecordStore.openRecordStore("LocType", true);
            ward_rs = RecordStore.openRecordStore("Ward", true);
            dia_rs = RecordStore.openRecordStore("Diag", true);
            status_rs = RecordStore.openRecordStore("CState", true);
            age_rs = RecordStore.openRecordStore("Age", true);
            gender_rs = RecordStore.openRecordStore("gender", true);
            sym_rs = RecordStore.openRecordStore("Symp", true);
            sign_rs = RecordStore.openRecordStore("Sign", true);

            //downloading location types
            if (loctype_rs.getNumRecords() == 0) {
                Connector connector = new Connector();
                JSONArray a = new JSONArray(connector.getContain("view/locationType.jag"));

                for (int i = 0; i < a.length(); i++) {

                    String str = a.getString(i);
                    System.out.println(i + ".location" + str);
                    //      loctype_rs.addRecord(data, 0, i);
                    byte[] rec = str.getBytes();

                    loctype_rs.addRecord(rec, 0, rec.length);

                }
                System.out.println("Length: " + loctype_rs.getNumRecords());

            }
            //closeing rms
            pro_rs.closeRecordStore();
            loc_rs.closeRecordStore();
            loctype_rs.closeRecordStore();
            ward_rs.closeRecordStore();
            status_rs.closeRecordStore();
            dia_rs.closeRecordStore();
            sym_rs.closeRecordStore();
            sign_rs.closeRecordStore();
            age_rs.closeRecordStore();
            gender_rs.closeRecordStore();

        } catch (RecordStoreException ex) {
            alertBox("Creating RMS was problem", "Error in RMS", AlertType.ERROR);
            ex.printStackTrace();
        }
    }
    
    public void testBaseData(){
    Recorder recorder = new Recorder();
    recorder.openRecStore();
//    String uname = recorder.readRecords(1);
//        System.out.println("u Name  --- "+ uname);
        recorder.closeRecStore();
    }
}
