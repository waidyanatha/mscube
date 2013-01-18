/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ms.cube.noncommunicable;

import org.ms.cube.OPD.*;
import org.ms.cube.LaboratoryTestInvestigations;
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
public class NonCommunicablePatientDiagnosis implements CommandListener, ItemStateListener {

    /**
     * Date Time Hospital Gender Age Group Additional Note
     *
     * OPD Patient Diagnosis
     *
     * Onset Date disease* - Search/add Select Disease Symptoms* - Search/add
     * Select Symptom Signs* - Search/add Select Sign Status
     *
     * Lab test Submit
     */
    private Display display;
    private TextField onsetDate, disease, symptom, sign, status;
    private Command back, labTest, rest, submit;
    private MsCube medAdminList;
    private ChoiceGroup diseases, symptoms, signs;
    NonCommunicableData nonCommunicableData;
    public Form form;

    public NonCommunicablePatientDiagnosis(MsCube medAdminList, NonCommunicableData data) {
        this.nonCommunicableData = data;
        System.out.println("nonCommunicable PatientDiagnosis pname" + data.getPatientName());
        this.medAdminList = medAdminList;
        form = new Form("nonCommunicable Clinic");
        Date datex = new Date();
        String datestr = datex.toString().substring(0, 10);
        String timestr = datex.toString().substring(10);
        onsetDate = new TextField("OnSet Date", datestr, 100, TextField.ANY);
        disease = new TextField("disease* - Search/add", "", 100, TextField.ANY);
        symptom = new TextField("Symptoms* - Search/add", "", 200, TextField.ANY);
        sign = new TextField("Signs* - Search/add", "", 200, TextField.ANY);
        status = new TextField("Status", "", 200, TextField.ANY);
        String[] gedArr = {"Male", "Female","Unknown"};
        Recorder rec = new Recorder();
        diseases = new ChoiceGroup("Select Disease", ChoiceGroup.POPUP, rec.getArrayRMSFromKey("Diag","Noncomunicable"), null);
        symptoms = new ChoiceGroup("Select Symptom", ChoiceGroup.POPUP, rec.getArrayRMS("Symp"), null);
        signs = new ChoiceGroup("Select Sign", ChoiceGroup.POPUP, rec.getArrayRMS("Sign"), null);



        form.append(onsetDate);
        form.append(disease);
        form.append(diseases);
        form.append(symptom);
        form.append(symptoms);
        form.append(sign);
        form.append(signs);
        form.append(status);



        back = new Command("Back", Command.BACK, 2);
        labTest = new Command("Lab Test", Command.OK, 2);
        submit = new Command("Submit", Command.OK, 2);
        rest = new Command("Reset", Command.OK, 2);
        form.addCommand(back);

        form.addCommand(labTest);
        form.addCommand(submit);
        form.addCommand(rest);
        form.setCommandListener(this);
        form.setItemStateListener(this);
    }

    public void commandAction(Command c, Displayable d) {
        String label = c.getLabel();
        if (label.equals("Back")) {
            backCom();
        } else if (c == labTest) {
           goNextLab();
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
        NonCommunicablePatientDetails oPDPatientDetails = new NonCommunicablePatientDetails(medAdminList);
        medAdminList.display.setCurrent(oPDPatientDetails.form);
    }

    public void reset() {
        sign.setString("");
        disease.setString("");
        symptom.setString("");
        onsetDate.setString("");
        status.setString("");

    }

    public void goNextLab() {
        LaboratoryTestInvestigations laboratoryTestInvestigations = new LaboratoryTestInvestigations(medAdminList, nonCommunicableData);
        medAdminList.display.setCurrent(laboratoryTestInvestigations.list);
    }

    public void itemStateChanged(Item item) {
        Recorder rec = new Recorder();
        if (item == disease) {
            diseases.deleteAll();


            String[] strDi = rec.getArrayRMS("Diag", disease.getString());
            for (int i = 0; i < strDi.length; i++) {
                diseases.append(strDi[i], null);
            }
            String[] strSy = rec.getArrayRMS("Symp", disease.getString());
            for (int i = 0; i < strDi.length; i++) {
                diseases.append(strDi[i], null);
            }
        } else if (item == diseases) {
            signs.deleteAll();
            symptoms.deleteAll();
            String strdis = diseases.getString(diseases.getSelectedIndex());
            String[] strSy = rec.getArrayRMSFromKey("Symp",strdis);
            System.out.println("sss12" + strdis);
            disease.setString(strdis);
            for (int i = 0; i < strSy.length; i++) {
                symptoms.append(strSy[i], null);
                System.out.println("sym" + strSy[i]);
            }
            
            //signs
              String[] strSi = rec.getArrayRMSFromKey("Sign", strdis);
        ///    System.out.println("sss12" + strdis);
         //   disease.setString(strdis);
            for (int i = 0; i < strSi.length; i++) {
                signs.append(strSi[i], null);
                System.out.println("sym" + strSi[i]);
            }
        } else {
            System.out.println("itemStateChanged on " + item.getLabel());
        }
    }


}
