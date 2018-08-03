package com.camelbell.jobrecord.utils;




/**
 * DB的语句
 * 
 * @author camelbell
 * 
 */
public class ConstantDatas
{

	public static final String XLS_DB_NAME = "record.db";
	public static final String XLS_TABLE_NAME = "record_test01_xls";
	public static final String WORK_RECORD_TABLE = "work_job";
	public static final String XLS_ID = "id";
	public static final String XLS_LEVEL = "level";//层次
	public static final String XLS_PROJECT = "projectNum";//工程号
	public static final String XLS_SECTION = "sectionNum";//分段号
	public static final String XLS_CLASS_FICATION = "classificationSociety";//船籍社
	public static final String XLS_TEXTURE = "textureMaterial";//材质
	public static final String XLS_SPECIFICATION = "specifications";//规格
	public static final String XLS_BATCH = "batchNumber";//炉批号
	public static final String XLS_GOOD = "goodNum";//货号位
	public static final String XLS_CUTTING_PLATE = "cuttingPlateDrawingNum";//切割板图号
	public static final String XLS_AUALIFIED= "qualifiedNo";//合格号

	public static final String USER_ID = "user_id";
	public static final String USER_START_TIME = "start_work_time";
	public static final String USER_END_TIME = "end_work_time";
	public static final String USER_WORK_TIME = "work_time_length";
	public static final String USER_TOTAL_TIME = "work_total_time";
	//向XLS的表中添加相应的数据（xls中的数据添加到表中）
	public final static  String ADD_XLS_INFO = "INSERT or ignore into "
			+XLS_TABLE_NAME +" ( "
			+XLS_ID +" , "
			+XLS_LEVEL +" , "
			+XLS_PROJECT + " , "
			+XLS_SECTION + " , "
			+XLS_CLASS_FICATION + " , "
			+XLS_TEXTURE + " , "
			+XLS_SPECIFICATION+ " , "
			+XLS_BATCH+" , "
			+XLS_GOOD+" , "
			+XLS_CUTTING_PLATE+" , "
			+XLS_AUALIFIED +" ) " 
			+ "values ( ?,?,?,?,?,?,?,?,?,?,?)" ;
	//创建XLS表
	public final static  String CREATE_XLS_TABLE = "CREATE TABLE "
			+ XLS_TABLE_NAME + "("
			+ XLS_ID + " TEXT PRIMARY KEY , "
			+ XLS_LEVEL + " TEXT , "
			+ XLS_PROJECT + " TEXT ,"
			+ XLS_SECTION + " TEXT , "
			+ XLS_CLASS_FICATION + " TEXT,"
			+ XLS_TEXTURE + " TEXT ,"
			+ XLS_SPECIFICATION + " TEXT ," 
			+ XLS_BATCH + " TEXT ,"
			+XLS_GOOD+" TEXT , "
			+ XLS_CUTTING_PLATE + " TEXT ,"
			+ XLS_AUALIFIED + " TEXT "
			+ ");";

	//从表中获取数据
	public final static  String GET_DATA_XLS_TABLE = "SELECT TABLE "
			+ XLS_TABLE_NAME + "("
			+ XLS_ID + " TEXT PRIMARY KEY , "
			+ XLS_LEVEL + " TEXT , "
			+ XLS_PROJECT + " TEXT ,"
			+ XLS_SECTION + " TEXT , "
			+ XLS_CLASS_FICATION + " TEXT,"
			+ XLS_TEXTURE + " TEXT ,"
			+ XLS_SPECIFICATION + " TEXT ," 
			+ XLS_BATCH + " TEXT ,"
			+XLS_GOOD+" TEXT , "
			+ XLS_CUTTING_PLATE + " TEXT ,"
			+ XLS_AUALIFIED + " TEXT "
			+ ");";

	//创建工作记录表
	public final static  String CREATE_WORK_RECORD = "CREATE TABLE "
			+"if not exists "+ WORK_RECORD_TABLE + "("
			+ "_id integer primary key autoincrement,"
			+ USER_ID + " TEXT , "
			+ XLS_TEXTURE + " TEXT ,"
			+ XLS_SPECIFICATION + " TEXT ," 
			+ XLS_BATCH + " TEXT ,"
			+ XLS_CUTTING_PLATE + " TEXT ,"
			+ XLS_AUALIFIED + " TEXT ,"
			+ USER_START_TIME + " TEXT ,"
			+ USER_END_TIME + " TEXT , "
			+ USER_WORK_TIME + " TEXT , "
			+ USER_TOTAL_TIME + " INT "
			+ ");";
	//向工作记录表中添加相应的数据
	public final static  String ADD_RECORD_WORK_INFO = "INSERT into "
			+WORK_RECORD_TABLE +" ( "
			+USER_ID +" , "
			+XLS_TEXTURE + " , "
			+XLS_SPECIFICATION+ " , "
			+XLS_BATCH+" , "
			+XLS_CUTTING_PLATE+" , "
			+XLS_AUALIFIED+" , "
			+USER_START_TIME+" , "
			+USER_END_TIME+" , "
			+USER_WORK_TIME+" , "
			+USER_TOTAL_TIME +" ) " 
			+ "values ( ?,?,?,?,?,?,?,?,?,?)" ;

	/**
	 * 获取当前月份的所有工作时间总和
	 * 
	 */
	public static final String getWorkTimeFromTable(String userId,String month){
		String SELECT_TIME_FROM_WORK_TAB = "select sum(USER_TOTAL_TIME) from "+
				WORK_RECORD_TABLE+" where "+ USER_ID +" = " + userId +" and "+USER_START_TIME+
				" like "+month;
				return  SELECT_TIME_FROM_WORK_TAB;
		}

}