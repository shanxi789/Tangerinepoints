package com.mzth.tangerinepoints.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Administrator on 2017/4/22.
 */

public class Base64Util {
    /**
     * 质量压缩
     *
     * @param bm
     * @param size
     *            压缩到小于或者等于这个尺寸的图片
     * @return
     */
    public Bitmap compressImage(Bitmap bm, int size) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 50;
        while (baos.toByteArray().length / 1024 > size) { // 循环判断如果压缩后图片是否大于50kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            bm.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中

            options -= 10;// 每次都减少10
            if(options<0){
                break;
            }
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 图片按比例大小压缩方法
     *
     * @param srcPath
     * @param size
     * @param width
     * @return
     */
    public static Bitmap getImageFromPath(String srcPath, int size, float width) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;// 这里设置高度为800f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > width) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / width);
        }
        // else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
        // be = (int) (newOpts.outHeight / hh);
        // }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return new Base64Util().compressImage(bitmap, size);// 压缩好比例大小后再进行质量压缩
    }

    /**
     * 图片按比例大小压缩方法
     *
     * @param image
     * @param size
     *            缩放的后图片大小
     * @return
     */
    public Bitmap compBitmap(Bitmap image, int size) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 80;
        while (baos.toByteArray().length / 1024 > 200) {// 判断如果图片大于0.2M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩80%，把压缩后的数据存放到baos中
            options -= 10;
            if (options < 20) {
                break;
            }
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inTempStorage = new byte[100 * 1024];
        // 设置位图颜色显示优化方式
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;

        // 4.设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 320f;// 这里设置高度为800f
        float ww = 280f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        }
        // else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
        // be = (int) (newOpts.outHeight / hh);
        // }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        try {
            isBm.reset();
            baos.reset();
            isBm.close();
            baos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new Base64Util().compressImage(bitmap, size);// 压缩好比例大小后再进行质量压缩
    }

    // 图片按比例大小压缩方法
    public static Bitmap compBitmap(InputStream inputStream, int size,
                                    float width) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;// 这里设置高度为800f
        // float ww = 150f;//这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > width) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / width);
        }
        // else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
        // be = (int) (newOpts.outHeight / hh);
        // }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeStream(inputStream, null, newOpts);
        return new Base64Util().compressImage(bitmap, size);// 压缩好比例大小后再进行质量压缩
    }
    /**
     * 将file文件转化为byte数组
     *
     * @param filePath
     * @return
     */
    public static byte[] getBytes(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    // 把Bitmap转换成Base64
    public static String getBitmapStrBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        return Base64.encodeToString(bytes, 0);
    }

    // 把Base64转换成Bitmap
    public static byte[] getBitmapFromBase64(String iconBase64) {
        byte[] bitmapArray = Base64.decode(iconBase64, Base64.DEFAULT);
        return bitmapArray;
    }
    //将bitmip转换成png格式保存到手机
    public static void saveBitmap(Bitmap bitmap,String bitName)
    {   try{
        String path=Environment.getExternalStorageDirectory()+bitName;
        File file = new File(path);
        if(file.exists()){
            file.delete();
        }
        FileOutputStream out;

            out = new FileOutputStream(file);
            if(bitmap.compress(Bitmap.CompressFormat.PNG, 90, out))
            {
                out.flush();
                out.close();
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    //byte数组转换成图片
    public static void byte2image(byte[] data){
        if(data.length<3) return;
        try{
            String fileName = System.currentTimeMillis() + ".png";
            File tempFile = new File("/sdcard/tangerinepoints/image");
            if(!tempFile.exists()){
                tempFile.mkdirs();
            }
            String paths="/sdcard/tangerinepoints/image/"+fileName;
            FileOutputStream imageOutput = new FileOutputStream(paths);
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
            //System.out.println("Make Picture success,Please find image in " + path);
        } catch(Exception ex) {
            //System.out.println("Exception: " + ex);
            ex.printStackTrace();
        }
    }


}
