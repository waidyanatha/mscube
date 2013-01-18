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
public class PatientCaseDetails implements CommandListener {

    /**
     * Date Time Hospital Gender Age Group Additional Note
     */
    private Display display;
    private TextField patientName, gaurianName, addressLine1, addressLine2, city, telephone;
    private Command back, next, rest;
    private MsCube medAdminList;
    private ChoiceGroup age, gender;
    public Form form;
    public AdmisionData admisionData;

    public PatientCaseDetails(MsCube medAdminList, AdmisionData admisionData) {
        this.medAdminList = medAdminList;
        this.admisionData = admisionData;
        form = new Form("Admissioins Patient case details");

        patientName = new TextField("Patient Name*", "", 100, TextField.ANY);
        gaurianName = new TextField("Gaurian's Name", "", 100, TextField.ANY);
        addressLine1 = new TextField("Address Line1*", "", 100, TextField.ANY);
        addressLine2 = new TextField("Address Line2", "", 100, TextField.ANY);
        city = new TextField("City/Town/Village*", "", 100, TextField.ANY);
        telephone = new TextField("Telephone", "", 100, TextField.PHONENUMBER);

        String[] gedArr = {"Male", "Female","Unknown"};
        Recorder rec = new Recorder();
        gender = new ChoiceGroup("Gender", ChoiceGroup.POPUP, gedArr, null);
        age = new ChoiceGroup("Age", ChoiceGroup.POPUP, rec.getAllArrayRMS("Age"), null);
        form.append(patientName);
        form.append(gaurianName);
        form.append(age);
        form.append(gender);
        form.append(addressLine1);
        form.append(addressLine2);
        form.append(city);
        form.append(telephone);
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

    public static String[] Split(String splitStr, String delimiter) {
        StringBuffer token = new StringBuffer();
        Vector tokens = new Vector();
        // split
        char[] chars = splitStr.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (delimiter.indexOf(chars[i]) != -1) {
                // we bumbed into a delimiter
                if (token.length() > 0) {
                    tokens.addElement(token.toString());
                    token.setLength(0);
                }
            } else {
                token.append(chars[i]);
            }
        }
        // don't forget the "tail"...
        if (token.length() > 0) {
            tokens.addElement(token.toString());
        }
        // convert the vector into an array
        String[] splitArray = new String[tokens.size()];
        for (int i = 0; i < splitArray.length; i++) {
            splitArray[i] = (String) tokens.elementAt(i);
        }
        return splitArray;
    }

    public void backCom() {
        medAdminList.display.setCurrent(medAdminList.list);
    }

    public void reset() {
        patientName.setString("");
        gaurianName.setString("");
        addressLine1.setString("");
        addressLine2.setString("");
        city.setString("");
        telephone.setString("");
    }

    public void goNext() {
        admisionData.setPatientName(patientName.getString());
        AdmissioinsPatientDiagnosis apd = new AdmissioinsPatientDiagnosis(medAdminList, admisionData);
        medAdminList.display.setCurrent(apd.form);
    }
}
