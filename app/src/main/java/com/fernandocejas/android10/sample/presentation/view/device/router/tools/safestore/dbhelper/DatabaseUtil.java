package com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.fernandocejas.android10.sample.presentation.view.device.router.tools.bandspeedmeasure.BandSpeedHisBean;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.FileBean;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.FileUploadBean;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.SearchFileHistoryBean;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.utils.SambaUtils;
import com.qtec.router.model.rsp.GetBandSpeedResponse;

import java.util.ArrayList;
import java.util.List;

public class DatabaseUtil {
	private MyHelper helper;

	public DatabaseUtil(Context context) {
		super();
		helper = new MyHelper(context);
	}

	/**插入数据
	 * @param
	 * */
	public final boolean insert(FileUploadBean uploadFiles){
		SQLiteDatabase db = helper.getWritableDatabase();
		String sql = "insert into "+MyHelper.TABLE_NAME
				+"(name,num,sumSize,finishedSize,netSpeed,circleProgress,date,path) values ("
				+ "'"+uploadFiles.getName()
				+ "' ," + "'"+ uploadFiles.getNum()
				+ "' ," + "'"+ uploadFiles.getSumSize()
				+ "' ," + "'"+ uploadFiles.getFinishedSize()
				+ "' ," + "'"+ uploadFiles.getNetSpeed()
				+ "' ," + "'"+ uploadFiles.getCircleProgress()
				+ "' ," + "'"+ uploadFiles.getDate()
				+ "' ," + "'"+ uploadFiles.getPath() + "'" + ")";
		try {
			db.execSQL(sql);
			return true;
		} catch (SQLException e){
			Log.e("err", "insert failed");
			e.printStackTrace();
			System.out.println("\"insert failed\" = " + "insert failed");
			return false;
		}finally{
			db.close();
		}

	}

	/**插入下载器的信息
	 * @param
	 * */
/*	public final boolean insertloadersKey(String key){
		SQLiteDatabase db = helper.getWritableDatabase();
		String sql = "insert into "+MyHelper.TABLE_LODERS_KEY
				+"(key) values ("
				+"'"+ key + "'" + ")";
		try {
			db.execSQL(sql);
			return true;
		} catch (SQLException e){
			Log.e("err", "insert key table failed");
			e.printStackTrace();
			System.out.println("\"insert key table failed\" = " + "insert failed");
			return false;
		}finally{
			db.close();
		}

	}*/

	/**
	 * 插入上传数据
	 * @param
	 * */
	public final boolean insertUploadData(FileUploadBean uploadFiles){
		SQLiteDatabase db = helper.getWritableDatabase();
		String sql = "insert into "+MyHelper.TABLE_UPLOAD
				+"(name,num,sumSize,finishedSize,netSpeed,circleProgress,date,path) values ("
				+ "'"+uploadFiles.getName()
				+ "' ," + "'"+ uploadFiles.getNum()
				+ "' ," + "'"+ uploadFiles.getSumSize()
				+ "' ," + "'"+ uploadFiles.getFinishedSize()
				+ "' ," + "'"+ uploadFiles.getNetSpeed()
				+ "' ," + "'"+ uploadFiles.getCircleProgress()
				+ "' ," + "'"+ uploadFiles.getDate()
				+ "' ," + "'"+ uploadFiles.getPath() + "'" + ")";
		try {
			db.execSQL(sql);
			return true;
		} catch (SQLException e){
			Log.e("err", "insert failed");
			e.printStackTrace();
			System.out.println("\"insert failed\" = " + "insert failed");
			return false;
		}finally{
			db.close();
		}

	}

	/**
	* 插入搜索历史
	*
	* @param
	* @return
	*/
	public final boolean insertSearchHistory(SearchFileHistoryBean bean){
		SQLiteDatabase db = helper.getWritableDatabase();
		String sql = "insert into "+MyHelper.TABLE_HISTORY
				+"(name) values ("
						+"'"+ bean.getName() + "'" + ")";
		try {
			db.execSQL(sql);
			return true;
		} catch (SQLException e){
			Log.e("err", "insert history failed");
			e.printStackTrace();
			System.out.println("\"insert history failed\" = " + "insert failed");
			return false;
		}finally{
			db.close();
		}

	}

	/**
	* 插入宽带测速历史
	*
	* @param
	* @return
	*/
	public final boolean insertBandSpeedHistory(BandSpeedHisBean bean){
		SQLiteDatabase db = helper.getWritableDatabase();
		String sql = "insert into "+MyHelper.TABLE_BAND_HISTORY
				+"(date,speed) values ("
				+ "'"+bean.getDate()
				+ "' ," + "'"+ bean.getSpeed() + "'" + ")";
		try {
			db.execSQL(sql);
			return true;
		} catch (SQLException e){
			Log.e("err", "insert failed");
			e.printStackTrace();
			System.out.println("\"insert failed\" = " + "insert failed");
			return false;
		}finally{
			db.close();
		}

	}

	/**
	* 保存图片备份已经备份的url
	*
	* @param
	* @return
	*/
	public final boolean saveRestoredPath(String path){
		SQLiteDatabase db = helper.getWritableDatabase();
		String sql = "insert into "+MyHelper.TABLE_RESTORE_PATH
				+"(path) values ("
				+ "'"+ path + "'" + ")";
		try {
			db.execSQL(sql);
			return true;
		} catch (SQLException e){
			Log.e("err", "insert failed");
			e.printStackTrace();
			System.out.println("\"insert failed\" = " + "insert failed");
			return false;
		}finally{
			db.close();
		}

	}

	/**更新数据
	 * @param   , int id
	 * */
	public void Update(FileUploadBean uploadFiles ,int id){

		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("name", uploadFiles.getName());
		values.put("num", uploadFiles.getNum());
		values.put("sum_size", uploadFiles.getSumSize());
		values.put("finised_size", uploadFiles.getFinishedSize());
		values.put("net_speed", uploadFiles.getNetSpeed());
		values.put("circle_progress", uploadFiles.getCircleProgress());
		values.put("date", uploadFiles.getDate());
		values.put("path", uploadFiles.getPath());
		int rows = db.update(MyHelper.TABLE_NAME, values, "_id=?", new String[] { id + "" });

		db.close();
	}

	/**删除数据
	 * @param
	 * */

	public void deleteDownloadUrls(String url){
		SQLiteDatabase db = helper.getWritableDatabase();

		try{
			db.delete(MyHelper.TABLE_NAME, "path=?", new String[]{url+""});
			db.close();

		}catch (Exception e){
			e.printStackTrace();
		}

		finally {
			if (null != db) {
				db.close();
			}
		}
	}

	public void deleteUploadUrls(String url){
		SQLiteDatabase db = helper.getWritableDatabase();

		try{
			db.delete(MyHelper.TABLE_UPLOAD, "path=?", new String[]{url+""});
			db.close();

		}catch (Exception e){
			e.printStackTrace();
		}

		finally {
			if (null != db) {
				db.close();
			}
		}
	}

	/**
	* 删除历史记录
	*
	* @param
	* @return
	*/
	public void DeleteHistoryData(String name1){

		SQLiteDatabase db = helper.getWritableDatabase();
		int raw = db.delete(MyHelper.TABLE_HISTORY, "name=?", new String[]{name1+""});
		db.close();
	}

	/**
	* 删除Key
	*
	* @param
	* @return
	*/
/*	public synchronized void deleteKey(String key1){

		SQLiteDatabase db = helper.getWritableDatabase();

		try{
			db.delete(MyHelper.TABLE_LODERS_KEY, "key=?", new String[]{key1+""});
			db.close();

		}catch (Exception e){
			e.printStackTrace();
		}

		 finally {
			if (null != db) {
				db.close();
			}
		}
	}*/

	/**查询所有数据
	 *
	 * */
	public List<FileUploadBean> queryAll(){
		SQLiteDatabase db = helper.getReadableDatabase();
		List<FileUploadBean> list = new ArrayList<FileUploadBean>();
		Cursor cursor = db.query(MyHelper.TABLE_NAME, null, null,null, null, null, null);

		while(cursor.moveToNext()){
			FileUploadBean file = new FileUploadBean();
			file.setId(cursor.getInt(cursor.getColumnIndex("_id")));
			file.setName(cursor.getString(cursor.getColumnIndex("name")));
			file.setNum(cursor.getString(cursor.getColumnIndex("num")));
			file.setSumSize(cursor.getLong(cursor.getColumnIndex("sumSize")));
			file.setFinishedSize(cursor.getLong(cursor.getColumnIndex("finishedSize")));
			file.setNetSpeed(cursor.getString(cursor.getColumnIndex("netSpeed")));
			file.setCircleProgress(cursor.getString(cursor.getColumnIndex("circleProgress")));
			file.setDate(cursor.getString(cursor.getColumnIndex("date")));
			file.setPath(cursor.getString(cursor.getColumnIndex("path")));

			list.add(file);
		}

		try{
			//去除重复的元素
			/*SambaUtils.removeDuplicateData(list);*/
		}catch (Exception e){
			e.printStackTrace();
		}

		db.close();
		return list;
	}

	/**
	 * 查询所有上传数据
	 *
	 * */
	public List<FileUploadBean> queryAllUploadData(){
		SQLiteDatabase db = helper.getReadableDatabase();
		List<FileUploadBean> list = new ArrayList<FileUploadBean>();
		Cursor cursor = db.query(MyHelper.TABLE_UPLOAD, null, null,null, null, null, null);

		while(cursor.moveToNext()){
			FileUploadBean file = new FileUploadBean();
			file.setId(cursor.getInt(cursor.getColumnIndex("_id")));
			file.setName(cursor.getString(cursor.getColumnIndex("name")));
			file.setNum(cursor.getString(cursor.getColumnIndex("num")));
			file.setSumSize(cursor.getLong(cursor.getColumnIndex("sumSize")));
			file.setFinishedSize(cursor.getLong(cursor.getColumnIndex("finishedSize")));
			file.setNetSpeed(cursor.getString(cursor.getColumnIndex("netSpeed")));
			file.setCircleProgress(cursor.getString(cursor.getColumnIndex("circleProgress")));
			file.setDate(cursor.getString(cursor.getColumnIndex("date")));
			file.setPath(cursor.getString(cursor.getColumnIndex("path")));

			list.add(file);
		}

		try {
			//去除重复元素
			/*SambaUtils.removeDuplicateData(list);*/
		}catch (Exception e){
			e.printStackTrace();
		}

		db.close();
		return list;
	}

	/**
	* 查询文件搜索历史记录
	*
	* @param
	* @return
	*/
	public List<SearchFileHistoryBean> queryAllSearchHistory(){
		SQLiteDatabase db = helper.getReadableDatabase();
		List<SearchFileHistoryBean> list = new ArrayList<SearchFileHistoryBean>();
		Cursor cursor = db.query(MyHelper.TABLE_HISTORY, null, null,null, null, null, null);

		while(cursor.moveToNext()){
			SearchFileHistoryBean file = new SearchFileHistoryBean();
			file.setName(cursor.getString(cursor.getColumnIndex("name")));
			list.add(file);
		}
		db.close();
		return list;
	}

	public List<BandSpeedHisBean> queryAllBandSpeedHistory(){
		SQLiteDatabase db = helper.getReadableDatabase();
		List<BandSpeedHisBean> list = new ArrayList<>();
		Cursor cursor = db.query(MyHelper.TABLE_BAND_HISTORY, null, null,null, null, null, null);

		while(cursor.moveToNext()){
			BandSpeedHisBean file = new BandSpeedHisBean();
			file.setDate(cursor.getString(cursor.getColumnIndex("date")));
			file.setSpeed(cursor.getString(cursor.getColumnIndex("speed")));
			list.add(file);
		}
		db.close();
		return list;
	}

	/**
	*
	*
	* @param
	* @return
	*/
	public List<String> queryAllRestoredPaths(){
		SQLiteDatabase db = helper.getReadableDatabase();
		List<String> list = new ArrayList<>();
		Cursor cursor = db.query(MyHelper.TABLE_RESTORE_PATH, null, null,null, null, null, null);

		while(cursor.moveToNext()){
			list.add(cursor.getString(cursor.getColumnIndex("path")));
		}
		db.close();
		return list;
	}

	/**
	* 查询下载器的key
	*
	* @param
	* @return
	*/
	public List<String> queryAllKey(){
		SQLiteDatabase db = helper.getReadableDatabase();
		List<String> list = new ArrayList<String>();
		Cursor cursor = db.query(MyHelper.TABLE_LODERS_KEY, null, null,null, null, null, null);

		while(cursor.moveToNext()){
			list.add(cursor.getString(cursor.getColumnIndex("key")));
		}
		db.close();
		return list;
	}

	/**按姓名进行查找并排序
	 *
	 * */
/*	public List<Person> queryByname(String name){
		SQLiteDatabase db = helper.getReadableDatabase();
		List<Person> list = new ArrayList<Person>();
		Cursor cursor = db.query(MyHelper.TABLE_NAME, new String[]{"_id","name","sex"}, "name like ? " ,new String[]{"%" +name + "%" }, null, null, "name asc");
//		Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy)
		while(cursor.moveToNext()){
			Person person = new Person();
			person.setId(cursor.getInt(cursor.getColumnIndex("_id")));
			person.setName(cursor.getString(cursor.getColumnIndex("name")));
			person.setSex(cursor.getString(cursor.getColumnIndex("sex")));
			list.add(person);
		}
		db.close();
		return list;
	}*/


	/**
	 * 按path查询
	 *
	 * */
/*	public Person queryByid(int id){

		SQLiteDatabase db = helper.getReadableDatabase();
		Person person = new Person();
		Cursor cursor = db.query(MyHelper.TABLE_NAME, new String[]{"name","sex"}, "_id=?",new String[]{ id + ""}, null, null, null);
//		db.delete(table, whereClause, whereArgs)
		while(cursor.moveToNext()){
			person.setId(id);
			person.setName(cursor.getString(cursor.getColumnIndex("name")));
			person.setSex(cursor.getString(cursor.getColumnIndex("sex")));
		}
		db.close();
		return person;
	}*/

	/**
	 * 查询是否有记录 下载
	 *
	 * */
	public Boolean queryByUrl(String path){
		Boolean isHasData = false;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query(MyHelper.TABLE_NAME, new String[]{"name","path"}, "path=?",new String[]{ path + ""}, null, null, null);
//		db.delete(table, whereClause, whereArgs)
		while(cursor.moveToNext()){
			isHasData = true;
		}
		db.close();
		return isHasData;
	}

	/**
	* 上传
	*
	* @param
	* @return
	*/
	public Boolean queryByUploadUrl(String path){
		Boolean isHasData = false;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query(MyHelper.TABLE_UPLOAD, new String[]{"name","path"}, "path=?",new String[]{ path + ""}, null, null, null);
//		db.delete(table, whereClause, whereArgs)
		while(cursor.moveToNext()){
			isHasData = true;
		}
		db.close();
		return isHasData;
	}

}
