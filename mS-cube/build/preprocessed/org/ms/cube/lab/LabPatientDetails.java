/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ms.cube.lab;

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
public class LabPatientDetails implements CommandListener {

    /**
     * Date Time Hospital Gender Age Group Additional Note
     */
    private Display display;
    private TextField date, time, note,patientName;
    private Command back, next, rest;
    private MsCube medAdminList;
    private ChoiceGroup age, gender, hosdpital;
    public Form form;

    public LabPatientDetails(MsCube medAdminList) {
        this.medAdminList = medAdminList;
        form = new Form("Laboratory Test  Patient Details");
        Date datex = new Date();
        String datestr = datex.toString().substring(0, 10);
        String timestr = datex.toString().substring(10);
        date = new TextField("Date", datestr, 100, TextField.ANY);
        time = new TextField("Time", timestr, 100, TextField.ANY);
        patientName = new TextField("Patient Name", "", 200, TextField.ANY);
        note = new TextField("Additional Note", "", 200, TextField.ANY);
        hosdpital = new ChoiceGroup("Hospital", ChoiceGroup.POPUP);
        String[] gedArr = {"Male", "Female","Unknown"};
        Recorder rec = new Recorder();
        gender = new ChoiceGroup("Gender", ChoiceGroup.POPUP, gedArr, null);
        age = new ChoiceGroup("Age", ChoiceGroup.POPUP, rec.getAllArrayRMS("Age"), null);
        form.append(date);
        form.append(time);
        form.append(patientName);
         form.append(hosdpital); 
         form.append(age); 
         form.append(gender);
         
         
        form.append(note);




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

//    public static String[] Split(String splitStr, String delimiter) {
//        StringBuffer token = new StringBuffer();
//        Vector tokens = new Vector();
//        // split
//        char[] chars = splitStr.toCharArray();
//        for (int i = 0; i < chars.length; i++) {
//            if (delimiter.indexOf(chars[i]) != -1) {
//                // we bumbed into a delimiter
//                if (token.length() > 0) {
//                    tokens.addElement(token.toString());
//                    token.setLength(0);
//                }
//            } else {
//                token.append(chars[i]);
//            }
//        }
//        // don't forget the "tail"...
//        if (token.length() > 0) {
//            tokens.addElement(token.toString());
//        }
//        // convert the vector into an array
//        String[] splitArray = new String[tokens.size()];
//        for (int i = 0; i < splitArray.length; i++) {
//            splitArray[i] = (String) tokens.elementAt(i);
//        }
//        return splitArray;
//    }

    public void backCom() {
        medAdminList.display.setCurrent(medAdminList.list);
    }

    public void reset() {
        date.setString("");
        time.setString("");
        note.setString("");
    }

   public void goNext() {
//        System.out.println("Go to next fro mlab where??? ");
//        OPDData oPDdata = new OPDData();
//        oPDdata.setPatientName(patientName.getString());
//        OPDPatientDiagnosis opdpd = new OPDPatientDiagnosis(medAdminList,oPDdata);
//        medAdminList.display.setCurrent(opdpd.form);
    }


}
