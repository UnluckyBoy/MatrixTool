package com.matrix.matrixtool.UtilTools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @ClassName StreamTool
 * @Author Create By Administrator
 * @Date 2023/4/9 0009 18:58
 */


public class StreamTool {
    /*
     * 从数据流中获得数据
     */
    public static byte[] readInputStream(InputStream inputStream)throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }
}
