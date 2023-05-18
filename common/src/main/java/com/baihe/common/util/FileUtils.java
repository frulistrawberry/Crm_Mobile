package com.baihe.common.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Author：xubo
 * Time：2019-06-03
 * Description：文件工具类
 */
public class FileUtils {
    /**
     * 删除文件/文件夹
     * @param file
     * @return
     */
    public static boolean deleteFile(File file) {
        if (!file.exists()) {
            return false;
        }
        if (file.isFile()) {
            file.delete();
            return true;
        } else {
            File[] childFiles = file.listFiles();
            for (int i = 0; i < childFiles.length; i++) {
                File childFile = childFiles[i];
                deleteFile(childFile);
            }
            file.delete();
            return true;
        }
    }

    /**
     * 文本写入指定文件
     * @param file
     * @param text
     */
    public static void writeTextToFile(File file, String text) {
        BufferedWriter bfw = null;
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            OutputStreamWriter fos = new OutputStreamWriter(new FileOutputStream(file, true));
            bfw = new BufferedWriter(fos);
            bfw.write(text);
            bfw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bfw != null) {
                try {
                    bfw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }



}
