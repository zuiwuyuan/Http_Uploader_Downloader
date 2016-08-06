package com.lnyp.updown.util;


import android.app.Activity;
import android.text.TextUtils;
import android.widget.Toast;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;

import java.io.File;
import java.io.IOException;

/**
 * 下载工具类
 */
public class HttpDownloader {

    /**
     * @param mContext  上下文
     * @param targetUrl 文件上传地址
     * @param filePath  文件路径
     */
    private void uploadFile(final Activity mContext, String targetUrl, final String filePath) {

        System.out.println("targetUrl: " + targetUrl + "  filePath: " + filePath);

        if (TextUtils.isEmpty(filePath)) {
            Toast.makeText(mContext, "文件不存在", Toast.LENGTH_SHORT).show();
            return;
        }

        final PostMethod filePost = new PostMethod(targetUrl) {//这个用来中文乱码
            public String getRequestCharSet() {
                return "UTF-8";
            }
        };

        try {

            final HttpClient client = new HttpClient();

            File file = new File(filePath);

            if (file.exists() && file.isFile()) {

                long fileSize = file.length();

                if (fileSize >= 5 * 1024 * 1024) {
                    Toast.makeText(mContext, "文件不得大于5M", Toast.LENGTH_SHORT).show();
                    return;
                }

            } else {
                Toast.makeText(mContext, "文件不存在", Toast.LENGTH_SHORT).show();

                return;
            }

            // 上传文件和参数
            Part[] parts = new Part[]{new CustomFilePart(file.getName(), file),
                    new StringPart("filename", file.getName(), "UTF-8")};
            filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams()));

            new Thread(new Runnable() {

                @Override
                public void run() {

                    int statuscode = 0;
                    try {
                        statuscode = client.executeMethod(filePost);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    final int finalStatuscode = statuscode;

                    mContext.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (finalStatuscode == HttpStatus.SC_OK) {
                                Toast.makeText(mContext, "上传成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "上传失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }).start();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
