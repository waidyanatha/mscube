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
import org.json.me.JSONObject;

/**
 *
 * @author Madhuka
 */
public class Hospitals implements CommandListener, ItemStateListener {

    private Display display;
    private TextField code, quty, out;
    private Command back, ok, markDef, removeDef;
    private MedAdminList medAdminList;
    private StringItem details;
    public List list;

    public Hospitals(MedAdminList medAdminList, String loc, String name) {
        this.medAdminList = medAdminList;
        list = new List("Hospitals " + name, Choice.IMPLICIT);
        Connector connector = new Connector();
        JSONArray a;
        //  http://localhost:9763/mscube/view/locationType.jag?ltype=PHI&name=Nakkawatta
        try {
            a = new JSONArray(connector.getContain("view/locationType.jag?ltype=" + loc + "&name=" + name));
            for (int i = 0; i < a.length(); i++) {
                System.out.println(a.get(i));
                list.append(a.get(i).toString(), null);
            }

        } catch (JSONException ex) {
            ex.printStackTrace();
        }


        ok = new Command("Drug Audit", Command.OK, 1);
        back = new Command("Back", Command.BACK, 2);
        markDef = new Command("Mark Default", Command.ITEM, 3);
        removeDef = new Command("Remove Default", Command.ITEM, 4);


        //   form.append(search);
        //    form.append(symptoms);
        //    form.append(signs);
        list.addCommand(back);
        list.addCommand(ok);
        list.addCommand(markDef);
        list.addCommand(removeDef);
        list.setCommandListener(this);

    }

    public void commandAction(Command c, Displayable d) {
        String label = c.getLabel();
        if (label.equals("Back")) {
            backCom();
        }
        else if (label.equals("Drug Audit")) {           
            drugAudit();
        }else if (label.equals("Mark Default")) {           
            markDeafult();
        }
        else if (label.equals("Remove Default")) {           
            removeDeafult();
        }
    }

    public void backCom() {
        medAdminList.display.setCurrent(medAdminList.list);
    }

    public void drugAudit() {
        Drugs drugs = new Drugs(medAdminList);
        medAdminList.display.setCurrent(drugs.form);
    }
    
        public void markDeafult() {
            System.out.println("mark as def"+list.getSelectedIndex());
         Recorder rec = new Recorder();
            rec.openRecStore();
            String hospital = list.getString(list.getSelectedIndex());
            if (rec.isExitingRecord(2)) {
                System.out.println("2 we have one ExitingRecord");
                rec.updateRecord(2, hospital);
            } else {
                rec.writeRecord(hospital);
            }
            rec.closeRecStore();
    }

                public void removeDeafult() {
         Recorder rec = new Recorder();
            rec.openRecStore();
         //   String hospital = list.getString(list.getSelectedIndex());
            if (rec.isExitingRecord(1)) {
                rec.deleteRec(1);
            } 
            rec.closeRecStore();
    }
    public void itemStateChanged(Item item) {
        System.out.println(item.getLabel());
    }
}
