package com.ssmData.config.entity;

public class Backup {
    private int BackupID;
    private int[] BackupAtt;
    private int[] BackupAttvalue;
    private String BackupTxt;

    public int getBackupID() {
        return this.BackupID;
    }

    public void setBackupID(int BackupID) {
        this.BackupID = BackupID;
    }

    public int[] getBackupAtt() {
        return this.BackupAtt;
    }

    public void setBackupAtt(int[] BackupAtt) {
        this.BackupAtt = BackupAtt;
    }

    public int[] getBackupAttvalue() {
        return this.BackupAttvalue;
    }

    public void setBackupAttvalue(int[] BackupAttvalue) {
        this.BackupAttvalue = BackupAttvalue;
    }

    public String getBackupTxt() {
        return this.BackupTxt;
    }

    public void setBackupTxt(String BackupTxt) {
        this.BackupTxt = BackupTxt;
    }


}
