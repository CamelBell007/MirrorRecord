package com.camelbell.jobrecord.activity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.camelbell.jobrecord.utils.ConstantDatas;

/**
 * SQLiteOpenHelper是一个辅助类，用来管理数据库的创建和版本他，它提供两个方面的功能
 * 第一，getReadableDatabase()、getWritableDatabase
 * ()可以获得SQLiteDatabase对象，通过该对象可以对数据库进行操作
 * 第二，提供了onCreate()、onUpgrade()两个回调函数，允许我们再创建和升级数据库时，进行自己的操作
 * 
 * @author 殷加伟
 */
public class DatabaseHelper extends SQLiteOpenHelper {
	private static final int VERSION = 1;
	private SQLiteDatabase db = null;
	/**
	 * 在SQLiteOpenHelper的子类当中，必须有该构造函数
	 * 
	 * @param context
	 *            上下文对象
	 * @param name
	 *            数据库名称
	 * @param factory
	 * @param version
	 *            当前数据库的版本，值必须是整数并且是递增的状态
	 */

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		// 必须通过super调用父类当中的构造函数
		super(context, name, factory, version);
	}

	public DatabaseHelper(Context context, String name, int version) {
		this(context, name, null, version);
	}

	public DatabaseHelper(Context context, String name) {
		this(context, name, VERSION);
	}
	public DatabaseHelper(Context context) {
		this(context, ConstantDatas.XLS_DB_NAME, VERSION);
	}

	/**
	 * 该函数是在第一次创建的时候执行，实际上是第一次得到SQLiteDatabase对象的时候才会调用这个方法
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		// execSQL用于执行SQL语句
		System.out.println(ConstantDatas.CREATE_WORK_RECORD);
//		db.execSQL("TRUNCATE TABLE IF EXISTS record_test01_xls");
		db.execSQL(ConstantDatas.CREATE_XLS_TABLE);
		db.execSQL(ConstantDatas.CREATE_WORK_RECORD);
	}

	/**
	 * 更新数据库
	 */
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		//		System.out.println("upgrade a database");
	}

	/**
	 * 获取
	 * 
	 * @return SQLiteDatabase
	 */
	public SQLiteDatabase getDatabase() {
		try {
			db = this.getWritableDatabase();
			if(db==null){
				db = this.getReadableDatabase();
			}
		} catch (Exception e) {
			db = this.getReadableDatabase();
		}
		return db;

	}

	/**
	 * 关闭数据库
	 */
	@Override
	public void close() {
		if (db != null) {
			if (db.isOpen()) {
				db.close();
			}

		}
	}
}