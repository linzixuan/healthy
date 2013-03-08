package com.zerodo.backup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.zip.ZipOutputStream;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import com.zerodo.base.util.StringUtil;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

public class BackupTask extends AsyncTask<String, Void, Integer> {
	private static final String COMMAND_BACKUP = "backup";
	public static final String COMMAND_RESTORE = "restroe";
	private static final int BACKUP_SUCCESS = 1;
	private static final int RESTORE_SUCCESS = 2;
	private static final int BACKUP_ERROR = 3;
	private static final int RESTORE_NOFLEERROR = 4;
	private static final int SD_DISABLE = 5;
	private Context myContext;
	private String fileName;
	AlertDialog.Builder builder;
	Dialog dialog;
	static int total = 0;

	public BackupTask(Context context,String fileName) {
		this.myContext = context;
		this.fileName=fileName;
		builder = new Builder(myContext);
		builder.setMessage("处理中。。。请稍后");
		dialog = builder.show();
	}

	@Override
	protected Integer doInBackground(String... params) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File dbFile = myContext.getDatabasePath("healthy.db");
			// 创建目录
			File exportDir = new File(
					Environment.getExternalStorageDirectory(), "healthy");
			File zipDir= new File(
					Environment.getExternalStorageDirectory(), "dohealthy");
			if (!exportDir.exists()) {
				exportDir.mkdirs();
			}
			if (!zipDir.exists()) {
				zipDir.mkdirs();
			}
			File backup = new File(exportDir, dbFile.getName());
			File zip;
			if(StringUtil.isNull(fileName)){
				SimpleDateFormat df=new SimpleDateFormat("yyyyMMddHHmmss");
				String time=df.format(new Date());
				zip= new File(zipDir, "healthy"+time+".backup");
			}else{
				zip= new File(zipDir, fileName);
			}
			
			String command = params[0];
			if (command.equals(COMMAND_BACKUP)) {
				try {
					backup.createNewFile();
					fileCopy(dbFile, backup);
					File[] files = new File[] { exportDir };
					ZipFiles(zip, "", files);
					return BACKUP_SUCCESS;
				} catch (Exception e) {
					e.printStackTrace();
					return BACKUP_ERROR;
				}
			} else if (command.equals(COMMAND_RESTORE)) {
				try {
					String path = Environment.getExternalStorageDirectory()
							+ "/";
					unZipFiles(zip, path);
					fileCopy(backup, dbFile);
					return RESTORE_SUCCESS;
				} catch (Exception e) {
					e.printStackTrace();
					return RESTORE_NOFLEERROR;
				}
			} else {
				return null;
			}
		} else {
			return SD_DISABLE;
		}
	}

	private void fileCopy(File dbFile, File backup) throws IOException {
		FileChannel inChannel = new FileInputStream(dbFile).getChannel();
		FileChannel outChannel = new FileOutputStream(backup).getChannel();
		try {
			inChannel.transferTo(0, inChannel.size(), outChannel);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inChannel != null) {
				inChannel.close();
			}
			if (outChannel != null) {
				outChannel.close();
			}
		}
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		switch (result) {
		case BACKUP_SUCCESS:
			dialog.dismiss();
			Toast.makeText(myContext, "数据备份成功，备份文件存放于SD/dohealthy/目录下，可直接拷贝到其他手机中", 1).show();
			Intent backupIntent = new Intent("backup");   
			myContext.sendBroadcast(backupIntent);
			break;
		case BACKUP_ERROR:
			dialog.dismiss();
			Toast.makeText(myContext, "备份失败，请重试！", 1).show();
			break;
		case RESTORE_SUCCESS:
			dialog.dismiss();
			Toast.makeText(myContext, "数据还原成功", 1).show();
			break;
		case RESTORE_NOFLEERROR:
			dialog.dismiss();
			Toast.makeText(myContext, "还原失败，找不到备份文件！", 1).show();
			break;
		case SD_DISABLE:
			dialog.dismiss();
			Toast.makeText(myContext, "SD卡不可用，请重试！", 1).show();
			break;
		default:
			break;
		}
	}

	private static void ZipFiles(File zip, String path, File... srcFiles)
			throws IOException {
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zip));
		ZipFiles(out, path, srcFiles);
		out.close();
		System.out.println("*****************压缩完毕*******************");
	}

	private static void ZipFiles(ZipOutputStream out, String path,
			File... srcFiles) {
		path = path.replaceAll("\\*", "/");
		if (!path.endsWith("/")) {
			path += "/";
		}
		byte[] buf = new byte[1024];
		try {
			for (int i = 0; i < srcFiles.length; i++) {
				if (srcFiles[i].isDirectory()) {
					File[] files = srcFiles[i].listFiles();
					String srcPath = srcFiles[i].getName();
					srcPath = srcPath.replaceAll("\\*", "/");
					if (!srcPath.endsWith("/")) {
						srcPath += "/";
					}
					out.putNextEntry(new ZipEntry(path + srcPath));
					ZipFiles(out, path + srcPath, files);
				} else {
					FileInputStream in = new FileInputStream(srcFiles[i]);
					System.out.println(path + srcFiles[i].getName());
					out.putNextEntry(new ZipEntry(path + srcFiles[i].getName()));
					int len;
					while ((len = in.read(buf)) > 0) {
						out.write(buf, 0, len);
					}
					out.closeEntry();
					in.close();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 解压到指定目录
	 * 
	 * @param zipPath
	 * 
	 * @param descDir
	 * 
	 * @author isea533
	 */
	private static void unZipFiles(String zipPath, String descDir)
			throws IOException {
		unZipFiles(new File(zipPath), descDir);
	}

	/**
	 * 解压文件到指定目录
	 * 
	 * @param zipFile
	 * @param descDir
	 * @author isea533
	 */
	@SuppressWarnings("rawtypes")
	private static void unZipFiles(File zipFile, String descDir)
			throws IOException {
		File pathFile = new File(descDir);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}
		ZipFile zip = new ZipFile(zipFile);
		for (Enumeration entries = zip.getEntries(); entries.hasMoreElements();) {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			String zipEntryName = entry.getName();
			InputStream in = zip.getInputStream(entry);
			String outPath = (descDir + zipEntryName).replaceAll("\\*", "/");
			// 判断路径是否存在,不存在则创建文件路径
			File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
			if (!file.exists()) {
				file.mkdirs();
			}
			// 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
			if (new File(outPath).isDirectory()) {
				continue;
			}
			// 输出文件路径信息
			System.out.println(outPath);

			OutputStream out = new FileOutputStream(outPath);
			byte[] buf1 = new byte[1024];
			int len;
			while ((len = in.read(buf1)) > 0) {
				out.write(buf1, 0, len);
			}
			in.close();
			out.close();
		}
		System.out.println("******************解压完毕********************");
	}

	public int getFileSize(File f) throws Exception// 取得文件夹大小
	{
		int size = 0;
		File flist[] = f.listFiles();
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFileSize(flist[i]);
			} else {
				size = size
						+ Integer.parseInt(String.valueOf(flist[i].length()));
			}
		}
		return size;
	}
}
