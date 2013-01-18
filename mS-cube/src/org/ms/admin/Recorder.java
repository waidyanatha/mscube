/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ms.admin;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;



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
            recData = new byte[rs.getRecordSize(place)];
          int  len = rs.getRecord(place, recData, 0);
        outstr = new String(recData, 0, len);
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

}
