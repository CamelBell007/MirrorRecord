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
import android.widget.ImageView;
import android.widget.Toast;

import com.camelbell.jobrecord.R;
import com.camelbell.jobrecord.application.MyApplication;
import com.camelbell.jobrecord.utils.CamelBellUtils;
/**
 * 设置用户信息
 * @author Administrator
 *
 */
public class SetUserActivity extends Activity
{
	private EditText vUserName;
	private EditText vUserPasswd;
	private Button vChangePasswordButton;
	private Button vAddNewUserButton;
	private ImageView vBackImage;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_set_user);
		MetricsUtil.getCurrentWindowMetrics(SetUserActivity.this);
		vUserName = (EditText) findViewById(R.id.set_user_name_edit);
		vUserPasswd = (EditText) findViewById(R.id.set_user_passwd_edit);
		vChangePasswordButton = (Button) findViewById(R.id.set_user_change_passwd_button);
		vAddNewUserButton = (Button) findViewById(R.id.set_user_add_new_button);
		vBackImage = (ImageView)findViewById(R.id.set_user_back_image);
		vChangePasswordButton.setOnClickListener(new OnClickListener() {

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
				judgeChangePasswd(userName,userPasswd);
			}
		});
		vAddNewUserButton.setOnClickListener(new OnClickListener() {

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
				judgeAddUser(userName,userPasswd);
			}
		});
		vBackImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SetUserActivity.this.finish();
			}
		});

	}
	/**
	 * 判断用户是否存在，或者用户名密码是否正确
	 * @return
	 */
	public void judgeChangePasswd(String userName,String userPasswd){
		SharedPreferences userShare = getSharedPreferences("userShare", MODE_PRIVATE);
		String userPassword = userShare.getString(userName, "null");
		if(userPassword.equals("null")){
			if(userName.equals("admin")){
				Editor editor = userShare.edit();
				editor.putString(userName, userPasswd);
				editor.commit();
				Toast.makeText(getApplicationContext(), R.string.change_user_passwd_successed, Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(getApplicationContext(), R.string.user_auther_not_exist, Toast.LENGTH_SHORT).show();
			}
		}else{
			Editor editor = userShare.edit();
			editor.putString(userName, userPasswd);
			editor.commit();
			Toast.makeText(getApplicationContext(), R.string.change_user_passwd_successed, Toast.LENGTH_SHORT).show();
		}
	}
	/**
	 * 判断用户是否存在，或者用户名密码是否正确
	 * @return
	 */
	public void judgeAddUser(String userName,String userPasswd){
		SharedPreferences userShare = getSharedPreferences("userShare", MODE_PRIVATE);
		String userPassword = userShare.getString(userName, "null");
		if(userPassword.equals("null")){
			Editor editor = userShare.edit();
			editor.putString(userName, userPasswd);
			editor.commit();
			Toast.makeText(getApplicationContext(), R.string.add_new_user_successed, Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(getApplicationContext(), R.string.user_has_exist, Toast.LENGTH_SHORT).show();
		}
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
		SetUserActivity.this.finish();
	}

}
