package com.camelbell.jobrecord.activity;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.camelbell.jobrecord.R;

import com.camelbell.jobrecord.utils.MetricsUtil;
import cn.yohoutils.Model.VersionBaseInfo;

/**
 * 所有Activity中的actionBar
 * 
 * @author sunyan
 * 
 */

public abstract class BaseActionBarActivity extends FragmentActivity implements
		OnClickListener {
	public boolean isOk = true;
	private ActionBar actionBar;
	protected RelativeLayout vActionBarLayout;
	protected ImageView vLeftOneImage;// 左一键（返回）
	protected ImageView vLeftTwoImage;// 左二键（一二级菜单的logo）
	protected ImageView vRightOneImage;// 右一键（分享；删除；下载；）
	protected ImageView vRightTwoImage;// 右二键(收藏)
	protected ImageView vCommentImage;// 评论
	protected TextView tv_comment;
	protected RelativeLayout vCommentLayout;// 评论区
	protected TextView vLeftTitleText;// 左标题
	protected TextView vMidTitleText;// 中标题
	protected RelativeLayout customView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.MyActionBar);
		getActionBar().setBackgroundDrawable(new ColorDrawable());
		super.onCreate(savedInstanceState);
		initActionBar();

		initViewParamsAndMargin();
	}

	/**
	 * 设置图片所占空间的大小
	 */
	private void initViewParamsAndMargin() {
		setViewParams(
				vLeftOneImage,
				getResources().getDimensionPixelSize(
						R.dimen.activity_content_icon_normal_size),
				getResources().getDimensionPixelSize(
						R.dimen.activity_content_icon_normal_size));
		setViewParams(
				vRightOneImage,
				getResources().getDimensionPixelSize(
						R.dimen.activity_content_icon_normal_size),
				getResources().getDimensionPixelSize(
						R.dimen.activity_content_icon_normal_size));
//		setViewParams(
//				vLeftTwoImage,
//				getResources().getDimensionPixelSize(
//						R.dimen.activity_content_icon_normal_size),
//				getResources().getDimensionPixelSize(
//						R.dimen.activity_content_icon_normal_size));
		setViewParams(
				vRightTwoImage,
				getResources().getDimensionPixelSize(
						R.dimen.activity_content_icon_normal_size),
				getResources().getDimensionPixelSize(
						R.dimen.activity_content_icon_normal_size));
//		setViewParams(
//				vCommentImage,
//				getResources().getDimensionPixelSize(
//						R.dimen.activity_content_icon_normal_size),
//				getResources().getDimensionPixelSize(
//						R.dimen.activity_content_icon_normal_size));

		setTitleParamsAndParams();
//		setLeftTwoImageParams();
//		setCommentTextMargin();
	}
	
	private void MetricsComment(){
		if(MetricsUtil.CURRENT_SCREEN_WIDTH == 1200.0 && MetricsUtil.CURRENT_SCREEN_HEIGHT == 1824.0){
			setViewParams(
					vCommentImage,
					156,156);
		}else if(MetricsUtil.CURRENT_SCREEN_WIDTH == 1080.0 && MetricsUtil.CURRENT_SCREEN_HEIGHT == 1776.0){
			setViewParams(
					vCommentImage,
					156,148);
		}else{
			setViewParams(
					vCommentImage,
					getResources().getDimensionPixelSize(
							R.dimen.activity_content_love_width),
							getResources().getDimensionPixelSize(
									R.dimen.activity_content_love_height));
		}
	}

//	/**
//	 * 设置评论区域评论个数的外边距
//	 */
//	private void setCommentTextMargin() {
//		MetricsUtil
//				.setMargins(
//						vBadgeText,
//						(int) (MetricsUtil
//								.getCurrentWidthSize(getResources()
//										.getDimensionPixelSize(
//												R.dimen.activity_content_icon_normal_size) / 2)
//								* MetricsUtil.CURRENT_DENSITY / MetricsUtil.STANDARD_DENSITY),
//						(int) (MetricsUtil
//								.getCurrentWidthSize(getResources()
//										.getDimensionPixelSize(
//												R.dimen.activity_content_icon_normal_size) / 2)
//								* MetricsUtil.CURRENT_DENSITY / MetricsUtil.STANDARD_DENSITY),
//						0, 0);
//	}

	/**
	 * 设置左二图片Parmas
	 */
	private void setLeftTwoImageParams() {

		ViewGroup.LayoutParams name_layoutParams = vLeftTwoImage
				.getLayoutParams();

		name_layoutParams.width = (int) (MetricsUtil.getCurrentWidthSize(400)
				* MetricsUtil.CURRENT_DENSITY / MetricsUtil.STANDARD_DENSITY);

		name_layoutParams.height = (int) (MetricsUtil.getCurrentHeightSize(160)
				* MetricsUtil.CURRENT_DENSITY / MetricsUtil.STANDARD_DENSITY);

		vLeftTwoImage.setLayoutParams(name_layoutParams);
	}

	/**
	 * 设置标题的params
	 */
	private void setTitleParamsAndParams() {

		ViewGroup.LayoutParams layoutNameParams = vLeftTitleText
				.getLayoutParams();

		layoutNameParams.width = (int) (MetricsUtil
				.getCurrentWidthSize(getResources()
						.getDimensionPixelSize(
								R.dimen.activity_content_textview_contentname_width_size))
				* MetricsUtil.CURRENT_DENSITY / MetricsUtil.STANDARD_DENSITY);
		vLeftTitleText.setLayoutParams(layoutNameParams);
//		vLeftTitleText.setTextColor(getResources().getColor(R.color.black));
		vLeftTitleText.setTextSize(MetricsUtil
				.getCurrentTextSize(getResources().getDimensionPixelSize(
						R.dimen.activity_content_textview_love_textsize)));
	}

	/**
	 * 设置普通控件所占空间的大小
	 * 
	 * @param view
	 */
	protected void setViewParams(View view, int width, int height) {
		ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
		layoutParams.width = (int) (MetricsUtil.getCurrentWidthSize(width)
				* MetricsUtil.CURRENT_DENSITY / MetricsUtil.STANDARD_DENSITY);

		layoutParams.height = (int) (MetricsUtil.getCurrentHeightSize(height)
				* MetricsUtil.CURRENT_DENSITY / MetricsUtil.STANDARD_DENSITY);

		view.setLayoutParams(layoutParams);
	}

	/**
	 * 初始化ActionBar控件
	 */
	private void initActionBar() {
		MetricsUtil.getCurrentWindowMetrics(this);

		actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(false);

		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		customView = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.actionbar_view, null);
		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.ECLAIR_MR1) {
			customView.findViewById(R.id.shadow_line).setVisibility(
					View.VISIBLE);
		} else {
			customView.findViewById(R.id.shadow_line).setVisibility(View.GONE);
		}
		actionBar.setCustomView(customView);
//		setLayoutParams(customView, 2000, 150);
		// customView.setLayoutParams(new LayoutParams(
		// RelativeLayout.LayoutParams.MATCH_PARENT,
		// RelativeLayout.LayoutParams.WRAP_CONTENT));
//		vActionBarLayout = (RelativeLayout)customView
//				.findViewById(R.id.rlyTop);
//		setLayoutParams(vActionBarLayout, 2000, 200);
		vLeftOneImage = (ImageView) customView
				.findViewById(R.id.actionbar_left_one_image);

		vLeftTwoImage = (ImageView) customView
				.findViewById(R.id.actionbar_left_two_image);

		vRightOneImage = (ImageView) customView
				.findViewById(R.id.actionbar_right_one_image);

		vRightTwoImage = (ImageView) customView
				.findViewById(R.id.actionbar_right_two_image);

		vCommentImage = (ImageView) customView
				.findViewById(R.id.iv_comment);

		vCommentLayout = (RelativeLayout) customView
				.findViewById(R.id.rl_comment);
		tv_comment = (TextView) customView
				.findViewById(R.id.tv_comment);
		vLeftTitleText = (TextView) customView
				.findViewById(R.id.actionbar_left_title_text);

		vMidTitleText = (TextView) customView
				.findViewById(R.id.actionbar_mid_title_text);

		vLeftTitleText
				.setTextSize(MetricsUtil
						.getCurrentTextSize(getResources()
								.getDimensionPixelSize(
										R.dimen.activity_content_textview_contentname_textsize)));
		tv_comment.setTextSize(MetricsUtil.getCurrentTextSize(
				getResources().getDimensionPixelSize(
						R.dimen.activity_content_textview_love_textsize)));
		MetricsUtil
		.setMargins(
				tv_comment,
				(int) (getResources().getDimensionPixelSize(
						R.dimen.activity_content_love_text_margin_left)
						* MetricsUtil.CURRENT_DENSITY / MetricsUtil.STANDARD_DENSITY),
						0,
						0,
						(int) (getResources()
								.getDimensionPixelSize(
										R.dimen.activity_content_love_text_margin_bottom)
										* MetricsUtil.CURRENT_DENSITY / MetricsUtil.STANDARD_DENSITY));
		MetricsUtil.setMargins(vLeftTitleText, 80, 0, 0, 0);
		setLayoutWidthParams(tv_comment, 70);
		MetricsComment();
		vLeftTitleText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}
		});
		vLeftOneImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}
		});
	}
	private void setLayoutWidthParams(View view, int width) {
		ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
		layoutParams.width = (int) (MetricsUtil.getCurrentHeightSize(width)
				* MetricsUtil.CURRENT_DENSITY / MetricsUtil.STANDARD_DENSITY);
		view.setLayoutParams(layoutParams);
	}
	
	public void setLayoutHightParams(View view, int hight) {
		ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
		layoutParams.height = (int) (MetricsUtil.getCurrentHeightSize(hight)
				* MetricsUtil.CURRENT_DENSITY / MetricsUtil.STANDARD_DENSITY);
		view.setLayoutParams(layoutParams);
	}
	/**
	 * 设置评论按钮的响应事件
	 * 
	 * @param resoure
	 * @param onClickListener
	 */
	public void setLeftImageOnClickeListener(OnClickListener onClickListener) {

		vLeftOneImage.setOnClickListener(onClickListener);
	}

	/**
	 * 设置评论按钮的响应事件
	 * 
	 * @param resoure
	 * @param onClickListener
	 */
	public void setLeftTitleOnClickeListener(OnClickListener onClickListener) {

		vLeftTitleText.setOnClickListener(onClickListener);
	}

	/**
	 * 设置控件长宽自适应分辨率
	 * 
	 * @param view
	 *            控件
	 * @param width
	 *            宽
	 * @param height
	 *            高
	 */
	private void setLayoutParams(View view, int width, int height) {
		ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
		layoutParams.width = (int) (MetricsUtil.getCurrentWidthSize(width)
				* MetricsUtil.CURRENT_DENSITY / MetricsUtil.STANDARD_DENSITY);
		layoutParams.height = (int) (MetricsUtil.getCurrentHeightSize(height)
				* MetricsUtil.CURRENT_DENSITY / MetricsUtil.STANDARD_DENSITY);
		view.setLayoutParams(layoutParams);
	}

	/**
	 * 设置左二按钮的背景图片和响应事件
	 * 
	 * @param resoure
	 * @param onClickListener
	 */
	public void setLeftTwoImageButton(int resoure,
			OnClickListener onClickListener) {
		vLeftTwoImage.setVisibility(View.VISIBLE);
		if (onClickListener != null) {
			vLeftTwoImage.setOnClickListener(onClickListener);
		}
		vLeftTwoImage.setImageResource(resoure);
	}

	/**
	 * 设置右二按钮的背景图片和响应事件
	 * 
	 * @param resoure
	 * @param onClickListener
	 */
	public void setRightTwoImageButton(int resoure,
			OnClickListener onClickListener) {
		vRightTwoImage.setVisibility(View.VISIBLE);
		vRightTwoImage.setOnClickListener(onClickListener);
		vRightTwoImage.setBackgroundResource(resoure);
	}

	/**
	 * 设置右一按钮的背景图片和响应事件
	 * 
	 * @param resoure
	 * @param onClickListener
	 */
	public void setRighOneImageButton(int resoure,
			OnClickListener onClickListener) {
		vRightOneImage.setVisibility(View.VISIBLE);
		vRightOneImage.setOnClickListener(onClickListener);
		vRightOneImage.setBackgroundResource(resoure);
	}

	/**
	 * 设置评论按钮的响应事件
	 * 
	 * @param resoure
	 * @param onClickListener
	 */
	public void setCommentLayoutOnClickeListener(OnClickListener onClickListener) {
		vCommentLayout.setVisibility(View.VISIBLE);
		vCommentLayout.setOnClickListener(onClickListener);
	}

	/**
	 * 设置评论按钮的响应事件
	 * 
	 * @param resoure
	 * @param onClickListener
	 */
	public void setCommentImageOnClickeListener(OnClickListener onClickListener) {
		vCommentLayout.setVisibility(View.VISIBLE);
		vCommentImage.setOnClickListener(onClickListener);
	}

	/**
	 * 设置左边标题文字
	 * 
	 * @param title
	 */
	public void setLeftTitleText(String title) {
		vLeftTitleText.setVisibility(View.VISIBLE);
		vLeftTitleText.setText(title);
		vLeftTitleText.setBackgroundDrawable(null);
		vLeftTwoImage.setVisibility(View.GONE);
	}
	
	/**
	 * 设置左边标题文字
	 * 
	 * @param title
	 */
	public void setLeftTitleText(String title,Typeface din_regular) {
		vLeftTitleText.setTypeface(din_regular);
		vLeftTitleText.setVisibility(View.VISIBLE);
		vLeftTitleText.setText(title);
		vLeftTitleText.setBackgroundDrawable(null);
		vLeftTwoImage.setVisibility(View.GONE);
	}
	
	/**
	 * 设置左边标题字体
	 * 
	 * @param face
	 */
	public void setLeftTwoImage(int resId){
//		vLeftTwoImage.setText("");
		vLeftTwoImage.setVisibility(View.VISIBLE);
		vLeftTwoImage.setImageResource(resId);
		vLeftTitleText.setVisibility(View.GONE);

	}
	
	public void hideLeftTwoImage(){
		vLeftTwoImage.setVisibility(View.GONE);
		vLeftTitleText.setVisibility(View.GONE);
	}
	

	public void setLeftTitleText(int title) {
		vLeftTitleText.setVisibility(View.VISIBLE);
		vLeftTitleText.setText(getResources().getString(title));
	}
	
	/**
	 * 设置左边标题文字字体
	 */
	public void setLeftTitleFont(Typeface din_regular){
		vLeftTitleText.setTypeface(din_regular);
	}

	/**
	 * 设置中间标题文字
	 * 
	 * @param title
	 *            //
	 */
	public void setMidTitleText(String title) {
		vMidTitleText.setVisibility(View.VISIBLE);
		vMidTitleText.setText(title);
	}

	/**
	 * 设置评论的字数
	 * 
	 * @param commentNum
	 */
	public void setCommentText(String commentNum) {
		if (vCommentLayout.getVisibility() != View.VISIBLE) {
			vCommentLayout.setVisibility(View.VISIBLE);
		}
		tv_comment.setText(commentNum);
	}


	@SuppressWarnings("unchecked")
	protected final <T extends View> T findView(int id) {
		return (T) super.findViewById(id);
	}

	/**
	 * 检查SD卡是否接入
	 * 
	 * @return
	 */
	protected boolean yohoCheckSDCard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}


	/**
	 * 判断当前网络是否已经连接
	 * 
	 * @return
	 */
	protected boolean yohoIsNetworkAvailable() {
		ConnectivityManager cwjManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cwjManager != null) {
			// 获取网络连接管理的对象
			NetworkInfo info = cwjManager.getActiveNetworkInfo();
			if (info != null && info.isConnected() && info.isAvailable()) {
				// 判断当前网络是否已经连接
				if (info.getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}


	/**
	 * 获取当前版本信息
	 */
	protected VersionBaseInfo yohoGetCurrentVersion() {
		VersionBaseInfo verinfo = new VersionBaseInfo();
		PackageManager manager = getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
			if (info != null) {
				verinfo.mVerCode = info.versionCode;
				verinfo.mVerName = info.versionName;
			}
		} catch (NameNotFoundException e) {
			return null;
		}
		return verinfo;
	}

	/**
	 * 以SharedPreferences的方式存数据
	 * 
	 * @param context
	 * @param fileName
	 *            ：
	 * @param key
	 * @param value
	 */
	protected void saveIntDataBySharedPreferences(String fileName, String key,
			int value) {
		if (fileName == null || key == null)
			return;
		if (fileName.equals("") || key.equals(""))
			return;

		SharedPreferences sp = getSharedPreferences(fileName, 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	protected void saveStringDataBySharedPreferences(String fileName,
			String key, String value) {
		if (fileName == null || key == null || value == null)
			return;
		if (fileName.equals("") || key.equals("") || value.equals(""))
			return;

		SharedPreferences sp = getSharedPreferences(fileName, 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * 从SharedPreferences中取响应的数据数据
	 * 
	 * @param context
	 * @param fileName
	 * @param key
	 * @return
	 */
	protected int getIntDataFromSharedPreferences(String fileName, String key) {
		if (fileName == null || key == null)
			return -1;
		if (fileName.equals("") || key.equals(""))
			return -1;

		SharedPreferences sp = getSharedPreferences(fileName, 0);
		int value = sp.getInt(key, 0);
		return value;
	}

	protected String getStringDataFromSharedPreferences(String fileName,
			String key) {
		if (fileName == null || key == null)
			return null;
		if (fileName.equals("") || key.equals(""))
			return null;
		SharedPreferences sp = getSharedPreferences(fileName, 0);
		String value = sp.getString(key, null);
		return value;
	}

	public void noAnimFinish() {
		super.finish();
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	protected void showLoadingDialog() {

	}

	protected void dismissLoadingDialog() {

	}

}