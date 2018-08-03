package com.camelbell.jobrecord.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

import com.artifex.mupdfdemo.AsyncTask;
import com.artifex.mupdfdemo.MuPDFAlert;
import com.artifex.mupdfdemo.MuPDFCore;
import com.artifex.mupdfdemo.MuPDFPageAdapter;
import com.artifex.mupdfdemo.MuPDFReaderView;
import com.artifex.mupdfdemo.OutlineActivityData;
import com.artifex.mupdfdemo.SearchTaskResult;
import com.camelbell.jobrecord.R;

public class ShowAllScreenActivity extends Activity {

	private MuPDFCore    core;
	private String       mFileName;
	private MuPDFReaderView mDocView;
	private RelativeLayout     mButtonsView;
	private AlertDialog.Builder mAlertBuilder;
	private AsyncTask<Void,Void,MuPDFAlert> mAlertTask;
	private String pdfPath;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		pdfPath = getIntent().getStringExtra("Pdf_Path");
		setContentView(R.layout.show_pdf);
		mButtonsView = (RelativeLayout)findViewById(R.id.document_view);
		initPDF(pdfPath, mButtonsView);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		if (mFileName != null && mDocView != null) {
			outState.putString("FileName", mFileName);

			// Store current page in the prefs against the file name,
			// so that we can pick it up each time the file is loaded
			// Other info is needed only for screen-orientation change,
			// so it can go in the bundle
			SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
			SharedPreferences.Editor edit = prefs.edit();
			edit.putInt("page"+mFileName, mDocView.getDisplayedViewIndex());
			edit.commit();
		}

	}

	@Override
	protected void onPause() {
		super.onPause();

		if (mFileName != null && mDocView != null) {
			SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
			SharedPreferences.Editor edit = prefs.edit();
			edit.putInt("page"+mFileName, mDocView.getDisplayedViewIndex());
			edit.commit();
		}
	}

	@Override
	public void onDestroy()
	{
		if (core != null)
			core.onDestroy();
		if (mAlertTask != null) {
			mAlertTask.cancel(true);
			mAlertTask = null;
		}
		core = null;
		super.onDestroy();
	}


	//	@Override
	@Override
	protected void onStart() {
		if (core != null)
		{
			core.startAlerts();
		}
		super.onStart();
	}

	@Override
	protected void onStop() {
		if (core != null)
		{
			core.stopAlerts();
		}
		super.onStop();
	}

	/**
	 * ��path·���µ�pdf(���������ʽ���ļ�)
	 * @param path
	 * @return
	 */
	private MuPDFCore openFile(String path)
	{
		int lastSlashPos = path.lastIndexOf('/');
		mFileName = new String(lastSlashPos == -1
				? path
						: path.substring(lastSlashPos+1));
		System.out.println("Trying to open "+path);
		try
		{
			core = new MuPDFCore(this, path);
			// New file: drop the old outline data
			OutlineActivityData.set(null);
		}
		catch (Exception e)
		{
			System.out.println(e);
			return null;
		}
		return core;
	}
	
	/**
	 * 添加PDF文件
	 * @param pdfPath	
	 * @param parenetView
	 */
	private void initPDF(String pdfPath,RelativeLayout parenetView){

		mAlertBuilder = new AlertDialog.Builder(this);

		if (core == null) {
			core = (MuPDFCore)getLastNonConfigurationInstance();
		}
		if (core == null) {
			core = openFile(pdfPath);
			SearchTaskResult.set(null);
			if (core != null && core.countPages() == 0)
			{
				core = null;
			}
		}
		if (core == null)
		{
			AlertDialog alert = mAlertBuilder.create();
			alert.setTitle(R.string.cannot_open_document);
			alert.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.dismiss),
					new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
			alert.show();
			return;
		}
		
		mDocView = new MuPDFReaderView(this);
		mDocView.setAdapter(new MuPDFPageAdapter(this, core));
		SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
		mDocView.setDisplayedViewIndex(prefs.getInt("page"+mFileName, 0));
		RelativeLayout.LayoutParams linearParams = new RelativeLayout.
				LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT); //取控件textView当前的布局参数  
		mDocView.setLayoutParams(linearParams);
		parenetView.removeAllViews();
		parenetView.addView(mDocView);
	}
}
