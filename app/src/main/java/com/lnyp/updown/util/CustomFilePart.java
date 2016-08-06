package com.lnyp.updown.util;

import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.util.EncodingUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 解决中文文件名乱码
 */
public class CustomFilePart extends FilePart {
    public CustomFilePart(String filename, File file)
            throws FileNotFoundException {
        super(filename, file);
    }

    protected void sendDispositionHeader(OutputStream out) throws IOException {
        super.sendDispositionHeader(out);
        String filename = getSource().getFileName();
        if (filename != null) {
            out.write(EncodingUtil.getAsciiBytes(FILE_NAME));
            out.write(QUOTE_BYTES);
            out.write(EncodingUtil.getBytes(filename, "UTF-8"));
            out.write(QUOTE_BYTES);
        }
    }
}