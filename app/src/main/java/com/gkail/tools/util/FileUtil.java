package com.gkail.tools.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.text.format.Formatter;

import com.gkail.tools.MainApplication;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by weiwenbin on 16/6/23.
 */
public class FileUtil {
    public static final String LOG = "log";//日志
    public static final String CAPTURE = "capture";//照片
    public static final String UPDATE = "update";
    public static final String AUDIOTO = "audioTo";//呼喊发送
    public static final String AUDIOFrom = "audioFrom";//呼喊接收

    private static final String directory = "gongkai";
    private static final File FilesDir = MainApplication.getContext().getFilesDir();
    private static File ExternalFilesDir = MainApplication.getContext().getExternalFilesDir(null);
    private static final File ExternalStorageDirectory = new File(Environment.getExternalStorageDirectory(), directory);

    static {
        if (isSDCardEnable() && !ExternalStorageDirectory.exists()) {
            ExternalStorageDirectory.mkdir();
        }
    }


    /**
     * new了文件或文件夹，不一定存在,先sd卡根目录，无权限则外置私有目录，无sd卡则内置私有目录
     *
     * @param dirName  可以为null
     * @param fileName 为空表示返回目录
     * @return
     */
    public static File getFileOrDir(String dirName, String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return getDir(dirName, 1);
        }
        return new File(getDir(dirName, 1), fileName);
    }

    /**
     * 只是new了文件，不一定存在
     *
     * @param dirName  可以为null
     * @param fileName 为空表示返回目录
     * @param modeType 1,sd卡文件夹//storage/emulated/kidcares/，卸载后不删除
     *                 2,私有外置文件夹//storage/emulated/Android/data/com.viapalm/files/ 卸载后删除，
     *                 3,私有内置文件夹data/com.viapalm/files/ 卸载后删除
     * @return
     */
    public static File getFileOrDir(String dirName, String fileName, int modeType) {
        if (TextUtils.isEmpty(fileName)) {
            return getDir(dirName, modeType);
        }
        return new File(getDir(dirName, modeType), fileName);
    }

    public static boolean renameTo(File file, String newName) {
        if (file == null || !file.exists()) return false;
        return file.renameTo(new File(getDir(null, 2), newName));
    }


    /**
     * 判断SDCard根目录是否可用
     *
     * @return boolean
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED) && Environment.getExternalStorageDirectory().canWrite();

    }

    /**
     * 判断SDCard是否存在
     *
     * @return boolean
     */
    public static boolean isSDCardExist() {
        boolean isSDCardExist = false;
        try {
            isSDCardExist = Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED);
        } catch (NullPointerException e) {
        }
        return isSDCardExist;

    }

    /**
     * 获得机身内存总大小
     *
     * @return
     */
    public static String getRomTotalSize(Context context) {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return Formatter.formatFileSize(context, blockSize * totalBlocks);
    }

    /**
     * 递归删除文件和文件夹
     *
     * @param file 要删除的根目录
     */
    public static void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    deleteFile(f);
                }
                file.delete();
            }
        }
    }

    public static String saveBitmap(Bitmap bm, String filename) {
        FileOutputStream out = null;
        try {
            if (bm == null) {
                return null;
            }
            File dir = getDir(null, 2);
            File file = new File(dir, filename);
            out = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(out);
            bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            out.flush();
            out.close();
            bm.recycle();
            bm.recycle();
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bm != null) {
                bm.recycle();
            }
        }
        return null;
    }

    /**
     * @param dirName  ，建立的目录名,可以为null
     * @param modeType 1,sd卡文件夹//storage/emulated/kidcares/，卸载后不删除
     *                 2,私有外置文件夹//storage/emulated/Android/data/com.viapalm/files/ 卸载后删除，
     *                 3,私有内置文件夹data/com.viapalm/files/ 卸载后删除
     * @return dirName目录
     */
    private static File getDir(String dirName, int modeType) {
        int support = getModel();
        File dir;
        if (modeType > support) {
            if (modeType == 3 || !isSDCardExist()) {
                dir = TextUtils.isEmpty(dirName) ? FilesDir : new File(FilesDir, dirName);
            } else {
                dir = TextUtils.isEmpty(dirName) ? ExternalFilesDir : new File(ExternalFilesDir, dirName);
            }
        } else {
            if (support == 1) {
                dir = TextUtils.isEmpty(dirName) ? ExternalStorageDirectory : new File(ExternalStorageDirectory, dirName);
            } else if (getModel() == 2) {
                dir = TextUtils.isEmpty(dirName) ? ExternalFilesDir : new File(ExternalFilesDir, dirName);
            } else {
                dir = TextUtils.isEmpty(dirName) ? FilesDir : new File(FilesDir, dirName);
            }
        }

        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * * @return
     * 1,sd卡文件夹//storage/emulated/kidcares/，卸载后不删除
     * 2,私有外置文件夹//storage/emulated/Android/data/com.viapalm/files/ 卸载后删除，
     * 3,私有内置文件夹data/com.viapalm/files/ 卸载后删除
     **/
    private static int getModel() {
        if (isSDCardEnable() && ExternalStorageDirectory.exists()) {
            return 1;
        }
        if (ExternalFilesDir == null) {
            ExternalFilesDir = MainApplication.getContext().getExternalFilesDir(null);
        }
        if (isSDCardExist() && ExternalFilesDir != null && ExternalFilesDir.exists()) {
            return 2;
        }
        return 3;
    }


}
