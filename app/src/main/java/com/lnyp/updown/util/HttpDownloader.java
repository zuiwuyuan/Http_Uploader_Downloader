package com.lnyp.updown.util;


import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 下载工具类
 */
public class HttpDownloader {

    private String SDPATH;

    public HttpDownloader() {
        //得到当前外部存储设备的目录( /SDCARD )
        SDPATH = Environment.getExternalStorageDirectory() + "/";
    }

    private URL url = null;

    /**
     * 根据URL下载文件,前提是这个文件当中的内容是文本,函数的返回值就是文本当中的内容
     * 1.创建一个URL对象
     * 2.通过URL对象,创建一个HttpURLConnection对象
     * 3.得到InputStream
     * 4.从InputStream当中读取数据
     *
     * @param urlStr
     * @return
     */
    public String download(String urlStr) {
        StringBuffer sb = new StringBuffer();
        String line = null;
        BufferedReader buffer = null;
        try {
            url = new URL(urlStr);
            //根据URL取得与资源提供的服务器的连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            //将连接流管道成BufferedReader
            buffer = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            //利用BufferedReader逐行读取文本信息,并添加到StringBuffer中
            while ((line = buffer.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (buffer != null) {
                    buffer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //将读取的文本信息以String的形式输出
        return sb.toString();
    }

    /**
     * @param urlStr   文件地址
     * @param path     文件保存路径
     * @param fileName 文件名
     * @return 文件的绝对路径
     */
    public String downFile(String urlStr, String path, String fileName) {

        InputStream inputStream = null;
        String filePath = null;

        try {
            FileUtils fileUtils = new FileUtils();
            //判断文件是否存在
            if (fileUtils.isFileExist(path + fileName)) {
                System.out.println("exits");
                filePath = SDPATH + path + fileName;
            } else {
                //得到io流
                inputStream = getInputStreamFromURL(urlStr);
                //从input流中将文件写入SD卡中
                File resultFile = fileUtils.write2SDFromInput(path, fileName, inputStream);
                if (resultFile != null) {

                    filePath = resultFile.getPath();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return filePath;
    }

    /**
     * 根据URL得到输入流
     *
     * @param urlStr
     * @return
     */
    public InputStream getInputStreamFromURL(String urlStr) {

        HttpURLConnection urlConn;
        InputStream inputStream = null;
        try {
            url = new URL(urlStr);
            urlConn = (HttpURLConnection) url.openConnection();
            inputStream = urlConn.getInputStream();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return inputStream;
    }

}
