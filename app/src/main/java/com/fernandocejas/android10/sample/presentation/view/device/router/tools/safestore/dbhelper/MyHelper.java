package com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyHelper extends SQLiteOpenHelper {

	private static String DB_NAME = "mydata.db";  //数据库名称
	public static String TABLE_NAME = "employee"; //表名
	public static String TABLE_UPLOAD = "upload"; //表名
	public static String TABLE_HISTORY = "history"; //搜索历史
	public static String TABLE_BAND_HISTORY = "band_speed_history"; //宽带测速历史
	public static String TABLE_RESTORE_PATH = "restore_path"; //图片备份 已经备份的图片
	public static String TABLE_LODERS_KEY = "key"; //downloaders key

	/**super(参数1，参数2，参数3，参数4)，其中参数4是代表数据库的版本，
	 * 是一个大于等于1的整数，如果要修改（添加字段）表中的字段，则设置
	 * 一个比当前的 参数4大的整数 ，把更新的语句写在onUpgrade(),下一次
	 * 调用
	 */
	public MyHelper(Context context) {
		super(context, DB_NAME, null, 2);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//Create table
		String sql = "CREATE TABLE "+TABLE_NAME + "("
				+ "_id INTEGER PRIMARY KEY,"
				+ "name TEXT,"
				+ "num TEXT,"
				+ "sumSize TEXT,"
				+ "finishedSize TEXT,"
				+ "netSpeed TEXT,"
				+ "circleProgress TEXT,"
				+ "date TEXT,"
				+ "path);";

		Log.e("table oncreate", "create table");
		db.execSQL(sql); 		//创建表
		System.out.println("\"建表：\" = " + "创建表");

		String sql1 = "CREATE TABLE "+TABLE_HISTORY + "("
				+ "name);";

		Log.e("table oncreate", "create table");
		db.execSQL(sql1); 		//创建文件历史搜索表
		System.out.println("\"建表：\" = " + "创建文件历史搜索表");

		String sql2 = "CREATE TABLE "+TABLE_UPLOAD + "("
				+ "_id INTEGER PRIMARY KEY,"
				+ "name TEXT,"
				+ "num TEXT,"
				+ "sumSize TEXT,"
				+ "finishedSize TEXT,"
				+ "netSpeed TEXT,"
				+ "circleProgress TEXT,"
				+ "date TEXT,"
				+ "path);";

		Log.e("table oncreate", "create table");
		db.execSQL(sql2); 		//创建表
		System.out.println("\"建表：\" = " + "创建表");

		String sql3 = "CREATE TABLE "+TABLE_LODERS_KEY + "("
				+ "key);";

		Log.e("table oncreate", "create table");
		db.execSQL(sql3); 		//创建文件历史搜索表
		System.out.println("\"建表：\" = " + "存储key的表");

		String sql4 = "CREATE TABLE "+TABLE_BAND_HISTORY + "("
				+ "date TEXT,"
				+ "speed);";

		Log.e("table oncreate", "create table");
		db.execSQL(sql4); 		//创建
		System.out.println("\"建表：\" = " + "创建宽带测速历史表");

		String sql5 = "CREATE TABLE "+TABLE_RESTORE_PATH + "("
				+ "path);";

		Log.e("table oncreate", "create table");
		db.execSQL(sql5); 		//创建
		System.out.println("\"建表：\" = " + "创建图片备份表");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.e("update", "update");
//		db.execSQL("ALTER TABLE "+ MyHelper.TABLE_NAME+" ADD sex TEXT"); //修改字段


	}

}
