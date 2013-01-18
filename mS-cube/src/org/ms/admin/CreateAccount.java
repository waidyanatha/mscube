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
public class CreateAccount implements CommandListener {

    public Form profile;
    private Display display;
    // private Display;
    private Command save, back, reset;
    public ChoiceGroup worktype_cgrp;
    public TextField hw_id, retype_pass, name, surname, midlename, phone, email, pass;
    private MedAdminList medAdminList;

    //http://localhost:9763/mscube/view/workType.jag
    //http://localhost:9763/mscube/view/profile.jag?uname=Ishara&lname=Sandaruwan&type=Suwacevo&emplid=T918020867V
    //[{"deactivate_dt" : null}]
    public CreateAccount(MedAdminList medAdminList) {
        try {
            this.medAdminList = medAdminList;
            profile = new Form("Configure Healthcare worker Profile");

            /**
             * Healthcare work type First Name Middle Initlals Last Name
             * (Surname) Employee ID Mobile Phone Username Password Re-enter
             * Password
             */
            save = new Command("Save", Command.OK, 1);
            reset = new Command("Reset", Command.OK, 4);

            back = new Command("Back", Command.BACK, 1);


            Connector connector = new Connector();
            JSONArray a = new JSONArray(connector.getContain("view/workType.jag"));
            String[] ary = new String[a.length()];

            for (int i = 0; i < a.length(); i++) {
                //   System.out.println("sssss"+i);
                ary[i] = a.getString(i);
            }
            worktype_cgrp = new ChoiceGroup("Healthcare work type", ChoiceGroup.POPUP, ary, null);
            name = new TextField("First Name:(*)", "Ishara", 25, TextField.ANY);
            midlename = new TextField("Middle Initlals", "", 25, TextField.ANY);
            surname = new TextField("Last Name (Surname):(*)", "Sandaruwan", 25, TextField.ANY);
            hw_id = new TextField("Employee ID:(*)", "T918020867V", 15, TextField.ANY);
            phone = new TextField("Mobile Phone:", "", 15, TextField.PHONENUMBER);
            pass = new TextField("Password:(*)", "", 15, TextField.PASSWORD);
            retype_pass = new TextField("Re-enter Password:(*)", "", 15, TextField.PASSWORD);



            profile.addCommand(save);
            profile.addCommand(reset);
            profile.addCommand(back);


            profile.append(worktype_cgrp);
            profile.append(name);
            profile.append(midlename);
            profile.append(surname);
            profile.append(hw_id);
            profile.append(phone);
            profile.append(pass);
            profile.append(retype_pass);
            profile.setCommandListener(this);
        } catch (JSONException ex) {
            medAdminList.alertBox("Connection fails while retrieving Healthcare work type", "Server Connection Fails", AlertType.ERROR);

        }
    }

    public void commandAction(Command c, Displayable d) {

        if (c == back) {
            backCom();

        } else if (c == reset) {
            hw_id.setConstraints(TextField.CONSTRAINT_MASK & TextField.UNEDITABLE);
            //   retype_pass.setConstraints(TextField.CONSTRAINT_MASK & TextField.UNEDITABLE);
            //    profile.addCommand(save);

            hw_id.setString("");
            retype_pass.setString("");
            name.setString("");
            midlename.setString("");
            surname.setString("");
            worktype_cgrp.setSelectedIndex(0, true);
            phone.setString("");
        } else if (c == save) {
            if (pass.getString().length() < 1) {
                medAdminList.alertBox("Please Enter password", "Enter passowrd", AlertType.WARNING);
            } else if (!pass.getString().endsWith(retype_pass.getString())) {
                medAdminList.alertBox("Please enter same password in both Fields in password", "Passwords are not matching", AlertType.WARNING);
            } else {

                if (userExisting(name.getString(), surname.getString(), worktype_cgrp.getString(worktype_cgrp.getSelectedIndex()), hw_id.getString())) {
                    System.out.println("User Exsting");
                    medAdminList.alertBox("Enter profile is existing in there server ", "User is Existing", AlertType.WARNING);
                } else {
                   System.out.println("adding new USer");
                    storeUser(name.getString(), surname.getString(), worktype_cgrp.getString(worktype_cgrp.getSelectedIndex()), hw_id.getString(), midlename.getString(), pass.getString());


                }
            }
        }
    }

    public void backCom() {
        if (MedAdminList.isLogin) {
            medAdminList.display.setCurrent(medAdminList.list);
        } else {
            Login login = new Login(medAdminList);
            medAdminList.display.setCurrent(login.form);
        }

    }

    public boolean userExisting(String Username, String lname, String type, String eid) {
        boolean userExisting = false;
        System.out.println("userExisting" + userExisting);
        try {

            Connector connector = new Connector();
            //view/profile.jag?uname=Ishara&lname=Sandaruwan&type=Suwacevo&emplid=T918020867V
            JSONArray a = new JSONArray(connector.getContain("view/profile.jag?uname=" + Username + "&lname=" + lname + "&type=" + type + "&emplid=" + eid));
            //   String[] ary = new String[a.length()];
            System.out.println("userExisting Out ID " + a.length());
            if (a.length() > 0) {
                userExisting = true;
                System.out.println("2 userExisting" + userExisting);
            }


        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        System.out.println("3 userExisting" + userExisting);
        return userExisting;
    }

    public void storeUser(String Username, String lname, String type, String eid, String midlName, String pass) {
        try {
            System.out.println("view/profile.jag?uname=" + Username + "&lname=" + lname + "&type=" + type + "&emplid=" + eid + "&action=store&mname=" + midlName + "&pass" + pass);
            //http://localhost:9763/mscube/view/profile.jag?action=lastID
            Connector connector = new Connector();
            //http://localhost:9763/mscube/view/profile.jag?uname=Ishara&lname=Sandaruwan&type=Suwacevo&emplid=T918660867V&action=store&pass=1
            JSONArray a = new JSONArray(connector.getContain("view/profile.jag?uname=" + Username + "&lname=" + lname + "&type=" + type + "&emplid=" + eid + "&action=store&mname=" + midlName + "&pass=" + pass));
            //   String[] ary = new String[a.length()];
            System.out.println("storeUser Out ID " + a.length());
            System.out.println("storeUser Out ID  " + a.get(0).toString());
            if (a.length() > 0) {
                System.out.println("Saved in " + a);

                medAdminList.alertBox(Username + " is saved in server data", "Saved In server", AlertType.INFO);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            medAdminList.alertBox("Sever contection fails", "Server failure", AlertType.ERROR);
        }
    }
}
