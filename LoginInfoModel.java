package com.omt.quikformz.model;

import static com.omt.quikformz.util.StringUtil.isNotNullOrEmpty;

import com.google.gson.Gson;

public class LoginInfoModel {

    public String phoneModel = "Nokia6131NFC/05.10";
    public String appVersion = "1.0.0";
	public String callType = "get";
    public String imei = "";
    public String pId = "1.0";
    public String ts;
    public String passwd;
    public String ptn; //Username
	public String cId; //AccountId
    public String cVer = "2.0.1";
    public String callName = "login";

    public boolean isValid() {
        return (isNotNullOrEmpty(ptn) && isNotNullOrEmpty(passwd) && isNotNullOrEmpty(cId));
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}
