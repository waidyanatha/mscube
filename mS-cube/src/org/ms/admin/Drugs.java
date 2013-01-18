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
public class Drugs implements CommandListener, ItemStateListener {

    //urls to calling 
    //http://localhost:9763/mscube/view/drugs.jag?code=1&uName=2&quty=37&hospital=Bihalpola-CD
    
    private Display display;
    private TextField code, quty, out;
    private Command back, add, submit ,rest;
    private MedAdminList medAdminList;
    private StringItem details;
    private Vector vector;
    public Form form;

    public Drugs(MedAdminList medAdminList) {
        this.medAdminList = medAdminList;
        form = new Form("Drugs");
        Connector connector = new Connector();
        JSONArray a;
        try {
            a = new JSONArray(connector.getContain("view/drugs.jag"));
            vector = new Vector();

            for (int i = 0; i < a.length(); i++) {
                JSONObject j = a.getJSONObject(i);
                vector.addElement(j);
            }
            Recorder rec = new Recorder();
            rec.openRecStore();
            //  rec.writeRecord(list.getString(list.getSelectedIndex()));
            System.out.println("Drugs....." + rec.isExitingRecord(2));
            if (rec.isExitingRecord(1)) {
                form.setTitle("Drugs at " + rec.readRecords(2) );
            }

            rec.closeRecStore();
            code = new TextField("Code", "", 100, TextField.NUMERIC);
            details = new StringItem("Description", "...");
            quty = new TextField("Quantity", "", 100, TextField.NUMERIC);
            out = new TextField("Added List", "", 200, TextField.ANY);
            form.append(code);
            form.append(details);
            form.append(quty);
            form.append(out);

        } catch (JSONException ex) {
            ex.printStackTrace();
        }


        add = new Command("Add", Command.SCREEN, 1);
        back = new Command("Back", Command.BACK, 2);
        submit = new Command("Submit", Command.OK, 2);
rest = new Command("Reset", Command.OK, 2);
        form.addCommand(back);
        form.addCommand(add);
        form.addCommand(submit);
        form.addCommand(rest);
        form.setCommandListener(this);
        form.setItemStateListener(this);
    }

    public void commandAction(Command c, Displayable d) {
        String label = c.getLabel();
        if (label.equals("Back")) {
            backCom();
        } else if (label.equals("Add")) {
            addDrug();
        } else if (c == submit) {
              String[] SplitArr = Split(out.getString(), ",");
              boolean finalrec = false;
              for(int i=0; i<SplitArr.length;i++){
              String vlues= SplitArr[i];
              
                  System.out.println("values :"+vlues);
                  System.out.println("values in last :"+vlues.indexOf(" "));
                  System.out.println("values in last :"+vlues.substring(0,vlues.indexOf(" ")));
              String codeDrug = vlues.substring(0,vlues.indexOf(" "));
               String qutyDrug = vlues.substring(vlues.indexOf(" ")+1,vlues.length());
                  System.out.println("xsxsxsxsxs Drugs "+codeDrug +" :  "+qutyDrug);
                  if(i==SplitArr.length-1){
                  finalrec =true;
                  }
                 submit(codeDrug,qutyDrug,finalrec);
              }
            
        } else if (c== rest){
        reset();
        }
    }

    
    public static String[] Split(String splitStr, String delimiter) {
     StringBuffer token = new StringBuffer();
     Vector tokens = new Vector();
     // split
     char[] chars = splitStr.toCharArray();
     for (int i=0; i < chars.length; i++) {
         if (delimiter.indexOf(chars[i]) != -1) {
             // we bumbed into a delimiter
             if (token.length() > 0) {
                 tokens.addElement(token.toString());
                 token.setLength(0);
             }
         } else {
             token.append(chars[i]);
         }
     }
     // don't forget the "tail"...
     if (token.length() > 0) {
         tokens.addElement(token.toString());
     }
     // convert the vector into an array
     String[] splitArray = new String[tokens.size()];
     for (int i=0; i < splitArray.length; i++) {
         splitArray[i] = (String)tokens.elementAt(i);
     }
     return splitArray;
 }
    public void backCom() {
        medAdminList.display.setCurrent(medAdminList.list);
    }

    public void submit(String codeName, String qty, boolean finalout) {
      
      //  http://localhost:9763/mscube/view/drugs.jag?code=1&uName=2&quty=37&hospital=Bihalpola-CD
         try {
readDataRMS();
            Connector connector = new Connector();
            //http://localhost:9763/mscube/view/drugs.jag?code=1&uName=2&quty=37&hospital=Bihalpola-CD
            JSONArray a = new JSONArray(connector.getContain("view/drugs.jag?code="+codeName+"&uName="+MedAdminList.Username+"&quty="+qty+"&hospital="+MedAdminList.Hospital));
            //   String[] ary = new String[a.length()];
            System.out.println("submitting drug to server " + a.length());
            if (a.length() > 0) {
                System.out.println("submitting drug to server out put" + a.get(0));
                if(finalout){
                  medAdminList.alertBox("Send to the Server", "Succesfully Send", AlertType.INFO);
                  out.setString("");
                  code.setString("");
                  quty.setString("");
                }
            }


        } catch (JSONException ex) {
           medAdminList.alertBox("Sending was fails to the  Server", "Server fails", AlertType.ERROR);
        }
    }
    
     public void reset() {
        code.setString("");
        quty.setString("");
        out.setString("");
        details.setText("");
    }

    public void addDrug() {
        if (code.getString().length() == 0) {
            medAdminList.alertBox("Please enter code first", "Enter Code", AlertType.INFO);
        } else {
            String toAdd = code.getString() + " " + quty.getString();
            if(out.getString().length()>1){
            out.setString(toAdd + "," + out.getString());
            }else{
            out.setString(toAdd);
            }
            quty.setString("");
        }
    }

    public void itemStateChanged(Item item) {
        if (item == code) {
            try {
                System.out.println(code.getString());
                String  str = code.getString();
                if(str.length()>0){
                JSONObject j = (JSONObject) vector.elementAt(Integer.parseInt(code.getString()));
                String describe = j.getString("item_desc");
                String nameDrug = j.getString("item_name");
                System.out.println(describe);               
                //  details.setLabel(nameDrug + " Description");
                details.setText(" - " +nameDrug + " - " + describe);
                 }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }


    }
    public void readDataRMS(){
      Recorder rec = new Recorder();
            rec.openRecStore();
          //  String  uNmae= userName.getString();
            if (rec.isExitingRecord(1)) {
                MedAdminList.Username=rec.readRecords(1);
            } else {
                medAdminList.alertBox("Pease login first", "Login First", AlertType.WARNING);
                
            }
             if (rec.isExitingRecord(2)) {
                MedAdminList.Hospital=rec.readRecords(2);
            } else {
                medAdminList.alertBox("Pease Select the location from hte appliation tool", "Select the location", AlertType.WARNING);
                
            }
           
    }
}
