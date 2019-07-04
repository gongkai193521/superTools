package com.gkail.tools.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MyProvider extends ContentProvider {
    private Context mContext;
    DBHelper mDbHelper = null;
    SQLiteDatabase db = null;
    public static final String AUTOHORITY = "com.test.MyProvider";
    // 设置ContentProvider的唯一标识
    public static final int User_Code = 1;

    // UriMatcher类使用:在ContentProvider 中注册URI
    private static final UriMatcher mMatcher;

    static {
        mMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        //初始化 若URI资源路径 = content://com.test.MyProvider/user ，则返回注册码User_Code
        mMatcher.addURI(AUTOHORITY, "user", User_Code);
    }

    /**
     * 初始化ContentProvider
     */
    @Override
    public boolean onCreate() {
        mContext = getContext();
        // 在ContentProvider创建时对数据库进行初始化
        // 运行在主线程，故不能做耗时操作,此处仅作展示
        mDbHelper = new DBHelper(getContext());
        db = mDbHelper.getWritableDatabase();
        return true;
    }

    /**
     * 添加数据
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // 向该表添加数据
        db.insert(DBHelper.USER_TABLE_NAME, null, values);
        // 当该URI的ContentProvider数据发生变化时，通知外界（即访问该ContentProvider数据的访问者）
        mContext.getContentResolver().notifyChange(uri, null);
        return uri;
    }

    /**
     * 查询数据
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // 查询数据
        return db.query(DBHelper.USER_TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder, null);
    }

    /**
     * 更新数据
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // 由于不展示,此处不作展开
        return 0;
    }

    /**
     * 删除数据
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // 由于不展示,此处不作展开
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        // 由于不展示,此处不作展开
        return null;
    }
}