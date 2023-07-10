package com.matrix.matrixtool.UtilTools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.matrix.matrixtool.UiTools.MatrixToast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;

/**
 * @ClassName GetBitMap
 * @Author Create By Administrator
 * @Date 2023/4/8 0008 23:53
 */

/**
 * 打开线程将url的图片显示到ImageView
 */
public class ImageTool{
    private static byte[] data;
    private static Bitmap bitmap;
    private static Handler handler = null;

    public static void SetImageView(ImageView imageView,String url){
        // 创建属于主线程的handler
        handler = new Handler();
        /*在新开的线程中设置图片显示*/
        Runnable runnable = new Runnable() {
            public void run() {
                imageView.setImageBitmap(bitmap);
            }
        };
        new Thread() {
            public void run() {
                try {
                    // 通过url获得图片数据
                    data = GetUserHead(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                //子线程runnable将会执行
                handler.post(runnable);
            }
        }.start();
    }

    /**
     * @param urlpath String类型
     * @return byte[]类型
     * @throws IOException
     */
    public static byte[] GetUserHead(String urlpath) throws IOException {
        URL url = new URL(urlpath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET"); // 设置请求方法为GET
        conn.setReadTimeout(5 * 1000); // 设置请求过时时间为5秒
        InputStream inputStream = conn.getInputStream(); // 通过输入流获得图片数据
        byte[] data = StreamTool.readInputStream(inputStream); // 获得图片的二进制数据
        return data;
    }

    /**
     * 保存bitmap到本地
     * @param bitmap Bitmap
     */
    public static void saveBitmap(Bitmap bitmap, String path, Context mContext,String fileName) {
        String savePath;
        File filePic;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            savePath = path;
        } else {
            //Log.e("tag", "saveBitmap failure : sdcard not mounted");
            return;
        }
        try {
            filePic = new File(savePath,fileName);
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic+".png");
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            //Toast.makeText(mContext, "保存成功!!!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("tag", "saveBitmap: " + e.getMessage());
            MatrixToast.showToast(mContext, "保存失败!!!",Toast.LENGTH_SHORT);
            return;
        }
        Log.i("tag", "saveBitmap success: " + filePic.getAbsolutePath());
        MatrixToast.showToast(mContext, "保存成功!!!", Toast.LENGTH_SHORT);
    }

    /**
     * 压缩保存
     * @param bitmap
     * @param path
     * @param mContext
     * @param fileName
     */
    public static void compress_SaveBitmap(Bitmap bitmap, String path, Context mContext,String fileName) {
        String savePath;
        File filePic;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            savePath = path;
        } else {
            //Log.e("tag", "saveBitmap failure : sdcard not mounted");
            return;
        }
        try {
            filePic = new File(savePath,fileName);
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic+".jpg");
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, fos);
            fos.flush();
            fos.close();
            //Toast.makeText(mContext, "保存成功!!!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("tag", "saveBitmap: " + e.getMessage());
            MatrixToast.showToast(mContext, "保存失败!!!",Toast.LENGTH_SHORT);
            return;
        }
        Log.i("tag", "saveBitmap success: " + filePic.getAbsolutePath());
        MatrixToast.showToast(mContext, "保存成功!!!", Toast.LENGTH_SHORT);
    }

    /**
     * 通过path将图片转base64编码
     * @param path
     * @return
     */
    public static String getImagePath2BaseCode(String path,boolean add_head){
        File file=new File(path);
        byte[] data = null;
        try {
            InputStream in = new FileInputStream(file);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String filename=file.getName();
        String suffixName=filename.substring(filename.lastIndexOf(".")+1);//获取没有.的后缀名
        String imageBaseType="data:image/"+suffixName+";base64,";
        Base64.Encoder encoder = Base64.getEncoder();
        if(add_head){
            return imageBaseType+encoder.encodeToString(data);
        }else{
            return encoder.encodeToString(data);//若加入图片头,则使用:imageBaseType+encoder.encodeToString(data)
        }
    }

    /**将base64code进行进行urlencode**/
    public static String getBase2Urlencode(String base64code){
        String result=null;
        try {
            result=URLEncoder.encode(base64code, "utf-8");//将base64进行urlencode
            //String result4 = URLDecoder.decode(result, "utf-8");//URLDecoder解码
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /***
     * 获取文字识别规定body类型
     * @param path
     * @return
     */
    public static String getRecognitionImageBase(String path){
        String base64code=getImagePath2BaseCode(path,false);
        return getBase2Urlencode(base64code);
    }


    /**
     * 通过路径读取图片并转bitmap
     * @param path
     * @return
     */
    public static Bitmap imagePath2Bitmap(String path){
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            Log.e("msg","加载错误"+e.getMessage());
        }
        Bitmap bitmap  = BitmapFactory.decodeStream(fis);
        return bitmap;
    }

    /**
     * bitmap转为base64
     * @param path
     * @return
     */
    public static String bitmap2Base64(String  path,boolean add_head) {
        Bitmap bitmap=imagePath2Bitmap(path);//通过path获取图片内容,转Bitmap
        //bitmap=sizeScaleCompressBitmap(bitmap,450,450);//压缩
        bitmap=compressImage(bitmap);
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                baos.flush();
                baos.close();
                byte[] bitmapBytes = baos.toByteArray();
                Base64.Encoder encoder = Base64.getEncoder();
                result=encoder.encodeToString(bitmapBytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(add_head){
            return "data:image/jpg;base64,"+result;//加入图片头
        }else{
            return result;
        }
    }

    /**
     * base64转为bitmap
     * @param base64Data
     * @return
     */
    public static Bitmap base642Bitmap(String base64Data) {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] bytes = decoder.decode(base64Data);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    //region VARIABLES android四种压缩图片方法

    /**
     * 图片压缩：质量压缩方法
     * @param beforeBitmap 要压缩的图片
     * @return 压缩后的图片
     */
    public static Bitmap compressImage(Bitmap beforeBitmap) {
        // 可以捕获内存缓冲区的数据,转换成字节数组
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        if (beforeBitmap != null) {
            // 第一个参数:图片压缩的格式;第二个参数:压缩的比率;第三个参数:压缩的数据存放到bos中
            beforeBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            // 循环判断压缩后的图片大小是否满足要求,这里限制8192kb,即8M,若不满足则继续压缩,每次递减10%压缩
            int options = 90;
            while (bos.toByteArray().length / 1024 > 8192) {
                bos.reset();//置空bos
                beforeBitmap.compress(Bitmap.CompressFormat.JPEG, options, bos);//压缩options%,把压缩后的数据存放到bos中
                options -= 10;
            }
            //从bos中将数据读出来转换成图片
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            Bitmap afterBitmap = BitmapFactory.decodeStream(bis);
            return afterBitmap;
        }
        return null;
    }

    /***
     * 自定义图片压缩：质量压缩方法
     * @param beforeBitmap 压缩前图片
     * @param tarSize 目标大小,单位为kb
     * @return 压缩后图片
     */
    public static Bitmap customCompressImage(Bitmap beforeBitmap,long tarSize) {
        // 可以捕获内存缓冲区的数据,转换成字节数组
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        if (beforeBitmap != null) {
            // 第一个参数:图片压缩的格式;第二个参数:压缩的比率;第三个参数:压缩的数据存放到bos中
            beforeBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            // 循环判断压缩后的图片大小是否满足要求,这里限制8192kb,即8M,若不满足则继续压缩,每次递减10%压缩
            int options = 90;
            while (bos.toByteArray().length / 1024 > tarSize) {
                bos.reset();//置空bos
                beforeBitmap.compress(Bitmap.CompressFormat.JPEG, options, bos);//压缩options%,把压缩后的数据存放到bos中
                options -= 10;
            }
            //从bos中将数据读出来转换成图片
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            Bitmap afterBitmap = BitmapFactory.decodeStream(bis);
            return afterBitmap;
        }
        return null;
    }

    /**
     * 图片压缩：获得缩略图
     * @param beforeBitmap 要压缩的图片
     * @param width 缩略图宽度
     * @param height 缩略图高度
     * @return 压缩后的图片
     */
    public static Bitmap getThumbnail(Bitmap beforeBitmap, int width, int height) {
        return ThumbnailUtils.extractThumbnail(beforeBitmap, width, height);
    }

    /**
     * 图片压缩: 按尺寸压缩
     * @param beforeBitmap 要压缩的图片
     * @param newWidth 压缩后的宽度
     * @param newHeight 压缩后的高度
     * @return 压缩后的图片
     */
    public static Bitmap sizeCompressBitmap(Bitmap beforeBitmap, double newWidth, double newHeight) {
        // 图片原有的宽度和高度
        float beforeWidth = beforeBitmap.getWidth();
        float beforeHeight = beforeBitmap.getHeight();
        // 计算宽高缩放率
        float scaleWidth = 0;
        float scaleHeight = 0;
        if (beforeWidth > beforeHeight) {
            scaleWidth = ((float) newWidth) / beforeWidth;
            scaleHeight = ((float) newHeight) / beforeHeight;
        } else {
            scaleWidth = ((float) newWidth) / beforeHeight;
            scaleHeight = ((float) newHeight) / beforeWidth;
        }

        // 矩阵对象
        Matrix matrix = new Matrix();
        // 缩放图片动作 缩放比例
        matrix.postScale(scaleWidth, scaleHeight);
        // 创建一个新的Bitmap 从原始图像剪切图像
        Bitmap afterBitmap = Bitmap.createBitmap(beforeBitmap, 0, 0,(int) beforeWidth, (int) beforeHeight, matrix, true);
        return afterBitmap;
    }

    /**
     * 图片压缩: 规定尺寸等比例压缩，宽高不能超过限制要求
     * @param beforeBitmap 要压缩的图片
     * @param maxWidth 最大宽度限制
     * @param maxHeight 最大高度限制
     * @return 压缩后的图片
     */
    public static Bitmap sizeScaleCompressBitmap(Bitmap beforeBitmap, double maxWidth, double maxHeight) {
        // 图片原有的宽度和高度
        float beforeWidth = beforeBitmap.getWidth();
        float beforeHeight = beforeBitmap.getHeight();
        if (beforeWidth <= maxWidth && beforeHeight <= maxHeight) {
            return beforeBitmap;
        }
        // 计算宽高缩放率，等比例缩放
        float scaleWidth =  ((float) maxWidth) / beforeWidth;
        float scaleHeight = ((float)maxHeight) / beforeHeight;
        float scale = scaleWidth;
        if (scaleWidth > scaleHeight) {
            scale = scaleHeight;
        }
        Log.d("BitmapUtils", "before[" + beforeWidth + ", " + beforeHeight + "] max[" + maxWidth
                + ", " + maxHeight + "] scale:" + scale);

        // 矩阵对象
        Matrix matrix = new Matrix();
        // 缩放图片动作 缩放比例
        matrix.postScale(scale, scale);
        // 创建一个新的Bitmap 从原始图像剪切图像
        Bitmap afterBitmap = Bitmap.createBitmap(beforeBitmap, 0, 0,(int) beforeWidth, (int) beforeHeight, matrix, true);
        return afterBitmap;
    }
    //endregion
}
