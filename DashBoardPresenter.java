package com.omt.quikformz.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.TabHost;

import com.omt.quikformz.R;
import com.omt.quikformz.util.SettingsDetails;

public class DashBoardPresenter {

    private Context context;
    private TabHost tabHost;

    public DashBoardPresenter(Context context, TabHost tabHost) {
        this.context = context;
        this.tabHost = tabHost;
        presentDashBoard();
    }
    
  /*  public DashBoardPresenter(Context context) {
        this.context = context;
     //   this.tabHost = tabHost;
        presentDashBoard();
    }*/

    private void presentDashBoard() {
    	try{
        tabHost.addTab(tabHost.newTabSpec("FormGroup").setIndicator("FormGroup",context.getResources().getDrawable(R.drawable.tabitemicon)).setContent(new Intent(context, MainwithIcons.class)));
        if(SettingsDetails.ASSIGNMENT_ENABLED.equals("1")){
        	 tabHost.addTab(tabHost.newTabSpec("Reminder").setIndicator("Reminder",context.getResources().getDrawable(R.drawable.tableiconforassignment)).setContent(new Intent(context, Assignment.class)));
        }
        tabHost.addTab(tabHost.newTabSpec("My Reports").setIndicator("My Reports",context.getResources().getDrawable(R.drawable.tabiconformyreports)).setContent(new Intent(context, MyReport.class)));
        tabHost.setCurrentTab(0);
    	}catch (Exception e) {
			// TODO: handle exception
		}
    }
}
