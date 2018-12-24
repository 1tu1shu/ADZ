package com.tushu.sdk.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;

public class IOUtil {

    public static String readPath(String path){
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        FileInputStream inputStream = null;
        byte[] buffer = new byte[1024];
        int length;
        try {
            inputStream = new FileInputStream(path);
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            return result.toString("utf-8");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(null!=inputStream) inputStream.close();
                if(null!=result) result.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }




}
