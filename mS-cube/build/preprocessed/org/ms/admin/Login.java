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
public class Login implements CommandListener {

    private TextField userName, password;
    private Command exit, ok, singup;
    private StringItem item;
    private MedAdminList medAdminList;
    public Form form;

    public Login(MedAdminList medAdminList) {
        this.medAdminList = medAdminList;
        form = new Form("Login");
        item = new StringItem("Welcome to the Mobile Medical Administrator","");
        form.append(item);
        userName = new TextField("User Name: ", null, 100, TextField.ANY);
        password = new TextField("Password: ", null, 200, TextField.PASSWORD);

        ok = new Command("Login", Command.SCREEN, 1);
        exit = new Command("Exit", Command.EXIT, 1);
        singup = new Command("Sign Up", Command.OK, 2);

        form.append(userName);
        form.append(password);
        form.addCommand(exit);
        form.addCommand(ok);
        form.addCommand(singup);
        form.setCommandListener(this);
    }

    public void commandAction(Command c, Displayable d) {
        String label = c.getLabel();
        if (label.equals("Exit")) {
            System.out.println("exitapp");
            exitCom();
        }
        if (label.equals("Login")) {
            System.out.println("sssssss"+userName.getString().length());
            if (userName.getString().length() == 0) {
                medAdminList.alertBox("Please Enter the User Name", "User Name is empty", AlertType.INFO);
            } else if (password.getString().length() == 0) {
                medAdminList.alertBox("Please Enter Password", "Password is blank", AlertType.INFO);
            } else {
                System.out.println("Login");
                login(userName.getString(), password.getString());
            }
        }
        if (label.equals("Sign Up")) {
            signUp();
        }
    }

    public void exitCom() {
        medAdminList.destroyApp(true);
        medAdminList.notifyDestroyed();
    }

    public void signUp() {
        CreateAccount createAccount = new CreateAccount(medAdminList);
        medAdminList.display.setCurrent(createAccount.profile);
    }

    public void login(String name, String pass) {
        try {

            // http://localhost:9763/mscube/view/profile.jag?uname=us&pass=us1
            Connector connector = new Connector();
            
            JSONArray a = new JSONArray(connector.getContain("view/profile.jag?uname=" + name + "&pass=" + pass));
            if (a.getJSONObject(0).get("user_p_uuid") != null) {
                MedAdminList.isLogin = true;
                storeUsername();
            } else {
                MedAdminList.isLogin = false;
 medAdminList.alertBox("Please Enter the correct User Name and  Password", "Incorrect Password", AlertType.WARNING);
            }
            medAdminList.display.setCurrent(medAdminList.list);
        } catch (JSONException ex) {
            medAdminList.alertBox("Please Enter the correct User Name and  Password", "Exception in Login Server", AlertType.WARNING);
        }

    }
    
           public void storeUsername() {
         Recorder rec = new Recorder();
            rec.openRecStore();
            String  uNmae= userName.getString();
            if (rec.isExitingRecord(1)) {
                rec.updateRecord(1, uNmae);
                 System.out.println("update RMS Stored"+uNmae);
            } else {
                System.out.println("saved RMS Stored"+uNmae);
                rec.writeRecord(uNmae);
            }
          //  rec.readRecords(1);
            rec.closeRecStore();
    }
}
