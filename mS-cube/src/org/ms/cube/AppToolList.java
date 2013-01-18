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
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 *
 * @author Madhuka
 */
public class AppToolList implements CommandListener {

    private Display display;
    private Command back, cmExit;
    private MsCube medAdminList;
    public List listAppTools;

    public AppToolList(MsCube medAdminList) {
        this.medAdminList = medAdminList;
        listAppTools = new List("Application Tools", Choice.IMPLICIT);

        cmExit = new Command("Exit", Command.EXIT, 1);

        back = new Command("Back", Command.BACK, 1);
        //application Tool menu list 
        listAppTools.append("Register Locations", null);
        listAppTools.append("Download Diseae-Syndrome", null);
        listAppTools.append("Download Details", null);
        listAppTools.append("Create Account", null);
        listAppTools.addCommand(cmExit);
        listAppTools.addCommand(back);
        listAppTools.setCommandListener(this);





    }

    public void commandAction(Command c, Displayable d) {
        //  String label = c.getLabel();
        // If an implicit list generated the event
        if (c == List.SELECT_COMMAND) {
            switch (listAppTools.getSelectedIndex()) {
                case 0:
                    if (MsCube.isLogin == true) {
                        LocationType locationType = new LocationType(medAdminList);
                        medAdminList.display.setCurrent(locationType.form);
                    } else {
                        medAdminList.alertBox("Please Login first", "Infor", AlertType.INFO);
                    }
                    break;

                case 1:
                    downloadDiseaeSyndrome();
                    break;
                case 2:
                    downloadAll();
                    break;

                case 3:
                    CreateAccount createAccount = new CreateAccount(medAdminList);
                    medAdminList.display.setCurrent(createAccount.profile);
                    break;

                default:
                    System.out.println("default");
            }
        }
        if (c == back) {
            backCom();
        } else if (c == cmExit) {
            cmExit();
        }
    }

    public void backCom() {
        medAdminList.display.setCurrent(medAdminList.list);
    }

    public void cmExit() {
        medAdminList.destroyApp(true);
        medAdminList.notifyDestroyed();
    }

    public void downloadDiseaeSyndrome() {
        downloadDiseae();
        downloadSyndrome();
        downloadSign();
        medAdminList.alertBox("Record Store is updated on Diseaes, Signs and Syndromes", "Diseae Syndrome Dwonload Completed", AlertType.INFO);
    }

    public void downloadAll() {
//        downloadDiseae();
//        downloadSyndrome();
//        downloadSign();
        downloadAgeGroup();
        downloadWards();
        downloadCaseStatus();
        medAdminList.alertBox("Record Store is updated on Age Groups, Cases and Wards", "App Details Dwonload Completed", AlertType.INFO);
    }

    //http://localhost:9763/mscube/view/disease.jag?list=disease&mini=1
    public void downloadDiseae() {
        try {
            RecordStore dia_rs;
            RecordStore.deleteRecordStore("Diag");
            dia_rs = RecordStore.openRecordStore("Diag", true);
            //   }
            if (dia_rs.getNumRecords() == 0) {
                Connector connector = new Connector();
                JSONArray a = new JSONArray(connector.getContain("view/disease.jag?list=disease&mini=1"));

                for (int i = 0; i < a.length(); i++) {

                    String str = a.getString(i);
                    System.out.println(i + ".disease " + str);
                    //      loctype_rs.addRecord(data, 0, i);
                    byte[] rec = str.getBytes();

                    dia_rs.addRecord(rec, 0, rec.length);

                }
                System.out.println("End Length: " + dia_rs.getNumRecords());
                dia_rs.closeRecordStore();
            }
        } catch (JSONException ex) {
            medAdminList.alertBox("Server respond is not readable", "Server Respond Fail", AlertType.ERROR);
        } catch (RecordStoreException ex) {
            medAdminList.alertBox("Record Store is fail to process", "Phone Memory Error", AlertType.ERROR);
        }
    }

    //http://localhost:9763/mscube/view/disease.jag?list=symptom&mini
    public void downloadSyndrome() {
        try {
            RecordStore sym_r;
            RecordStore.deleteRecordStore("Symp");
            sym_r = RecordStore.openRecordStore("Symp", true);
            //   }
            if (sym_r.getNumRecords() == 0) {
                Connector connector = new Connector();
                JSONArray a = new JSONArray(connector.getContain("view/disease.jag?list=symptom&mini"));

                for (int i = 0; i < a.length(); i++) {

                    String str = a.getString(i);
                    System.out.println(i + ".Syndrome" + str);
                    //      loctype_rs.addRecord(data, 0, i);
                    byte[] rec = str.getBytes();

                    sym_r.addRecord(rec, 0, rec.length);

                }
                System.out.println("End Length: " + sym_r.getNumRecords());
                sym_r.closeRecordStore();
            }
        } catch (JSONException ex) {
            medAdminList.alertBox("Server respond is not readable", "Server Respond Fail", AlertType.ERROR);
        } catch (RecordStoreException ex) {
            medAdminList.alertBox("Record Store is fail to process", "Phone Memory Error", AlertType.ERROR);
        }
    }

    //http://localhost:9763/mscube/view/disease.jag?list=sign&mini
    public void downloadSign() {
        try {
            RecordStore sign_rs;
            RecordStore.deleteRecordStore("Sign");
            sign_rs = RecordStore.openRecordStore("Sign", true);
            //   }
            if (sign_rs.getNumRecords() == 0) {
                Connector connector = new Connector();
                JSONArray a = new JSONArray(connector.getContain("view/disease.jag?list=sign&mini"));

                for (int i = 0; i < a.length(); i++) {

                    String str = a.getString(i);
                    System.out.println(i + ".Sign" + str);
                    //      loctype_rs.addRecord(data, 0, i);
                    byte[] rec = str.getBytes();

                    sign_rs.addRecord(rec, 0, rec.length);

                }
                System.out.println("End Length: " + sign_rs.getNumRecords());
                sign_rs.closeRecordStore();
            }
        } catch (JSONException ex) {
            medAdminList.alertBox("Server respond is not readable", "Server Respond Fail", AlertType.ERROR);
        } catch (RecordStoreException ex) {
            medAdminList.alertBox("Record Store is fail to process", "Phone Memory Error", AlertType.ERROR);
        }
    }

    //http://localhost:9763/headsup/view/wards.jag
    public void downloadWards() {
        try {
            RecordStore ward_rs;
            RecordStore.deleteRecordStore("Ward");
            ward_rs = RecordStore.openRecordStore("Ward", true);
            //   }
            if (ward_rs.getNumRecords() == 0) {
                Connector connector = new Connector();
                JSONArray a = new JSONArray(connector.getContain("view/wards.jag"));

                for (int i = 0; i < a.length(); i++) {

                    String str = a.getString(i);
                    System.out.println(i + ".wards " + str);
                    //      loctype_rs.addRecord(data, 0, i);
                    byte[] rec = str.getBytes();

                    ward_rs.addRecord(rec, 0, rec.length);

                }
                System.out.println("End Length: " + ward_rs.getNumRecords());
                ward_rs.closeRecordStore();
            }
        } catch (JSONException ex) {
            medAdminList.alertBox("Server respond is not readable", "Server Respond Fail", AlertType.ERROR);
        } catch (RecordStoreException ex) {
            medAdminList.alertBox("Record Store is fail to process", "Phone Memory Error", AlertType.ERROR);
        }
    }

    //http://localhost:9763/headsup/view/agegroup.jag
    public void downloadAgeGroup() {
        try {
            RecordStore age_rs;
            RecordStore.deleteRecordStore("Age");
            age_rs = RecordStore.openRecordStore("Age", true);
            //   }
            if (age_rs.getNumRecords() == 0) {
                Connector connector = new Connector();
                JSONArray a = new JSONArray(connector.getContain("view/agegroup.jag"));

                for (int i = 0; i < a.length(); i++) {

                    String str = a.getString(i);
                    System.out.println(i + ".agegroup " + str);
                    //      loctype_rs.addRecord(data, 0, i);
                    byte[] rec = str.getBytes();

                    age_rs.addRecord(rec, 0, rec.length);

                }
                System.out.println("End Length: " + age_rs.getNumRecords());
                age_rs.closeRecordStore();
            }
        } catch (JSONException ex) {
            medAdminList.alertBox("Server respond is not readable", "Server Respond Fail", AlertType.ERROR);
        } catch (RecordStoreException ex) {
            medAdminList.alertBox("Record Store is fail to process", "Phone Memory Error", AlertType.ERROR);
        }
    }
    
        public void downloadCaseStatus() {
        try {
            RecordStore status_rs;
            RecordStore.deleteRecordStore("CState");
            status_rs = RecordStore.openRecordStore("CState", true);
            //   }
            if (status_rs.getNumRecords() == 0) {
                Connector connector = new Connector();
                JSONArray a = new JSONArray(connector.getContain("view/case.jag"));

                for (int i = 0; i < a.length(); i++) {

                    String str = a.getString(i);
                    System.out.println(i + ".CState " + str);
                    //      loctype_rs.addRecord(data, 0, i);
                    byte[] rec = str.getBytes();

                    status_rs.addRecord(rec, 0, rec.length);

                }
                System.out.println("End Length: " + status_rs.getNumRecords());
                status_rs.closeRecordStore();
            }
        } catch (JSONException ex) {
            medAdminList.alertBox("Server respond is not readable", "Server Respond Fail", AlertType.ERROR);
        } catch (RecordStoreException ex) {
            medAdminList.alertBox("Record Store is fail to process", "Phone Memory Error", AlertType.ERROR);
        }
    }
}
