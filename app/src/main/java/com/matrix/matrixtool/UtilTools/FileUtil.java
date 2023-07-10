package com.matrix.matrixtool.UtilTools;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName FileUtil
 * @Author Create By matrix
 * @Date 2023/5/23 0023 12:10
 */
public class FileUtil {

    /**
     * 写入文件
     */
    public static void GetWriteFile(String fileContent,String filePath,boolean isCover){
        try {
            //String mFilePath="/sdcard/Download/forecast.txt";
            createFileRecursion(filePath, 0);
            FileWriter fileWriter = new FileWriter(filePath,isCover);//覆盖写入:isCover是false
            fileWriter.write(fileContent+"\n");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * 保存文件
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void createFileRecursion(String fileName, Integer height) throws IOException {
        Path path = Paths.get(fileName);
        if (Files.exists(path)) {
            return;
        }
        if (Files.exists(path.getParent())) {
            if (height == 0) {
                Files.createFile(path);
            } else {
                Files.createDirectory(path);
            }
        } else {
            createFileRecursion(path.getParent().toString(), height + 1);
            createFileRecursion(fileName, height);
        }
    }

    /***
     * 读取文件
     */
    public static String readFileRecursion(String fileNamePath) throws IOException {
        BufferedReader bf= new BufferedReader(new FileReader(fileNamePath));
        String str_temp="",str_result="";
        while ((str_temp = bf.readLine())!= null) // 判断最后一行不存在，为空结束循环
        {
            str_result+=str_temp;
            //System.out.println(str_temp);//原样输出读到的内容
        }
        bf.close();

        return str_result;
    }

    /** 删除单个文件
     * @param filePath 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * toString转Map
     */
    public static Map<String,Object> mapStringToMap(String stringMap){
        Map<String,Object> map = new HashMap<>();
        String[] strings = stringMap.split(",");
        for (String str : strings) {
            String[] s = str.split("=");
            map.put(s[0],s[1]);
        }
        return map;
    }
}
