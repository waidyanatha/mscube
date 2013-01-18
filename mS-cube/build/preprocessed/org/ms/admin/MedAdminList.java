/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ms.admin;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.Ticker;
import javax.microedition.m3g.TriangleStripArray;
import javax.microedition.midlet.*;

/**
 * @author Madhuka
 */
public class MedAdminList extends MIDlet implements CommandListener {

    public Display display;
    public List list;
    private Command cmExit;
    private Ticker ticker;
    protected static boolean isLogin = false;
     protected static String Username = "";
      protected static String Hospital = "";

    // Create the Commands
    public MedAdminList() {
        list = new List("MedAdmin Main Menu", Choice.IMPLICIT);

    }

    public void startApp() {
        display = Display.getDisplay(this);

        System.out.println("Start");
        cmExit = new Command("Exit", Command.EXIT, 1);
        ticker = new Ticker("Powered by Spot On Solutions");

        list.append("Application tools", null);
        list.append("Drug Audits", null);
        list.append("LogOut", null);

        list.addCommand(cmExit);
        list.setCommandListener(this);
        list.setTicker(ticker);



        if (isLogin == true) {
            display.setCurrent(list);
        } else {
            Login login = new Login(this);
            display.setCurrent(login.form);
        }
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public void commandAction(Command c, Displayable s) {
        // If an implicit list generated the event
        if (c == List.SELECT_COMMAND) {
            switch (list.getSelectedIndex()) {
                case 0:
                    AppToolList createAccount = new AppToolList(this);
                    display.setCurrent(createAccount.listAppTools);
                    break;

                case 2:
                    isLogin = false;
                    Username="";
                    Hospital="";
                    Login login = new Login(this);
                    display.setCurrent(login.form);
                    break;

                case 1:
                    if (isLogin == true) {
                        Drugs drugs = new Drugs(this);
                        display.setCurrent(drugs.form);
                    } else {
                        alertBox("Please Login first", "Infor", AlertType.INFO);
                    }
                    break;
                              default:
                    System.out.println("H544");
            }
        } else if (c == cmExit) {
            destroyApp(false);
            notifyDestroyed();
        }
    }

    public void alertBox(String msg, String info, AlertType type) {
        Alert alert = new Alert(info, msg, null, type);
        alert.setTimeout(3000);
        display.setCurrent(alert);
    }
    
       
}
