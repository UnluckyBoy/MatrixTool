package com.matrix.matrixtool.UtilTools;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName TimeTool
 * @Author Create By Administrator
 * @Date 2023/4/9 0009 23:32
 */
public class TimeTool {
    public static String GetSystemTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");// HH:mm:ss
        // 获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }
}
