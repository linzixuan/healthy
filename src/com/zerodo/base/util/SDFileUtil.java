package com.zerodo.base.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class SDFileUtil {

	private String SDPATH;

	public String getSDPATH() {
		return SDPATH;
	}

	public SDFileUtil() {
		// �õ���ǰ�ⲿ�洢�豸��Ŀ¼
		// /SDCARD
		SDPATH = Environment.getExternalStorageDirectory() + "/";
	}

	/**
	 * ��SD���ϴ����ļ�
	 * 
	 * @throws IOException
	 */
	public File creatSDFile(String fileName) throws IOException {
		File file = new File(SDPATH + fileName);
		file.createNewFile();
		return file;
	}

	/**
	 * ��SD���ϴ���Ŀ¼
	 * 
	 * @param dirName
	 */
	public File creatSDDir(String dirName) {
		File dir = new File(SDPATH + dirName);
		dir.mkdirs();
		return dir;
	}

	/**
	 * �ж�SD���ϵ��ļ����Ƿ����
	 */
	public boolean isFileExist(String fileName) {
		File file = new File(SDPATH + fileName);
		return file.exists();
	}
	/**
	 * ��һ��Bitmap���������д�뵽SD����
	 */
	public File write2SDFromBitmap(String path, String fileName, Bitmap bitmap) {
		File file = null;
		try {
			creatSDDir(path);
			file = creatSDFile(path + fileName);
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(file));
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
			bos.flush();
			bos.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return file;
	}
	
	public Bitmap readFromSD2Bitmap(String path){
		Bitmap bitmap=null;
		try{
			BitmapFactory.Options opts = new BitmapFactory.Options();   
			opts.inSampleSize = 2;   
			bitmap=BitmapFactory.decodeFile(SDPATH+path,opts);
//			bitmap=BitmapFactory.decodeFile(SDPATH+path);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * ��һ��InputStream���������д�뵽SD����
	 */
	public File write2SDFromInput(String path, String fileName,
			InputStream input) {
		File file = null;
		OutputStream output = null;
		try {
			creatSDDir(path);
			file = creatSDFile(path + fileName);
			output = new FileOutputStream(file);
			byte buffer[] = new byte[4 * 1024];
			while ((input.read(buffer)) != -1) {
				output.write(buffer);
			}
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}
	
	public void deleteFile(String filePathAndName){
		File file = new File(SDPATH + filePathAndName);
		if(null!=file)file.delete();
	}
}
