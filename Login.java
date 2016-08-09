package com.omt.quikformz.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.omt.quikformz.R;
import com.omt.quikformz.model.CredentialInfo;

public class Login extends Activity implements LoginPresenter.IDisplay {
	ImageView scheduleIcon;
	Bundle extra;
	Context context;

	public static boolean isLoggedIn = true;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		CredentialInfo credentialInfo = CredentialInfo.queryCredentialSingle(getApplicationContext(), CredentialInfo.class, null);
		if(credentialInfo == null){
			extra = getIntent().getExtras();
			setContentView(R.layout.login);
			getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.window_title);
			scheduleIcon  = (ImageView) findViewById(R.id.quickformzIcon);
			scheduleIcon.setImageResource(R.drawable.title_logo);
			new LoginPresenter(this, this);
			if(extra != null){
				Toast.makeText(getApplicationContext(), "You have logged Out successfully, Thank You", Toast.LENGTH_LONG).show();
				finish();
				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(0);
			}
		}else{
			isLoggedIn = true;
			startActivity(new Intent(Login.this, ChangedDashboard.class));
		} 

	}

	public void addOnClickListener(View.OnClickListener listener) {
		((Button) findViewById(R.id.login)).setOnClickListener(listener);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if (keyCode == KeyEvent.KEYCODE_BACK){
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public String getPassword() {
		return getTextData(R.id.pass);
	}

	public String getPtn() {
		return getTextData(R.id.uName);
	}

	public String getCustomerId() {
		return getTextData(R.id.accountId);
	}

	private String getTextData(int editTextElementId) 
	{
		return ((EditText) findViewById(editTextElementId)).getText().toString();
	}

	public void startNewActivity(Intent intent) 
	{
		startActivity(intent);
	}
}