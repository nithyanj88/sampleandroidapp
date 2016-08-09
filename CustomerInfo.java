package com.omt.quikformz.model;

import android.content.Context;

import com.activeandroid.ActiveRecordBase;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.JsonParser;
import com.omt.quikformz.logic.AssignmentInfo;
import com.omt.quikformz.util.GsonUtil;

@Table(name = "CustomerInfo")
public class CustomerInfo extends ActiveRecordBase<CustomerInfo> implements JsonDomainConverter {

    public CustomerInfo(Context context) {
        super(context);
    }
    
    @Column(name = "CUSTOMER_ID")
    public String CUSTOMER_ID;
   
    @Column(name = "CUSTOMER_NAME")
    public String CUSTOMER_NAME;
            
    @Column(name = "ADDRESS_1")
    public String ADDRESS_1;
    
    @Column(name = "ADDRESS_2")
    public String ADDRESS_2;
    
    @Column(name = "CITY")
    public String CITY;
    
    @Column(name = "STATE")
    public String STATE;
    
    @Column(name = "ZIP")
    public String ZIP;
    
    @Column(name = "PHONE")
    public String PHONE;
    
    @Column(name = "MOBILE")
    public String MOBILE;
    
    @Column(name = "EMAIL_ID")
    public String EMAIL_ID;
    
    @Column(name = "LAT")
    public String LAT;
    
    @Column(name = "LONG")
    public String LONG;
    
    @Column(name = "PAN")
    public String PAN;
    
    @Column(name = "TIN")
    public String TIN;
    
    @Column(name = "DOB")
    public String DOB;
    
    @Column(name = "DOA")
    public String DOA;
    
    @Column(name = "uniqueId")
    public String uniqueId;
    
    @Column(name = "CREATED")
    public String CREATED;
    
    @Column(name = "ROUTEID")
    public String ROUTEID;
    
    @Override
	public void saveInfo(Context context, String customerJSON) {
		try{
		JsonParser jsonParser = new JsonParser();

		CustomerInfo[] info = GsonUtil.instance(context).getArrayData(customerJSON, jsonParser, CustomerInfo[].class, "subcustomer");
		 for (CustomerInfo customerInfo : info) {
			// assignStatus = 0;
			 customerInfo.save();
			 
		 }
		}catch (Exception e) {
			// TODO: handle exception
		}
	}

}
