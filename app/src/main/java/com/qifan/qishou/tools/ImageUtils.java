package com.qifan.qishou.tools;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

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
 * Created by Administrator on 2017/6/2.
 */

public class ImageUtils {
    public static Bitmap getBitmapFromUri(Context context, Uri uri) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    //拍照保存目录
    public static final String fileSavePath= Environment.getExternalStorageDirectory().getPath() + "/yangyu/save/";
    //图片压缩保存目录
    public static final String filePath= Environment.getExternalStorageDirectory().getPath() + "/yangyu/compression/";
    public static final String fileName= Long.toString(System.nanoTime())+".png";
    public static void makeFolder(String path) {
        makeFolder(path,false);
    }
    public static void makeFolder(String path, boolean isHidden) {
        File appDir = new File(path);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        if(isHidden){
            File hiddenDir = new File(path+".nomedia");
            if (!hiddenDir.exists()) {
                hiddenDir.mkdirs();
            }
        }

    }
    public static String saveBitmap(Bitmap bmp, String savePath, String saveName) {

        File appDir = new File(savePath);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        File f = new File(savePath, saveName);
        try {
            FileOutputStream out = new FileOutputStream(f);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return savePath+saveName;
    }
    public static String saveImage(Bitmap bmp) {
//        File appDir = new File(Environment.getExternalStorageDirectory(), "/haitao/compression/");
        String imgName= Long.toString(System.nanoTime())+".png";
        File appDir = new File(filePath);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        File file = new File(appDir, imgName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath+imgName;
    }

    public static void saveFile(File file, String path, String fileName) {
        try {
            InputStream input = new FileInputStream(file);
            saveFile(input,path,fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void saveFile(InputStream inputStream, String path, String fileName) {
        File tempFile = new File(path);
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }
        OutputStream os = null;
        try {
            // 2、保存到临时文件
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流保存到本地文件
//            os = new FileOutputStream(tempFile.getPath() + File.separator + fileName);
            os = new FileOutputStream(path + fileName);
            // 开始读取
            while ((len = inputStream.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 完毕，关闭所有链接
            try {
                os.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param fileName
     * @return 返回k 不是kb
     */
    public static long getFileSize(String fileName) {
        long lgth = 0;
        File file = new File(fileName);
        if (file.exists()) {
            lgth = file.length();
        }
        file = null;
        return lgth;
    }

    /**
     * 压缩大于700KB的图片
     * @param
     * @return
     */
    public static InputStream Bitmap2InputStream(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }
    public static Bitmap getSmallBitmap2(Bitmap bitmap ) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, bos);
        InputStream isBm = new ByteArrayInputStream(bos.toByteArray());
        //图片较大,压缩图片
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(isBm,null,options);
        options.inSampleSize = 4;
        options.inJustDecodeBounds = false;
        Bitmap bm = BitmapFactory.decodeStream(isBm,null, options);
        if(bm == null){
            return  null;
        }

        return bm ;
    }
    public static String getSmallBitmap(String imgPath, String savePath, String fileName) {
        long fileSize = getFileSize(imgPath);
        //图片较小,就不压缩
        if(fileSize<(700*1024)){
            return imgPath;
        }
        //图片较大,压缩图片
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgPath, options);
        options.inSampleSize = 4;
        options.inJustDecodeBounds = false;
        Bitmap bm = BitmapFactory.decodeFile(imgPath, options);
        if(bm == null){
            return  null;
        }
        //针对三星手机图片角度旋转问题
        int degree = getBitmapDegree(imgPath);
        if (degree!=0){
            bm = toTurn(bm, degree);
        }
        ByteArrayOutputStream baos = null ;
        try{
            baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 70, baos);//50
        }finally{
            try {
                if(baos != null)
                    baos.close() ;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        String filePath=Environment.getExternalStorageDirectory().getPath() + "/vocinno/compression/";
//        String imgName= Long.toString(System.nanoTime())+".png";
        try {
            File hiddenDir = new File(savePath+".nomedia");
            if (!hiddenDir.exists()) {
                hiddenDir.mkdirs();
            }

            File f = new File(savePath,fileName);
            File file = new File(savePath);
            if(!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 70, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            return imgPath;
        }
        return savePath+fileName ;
    }
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
            return degree;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static Bitmap toTurn(Bitmap img, int degree){
        Matrix matrix = new Matrix();
        matrix.postRotate(degree); /*翻转90度*/
        int width = img.getWidth();
        int height =img.getHeight();
        img = Bitmap.createBitmap(img, 0, 0, width, height, matrix, true);
        return img;
    }


    public static Bitmap getBitmapFormUri2(Activity ac, Uri uri) throws IOException {
        InputStream input = ac.getContentResolver().openInputStream(uri);
        /*BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input.close();

        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;
        //图片分辨率以480x800为标准
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (originalHeight / hh);
        }
        if (be <= 0)
            be = 1;
        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = 4;//设置缩放比例
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional*/
        input = ac.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input);
        input.close();

        return compressImage(bitmap);//再进行质量压缩
    }

    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

 public static Bitmap get(Activity activity, Uri originalUri){
     byte[] mContent= new byte[0];
     try {
         mContent = readStream(activity.getContentResolver().openInputStream(Uri.parse(originalUri.toString())));
     } catch (Exception e) {
         e.printStackTrace();
     }
     return  getPicFromBytes(mContent, null);
 }
    public static Bitmap getPicFromBytes(byte[] bytes, BitmapFactory.Options opts) {
        if (bytes != null)
            if (opts != null)
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,opts);
            else
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return null;
    }
    public static byte[] readStream(InputStream inStream) throws Exception {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();
        return data;

    }
    public static File uri2File(Activity activity, Uri uri) {
        File file = null;
        try {

            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor actualimagecursor = activity.managedQuery(uri, proj, null,
                    null, null);
            int actual_image_column_index = actualimagecursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualimagecursor.moveToFirst();
            String img_path = actualimagecursor
                    .getString(actual_image_column_index);
            file = new File(img_path);
        } catch ( Exception e) {
//            file = new File(new URI(uri.toString()));
        }
        return file;
    }


    public static String getRealPathFromURI(Activity activity, Uri contentUri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = activity.getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
    public static String getRealFilePath(final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}
