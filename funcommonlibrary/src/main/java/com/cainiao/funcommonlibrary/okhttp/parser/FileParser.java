package com.cainiao.funcommonlibrary.okhttp.parser;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Response;

public class FileParser extends BaseParser<File> {

    protected File destFile;

    public FileParser(File destFile){
        this.destFile = destFile;
    }


    @Override
    protected File parse(Response response) throws Exception {
        if(destFile == null || destFile.isDirectory()){
            return null;
        }

        InputStream is = null;
        byte[] buf = new byte[2048];
        int len;
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            if (!destFile.exists()) {
                if(!destFile.createNewFile()){
                    return null;
                }
            }
            fos = new FileOutputStream(destFile);
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            return destFile;
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
            }
        }
    }
}
