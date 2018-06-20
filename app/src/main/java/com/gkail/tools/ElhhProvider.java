package com.gkail.tools;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class ElhhProvider {
    /**
     * 用内容提供者获取另一个应用的数据
     *
     * @return
     */
    public static String getTenantId(Context context) {
        Uri idUri = Uri.parse("content://com.viapalm.educhildeluhuhang.TenantIdProvider/tenantId");
        String id = null;
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(idUri,
                    new String[]{"id"}, null, null, null);
            if (null != cursor) {
                while (cursor.moveToNext()) {
                    id = cursor.getString(cursor.getColumnIndex("id"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }
        return id;
    }
}
