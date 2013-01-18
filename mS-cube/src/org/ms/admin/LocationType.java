/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ms.admin;

import javax.microedition.lcdui.*;
import java.util.*;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordEnumeration;
import org.json.me.JSONArray;
import org.json.me.JSONException;

/**
 *
 * @author Madhuka
 */
public class LocationType implements CommandListener {

    private Display display;
    private TextField textField;
    private Command back,ok;
    private MedAdminList medAdminList;
    private ChoiceGroup CoursePOP;
    public Form form;

    public LocationType(MedAdminList medAdminList) {
        try {
            this.medAdminList = medAdminList;
            form = new Form("Location Type");

            back = new Command("Back", Command.BACK, 2);
             ok = new Command("List Hospitals", Command.OK, 2);

//locations from server call
            Connector connector = new Connector();
            JSONArray a = new JSONArray(connector.getContain("view/locationType.jag"));
             String[] ary = new String[a.length()];
       
            for (int i = 0; i < a.length(); i++) {
                System.out.println("sssss"+i);
                ary[i] = a.getString(i);
            }
            
            
            CoursePOP = new ChoiceGroup ("Location Type", Choice.POPUP,ary, null);
            form.append(CoursePOP);
            textField = new TextField("Name", "Nakkawatta", 100, TextField.ANY);
            form.append(textField);
            form.addCommand(back);
            form.addCommand(ok);
            form.setCommandListener(this);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public void commandAction(Command c, Displayable d) {
        String label = c.getLabel();
        if (label.equals("Back")) {
            backCom();
        }else if (label.equals("List Hospitals")) {
            listHospitals();
        }

    }

    public void backCom() {
        medAdminList.display.setCurrent(medAdminList.list);
    }
     public void listHospitals() {
         Hospitals hospitals  = new Hospitals(medAdminList, CoursePOP.getString(CoursePOP.getSelectedIndex()), textField.getString());
        medAdminList.display.setCurrent(hospitals.list);
    }
}
