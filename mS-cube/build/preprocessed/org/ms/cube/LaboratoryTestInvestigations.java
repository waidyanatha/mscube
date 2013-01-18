/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ms.cube;

import javax.microedition.lcdui.*;
import java.util.*;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordEnumeration;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.ms.cube.MsCube;
import org.ms.cube.OPD.OPDData;
import org.ms.cube.OPD.OPDData;
import org.ms.cube.OPD.OPDPatientDiagnosis;
import org.ms.cube.OPD.OPDPatientDiagnosis;
import org.ms.cube.admissioin.AdmisionData;
import org.ms.cube.admissioin.AdmissioinsPatientDiagnosis;
import org.ms.cube.noncommunicable.NonCommunicableData;
import org.ms.cube.noncommunicable.NonCommunicablePatientDiagnosis;

/**
 *
 * @author Madhuka
 */
public class LaboratoryTestInvestigations implements CommandListener {

    private Display display;
    private TextField code, quty, out;
    private Command back, reset, submit;
    private MsCube medAdminList;
    private StringItem details;
    public List list;
    public OPDData oPDData;
    public AdmisionData admisionData;
    public NonCommunicableData noncommunicableData;

    public LaboratoryTestInvestigations(MsCube medAdminList, OPDData opdd) {
        this.medAdminList = medAdminList;
        this.oPDData = opdd;
        list = new List("Laboratory test investigations " + opdd.getPatientName(), Choice.MULTIPLE);
        System.out.println("OPD data of " + opdd.getPatientName()
                + '\n' + opdd.getAgeGroup()
                + '\n' + opdd.getGender()
                + '\n' + opdd.getTime()
                + '\n' + opdd.getDate()
                + '\n' + opdd.getNote()
                + '\n' + opdd.getOnsetDate()
                + '\n' + opdd.getStatus()
                + '\n' + opdd.getDisease()
                + '\n' + opdd.getSymptom()
                + '\n' + opdd.getSign());
        screenShow();
    }

    public LaboratoryTestInvestigations(MsCube medAdminList, AdmisionData ad) {
        this.medAdminList = medAdminList;
        this.admisionData = ad;
        list = new List("Laboratory test investigations " + admisionData.getPatientName(), Choice.MULTIPLE);
        screenShow();
    }

    public LaboratoryTestInvestigations(MsCube medAdminList, NonCommunicableData ncd) {
        this.medAdminList = medAdminList;
        this.noncommunicableData = ncd;
        list = new List("Laboratory test investigations " + noncommunicableData.getPatientName(), Choice.MULTIPLE);
        screenShow();
    }

    private void screenShow() {
        //  details = new StringItem("Select all relevant tests", null);
        list.append("FBC(full blood count)", null);
        list.append("BS (blood sugar)", null);
        back = new Command("Back", Command.BACK, 2);
        reset = new Command("Reset", Command.ITEM, 3);
        submit = new Command("Submit", Command.ITEM, 4);
        list.addCommand(back);
        list.addCommand(reset);
        list.addCommand(submit);
        list.setCommandListener(this);

    }

    public void commandAction(Command c, Displayable d) {
        String label = c.getLabel();
        if (label.equals("Back")) {
            backCom();
        } else if (c == reset) {
            reset();
        } else if (c == submit) {
            submit();
        }
    }

    public void backCom() {
        if (oPDData != null) {
            OPDPatientDiagnosis oPDPatientDiagnosis = new OPDPatientDiagnosis(medAdminList, oPDData);
            medAdminList.display.setCurrent(oPDPatientDiagnosis.form);
        } else if (admisionData != null) {
            AdmissioinsPatientDiagnosis admissioinsPatientDiagnosis = new AdmissioinsPatientDiagnosis(medAdminList, admisionData);
            medAdminList.display.setCurrent(admissioinsPatientDiagnosis.form);

        } else if (noncommunicableData != null) {
            NonCommunicablePatientDiagnosis nonCommunicablePatientDiagnosis = new NonCommunicablePatientDiagnosis(medAdminList, noncommunicableData);
            medAdminList.display.setCurrent(nonCommunicablePatientDiagnosis.form);
        }

    }

    public void submit() {
        //http://10.224.244.247:9763/headsup/view/opd.jag?action=nextID

        //http://10.224.244.247:9763/headsup/view/opd.jag?action=submit&patientName=jhon&gender=male&ageGroup=65-75&disease=Acne
        System.out.println("Submit");
        try {
//readDataRMS();
            Connector connector = new Connector();
            
            String url = "view/opd.jag?action=submit&"
                    + "patientName=" + oPDData.getPatientName() + "&"
                    + "gender=" + oPDData.getGender() + "&"
                    + "ageGroup=" + oPDData.getAgeGroup() + "&"
                    + "disease=" + oPDData.getDisease() + "&"
                    + "symptom=" + oPDData.getSymptom() + "&"
                    + "sign=" + oPDData.getSign() + "&"            
                    + "uname=" + MsCube.Username + "&"
                    + "hospital=" + MsCube.Hospital + "&"
                    + "type=OPD" + "&"
                    + "status=" + oPDData.getStatus();
            //  url.re
            url = url.replace(' ', '-');

            System.out.println("URL" + url);
            JSONArray a = new JSONArray(connector.getContain(url));
            //   String[] ary = new String[a.length()];
            System.out.println("submitting OPD to server " + a.length());
            if (a.length() > 0) {

                medAdminList.alertBox("Succesfully " + a.getJSONObject(0).getString("task").toString(), "Succesfully Send", AlertType.INFO);

            }


        } catch (JSONException ex) {
            medAdminList.alertBox("Sending was fails to the  Server", "Server fails", AlertType.ERROR);
        }
    }

    public void reset() {
        for (int i = 0; i < list.size(); i++) {
            list.setSelectedIndex(i, false);
        }
    }
}
