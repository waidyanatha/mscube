/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ms.cube.OPD;

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
public class OPDPatientDiagnosis implements CommandListener, ItemStateListener {

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
    private TextField onsetDate, disease, symptom, sign;
    private Command back, labTest, rest, submit;
    private MsCube medAdminList;
    private ChoiceGroup diseases, symptoms, signs,status;
    OPDData oPDData;
    public Form form;

    public OPDPatientDiagnosis(MsCube medAdminList, OPDData oPDData) {
        this.oPDData = oPDData;
        System.out.println("OPDPatientDiagnosis pname" + oPDData.getPatientName());
        this.medAdminList = medAdminList;
        form = new Form("OPD patient case details");
        Date datex = new Date();
        String datestr = datex.toString().substring(0, 10);
        String timestr = datex.toString().substring(10);
        onsetDate = new TextField("OnSet Date", datestr, 100, TextField.ANY);
        disease = new TextField("disease* - Search/add", "", 100, TextField.ANY);
        symptom = new TextField("Symptoms* - Search/add", "", 200, TextField.ANY);
        sign = new TextField("Signs* - Search/add", "", 200, TextField.ANY);
       
        String[] gedArr = {"Male", "Female"};
        Recorder rec = new Recorder();
        diseases = new ChoiceGroup("Select Disease", ChoiceGroup.POPUP, rec.getArrayRMS("Diag"), null);
        symptoms = new ChoiceGroup("Select Symptom", ChoiceGroup.POPUP, rec.getArrayRMS("Symp"), null);
        signs = new ChoiceGroup("Select Sign", ChoiceGroup.POPUP, rec.getArrayRMS("Sign"), null);
        status = new ChoiceGroup("Status", ChoiceGroup.POPUP, rec.getArrayRMS("CState"), null);



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

    public void backCom() {
        OPDPatientDetails oPDPatientDetails = new OPDPatientDetails(medAdminList);
        medAdminList.display.setCurrent(oPDPatientDetails.form);
    }

    public void reset() {
        sign.setString("");
        disease.setString("");
        symptom.setString("");
        onsetDate.setString("");
       

    }

    public void goNextLab() {
        oPDData.setDisease(disease.getString());
        oPDData.setSymptom(symptom.getString());
        oPDData.setSign(sign.getString());
        oPDData.setOnsetDate(onsetDate.getString());
        oPDData.setStatus(status.getString(status.getSelectedIndex()));
        
        LaboratoryTestInvestigations laboratoryTestInvestigations = new LaboratoryTestInvestigations(medAdminList, oPDData);        
        medAdminList.display.setCurrent(laboratoryTestInvestigations.list);
    }

    public void itemStateChanged(Item item) {
        Recorder rec = new Recorder();
        
        //diseases mgt
        if (item == disease) {
            System.out.println(" --> disease");
            diseases.deleteAll();
            String[] strDi = rec.getArrayRMS("Diag", disease.getString());
            for (int i = 0; i < strDi.length; i++) {
                diseases.append(strDi[i], null);
            }

        }

          else if (item == diseases) {
            String sy = "";
            String si = "";
            String strdis = diseases.getString(diseases.getSelectedIndex());
            String[] strSy = rec.getArrayRMSFromKey("Symp", strdis);
          //  System.out.println("sss12" + strdis);
            disease.setString(strdis);
            for (int i = 0; i < strSy.length; i++) {
                sy += strSy[i].toString() + ",";
           //     System.out.println("sym" + strSy[i].toString());
            }
            System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxx" + sy);
            symptom.setString(sy);
            String[] strSi = rec.getArrayRMSFromKey("Sign", strdis);
            for (int i = 0; i < strSi.length; i++) {
                si += strSi[i] + ",";
                System.out.println("sing" + strSi[i]);
            }
            sign.setString(si);

        } else if (item == symptom) {
            System.out.println(" --> symptom");
            symptoms.deleteAll();

            String stry = symptom.getString();
            System.out.println("last id " + stry.substring(stry.length() - 1));
            String[] strDi = rec.getArrayRMS("Symp", stry.substring(stry.length() - 1));
            for (int i = 0; i < strDi.length; i++) {
                symptoms.append(strDi[i], null);
            }
        } else if (item == symptoms) {
             System.out.println(" --> symptoms");
            String strsym = symptoms.getString(symptoms.getSelectedIndex());
            String curstr = symptom.getString();
             if (curstr.length()>0){
            curstr = curstr.substring(0, curstr.lastIndexOf(',')+1) +strsym + ",";
            }else{
                curstr = strsym + ",";
            }
            symptom.setString(curstr);
        } 
         else if (item == sign) {
            System.out.println(" --> sign");
            signs.deleteAll();

            String stry = sign.getString();
            System.out.println("last id " + stry.substring(stry.length() - 1));
            String[] strDi = rec.getArrayRMS("Sign", stry.substring(stry.length() - 1));
            for (int i = 0; i < strDi.length; i++) {                
                signs.append(strDi[i], null);
            }
        } 
         else if (item == signs) {
             System.out.println(" --> signs");
            String strsym = signs.getString(signs.getSelectedIndex());
            String curstr = sign.getString();
            if (curstr.length()>0){
            curstr = curstr.substring(0, curstr.lastIndexOf(',')+1) +strsym + ",";
            }else{
                curstr = strsym + ",";
            }
            sign.setString(curstr);
        } 
        else {
            System.out.println("itemStateChanged on " + item.getLabel());
        }
    }
}
