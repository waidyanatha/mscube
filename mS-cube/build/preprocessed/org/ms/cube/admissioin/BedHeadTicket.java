/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ms.cube.admissioin;

import org.ms.cube.OPD.*;
import org.ms.cube.*;
import javax.microedition.lcdui.*;
import java.util.*;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordEnumeration;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 *
 * @author Madhuka
 */
public class BedHeadTicket implements CommandListener {

    /**
     * Date Time Hospital Gender Age Group Additional Note
     */
    private Display display;
    private TextField date, time, BHT, notifierName;
    private Command back, next, rest;
    private MsCube medAdminList;
    private ChoiceGroup hospitals, wards;
    public Form form;

    public BedHeadTicket(MsCube medAdminList) {
        this.medAdminList = medAdminList;
        form = new Form("Admissioins Bed Head Ticket");
        Date datex = new Date();
        String datestr = datex.toString().substring(0, 10);
        String timestr = datex.toString().substring(10);
        date = new TextField("Date", datestr, 100, TextField.ANY);
        time = new TextField("Time", timestr, 100, TextField.ANY);
        notifierName = new TextField("Notifier Name*", "", 200, TextField.ANY);
        BHT = new TextField("BHT No*", "", 200, TextField.ANY);
        hospitals = new ChoiceGroup("Hospitals*", ChoiceGroup.POPUP);
        Recorder rec = new Recorder();
        wards = new ChoiceGroup("Ward*", ChoiceGroup.POPUP, rec.getAllArrayRMS("Ward"), null);
//        String[] gedArr = {"Male", "Female"};
//        String[] ageArr = {"1-10", "10-20", "20-45", "45-80", "80-100"};
//        gender = new ChoiceGroup("Gender", ChoiceGroup.POPUP, gedArr, null);
//        age = new ChoiceGroup("Age", ChoiceGroup.POPUP, ageArr, null);
        form.append(date);
        form.append(time);
        form.append(hospitals);
        form.append(wards);
        form.append(BHT);
        form.append(notifierName);





        back = new Command("Back", Command.BACK, 2);
        next = new Command("Next", Command.OK, 2);
        rest = new Command("Reset", Command.OK, 2);
        form.addCommand(back);

        form.addCommand(next);
        form.addCommand(rest);
        form.setCommandListener(this);
    }

    public void commandAction(Command c, Displayable d) {
        String label = c.getLabel();
        if (label.equals("Back")) {
            backCom();
        } else if (c == next) {
            goNext();
        } else if (c == rest) {
            reset();
        }
    }

    public void backCom() {
        medAdminList.display.setCurrent(medAdminList.list);
    }

    public void reset() {
        date.setString("");
        time.setString("");
        BHT.setString("");
        notifierName.setString("");
    }

    public void goNext() {
        System.out.println("Go to next to OPDPatientDiagnosis ");
        AdmisionData ad = new AdmisionData();
        ad.setBht(BHT.getString());
        ad.setNotifyName(notifierName.getString());
        PatientCaseDetails pcd = new PatientCaseDetails(medAdminList, ad);
        medAdminList.display.setCurrent(pcd.form);
    }
}
