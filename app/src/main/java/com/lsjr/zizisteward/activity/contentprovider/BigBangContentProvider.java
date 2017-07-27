package com.lsjr.zizisteward.activity.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import static com.lsjr.zizisteward.contrl.ConstantUtil.SEPARATOR;
import static com.lsjr.zizisteward.contrl.ConstantUtil.TYPE_CLEAN;
import static com.lsjr.zizisteward.contrl.ConstantUtil.TYPE_CONTAIN;
import static com.lsjr.zizisteward.contrl.ConstantUtil.VALUE;


public class BigBangContentProvider extends ContentProvider {
    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        // 用这个来取数值
        String[] path= uri.getPath().split(SEPARATOR);
        String type=path[1];
        if (type.equals(TYPE_CLEAN)){
            SPHelperImpl.clear();
            return "";
        }
        String key=path[2];
        if (type.equals(TYPE_CONTAIN)){
            return SPHelperImpl.contains(key)+"";
        }
        return  ""+SPHelperImpl.get(key,type);
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String[] path= uri.getPath().split(SEPARATOR);
        String key=path[2];
        Object obj=  values.get(VALUE);
        if (obj!=null)
            SPHelperImpl.save(key,obj);
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String[] path= uri.getPath().split(SEPARATOR);
        String key=path[2];
        if (SPHelperImpl.contains(key)){
            SPHelperImpl.remove(key);
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
       insert(uri,values);
       return 0;
    }
}