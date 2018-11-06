package com.gkail.tools.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.gkail.tools.MainApplication;

import java.lang.reflect.Type;

/**
 * SharedPreferences存储数据工具类
 */
public abstract class SharedPreferencesUtils {
    static SharedPreferences sp = MainApplication.getContext().getSharedPreferences("gongkai", Context.MODE_PRIVATE);
    static Editor editor = sp.edit();

    /**
     * 根据key值存储数据
     *
     * @param key
     * @param data
     */
    public static boolean putValue(String key, Object data) {
        synchronized (SharedPreferencesUtils.class) {
            if (MainApplication.getContext() == null) {
                return false;
            }
            if(data == null){
                editor.remove(key).commit();
                return true;
            }
            if (data instanceof Boolean) {
                editor.putBoolean(key, (Boolean) data);
            } else if (data instanceof Float) {
                editor.putFloat(key, (Float) data);
            } else if (data instanceof Integer) {
                editor.putInt(key, (Integer) data);
            } else if (data instanceof Long) {
                editor.putLong(key, (Long) data);
            } else if (data instanceof String) {
                editor.putString(key, (String) data);
            } else {
//                editor.putString(key, new Gson().toJson(data));
            }
            return editor.commit();
        }
    }

    /**
     * 删除我们应用中所有sharePreferance的所有XML文件
     */
    public static void clear() {
        editor.clear().commit();
    }

    //根据key值删除数据
    public static boolean remove(String key) {
        return editor.remove(key).commit();
    }

    //判断是否有此key值
    public static boolean contains(String key) {
        return sp.contains(key);
    }




    /**
     * 根据key值读取数据
     *
     * @param key
     * @param defaultVal
     * @param responseType
     * @param <T>
     * @return
     */
    public static <T> T getValue(String key, T defaultVal,
                                 Class<T> responseType) {
        synchronized (SharedPreferencesUtils.class) {
            Object obj = null;
            if (responseType.equals(Boolean.class)) {
                obj = sp.getBoolean(key, (Boolean) defaultVal);
            } else if (responseType.equals(Float.class)) {
                obj = sp.getFloat(key, (Float) defaultVal);
            } else if (responseType.equals(Integer.class)) {
                obj = sp.getInt(key, (Integer) defaultVal);
            } else if (responseType.equals(Long.class)) {
                obj = sp.getLong(key, (Long) defaultVal);
            } else if (responseType.equals(String.class)) {
                obj = sp.getString(key, (String) defaultVal);
            } else if (responseType == null) {
                throw new NullPointerException();
            } else {
                String objectStr = sp.getString(key, null);
                if (!TextUtils.isEmpty(objectStr)) {
                    try {
//                        obj = new Gson().fromJson(objectStr, responseType);
                    }catch (Exception e){
                        e.printStackTrace();
                        remove(key);
                    }
                }

            }
            if (obj == null) {
                return defaultVal;
            }
            return (T) obj;
        }
    }

    /**
     * 亲测 set、list、map均可以使用，且以上只能使用这个方法。
     *
     * @param key
     * @param defaultVal
     * @param type       写法new TypeToken<ArrayList<AA>>(){}.getType()
     * @param <T>
     * @return
     */
    public static <T> T getValues(String key, T defaultVal, Type type) {
        if (TextUtils.isEmpty(key) || type == null) {throw new NullPointerException();}
        String objectStr = sp.getString(key, null);
        if (TextUtils.isEmpty(objectStr)) {
            return defaultVal;
        }
        T obj=defaultVal;
        try {
//            obj = new Gson().fromJson(objectStr, type);
        }catch (Exception e){
            e.printStackTrace();
        }
        return obj;
    }
}