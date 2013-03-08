package com.zerodo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import android.util.Base64;

import com.zerodo.base.util.CryptorFun;
import com.zerodo.base.util.RSAUtils;



public class test {
	public static String sPubkey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCNDySvNmCaiyJTTQelrUylzNHzlX9VFGcU1lX0DP38dV6B7H/nrd6oqMpqlCyEs/kCn+6b1fHMXxp/GWmPVQq07qbUEE0vnORA/rfg9ljYjrK1/BQ42VKTaNWYXmVNd94zRkZ/HnkIENwgV6XxrW+Dn8bAALS/vN7pQEkVMe+CMwIDAQAB";

	public static void main(String[] args) {
		try {
			
			String sRegKey="UWqZTmvzauk0CPUu+0Qy0Jm8AyJQUPS94wzsNZsyGSJD+NLJyo9jP+FF+4DsntAost4T4EGwjOfUONwSw6z27d7bOGoegwLIHKRnV+CfGmDblkWb4JltrQAKGezXCMCNG/OarX8Eu7ceamQ3hYlBjnadKaTobvNi5b35qOY1oiQ=";
     		String mac="380A948E4440";
     		String imei="356754047923943";
			RSAUtils.Cipher_ALGORITHM = RSAUtils.NoPadding;
     		String realKey=new String(RSAUtils.decryptByPublicKey(Base64.decode(sRegKey, Base64.DEFAULT),sPubkey));
     		System.out.println(realKey);
     		System.out.println(CryptorFun.Encrypt(imei.substring(5)+mac.substring(0, mac.length()-3), "SHA"));
     			
			
			
			/**
			 * 压缩文件
			 */
//			File[] files = new File[] { new File("E:/healthy")};
//			File zip = new File("e:/healthy.back");
//			ZipFiles(zip, "", files);

			/**
			 * 解压文件
			 */
//			File zipFile = new File("e:/healthy.back");
//			String path = "E:/";
//			unZipFiles(zipFile, path);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void ZipFiles(File zip, String path, File... srcFiles)
			throws IOException {
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zip));
		ZipFiles(out, path, srcFiles);
		out.close();
		System.out.println("*****************压缩完毕*******************");
	}

	public static void ZipFiles(ZipOutputStream out, String path,
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
	public static void unZipFiles(String zipPath, String descDir)
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
	public static void unZipFiles(File zipFile, String descDir)
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
			;
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
}
