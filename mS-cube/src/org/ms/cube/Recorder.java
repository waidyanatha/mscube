/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ms.cube;

import java.util.Vector;
import javax.microedition.lcdui.List;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import org.json.me.JSONArray;
import org.json.me.JSONException;



/**
 *
 * @author Madhuka
 */
public class Recorder {
    private RecordStore rs = null;
  static final String REC_STORE = "ReadWriteRMS";
  
   public void openRecStore(){
    try{
      rs = RecordStore.openRecordStore(REC_STORE, true );
    }catch (Exception e){}
  }    

  public void closeRecStore(){
    try{
      rs.closeRecordStore();
    }catch (Exception e){}
  }

  public void deleteRecStore(){
    if (RecordStore.listRecordStores() != null){
      try{
        RecordStore.deleteRecordStore(REC_STORE);
      }catch (Exception e){}
    }      
  }
  
    public void deleteRec(int id){
    if (RecordStore.listRecordStores() != null){
      try{
          rs.deleteRecord(id);
      }catch (Exception e){}
    }      
  }

  public void writeRecord(String str){
    byte[] rec = str.getBytes();
    try{
      rs.addRecord(rec, 0, rec.length);
    }catch (Exception e){}
  }
  

    
  public void updateRecord(int recordId, String str){
    byte[] rec = str.getBytes();
    try{
      rs.setRecord(recordId, rec, 0, rec.length);
    }catch (Exception e){
        System.out.println("errr"+e);
    }
  }
  
  public boolean isExitingRecord(int recordId){
      boolean out = false;
 //   byte[] rec = str.getBytes();
    try{
      byte[] recData= rs.getRecord(recordId);
      if(recData!=null){out=true;}
    }catch (Exception e){
   
    }return out;
  }
  public String readRecords(int place){
      String outstr="";
      byte[] recData = new byte[5]; 
        try {
            System.out.println("place"+place);
            recData = new byte[rs.getRecordSize(place)];
         // int  len = rs.getRecord(place, recData, 0);
       // outstr = new String(recData, 0, len);
           int  len = rs.getRecord(place, recData, 0);
      //   recData = new byte[rs.getRecordSize(place+1)];
                           //  int len = rs.getRecord(place, recData, 0);
                             outstr =  new String(recData, 0, len);
        //  str   = rs.getRecord(place).toString();
            
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
        return outstr;
  }
  
  public String readRecords(){
      String out ="";
    try{
      byte[] recData = new byte[5]; 
      int len;
        System.out.println("getNumRecords"+rs.getNumRecords());
      for(int i = 1; i <= rs.getNumRecords(); i++){
        if(rs.getRecordSize(i) > recData.length){
          recData = new byte[rs.getRecordSize(i)];
        }
        len = rs.getRecord(i, recData, 0);
        out = new String(recData, 0, len);
        System.out.println("------------------------------");
        System.out.println("Record " + i + " : " + out);
        System.out.println("------------------------------");                        
      }
    }catch (Exception e){}
    return out;
  }

  public String[] getAllArrayRMS(String rmsName){
       String[] ary=null;
       JSONArray a ;
        try {
            RecordStore recordMS= RecordStore.openRecordStore(rmsName, true);
                     System.out.println(rmsName +"type RMS" + recordMS.getNumRecords());
                    

                         ary = new String[recordMS.getNumRecords()];
                         String outstr = "";
                         byte[] recData = new byte[5];
                         for (int i = 0; i < ary.length; i++) {
//                             System.out.println("sssss" + i);
//                             System.out.println("ssasa"+recordMS.getRecordSize(i+1));
//                             System.out.println("ssasa"+recordMS.getRecord(i+1));
                             recData = new byte[recordMS.getRecordSize(i+1)];
                             int len = recordMS.getRecord(i+1, recData, 0);
                             outstr = new String(recData, 0, len);
                             ary[i] = outstr;

                         
                     }
        }  catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
        return  ary;
  }
  
    public String[] getArrayRMS(String rmsName){
       String[] ary=null;
       JSONArray a ;
        try {
            RecordStore recordMS= RecordStore.openRecordStore(rmsName, true);
                     System.out.println(rmsName +"type RMS" + recordMS.getNumRecords());
                    

                         ary = new String[recordMS.getNumRecords()];
                         String outstr = "";
                         byte[] recData = new byte[5];
                         for (int i = 0; i < ary.length; i++) {
//                             System.out.println("sssss" + i);
//                             System.out.println("ssasa"+recordMS.getRecordSize(i+1));
//                             System.out.println("ssasa"+recordMS.getRecord(i+1));
                             recData = new byte[recordMS.getRecordSize(i+1)];
                             int len = recordMS.getRecord(i+1, recData, 0);
                             outstr = new String(recData, 0, len);
                             ary[i] = outstr.substring(outstr.indexOf("-")+1);

                         
                     }
        }  catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
        return  ary;
  }
  
  
   public String[] getArrayRMS(String rmsName,String filtr){
       String[] ary=null;
      Vector vector=null;
       JSONArray a ;
        try {
            RecordStore recordMS= RecordStore.openRecordStore(rmsName, true);
                     System.out.println(rmsName +"type RMS" + recordMS.getNumRecords());
                    vector = new Vector();

                         ary = new String[recordMS.getNumRecords()];
                         String outstr = "";
                         String outstrx = "";
                         byte[] recData = new byte[5];
                         for (int i = 0; i < ary.length; i++) {
//                             System.out.println("sssss" + i);
//                             System.out.println("ssasa"+recordMS.getRecordSize(i+1));
//                             System.out.println("ssasa"+recordMS.getRecord(i+1));
                             recData = new byte[recordMS.getRecordSize(i+1)];
                             int len = recordMS.getRecord(i+1, recData, 0);
                             outstr = new String(recData, 0, len);
                             outstrx = outstr.substring(outstr.indexOf("-")+1);
                             if(outstrx.startsWith(filtr)){
                             vector.addElement(outstrx);
                             }

                     }
                         ary = new String[vector.size()];
for (int i = 0; i < vector.size(); i++) {
    ary[i] = (String)vector.elementAt(i);
}
        }  catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
        return  ary;
  }
   
     
   public String[] getArrayRMSFromKey(String rmsName,String key){
       String[] ary=null;
      Vector vector=null;
       JSONArray a ;
        try {
            RecordStore recordMS= RecordStore.openRecordStore(rmsName, true);
                     System.out.println(rmsName +"type RMS" + recordMS.getNumRecords());
                    vector = new Vector();

                         ary = new String[recordMS.getNumRecords()];
                         String outstr = "";
                         String outstrx = "";
                         String outstry = "";
                         byte[] recData = new byte[5];
                         for (int i = 0; i < ary.length; i++) {
//                             System.out.println("sssss" + i);
//                             System.out.println("ssasa"+recordMS.getRecordSize(i+1));
//                             System.out.println("ssasa"+recordMS.getRecord(i+1));
                             recData = new byte[recordMS.getRecordSize(i+1)];
                             int len = recordMS.getRecord(i+1, recData, 0);
                             outstr = new String(recData, 0, len);
                             outstrx = outstr.substring(0, outstr.indexOf("-"));
                             if(outstrx.startsWith(key)){
                                 outstry = outstr.substring(outstr.indexOf("-")+1);
                             vector.addElement(outstry);
                             }

                     }
                         ary = new String[vector.size()];
for (int i = 0; i < vector.size(); i++) {
    ary[i] = (String)vector.elementAt(i);
}
        }  catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
        return  ary;
  }
}
