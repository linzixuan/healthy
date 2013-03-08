package com.zerodo.base.db.helper;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Blob;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;

import com.zerodo.activity.R;
import com.zerodo.base.db.annotation.Column;
import com.zerodo.base.db.annotation.Id;
import com.zerodo.base.db.annotation.Table;

public class TableHelper {
	private static final String FILENAME="table";
	public static <T> void createTablesByClasses(SQLiteDatabase db,
			Class<?>[] clazzs,Context context) {
		for (Class clazz : clazzs)
			createTable(db, clazz,context);
	}

	public static <T> void dropTablesByClasses(SQLiteDatabase db,
			Class<?>[] clazzs) {
		for (Class clazz : clazzs)
			dropTable(db, clazz);
	}

	public static <T> void createTable(SQLiteDatabase db, Class<T> clazz,Context context) {
		String tableName = "";
		boolean init=false;
		if (clazz.isAnnotationPresent(Table.class)) {
			Table table = (Table) clazz.getAnnotation(Table.class);
			tableName = table.name();
			init=table.init();
			System.out.println("tableName:" + tableName);
		}

		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE ").append(tableName).append(" (");

		Field[] fields = clazz.getDeclaredFields();
		System.out.println(fields.length);
		for (Field field : fields) {
			System.out.println(field.getName());
			if (!(field.isAnnotationPresent(Column.class))) {
				continue;
			}
			Column column = (Column) field.getAnnotation(Column.class);
			String columnType = "";
			if (column.type().equals(""))
				columnType = getColumnType(field.getType());
			else {
				columnType = column.type();
			}
			sb.append(column.name() + " " + columnType);
			if ((!(column.type().equals(""))) && (column.length() != 0)) {
				sb.append("(" + column.length() + ")");
			}
			if (((field.isAnnotationPresent(Id.class)) && (field.getType() == Integer.TYPE))
					|| (field.getType() == Integer.class))
				sb.append(" primary key autoincrement");
			else if (field.isAnnotationPresent(Id.class)) {
				sb.append(" primary key");
			}
			sb.append(", ");
		}

		sb.delete(sb.length() - 2, sb.length() - 1);
		sb.append(")");
		String sql = sb.toString();
		System.out.println("sql:" + sql);
		db.execSQL(sql);
		if(init){
			try {
				JSONObject fileObject=getJsonFromLocal(context.getString(R.string.json_init_fileName), context);
				JSONArray tableArray = fileObject.getJSONArray(context.getString(R.string.json_init_tableTag));
				for(int i=0;i<tableArray.length();i++){
					JSONObject tableObject = (JSONObject)tableArray.opt(i); 
					if(tableName.equals(tableObject.getString(context.getString(R.string.json_init_tableName)))){
						JSONArray sqlArray = tableObject.getJSONArray(context.getString(R.string.json_init_sqlTag));
						for(int m=0;m<sqlArray.length();m++){
							String insertSql=sqlArray.getString(m);
							db.execSQL(insertSql);
						}
					}
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
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
	
	public static <T> void dropTable(SQLiteDatabase db, Class<T> clazz) {
		String tableName = "";
		if (clazz.isAnnotationPresent(Table.class)) {
			Table table = (Table) clazz.getAnnotation(Table.class);
			tableName = table.name();
			System.out.println("tableName:" + tableName);
		}
		String sql = "DROP TABLE IF EXISTS " + tableName;
		db.execSQL(sql);
	}

	private static String getColumnType(Class fieldType) {
		if (String.class == fieldType) {
			return "TEXT";
		}
		if ((Integer.TYPE == fieldType) || (Integer.class == fieldType)) {
			return "INTEGER";
		}
		if ((Long.TYPE == fieldType) || (Long.class == fieldType)) {
			return "BIGINT";
		}
		if ((Float.TYPE == fieldType) || (Float.class == fieldType)) {
			return "FLOAT";
		}
		if ((Short.TYPE == fieldType) || (Short.class == fieldType)) {
			return "INT";
		}
		if ((Double.TYPE == fieldType) || (Double.class == fieldType)) {
			return "DOUBLE";
		}
		if (Blob.class == fieldType) {
			return "BLOB";
		}

		return "TEXT";
	}
}
