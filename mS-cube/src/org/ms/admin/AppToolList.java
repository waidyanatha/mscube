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
public class AppToolList implements CommandListener {

    private Display display;
    private Command back, cmExit;
    private MedAdminList medAdminList;
    public List listAppTools;

    public AppToolList(MedAdminList medAdminList) {
        this.medAdminList = medAdminList;
        listAppTools = new List("Application Tools", Choice.IMPLICIT);

        cmExit = new Command("Exit", Command.EXIT, 1);

        back = new Command("Back", Command.BACK, 1);
        //application Tool menu list 
        listAppTools.append("Locatioin Types", null);
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
                    if (MedAdminList.isLogin == true) {
                        LocationType locationType = new LocationType(medAdminList);
                        medAdminList.display.setCurrent(locationType.form);
                    } else {
                        medAdminList.alertBox("Please Login first", "Infor", AlertType.INFO);
                    }
                    break;

                case 1:
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
}
