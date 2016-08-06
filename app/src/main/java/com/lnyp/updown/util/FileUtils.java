package com.lnyp.updown.util;


import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {
	/**
	 * SD卡的路径
	 */
	private String SDPATH;
	/**
	 * 设置最大的文件大小
	 */
	private int FILESIZE = 10 * 1024;

	public String getSDPATH(){
		return SDPATH;
	}

	public FileUtils(){
		//得到当前外部存储设备的目录( /SDCARD )
		SDPATH = Environment.getExternalStorageDirectory() + "/";
	}

	/**
	 * 在SD卡上创建文件
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public File createSDFile(String fileName) throws IOException{
		File file = new File(SDPATH + fileName);
		file.createNewFile();
		return file;
	}

	/**
	 * 在SD卡上创建目录
	 * @param dirName
	 * @return
	 */
	public File createSDDir(String dirName){
		File dir = new File(SDPATH + dirName);
		dir.mkdir();
		return dir;
	}

	/**
	 * 判断SD卡上的文件夹是否存在
	 * @param fileName
	 * @return
	 */
	public boolean isFileExist(String fileName){
		File file = new File(SDPATH + fileName);
		return file.exists();
	}

	/**
	 * 将一个InputStream里面的数据写入到SD卡中
	 * @param path
	 * @param fileName
	 * @param input
	 * @return
	 */
	public File write2SDFromInput(String path, String fileName,
								  InputStream input) {
		File file = null;
		OutputStream output = null;
		try {
			// 创建文件夹
			createSDDir(path);
			// 创建文件
			file = createSDFile(path + fileName);

			// 开启输出流，准备写入文件
			output = new FileOutputStream(file);
			// 缓冲区
			byte[] buffer = new byte[FILESIZE];
			int count;
			while ((count = input.read(buffer)) != -1) {
				output.write(buffer, 0, count);
			}
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

}
