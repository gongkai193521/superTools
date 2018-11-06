package com.gkail.tools.util;

import android.text.TextUtils;
import android.util.Log;


import com.gkail.tools.BuildConfig;
import com.gkail.tools.MainApplication;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 日志工具类
 *
 * @author panjichang
 */
public class LogUtils {
    /**
     * 本地log文件保留大小设置
     * 生产环境为1000000，即1M
     */
    private final static long LOG_MAX_SIZE = 1000 * 1000;
    private final static long DEBUG_KEEP_TIME = 20 * 60 * 1000;

    private final static String LOG_FLAG = "logkc";
    private final static String TYPE_DEBUG = "debug";
    private final static String TYPE_RELEASE = "release";


    private static StringBuilder cacheLog = new StringBuilder(60 * 1000);

    public static String getCacheLog() {
        synchronized (cacheLog) {
            String str_cache = cacheLog.toString();
            cacheLog.setLength(0);
            return str_cache;
        }
    }


    /**
     * 格式：logkc:msg
     */
    public static void i(String msg) {// 调试信息
        Log.i(LOG_FLAG, msg);
    }

    /**
     * 格式：logkc-tag:msg
     */
    public static void i(String tag, String msg) {// 调试信息
        Log.i(LOG_FLAG + "-" + tag, msg);
    }

    /**
     * 格式：logkc:msg
     */
    public static void d(String msg) {// 调试信息
        if (!BuildConfig.DEBUG) {
            Log.d(LOG_FLAG, msg);
        }
    }

    public static void d(Object msg) {// 调试信息
        d(msg.toString());
    }

    /**
     * 格式：logkc-tag:msg
     */
    public static void d(String tag, String msg) {// 调试信息
        if (BuildConfig.DEBUG) {
            Log.d(LOG_FLAG + "-" + tag, msg);
        }
    }


    /**
     * 格式：time+类名＋方法名＋msg
     */
    public static void f(String msg) {// 调试信息
        Log.i(LOG_FLAG, msg);
        StackTraceElement[] trace = new Throwable().getStackTrace();
        writeLogtoFile(trace, msg);
    }

    /**
     * 格式：time+类名＋方法名＋msg
     */
    public static void f(String tage, String msg) {// 调试信息
        f(tage + msg);
    }

    /**
     * 日志的输出格式
     */
    private static SimpleDateFormat myLogSdf = new SimpleDateFormat(
            "MM-dd HH:mm:ss", Locale.getDefault());

    private static File logFile;

    static {
        logFile = FileUtil.getFileOrDir(FileUtil.LOG, MainApplication.getContext().getPackageName() + ".txt");
    }

    private static StringBuilder buffer = new StringBuilder();
    private static long writeLogTime = 0;
    /**
     * 最长等待写入文件的时间
     */
    private final static int TIME_WAIT_WRITE_TO_FILE = 60000;
    private static boolean spaceCanWrite = true;
    /**
     * 缓存1k
     */
    private static final int MAX_LOG_SIZE = 2000;
    private static final int MIN_AVAILABLE_SIZE = MAX_LOG_SIZE * 100;

    /**
     * 打开日志文件并写入日志
     **/
    private synchronized static void writeLogtoFile(StackTraceElement[] trace, String msg) {// 新建或打开日志文件
//        if (!spaceCanWrite) {
//            if (DeviceUtils.getRomAvailable(MainApplication.getContext()) >= MIN_AVAILABLE_SIZE) {
//                spaceCanWrite = true;
//            } else {
//                return;
//            }
//        }
        String tagKey = "unKnow";
        if (trace != null && !TextUtils.isEmpty(trace[1].getFileName())) {
            tagKey = trace[1].getFileName().replace(".java", "") + "-" + trace[1].getMethodName();
        }
        //大于1k的日志不再写入文件
        msg = msg.length() > MAX_LOG_SIZE ? msg.substring(0, MAX_LOG_SIZE) : msg;


        String needWriteMessage =
                myLogSdf.format(new Date()) + " " + tagKey + ":" + msg + "\r\n";

        buffer.append(needWriteMessage);
        if (System.currentTimeMillis() - writeLogTime < TIME_WAIT_WRITE_TO_FILE && buffer.length() < MAX_LOG_SIZE) {
            return;
        }
        writeLogTime = System.currentTimeMillis();

        if (logFile.exists()) {
            if (logFile.length() > LOG_MAX_SIZE) {
                File old = FileUtil.getFileOrDir(FileUtil.LOG, "bkb_" + logFile.getName());
                if (old.exists()) {
                    boolean delete = old.delete();
                    Log.i(LOG_FLAG, "old.delete()成功？" + delete);
                }
                boolean b = logFile.renameTo(old);
                Log.i(LOG_FLAG, "logFile.renameTo(old)成功？" + b);
            }
        } else {
            logFile = FileUtil.getFileOrDir(FileUtil.LOG, MainApplication.getContext().getPackageName() + ".txt");
            try {
                boolean newFile = logFile.createNewFile();
                Log.i(LOG_FLAG, "logFile.createNewFile()成功？" + newFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        tofilewriter(logFile, buffer.toString());
        try {
            buffer.delete(0, buffer.length());
        } catch (Exception e) {
            buffer.setLength(0);
        }

    }

    private static final String ENOSPC = "ENOSPC";

    /**
     * 写入文件
     */
    private static void tofilewriter(File file, String needWriteMessage) {
        if (!file.canWrite()) {
            return;
        }
        try {
            //后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
            FileWriter filerWriter = new FileWriter(file, true);
            BufferedWriter bufWriter = new BufferedWriter(filerWriter);
            bufWriter.write(needWriteMessage);
            bufWriter.close();
            filerWriter.close();
        } catch (IOException e) {
            if (e.getMessage().contains(ENOSPC)) {
                spaceCanWrite = false;
            }
            e.printStackTrace();
        }
    }
}
