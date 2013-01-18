/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ms.cube;

import javax.microedition.lcdui.*;
import java.util.*;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;
import org.json.me.JSONArray;
import org.json.me.JSONException;

/**
 *
 * @author Madhuka
 */
public class LocationType implements CommandListener {

    private Display display;
    private TextField textField;
    private Command back, ok;
    private MsCube medAdminList;
    private ChoiceGroup CoursePOP;
    public Form form;

    public LocationType(MsCube medAdminList) {
        try {
            this.medAdminList = medAdminList;
            form = new Form("Location Type");

            back = new Command("Back", Command.BACK, 2);
            ok = new Command("List Hospitals", Command.OK, 2);

//locations from server call
            Connector connector = new Connector();
            RecordStore locationRec= RecordStore.openRecordStore("Loc", true);
            System.out.println("location type RMS" + locationRec.getNumRecords());
            String[] ary;
            JSONArray a = new JSONArray(connector.getContain("view/locationType.jag"));
            if (locationRec.getNumRecords() != a.length()) {
                ary = new String[a.length()];

                for (int i = 0; i < a.length(); i++) {
                    System.out.println("sssss" + i);
                    ary[i] = a.getString(i);

                }
            } else {
                ary = new String[a.length()];
                String outstr = "";
                byte[] recData = new byte[5];
                for (int i = 0; i < a.length(); i++) {
                    System.out.println("sssss" + i);
                    System.out.println("ssasa"+locationRec.getRecordSize(i+1));
                    System.out.println("ssasa"+locationRec.getRecord(i+1));
                    recData = new byte[locationRec.getRecordSize(i+1)];
                    int len = locationRec.getRecord(i, recData, 0);
                    outstr = new String(recData, 0, len);
                    ary[i] = outstr;

                }
            }

            CoursePOP = new ChoiceGroup("Location Type", Choice.POPUP, ary, null);
            form.append(CoursePOP);
            textField = new TextField("Name", "Nakkawatta", 100, TextField.ANY);
            form.append(textField);
            form.addCommand(back);
            form.addCommand(ok);
            form.setCommandListener(this);
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public void commandAction(Command c, Displayable d) {
        String label = c.getLabel();
        if (label.equals("Back")) {
            backCom();
        } else if (label.equals("List Hospitals")) {
            listHospitals();
        }

    }

    public void backCom() {
        medAdminList.display.setCurrent(medAdminList.list);
    }

    public void listHospitals() {
        Hospitals hospitals = new Hospitals(medAdminList, CoursePOP.getString(CoursePOP.getSelectedIndex()), textField.getString());
        medAdminList.display.setCurrent(hospitals.list);
    }
}
