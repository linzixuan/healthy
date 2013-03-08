package com.zerodo.base.db.helper;

import java.io.InputStream;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zerodo.activity.R;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

	private Class<?>[] modelClasses;
	private Context context;
	public DBHelper(Context context, String databaseName,
			SQLiteDatabase.CursorFactory factory, int databaseVersion,
			Class<?>[] modelClasses) {
		super(context, databaseName, factory, databaseVersion);
		this.context=context;
		this.modelClasses = modelClasses;
	}

	public void onCreate(SQLiteDatabase db) {
		TableHelper.createTablesByClasses(db, this.modelClasses,context);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		try {
			JSONObject fileObject=getJsonFromLocal(context.getString(R.string.json_update_fileName), context);
			JSONArray versionArray = fileObject.getJSONArray(context.getString(R.string.json_update_versionTag));
			for(int i=0;i<versionArray.length();i++){
				JSONObject versionObject = (JSONObject)versionArray.opt(i); 
				int version=Integer.valueOf(versionObject.getString(context.getString(R.string.json_update_version)));
				if(version>oldVersion&&version<=newVersion){
					JSONArray sqlArray = versionObject.getJSONArray(context.getString(R.string.json_update_sqlTag));
					for(int m=0;m<sqlArray.length();m++){
						String insertSql=sqlArray.getString(m);
						db.execSQL(insertSql);
					}
				}
			}
			
		} catch (JSONException e) {
			db.setVersion(oldVersion);
			e.printStackTrace();
		}
	}
	//¶ÁÈ¡jsonÎÄ¼þ
	public static JSONObject getJsonFromLocal(String fileName,Context context){	
		JSONObject jsonObject = null; 
		String res;
		try{ 
			AssetManager am = context.getAssets();   
		    InputStream is = am.open(fileName);   
		    int length = is.available(); 
		    byte [] buffer = new byte[length]; 
		    is.read(buffer);     
		    res = EncodingUtils.getString(buffer, "UTF-8"); 
		    is.close(); 
		    jsonObject=new JSONObject(res);
		}catch(Exception e){ 
			e.printStackTrace(); 
		} 
		return jsonObject;
	}
}
