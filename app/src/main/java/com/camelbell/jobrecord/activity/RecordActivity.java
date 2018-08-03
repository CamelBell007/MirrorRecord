package com.camelbell.jobrecord.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.artifex.mupdfdemo.AsyncTask;
import com.artifex.mupdfdemo.MuPDFAlert;
import com.artifex.mupdfdemo.MuPDFCore;
import com.artifex.mupdfdemo.MuPDFPageAdapter;
import com.artifex.mupdfdemo.MuPDFReaderView;
import com.artifex.mupdfdemo.OutlineActivityData;
import com.artifex.mupdfdemo.SearchTaskResult;
import com.camelbell.jobrecord.R;
import com.camelbell.jobrecord.application.MyApplication;
import com.camelbell.jobrecord.bean.WpsModel;
import com.camelbell.jobrecord.bean.XlsBean;
import com.camelbell.jobrecord.utils.ActivityStackManager;
import com.camelbell.jobrecord.utils.CamelBellUtils;
import com.camelbell.jobrecord.utils.ConstantDatas;
import com.camelbell.jobrecord.utils.FileUtil;
import com.camelbell.jobrecord.utils.TimeUtil;
import com.camelbell.jobrecord.utils.UIUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cn.yohoutils.StringUtil;
import jxl.Sheet;
import jxl.Workbook;

/**
 * 
 * 事件记录
 * @author gaojianming
 * @date 2015年3月26日 下午5:28:24
 */
public class RecordActivity extends Activity
{
	private static final String DOCUMENT_VIEW_STATE_PREFERENCES = "DjvuDocumentViewState";
	
	private DatabaseHelper database;
//	private TextView vHeGeHao;
//	private TextView vGuiGeHao;
//	private TextView vLuPiHao;
	
//	private TextView vCaiZhiHao;
	private TextView vUserSetText;
	private TextView vJiShi;
	private TextView vMonthTime;
	private TextView vStartTime;
	private TextView vEndTime;
//	private AutoCompleteTextView vQieGeBanHao;
	private EditText vNcText;//NC号码
	private Button startButton;//开工
	private Button pauseButton;//暂停
	private Button continueButton;//继续
	private Button endButton;//完成
	
	private Button vSearchButton;
	private Button vScannerButton;
	private ImageButton vFullScreenButton;
	private ImageView vTopBackImage;
	private ImageView vTopRefreshImage;
	private TextView vUserName;
//	private ImageButton vAddButton;
//	private ImageButton vMinusButton;

	// 第一次点击时间
	private long mFirstTime;
	private String mHeGeHao;
	private String mGuiGeHao;
	private String mLuPiHao;
	private String mCaiZhiHao;
	public long startTime;
	public long endTime;
	public String startTimeString;
	public String endTimeString;
	public String cutPltate ;
	
	//存放文件的文件夹名称
	private static final String STELL_NAME = "steelJob";
	private static final String CUT_CODES_OBJECT = "cutCodes";
	private static final String PDF = "stellPdf";
	private String eFilePath ;
	public  static String stellFile;
	private static String pdflFilePath;
	private static String cutObjectPath;
	private XlsBean xlsBean;
	private ArrayList<XlsBean> xlsBeanList;

	private RelativeLayout vPdfContaierLayout;
	private Toast pageNumberToast;
	private ArrayList<String> cutHistoryCodes ;

	private boolean workStatus;
	private Timer timer;
	private Handler mHandler ;
	
	private int workEnum;//1:开始工作中2：暂停3：继续4：结束
	/**
	 * PDF
	 */
	private MuPDFCore core;
	private String       mFileName;
	private MuPDFReaderView mDocView;
	private RelativeLayout     mButtonsView;
	private AlertDialog.Builder mAlertBuilder;
	private AsyncTask<Void,Void,MuPDFAlert> mAlertTask;
	
	 private Camera camera = null;
	 private Parameters parameters = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_record);
		ActivityStackManager.getActivityManager().addActivity(this);
		initView();
		initData();
		initDataBase();
		setListener();
	}



	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		
		super.onResume();
		try{
			camera = Camera.open();	
			parameters = camera.getParameters();
//			//直接开启
//			camera = Camera.open();	
//							parameters = camera.getParameters();
//							parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);//开启
//							camera.setParameters(parameters);
			//直接关闭
			parameters.setFlashMode(Parameters.FLASH_MODE_OFF);//关闭
							camera.setParameters(parameters);
							camera.release();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		database.getDatabase().close();
	}

	private void initView() {
		vUserSetText = (TextView)findViewById(R.id.top_set_user_text);
//		documentView = (DocumentView)findViewById(R.id.document_view);
		vPdfContaierLayout = (RelativeLayout)findViewById(R.id.document_view);
		vNcText = (EditText) findViewById(R.id.cut_code_text);
//		vHeGeHao = (TextView) findViewById(R.id.qualified_code_text);
//		vGuiGeHao = (TextView) findViewById(R.id.version_number_text);
//		vLuPiHao = (TextView) findViewById(R.id.stove_code_text);
//		vCaiZhiHao = (TextView) findViewById(R.id.material_quality_text);
		vJiShi = (TextView) findViewById(R.id.statistics_time_text);
		vMonthTime = (TextView) findViewById(R.id.current_month_time_text);
		vStartTime = (TextView) findViewById(R.id.start_job_time_text);
		vEndTime = (TextView) findViewById(R.id.end_job_time_text);
		startButton = (Button) findViewById(R.id.start_job_button);
		pauseButton = (Button) findViewById(R.id.pause_job_button);
		continueButton = (Button) findViewById(R.id.continue_job_button);
		endButton = (Button) findViewById(R.id.end_job_button);
		vFullScreenButton = (ImageButton) findViewById(R.id.screen_all_button);
//		vAddButton = (ImageButton) findViewById(R.id.screen_add_button);
//		vMinusButton = (ImageButton) findViewById(R.id.screen_minus_button);
		vScannerButton = (Button)findViewById(R.id.scanner_code_button);
		vSearchButton = (Button)findViewById(R.id.search_nc_button);
		vUserName = (TextView)findViewById(R.id.current_user_name_text);
		vTopBackImage = (ImageView)findViewById(R.id.top_back_image);
		vTopRefreshImage = (ImageView)findViewById(R.id.top_refresh_image);
	}
	private void initData() {
		String userName = getUserName();
		vUserName.setText(userName);
		//admin 原始密码为123456
		if(!StringUtil.isEmpty(userName)&&userName.equals("admin")){
			vUserSetText.setVisibility(View.VISIBLE);
		}else{
			vUserSetText.setVisibility(View.GONE);
		}
		workStatus = false;
		eFilePath= CamelBellUtils.getSDPath();
		stellFile = eFilePath+File.separator+STELL_NAME;
		pdflFilePath = stellFile +File.separator+ PDF;
		File pdf = new File(pdflFilePath);
		if(!pdf.exists()){
			Toast.makeText(getApplicationContext(), R.string.please_input_file, Toast.LENGTH_LONG).show();
		}
		try {
			FileUtil.createFolder(stellFile);
			FileUtil.createFolder(pdflFilePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		cutObjectPath = stellFile+File.separator+CUT_CODES_OBJECT;
		// 创建一个ArrayAdapter封装数组
		//		cutHistoryCodes = new ArrayList<String>();
		//		cutHistoryCodes = (ArrayList<String>) FileUtil.getObjectFromFile(cutObjectPath);
		//		if(cutHistoryCodes!=null&&cutHistoryCodes.size()>0){
		//			allHasCutCodes = new ArrayAdapter<String>(this,
		//					android.R.layout.simple_dropdown_item_1line, cutHistoryCodes);
		//		}else{
		//			cutHistoryCodes = new ArrayList<String>();
		//			allHasCutCodes = new ArrayAdapter<String>(this,
		//					android.R.layout.simple_dropdown_item_1line);
		//		}
		//		vQieGeBanHao.setAdapter(allHasCutCodes);

		mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				switch(msg.what){
				case 1:
					endTime = System.currentTimeMillis();
					endTimeString = TimeUtil.formatDetailDate(endTime);

					StringBuilder workTime = new StringBuilder();
					long work = endTime - startTime;
					int hour = (int) (work/1000/60/60);
					int minute = (int) (work%(1000*60*60)/1000/60);
					workTime.append(hour);
					workTime.append("小时");
					workTime.append(minute);
					workTime.append("分钟");
					vJiShi.setText(workTime);
					break;
				}
				super.handleMessage(msg);
			}

		};
	}


	private void setListener() {
		//设置用户的密码
		vUserSetText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(RecordActivity.this, SetUserActivity.class);
				startActivity(intent);
			}
		});
		
		startButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(!workStatus){
					if(CamelBellUtils.isNotEmpty(vNcText.getText().toString()))
					{
						if(CamelBellUtils.isNotEmpty(mGuiGeHao)){
							workEnum = 1;
							setButtonBackColor(startButton);
							workStatus = true;
							vNcText.setEnabled(false);
							startTime = System.currentTimeMillis();
							startTimeString = TimeUtil.formatDetailDate(startTime);
							vStartTime.setText(startTimeString.substring(11,startTimeString.length()));
							vEndTime.setText("");
							vJiShi.setText("");
							timer = new Timer();
							timer.scheduleAtFixedRate(new MyStatisticsTimerTask(), 0, 1000);
						}else{
							Toast.makeText(getApplicationContext(), R.string.please_choose_real_cut_plate, Toast.LENGTH_SHORT).show();
						}
					}else{
						Toast.makeText(getApplicationContext(), R.string.please_choose_cut_plate, Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(getApplicationContext(), R.string.please_close_and_open, Toast.LENGTH_SHORT).show();
				}

			}
		});
		
		pauseButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(workStatus){
					setButtonBackColor(pauseButton);
					workEnum = 2;
					vNcText.setEnabled(false);
					workStatus = false;
					if(CamelBellUtils.isNotEmpty(vStartTime.getText().toString())){
						//释放定时器资源
						timer.cancel();
						timer = null;

						endTime = System.currentTimeMillis();
						endTimeString = TimeUtil.formatDetailDate(endTime);

						StringBuilder workTime = new StringBuilder();
						long work = endTime - startTime;
						int hour = (int) (work/1000/60/60);
						int minute = (int) (work%(1000*60*60)/1000/60);
						int workminute = (int) (work/(1000*60));
						workTime.append(hour);
						workTime.append("小时");
						if((work%(1000*60*60*60)/1000/60/60)==0){
							workTime.append(minute);
						}else{
							workTime.append(minute+1);
						}
						workTime.append("分钟");
						vJiShi.setText(workTime);
						setWorkRecordIntoDataBase(MyApplication.userId, mCaiZhiHao,mGuiGeHao,
								mLuPiHao,vNcText.getText().toString().trim(), mHeGeHao,
								startTimeString, endTimeString, workTime.toString(),workminute);
						copyDataBaseToSD();
						vEndTime.setText(endTimeString.substring(11,endTimeString.length()));

						queryTotalWorkTimeFromTable(MyApplication.userId, startTimeString.substring(0, 7));
					}}else{
						Toast.makeText(getApplicationContext(), R.string.please_frist_work, Toast.LENGTH_SHORT).show();
					}

			}
			
		});
		continueButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!workStatus){
					workEnum = 3;
					if(CamelBellUtils.isNotEmpty(vNcText.getText().toString()))
					{
						if(CamelBellUtils.isNotEmpty(mGuiGeHao)){
							setButtonBackColor(continueButton);
							workStatus = true;
							vNcText.setEnabled(false);
							startTime = System.currentTimeMillis();
							startTimeString = TimeUtil.formatDetailDate(startTime);
							vStartTime.setText(startTimeString.substring(11,startTimeString.length()));
							vEndTime.setText("");
							vJiShi.setText("");
							timer = new Timer();
							timer.scheduleAtFixedRate(new MyStatisticsTimerTask(), 0, 1000);
						}else{
							Toast.makeText(getApplicationContext(), R.string.please_choose_real_cut_plate, Toast.LENGTH_SHORT).show();
						}
					}else{
						Toast.makeText(getApplicationContext(), R.string.please_choose_cut_plate, Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(getApplicationContext(), R.string.please_close_and_open, Toast.LENGTH_SHORT).show();
				}
			}
			
		});
		endButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(workStatus){
					workEnum = 4;
					setButtonBackColor(endButton);
					vNcText.setEnabled(true);
					workStatus = false;
					if(CamelBellUtils.isNotEmpty(vStartTime.getText().toString())){
						//释放定时器资源
						timer.cancel();
						timer = null;

						endTime = System.currentTimeMillis();
						endTimeString = TimeUtil.formatDetailDate(endTime);

						StringBuilder workTime = new StringBuilder();
						long work = endTime - startTime;
						int hour = (int) (work/1000/60/60);
						int minute = (int) (work%(1000*60*60)/1000/60);
						int workminute = (int) (work/(1000*60));
						workTime.append(hour);
						workTime.append("小时");
						if((work%(1000*60*60*60)/1000/60/60)==0){
							workTime.append(minute);
						}else{
							workTime.append(minute+1);
						}
						workTime.append("分钟");
						vJiShi.setText(workTime);
						setWorkRecordIntoDataBase(MyApplication.userId, mCaiZhiHao,mGuiGeHao,
								mLuPiHao,vNcText.getText().toString().trim(), mHeGeHao,
								startTimeString, endTimeString, workTime.toString(),workminute);
						copyDataBaseToSD();
						vEndTime.setText(endTimeString.substring(11,endTimeString.length()));

						queryTotalWorkTimeFromTable(MyApplication.userId, startTimeString.substring(0, 7));
					}}else{
						if(workEnum == 2){
							setButtonBackColor(endButton);
						}else{
							Toast.makeText(getApplicationContext(), R.string.please_frist_work, Toast.LENGTH_SHORT).show();
						}
					}
			}
		});
		vFullScreenButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(CamelBellUtils.isNotEmpty(cutPltate)){
					Intent intent = new Intent();
					intent.setClass(RecordActivity.this, ShowAllScreenActivity.class);
					intent.putExtra("Pdf_Path", pdflFilePath+File.separator+cutPltate+".pdf");
					startActivity(intent);
//					openFileByWps(pdflFilePath+File.separator+cutPltate+".pdf");
				}
			}
		});
		vSearchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					String ncNum = vNcText.getText().toString().trim();
					if(ncNum!=null&&!"".equals(ncNum)){
						boolean checkOut = queryByNcCode(ncNum);
						if(!checkOut){
							Toast.makeText(RecordActivity.this, "没有查询到对应的切割板资料",Toast.LENGTH_SHORT).show();
							//没有查询到任务数据的时候，将界面制空。
							vPdfContaierLayout.removeAllViews();
						}
					}else{
						Toast.makeText(RecordActivity.this, "请输入NC号", Toast.LENGTH_SHORT).show();
					}
			}
		});
//		vAddButton.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				documentView.addZoom();
//			}
//		});
//		vMinusButton.setOnClickListener(new OnClickListener() {

//			@Override
//			public void onClick(View arg0) {
//				documentView.minusZoom();
//			}
//		});
		vNcText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence chars, int arg1, int arg2, int arg3) {
//				String content = chars.toString();  
//				if(CamelBellUtils.isNotEmpty(content)){
//					query(content);
//				}else{
//					vHeGeHao.setText("");
//					vGuiGeHao.setText("");
//					vLuPiHao.setText("");
//					vCaiZhiHao.setText("");
//					vJiShi.setText("");
//					vStartTime.setText("");
//				}
			}
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

			}
			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});

		vScannerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(RecordActivity.this, SingleMipcaActivity.class);
				startActivityForResult(intent, 400100);
			}
		});
		
		vTopBackImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				long secondTime = System.currentTimeMillis();
				if (secondTime - mFirstTime > 2000) {
					UIUtil.showShortMessage(R.string.exit);
					mFirstTime = secondTime;// 更新firstTime
				} else {
					ActivityStackManager.finishAllActivity();
				}
			}
		});
		

		vTopRefreshImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(CamelBellUtils.isNotEmpty(vNcText.getText().toString().trim())){
					boolean checkOut = queryByNcCode(vNcText.getText().toString().trim());
					if(!checkOut){
						Toast.makeText(RecordActivity.this, "没有该NC号相关资料", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(RecordActivity.this, "没有输入查询的NC号", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		
		
	}
	/**
	 * 当前的button更改颜色为选中状态
	 * @param clickButton
	 */
	public void setButtonBackColor(Button clickButton){
		setButtonBackToCommon();
		clickButton.setBackgroundColor(getResources().getColor(R.color.choose_status_back));
	}
	/**
	 * 将所有的按钮背景改为普通颜色
	 */
	public void setButtonBackToCommon(){
		startButton.setBackgroundColor(getResources().getColor(R.color.work_status_back));
		pauseButton.setBackgroundColor(getResources().getColor(R.color.work_status_back));
		continueButton.setBackgroundColor(getResources().getColor(R.color.work_status_back));
		endButton.setBackgroundColor(getResources().getColor(R.color.work_status_back));
	}
	class MyStatisticsTimerTask extends TimerTask{

		@Override
		public void run() {
			mHandler.sendEmptyMessage(1);
		}

	}
	/**
	 * 查询对应切割版号对应钢材的信息（并显示）
	 * @param cutCode
	 * @return boolean 是否查询到数据
	 */
	private boolean queryByNcCode(String cutCode)   
	{   
		boolean isCheck = false;
		SQLiteDatabase db = database.getDatabase();
		String section = "id = ?";
		Cursor cursor = db.query(ConstantDatas.XLS_TABLE_NAME, new String[]{}, section, new String[]{cutCode}, null, null, null);   

		//根据自己程序需要对数据库做对应的操作   
		if(cursor.moveToLast()){
//			new Handler().postAtTime(new Runnable() {
//
//				@Override
//				public void run() {
//					vNcText.dismissDropDown();
//				}
//			}, 500);
			//		String level = cursor.getString(cursor.getColumnIndexOrThrow(ConstantDatas.XLS_LEVEL));   
			//		String project = cursor.getString(cursor.getColumnIndexOrThrow(ConstantDatas.XLS_PROJECT));   
			//		String sections = cursor.getString(cursor.getColumnIndexOrThrow(ConstantDatas.XLS_SECTION));   
			//		String classFiction = cursor.getString(cursor.getColumnIndexOrThrow(ConstantDatas.XLS_CLASS_FICATION));   
			//材质
			String texture = cursor.getString(cursor.getColumnIndexOrThrow(ConstantDatas.XLS_TEXTURE)); 
			//炉批号
			String batch = cursor.getString(cursor.getColumnIndexOrThrow(ConstantDatas.XLS_BATCH));
			//规格
			String specification = cursor.getString(cursor.getColumnIndexOrThrow(ConstantDatas.XLS_SPECIFICATION));   
			//切割板图号（唯一的Id）
			cutPltate = cursor.getString(cursor.getColumnIndexOrThrow(ConstantDatas.XLS_CUTTING_PLATE));
			Log.d("VC-VC",cutPltate);
			//合格号
			String aualified = cursor.getString(cursor.getColumnIndexOrThrow(ConstantDatas.XLS_AUALIFIED));   
			cursor.close();

			initPDF(pdflFilePath+File.separator+cutCode+".pdf", vPdfContaierLayout);
//			openPDFfile(pdflFilePath+File.separator+cutPltate+".pdf");

			mHeGeHao = aualified;
			mGuiGeHao = specification;
			mLuPiHao = batch;
			mCaiZhiHao = texture;
			if(CamelBellUtils.isNotEmpty(mHeGeHao)||CamelBellUtils.isNotEmpty(mGuiGeHao)
					||CamelBellUtils.isNotEmpty(mLuPiHao)||CamelBellUtils.isNotEmpty(mCaiZhiHao)){
				isCheck = true;
			}

			//			if(!cutHistoryCodes.contains(vQieGeBanHao.getText().toString().trim())){
			//				cutHistoryCodes.add(vQieGeBanHao.getText().toString().trim());
			//			}

			//			FileUtil.setObjectToFile(cutHistoryCodes, cutObjectPath);
		} else{
			clearRecordData();
			Toast.makeText(RecordActivity.this, "没有对应的文档数据！", Toast.LENGTH_LONG).show();
			//没有查询到任务数据的时候，将界面制空。
			vPdfContaierLayout.removeAllViews();
		}
		
		return isCheck;
	}
	/**
	 * 清除当前查询的切割板数据
	 */
	public void clearRecordData(){
		mHeGeHao = "";
		mGuiGeHao = "";
		mLuPiHao = "";
		mCaiZhiHao = "";
	}

	/**
	 * 查询当月的工作时间
	 */
	private void queryTotalWorkTimeFromTable(String userId,String month)   
	{   
		try{
		SQLiteDatabase db = database.getDatabase();
		String section = "user_id = ? and start_work_time like ?";
		Cursor cursor = db.query(ConstantDatas.WORK_RECORD_TABLE, new String[]{}, section, new String[]{userId,month+"%"}, null, null, null);   
		int totalTime = 0;
		//根据自己程序需要对数据库做对应的操作   
		if(cursor.moveToFirst()){
			for (int i = 0; i < cursor.getCount(); i++) {  
				int  wrokTime = cursor.getInt(cursor.getColumnIndexOrThrow(ConstantDatas.USER_TOTAL_TIME)); 
				cursor.moveToNext();  
				totalTime = totalTime + wrokTime;
			}  
		} 
		cursor.close();
		if(totalTime!=0){
			vMonthTime.setText(totalTime/60+"小时"+totalTime%60+"分钟");
		}else{
			vMonthTime.setText("");
		}
		}catch(Exception e ){
			e.printStackTrace();
		}
	}
	/**
	 * 将当前用户结束后的工作记录输入数据库
	 * @param userid 用户Id
	 * @param textture 材质
	 * @param specification 规格
	 * @param batch 炉号
	 * @param cutplate 切割板号
	 * @param aualified  合格号
	 * @param startTime	开始时间	
	 * @param endTime	结束时间
	 * @param workTime	工作时长
	 */
	public void setWorkRecordIntoDataBase(String userid,String textture,String specification,String batch,
			String cutplate,String aualified,String startTime,String endTime,String workTime,int workminute){
		SQLiteDatabase db = null;
		db = database.getDatabase();
		String sql = ConstantDatas.ADD_RECORD_WORK_INFO;
		// 执行SQL语句
		db.execSQL(sql, new Object[] {userid,textture,specification,batch,cutplate,
				aualified ,startTime ,endTime , workTime,workminute});
	}
	/**
	 * 将XLS中的数据放到数据库中去
	 */
	private void initDataBase(){
		database = new DatabaseHelper(this);// 这段代码放到Activity类中才用this
		SQLiteDatabase db = null;
		xlsBeanList = new ArrayList<XlsBean>();
		cutHistoryCodes = new ArrayList<String>();
		db = database.getDatabase();
		try {
			//删除之前表中的数据，重新载入表中的数据
//			db.execSQL("TRUNCATE TABLE record_test01_xls");
			
			Workbook book = Workbook.getWorkbook(
					new File(stellFile+File.separator+"reference_table.xls"));
			// get a Sheet object.
			Sheet sheet = book.getSheet(0);
			int a = sheet.getRows();// 列
			int b = sheet.getColumns();// 行
			System.out.println(a + "--" + b);
			// get 1st-Column,1st-Row content.
			// 遍历所有的数据
			for (int i = 0; i < a; i++) {
				xlsBean = new XlsBean();
				xlsBean.level = sheet.getCell(0, i).getContents();
				xlsBean.projectNum = sheet.getCell(1, i).getContents();
				xlsBean.sectionNum = sheet.getCell(2, i).getContents();
				xlsBean.classificationSociety = sheet.getCell(3, i).getContents();
				xlsBean.textureMaterial = sheet.getCell(4, i).getContents();
				xlsBean.specifications = sheet.getCell(5, i).getContents();
				xlsBean.batchNumber = sheet.getCell(6, i).getContents();
				xlsBean.goodNum = sheet.getCell(7, i).getContents();
				xlsBean.cuttingPlateDrawingNum = sheet.getCell(8, i).getContents();
				xlsBean.qualifiedNo = sheet.getCell(9, i).getContents();
				if(!CamelBellUtils.isNotEmpty(xlsBean.level)&&!CamelBellUtils.isNotEmpty(xlsBean.cuttingPlateDrawingNum)){
					break;
				}
				xlsBeanList.add(xlsBean);
				if(i>0){
						cutHistoryCodes.add(xlsBean.cuttingPlateDrawingNum);
				}

				System.out.println("xlsBeanList:"+xlsBeanList.size());
			}
			book.close();
			//			allHasCutCodes.notifyDataSetChanged();
//			allHasCutCodes = new ArrayAdapter<String>(this,
//					android.R.layout.simple_dropdown_item_1line,cutHistoryCodes);
			//		}
//			vQieGeBanHao.setAdapter(allHasCutCodes);

			//将所有的数据存到数据库中去
			String sql = ConstantDatas.ADD_XLS_INFO;
			for(int j = 0;j<xlsBeanList.size();j++){
				// 执行SQL语句
				db.execSQL(sql, new Object[] {
						xlsBeanList.get(j).cuttingPlateDrawingNum,xlsBeanList.get(j).level,
						xlsBeanList.get(j).projectNum,xlsBeanList.get(j).sectionNum,
						xlsBeanList.get(j).classificationSociety,xlsBeanList.get(j).textureMaterial,
						xlsBeanList.get(j).specifications,xlsBeanList.get(j).batchNumber,
						xlsBeanList.get(j).goodNum,xlsBeanList.get(j).cuttingPlateDrawingNum,
						xlsBeanList.get(j).qualifiedNo
				});
			}

		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
		}


		long currentTime = System.currentTimeMillis();
		String currentTimeString = TimeUtil.formatDetailDate(currentTime);
		if(!StringUtil.isEmpty(currentTimeString)&&currentTimeString.length()>7){
			queryTotalWorkTimeFromTable(MyApplication.userId, currentTimeString.substring(0, 7));
		}else{
			queryTotalWorkTimeFromTable(MyApplication.userId, "");
		}
		
	}
	/**
	 * 复制数据库到SD卡
	 */
	public void copyDataBaseToSD(){
		//		String DBPath,String trimPath
		String dataPath = "/data/data/"+getPackageName()+File.separator+"databases"+File.separator+ConstantDatas.XLS_DB_NAME;
		String trimPath = stellFile+File.separator+ConstantDatas.XLS_DB_NAME;
		try {
			FileUtil.copyAndReplaceFile(dataPath, trimPath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if(requestCode == 400100){
			if(intent!=null){
				String code = intent.getStringExtra("codeNum");
				if(!StringUtil.isEmpty(code)){
					vNcText.setText(code);
					core = null;
					queryByNcCode(code);
				}else{
					Toast.makeText(RecordActivity.this, "没有查询到对应的文档。", Toast.LENGTH_LONG).show();
					//没有查询到任务数据的时候，将界面制空。
					vPdfContaierLayout.removeAllViews();
				}
			}else{
				Toast.makeText(RecordActivity.this, "没有查询到对应的文档。", Toast.LENGTH_LONG).show();
				//没有查询到任务数据的时候，将界面制空。
				vPdfContaierLayout.removeAllViews();
			}
			
		}
	}

	@Override
	public void onBackPressed() {
		long secondTime = System.currentTimeMillis();
		if (secondTime - mFirstTime > 2000) {
			UIUtil.showShortMessage(R.string.exit);
			mFirstTime = secondTime;// 更新firstTime
		} else {
			ActivityStackManager.finishAllActivity();
		}
	}
	
	
	/**
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
		
//		if (core == null) {
//			core = (MuPDFCore)getLastNonConfigurationInstance();
//		}
		core = null;
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
//			final AlertDialog alert = mAlertBuilder.create();
//			alert.setTitle(R.string.cannot_open_document);
//			alert.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.dismiss),
//					new DialogInterface.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
////					finish();
//					alert.dismiss();
//				}
//			});
//			alert.show();
			Toast.makeText(RecordActivity.this, "没有对应的PDF文件！", Toast.LENGTH_LONG).show();
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
	
	/**
	 * 获取当前用户姓名
	 * @return
	 */
	public String getUserName(){
		SharedPreferences userShare = getSharedPreferences("userShare", MODE_PRIVATE);
		String userName = "";
		userName = userShare.getString("userName", "");
		return userName;
	}
	/**
	 * 通过wps打开PDF
	 * @return
	 */
	public boolean openFileByWps(String path) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(WpsModel.OPEN_MODE, WpsModel.OpenMode.NORMAL); // 打开模式
        bundle.putBoolean(WpsModel.SEND_CLOSE_BROAD, true); // 关闭时是否发送广播
        bundle.putString(WpsModel.THIRD_PACKAGE, getPackageName()); // 第三方应用的包名，用于对改应用合法性的验证
        bundle.putBoolean(WpsModel.CLEAR_TRACE, true);// 清除打开记录
        // bundle.putBoolean(CLEAR_FILE, true); //关闭后删除打开文件
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setClassName(WpsModel.PackageName.NORMAL, WpsModel.ClassName.NORMAL);

        File file = new File(path);
        if (file == null || !file.exists()) {
            return false;
        }

        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        intent.putExtras(bundle);

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();

            return false;
        }

        return true;
    }


}
