package com.omt.quikformz.model;

import java.util.ArrayList;

import android.content.Context;

import com.activeandroid.ActiveRecordBase;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "CredentialInfo")
public class CredentialInfo extends ActiveRecordBase<CredentialInfo> {

    public CredentialInfo(Context context) {
        super(context);
    }

    @Column(name = "cId")
    public String cId; //AccountId

    @Column(name = "ptn")
    public String ptn; //Username

    @Column(name = "passwd")
    public String passwd;

    public static void clearCredentialInfo(Context context){
    	try{
        ArrayList<CredentialInfo> list = CredentialInfo.query(context.getApplicationContext(), CredentialInfo.class, null, null, null);
        for (CredentialInfo info : list) {
            info.delete();
        }
    	}catch (Exception e) {
			// TODO: handle exception
		}
    }

    public static void saveCredentialInfo(Context context,LoginInfoModel loginInfoModel) {
    	try{
        clearCredentialInfo(context);
        CredentialInfo credentialInfo = new CredentialInfo(context);
        credentialInfo.clearCredentialInfo(context);
        credentialInfo.cId = loginInfoModel.cId;
        credentialInfo.ptn = loginInfoModel.ptn;
        credentialInfo.passwd = loginInfoModel.passwd;
        credentialInfo.save();
    	}catch (Exception e) {
			// TODO: handle exception
		}
    }
    
    public static synchronized CredentialInfo queryCredentialSingle(Context context,
            java.lang.Class<? extends ActiveRecordBase<?>> type,
            java.lang.String[] columns)  {
            
            return CredentialInfo.querySingle(context, type, columns);
    }

}