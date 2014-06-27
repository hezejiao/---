package com.sary.arounddepot.util;





import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sary.arounddepot.entity.AroundParkData;
import com.sary.arounddepot.entity.AroundParkListData;
/**
 * sqlite数据库
 * @author Administrator
 *
 */
public class DBHelper extends SQLiteOpenHelper {
	private static DBHelper instance = null;
	private static final String DB_NAME = "park.db";
	private static final String ALARM_COLOCK_TABLE = "alarmcolock";
	private static final int VERSION = 1;
	
	public final static String ALARM_ID = "_id";
	public final static String ALARM_TIME = "alarmtime"; //alarm time
	public final static String ALARM_REPEAT = "alarmrepeat";//alarm repeate is or not
	public final static String ALARM_ISOPEN = "alarmisopen";//alarm open is 0r not
	public final static String ALARM_KIND = "alarmkind"; //alarm is kind(1 is shuangseqiu ,2 is da le tou )
	public final static String ALARM_SPARE1 = "alarmspare1";//spare1
	public final static String ALARM_SPARE2 = "alarmspare2";//spare2
	public final static String ALARM_SPARE3 = "alarmspare3";//spare3

	public DBHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}

	public static DBHelper getInstance(Context context) {
		if (instance == null)
			instance = new DBHelper(context);
		return instance;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
	    db.execSQL("CREATE TABLE around_park(id INTEGER PRIMARY KEY AUTOINCREMENT,shortName VARCHAR NOT NULL,shortAddres VARCHAR NOT NULL, price VARCHAR NOT NULL,total INTEGER NOT NULL,remainNum INTEGER NOT NULL,online INTEGER NOT NULL,vip1 INTEGER NOT NULL,vip2 INTEGER NOT NULL,vip3 INTEGER NOT NULL,vip4 INTEGER NOT NULL,confirm INTEGER NOT NULL,distance INTEGER NOT NULL,isFree INTEGER NOT NULL,sortPrice REAL NOT NULL,latitude REAL NOT NULL,longitude REAL NOT NULL,turn INTEGER,wc INTEGER,lift INTEGER,slopeCharge INTEGER,twoFloor INTEGER,cmsingal INTEGER,cusingal INTEGER,ctsingal INTEGER,wheel INTEGER,chargenot INTEGER,imagepath VARCHAR,newsinfo VARCHAR)");
//	    String  sql = "create table "+ALARM_COLOCK_TABLE+" ("
//                +ALARM_ID+" integer primary key autoincrement, "
//                +ALARM_TIME+" text, "
//                +ALARM_REPEAT+" text, "
//                +ALARM_ISOPEN+" text, "
//                +ALARM_KIND+" text, "
//                +ALARM_SPARE1+" text, "
//                +ALARM_SPARE2+" text, "
//                +ALARM_SPARE3+" text )";
       db.execSQL("create table alarmcolock(_id INTEGER PRIMARY KEY AUTOINCREMENT,userid VARCHAR,alarmtime VARCHAR,alarmrepeat VARCHAR,alarmisopen VARCHAR)");
	}
	//create table alarmcolock(_id INTEGER PRIMARY KEY AUTOINCREMENT,alarmtime VARCHAR,alarmrepeat VARCHAR,alarmisopen VARCHAR,alarmkind VARCHAR)
	//,newsinfo VARCHAR NOT NULL
//	"pid INTEGER NOT NULL,turn INTEGER NOT NULL,wc INTEGER NOT NULL,lift INTEGER NOT NULL,slopeCharge INTEGER NOT NULL,twoFloor INTEGER NOT NULL,cmsingal INTEGER NOT NULL,cusingal INTEGER NOT NULL,ctsingal INTEGER NOT NULL,wheel INTEGER NOT NULL,chargenot INTEGER NOT NULL,name VARCHAR NOT NULL,address VARCHAR NOT NULL,tel VARCHAR NOT NULL,businessTime VARCHAR NOT NULL,price VARCHAR NOT NULL,price VARCHAR NOT NULL,)");
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE around_park");
		String sql = "drop table if exists "+ALARM_COLOCK_TABLE;
		db.execSQL(sql);
	}
	
	public void savePark(AroundParkData data){
		ContentValues values = new ContentValues();
		values.put("id", data.getId());
		values.put("shortName",data.getShortName());
		values.put("shortAddres",data.getShortAddres());
		values.put("price",data.getPrice());
		values.put("total",data.getTotal());
		values.put("remainNum",data.getRemainNum());
		values.put("online",data.getOnline());
		values.put("vip1",data.getVip1());
		values.put("vip2",data.getVip2());
		values.put("vip3",data.getVip3());
		values.put("vip4",data.getVip4());
		values.put("confirm",data.getConfirm());
		values.put("distance",data.getDistance());
		values.put("isFree",data.getIsFree());
		values.put("sortPrice",data.getSortPrice());
		values.put("latitude", data.getLatitude());
		values.put("longitude", data.getLongitude());
		values.put("turn",data.getTurn());
		values.put("wc",data.getWc());
		values.put("lift",data.getLift());
		values.put("slopeCharge",data.getSlopeCharge());
		values.put("twoFloor",data.getTwoFloor());
		values.put("cmsingal",data.getCmsingal());
		values.put("cusingal",data.getCusingal());
		values.put("ctsingal",data.getCtsingal());
		values.put("wheel",data.getWheel());
		values.put("chargenot",data.getChargenot());
		values.put("imagepath",data.getImageURL());
		values.put("newsinfo", data.getNewsInfo());
		SQLiteDatabase db = getWritableDatabase();
		long temp = db.insert("around_park", null, values);
		db.close();
	}
	
	public AroundParkListData getParkByCondition(String columnName){
		AroundParkListData listData = new AroundParkListData();
		 SQLiteDatabase db = getReadableDatabase();  
		 Cursor cursor = db.rawQuery("select * from around_park where 1=1 order by "+columnName, null);
		 while (cursor.moveToNext()) {
			  AroundParkData data = new AroundParkData();
			  data.id = cursor.getInt(0);
		      data.shortName = cursor.getString(1);
		      data.shortAddres = cursor.getString(2);
		      data.price = cursor.getString(3);
		      data.total = cursor.getInt(4);
		      data.remainNum = cursor.getInt(5);
		      data.online = cursor.getInt(6);
		      data.vip1 = cursor.getInt(7);
		      data.vip2 = cursor.getInt(8);
		      data.vip3 = cursor.getInt(9);
		      data.vip4 = cursor.getInt(10);
		      data.confirm = cursor.getInt(11);
		      data.distance = cursor.getInt(12);
		      data.isFree = cursor.getInt(13);
		      data.sortPrice = cursor.getDouble(14);
		      data.latitude = cursor.getDouble(15);
		      data.longitude = cursor.getDouble(16);
		      data.turn= cursor.getInt(17);
		      data.wc = cursor.getInt(18);
		      data.lift = cursor.getInt(19);
		      data.slopeCharge = cursor.getInt(20);
		      data.twoFloor = cursor.getInt(21);
		      data.cmsingal = cursor.getInt(22);
		      data.cusingal = cursor.getInt(23);
		      data.ctsingal = cursor.getInt(24);
		      data.wheel = cursor.getInt(25);
		      data.chargenot = cursor.getInt(26);
		      data.imageURL = cursor.getString(27);
		      data.newsInfo=cursor.getString(28);
		      listData.datalist.add(data);
		 }
		 cursor.close();
		 db.close();
		return listData;
	}
	public AroundParkListData getParkByStatus(){
		AroundParkListData listData = new AroundParkListData();
		 SQLiteDatabase db = getReadableDatabase();  
		 Cursor cursor = db.rawQuery("select * from around_park where 1=1 order by remainNum desc", null);
		 while (cursor.moveToNext()) {
			  AroundParkData data = new AroundParkData();
			  data.id = cursor.getInt(0);
		      data.shortName = cursor.getString(1);
		      data.shortAddres = cursor.getString(2);
		      data.price = cursor.getString(3);
		      data.total = cursor.getInt(4);
		      data.remainNum = cursor.getInt(5);
		      data.online = cursor.getInt(6);
		      data.vip1 = cursor.getInt(7);
		      data.vip2 = cursor.getInt(8);
		      data.vip3 = cursor.getInt(9);
		      data.vip4 = cursor.getInt(10);
		      data.confirm = cursor.getInt(11);
		      data.distance = cursor.getInt(12);
		      data.isFree = cursor.getInt(13);
		      data.sortPrice = cursor.getDouble(14);
		      data.latitude = cursor.getDouble(15);
		      data.longitude = cursor.getDouble(16);
		      data.turn= cursor.getInt(17);
		      data.wc = cursor.getInt(18);
		      data.lift = cursor.getInt(19);
		      data.slopeCharge = cursor.getInt(20);
		      data.twoFloor = cursor.getInt(21);
		      data.cmsingal = cursor.getInt(22);
		      data.cusingal = cursor.getInt(23);
		      data.ctsingal = cursor.getInt(24);
		      data.wheel = cursor.getInt(25);
		      data.chargenot = cursor.getInt(26);
		      data.imageURL = cursor.getString(27);
		      data.newsInfo=cursor.getString(28);
		      listData.datalist.add(data);
		 }
		 cursor.close();
		 db.close();
		return listData;
	}
	public AroundParkListData getParkByIntelligence(){
		AroundParkListData listData = new AroundParkListData();
		 SQLiteDatabase db = getReadableDatabase();  
		 Cursor cursor = db.rawQuery("select * from around_park", null);
		 while (cursor.moveToNext()) {
			  AroundParkData data = new AroundParkData();
			  data.id = cursor.getInt(0);
		      data.shortName = cursor.getString(1);
		      data.shortAddres = cursor.getString(2);
		      data.price = cursor.getString(3);
		      data.total = cursor.getInt(4);
		      data.remainNum = cursor.getInt(5);
		      data.online = cursor.getInt(6);
		      data.vip1 = cursor.getInt(7);
		      data.vip2 = cursor.getInt(8);
		      data.vip3 = cursor.getInt(9);
		      data.vip4 = cursor.getInt(10);
		      data.confirm = cursor.getInt(11);
		      data.distance = cursor.getInt(12);
		      data.isFree = cursor.getInt(13);
		      data.sortPrice = cursor.getDouble(14);
		      data.latitude = cursor.getDouble(15);
		      data.longitude = cursor.getDouble(16);
		      data.turn= cursor.getInt(17);
		      data.wc = cursor.getInt(18);
		      data.lift = cursor.getInt(19);
		      data.slopeCharge = cursor.getInt(20);
		      data.twoFloor = cursor.getInt(21);
		      data.cmsingal = cursor.getInt(22);
		      data.cusingal = cursor.getInt(23);
		      data.ctsingal = cursor.getInt(24);
		      data.wheel = cursor.getInt(25);
		      data.chargenot = cursor.getInt(26);
		      data.imageURL = cursor.getString(27);
		      data.newsInfo=cursor.getString(28);
		      listData.datalist.add(data);
		 }
		 cursor.close();
		 db.close();
		return listData;
	}
	
	public void clearPark(){
		Log.i("tag", "进入删除");
		SQLiteDatabase db = getWritableDatabase();
		db.delete("around_park", null, null);
		db.close();
	}
	
	public long insertAlarmColock(String[] strArray)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues conv = new ContentValues();
		conv.put("_id", strArray[0]);
		conv.put("userid", strArray[1]);
		conv.put("alarmtime", strArray[2]);
		conv.put("alarmrepeat", strArray[3]);
		conv.put("alarmisopen", strArray[4]);
		return db.insert("alarmcolock", null, conv);
	}
	
	public Cursor selectAlarmColock(){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query("alarmcolock", null, null, null, null, null, null);
		return cursor;
	}
	
	public Cursor getAlarmColock(String userid,String pid){
		SQLiteDatabase db = this.getReadableDatabase();
		String where ="_id=?and userid=?";
		String[] whereValues = {pid,userid};
		Cursor cursor = db.query("alarmcolock", null, where, whereValues, null, null, null);
		return cursor;
	}
	
	public void deleteAlarmColock(String userid,String pid){
		SQLiteDatabase db = this.getWritableDatabase();
		String where = "_id=?and userid=?";
		String[] whereValues = {pid,userid};
		db.delete("alarmcolock", where, whereValues);
	}
	
	public int updateAlarmColock(String userid,String pid,String[] strArray){
		SQLiteDatabase db = this.getWritableDatabase();
		String where = "_id=?and userid=?";
		String[] whereValues = {pid,userid};
		ContentValues cv = new ContentValues();
		cv.put("_id", strArray[0]);
		cv.put("userid", strArray[1]);
		cv.put("alarmtime", strArray[2]);
		cv.put("alarmrepeat", strArray[3]);
		cv.put("alarmisopen", strArray[4]);	
		return db.update("alarmcolock", cv, where, whereValues);
	}

}
