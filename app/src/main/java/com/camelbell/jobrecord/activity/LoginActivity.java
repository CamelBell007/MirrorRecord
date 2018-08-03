package com.camelbell.jobrecord.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.camelbell.jobrecord.R;
import com.camelbell.jobrecord.application.MyApplication;
import com.camelbell.jobrecord.utils.CamelBellUtils;

public class LoginActivity extends Activity
{
	private EditText vUserName;
	private EditText vUserPasswd;
	private Button vLoginButton;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		MetricsUtil.getCurrentWindowMetrics(LoginActivity.this);
		vUserName = (EditText) findViewById(R.id.user_name_edit);
		vUserPasswd = (EditText) findViewById(R.id.user_passwd_edit);
		vLoginButton = (Button) findViewById(R.id.user_login_button);
		
		vLoginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String userName = vUserName.getText().toString().trim();
				String userPasswd = vUserPasswd.getText().toString().trim();

				if(!CamelBellUtils.isNotEmpty(userName)){
					Toast.makeText(getApplicationContext(), R.string.please_input_user_name, Toast.LENGTH_SHORT).show();
					return;
				}
				if(!CamelBellUtils.isNotEmpty(userPasswd)){
					Toast.makeText(getApplicationContext(), R.string.please_input_user_passwd, Toast.LENGTH_SHORT).show();
					return;
				}
				judgeUserIsEist(userName,userPasswd);
			}
		});
		


	}
	@Override
	protected void onResume() {
		super.onResume();
		
	}
	/**
	 * 判断用户是否存在，或者用户名密码是否正确
	 * @return
	 */
	public void judgeUserIsEist(String userName,String userPasswd){
		SharedPreferences userShare = getSharedPreferences("userShare", MODE_PRIVATE);
		String userPassword = userShare.getString(userName, "null");
//		if(userPassword.equals("null")){
//			Editor editor = userShare.edit();
//			editor.putString(userName, userPasswd);
//			editor.commit();
//			startActivityToNext();
//			setUserName(userName);
//		}else{
		if(userPassword.equals("null")&&"admin".equals(userName)&&userPasswd.equals("123456")){
			setUserName("admin");
			startActivityToNext();
		}else{
			if(userPasswd.equals(userPassword)){
				setUserName(userName);
				startActivityToNext();
			}else{
				Toast.makeText(getApplicationContext(), R.string.user_auther_error, Toast.LENGTH_SHORT).show();
			}
		}
			
//		}
	}
	
	public void setUserName(String userName){
		SharedPreferences userShare = getSharedPreferences("userShare", MODE_PRIVATE);
			Editor editor = userShare.edit();
			editor.putString("userName", userName);
			editor.commit();
	}
	
	/**
	 * 登陆成功后的操作
	 */
	public void startActivityToNext(){
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), RecordActivity.class);
		startActivity(intent);
		MyApplication.userId = vUserName.getText().toString().trim();
		LoginActivity.this.finish();
	}

}
